package ru.clevertec.news.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.news.dto.CommentDto;
import ru.clevertec.news.dto.create.CommentCreateDto;
import ru.clevertec.news.dto.update.CommentUpdateDto;
import ru.clevertec.news.exception.NoAccessError;
import ru.clevertec.news.feign.AuthClient;
import ru.clevertec.news.service.CommentService;

import static ru.clevertec.news.constant.Constant.LIMIT_DEFAULT;
import static ru.clevertec.news.constant.Constant.OFFSET_DEFAULT;

/**
 * Контроллер для работы с комментариями
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    private final AuthClient authClient;

    /**
     * Получение списка всех комментариев с пагинацией
     *
     * @param offset смещение (начальный индекс комментариев)
     * @param limit  количество комментариев на странице
     * @return объект ResponseEntity со списком комментариев типа Page<CommentDto> и статусом OK
     */
    @GetMapping
    public ResponseEntity<Page<CommentDto>> getAll(@RequestParam(value = "offset", defaultValue = OFFSET_DEFAULT) Integer offset,
                                                   @RequestParam(value = "limit", defaultValue = LIMIT_DEFAULT) Integer limit) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(commentService.findAll(offset, limit));
    }

    /**
     * Получение комментария по его идентификатору
     *
     * @param id идентификатор комментария
     * @return объект ResponseEntity с найденным комментарием типа CommentDto и статусом OK
     */
    @GetMapping("/{id}")
    public ResponseEntity<CommentDto> getById(@PathVariable("id") Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(commentService.findById(id));
    }

    /**
     * Получение списка комментариев для определенной новости с пагинацией
     *
     * @param offset смещение (начальный индекс комментариев)
     * @param limit  количество комментариев на странице
     * @param id     идентификатор новости
     * @return объект ResponseEntity со списком комментариев для новости типа Page<CommentDto> и статусом OK
     */
    @GetMapping("news-id/{id}")
    public ResponseEntity<Page<CommentDto>> getByNewsId(@RequestParam(value = "offset", defaultValue = OFFSET_DEFAULT) Integer offset,
                                                        @RequestParam(value = "limit", defaultValue = LIMIT_DEFAULT) Integer limit,
                                                        @PathVariable("id") Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(commentService.findByNewsId(offset, limit, id));
    }

    /**
     * Поиск комментариев по фрагменту текста с пагинацией
     *
     * @param offset   смещение (начальный индекс комментариев)
     * @param limit    количество комментариев на странице
     * @param fragment текстовый фрагмент для поиска
     * @return объект ResponseEntity со списком найденных комментариев типа Page<CommentDto> и статусом OK
     */
    @GetMapping("/search/text/{text}")
    public ResponseEntity<Page<CommentDto>> searchByText(@RequestParam(value = "offset", defaultValue = OFFSET_DEFAULT) Integer offset,
                                                         @RequestParam(value = "limit", defaultValue = LIMIT_DEFAULT) Integer limit,
                                                         @PathVariable("text") String fragment) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(commentService.searchByText(offset, limit, fragment));
    }

    /**
     * Поиск комментариев по фрагменту имени пользователя с пагинацией
     *
     * @param offset   смещение (начальный индекс комментариев)
     * @param limit    количество комментариев на странице
     * @param fragment фрагмент имени пользователя для поиска
     * @return объект ResponseEntity со списком найденных комментариев типа Page<CommentDto> и статусом OK
     */
    @GetMapping("/search/username/{username}")
    public ResponseEntity<Page<CommentDto>> searchByUsername(@RequestParam(value = "offset", defaultValue = OFFSET_DEFAULT) Integer offset,
                                                             @RequestParam(value = "limit", defaultValue = LIMIT_DEFAULT) Integer limit,
                                                             @PathVariable("username") String fragment) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(commentService.searchByUsername(offset, limit, fragment));
    }

    /**
     * Создание нового комментария
     *
     * @param commentCreateDto данные для создания комментария
     * @param auth             строка заголовка Authorization, содержащая JWT-токен
     * @return объект ResponseEntity с созданным комментарием типа CommentDto и статусом CREATED
     * @throws NoAccessError если отсутствует доступ или неверный JWT-токен
     */
    @PostMapping
    public ResponseEntity<CommentDto> create(@RequestBody CommentCreateDto commentCreateDto, @RequestHeader("Authorization") String auth) {
        var token = auth.replace("Bearer ", "");
        if (authClient.check(token, null, commentCreateDto.getUsername())) {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(commentService.create(commentCreateDto));
        }
        throw new NoAccessError();
    }

    /**
     * Обновление комментария
     *
     * @param commentUpdateDto объект CommentUpdateDto с обновленными данными комментария
     * @param auth             строка заголовка Authorization, содержащая JWT-токен
     * @return объект ResponseEntity с обновленным комментарием типа CommentDto и статусом OK
     * @throws NoAccessError если отсутствует доступ или неверный JWT-токен
     */
    @PutMapping
    public ResponseEntity<CommentDto> update(@RequestBody CommentUpdateDto commentUpdateDto, @RequestHeader("Authorization") String auth) {
        var token = auth.replace("Bearer ", "");
        if (authClient.check(token, null, commentUpdateDto.getUsername())) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(commentService.update(commentUpdateDto));
        }
        throw new NoAccessError();
    }

    /**
     * Удаление комментария по его идентификатору
     *
     * @param id     идентификатор комментария
     * @param userId идентификатор пользователя, связанного с комментарием
     * @param auth   строка заголовка Authorization, содержащая JWT-токен
     * @return объект ResponseEntity со статусом NO_CONTENT
     * @throws NoAccessError если отсутствует доступ или неверный JWT-токен
     */
    @DeleteMapping("/{id}/{user-id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id, @PathVariable("user-id") Long userId, @RequestHeader("Authorization") String auth) {
        var token = auth.replace("Bearer ", "");
        if (authClient.check(token, userId, null)) {
            commentService.delete(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        throw new NoAccessError();
    }
}
