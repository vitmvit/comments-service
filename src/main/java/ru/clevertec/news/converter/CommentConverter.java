package ru.clevertec.news.converter;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ru.clevertec.news.dto.CommentCreateDto;
import ru.clevertec.news.dto.CommentDto;
import ru.clevertec.news.dto.CommentUpdateDto;
import ru.clevertec.news.model.Comment;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentConverter {

    CommentDto convert(Comment source);

    Comment convert(CommentCreateDto source);

    Comment convert(CommentUpdateDto source);

    Comment merge(@MappingTarget Comment comment, CommentUpdateDto dto);
}