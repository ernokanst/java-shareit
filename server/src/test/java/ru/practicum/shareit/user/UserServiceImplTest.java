package ru.practicum.shareit.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.dto.*;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceImplTest {
    private final EntityManager em;
    private final UserService userService;
    private final UserMapper userMapper;
    private UserDto user1 = new UserDto(1, "John Doe", "noreply@example.com");
    private UserDto user2 = new UserDto(2, "Jane Doe", "email@example.ru");

    @Test
    void testAddGet() {
        UserDto resultService = userService.add(user1);
        user1.setId(resultService.getId());
        TypedQuery<User> query = em.createQuery("Select u from User u where u.id = :id", User.class);
        UserDto resultQuery = userMapper.toUserDto(query.setParameter("id", user1.getId()).getSingleResult());
        assertThat(resultQuery, equalTo(resultService));
        assertThat(resultQuery, equalTo(user1));
        assertThat(userService.getUser(user1.getId()), equalTo(user1));
        UserDto resultService2 = userService.add(user2);
        user2.setId(resultService2.getId());
        assertThat(userService.get(), equalTo(List.of(user1, user2)));
    }

    @Test
    void testUpdate() {
        user1 = userService.add(user1);
        UserUpdateDto upd = new UserUpdateDto(null, "Jane Doe", "email@example.com");
        user2 = userService.add(user2);
        userService.update(upd, user1.getId());
        UserDto result = userService.getUser(user1.getId());
        assertThat(result.getName(), equalTo(upd.getName()));
        assertThat(result.getEmail(), equalTo(upd.getEmail()));
    }
}
