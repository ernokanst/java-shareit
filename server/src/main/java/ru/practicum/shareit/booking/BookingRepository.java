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

    List<Booking> findByItemOwner_Id(Integer ownerId, Sort sort);

    List<Booking> findByItemOwner_IdAndEndIsBefore(Integer ownerId, LocalDateTime end, Sort sort);

    List<Booking> findByItemOwner_IdAndStartIsAfter(Integer ownerId, LocalDateTime start, Sort sort);

    List<Booking> findByItemOwner_IdAndStartIsBeforeAndEndIsAfter(Integer ownerId, LocalDateTime start,
                                                                  LocalDateTime end, Sort sort);

    List<Booking> findByItemOwner_IdAndStatusIs(Integer ownerId, BookingStatus status, Sort sort);

    Booking findFirstByItem_IdAndEndIsBefore(Integer itemId, LocalDateTime end, Sort sort);

    Booking findFirstByItem_IdAndStartIsAfter(Integer itemId, LocalDateTime start, Sort sort);

    List<Booking> findByBooker_IdAndItem_IdIsAndStatusIsAndEndIsBefore(Integer bookerId, Integer itemId,
                                                                       BookingStatus status, LocalDateTime end);

    List<Booking> findByItemOwnerId(int id);

    default Map<Integer, List<Booking>> findLastAndNext(int id) {
        List<Booking> bookings = findByItemOwnerId(id);
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
