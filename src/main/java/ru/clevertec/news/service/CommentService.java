package ru.clevertec.news.service;

import org.springframework.data.domain.Page;
import ru.clevertec.news.dto.CommentDto;
import ru.clevertec.news.dto.create.CommentCreateDto;
import ru.clevertec.news.dto.update.CommentUpdateDto;

public interface CommentService {

    CommentDto findById(Long id);

    Page<CommentDto> findByNewsId(Integer offset, Integer limit, Long id);

    Page<CommentDto> findAll(Integer offset, Integer limit);

    Page<CommentDto> searchByText(Integer offset, Integer limit, String fragment);

    Page<CommentDto> searchByUsername(Integer offset, Integer limit, String fragment);

    CommentDto create(CommentCreateDto dto);

    CommentDto update(CommentUpdateDto dto);

    void delete(Long id);
}
