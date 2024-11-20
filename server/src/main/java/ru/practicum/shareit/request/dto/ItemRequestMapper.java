package ru.practicum.shareit.request.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ItemRequestMapper {
    private final ItemMapper itemMapper;

    public ItemRequestDto toItemRequestDto(ItemRequest request, List<Item> items) {
        return new ItemRequestDto(
                request.getId(),
                request.getDescription(),
                request.getRequester().getId(),
                request.getCreated(),
                items.stream().map(x -> itemMapper.toItemDto(x, new ArrayList<>())).toList()
        );
    }

    public ItemRequest toItemRequest(ItemRequestDto request) {
        return new ItemRequest(
                request.getId(),
                request.getDescription(),
                new User(request.getRequester()),
                request.getCreated()
        );
    }
}
