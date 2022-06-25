package ru.yandex.practicum.filmorate.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.filmorate.storage.interfaces.MpaStorage;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Mpa> getMpa() {
        String sql = "SELECT RATING_ID, RATING_NAME FROM RATING";
        SqlRowSet ratingRows = jdbcTemplate.queryForRowSet(sql);
        List<Mpa> genres = new ArrayList<>();
        while (ratingRows.next()) {
            genres.add(new Mpa(
                    ratingRows.getInt("RATING_ID"),
                    ratingRows.getString("RATING_NAME")
            ));
        }
        return genres;
    }

    @Override
    public Mpa getMpaById(int id) {
        String sql = "SELECT RATING_ID, RATING_NAME FROM RATING WHERE RATING_ID = ?";
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet(sql, id);
        Mpa mpa = null;

        if (mpaRows.next()) {
            mpa = new Mpa(
                    mpaRows.getInt("RATING_ID"),
                    mpaRows.getString("RATING_NAME")
            );
            log.info("Найден mpa: {}{}", mpa.getId(), mpa.getName());
        } else {
            log.info("Mpa с идентификатором: {} не найден", id);
            throw new ObjectNotFoundException("Указанного mpa не существует");
        }
        return mpa;
    }
}
