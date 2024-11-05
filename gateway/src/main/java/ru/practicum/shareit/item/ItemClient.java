package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> add(ItemDto item, int userId) {
        return post("", userId, item);
    }

    public ResponseEntity<Object> update(ItemDto item, int userId) {
        return patch("", userId, item);
    }

    public ResponseEntity<Object> get(int userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> getItem(int id, int userId) {
        return get("/" + id, userId);
    }

    public ResponseEntity<Object> deleteItem(int id) {
        return delete("/" + id);
    }

    public ResponseEntity<Object> search(String text) {
        return get("/search?text=" + text);
    }

    public ResponseEntity<Object> comment(CommentDto c, int itemId, int userId) {
        return post("/" + itemId + "/comment", userId, c);
    }

}
