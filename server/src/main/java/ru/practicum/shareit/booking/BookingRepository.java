package ru.practicum.shareit.booking;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import java.time.LocalDateTime;
import java.util.*;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findByBooker_Id(Integer bookerId, Sort sort);

    List<Booking> findByBooker_IdAndEndIsBefore(Integer bookerId, LocalDateTime end, Sort sort);

    List<Booking> findByBooker_IdAndStartIsAfter(Integer bookerId, LocalDateTime start, Sort sort);

    List<Booking> findByBooker_IdAndStartIsBeforeAndEndIsAfter(Integer bookerId, LocalDateTime start, LocalDateTime end,
                                                               Sort sort);

    List<Booking> findByBooker_IdAndStatusIs(Integer bookerId, BookingStatus status, Sort sort);

    List<Booking> findByItemOwnerId(Integer ownerId, Sort sort);

    List<Booking> findByItemOwnerIdAndEndIsBefore(Integer ownerId, LocalDateTime end, Sort sort);

    List<Booking> findByItemOwnerIdAndStartIsAfter(Integer ownerId, LocalDateTime start, Sort sort);

    List<Booking> findByItemOwnerIdAndStartIsBeforeAndEndIsAfter(Integer ownerId, LocalDateTime start,
                                                                  LocalDateTime end, Sort sort);

    List<Booking> findByItemOwnerIdAndStatusIs(Integer ownerId, BookingStatus status, Sort sort);

    Booking findFirstByItem_IdAndEndIsBefore(Integer itemId, LocalDateTime end, Sort sort);

    Booking findFirstByItem_IdAndStartIsAfter(Integer itemId, LocalDateTime start, Sort sort);

    List<Booking> findByBooker_IdAndItem_IdIsAndStatusIsAndEndIsBefore(Integer bookerId, Integer itemId,
                                                                       BookingStatus status, LocalDateTime end);

    List<Booking> findByItemOwnerId(int id);
}
