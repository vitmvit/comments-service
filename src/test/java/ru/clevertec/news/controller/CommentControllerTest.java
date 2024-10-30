package ru.clevertec.news.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.clevertec.news.config.PostgresSqlContainerInitializer;
import ru.clevertec.news.dto.page.PageParamDto;
import ru.clevertec.news.exception.EntityNotFoundException;
import ru.clevertec.news.model.dto.CommentFilterDto;
import ru.clevertec.news.service.CommentService;
import ru.clevertec.news.util.CommentTestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.clevertec.news.constant.Constant.LIMIT;
import static ru.clevertec.news.constant.Constant.OFFSET;

@SpringBootTest
@AutoConfigureMockMvc
public class CommentControllerTest extends PostgresSqlContainerInitializer {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CommentService commentService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getByIdShouldReturnExpectedCommentDtoAndStatus200() throws Exception {
        var id = 1L;
        var expected = commentService.findById(id);

        mockMvc.perform(get("/api/comments/" + id))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
    }

    @Test
    public void getByIdShouldReturnExceptionAndStatus404() throws Exception {
        var id = 20L;

        mockMvc.perform(get("/api/comments/" + id))
                .andExpect(status().isNotFound())
                .andExpect(MvcResult::getResolvedException).getClass().equals(EntityNotFoundException.class);
    }

    @Test
    public void getAllShouldReturnExpectedPageCommentDtoAndStatus200() throws Exception {
        var pageParamDto = new PageParamDto(OFFSET, LIMIT);
        var commentFilterDto = new CommentFilterDto(null, null);
        var expected = commentService.findAll(pageParamDto, commentFilterDto);

        mockMvc.perform(get("/api/comments?pageNumber=" + OFFSET + "&pageSize=" + LIMIT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
    }


    @Test
    public void getByNewsIdShouldReturnExpectedPageCommentDtoAndStatus200() throws Exception {
        var id = 1L;
        var pageParamDto = new PageParamDto(OFFSET, LIMIT);
        var expected = commentService.findByNewsId(pageParamDto, id);

        mockMvc.perform(get("/api/comments/newsId/" + id + "?pageNumber=" + OFFSET + "&pageSize=" + LIMIT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
    }

    @Test
    public void createShouldReturnCreatedCommentAndStatus201() throws Exception {
        var commentCreateDto = CommentTestBuilder.builder().build().buildCommentCreateDto();

        mockMvc.perform(post("/api/comments")
                        .content(objectMapper.writeValueAsString(commentCreateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void updateShouldReturnUpdatedCommentAndStatus200() throws Exception {
        var commentUpdateDto = CommentTestBuilder.builder().build().buildCommentUpdateDto();
        var expected = commentService.update(commentUpdateDto);

        mockMvc.perform(put("/api/comments")
                        .content(objectMapper.writeValueAsString(commentUpdateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
    }

    @Test
    public void updateShouldReturnExceptionAndStatus404() throws Exception {
        var commentUpdateDto = CommentTestBuilder.builder().build().buildCommentUpdateDto();
        commentUpdateDto.setId(100L);

        mockMvc.perform(put("/api/comments")
                        .content(objectMapper.writeValueAsString(commentUpdateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MvcResult::getResolvedException).getClass().equals(EntityNotFoundException.class);
    }

    @Test
    public void deleteShouldReturnStatus204() throws Exception {
        var id = 2L;

        mockMvc.perform(delete("/api/comments/" + id))
                .andExpect(status().isNoContent());
    }
}