package com.example.blogengine.service.implementation;

import com.example.blogengine.api.response.*;
import com.example.blogengine.exception.PostNotFoundException;
import com.example.blogengine.exception.UsernameNotFoundException;
import com.example.blogengine.model.Post;
import com.example.blogengine.model.PostComment;
import com.example.blogengine.model.PostVotes;
import com.example.blogengine.model.User;
import com.example.blogengine.repository.*;
import com.example.blogengine.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


@Component
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final PostVotesRepository postVotesRepository;

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
        List<CommentResponse> commentResponseList = new ArrayList<>();
        for (PostComment c : commentsList) {
            commentResponseList.add(new CommentResponse()
                    .setId(c.getId())
                    .setTimestamp(c.getTime().getTime() / 1000)
                    .setText(c.getText())
                    .setUser(new UserPostResponse().setId(c.getUser().getId()).setName(c.getUser().getName()))
            );
        }
        return getPostResponse(commentResponseList, post, tagList);
    }

    public PostsResponse getPostsMy(int offset, int limit, String status, Principal principal) {
        Pageable pageable;
        pageable = PageRequest.of(offset / limit, limit);
        switch (status) {
            case "inactive" -> {
                Page<Post> posts = postRepository.findPostsMyInactive(pageable, principal.getName());
                return createPostResponse(posts ,(int) posts.getTotalElements());
            }
            case "pending" -> {
                Page<Post> posts = postRepository.findPostsMyIsActive("NEW", principal.getName(), pageable);
                return createPostResponse(posts ,(int) posts.getTotalElements());
            }
            case "declined" -> {
                Page<Post> posts = postRepository.findPostsMyIsActive("DECLINED", principal.getName(), pageable);
                return createPostResponse(posts ,(int) posts.getTotalElements());
            }
            case "published" -> {
                Page<Post> posts = postRepository.findPostsMyIsActive("ACCEPTED", principal.getName(), pageable);
                return createPostResponse(posts ,(int) posts.getTotalElements());
            }
        }
        return null;
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

    private PostResponse getPostResponse(List<CommentResponse> commentResponseList, Post post, List<String> tagList) {
        List<PostVotes> likeAndDislike = postVotesRepository.getAllByPostId(post.getId());
        return new PostResponse()
                .setId(post.getId())
                .setTimestamp(post.getTime().toInstant().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() / 1000)
                .setActive(post.getIsActive() == 1)
                .setUser(new UserPostResponse().setName(post.getUser().getName()).setId(post.getUser().getId()))
                .setTitle(post.getTitle())
                .setText(post.getText())
                .setTags(tagList)
                .setComments(commentResponseList)
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
                if (l.getValue() == 0) {
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

