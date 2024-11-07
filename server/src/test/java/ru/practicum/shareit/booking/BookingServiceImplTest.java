package ru.practicum.shareit.booking;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hamcrest.beans.HasPropertyWithValue;
import org.hamcrest.core.Every;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceImplTest {
    private final EntityManager em;
    private final BookingMapper bookingMapper;
    private final BookingService bookingService;
    private final ItemService itemService;
    private final UserService userService;

    private ItemDto item = new ItemDto(1, "Item", "Description", true, new ArrayList<>(), null);
    private UserDto user1 = new UserDto(1, "John Doe", "noreply@example.com");
    private UserDto user2 = new UserDto(2, "Jane Doe", "email@example.ru");
    private UserDto user3 = new UserDto(3, "No name", "email@example.org");

    private BookingCreateDto bookingCreate;

    private final BookingDto booking = new BookingDto(
            1,
            LocalDateTime.parse("2025-01-01T12:34:56"),
            LocalDateTime.parse("2025-01-03T11:11:11"),
            item,
            user2,
            "WAITING");

    @BeforeEach
    void setup() {
        user1 = userService.add(user1);
        user2 = userService.add(user2);
        user3 = userService.add(user3);
        item = itemService.add(item, user1.getId());
        bookingCreate = new BookingCreateDto(
                null,
                LocalDateTime.parse("2025-01-01T12:34:56"),
                LocalDateTime.parse("2025-01-03T11:11:11"),
                item.getId(),
                null,
                null);
    }

    @Test
    void testAddGetApprove() {
        BookingDto resultService = bookingService.add(bookingCreate, user2.getId());
        TypedQuery<Booking> query = em.createQuery("Select b from Booking b where b.id = :id", Booking.class);
        BookingDto resultQuery = bookingMapper.toBookingDto(query.setParameter("id", 1).getSingleResult());
        assertThat(resultQuery, equalTo(resultService));
        assertThat(resultQuery, equalTo(booking));
        assertThat(resultQuery.getBooker(), equalTo(user2));
        assertThat(resultQuery.getItem(), equalTo(item));
        assertThat(resultQuery, equalTo(bookingService.get(1, user1.getId())));
        assertThrows(ValidationException.class, () -> bookingService.get(1, user3.getId()));
        assertThrows(ValidationException.class, () -> bookingService.approve(user2.getId(), 1, true));
        assertThat(bookingService.approve(user1.getId(), 1, false).getStatus(), equalTo("REJECTED"));
    }

    @Test
    void testGetAll() {
        List<BookingDto> result;
        int b1 = bookingService.add(bookingCreate, user2.getId()).getId();
        int b2 = bookingService.add(bookingCreate, user2.getId()).getId();
        int b3 = bookingService.add(bookingCreate, user3.getId()).getId();
        int b4 = bookingService.add(bookingCreate, user3.getId()).getId();
        bookingService.approve(user1.getId(), b1, true);
        bookingService.approve(user1.getId(), b2, false);
        bookingService.approve(user1.getId(), b3, true);
        result = bookingService.getAll(BookingState.ALL, user1.getId());
        assertThat(result, hasSize(0));
        result = bookingService.getAll(BookingState.ALL, user2.getId());
        assertThat(result, hasSize(2));
        assertThat(result, (Every.everyItem(HasPropertyWithValue.hasProperty("booker", Is.is(user2)))));
        result = bookingService.getAll(BookingState.PAST, user2.getId());
        assertThat(result, hasSize(0));
        result = bookingService.getAll(BookingState.FUTURE, user2.getId());
        assertThat(result, hasSize(2));
        result = bookingService.getAll(BookingState.REJECTED, user2.getId());
        assertThat(result, hasSize(1));
        result = bookingService.getAll(BookingState.WAITING, user2.getId());
        assertThat(result, hasSize(0));
        result = bookingService.getAll(BookingState.ALL, user3.getId());
        assertThat(result, hasSize(2));
        assertThat(result, (Every.everyItem(HasPropertyWithValue.hasProperty("booker", Is.is(user3)))));
        result = bookingService.getAll(BookingState.WAITING, user3.getId());
        assertThat(result, hasSize(1));
        result = bookingService.getAll(BookingState.FUTURE, user3.getId());
        assertThat(result, hasSize(2));
    }

    @Test
    void testGetAllOwner() {
        List<BookingDto> result;
        int b1 = bookingService.add(bookingCreate, user2.getId()).getId();
        int b2 = bookingService.add(bookingCreate, user2.getId()).getId();
        int b3 = bookingService.add(bookingCreate, user3.getId()).getId();
        int b4 = bookingService.add(bookingCreate, user3.getId()).getId();
        bookingService.approve(user1.getId(), b1, true);
        bookingService.approve(user1.getId(), b2, false);
        bookingService.approve(user1.getId(), b3, true);
        result = bookingService.getAllOwner(BookingState.ALL, user1.getId());
        assertThat(result, hasSize(4));
        assertThat(result, (Every.everyItem(HasPropertyWithValue.hasProperty("item", Is.is(item)))));
        result = bookingService.getAllOwner(BookingState.PAST, user1.getId());
        assertThat(result, hasSize(0));
        result = bookingService.getAllOwner(BookingState.FUTURE, user1.getId());
        assertThat(result, hasSize(4));
        result = bookingService.getAllOwner(BookingState.REJECTED, user1.getId());
        assertThat(result, hasSize(1));
        result = bookingService.getAllOwner(BookingState.WAITING, user1.getId());
        assertThat(result, hasSize(1));
    }
}
