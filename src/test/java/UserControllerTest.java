import filmorate.controller.UserController;
import filmorate.exception.ValidationException;
import filmorate.models.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {
    private static final UserController USER_CONTROLLER = new UserController();
    private final User user = new User("fiiinko@mail.ru", "finko",
            "Sofya", "2000-21-09");

    @Test
    public void addUserTest() throws ValidationException {
        USER_CONTROLLER.addUser(user);
        assertTrue(USER_CONTROLLER.getAllUsers().contains(user));
    }

    @Test
    public void addUserWithNotCorrectDataTest() throws ValidationException {
        final User wrongUser = new User("fiiinko@mail.ru", "finko",
                "Sofya", "2025-21-09");
        USER_CONTROLLER.addUser(wrongUser);
        assertFalse(USER_CONTROLLER.getAllUsers().contains(user));
    }

    @Test
    public void updateUserTest() throws ValidationException {
        User returnUser = USER_CONTROLLER.addUser(user);
        User userForUpdate = new User("fiiinko@mail.ru", "finko",
                "Sonya", "2000-21-09");
        USER_CONTROLLER.updateUser(userForUpdate);
        assertEquals(USER_CONTROLLER.getAllUsers().get(returnUser.getId()), userForUpdate);
    }

    @Test
    public void getAllUsersTest() {
        assertNotNull(USER_CONTROLLER.getAllUsers());
    }
}

