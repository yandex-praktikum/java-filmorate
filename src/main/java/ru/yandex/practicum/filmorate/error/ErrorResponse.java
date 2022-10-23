package ru.yandex.practicum.filmorate.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse{
    public String error;
    public String description;
}
