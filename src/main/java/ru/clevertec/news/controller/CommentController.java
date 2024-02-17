package ru.clevertec.news.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.news.dto.CommentDto;
import ru.clevertec.news.dto.create.CommentCreateDto;
import ru.clevertec.news.dto.update.CommentUpdateDto;
import ru.clevertec.news.service.CommentService;

import static ru.clevertec.news.constant.Constant.LIMIT_DEFAULT;
import static ru.clevertec.news.constant.Constant.OFFSET_DEFAULT;

@RestController
@AllArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<Page<CommentDto>> getAll(@RequestParam(value = "offset", defaultValue = OFFSET_DEFAULT) Integer offset,
                                                   @RequestParam(value = "limit", defaultValue = LIMIT_DEFAULT) Integer limit) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(commentService.findAll(offset, limit));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentDto> getById(@PathVariable("id") Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(commentService.findById(id));
    }

    @GetMapping("news-id/{id}")
    public ResponseEntity<Page<CommentDto>> getByNewsId(@RequestParam(value = "offset", defaultValue = OFFSET_DEFAULT) Integer offset,
                                                        @RequestParam(value = "limit", defaultValue = LIMIT_DEFAULT) Integer limit,
                                                        @PathVariable("id") Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(commentService.findByNewsId(offset, limit, id));
    }

    @GetMapping("/search/text/{text}")
    public ResponseEntity<Page<CommentDto>> searchByText(@RequestParam(value = "offset", defaultValue = OFFSET_DEFAULT) Integer offset,
                                                         @RequestParam(value = "limit", defaultValue = LIMIT_DEFAULT) Integer limit,
                                                         @PathVariable("text") String fragment) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(commentService.searchByText(offset, limit, fragment));
    }

    @GetMapping("/search/username/{username}")
    public ResponseEntity<Page<CommentDto>> searchByUsername(@RequestParam(value = "offset", defaultValue = OFFSET_DEFAULT) Integer offset,
                                                             @RequestParam(value = "limit", defaultValue = LIMIT_DEFAULT) Integer limit,
                                                             @PathVariable("username") String fragment) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(commentService.searchByUsername(offset, limit, fragment));
    }

    @PostMapping
    public ResponseEntity<CommentDto> create(@RequestBody CommentCreateDto commentCreateDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(commentService.create(commentCreateDto));
    }

    @PutMapping
    public ResponseEntity<CommentDto> update(@RequestBody CommentUpdateDto commentUpdateDto) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(commentService.update(commentUpdateDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        commentService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
