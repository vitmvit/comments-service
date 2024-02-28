package ru.clevertec.news.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.clevertec.news.converter.CommentConverter;
import ru.clevertec.news.dto.CommentDto;
import ru.clevertec.news.dto.create.CommentCreateDto;
import ru.clevertec.news.dto.update.CommentUpdateDto;
import ru.clevertec.news.exception.EmptyListException;
import ru.clevertec.news.exception.EntityNotFoundException;
import ru.clevertec.news.model.Comment;
import ru.clevertec.news.repository.CommentRepository;
import ru.clevertec.news.service.CommentService;

/**
 * Реализация сервисного слоя для работы с комментариями.
 */
@Service
@Transactional
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentConverter commentConverter;

    /**
     * Возвращает информацию о комментарии по заданному id.
     *
     * @param id комментария
     * @return информация о комментарии
     * @throws EntityNotFoundException если комментарий не найден
     */
    @Override
    public CommentDto findById(Long id) {
        return commentConverter.convert(commentRepository.findById(id).orElseThrow(EntityNotFoundException::new));
    }

    /**
     * Возвращает страницу с информацией о комментариях по id новости.
     *
     * @param offset смещение страницы
     * @param limit  лимит элементов на странице
     * @param id     новости
     * @return страница с информацией о комментариях
     */
    @Override
    public Page<CommentDto> findByNewsId(Integer offset, Integer limit, Long id) {
        var commentPage = commentRepository.findByNewsId(PageRequest.of(offset, limit), id);
        return commentPage.map(commentConverter::convert);
    }

    /**
     * Возвращает страницу с информацией о комментариях.
     *
     * @param offset смещение страницы
     * @param limit  лимит элементов на странице
     * @return страница с информацией о комментариях
     */
    @Override
    public Page<CommentDto> findAll(Integer offset, Integer limit) {
        Page<Comment> commentPage = commentRepository.findAll(PageRequest.of(offset, limit));
        commentPage.stream().findAny().orElseThrow(EmptyListException::new);
        return commentPage.map(commentConverter::convert);
    }

    /**
     * Возвращает список комментариев, найденных по фрагменту контента.
     *
     * @param offset   смещение страницы
     * @param limit    лимит элементов на странице
     * @param fragment фрагмент контента
     * @return список комментариев
     * @throws EmptyListException если список комментариев пуст
     */
    @Override
    public Page<CommentDto> searchByText(Integer offset, Integer limit, String fragment) {
        var commentPage = commentRepository.findByTextContaining(PageRequest.of(offset, limit), fragment);
        commentPage.stream().findAny().orElseThrow(EmptyListException::new);
        return commentPage.map(commentConverter::convert);
    }

    /**
     * Возвращает страницу комментариев, найденных по фрагменту имени пользователя.
     *
     * @param offset   смещение страницы
     * @param limit    лимит элементов на странице
     * @param fragment фрагмент имени пользователя
     * @return список комментариев
     * @throws EmptyListException если список комментариев пуст
     */
    @Override
    public Page<CommentDto> searchByUsername(Integer offset, Integer limit, String fragment) {
        var commentPage = commentRepository.findByUsernameContaining(PageRequest.of(offset, limit), fragment);
        commentPage.stream().findAny().orElseThrow(EmptyListException::new);
        return commentPage.map(commentConverter::convert);
    }

    /**
     * Создает новый комментарий на основе данных из DTO.
     *
     * @param dto данные для создания комментария
     * @return созданный комментарий
     */
    @Override
    public CommentDto create(CommentCreateDto dto) {
        var comment = commentConverter.convert(dto);
        return commentConverter.convert(commentRepository.save(comment));
    }

    /**
     * Обновляет информацию о комментарии на основе данных из DTO.
     *
     * @param dto данные для обновления комментария
     * @return обновленный комментарий
     * @throws EntityNotFoundException если комментарий не найден
     */
    @Override
    public CommentDto update(CommentUpdateDto dto) {
        var comment = commentRepository.findById(dto.getId()).orElseThrow(EntityNotFoundException::new);
        commentConverter.merge(comment, dto);
        return commentConverter.convert(commentRepository.save(comment));
    }

    /**
     * Удаляет комментарий по заданному id.
     *
     * @param id комментария
     */
    @Override
    public void delete(Long id) {
        commentRepository.deleteById(id);
    }
}
