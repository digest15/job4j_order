package ru.job4j.client;

import ru.job4j.domain.dto.DishDTO;

public interface DishClient {
    DishDTO getDishByName(String name);
}
