package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTest {
    private Validator validator;

    @BeforeEach
    public void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    private static User getUserWithSettings(String change, String value) {
        final User user = User.builder()
                .id(1)
                .email("king@mail.ru")
                .login("King")
                .name("Dmitry")
                .birthday("1996-11-02")
                .build();

        switch (change) {
            case "email":
                user.setEmail(value);
                break;
            case "login":
                user.setLogin(value);
                break;
            case "name":
                user.setName(value);
                break;
            case "birthday":
                user.setBirthday(value);
                break;
        }
        return user;
    }

    private static Stream<Arguments> provideUserAndConstraint() {
        return Stream.of(
                Arguments.of(getUserWithSettings("email", "king@mail.ru"), 0),
                Arguments.of(getUserWithSettings("email", ""), 1),
                Arguments.of(getUserWithSettings("email", null), 1),
                Arguments.of(getUserWithSettings("email", "king.mail.ru"), 1),
                Arguments.of(getUserWithSettings("login", "King"), 0),
                Arguments.of(getUserWithSettings("login", " "), 1),
                Arguments.of(getUserWithSettings("login", null), 1),
                Arguments.of(getUserWithSettings("name", "Dmitry"), 0),
                Arguments.of(getUserWithSettings("name", ""), 0),
                Arguments.of(getUserWithSettings("name", null), 0),
                Arguments.of(getUserWithSettings("birthday", LocalDate.now().toString()), 1),
                Arguments.of(getUserWithSettings("birthday", LocalDate.now().plusDays(1).toString()), 1),
                Arguments.of(getUserWithSettings("birthday", LocalDate.now().minusDays(1).toString()), 0)
        );
    }

    @ParameterizedTest
    @MethodSource("provideUserAndConstraint")
    void userValidationTest(User user, int constraint) {
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(constraint, violations.size(), "User не прошел валидацию");
    }
}