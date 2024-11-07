package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDtoWithDates;
import java.time.LocalDateTime;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemDtoWithDatesTest {
    private final JacksonTester<ItemDtoWithDates> json;

    @Test
    void testItemDto() throws Exception {
        ItemDtoWithDates item = new ItemDtoWithDates(
                1,
                "Item",
                "Description",
                true,
                List.of(new CommentDto(1, "Good", "John Doe", "2024-01-01T12:34:56")),
                new BookingDto(
                        1,
                        LocalDateTime.parse("2024-11-01T12:34:56"),
                        LocalDateTime.parse("2024-11-03T11:11:11"),
                        null,
                        null,
                        null),
                new BookingDto(
                        2,
                        LocalDateTime.parse("2024-12-01T12:34:56"),
                        LocalDateTime.parse("2024-12-03T11:11:11"),
                        null,
                        null,
                        null),
                1);

        JsonContent<ItemDtoWithDates> result = json.write(item);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Item");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Description");
        assertThat(result).extractingJsonPathStringValue("$.comments[0].text").isEqualTo("Good");
        assertThat(result).extractingJsonPathStringValue("$.comments[0].authorName").isEqualTo("John Doe");
        assertThat(result).extractingJsonPathStringValue("$.lastBooking.start").isEqualTo("2024-11-01T12:34:56");
        assertThat(result).extractingJsonPathStringValue("$.lastBooking.end").isEqualTo("2024-11-03T11:11:11");
        assertThat(result).extractingJsonPathStringValue("$.nextBooking.start").isEqualTo("2024-12-01T12:34:56");
        assertThat(result).extractingJsonPathStringValue("$.nextBooking.end").isEqualTo("2024-12-03T11:11:11");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Description");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(1);
    }
}
