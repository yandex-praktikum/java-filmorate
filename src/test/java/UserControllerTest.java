import filmorate.controller.UserController;
import filmorate.exception.ResourceException;
import filmorate.exception.ValidationException;
import filmorate.models.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {
    private static final UserController USER_CONTROLLER = new UserController();
    private final User user = new User("fiiinko@mail.ru", "finko",
            "Sofya", "2000-09-21");

    @Test
    public void addUserTest() throws ValidationException {
        USER_CONTROLLER.addUser(user);
        assertTrue(USER_CONTROLLER.getAllUsers().contains(user));
    }

    @Test
    public void addUserWithNotCorrectDataTest() {
        final User wrongUser = new User("fiiinko@mail.ru", "finko",
                "Sofya", "2025-09-21");
        Assertions.assertThrows(ValidationException.class, () -> USER_CONTROLLER.addUser(wrongUser));
    }

    @Test
    public void updateUserTest() throws ValidationException {
        User addUser = USER_CONTROLLER.addUser(user);
        User userForUpdate = new User(addUser.getId(), "fiiinko@mail.ru", "finko",
                "Sonya", "2000-09-21");
        USER_CONTROLLER.updateUser(userForUpdate);
        assertEquals(USER_CONTROLLER.getAllUsers().get(addUser.getId()-1), userForUpdate);
    }

    @Test
    public void getAllUsersTest() {
        assertNotNull(USER_CONTROLLER.getAllUsers());
    }
}

