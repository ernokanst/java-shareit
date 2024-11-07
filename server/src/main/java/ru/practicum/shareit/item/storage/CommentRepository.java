package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Comment;
import java.util.*;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findByItemId(int id);

    List<Comment> findByItemOwnerId(int id);
}
