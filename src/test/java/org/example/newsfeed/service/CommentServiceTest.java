package org.example.newsfeed.service;

import org.example.newsfeed.dto.CommentRequestDTO;
import org.example.newsfeed.entity.Comment;
import org.example.newsfeed.entity.Post;
import org.example.newsfeed.entity.User;
import org.example.newsfeed.exception.CommentNotFoundException;
import org.example.newsfeed.exception.InvalidUserException;
import org.example.newsfeed.exception.PostNotFoundException;
import org.example.newsfeed.repository.CommentRepository;
import org.example.newsfeed.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private CommentService commentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateComment() {
        // given
        Long postId = 1L;
        CommentRequestDTO dto = new CommentRequestDTO();
        dto.setContent("New comment content");
        User user = new User();
        user.setId(1L);

        Post post = new Post();
        post.setId(postId);

        when(postRepository.findByIdAndDeleted(postId, Boolean.FALSE)).thenReturn(Optional.of(post));
        when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        Comment createdComment = commentService.createComment(postId, dto, user);

        // then
        assertNotNull(createdComment);
        assertEquals(dto.getContent(), createdComment.getContent());
        assertEquals(user, createdComment.getUser());
        assertEquals(post, createdComment.getPost());
    }

    @Test
    public void testGetComments() {
        // given
        Long postId = 1L;
        List<Comment> comments = new ArrayList<>();
        comments.add(new Comment());
        when(postRepository.findByIdAndDeleted(postId, Boolean.FALSE)).thenReturn(Optional.of(new Post()));
        when(commentRepository.findAllByPostId(postId)).thenReturn(comments);

        // when
        List<Comment> retrievedComments = commentService.getComments(postId);

        // then
        assertNotNull(retrievedComments);
        assertEquals(comments.size(), retrievedComments.size());
    }

    @Test
    public void testGetCommentsPostNotFound() {
        // given
        Long postId = 1L;
        when(postRepository.findByIdAndDeleted(postId, Boolean.FALSE)).thenReturn(Optional.empty());

        // when & then
        assertThrows(PostNotFoundException.class, () -> commentService.getComments(postId));
    }

    @Test
    public void testUpdateComment() {
        // given
        Long commentId = 1L;
        CommentRequestDTO dto = new CommentRequestDTO();
        dto.setContent("Updated comment content");
        User user = new User();
        user.setId(1L);

        Comment existingComment = new Comment();
        existingComment.setId(commentId);
        existingComment.setUser(user);
        existingComment.setPost(new Post());

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(existingComment));
        when(postRepository.findByIdAndDeleted(anyLong(), eq(Boolean.FALSE))).thenReturn(Optional.of(new Post()));
        when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        Comment updatedComment = commentService.updateComment(commentId, dto, user);

        // then
        assertEquals(dto.getContent(), updatedComment.getContent());
    }

    @Test
    public void testUpdateCommentCommentNotFound() {
        // given
        Long commentId = 1L;
        CommentRequestDTO dto = new CommentRequestDTO();
        dto.setContent("Updated comment content");
        User user = new User();
        user.setId(1L);

        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(CommentNotFoundException.class, () -> commentService.updateComment(commentId, dto, user));
    }

    @Test
    public void testUpdateCommentPostNotFound() {
        // given
        Long commentId = 1L;
        CommentRequestDTO dto = new CommentRequestDTO();
        dto.setContent("Updated comment content");
        User user = new User();
        user.setId(1L);

        Comment existingComment = new Comment();
        existingComment.setId(commentId);
        existingComment.setUser(user);
        existingComment.setPost(new Post());

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(existingComment));
        when(postRepository.findByIdAndDeleted(anyLong(), eq(Boolean.FALSE))).thenReturn(Optional.empty());

        // when & then
        assertThrows(PostNotFoundException.class, () -> commentService.updateComment(commentId, dto, user));
    }

    @Test
    public void testUpdateCommentInvalidUser() {
        // given
        Long commentId = 1L;
        CommentRequestDTO dto = new CommentRequestDTO();
        dto.setContent("댓글 수정 내용");
        User user = new User();
        user.setId(1L);

        Comment existingComment = new Comment();
        existingComment.setId(commentId);
        existingComment.setUser(new User()); // 다른 사용자인 경우를 시뮬레이션

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(existingComment));

        // when & then
        assertThrows(InvalidUserException.class, () -> commentService.updateComment(commentId, dto, user));
    }


    @Test
    public void testDeleteComment() {
        // given
        Long commentId = 1L;
        User user = new User();
        user.setId(1L);

        Comment existingComment = new Comment();
        existingComment.setId(commentId);
        existingComment.setUser(new User()); // 다른 사용자인 경우를 시뮬레이션

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(existingComment));

        // when & then
        assertThrows(InvalidUserException.class, () -> commentService.deleteComment(commentId, user));
    }


    @Test
    public void testDeleteCommentCommentNotFound() {
        // given
        Long commentId = 1L;
        User user = new User();
        user.setId(1L);

        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(CommentNotFoundException.class, () -> commentService.deleteComment(commentId, user));
    }

    @Test
    public void testDeleteCommentInvalidUser() {
        // given
        Long commentId = 1L;
        User ownerUser = new User();
        ownerUser.setId(1L);

        User anotherUser = new User();
        anotherUser.setId(2L);

        Comment existingComment = new Comment();
        existingComment.setId(commentId);
        existingComment.setUser(ownerUser);
        existingComment.setPost(new Post());

        // mock repository methods
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(existingComment));
        when(postRepository.findByIdAndDeleted(anyLong(), eq(Boolean.FALSE))).thenReturn(Optional.of(new Post()));

//        // when & then
//        assertThrows(InvalidUserException.class, () -> commentService.deleteComment(commentId, anotherUser));
//    }


}
