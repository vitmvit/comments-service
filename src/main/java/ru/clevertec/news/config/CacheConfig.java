package ru.clevertec.news.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ru.clevertec.news.cache.Cache;
import ru.clevertec.news.cache.impl.CacheLfu;
import ru.clevertec.news.cache.impl.CacheLru;
import ru.clevertec.news.dto.CommentDto;
import ru.clevertec.news.exception.CacheNotFoundException;
import ru.clevertec.news.service.proxy.CommentProxyService;

import static ru.clevertec.news.constant.Variable.LFU;
import static ru.clevertec.news.constant.Variable.LRU;

/**
 * Класс, который отвечает за конфигурацию кэша.
 */
@Configuration
@Profile("dev")
public class CacheConfig {

    /**
     * Алгоритм кэширования.
     */
    @Value("${cache.algorithm}")
    private String algorithm;

    /**
     * Вместимость кэша.
     */
    @Value("${cache.capacity}")
    private int capacity;

    /**
     * Возвращает кэш LRU.
     *
     * @return кэш LRU.
     */
    @Bean
    public Cache<Long, CommentDto> getLruCache() {
        return new CacheLru<>();
    }

    /**
     * Возвращает кэш LFU.
     *
     * @return кэш LFU.
     */
    @Bean
    public Cache<Long, CommentDto> getLfuCache() {
        return new CacheLfu<>();
    }

    /**
     * Возвращает кэш в зависимости от выбранного алгоритма.
     *
     * @return кэш.
     * @throws CacheNotFoundException если выбран некорректный алгоритм кэширования.
     */
    @Bean
    public Cache<Long, CommentDto> cache() {
        Cache<Long, CommentDto> cache;
        if (algorithm.equals(LFU)) {
            cache = getLfuCache();
        } else if (algorithm.equals(LRU)) {
            cache = getLruCache();
        } else {
            throw new CacheNotFoundException();
        }
        cache.capacity(capacity);
        return cache;
    }

    /**
     * Возвращает сервис прокси для работы с HouseDto.
     *
     * @return CommentProxyService - сервис прокси для работы с HouseDto.
     */
    @Bean
    public CommentProxyService getCommentProxyService() {
        return new CommentProxyService(cache());
    }
}
