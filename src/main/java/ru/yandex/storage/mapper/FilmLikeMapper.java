package ru.yandex.storage.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.model.Film;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

@Component
public class FilmLikeMapper implements RowMapper<Film> {

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Set<Long> userLikes = new HashSet<>();
        long id = rs.getLong("id_film");
        while(rs.next()) {
            Long idFilm = rs.getLong("id_film");
            while(id == idFilm) {
                Long idUser = rs.getLong("id_user");
                userLikes.add(idUser);
            }
            break;
        }
        Film.builder().id(id).userLikes(userLikes).build();
        return null;
    }
}
