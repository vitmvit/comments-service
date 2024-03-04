package ru.clevertec.news.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.clevertec.news.model.entity.Comment;

/**
 * Репозиторий для работы с комментариями новостей
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * Поиск комментариев по содержанию текстового фрагмента с пагинацией
     *
     * @param pageable объект Pageable с информацией о пагинации
     * @param fragment текстовый фрагмент для поиска
     * @return объект Page Comment со списком найденных комментариев
     */
    Page<Comment> findByTextContaining(Pageable pageable, String fragment);

    /**
     * Поиск комментариев по фрагменту имени пользователя с пагинацией
     *
     * @param pageable объект Pageable с информацией о пагинации
     * @param fragment фрагмент имени пользователя для поиска
     * @return объект Page Comment со списком найденных комментариев
     */
    Page<Comment> findByUsernameContaining(Pageable pageable, String fragment);

    /**
     * Поиск комментариев по идентификатору новости с пагинацией
     *
     * @param pageable объект Pageable с информацией о пагинации
     * @param id       идентификатор новости
     * @return объект Page Comment со списком найденных комментариев
     */
    Page<Comment> findByNewsId(Pageable pageable, Long id);
}
