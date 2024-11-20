package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository requestRepository;
    private final ItemRequestMapper requestMapper;
    private final ItemRepository itemRepository;

    public ItemRequestDto create(ItemRequestDto request, int userId) {
        request.setRequester(userId);
        request.setCreated(LocalDateTime.now());
        ItemRequest ir = requestRepository.save(requestMapper.toItemRequest(request));
        return requestMapper.toItemRequestDto(ir, itemRepository.findByRequestId(ir.getId()));
    }

    public List<ItemRequestDto> getFromUser(int userId) {
        Map<Integer, List<Item>> items = findItemsGroupByRequestId();
        return requestRepository.findByRequester_Id(userId, Sort.by(Sort.Direction.DESC, "created"))
                .stream()
                .map(x -> requestMapper.toItemRequestDto(x, items.containsKey(x.getId()) ? items.get(x.getId()) : new ArrayList<>()))
                .toList();
    }

    public List<ItemRequestDto> getAll() {
        Map<Integer, List<Item>> items = findItemsGroupByRequestId();
        return requestRepository.findAll(Sort.by(Sort.Direction.DESC, "created"))
                .stream()
                .map(x -> requestMapper.toItemRequestDto(x, items.containsKey(x.getId()) ? items.get(x.getId()) : new ArrayList<>()))
                .toList();
    }

    public ItemRequestDto get(int requestId) {
        return requestMapper.toItemRequestDto(requestRepository.findById(requestId).orElseThrow(), itemRepository.findByRequestId(requestId));
    }

    private Map<Integer, List<Item>> findItemsGroupByRequestId() {
        List<Item> itemsWithRequest = itemRepository.findByRequestIdNotNull();
        Map<Integer, List<Item>> result = new HashMap<>();
        for (Item i : itemsWithRequest) {
            if (!(result.containsKey(i.getRequest().getId()))) {
                result.put(i.getRequest().getId(), new ArrayList<>());
            }
            result.get(i.getRequest().getId()).add(i);
        }
        return result;
    }
}
