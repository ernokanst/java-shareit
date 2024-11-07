package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.*;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTest {
    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private MockMvc mvc;

    private final BookingCreateDto bookingCreate = new BookingCreateDto(
            1,
            LocalDateTime.parse("2025-01-01T12:34:56"),
            LocalDateTime.parse("2025-01-03T11:11:11"),
            2,
            3,
            "WAITING");

    private final BookingDto booking = new BookingDto(
            1,
            LocalDateTime.parse("2025-01-01T12:34:56"),
            LocalDateTime.parse("2025-01-03T11:11:11"),
            new ItemDto(2, "Item", "Description", true, new ArrayList<>(), null),
            new UserDto(3, "John Doe", "noreply@example.com"),
            "WAITING");

    @Test
    void testAdd() throws Exception {
        when(bookingService.add(any(BookingCreateDto.class), anyInt())).thenReturn(booking);

        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 3)
                        .content(mapper.writeValueAsString(bookingCreate))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(booking.getId()), Integer.class))
                .andExpect(jsonPath("$.start", is(booking.getStart().toString())))
                .andExpect(jsonPath("$.end", is(booking.getEnd().toString())))
                .andExpect(jsonPath("$.item.id", is(booking.getItem().getId())))
                .andExpect(jsonPath("$.item.name", is(booking.getItem().getName())))
                .andExpect(jsonPath("$.item.available", is(booking.getItem().getAvailable())))
                .andExpect(jsonPath("$.booker.id", is(booking.getBooker().getId())))
                .andExpect(jsonPath("$.booker.email", is(booking.getBooker().getEmail())))
                .andExpect(jsonPath("$.status", is(booking.getStatus())));
    }

    @Test
    void testApprove() throws Exception {
        when(bookingService.approve(anyInt(), anyInt(), anyBoolean())).thenReturn(booking);

        mvc.perform(patch("/bookings/1")
                        .header("X-Sharer-User-Id", 2)
                        .param("approved", "true")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(booking.getId()), Integer.class))
                .andExpect(jsonPath("$.start", is(booking.getStart().toString())))
                .andExpect(jsonPath("$.end", is(booking.getEnd().toString())))
                .andExpect(jsonPath("$.item.id", is(booking.getItem().getId())))
                .andExpect(jsonPath("$.item.name", is(booking.getItem().getName())))
                .andExpect(jsonPath("$.item.available", is(booking.getItem().getAvailable())))
                .andExpect(jsonPath("$.booker.id", is(booking.getBooker().getId())))
                .andExpect(jsonPath("$.booker.email", is(booking.getBooker().getEmail())))
                .andExpect(jsonPath("$.status", is(booking.getStatus())));
    }

    @Test
    void testGet() throws Exception {
        when(bookingService.get(anyInt(), anyInt())).thenReturn(booking);

        mvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 2)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(booking.getId()), Integer.class))
                .andExpect(jsonPath("$.start", is(booking.getStart().toString())))
                .andExpect(jsonPath("$.end", is(booking.getEnd().toString())))
                .andExpect(jsonPath("$.item.id", is(booking.getItem().getId())))
                .andExpect(jsonPath("$.item.name", is(booking.getItem().getName())))
                .andExpect(jsonPath("$.item.available", is(booking.getItem().getAvailable())))
                .andExpect(jsonPath("$.booker.id", is(booking.getBooker().getId())))
                .andExpect(jsonPath("$.booker.email", is(booking.getBooker().getEmail())))
                .andExpect(jsonPath("$.status", is(booking.getStatus())));
    }

    @Test
    void testGetAll() throws Exception {
        when(bookingService.getAll(any(BookingState.class), anyInt())).thenReturn(List.of(booking));

        mvc.perform(get("/bookings")
                        .param("state", "ALL")
                        .header("X-Sharer-User-Id", 2)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(booking.getId()), Integer.class))
                .andExpect(jsonPath("$[0].start", is(booking.getStart().toString())))
                .andExpect(jsonPath("$[0].end", is(booking.getEnd().toString())))
                .andExpect(jsonPath("$[0].item.id", is(booking.getItem().getId())))
                .andExpect(jsonPath("$[0].item.name", is(booking.getItem().getName())))
                .andExpect(jsonPath("$[0].item.available", is(booking.getItem().getAvailable())))
                .andExpect(jsonPath("$[0].booker.id", is(booking.getBooker().getId())))
                .andExpect(jsonPath("$[0].booker.email", is(booking.getBooker().getEmail())))
                .andExpect(jsonPath("$[0].status", is(booking.getStatus())));
    }

    @Test
    void testGetAllOwner() throws Exception {
        when(bookingService.getAllOwner(any(BookingState.class), anyInt())).thenReturn(List.of(booking));

        mvc.perform(get("/bookings/owner")
                        .param("state", "ALL")
                        .header("X-Sharer-User-Id", 2)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(booking.getId()), Integer.class))
                .andExpect(jsonPath("$[0].start", is(booking.getStart().toString())))
                .andExpect(jsonPath("$[0].end", is(booking.getEnd().toString())))
                .andExpect(jsonPath("$[0].item.id", is(booking.getItem().getId())))
                .andExpect(jsonPath("$[0].item.name", is(booking.getItem().getName())))
                .andExpect(jsonPath("$[0].item.available", is(booking.getItem().getAvailable())))
                .andExpect(jsonPath("$[0].booker.id", is(booking.getBooker().getId())))
                .andExpect(jsonPath("$[0].booker.email", is(booking.getBooker().getEmail())))
                .andExpect(jsonPath("$[0].status", is(booking.getStatus())));
    }
}
