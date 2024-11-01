package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Comment;
import java.util.*;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findByItemId(int id);

    List<Comment> findByItemOwnerId(int id);

    default Map<Integer, List<Comment>> findAllGroupById(int userId) {
        List<Comment> allComments = findByItemOwnerId(userId);
        Map<Integer, List<Comment>> result = new HashMap<>();
        for (Comment c : allComments) {
            if (!(result.containsKey(c.getItem().getId()))) {
                result.put(c.getItem().getId(), new ArrayList<>());
            }
            result.get(c.getItem().getId()).add(c);
        }
        return result;
    }
}
