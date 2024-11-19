package com.noize.medicalcenter.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor

public class OrdersFormDto {
    private int orderId;
    private Date orderDate;
    private int patientId;

    private ArrayList<OrderDetailsFormDto> orderDetailsFormDtos;
}
