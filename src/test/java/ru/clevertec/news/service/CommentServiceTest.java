package ru.clevertec.news.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.clevertec.news.converter.CommentConverter;
import ru.clevertec.news.dto.create.CommentCreateDto;
import ru.clevertec.news.dto.update.CommentUpdateDto;
import ru.clevertec.news.exception.EmptyListException;
import ru.clevertec.news.exception.EntityNotFoundException;
import ru.clevertec.news.model.entity.Comment;
import ru.clevertec.news.repository.CommentRepository;
import ru.clevertec.news.service.impl.CommentServiceImpl;
import ru.clevertec.news.util.CommentTestBuilder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ru.clevertec.news.constant.Constant.LIMIT;
import static ru.clevertec.news.constant.Constant.OFFSET;

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


//    @Test
//    void findByIdShouldReturnExpectedCommentWhenFound() {
//        Comment expected = CommentTestBuilder.builder().build().buildComment();
//        CommentDto commentDto = CommentTestBuilder.builder().build().buildCommentDto();
//        Long id = expected.getId();
//
//        when(commentRepository.findById(id)).thenReturn(Optional.of(expected));
//        when(commentConverter.convert(expected)).thenReturn(commentDto);
//
//        CommentDto actual = commentService.findById(id);
//
//        assertThat(actual)
//                .hasFieldOrPropertyWithValue(Comment.Fields.id, expected.getId())
//                .hasFieldOrPropertyWithValue(Comment.Fields.newsId, expected.getNewsId())
//                .hasFieldOrPropertyWithValue(Comment.Fields.username, expected.getUsername())
//                .hasFieldOrPropertyWithValue(Comment.Fields.text, expected.getText())
//                .hasFieldOrPropertyWithValue(Comment.Fields.time, expected.getTime());
//    }

    @Test
    void findByIdShouldThrowEntityNotFoundExceptionWhenCommentNotFound() {
        var exception = assertThrows(Exception.class, () -> commentService.findById(null));
        assertEquals(exception.getClass(), EntityNotFoundException.class);
    }

    @Test
    void findByNewsIdReturnExpectedPageComments() {
        Long newsId = 1L;
        Page<Comment> page = new PageImpl<>(List.of(
                CommentTestBuilder.builder().build().buildComment()
        ));

        when(commentRepository.findByNewsId(PageRequest.of(OFFSET, LIMIT), newsId)).thenReturn(page);

        var actual = commentService.findByNewsId(OFFSET, LIMIT, newsId);

        assertEquals(page.getTotalElements(), actual.getTotalElements());
        verify(commentRepository).findByNewsId(PageRequest.of(OFFSET, LIMIT), newsId);
        verifyNoMoreInteractions(commentRepository);
    }

    @Test
    void findAllShouldReturnExpectedPageComments() {
        Page<Comment> page = new PageImpl<>(List.of(
                CommentTestBuilder.builder().build().buildComment()
        ));

        when(commentRepository.findAll(any(PageRequest.class))).thenReturn(page);

        var actual = commentService.findAll(OFFSET, LIMIT);

        assertEquals(page.getTotalElements(), actual.getTotalElements());
        verify(commentRepository).findAll(any(PageRequest.class));
        verifyNoMoreInteractions(commentRepository);
    }

    @Test
    void findAllShouldReturnEmptyPageWhenEmptyPageComments() {
        when(commentRepository.findAll(PageRequest.of(OFFSET, LIMIT))).thenReturn(Page.empty());

        var exception = assertThrows(Exception.class, () -> commentService.findAll(OFFSET, LIMIT));
        assertEquals(exception.getClass(), EmptyListException.class);
    }

    @Test
    void searchByTextNewsIdReturnExpectedPageComments() {
        String text = CommentTestBuilder.builder().build().getText();
        Page<Comment> page = new PageImpl<>(List.of(
                CommentTestBuilder.builder().build().buildComment()
        ));

        when(commentRepository.findByTextContaining(PageRequest.of(OFFSET, LIMIT), text)).thenReturn(page);

        var actual = commentService.searchByText(OFFSET, LIMIT, text);

        assertEquals(page.getTotalElements(), actual.getTotalElements());
        verify(commentRepository).findByTextContaining(PageRequest.of(OFFSET, LIMIT), text);
        verifyNoMoreInteractions(commentRepository);
    }

    @Test
    void searchByTextShouldReturnEmptyPageWhenEmptyPageComments() {
        String text = CommentTestBuilder.builder().build().getText();

        when(commentRepository.findByTextContaining(PageRequest.of(OFFSET, LIMIT), text)).thenReturn(Page.empty());

        var exception = assertThrows(Exception.class, () -> commentService.searchByText(OFFSET, LIMIT, text));
        assertEquals(exception.getClass(), EmptyListException.class);
    }

    @Test
    void searchByUsernameNewsIdReturnExpectedPageComments() {
        String username = CommentTestBuilder.builder().build().getUsername();
        Page<Comment> page = new PageImpl<>(List.of(
                CommentTestBuilder.builder().build().buildComment()
        ));

        when(commentRepository.findByUsernameContaining(PageRequest.of(OFFSET, LIMIT), username)).thenReturn(page);

        var actual = commentService.searchByUsername(OFFSET, LIMIT, username);

        assertEquals(page.getTotalElements(), actual.getTotalElements());
        verify(commentRepository).findByUsernameContaining(PageRequest.of(OFFSET, LIMIT), username);
        verifyNoMoreInteractions(commentRepository);
    }

    @Test
    void searchByUsernameShouldReturnEmptyPageWhenEmptyPageComments() {
        String username = CommentTestBuilder.builder().build().getUsername();

        when(commentRepository.findByUsernameContaining(PageRequest.of(OFFSET, LIMIT), username)).thenReturn(Page.empty());

        var exception = assertThrows(Exception.class, () -> commentService.searchByUsername(OFFSET, LIMIT, username));
        assertEquals(exception.getClass(), EmptyListException.class);
    }

    @Test
    void createShouldInvokeRepositoryWithoutCommentId() {
        Comment commentToSave = CommentTestBuilder.builder().withId(null).build().buildComment();
        Comment expected = CommentTestBuilder.builder().build().buildComment();
        CommentCreateDto dto = CommentTestBuilder.builder().build().buildCommentCreateDto();

        doReturn(expected).when(commentRepository).save(commentToSave);
        when(commentConverter.convert(dto)).thenReturn(commentToSave);

        commentService.create(dto);

        verify(commentRepository).save(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue()).hasFieldOrPropertyWithValue(Comment.Fields.id, null);
    }


    @Test
    void updateShouldCallsMergeAndSaveWhenCommentFound() {
        Long id = CommentTestBuilder.builder().build().getId();
        CommentUpdateDto dto = CommentTestBuilder.builder().build().buildCommentUpdateDto();
        Comment comment = CommentTestBuilder.builder().build().buildComment();

        when(commentRepository.findById(id)).thenReturn(Optional.of(comment));
        commentService.update(dto);

        verify(commentRepository, times(1)).findById(id);
        verify(commentConverter, times(1)).merge(argumentCaptor.capture(), eq(dto));
        assertSame(comment, argumentCaptor.getValue());
        verify(commentRepository, times(1)).save(comment);
    }

    @Test
    void updateShouldThrowEntityNotFoundExceptionWhenCommentNotFound() {
        Long id = CommentTestBuilder.builder().build().getId();
        CommentUpdateDto dto = CommentTestBuilder.builder().build().buildCommentUpdateDto();

        when(commentRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> commentService.update(dto));
        verify(commentRepository, times(1)).findById(id);
    }

    @Test
    void delete() {
        Long id = CommentTestBuilder.builder().build().getId();
        commentService.delete(id);
        verify(commentRepository).deleteById(id);
    }
}
