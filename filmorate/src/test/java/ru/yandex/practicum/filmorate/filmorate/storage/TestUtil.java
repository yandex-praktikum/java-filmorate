package ru.yandex.practicum.filmorate.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;

public class TestUtil {
    private  final JdbcTemplate jdbcTemplate;

    public TestUtil(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void clean(){
        String sql = "DROP TABLE IF EXISTS FILM RESTRICT ;" +
                "DROP TABLE IF EXISTS USER RESTRICT;";
        jdbcTemplate.update(sql);
    }

    public void addUser(){
        String sql = "INSERT INTO USER(USER_NAME, EMAIL, LOGIN, BIRTHDAY) VALUES ( 'dima', 'l@d.com', 'dz', '1994-03-03' );";
        jdbcTemplate.update(sql);
    }
}
