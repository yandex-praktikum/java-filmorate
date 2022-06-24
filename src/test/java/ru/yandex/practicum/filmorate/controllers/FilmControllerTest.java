package ru.yandex.practicum.filmorate.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
class FilmControllerTest {

    @Autowired
    ObjectMapper mapper;
    @Autowired
    MockMvc mockMvc;

    @ParameterizedTest
    @MethodSource("validObjectFactory")
    void filmValidationOkTest(Film film) throws Exception {
        this.mockMvc.perform(post("/films")
                        .content(mapper.writeValueAsString(film))
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @MethodSource("invalidObjectFactory")
    void filmValidationNotOkTest(Film film) throws Exception {
        this.mockMvc.perform(post("/films")
                        .content(mapper.writeValueAsString(film))
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    static Stream<Film> validObjectFactory() {
        return Stream.of(
            new Film(null, "Oreshek", "Big boy", LocalDate.of(1999,1,1), 123),
            new Film(null, "Oreshek", "Big boy", LocalDate.of(1895,12,28), 123),
            new Film(null, "Oreshek", "Big boy", LocalDate.of(1999,1,1), 123)
        );
    }

    static Stream<Film> invalidObjectFactory() {
        return Stream.of(
                new Film(null, "", "Empty name", LocalDate.of(1999,1,1), 123),
                new Film(null, "Empty description", "", LocalDate.of(1999,1,1), 123),
                new Film(null, "Description 200+", "Sddnvlkdshfvskdjfksikhfunsdcwjhikjemkfjdsjvfisjnvujshosiufhiuerhfpoerjfverbvefddfshfslnfsnvofedhvuofshjmochdlhdjmcfjdbmgkdjdgfjcmdgfjhdvkfjgfhgfgfdgfvvtvrwvcgsvcgvcsfdchcgfcxhwfuxngbgbfdvgsvfjhgvhfdbdsss", LocalDate.of(1999,1,1), 123),
                new Film(null, "Date", "Date is before 28.12.1895", LocalDate.of(1895,12,27), 123),
                new Film(null, "Duration", "Duration is zero", LocalDate.of(1895,12,27), 0)
        );
    }
}