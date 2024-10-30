package ru.clevertec.news.specification;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import ru.clevertec.news.model.dto.CommentFilterDto;
import ru.clevertec.news.model.entity.Comment;

/**
 * Утилитный класс для создания спецификаций фильтрации комментариев.
 */
@UtilityClass
public class CommentSpecification {

    /**
     * Создает спецификацию для фильтрации комментариев по заданным параметрам из {@link CommentFilterDto}.
     *
     * @param filter объект, содержащий параметры фильтрации комментариев
     * @return спецификация для поиска комментариев
     */
    public static Specification<Comment> findAll(CommentFilterDto filter) {
        Specification<Comment> spec = Specification.where(null);
        if (StringUtils.isNotEmpty(filter.username())) {
            spec = spec.and(findByUsername(filter.username()));
        }
        if (filter.text() != null && StringUtils.isNotEmpty(filter.text())) {
            spec = spec.and(findByText(filter.text()));
        }
        return spec;
    }

    /**
     * Создает спецификацию для фильтрации комментариев по имени пользователя.
     *
     * @param username имя пользователя для фильтрации
     * @return спецификация для поиска комментариев с указанным именем пользователя
     */
    private static Specification<Comment> findByUsername(String username) {
        return (channel, criteriaQuery, criteriaBuilder) -> {
            return criteriaBuilder.like(channel.get("username"), "%" + username + "%");
        };
    }

    /**
     * Создает спецификацию для фильтрации комментариев по тексту комментария.
     *
     * @param fragment фрагмент текста для фильтрации
     * @return спецификация для поиска комментариев, содержащих указанный текст
     */
    private static Specification<Comment> findByText(String fragment) {
        return (channel, criteriaQuery, criteriaBuilder) -> {
            return criteriaBuilder.like(channel.get("text"), "%" + fragment + "%");
        };
    }
}