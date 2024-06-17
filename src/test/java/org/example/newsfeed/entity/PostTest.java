package org.example.newsfeed.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PostTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = mock(User.class);
    }

    @Test
    public void testPostBuilder() {
        // given
        String content = "This is a post content";

        // when
        Post post = Post.builder()
            .user(user)
            .content(content)
            .build();

        // then
        assertEquals(user, post.getUser());
        assertEquals(content, post.getContent());
        assertTrue(post.getComments().isEmpty());
        assertTrue(!post.isDeleted());
    }

    @Test
    public void testSetDeleted() {
        // given
        Post post = new Post(user, "Post content");

        // when
        post.setDeleted();

        // then
        assertTrue(post.isDeleted());
    }

    @Test
    public void testSetContent() {
        // given
        Post post = new Post(user, "Initial content");
        String newContent = "Updated content";

        // when
        post.setContent(newContent);

        // then
        assertEquals(newContent, post.getContent());
    }

    @Test
    public void testAddComment() {
        // given
        Post post = new Post(user, "Post content");
        Comment comment = new Comment(user, "This is a comment");

        // when
        post.getComments().add(comment);
        comment.setPost(post);

        // then
        List<Comment> comments = new ArrayList<>();
        comments.add(comment);
        assertEquals(comments, post.getComments());
    }
}
