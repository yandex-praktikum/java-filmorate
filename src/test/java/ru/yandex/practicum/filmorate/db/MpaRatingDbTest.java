package ru.yandex.practicum.filmorate.db;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import ru.yandex.practicum.filmorate.model.film.MpaRating;
import ru.yandex.practicum.filmorate.storage.film.MpaDbStorage;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase(replace = Replace.ANY)
@Import(MpaDbStorage.class)
@Sql(scripts = {"classpath:schema.sql", "classpath:testdata-h2.sql"},
        executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
class MpaRatingDbTest {

    @org.springframework.beans.factory.annotation.Autowired
    private MpaDbStorage mpaStorage;

    @Test
    void findAll_shouldReturnKnownOrderedList() {
        List<MpaRating> all = new ArrayList<>(mpaStorage.findAll());
        assertThat(all).extracting(MpaRating::getId).containsExactly(1, 2, 3, 4, 5);
    }

    @Test
    void findById_whenExists_returnsEntity() {
        MpaRating r = mpaStorage.findById(2).orElseThrow();
        assertThat(r.getId()).isEqualTo(2);
        assertThat(r.getName()).isNotBlank();
    }

    @Test
    void findById_whenNotExists_returnsEmpty() {
        assertThat(mpaStorage.findById(999)).isEmpty();
    }
}
