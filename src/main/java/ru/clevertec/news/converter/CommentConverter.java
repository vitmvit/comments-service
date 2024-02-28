package ru.clevertec.news.converter;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ru.clevertec.news.dto.CommentDto;
import ru.clevertec.news.dto.create.CommentCreateDto;
import ru.clevertec.news.dto.update.CommentUpdateDto;
import ru.clevertec.news.model.Comment;

/**
 * Конвертер для преобразования моделей и DTO комментариев
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentConverter {

    /**
     * Преобразование объекта Comment в объект CommentDto
     *
     * @param source исходный комментарий типа Comment
     * @return преобразованный комментарий типа CommentDto
     */
    CommentDto convert(Comment source);

    /**
     * Преобразование объекта CommentCreateDto в объект Comment
     *
     * @param source исходный DTO для создания комментария типа CommentCreateDto
     * @return преобразованный комментарий типа Comment
     */
    Comment convert(CommentCreateDto source);

    /**
     * Преобразование объекта CommentUpdateDto в объект Comment
     *
     * @param source исходный DTO для обновления комментария типа CommentUpdateDto
     * @return преобразованная модель комментария типа Comment
     */
    Comment convert(CommentUpdateDto source);

    /**
     * Объединение значений из объекта CommentUpdateDto в существующий комментарий
     *
     * @param comment существующий комментарий типа Comment, в который нужно объединить значения
     * @param dto     DTO для обновления комментария типа CommentUpdateDto
     * @return обновленный комментарий типа Comment
     */
    Comment merge(@MappingTarget Comment comment, CommentUpdateDto dto);
}