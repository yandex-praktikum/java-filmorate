package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
// Класс тестов для FilmController
class FilmControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Тест успешного создания фильма
    @Test
    void shouldCreateValidFilm() throws Exception {
        Film film = createFilm();

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Avatar"));
    }

    // Тест проверки пустого названия фильма
    @Test
    void shouldRejectBlankName() throws Exception {
        Film film = createFilm();
        film.setName(" ");

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest());
    }

    // Тест проверки слишком длинного описания
    @Test
    void shouldRejectDescriptionLongerThan200Symbols() throws Exception {
        Film film = createFilm();
        film.setDescription("a".repeat(201));

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest());
    }

    // Тест успешной проверки описания длиной ровно 200 символов
    @Test
    void shouldAcceptDescriptionWith200Symbols() throws Exception {
        Film film = createFilm();
        film.setDescription("a".repeat(200));

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isOk());
    }

    // Тест проверки даты релиза раньше допустимой
    @Test
    void shouldRejectReleaseDateBeforeCinemaBirthday() throws Exception {
        Film film = createFilm();
        film.setReleaseDate(LocalDate.of(1895, 12, 27));

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest());
    }

    // Тест проверки допустимой минимальной даты
    @Test
    void shouldAcceptCinemaBirthdayReleaseDate() throws Exception {
        Film film = createFilm();
        film.setReleaseDate(LocalDate.of(1895, 12, 28));

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isOk());
    }

    // Тест проверки продолжительности фильма
    @Test
    void shouldRejectNonPositiveDuration() throws Exception {
        Film film = createFilm();
        film.setDuration(0);

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest());
    }

    // Тест проверки пустого тела запроса
    @Test
    void shouldRejectEmptyRequestBody() throws Exception {
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest());
    }

    // Тест получения списка фильмов
    @Test
    void shouldReturnFilmsList() throws Exception {
        mockMvc.perform(get("/films"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(org.hamcrest.Matchers.greaterThanOrEqualTo(0))));
    }

    // Вспомогательный метод создания корректного фильма
    private Film createFilm() {
        Film film = new Film();
        film.setName("Avatar");
        film.setDescription("Sci-fi movie");
        film.setReleaseDate(LocalDate.of(2009, 12, 10));
        film.setDuration(162);
        return film;
    }
}
