package ru.clevertec.news.service;

import ru.clevertec.news.dto.CommentDto;
import ru.clevertec.news.dto.create.CommentCreateDto;
import ru.clevertec.news.dto.page.PageContentDto;
import ru.clevertec.news.dto.page.PageParamDto;
import ru.clevertec.news.dto.update.CommentUpdateDto;
import ru.clevertec.news.model.dto.CommentFilterDto;

public interface CommentService {

    CommentDto findById(Long id);

    PageContentDto<CommentDto> findByNewsId(PageParamDto param, Long id);

    PageContentDto<CommentDto> findAll(PageParamDto param, CommentFilterDto filter);

    CommentDto create(CommentCreateDto dto);

    CommentDto update(CommentUpdateDto dto);

    void delete(Long id);
}