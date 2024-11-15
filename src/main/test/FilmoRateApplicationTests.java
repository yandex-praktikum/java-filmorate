import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.model.User;
import ru.yandex.storage.UserDbStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserDbStorage.class})
@Sql({"schema.sql", "test-data.sql"})
class FilmoRateApplicationTests {
    private final UserDbStorage userStorage;

    @Test
    public void testAllUsers() {

        Optional<Collection<User>> userOptional = Optional.of(userStorage.allUsers());

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(users ->
                        assertThat(users).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    public void testDeleteUser() {
        User user = User.builder().name("Ivan").email("sid.i744@yandex.ru").login("@ivanLead").id(1L).
                birthday(LocalDate.parse("2004-04-07")).build();
        userStorage.deleteUser(user);
        assertNull(userStorage.allUsers());
    }

    @Test
    public void testCreateUser() {

        User user = User.builder().name("Michail").email("sid.i744@yandex.ru").login("@ivanLead").id(2L).
                birthday(LocalDate.parse("2004-04-07")).build();
        Optional<User> userOptional = Optional.of(user);
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(el ->
                        assertThat(el).hasFieldOrPropertyWithValue("id", 2)
                );
    }

    @Test
    public void testUpdateUser() {
        User user = User.builder().name("Michail").email("sid.i744@yandex.ru").login("@ivanLead").id(1L).
                birthday(LocalDate.parse("2004-04-07")).build();
        Optional<User> userOptional = Optional.of(userStorage.updateUser(user));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(el ->
                        assertThat(el).hasFieldOrPropertyWithValue("id", 1)
                );
    }

}