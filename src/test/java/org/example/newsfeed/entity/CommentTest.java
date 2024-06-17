package org.example.newsfeed.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CommentTest {

    private User user;
    private Post post;

    @BeforeEach
    void setUp() {
        user = mock(User.class);
        post = mock(Post.class);
    }

    @Test
    public void testCommentBuilder() {
        // given
        String content = "This is a comment";

        // when
        Comment comment = Comment.builder()
            .user(user)
            .content(content)
            .build();

        // then
        assertEquals(user, comment.getUser());
        assertEquals(content, comment.getContent());
        assertNull(comment.getPost());
    }

    @Test
    public void testSetPost() {
        // given
        Comment comment = new Comment(user, "Initial content");

        // when
        comment.setPost(post);

        // then
        assertEquals(post, comment.getPost());
    }

    @Test
    public void testSetContent() {
        // given
        Comment comment = new Comment(user, "Initial content");
        String newContent = "Updated content";

        // when
        comment.setContent(newContent);

        // then
        assertEquals(newContent, comment.getContent());
    }

    @Test
    public void testExceptionScenario() {
        // given
        Comment comment = new Comment(user, null);

        // when
        Exception exception = null;
        try {
            comment.setContent(null);
        } catch (Exception e) {
            exception = e;
        }

        // then
        assertNull(exception, "Exception should not be thrown when setting content to null");
    }
}
