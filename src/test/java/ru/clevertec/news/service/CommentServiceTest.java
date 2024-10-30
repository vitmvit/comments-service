package ru.clevertec.news.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.news.converter.CommentConverter;
import ru.clevertec.news.exception.EntityNotFoundException;
import ru.clevertec.news.model.entity.Comment;
import ru.clevertec.news.repository.CommentRepository;
import ru.clevertec.news.service.impl.CommentServiceImpl;
import ru.clevertec.news.util.CommentTestBuilder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentConverter commentConverter;

    @InjectMocks
    private CommentServiceImpl commentService;

    @Captor
    private ArgumentCaptor<Comment> argumentCaptor;


    @Test
    void findByIdShouldReturnExpectedCommentWhenFound() {
        var expected = CommentTestBuilder.builder().build().buildComment();
        var commentDto = CommentTestBuilder.builder().build().buildCommentDto();
        var id = expected.getId();

        when(commentRepository.findById(id)).thenReturn(Optional.of(expected));
        when(commentConverter.convert(expected)).thenReturn(commentDto);

        var actual = commentService.findById(id);

        assertThat(actual)
                .hasFieldOrPropertyWithValue(Comment.Fields.id, expected.getId())
                .hasFieldOrPropertyWithValue(Comment.Fields.newsId, expected.getNewsId())
                .hasFieldOrPropertyWithValue(Comment.Fields.username, expected.getUsername())
                .hasFieldOrPropertyWithValue(Comment.Fields.text, expected.getText())
                .hasFieldOrPropertyWithValue(Comment.Fields.time, expected.getTime());
    }

    @Test
    void findByIdShouldThrowEntityNotFoundExceptionWhenCommentNotFound() {
        var exception = assertThrows(Exception.class, () -> commentService.findById(null));
        assertEquals(exception.getClass(), EntityNotFoundException.class);
    }

    @Test
    void createShouldInvokeRepositoryWithoutCommentId() {
        var commentToSave = CommentTestBuilder.builder().withId(null).build().buildComment();
        var expected = CommentTestBuilder.builder().build().buildComment();
        var dto = CommentTestBuilder.builder().build().buildCommentCreateDto();

        doReturn(expected).when(commentRepository).save(commentToSave);
        when(commentConverter.convert(dto)).thenReturn(commentToSave);

        commentService.create(dto);

        verify(commentRepository).save(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue()).hasFieldOrPropertyWithValue(Comment.Fields.id, null);
    }

    @Test
    void updateShouldCallsMergeAndSaveWhenCommentFound() {
        var id = CommentTestBuilder.builder().build().getId();
        var dto = CommentTestBuilder.builder().build().buildCommentUpdateDto();
        var comment = CommentTestBuilder.builder().build().buildComment();
        var commentDto = CommentTestBuilder.builder().build().buildCommentDto();

        when(commentRepository.findById(id)).thenReturn(Optional.of(comment));
        when(commentConverter.merge(comment, dto)).thenReturn(comment);
        when(commentRepository.save(comment)).thenReturn(comment);
        when(commentConverter.convert(comment)).thenReturn(commentDto);

        commentService.update(dto);

        verify(commentRepository, times(1)).findById(id);
        verify(commentConverter, times(1)).merge(argumentCaptor.capture(), eq(dto));
        assertSame(comment, argumentCaptor.getValue());
        verify(commentRepository, times(1)).save(comment);
    }

    @Test
    void updateShouldThrowEntityNotFoundExceptionWhenCommentNotFound() {
        var id = CommentTestBuilder.builder().build().getId();
        var dto = CommentTestBuilder.builder().build().buildCommentUpdateDto();

        when(commentRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> commentService.update(dto));
        verify(commentRepository, times(1)).findById(id);
    }

    @Test
    void delete() {
        var id = CommentTestBuilder.builder().build().getId();

        commentService.delete(id);

        verify(commentRepository).deleteById(id);
    }
}