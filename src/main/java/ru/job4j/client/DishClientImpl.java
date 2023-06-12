package ru.job4j.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.job4j.domain.dto.DishDTO;

@Component
@RequiredArgsConstructor
public class DishClientImpl implements DishClient {

    @Value("${api-url}")
    private String url;

    private final RestTemplate client;


    @Override
    public DishDTO getDishByName(String name) {
        return client.getForEntity(
                String.format("%s/%s/%s", url, "name", name),
                DishDTO.class
        ).getBody();

    }
}
