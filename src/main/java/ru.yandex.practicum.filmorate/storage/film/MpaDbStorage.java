package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.film.MpaRating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
@Profile("dbFilms")
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDbStorage(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<MpaRating> findAll() {
        String sql = "SELECT id, name FROM mpa_ratings ORDER BY id";
        return jdbcTemplate.query(sql, this::map);
    }

    @Override
    public Optional<MpaRating> findById(int id) {
        String sql = "SELECT id, name FROM mpa_ratings WHERE id = ?";
        List<MpaRating> result = jdbcTemplate.query(sql, this::map, id);
        return result.stream()
                .findFirst();
    }

    @Override
    public boolean existsById(int id) {
        Boolean x = jdbcTemplate.queryForObject(
                "SELECT EXISTS(SELECT 1 FROM mpa_ratings WHERE id = ?)",
                Boolean.class, id);
        return x;
    }


    private MpaRating map(ResultSet rs, int rowNum) throws SQLException {
        return MpaRating.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .build();
    }
}
