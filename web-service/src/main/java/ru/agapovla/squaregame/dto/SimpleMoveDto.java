package ru.agapovla.squaregame.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimpleMoveDto {
    private int x;
    private int y;
    private String color;
}
