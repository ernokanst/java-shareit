package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
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
public class BookingServiceImpl implements BookingService{
    private final BookingMapper bookingMapper;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public Booking add(BookingDto booking, int userId) {
        Booking b = bookingMapper.toBooking(booking);
        b.setBooker(userRepository.findById(userId).orElseThrow());
        Item item = itemRepository.findById(b.getItem().getId()).orElseThrow();
        if (!(item.isAvailable())) {
            throw new ValidationException("Вещь недоступна");
        }
        b.setItem(item);
        return bookingRepository.save(b);
    }

    @Override
    public Booking approve(int userId, int bookingId, boolean approved) {
        Booking b = bookingRepository.findById(bookingId).orElseThrow();
        if (itemRepository.findById(b.getItem().getId()).orElseThrow().getOwner() == userId) {
            if (approved) {
                b.setStatus(BookingStatus.APPROVED);
            } else {
                b.setStatus(BookingStatus.REJECTED);
            }
            return bookingRepository.save(b);
        } else {
            throw new ValidationException("Пользователь не является владельцем вещи", b);
        }
    }

    @Override
    public Booking get(int bookingId, int userId) {
        Booking b = bookingRepository.findById(bookingId).orElseThrow();
        if (b.getBooker().getId() == userId || b.getItem().getOwner() == userId) {
            return b;
        } else {
            throw new ValidationException("Пользователь не является владельцем или заказчиком");
        }
    }

    @Override
    public List<Booking> getAll(BookingState state, int userId) {
        if (!(userRepository.existsById(userId))) {
            throw new NotFoundException("Пользователь не найден", userId);
        }
        switch (state) {
            case CURRENT -> {
                return bookingRepository.findByBooker_IdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(userId, LocalDateTime.now(), LocalDateTime.now());
            }
            case PAST -> {
                return bookingRepository.findByBooker_IdAndEndIsBeforeOrderByStartDesc(userId, LocalDateTime.now());
            }
            case FUTURE -> {
                return bookingRepository.findByBooker_IdAndStartIsAfterOrderByStartDesc(userId, LocalDateTime.now());
            }
            case WAITING -> {
                return bookingRepository.findByBooker_IdAndStatusIsOrderByStartDesc(userId, BookingStatus.WAITING);
            }
            case REJECTED -> {
                return bookingRepository.findByBooker_IdAndStatusIsOrderByStartDesc(userId, BookingStatus.REJECTED);
            }
            default -> {
                return bookingRepository.findByBooker_IdOrderByStartDesc(userId);
            }
        }
    }

    @Override
    public List<Booking> getAllOwner(BookingState state, int userId) {
        if (!(userRepository.existsById(userId))) {
            throw new NotFoundException("Пользователь не найден", userId);
        }
        switch (state) {
            case CURRENT -> {
                return bookingRepository.findByItemOwner_IdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(userId, LocalDateTime.now(), LocalDateTime.now());
            }
            case PAST -> {
                return bookingRepository.findByItemOwner_IdAndEndIsBeforeOrderByStartDesc(userId, LocalDateTime.now());
            }
            case FUTURE -> {
                return bookingRepository.findByItemOwner_IdAndStartIsAfterOrderByStartDesc(userId, LocalDateTime.now());
            }
            case WAITING -> {
                return bookingRepository.findByItemOwner_IdAndStatusIsOrderByStartDesc(userId, BookingStatus.WAITING);
            }
            case REJECTED -> {
                return bookingRepository.findByItemOwner_IdAndStatusIsOrderByStartDesc(userId, BookingStatus.REJECTED);
            }
            default -> {
                return bookingRepository.findByItemOwner_IdOrderByStartDesc(userId);
            }
        }
    }
}
