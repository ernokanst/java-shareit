package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;

import java.time.LocalDateTime;
import java.util.List;

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
        return requestRepository.findByRequester_Id(userId, Sort.by(Sort.Direction.DESC, "created"))
                .stream().map(x -> requestMapper.toItemRequestDto(x, itemRepository.findByRequestId(x.getId()))).toList();
    }

    public List<ItemRequestDto> getAll() {
        return requestRepository.findAll(Sort.by(Sort.Direction.DESC, "created"))
                .stream().map(x -> requestMapper.toItemRequestDto(x, itemRepository.findByRequestId(x.getId()))).toList();
    }

    public ItemRequestDto get(int requestId) {
        return requestMapper.toItemRequestDto(requestRepository.findById(requestId).orElseThrow(), itemRepository.findByRequestId(requestId));
    }
}
