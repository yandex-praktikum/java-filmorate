package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;

@Service
public class IdGeneratorUser {

    private int id = 0;

    public int generate() {
        id = id+1;
        return id;
    }
}
