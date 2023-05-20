package ru.job4j.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.job4j.domain.Status;

@Data
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OrderStatusDTO {

    @EqualsAndHashCode.Include
    private int orderId;

    private Status status;
}
