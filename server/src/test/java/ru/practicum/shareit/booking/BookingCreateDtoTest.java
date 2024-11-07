package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingCreateDtoTest {
    private final JacksonTester<BookingCreateDto> json;

    @Test
    void testBookingCreateDto() throws Exception {
        BookingCreateDto booking = new BookingCreateDto(
                1,
                LocalDateTime.parse("2025-01-01T12:34:56"),
                LocalDateTime.parse("2025-01-03T12:00:00"),
                2,
                3,
                "WAITING");

        JsonContent<BookingCreateDto> result = json.write(booking);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo("2025-01-01T12:34:56");
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo("2025-01-03T12:00:00");
        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(2);
        assertThat(result).extractingJsonPathNumberValue("$.booker").isEqualTo(3);
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo("WAITING");
    }
}
