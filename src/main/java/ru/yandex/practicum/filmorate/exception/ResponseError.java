package ru.yandex.practicum.filmorate.exception;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ResponseError {
    private String message;
}