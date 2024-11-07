package ru.practicum.shareit.request;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestServiceImplTest {
    private final ItemRequestService requestService;
    private final UserService userService;
    private final ItemService itemService;

    private UserDto user1 = new UserDto(1, "John Doe", "noreply@example.com");
    private UserDto user2 = new UserDto(2, "Jane Doe", "email@example.ru");
    private ItemDto item1 = new ItemDto(1, "Item 1", "Description", true, new ArrayList<>(), 1);
    private ItemDto item2 = new ItemDto(2, "Item 2", "Description", true, new ArrayList<>(), 1);

    ItemRequestDto request = new ItemRequestDto(
            null,
            "Description",
            1,
            LocalDateTime.parse("2025-01-01T12:34:56"),
            new ArrayList<>());

    @BeforeEach
    void setup() {
        user1 = userService.add(user1);
        user2 = userService.add(user2);
        request.setId(null);
    }

    @Test
    void testAddGet() {
        ItemRequestDto result = requestService.create(request, user1.getId());
        request.setId(result.getId());
        assertThat(result, equalTo(request));
        item1.setRequestId(result.getId());
        item2.setRequestId(result.getId());
        item1 = itemService.add(item1, user2.getId());
        item2 = itemService.add(item2, user2.getId());
        request.setItems(List.of(item1, item2));
        assertThat(requestService.get(request.getId()), equalTo(request));
    }

    @Test
    void testGetAllGetUser() {
        List<ItemRequestDto> result;
        requestService.create(request, user1.getId());
        requestService.create(request, user2.getId());
        itemService.add(item1, user2.getId());
        itemService.add(item2, user1.getId());
        result = requestService.getAll();
        assertThat(result, hasSize(2));
        assertThat(result.getLast().getItems(), hasSize(2));
        assertThat(result.getFirst().getItems(), hasSize(0));
        assertThat(result.getLast().getItems().getFirst(), equalTo(item1));
        assertThat(result.getLast().getItems().getLast(), equalTo(item2));
        result = requestService.getFromUser(user1.getId());
        assertThat(result, hasSize(1));
        assertThat(result.getFirst().getItems(), hasSize(2));
        result = requestService.getFromUser(user2.getId());
        assertThat(result, hasSize(1));
        assertThat(result.getFirst().getItems(), hasSize(0));
    }
}
