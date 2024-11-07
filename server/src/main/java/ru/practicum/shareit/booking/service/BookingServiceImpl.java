package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.storage.UserRepository;
import ru.practicum.shareit.item.model.Item;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingMapper bookingMapper;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public BookingDto add(BookingCreateDto booking, int userId) {
        Booking b = bookingMapper.toBooking(booking);
        b.setBooker(userRepository.findById(userId).orElseThrow());
        Item item = itemRepository.findById(b.getItem().getId()).orElseThrow();
        if (!(item.isAvailable())) {
            throw new ValidationException("Вещь недоступна");
        }
        b.setItem(item);
        return bookingMapper.toBookingDto(bookingRepository.save(b));
    }

    @Override
    public BookingDto approve(int userId, int bookingId, boolean approved) {
        Booking b = bookingRepository.findById(bookingId).orElseThrow();
        if (itemRepository.findById(b.getItem().getId()).orElseThrow().getOwner().getId() != userId) {
            throw new ValidationException("Пользователь не является владельцем вещи", b);
        }
        if (approved) {
            b.setStatus(BookingStatus.APPROVED);
        } else {
            b.setStatus(BookingStatus.REJECTED);
        }
        return bookingMapper.toBookingDto(bookingRepository.save(b));
    }

    @Override
    public BookingDto get(int bookingId, int userId) {
        Booking b = bookingRepository.findById(bookingId).orElseThrow();
        if (b.getBooker().getId() != userId && b.getItem().getOwner().getId() != userId) {
            throw new ValidationException("Пользователь не является владельцем или заказчиком");
        }
        return bookingMapper.toBookingDto(b);
    }

    @Override
    public List<BookingDto> getAll(BookingState state, int userId) {
        if (!(userRepository.existsById(userId))) {
            throw new NotFoundException("Пользователь не найден", userId);
        }
        List<Booking> found;
        switch (state) {
            case CURRENT -> {
                found = bookingRepository.findByBooker_IdAndStartIsBeforeAndEndIsAfter(userId,
                        LocalDateTime.now(), LocalDateTime.now(), Sort.sort(Booking.class));
                break;
            }
            case PAST -> {
                found = bookingRepository.findByBooker_IdAndEndIsBefore(userId, LocalDateTime.now(),
                        Sort.by(Sort.Direction.DESC, "start"));
                break;
            }
            case FUTURE -> {
                found = bookingRepository.findByBooker_IdAndStartIsAfter(userId, LocalDateTime.now(),
                        Sort.by(Sort.Direction.DESC, "start"));
                break;
            }
            case WAITING -> {
                found = bookingRepository.findByBooker_IdAndStatusIs(userId, BookingStatus.WAITING,
                        Sort.by(Sort.Direction.DESC, "start"));
                break;
            }
            case REJECTED -> {
                found = bookingRepository.findByBooker_IdAndStatusIs(userId, BookingStatus.REJECTED,
                        Sort.by(Sort.Direction.DESC, "start"));
                break;
            }
            default -> {
                found = bookingRepository.findByBooker_Id(userId, Sort.by(Sort.Direction.DESC, "start"));
                break;
            }
        }
        return found.stream().map(bookingMapper::toBookingDto).toList();
    }

    @Override
    public List<BookingDto> getAllOwner(BookingState state, int userId) {
        if (!(userRepository.existsById(userId))) {
            throw new NotFoundException("Пользователь не найден", userId);
        }
        List<Booking> found;
        switch (state) {
            case CURRENT -> {
                found = bookingRepository.findByItemOwnerIdAndStartIsBeforeAndEndIsAfter(userId,
                        LocalDateTime.now(), LocalDateTime.now(), Sort.by(Sort.Direction.DESC, "start"));
                break;
            }
            case PAST -> {
                found = bookingRepository.findByItemOwnerIdAndEndIsBefore(userId, LocalDateTime.now(),
                        Sort.by(Sort.Direction.DESC, "start"));
                break;
            }
            case FUTURE -> {
                found = bookingRepository.findByItemOwnerIdAndStartIsAfter(userId, LocalDateTime.now(),
                        Sort.by(Sort.Direction.DESC, "start"));
                break;
            }
            case WAITING -> {
                found = bookingRepository.findByItemOwnerIdAndStatusIs(userId, BookingStatus.WAITING,
                        Sort.by(Sort.Direction.DESC, "start"));
                break;
            }
            case REJECTED -> {
                found = bookingRepository.findByItemOwnerIdAndStatusIs(userId, BookingStatus.REJECTED,
                        Sort.by(Sort.Direction.DESC, "start"));
                break;
            }
            default -> {
                found = bookingRepository.findByItemOwnerId(userId, Sort.by(Sort.Direction.DESC,
                        "start"));
                break;
            }
        }
        return found.stream().map(bookingMapper::toBookingDto).toList();
    }

    @Override
    public Map<Integer, List<Booking>> findLastAndNext(int id) {
        List<Booking> bookings = bookingRepository.findByItemOwnerId(id);
        Map<Integer, List<Booking>> result = new HashMap<>();
        for (Booking b : bookings) {
            int bId = b.getItem().getId();
            if (!(result.containsKey(bId))) {
                result.put(bId, new ArrayList<>(2));
            }
            if (b.getEnd().isBefore(LocalDateTime.now())) {
                if (result.get(bId).getFirst() == null || result.get(bId).getFirst().getEnd().isBefore(b.getEnd())) {
                    result.get(bId).addFirst(b);
                }
            }
            if (b.getStart().isAfter(LocalDateTime.now())) {
                if (result.get(bId).getLast() == null || result.get(bId).getLast().getStart().isAfter(b.getStart())) {
                    result.get(bId).addLast(b);
                }
            }
        }
        return result;
    }
}
