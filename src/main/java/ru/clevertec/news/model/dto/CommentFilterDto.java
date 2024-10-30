package ru.clevertec.news.model.dto;

public record CommentFilterDto(
        String username,
        String text
) {
}