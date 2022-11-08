import filmorate.controller.FilmController;
import filmorate.exception.ValidationException;
import filmorate.models.Film;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FilmControllerTest {

    private static final FilmController FILM_CONTROLLER = new FilmController();
    private final Film film = new Film("Король лев",
            "Король лев, описание", "1995-01-20", 90);

    @Test
    public void addFilmTest() throws ValidationException {
        FILM_CONTROLLER.addFilm(film);
        assertTrue(FILM_CONTROLLER.getAllFilms().contains(film));
    }

    @Test
    public void addFilmTestWithNotCorrectDataTest() throws ValidationException {
        final Film wrongFilm = new Film("Король лев",
                "Король лев, описание", "1995-01-20", -200);
        FILM_CONTROLLER.addFilm(wrongFilm);
        assertFalse(FILM_CONTROLLER.getAllFilms().contains(film));
    }

    @Test
    public void updateFilmTest() throws ValidationException {
        Film returnFilm = FILM_CONTROLLER.addFilm(film);
        Film filmForUpdate = new Film("Король лев",
                "Король лев, описание", "1995-01-20", 87);
        FILM_CONTROLLER.updateFilm(filmForUpdate);
        assertEquals(FILM_CONTROLLER.getAllFilms().get(returnFilm.getId()), filmForUpdate);
    }

    @Test
    public void getAllFilmsTest() {
        assertNotNull(FILM_CONTROLLER.getAllFilms());
    }
}
