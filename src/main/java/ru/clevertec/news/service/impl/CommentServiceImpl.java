package ru.clevertec.news.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.clevertec.news.converter.CommentConverter;
import ru.clevertec.news.dto.CommentDto;
import ru.clevertec.news.dto.create.CommentCreateDto;
import ru.clevertec.news.dto.page.PageContentDto;
import ru.clevertec.news.dto.page.PageDto;
import ru.clevertec.news.dto.page.PageParamDto;
import ru.clevertec.news.dto.update.CommentUpdateDto;
import ru.clevertec.news.dto.util.PageUtils;
import ru.clevertec.news.exception.EntityNotFoundException;
import ru.clevertec.news.model.dto.CommentFilterDto;
import ru.clevertec.news.repository.CommentRepository;
import ru.clevertec.news.service.CommentService;
import ru.clevertec.news.specification.CommentSpecification;

/**
 * Реализация сервисного слоя для работы с комментариями.
 */
@Slf4j
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
    @Cacheable(value = "comment", key = "#id")
    @Override
    public CommentDto findById(Long id) {
        log.info("CommentService: find comment by id: " + id);
        return commentConverter.convert(commentRepository.findById(id).orElseThrow(EntityNotFoundException::new));
    }

    /**
     * Ищет комментарии по идентификатору новости с учетом пагинации.
     *
     * @param param объект, содержащий параметры пагинации (номер страницы и размер страницы)
     * @param id    идентификатор новости, для которой нужно найти комментарии
     * @return экземпляр {@link PageContentDto}, содержащий пагинированный список объектов {@link CommentDto}
     * и информацию о пагинации
     */
    @Override
    public PageContentDto<CommentDto> findByNewsId(PageParamDto param, Long id) {
        log.info("CommentService: find comment by news id: " + id);
        var pageable = PageUtils.page(param);
        var page = commentRepository.findByNewsId(pageable, id);
        return new PageContentDto<>(
                new PageDto(param.pageNumber(), param.pageSize(), page.getTotalPages(), page.getTotalElements()),
                commentConverter.convertToList(page.getContent())
        );
    }

    /**
     * Ищет все комментарии с учетом пагинации и фильтрации.
     *
     * @param param  объект, содержащий параметры пагинации (номер страницы и размер страницы)
     * @param filter объект, содержащий условия фильтрации комментариев
     * @return экземпляр {@link PageContentDto}, содержащий пагинированный список объектов {@link CommentDto}
     * и информацию о пагинации
     */
    @Override
    public PageContentDto<CommentDto> findAll(PageParamDto param, CommentFilterDto filter) {
        log.info("CommentService: find all comment");
        var pageable = PageUtils.page(param);
        var specification = Specification.where(CommentSpecification.findAll(filter));
        var page = commentRepository.findAll(specification, pageable);
        return new PageContentDto<>(
                new PageDto(param.pageNumber(), param.pageSize(), page.getTotalPages(), page.getTotalElements()),
                commentConverter.convertToList(page.getContent())
        );
    }

    /**
     * Создает новый комментарий на основе данных из DTO.
     *
     * @param dto данные для создания комментария
     * @return созданный комментарий
     */
    @CacheEvict(value = "comments", key = "#dto.username")
    @Override
    public CommentDto create(CommentCreateDto dto) {
        log.info("CommentService: create comment: " + dto);
        return commentConverter.convert(commentRepository.save(commentConverter.convert(dto)));
    }

    /**
     * Обновляет информацию о комментарии на основе данных из DTO.
     *
     * @param dto данные для обновления комментария
     * @return обновленный комментарий
     * @throws EntityNotFoundException если комментарий не найден
     */
    @CacheEvict(value = "comments", key = "#dto.id")
    @Override
    public CommentDto update(CommentUpdateDto dto) {
        log.info("CommentService: update comment: " + dto);
        var comment = commentRepository.findById(dto.getId()).orElseThrow(EntityNotFoundException::new);
        return commentConverter.convert(commentRepository.save(commentConverter.merge(comment, dto)));
    }

    /**
     * Удаляет комментарий по заданному id.
     *
     * @param id комментария
     */
    @CacheEvict(value = "comments", allEntries = true)
    @Override
    public void delete(Long id) {
        log.info("CommentService: delete comment by id: " + id);
        commentRepository.deleteById(id);
    }
}