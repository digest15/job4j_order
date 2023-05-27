package ru.job4j.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.job4j.domain.dto.DishDTO;

@Component
public class DishClientImpl implements DishClient {

    @Value("${api-url}")
    private String url;

    private final RestTemplate client;

    public DishClientImpl(RestTemplate client) {
        this.client = client;
    }

    @Override
    public DishDTO getDishByName(String name) {
        return client.getForEntity(
                String.format("%s/%s/%s", url, "name", name),
                DishDTO.class
        ).getBody();
    }
}
