package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.ItemStorage;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserStorage;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ItemServiceImpl implements ItemService {
    private ItemStorage itemStorage;
    private UserStorage userStorage;

    @Autowired
    public ItemServiceImpl(ItemStorage itemStorage, UserStorage userStorage) {
        this.itemStorage = itemStorage;
        this.userStorage = userStorage;
    }

    @Override
    public Item add(Item item, Integer userId) {
        item.setOwner(userStorage.get(userId));
        checkItem(item);
        return itemStorage.add(item);
    }

    @Override
    public Item update(Item item, Integer id, Integer userId) {
        if (itemStorage.get(id) == null) {
            throw new NotFoundException("Вещь не найдена");
        }
        if (!Objects.equals(itemStorage.get(id).getOwner().getId(), userId)) {
            throw new NotFoundException("Пользователь не соответствует владельцу вещи");
        }
        return itemStorage.update(item, id);
    }

    @Override
    public List<Item> get(Integer userId) {
        return itemStorage.getFromUser(userId);
    }

    @Override
    public Item getItem(Integer id) {
        if (itemStorage.get(id) == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        return itemStorage.get(id);
    }

    @Override
    public void deleteItem(Integer id) {
        itemStorage.delete(id);
    }

    @Override
    public List<Item> search(String query) {
        if (query.isBlank()) {
            return new ArrayList<>();
        }
        return itemStorage.search(query);
    }

    public void checkItem(Item item) {
        if (item.getOwner() == null || userStorage.get(item.getOwner().getId()) == null) {
            throw new NotFoundException("Владелец указан неверно");
        }
        if (item.getName() == null || item.getName().isBlank()) {
            throw new ValidationException("Название не указано");
        }
        if (item.getDescription() == null || item.getDescription().isBlank()) {
            throw new ValidationException("Описание не указано");
        }
        if (item.getAvailable() == null) {
            throw new ValidationException("Не указан статус вещи");
        }
    }
}
