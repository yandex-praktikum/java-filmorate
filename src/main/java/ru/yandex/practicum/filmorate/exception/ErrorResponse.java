package ru.yandex.practicum.filmorate.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
// Класс для отправки информации об ошибке клиенту
public class ErrorResponse {
    // Поле для хранения текста ошибки
    private String error;
}
