package ru.clevertec.news.starter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.clevertec.news.exception.EmptyListException;
import ru.clevertec.news.exception.EntityNotFoundException;
import ru.clevertec.news.service.CommentService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static ru.clevertec.news.constant.Constant.LIMIT;
import static ru.clevertec.news.constant.Constant.OFFSET;

@SpringBootTest
@AutoConfigureMockMvc
public class GlobalExceptionHandlerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @Test
    public void testHandleEntityNotFoundException() throws Exception {
        Long id = 1L;

        when(commentService.findById(id)).thenThrow(new EntityNotFoundException());

        mockMvc.perform(get("/api/comments/" + id))
                .andExpect(MvcResult::getResolvedException).getClass().equals(EntityNotFoundException.class);
    }

    @Test
    public void testHandleEmptyListException() throws Exception {
        Long id = 1L;

        when(commentService.findByNewsId(OFFSET, LIMIT, id)).thenThrow(new EmptyListException());

        mockMvc.perform(get("/api/comments/" + id, "?limit=" + LIMIT + "&offset=" + OFFSET))
                .andExpect(MvcResult::getResolvedException).getClass().equals(EmptyListException.class);
    }
}
