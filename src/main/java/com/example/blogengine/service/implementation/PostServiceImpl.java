package com.example.blogengine.service.implementation;

import com.example.blogengine.api.request.ModeratorRequest;
import com.example.blogengine.api.request.PostRequest;
import com.example.blogengine.api.response.ErrorResponse;
import com.example.blogengine.api.response.ResultResponse;
import com.example.blogengine.api.response.posts.*;
import com.example.blogengine.exception.*;
import com.example.blogengine.model.*;
import com.example.blogengine.model.enumerated.ModerationStatus;
import com.example.blogengine.repository.*;
import com.example.blogengine.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.*;


@Component
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostVotesRepository postVotesRepository;
    private final Tag2PostRepository tag2PostRepository;
    private final TagRepository tagRepository;
    private final GlobalSettingsRepository globalSettingsRepository;

    public PostsResponse getPosts(int offset, int limit, String mode) {
        Pageable pageable = PageRequest.of(offset/limit, limit);
        Page<Post> postPage;
        switch (mode) {
            case "popular": {
                postPage = postRepository.findAllPostsByCommentsDesc(pageable);
                break;
            }
            case "best": {
                postPage = postRepository.findAllPostsByVotesDesc(pageable);
                break;
            }
            case "early": {
                postPage = postRepository.findAllPostsByTime(pageable);
                break;
            }
            default: {
                postPage = postRepository.findAllPostsByTimeDesc(pageable);
                break;
            }
        }
        return createPostResponse(postPage, postRepository.findAllPosts().size());
    }

    public PostsResponse getPostsSearch(int offset, int limit, String query) {
        Pageable pageable = PageRequest.of(offset, limit);
        Page<Post> pageOfTags;
        if (query.trim().equals("")) {
            pageOfTags = postRepository.findAllPostsByTimeDesc(pageable);
        } else {
            pageOfTags = postRepository.findAllPostsBySearch(query, pageable);
        }
        return createPostResponse(pageOfTags, (int) pageOfTags.getTotalElements());
    }

    public PostsResponse getPostsByDate(int offset, int limit, String date) {
        Pageable pageable = PageRequest.of(offset, limit);
        Page<Post> postPage = postRepository.findAllPostsByDate(date, pageable);
        return createPostResponse(postPage, (int) postPage.getTotalElements());
    }

    public PostsResponse getPostsByTag(int offset, int limit, String tag) {
        Pageable pageable = PageRequest.of(offset, limit);
        Page<Post> postPage = postRepository.findAllPostsByTag(tag, pageable);
        return createPostResponse(postPage, (int) postPage.getTotalElements());
    }

    public PostResponse getPostsById(int id, Principal principal) throws UsernameNotFoundException, PostNotFoundException {
        Post post = postRepository.findPostById(id)
                .orElseThrow(() -> new PostNotFoundException("Поста с данным id не существует"));
        if (principal == null) {
            post.setViewCount(post.getViewCount() + 1);
            postRepository.save(post);
        } else {
            User user = userRepository.findByEmail(principal.getName())
                    .orElseThrow(() -> new UsernameNotFoundException("user not found"));
            if (!post.getUser().getEmail().equals(user.getEmail())) {
                post.setViewCount(post.getViewCount() + 1);
                postRepository.updateViewCount(post.getId(), post.getViewCount());
            }
        }
        List<PostComment> commentsList = commentRepository.findPostCommentsById(id);
        List<String> tagList = new ArrayList<>();
        post.getTags().forEach(tag -> tagList.add(tag.getName()));
        List<PostCommentResponse> postCommentResponseList = new ArrayList<>();
        for (PostComment c : commentsList) {
            postCommentResponseList.add(new PostCommentResponse()
                    .setId(c.getId())
                    .setTimestamp(c.getTime().getTime() / 1000)
                    .setText(c.getText())
                    .setUser(new UserPostResponse().setId(c.getUser().getId()).setName(c.getUser().getName()))
            );
        }
        return getPostResponse(postCommentResponseList, post, tagList);
    }

    public PostsResponse getPostsMy(int offset, int limit, String status, Principal principal) throws StatusNotFoundException {
        Pageable pageable;
        pageable = PageRequest.of(offset, limit);
        Page<Post> posts;
        switch (status) {
            case "inactive": {
                posts = postRepository.findPostsMyInactive(pageable, principal.getName());
                break;
            }
            case "pending": {
                posts = postRepository.findPostsMyIsActive("NEW", principal.getName(), pageable);
                break;
            }
            case "declined": {
                posts = postRepository.findPostsMyIsActive("DECLINED", principal.getName(), pageable);
                break;
            }
            default: {
                posts = postRepository.findPostsMyIsActive("ACCEPTED", principal.getName(), pageable);
                break;
            }
        }
        return createPostResponse(posts, (int) posts.getTotalElements());
    }

    public PostsResponse getModeratePost(int offset, int limit, String status, Principal principal) throws StatusNotFoundException {
        Pageable pageable = PageRequest.of(offset, limit);
        User moder = userRepository.findByEmail(principal.getName()).orElseThrow();
        switch (status) {
            case "new": {
                Page<Post> posts = postRepository.findPostsByModerate(ModerationStatus.NEW, pageable);
                return createPostResponse(posts, (int) posts.getTotalElements());
            }
            case "declined": {
                Page<Post> posts = postRepository.findPostsMyModerate(ModerationStatus.DECLINED, moder.getId(), pageable);
                return createPostResponse(posts, (int) posts.getTotalElements());
            }
            case "accepted": {
                Page<Post> posts = postRepository.findPostsMyModerate(ModerationStatus.ACCEPTED, moder.getId(), pageable);
                return createPostResponse(posts, (int) posts.getTotalElements());
            }
        }
        throw new StatusNotFoundException("статус не найден");
    }

    public ResultResponse createPost(PostRequest postRequest, Principal principal) {
        User user = userRepository.findByEmail(principal.getName()).orElseThrow();
        if (postRequest.getTitle().length() < 3 || postRequest.getText().length() < 50) {
            return new ResultResponse()
                    .setResult(false)
                    .setErrors(new ErrorResponse()
                            .setTitle("Заголовок не установлен")
                            .setText("Текст публикации слишком короткий"));
        }
        Date date = setDatePost(postRequest.getTimestamp());
        Post post = new Post()
                .setText(postRequest.getText())
                .setTitle(postRequest.getTitle())
                .setIsActive(postRequest.getActive())
                .setTime(date)
                .setUser(user);
        if (globalSettingsRepository.findAllGlobalSettings("POST_PREMODERATION").getValue().equals("YES")
                && user.getIsModerator() == 0) {
            post.setModerationStatus(ModerationStatus.NEW);
        }
        if (globalSettingsRepository.findAllGlobalSettings("POST_PREMODERATION").getValue().equals("NO")
                && postRequest.getActive() == 1) {
            post.setModerationStatus(ModerationStatus.ACCEPTED);
        }
        Post sPost = postRepository.save(post);
        setTagsForPost(sPost, postRequest.getTags());
        postRepository.save(sPost);
        return new ResultResponse().setResult(true);
    }

    public ResultResponse putPostsById(int id, PostRequest postRequest, Principal principal) throws PostNotFoundException, AuthorAndUserNoEqualsException {
        if (postRequest.getTitle().length() < 3 || postRequest.getText().length() < 50) {
            return new ResultResponse()
                    .setResult(false)
                    .setErrors(new ErrorResponse()
                            .setTitle("Заголовок не установлен")
                            .setText("Текст публикации слишком короткий"));
        }
        User user = userRepository.findByEmail(principal.getName()).orElseThrow();
        Post post = postRepository.findPostById(id).orElseThrow(() -> new PostNotFoundException("Поста с данным id не существует"));
        if (user.getId() == post.getUser().getId() || user.getIsModerator() == 1) {
            Date date = setDatePost(postRequest.getTimestamp());
            post
                    .setIsActive(postRequest.getActive())
                    .setTitle(postRequest.getTitle())
                    .setText(postRequest.getText());
            if (user.getId() == post.getUser().getId()) {
                post.setTime(date);
            }
            if (user.getIsModerator() == 1) {
                if (post.getModerationStatus().equals(ModerationStatus.NEW)) {
                    post.setModerationStatus(ModerationStatus.NEW);
                } else if (post.getModerationStatus().equals(ModerationStatus.DECLINED)) {
                    post.setModerationStatus(ModerationStatus.DECLINED);
                } else {
                    post.setModerationStatus(ModerationStatus.ACCEPTED);
                }
            }
            Post sPost = postRepository.save(post);
            setTagsForPost(sPost, postRequest.getTags());
            postRepository.save(sPost);
        } else throw new AuthorAndUserNoEqualsException("пользователь не может изменить чужую статью");
        return new ResultResponse().setResult(true);
    }

    public ResultResponse postModeratePost(ModeratorRequest moderatorRequest, Principal principal) throws PostNotFoundException, ModeratorNotFoundException {
        User moderator = userRepository.findByEmail(principal.getName()).orElseThrow(() -> new ModeratorNotFoundException(""));
        Post post = postRepository.findPostById(moderatorRequest.getPostId())
                .orElseThrow(() -> new PostNotFoundException("Поста с данным id не существует"));
        switch (moderatorRequest.getDecision()) {
            case "accept": {
                post.setModerationStatus(ModerationStatus.ACCEPTED);
                break;
            }
            case "decline": {
                post.setModerationStatus(ModerationStatus.DECLINED);
                break;
            }
        }
        post.setModerator(moderator);
        postRepository.save(post);
        return new ResultResponse().setResult(true);
    }

    private void setTagsForPost(Post post, List<String> tags) {
        List<Tag2Post> tags2Post = tag2PostRepository.findTagsByPost(post.getId());
        List<String> tagList = new ArrayList<>(tags);
        Set<String> setTag = new HashSet<>(tagList);
        if (!tags2Post.isEmpty() && tagList.isEmpty()) {
            for (Tag2Post t2p : tags2Post) {
                tag2PostRepository.deleteById(t2p.getId());
            }
        }
        if (!tags2Post.isEmpty() && !tagList.isEmpty()) {
            for (Tag2Post t2p : tags2Post) {
                tag2PostRepository.deleteById(t2p.getId());
            }
            for (String t : setTag) {
                Optional<Tag> byTag = tagRepository.findByTag(t);
                if (byTag.isPresent()) {
                    Tag2Post tag2Post = new Tag2Post()
                            .setPost_id(post.getId())
                            .setTag_id(byTag.get().getId());
                    tag2PostRepository.save(tag2Post);
                } else {
                    Tag tag = new Tag()
                            .setName(t);
                    tagRepository.save(tag);
                    Tag2Post tag2Post = new Tag2Post()
                            .setPost_id(post.getId())
                            .setTag_id(tag.getId());
                    tag2PostRepository.save(tag2Post);
                }
            }
        }
        if (tags2Post.isEmpty() && !tagList.isEmpty()) {
            for (String t : setTag) {
                Optional<Tag> byTag = tagRepository.findByTag(t);
                if (byTag.isPresent()) {
                    Optional<Tag2Post> tagByPost = tag2PostRepository.findTagByPost(byTag.get().getId(), post.getId());
                    if (tagByPost.isPresent()) continue;
                    Tag2Post tag2Post = new Tag2Post()
                            .setPost_id(post.getId())
                            .setTag_id(byTag.get().getId());
                    tag2PostRepository.save(tag2Post);
                } else {
                    Tag tag = new Tag()
                            .setName(t);
                    tagRepository.save(tag);
                    Tag2Post tag2Post = new Tag2Post()
                            .setPost_id(post.getId())
                            .setTag_id(tag.getId());
                    tag2PostRepository.save(tag2Post);
                }
            }
        }
    }

    private Date setDatePost(long timestamp) {
        Date dateNow = new Date();
        Date datePost = new Date(timestamp);
        if (datePost.before(dateNow)) {
            datePost = dateNow;
        }
        return datePost;
    }

    private PostsResponse createPostResponse(Page<Post> pageTags, int size) {
        List<PostResponseList> postResponseList = new ArrayList<>();
        for (Post post : pageTags) {
            postResponseList.add(getPostResponseList(post));
        }
        return new PostsResponse()
                .setPosts(postResponseList)
                .setCount(size);
    }

    private PostResponseList getPostResponseList(Post post) {
        List<PostVotes> likeAndDislike = postVotesRepository.getAllByPostId(post.getId());
        return new PostResponseList()
                .setActive(post.getIsActive() == 1)
                .setId(post.getId())
                .setTimestamp(post.getTime().getTime() / 1000)
                .setUser(new UserPostResponse().setName(post.getUser().getName()).setId(post.getUser().getId()))
                .setTitle(post.getTitle())
                .setAnnounce(setAnnounce(post))
                .setLikeCount(getLikeCount(likeAndDislike))
                .setDislikeCount(getDislikeCount(likeAndDislike))
                .setViewCount(post.getViewCount())
                .setCommentCount(setCommentCount(post));
    }

    private PostResponse getPostResponse(List<PostCommentResponse> postCommentResponseList, Post post, List<String> tagList) {
        List<PostVotes> likeAndDislike = postVotesRepository.getAllByPostId(post.getId());
        return new PostResponse()
                .setId(post.getId())
                .setTimestamp(new Date().getTime() / 1000)
                .setActive(post.getIsActive() == 1)
                .setUser(new UserPostResponse().setName(post.getUser().getName()).setId(post.getUser().getId()))
                .setTitle(post.getTitle())
                .setText(post.getText())
                .setTags(tagList)
                .setComments(postCommentResponseList)
                .setDislikeCount(getDislikeCount(likeAndDislike))
                .setLikeCount(getLikeCount(likeAndDislike))
                .setViewCount(post.getViewCount());
    }

    private long setCommentCount(Post post) {
        int commentCount;
        if (!(post.getComments() == null)) {
            commentCount = post.getComments().size();
        } else {
            commentCount = 0;
        }
        return commentCount;
    }

    public long getDislikeCount(List<PostVotes> postVotes) {
        long dislikeCount = 0;
        if (postVotes.size() != 0) {
            LinkedList<PostVotes> like = new LinkedList<>(postVotes);
            for (PostVotes l : like) {
                if (l.getValue() == -1) {
                    dislikeCount++;
                }
            }
        }
        return dislikeCount;
    }

    private String setAnnounce(Post post) {
        String announce = post.getText()
                .replaceAll("</div>", " ")
                .replaceAll("\\<.*?\\>", "")
                .replaceAll("&nbsp;", " ");
        if (announce.length() > 400) {
            return announce.substring(0, 400) + "...";
        }
        return announce;
    }

    public long getLikeCount(List<PostVotes> postVotes) {
        long likeCount = 0;
        if (postVotes.size() != 0) {
            LinkedList<PostVotes> like = new LinkedList<>(postVotes);
            for (PostVotes l : like) {
                if (l.getValue() == 1) {
                    likeCount++;
                }
            }
        }
        return likeCount;
    }
}

