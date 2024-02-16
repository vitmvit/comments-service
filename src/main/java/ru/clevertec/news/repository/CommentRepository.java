package ru.clevertec.news.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.clevertec.news.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findByTextContaining(Pageable pageable, String fragment);

    Page<Comment> findByUsernameContaining(Pageable pageable, String fragment);

    Page<Comment> findByNewsId(Pageable pageable, Long id);
}
