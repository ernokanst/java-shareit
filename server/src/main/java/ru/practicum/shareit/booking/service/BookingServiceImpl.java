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
import java.util.List;

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
                found = bookingRepository.findByItemOwner_IdAndStartIsBeforeAndEndIsAfter(userId,
                        LocalDateTime.now(), LocalDateTime.now(), Sort.by(Sort.Direction.DESC, "start"));
                break;
            }
            case PAST -> {
                found = bookingRepository.findByItemOwner_IdAndEndIsBefore(userId, LocalDateTime.now(),
                        Sort.by(Sort.Direction.DESC, "start"));
                break;
            }
            case FUTURE -> {
                found = bookingRepository.findByItemOwner_IdAndStartIsAfter(userId, LocalDateTime.now(),
                        Sort.by(Sort.Direction.DESC, "start"));
                break;
            }
            case WAITING -> {
                found = bookingRepository.findByItemOwner_IdAndStatusIs(userId, BookingStatus.WAITING,
                        Sort.by(Sort.Direction.DESC, "start"));
                break;
            }
            case REJECTED -> {
                found = bookingRepository.findByItemOwner_IdAndStatusIs(userId, BookingStatus.REJECTED,
                        Sort.by(Sort.Direction.DESC, "start"));
                break;
            }
            default -> {
                found = bookingRepository.findByItemOwner_Id(userId, Sort.by(Sort.Direction.DESC,
                        "start"));
                break;
            }
        }
        return found.stream().map(bookingMapper::toBookingDto).toList();
    }
}
