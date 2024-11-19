package com.noize.medicalcenter.dto.tm;

import javafx.scene.control.Button;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class OrdersTM {
    private int itemId;
    private String name;
    private int cartQty;
    private double unitPrice;
    private double total;
    private Button removeBtn;
}
