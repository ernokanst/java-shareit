package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
public class ItemControllerTest {
    @Autowired
    ObjectMapper mapper;

    @MockBean
    ItemService itemService;

    @Autowired
    private MockMvc mvc;

    private final CommentDto comment = new CommentDto(1, "Good", "John Doe", "2024-01-01T12:34:56");

    private final ItemDto item = new ItemDto(
            1,
            "Item",
            "Description",
            true,
            List.of(comment),
            1);

    private final ItemDtoWithDates itemWithDates = new ItemDtoWithDates(
            1,
            "Item with dates",
            "Description",
            true,
            List.of(comment),
            new BookingDto(
                    1,
                    LocalDateTime.parse("2024-11-01T12:34:56"),
                    LocalDateTime.parse("2024-11-03T11:11:11"),
                    null,
                    null,
                    null),
            new BookingDto(
                    2,
                    LocalDateTime.parse("2024-12-01T12:34:56"),
                    LocalDateTime.parse("2024-12-03T11:11:11"),
                    null,
                    null,
                    null),
            1);

    @Test
    void testAdd() throws Exception {
        when(itemService.add(ArgumentMatchers.any(ItemDto.class), anyInt())).thenReturn(item);

        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(item))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(item.getId()), Integer.class))
                .andExpect(jsonPath("$.name", is(item.getName()), String.class))
                .andExpect(jsonPath("$.description", is(item.getDescription()), String.class))
                .andExpect(jsonPath("$.available", is(item.getAvailable()), Boolean.class))
                .andExpect(jsonPath("$.comments[0].text", is(item.getComments().getFirst().getText()), String.class))
                .andExpect(jsonPath("$.id", is(item.getRequestId()), Integer.class));
    }

    @Test
    void testUpdate() throws Exception {
        when(itemService.update(ArgumentMatchers.any(ItemDto.class), anyInt())).thenReturn(item);

        mvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(item))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(item.getId()), Integer.class))
                .andExpect(jsonPath("$.name", is(item.getName()), String.class))
                .andExpect(jsonPath("$.description", is(item.getDescription()), String.class))
                .andExpect(jsonPath("$.available", is(item.getAvailable()), Boolean.class))
                .andExpect(jsonPath("$.comments[0].text", is(item.getComments().getFirst().getText()), String.class))
                .andExpect(jsonPath("$.id", is(item.getRequestId()), Integer.class));
    }

    @Test
    void testGet() throws Exception {
        when(itemService.get(anyInt())).thenReturn(List.of(itemWithDates));

        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemWithDates.getId()), Integer.class))
                .andExpect(jsonPath("$[0].name", is(itemWithDates.getName()), String.class))
                .andExpect(jsonPath("$[0].description", is(itemWithDates.getDescription()), String.class))
                .andExpect(jsonPath("$[0].lastBooking.start", is(itemWithDates.getLastBooking().getStart().toString()), String.class))
                .andExpect(jsonPath("$[0].lastBooking.end", is(itemWithDates.getLastBooking().getEnd().toString()), String.class))
                .andExpect(jsonPath("$[0].nextBooking.start", is(itemWithDates.getNextBooking().getStart().toString()), String.class))
                .andExpect(jsonPath("$[0].nextBooking.end", is(itemWithDates.getNextBooking().getEnd().toString()), String.class))
                .andExpect(jsonPath("$[0].available", is(itemWithDates.getAvailable()), Boolean.class))
                .andExpect(jsonPath("$[0].comments[0].text", is(itemWithDates.getComments().getFirst().getText()), String.class))
                .andExpect(jsonPath("$[0].id", is(itemWithDates.getRequestId()), Integer.class));
    }

    @Test
    void testGetItem() throws Exception {
        when(itemService.getItem(anyInt(), anyInt())).thenReturn(itemWithDates);

        mvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemWithDates.getId()), Integer.class))
                .andExpect(jsonPath("$.name", is(itemWithDates.getName()), String.class))
                .andExpect(jsonPath("$.description", is(itemWithDates.getDescription()), String.class))
                .andExpect(jsonPath("$.lastBooking.start", is(itemWithDates.getLastBooking().getStart().toString()), String.class))
                .andExpect(jsonPath("$.lastBooking.end", is(itemWithDates.getLastBooking().getEnd().toString()), String.class))
                .andExpect(jsonPath("$.nextBooking.start", is(itemWithDates.getNextBooking().getStart().toString()), String.class))
                .andExpect(jsonPath("$.nextBooking.end", is(itemWithDates.getNextBooking().getEnd().toString()), String.class))
                .andExpect(jsonPath("$.available", is(itemWithDates.getAvailable()), Boolean.class))
                .andExpect(jsonPath("$.comments[0].text", is(itemWithDates.getComments().getFirst().getText()), String.class))
                .andExpect(jsonPath("$.id", is(itemWithDates.getRequestId()), Integer.class));
    }

    @Test
    void testDeleteItem() throws Exception {
        mvc.perform(delete("/items/1")
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testSearch() throws Exception {
        when(itemService.search(anyString())).thenReturn(List.of(item));

        mvc.perform(get("/items/search")
                        .param("text", "name")
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(item.getId()), Integer.class))
                .andExpect(jsonPath("$[0].name", is(item.getName()), String.class))
                .andExpect(jsonPath("$[0].description", is(item.getDescription()), String.class))
                .andExpect(jsonPath("$[0].available", is(item.getAvailable()), Boolean.class))
                .andExpect(jsonPath("$[0].comments[0].text", is(item.getComments().getFirst().getText()), String.class))
                .andExpect(jsonPath("$[0].id", is(item.getRequestId()), Integer.class));
    }

    @Test
    void testComment() throws Exception {
        when(itemService.comment(any(CommentDto.class), anyInt(), anyInt(), any(LocalDateTime.class))).thenReturn(comment);

        mvc.perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(comment))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(comment.getId()), Integer.class))
                .andExpect(jsonPath("$.text", is(comment.getText()), String.class))
                .andExpect(jsonPath("$.authorName", is(comment.getAuthorName()), String.class))
                .andExpect(jsonPath("$.created", is(comment.getCreated()), String.class));
    }

    @Test
    void testUpdateNotOwner() throws Exception {
        when(itemService.update(ArgumentMatchers.any(ItemDto.class), anyInt())).thenThrow(new NotFoundException("Пользователь не соответствует владельцу вещи", item));

        mvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", 2)
                        .content(mapper.writeValueAsString(item))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testNotFound() throws Exception {
        when(itemService.getItem(anyInt(), anyInt())).thenThrow(NoSuchElementException.class);

        mvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
