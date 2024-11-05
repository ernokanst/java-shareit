package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.validation.DateRange;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@DateRange(before = "start", after = "end")
public class BookingCreateDto {
    private Integer id;
    @FutureOrPresent
    private LocalDateTime start;
    @Future
    private LocalDateTime end;
    private Integer itemId;
    private Integer booker;
    private String status;
}
