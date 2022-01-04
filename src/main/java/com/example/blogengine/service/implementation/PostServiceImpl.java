package com.example.blogengine.service.implementation;

import com.example.blogengine.api.request.ModeratorRequest;
import com.example.blogengine.api.request.PostRequest;
import com.example.blogengine.api.request.PostVoteRequest;
import com.example.blogengine.api.response.ErrorResponse;
import com.example.blogengine.api.response.ResultResponse;
import com.example.blogengine.api.response.posts.*;
import com.example.blogengine.exception.AuthorAndUserNoEqualsException;
import com.example.blogengine.exception.PostNotFoundException;
import com.example.blogengine.exception.StatusNotFoundException;
import com.example.blogengine.exception.UsernameNotFoundException;
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
import java.time.ZoneId;
import java.util.*;


@Component
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostVotesRepository postVotesRepository;
    private final Tag2PostRepository tag2PostRepository;

    public PostsResponse getPosts(int offset, int limit, String mode) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        Page<Post> postPage;
        switch (mode) {
            case "popular" -> postPage = postRepository.findAllPostsByCommentsDesc(pageable);
            case "best" -> postPage = postRepository.findAllPostsByVotesDesc(pageable);
            case "early" -> postPage = postRepository.findAllPostsByTime(pageable);
            default -> postPage = postRepository.findAllPostsByTimeDesc(pageable);
        }
        return createPostResponse(postPage, postRepository.findAllPosts().size());
    }

    public PostsResponse getPostsSearch(int offset, int limit, String query) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        Page<Post> pageOfTags;
        if (query.trim().equals("")) {
            pageOfTags = postRepository.findAllPostsByTimeDesc(pageable);
        } else {
            pageOfTags = postRepository.findAllPostsBySearch(query, pageable);
        }
        return createPostResponse(pageOfTags, (int) pageOfTags.getTotalElements());
    }

    public PostsResponse getPostsByDate(int offset, int limit, String date) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        Page<Post> postPage = postRepository.findAllPostsByDate(date, pageable);
        return createPostResponse(postPage, (int) postPage.getTotalElements());
    }

    public PostsResponse getPostsByTag(int offset, int limit, String tag) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
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
            if (!post.getUser().equals(user) || user.getIsModerator() == 0) {
                post.setViewCount(post.getViewCount() + 1);
                postRepository.save(post);
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
        pageable = PageRequest.of(offset / limit, limit);
        switch (status) {
            case "inactive" -> {
                Page<Post> posts = postRepository.findPostsMyInactive(pageable, principal.getName());
                return createPostResponse(posts, (int) posts.getTotalElements());
            }
            case "pending" -> {
                Page<Post> posts = postRepository.findPostsMyIsActive("NEW", principal.getName(), pageable);
                return createPostResponse(posts, (int) posts.getTotalElements());
            }
            case "declined" -> {
                Page<Post> posts = postRepository.findPostsMyIsActive("DECLINED", principal.getName(), pageable);
                return createPostResponse(posts, (int) posts.getTotalElements());
            }
            case "published" -> {
                Page<Post> posts = postRepository.findPostsMyIsActive("ACCEPTED", principal.getName(), pageable);
                return createPostResponse(posts, (int) posts.getTotalElements());
            }
        }
        throw new StatusNotFoundException("статус не найден");
    }

    public PostsResponse getModeratePost(int offset, int limit, String status, Principal principal) throws StatusNotFoundException {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        User moder = userRepository.findByEmail(principal.getName()).orElseThrow();
        switch (status) {
            case "new" -> {
                Page<Post> posts = postRepository.findPostsByModerate(ModerationStatus.NEW, pageable);
                return createPostResponse(posts, (int) posts.getTotalElements());
            }
            case "declined" -> {
                Page<Post> posts = postRepository.findPostsMyModerate(ModerationStatus.DECLINED, moder.getId(), pageable);
                return createPostResponse(posts, (int) posts.getTotalElements());
            }
            case "accepted" -> {
                Page<Post> posts = postRepository.findPostsMyModerate(ModerationStatus.ACCEPTED, moder.getId(), pageable);
                return createPostResponse(posts, (int) posts.getTotalElements());
            }
        }
        throw new StatusNotFoundException("статус не найден");
    }

    public ResultResponse createPost(PostRequest postRequest, Principal principal) {
        User user = userRepository.findByEmail(principal.getName()).orElseThrow();
        getErrorResponseForResultResponseIfLengthTitleOrTextNotFit(postRequest.getTitle().length(), postRequest.getText().length());

        Date date = setDatePost(postRequest.getTimestamp());
        Post post = new Post()
                .setText(postRequest.getText())
                .setTitle(postRequest.getTitle())
                .setIsActive(postRequest.getActive())
                .setTime(date)
                .setUser(user)
                .setModerationStatus(ModerationStatus.NEW);
        setTagsForPost(post, postRequest.getTags());
        postRepository.save(post);
        return new ResultResponse().setResult(true);
    }

    public ResultResponse putPostsById(int id, PostRequest postRequest, Principal principal) throws PostNotFoundException, AuthorAndUserNoEqualsException {
        getErrorResponseForResultResponseIfLengthTitleOrTextNotFit(postRequest.getTitle().length(), postRequest.getText().length());
        User user = userRepository.findByEmail(principal.getName()).orElseThrow();
        Post post = postRepository.findPostById(id).orElseThrow(() -> new PostNotFoundException("Поста с данным id не существует"));
        if (user.getId() == post.getUser().getId()) {
            Date date = setDatePost(postRequest.getTimestamp());
            post
                    .setTime(date)
                    .setIsActive(postRequest.getActive())
                    .setTitle(postRequest.getTitle())
                    .setText(postRequest.getText());
            setTagsForPost(post, postRequest.getTags());
            postRepository.save(post);
        } else throw new AuthorAndUserNoEqualsException("пользователь не может изменить чужую статью");
        return new ResultResponse().setResult(true);
    }

    public ResultResponse postModeratePost(ModeratorRequest moderatorRequest, Principal principal) throws PostNotFoundException {
        User moderator = userRepository.findByEmail(principal.getName()).orElseThrow();
        Post post = postRepository.findPostById(moderatorRequest.getPostId())
                .orElseThrow(() -> new PostNotFoundException("Поста с данным id не существует"));
        switch (moderatorRequest.getDecision()) {
            case "accept" -> post.setModerationStatus(ModerationStatus.ACCEPTED);
            case "decline" -> post.setModerationStatus(ModerationStatus.DECLINED);
        }
        post.setModerator(moderator);
        postRepository.save(post);
        return new ResultResponse().setResult(true);
    }


    private void getErrorResponseForResultResponseIfLengthTitleOrTextNotFit(int titleLength, int textLength) {
        if (titleLength < 3 || textLength < 50) {
            new ResultResponse()
                    .setResult(false)
                    .setErrors(new ErrorResponse()
                            .setTitle("Заголовок не установлен")
                            .setText("Текст публикации слишком короткий"));
        }
    }

    private void setTagsForPost(Post post, List<String> tags) {
        List<String> tagList = new ArrayList<>(tags);
        for (String t : tagList) {
            Tag tag = new Tag()
                    .setName(t);
            Tag2Post tag2Post = new Tag2Post()
                    .setPost_id(post.getId())
                    .setTag_id(tag.getId());
            tag2PostRepository.save(tag2Post);
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
                .setTimestamp(post.getTime().toInstant().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() / 1000)
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
                .setTimestamp(post.getTime().toInstant().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() / 1000)
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

