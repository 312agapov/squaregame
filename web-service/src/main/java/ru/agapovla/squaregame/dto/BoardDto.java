package ru.agapovla.squaregame.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardDto {
    private int size;
    private String data;
    private String nextPlayerColor;
}
