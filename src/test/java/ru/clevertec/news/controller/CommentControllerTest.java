package ru.clevertec.news.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.clevertec.news.config.PostgresSqlContainerInitializer;
import ru.clevertec.news.dto.create.CommentCreateDto;
import ru.clevertec.news.dto.update.CommentUpdateDto;
import ru.clevertec.news.exception.EmptyListException;
import ru.clevertec.news.exception.EntityNotFoundException;
import ru.clevertec.news.feign.AuthClient;
import ru.clevertec.news.service.CommentService;
import ru.clevertec.news.util.CommentTestBuilder;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.clevertec.news.constant.Constant.LIMIT;
import static ru.clevertec.news.constant.Constant.OFFSET;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class CommentControllerTest extends PostgresSqlContainerInitializer {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CommentService commentService;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthClient authClient;

    @Test
    public void getByIdShouldReturnExpectedCommentDtoAndStatus200() throws Exception {
        Long id = 1L;

        var expected = commentService.findById(id);

        mockMvc.perform(get("/api/comments/" + id))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
    }

    @Test
    public void getByIdShouldReturnExceptionAndStatus404() throws Exception {
        Long id = 20L;

        mockMvc.perform(get("/api/comments/" + id))
                .andExpect(status().isNotFound())
                .andExpect(MvcResult::getResolvedException).getClass().equals(EntityNotFoundException.class);
    }

    @Test
    public void getAllShouldReturnExpectedPageCommentDtoAndStatus200() throws Exception {
        var expected = commentService.findAll(OFFSET, LIMIT);

        mockMvc.perform(get("/api/comments?offset=" + OFFSET + "&limit=" + LIMIT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
    }

    @Test
    public void getAllShouldReturnExceptionAndStatus404() throws Exception {
        Integer page = 100;

        mockMvc.perform(get("/api/comments?offset=" + page + "&limit=" + LIMIT))
                .andExpect(status().isNotFound())
                .andExpect(MvcResult::getResolvedException).getClass().equals(EmptyListException.class);
    }

    @Test
    public void getByNewsIdShouldReturnExpectedPageCommentDtoAndStatus200() throws Exception {
        Long id = 1L;

        var expected = commentService.findByNewsId(OFFSET, LIMIT, id);

        mockMvc.perform(get("/api/comments/news-id/" + id + "?offset=" + OFFSET + "&limit=" + LIMIT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
    }

    @Test
    public void searchByTextShouldReturnExpectedPageCommentDtoAndStatus200() throws Exception {
        String fragment = "t";

        var expected = commentService.searchByText(OFFSET, LIMIT, fragment);

        mockMvc.perform(get("/api/comments/search/text/" + fragment + "?offset=" + OFFSET + "&limit=" + LIMIT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
    }

    @Test
    public void searchByTextShouldReturnExceptionAndStatus404() throws Exception {
        String fragment = "z";

        mockMvc.perform(get("/api/comments/search/text/" + fragment + "?offset=" + OFFSET + "&limit=" + LIMIT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MvcResult::getResolvedException).getClass().equals(EmptyListException.class);
    }

    @Test
    public void searchByUsernameShouldReturnExpectedPageCommentDtoAndStatus200() throws Exception {
        String fragment = "n";

        var expected = commentService.searchByUsername(OFFSET, LIMIT, fragment);

        mockMvc.perform(get("/api/comments/search/username/" + fragment + "?offset=" + OFFSET + "&limit=" + LIMIT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
    }

    @Test
    public void searchByUsernameShouldReturnExceptionAndStatus404() throws Exception {
        String fragment = "z";

        mockMvc.perform(get("/api/comments/search/username/" + fragment + "?offset=" + OFFSET + "&limit=" + LIMIT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MvcResult::getResolvedException).getClass().equals(EmptyListException.class);
    }

    @Test
    public void createShouldReturnCreatedCommentAndStatus201() throws Exception {
        CommentCreateDto commentCreateDto = CommentTestBuilder.builder().build().buildCommentCreateDto();
        String token = CommentTestBuilder.builder().build().getToken();

        when(authClient.check(token.replace("Bearer ", ""), null, commentCreateDto.getUsername())).thenReturn(true);

        mockMvc.perform(post("/api/comments")
                        .header("Authorization", token)
                        .content(objectMapper.writeValueAsString(commentCreateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void updateShouldReturnUpdatedCommentAndStatus201() throws Exception {
        CommentUpdateDto commentUpdateDto = CommentTestBuilder.builder().build().buildCommentUpdateDto();
        String token = CommentTestBuilder.builder().build().getToken();

        when(authClient.check(token.replace("Bearer ", ""), null, commentUpdateDto.getUsername())).thenReturn(true);

        var expected = commentService.update(commentUpdateDto);

        mockMvc.perform(put("/api/comments")
                        .header("Authorization", token)
                        .content(objectMapper.writeValueAsString(commentUpdateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
    }

    @Test
    public void updateShouldReturnExceptionAndStatus404() throws Exception {
        CommentUpdateDto commentUpdateDto = CommentTestBuilder.builder().build().buildCommentUpdateDto();
        commentUpdateDto.setId(100L);
        String token = CommentTestBuilder.builder().build().getToken();

        when(authClient.check(token.replace("Bearer ", ""), null, commentUpdateDto.getUsername())).thenReturn(true);

        mockMvc.perform(put("/api/comments")
                        .header("Authorization", token)
                        .content(objectMapper.writeValueAsString(commentUpdateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MvcResult::getResolvedException).getClass().equals(EntityNotFoundException.class);
    }

    @Test
    public void deleteShouldReturnStatus204() throws Exception {
        Long id = 2L;
        Long userId = 2L;
        String token = CommentTestBuilder.builder().build().getToken();

        when(authClient.check(token.replace("Bearer ", ""), userId, null)).thenReturn(true);

        mockMvc.perform(delete("/api/comments/" + id + "/" + userId)
                        .header("Authorization", token))
                .andExpect(status().isNoContent());
    }
}
