package ru.clevertec.news.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import ru.clevertec.news.config.PostgresSqlContainerInitializer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.clevertec.news.constant.Constant.LIMIT;
import static ru.clevertec.news.constant.Constant.OFFSET;

@DataJpaTest
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CommentRepositoryTest extends PostgresSqlContainerInitializer {

    @Autowired
    private CommentRepository commentRepository;

    @Test
    void findByTextContainingShouldReturnExpectedPageComments() {
        String fragment = "t";

        var commentPage = commentRepository.findByTextContaining(PageRequest.of(OFFSET, LIMIT), fragment);
        assertEquals(7, commentPage.getTotalElements());
    }

    @Test
    void findByTextContainingShouldReturnEmptyPageComments() {
        String fragment = "z";

        var commentPage = commentRepository.findByTextContaining(PageRequest.of(OFFSET, LIMIT), fragment);
        assertTrue(commentPage.isEmpty());
    }

    @Test
    void findByUsernameContainingShouldReturnExpectedPageComments() {
        String fragment = "n";

        var commentPage = commentRepository.findByUsernameContaining(PageRequest.of(OFFSET, LIMIT), fragment);
        assertEquals(7, commentPage.getTotalElements());
    }

    @Test
    void findByUsernameContainingShouldReturnEmptyPageComments() {
        String fragment = "z";

        var commentPage = commentRepository.findByUsernameContaining(PageRequest.of(OFFSET, LIMIT), fragment);
        assertTrue(commentPage.isEmpty());
    }

    @Test
    void findByNewsIdShouldReturnExpectedPageComments() {
        Long id = 1L;

        var commentPage = commentRepository.findByNewsId(PageRequest.of(OFFSET, LIMIT), id);
        assertEquals(7, commentPage.getTotalElements());
    }

    @Test
    void findByNewsIdShouldReturnEmptyPageComments() {
        Long id = Long.MAX_VALUE;

        var commentPage = commentRepository.findByNewsId(PageRequest.of(OFFSET, LIMIT), id);
        assertTrue(commentPage.isEmpty());
    }
}
