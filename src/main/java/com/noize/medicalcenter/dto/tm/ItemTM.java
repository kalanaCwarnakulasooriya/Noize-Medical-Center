package com.noize.medicalcenter.dto.tm;

import lombok.*;

import javafx.scene.image.ImageView;
import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor

public class ItemTM  {
    private String name;
    private String description;
    private String expireDate;
    private String packSize;
    private double unitPrice;
    private int stockQty;
    private ImageView type;
}
