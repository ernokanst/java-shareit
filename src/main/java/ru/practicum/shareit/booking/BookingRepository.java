package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findByBooker_IdOrderByStartDesc(Integer bookerId);

    List<Booking> findByBooker_IdAndEndIsBeforeOrderByStartDesc(Integer bookerId, LocalDateTime end);

    List<Booking> findByBooker_IdAndStartIsAfterOrderByStartDesc(Integer bookerId, LocalDateTime start);

    List<Booking> findByBooker_IdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(Integer bookerId, LocalDateTime start,
                                                                               LocalDateTime end);

    List<Booking> findByBooker_IdAndStatusIsOrderByStartDesc(Integer bookerId, BookingStatus status);

    List<Booking> findByItemOwner_IdOrderByStartDesc(Integer bookerId);

    List<Booking> findByItemOwner_IdAndEndIsBeforeOrderByStartDesc(Integer bookerId, LocalDateTime end);

    List<Booking> findByItemOwner_IdAndStartIsAfterOrderByStartDesc(Integer bookerId, LocalDateTime start);

    List<Booking> findByItemOwner_IdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(Integer bookerId, LocalDateTime start,
                                                                                  LocalDateTime end);

    List<Booking> findByItemOwner_IdAndStatusIsOrderByStartDesc(Integer bookerId, BookingStatus status);

    List<Booking> findByItem_IdAndEndIsBeforeOrderByEndDesc(Integer bookerId, LocalDateTime end);

    List<Booking> findByItem_IdAndStartIsAfterOrderByStartAsc(Integer bookerId, LocalDateTime start);

    List<Booking> findByBooker_IdAndItem_IdIsAndStatusIsAndEndIsBefore(Integer bookerId, Integer itemId,
                                                                       BookingStatus status, LocalDateTime end);
}
