package ru.clevertec.news.converter;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ru.clevertec.news.dto.CommentDto;
import ru.clevertec.news.dto.create.CommentCreateDto;
import ru.clevertec.news.dto.update.CommentUpdateDto;
import ru.clevertec.news.model.entity.Comment;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentConverter {

    CommentDto convert(Comment source);

    Comment convert(CommentCreateDto source);

    List<CommentDto> convertToList(List<Comment> source);

    Comment merge(@MappingTarget Comment comment, CommentUpdateDto dto);
}