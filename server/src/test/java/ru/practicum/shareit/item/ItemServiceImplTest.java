package ru.practicum.shareit.item;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceImplTest {
    private final EntityManager em;
    private final ItemService itemService;
    private final UserService userService;
    private final ItemMapper itemMapper;
    private final BookingService bookingService;

    private final CommentDto comment = new CommentDto(1, "Good", "Jane Doe", "2024-01-01T12:34:56");
    private UserDto user1 = new UserDto(1, "John Doe", "noreply@example.com");
    private UserDto user2 = new UserDto(2, "Jane Doe", "email@example.ru");

    private ItemDto item1 = new ItemDto(null, "Item 1", "Description", true, new ArrayList<>(), null);
    private ItemDto item2 = new ItemDto(null, "Item 2", "Description", true, new ArrayList<>(), null);

    private ItemDtoWithDates itemWithDates1;
    private ItemDtoWithDates itemWithDates2;

    @BeforeEach
    void setup() {
        user1 = userService.add(user1);
        user2 = userService.add(user2);
        itemWithDates1 = new ItemDtoWithDates(item1.getId(), item1.getName(), item1.getDescription(), true, new ArrayList<>(), null, null, null);
        itemWithDates2 = new ItemDtoWithDates(item2.getId(), item2.getName(), item2.getDescription(), true, new ArrayList<>(), null, null, null);
    }

    @Test
    void testAddGetDelete() {
        ItemDto resultService = itemService.add(item1, user1.getId());
        item1.setId(resultService.getId());
        itemWithDates1.setId(resultService.getId());
        TypedQuery<Item> query = em.createQuery("Select i from Item i where i.id = :id", Item.class);
        ItemDto resultQuery = itemMapper.toItemDto(query.setParameter("id", item1.getId()).getSingleResult(), new ArrayList<>());
        assertThat(resultQuery, equalTo(resultService));
        assertThat(resultQuery, equalTo(item1));
        assertThat(itemService.getItem(item1.getId(), user1.getId()), equalTo(itemWithDates1));
        ItemDto resultService2 = itemService.add(item2, user1.getId());
        item2.setId(resultService2.getId());
        itemWithDates2.setId(resultService2.getId());
        assertThat(itemService.get(user1.getId()), hasSize(2));
        assertThat(itemService.get(user2.getId()), hasSize(0));
        assertThat(itemService.get(user1.getId()), equalTo(List.of(itemWithDates1, itemWithDates2)));
        itemService.deleteItem(itemWithDates1.getId());
        assertThat(itemService.get(user1.getId()), equalTo(List.of(itemWithDates2)));
    }

    @Test
    void testUpdate() {
        item1 = itemService.add(item1, user1.getId());
        ItemDto newItem = new ItemDto(item1.getId(), "New name", "New description", false, null, null);
        assertThrows(NotFoundException.class, () -> itemService.update(newItem, user2.getId()));
        itemService.update(newItem, user1.getId());
        TypedQuery<Item> query = em.createQuery("Select i from Item i where i.id = :id", Item.class);
        ItemDto resultQuery = itemMapper.toItemDto(query.setParameter("id", item1.getId()).getSingleResult(), new ArrayList<>());
        assertThat(resultQuery.getName(), equalTo(newItem.getName()));
        assertThat(resultQuery.getDescription(), equalTo(newItem.getDescription()));
        assertThat(resultQuery.getAvailable(), equalTo(newItem.getAvailable()));
    }

    @Test
    void testSearch() {
        List<ItemDto> result;
        item1.setName("Binoculars");
        item2.setDescription("NoT a BiNoCuLaR");
        item2.setName("Rolls-Royce Trent 1000");
        item1 = itemService.add(item1, user1.getId());
        item2 = itemService.add(item2, user1.getId());
        result = itemService.search("binocular");
        assertThat(result, equalTo(List.of(item1, item2)));
        result = itemService.search("TRENT");
        assertThat(result, equalTo(List.of(item2)));
        result = itemService.search("");
        assertThat(result, hasSize(0));
    }

    @Test
    void testComment() {
        item1 = itemService.add(item1, user1.getId());
        assertThrows(ValidationException.class, () -> itemService.comment(comment, item1.getId(), user2.getId(), LocalDateTime.now()));
        BookingCreateDto bookingCreate = new BookingCreateDto(
                null,
                LocalDateTime.parse("2024-01-01T12:34:56"),
                LocalDateTime.parse("2024-01-03T11:11:11"),
                item1.getId(),
                null,
                null);
        bookingService.approve(user1.getId(), bookingService.add(bookingCreate, user2.getId()).getId(), true);
        CommentDto resultService = itemService.comment(comment, item1.getId(), user2.getId(), LocalDateTime.parse("2024-01-01T12:34:56"));
        TypedQuery<Comment> query = em.createQuery("Select c from Comment c where c.id = :id", Comment.class);
        CommentDto resultQuery = itemMapper.toCommentDto(query.setParameter("id", comment.getId()).getSingleResult());
        assertThat(resultQuery, equalTo(resultService));
        item1.setComments(List.of(resultService));
        assertThat(itemService.getItem(item1.getId(), user1.getId()).getComments(), equalTo(List.of(comment)));
    }
}
