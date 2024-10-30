package ru.clevertec.news.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import ru.clevertec.news.config.PostgresSqlContainerInitializer;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CommentRepositoryTest extends PostgresSqlContainerInitializer {

    @Autowired
    private CommentRepository commentRepository;

    @Test
    void findByNewsIdShouldReturnExpectedPageComment() {
        var newsId = 1L;
        var pageable = PageRequest.of(0, 10);
        var actual = commentRepository.findByNewsId(pageable, newsId);

        assertThat(actual).isNotNull();
        assertThat(actual.getTotalElements()).isGreaterThan(0);

        actual.getContent().forEach(comment -> {
            assertThat(comment.getNewsId()).isEqualTo(newsId);
        });
    }
}