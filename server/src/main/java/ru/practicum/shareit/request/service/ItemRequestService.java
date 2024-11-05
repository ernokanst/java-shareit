package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import java.util.List;

public interface ItemRequestService {
    ItemRequestDto create(ItemRequestDto request, int userId);

    List<ItemRequestDto> getFromUser(int userId);

    List<ItemRequestDto> getAll();

    ItemRequestDto get(int requestId);
}
