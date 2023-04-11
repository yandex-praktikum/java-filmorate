package ru.yandex.practicum;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import ru.yandex.practicum.controller.FilmController;
import ru.yandex.practicum.controller.UserController;
import ru.yandex.practicum.service.FilmService;
import ru.yandex.practicum.service.UserService;
import ru.yandex.practicum.storage.film.FilmStorage;
import ru.yandex.practicum.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.storage.user.UserStorage;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@SpringBootTest
class JavaFilmorateApplicationTests {
    private FilmController filmController;
    private UserController userController;
    private HttpClient client;
    private ConfigurableApplicationContext ctx;

    @BeforeEach
    void setUp() {
        ctx = SpringApplication.run(JavaFilmorateApplication.class);
        client = HttpClient.newHttpClient();
        FilmStorage filmStorage = new InMemoryFilmStorage();
        filmController = new FilmController(filmStorage, new FilmService(filmStorage));
        UserStorage userStorage = new InMemoryUserStorage();
        userController = new UserController(userStorage, new UserService(userStorage));
    }

    @AfterEach
    void exit() {
        SpringApplication.exit(ctx);
    }

    public HttpResponse<String> filmsResponse(String method, String s) {
        URI url = URI.create("http://localhost:8080/films");
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(s);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .header("Accept", "*/*")
                .method(method, body)
                .build();
        return sendText(request);
    }

    public HttpResponse<String> filmsResponseAdd(String method, String add) {
        URI url = URI.create("http://localhost:8080/films" + add);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(add);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .header("Accept", "*/*")
                .method(method, body)
                .build();
        return sendText(request);
    }

    public HttpResponse<String> usersResponseAdd(String method, String add) {
        URI url = URI.create("http://localhost:8080/users" + add);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(add);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .header("Accept", "*/*")
                .method(method, body)
                .build();
        return sendText(request);
    }

    public HttpResponse<String> usersResponse(String method, String s) {
        URI url = URI.create("http://localhost:8080/users");
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(s);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .header("Accept", "*/*")
                .method(method, body)
                .build();
        return sendText(request);
    }

    public String giveFilm1() {
        return "{\"name\":\"nisieiusmod\",\"description\":\"adipisicing\"," +
                "\"releaseDate\":\"1967-03-25\",\"duration\":100}";
    }

    public String giveFilm2() {
        return "{\"name\":\"nisieiusmod-Extended\",\"description\":\"adipisicing-Extended\"," +
                "\"releaseDate\":\"1968-07-28\",\"duration\":140}";
    }

    public String giveUser1() {
        return "{\n  \"login\": \"dolore\",\n  \"name\": \"Nick Name\"," +
                "\n  \"email\": \"mail@mail.ru\",\n  \"birthday\": \"1946-08-20\"\n}";
    }

    public String giveUser2() {
        return "{\n  \"login\": \"dolore2\",\n  \"name\": \"Nick Name2\"," +
                "\n  \"email\": \"mail2@mail.ru\",\n  \"birthday\": \"1946-08-20\"\n}";
    }

    @Test
    public void getAllFilmsTest() {
        filmsResponse("POST", giveFilm1());
        filmsResponse("POST", giveFilm2());

        HttpResponse<String> response = filmsResponse("GET", "");
        JsonArray jsonArray = JsonParser.parseString(response.body()).getAsJsonArray();

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(2, jsonArray.size());
    }

    @Test
    public void addFilmInvalidReleaseDataTest() {
        String invalidDateFilm = "{\"name\":\"Name\",\"description\":\"Description\"," +
                "\"releaseDate\":\"1890-03-25\",\"duration\":200}";

        HttpResponse<String> response = filmsResponse("POST", invalidDateFilm);

        Assertions.assertEquals(500, response.statusCode());
        Assertions.assertTrue(filmController.findAll().isEmpty());
    }

    @Test
    void addFilmEmptyNameTest() {
        String emptyNameFilm = "{\"name\":\"\",\"description\":\"Description\"," +
                "\"releaseDate\":\"1900-03-25\",\"duration\":200}";

        HttpResponse<String> response = filmsResponse("POST", emptyNameFilm);

        Assertions.assertEquals(400, response.statusCode());
        Assertions.assertTrue(filmController.findAll().isEmpty());
    }

    @Test
    public void addFilmMaxDescriptionTest() {
        String longDescriptionFilm = "{\"name\":\"Filmname\",\"description\":\"Пятеродрузей(комик-группа«Шарло»)," +
                "приезжаютвгородБризуль.ЗдесьонихотятразыскатьгосподинаОгюстаКуглова,которыйзадолжалимденьги," +
                "аименно20миллионов.оКуглов,которыйзавремя«своегоотсутствия»,сталкандидатомКоломбани.\"," +
                "\"releaseDate\":\"1900-03-25\",\"duration\":200}";

        HttpResponse<String> response = filmsResponse("POST", longDescriptionFilm);

        Assertions.assertEquals(400, response.statusCode());
        Assertions.assertTrue(filmController.findAll().isEmpty());
    }

    @Test
    public void addFilmNegativeDurationTest() {
        String invalidDuration = "{\"name\":\"Name\",\"description\":\"Descrition\"," +
                "\"releaseDate\":\"1980-03-25\",\"duration\":-200}";

        HttpResponse<String> response = filmsResponse("POST", invalidDuration);

        Assertions.assertEquals(400, response.statusCode());
        Assertions.assertTrue(filmController.findAll().isEmpty());
    }

    @Test
    void addFilmShouldAddTest() {
        HttpResponse<String> response = filmsResponse("POST", giveFilm1());
        JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(1, jsonObject.get("id").getAsInt());
    }

    @Test
    public void updateFilmNotFoundTest() {
        String film = "{\"id\":9999,\"name\":\"FilmUpdated\",\"releaseDate\":\"1989-04-17\"," +
                "\"description\":\"Newfilmupdatedecription\",\"duration\":190,\"rate\":4}";

        HttpResponse<String> response = filmsResponse("PUT", film);
        Assertions.assertEquals(404, response.statusCode());
    }

    @Test
    public void updateFilmInvalidReleaseDataTest() {
        String film = "{\"name\":\"Name\",\"description\":\"Description\"," +
                "\"releaseDate\":\"1904-08-20\",\"duration\":200}";
        String invalidDateFilm = "{\"name\":\"Name\",\"description\":\"Description\"," +
                "\"releaseDate\":\"1890-03-25\",\"duration\":200}";

        filmsResponse("POST", film);
        HttpResponse<String> response = filmsResponse("PUT", invalidDateFilm);
        JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();

        Assertions.assertEquals(500, response.statusCode());
        Assertions.assertFalse(jsonObject.has("releaseDate"));
    }

    @Test
    public void updateFilmShouldWorkTest() {
        String film = "{\"name\":\"Name\",\"description\":\"Description\"," +
                "\"releaseDate\":\"1904-08-20\",\"duration\":200}";
        String updateFilm = "{\"id\":1,\"name\":\"FilmUpdated\",\"releaseDate\":\"1906-11-25\"," +
                "\"description\":\"Newfilmupdatedecription\",\"duration\":190,\"rate\":4}";

        filmsResponse("POST", film);
        HttpResponse<String> response = filmsResponse("PUT", updateFilm);
        JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
        String updatedDate = jsonObject.get("releaseDate").toString();

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertTrue(jsonObject.has("releaseDate"));
        Assertions.assertEquals("\"1906-11-25\"", updatedDate);
    }

    @Test
    public void createUserBlankLoginTest() {
        String user = "{\n  \"login\": \"\",\n  \"name\": \"Nick Name\"," +
                "\n  \"email\": \"mail@mail.ru\",\n  \"birthday\": \"1996-08-20\"\n}";

        HttpResponse<String> response = usersResponse("POST", user);

        Assertions.assertEquals(400, response.statusCode());
        Assertions.assertTrue(userController.findAll().isEmpty());
    }

    @Test
    public void createUserIncorrectLoginTest() {
        String user = "{\n  \"login\": \"dolore ullamco\"," +
                "\n  \"email\": \"yandex@mail.ru\",\n  \"birthday\": \"1995-08-20\"\n}";

        HttpResponse<String> response = usersResponse("POST", user);

        Assertions.assertEquals(500, response.statusCode());
        Assertions.assertTrue(userController.findAll().isEmpty());
    }

    @Test
    public void createUserWithEmptyNameTest() {
        String user = "{\n  \"login\": \"common\"," +
                "\n  \"email\": \"friend@common.ru\",\n  \"birthday\": \"2000-08-20\"\n}";

        HttpResponse<String> response = usersResponse("POST", user);
        JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(1, jsonObject.get("id").getAsInt());
        Assertions.assertEquals("\"common\"", jsonObject.get("name").toString());
    }

    @Test
    public void createUserIncorrectEmailTest() {
        String user = "{\n  \"login\": \"common\"," +
                "\n  \"email\": \"friendDfdscommonru\",\n  \"birthday\": \"1975-08-20\"\n}";
        HttpResponse<String> response = usersResponse("POST", user);

        Assertions.assertEquals(400, response.statusCode());
        Assertions.assertTrue(userController.findAll().isEmpty());
    }

    @Test
    public void createUserWithIncorrectBirthdayTest() {
        String user = "{\n  \"login\": \"common\"," +
                "\n  \"email\": \"friendDfdscommonru\",\n  \"birthday\": \"2028-08-20\"\n}";
        HttpResponse<String> response = usersResponse("POST", user);

        Assertions.assertEquals(400, response.statusCode());
        Assertions.assertTrue(userController.findAll().isEmpty());
    }

    @Test
    public void updateUserWithIncorrectLoginTest() {
        String updatedUser = "{\n  \"login\": \"dolore Update\",\n  \"name\": \"est adipisicing\"," +
                "\n  \"id\": 1,\n  \"email\": \"mail@yandex.ru\",\n  \"birthday\": \"1976-09-20\"\n}";

        usersResponse("POST", giveUser1());
        HttpResponse<String> response = usersResponse("PUT", updatedUser);
        JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();

        Assertions.assertEquals(500, response.statusCode());
        Assertions.assertFalse(jsonObject.has("birthDay"));
    }

    @Test
    public void updateUserWithoutNameTest() {
        String updatedUser = "{\n  \"login\": \"doloreUpdate\",\n  \"name\": \"\"," +
                "\n  \"id\": 1,\n  \"email\": \"mail@yandex.ru\",\n  \"birthday\": \"1976-09-20\"\n}";

        usersResponse("POST", giveUser1());
        HttpResponse<String> response = usersResponse("PUT", updatedUser);
        JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals("\"doloreUpdate\"", jsonObject.get("name").toString());
    }

    @Test
    public void updateUserNotFoundTest() {
        String updatedUser = "{\n  \"login\": \"doloreUpdate\",\n  \"name\": \"est adipisicing\"," +
                "\n  \"id\": 9999,\n  \"email\": \"mail@yandex.ru\",\n  \"birthday\": \"1976-09-20\"\n}";

        usersResponse("POST", giveUser1());
        HttpResponse<String> response = usersResponse("PUT", updatedUser);

        Assertions.assertEquals(500, response.statusCode());
    }

    @Test
    public void getAllUsersTest() {
        String user2 = "{\n  \"login\": \"Mark\",\n  \"name\": \"markus\"," +
                "\n  \"email\": \"markus@mail.ru\",\n  \"birthday\": \"1952-10-10\"\n}";

        usersResponse("POST", giveUser1());
        usersResponse("POST", user2);
        HttpResponse<String> response = usersResponse("GET", "");
        JsonArray jsonArray = JsonParser.parseString(response.body()).getAsJsonArray();

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(2, jsonArray.size());
    }

    @Test
    public void findFilmByIdTest() {
        filmsResponse("POST", giveFilm1());
        HttpResponse<String> response = filmsResponseAdd("GET", "/1");
        JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals("\"nisieiusmod\"", jsonObject.get("name").toString());
    }

    @Test
    public void findFilmByIdNotFoundTest() {
        HttpResponse<String> response = filmsResponseAdd("GET", "/111");

        Assertions.assertEquals(404, response.statusCode());
    }

    @Test
    public void putLikeTest() {
        usersResponse("POST", giveUser1());
        filmsResponse("POST", giveFilm1());
        HttpResponse<String> response = filmsResponseAdd("PUT", "/1/like/1");
        JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(1, jsonObject.get("likes").getAsInt());
    }

    @Test
    public void deleteLikeTest() {
        usersResponse("POST", giveUser1());
        filmsResponse("POST", giveFilm1());
        filmsResponseAdd("PUT", "/1/like/1");
        HttpResponse<String> response = filmsResponseAdd("DELETE", "/1/like/1");
        JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertTrue(jsonObject.get("likes").getAsJsonArray().isEmpty());
    }

    @Test
    public void deleteLikeNotFoundTest() {
        usersResponse("POST", giveUser1());
        filmsResponse("POST", giveFilm1());
        filmsResponseAdd("PUT", "/1/like/1");
        HttpResponse<String> response = filmsResponseAdd("DELETE", "/1/like/3");

        Assertions.assertEquals(404, response.statusCode());
    }

    @Test
    public void getPopularFilmWithoutCountTest() {
        usersResponse("POST", giveUser1());
        usersResponse("POST", giveUser1());
        for (int i = 0; i < 13; i++) {
            filmsResponse("POST", giveFilm1());
        }
        filmsResponseAdd("PUT", "/2/like/1");
        filmsResponseAdd("PUT", "/3/like/1");
        filmsResponseAdd("PUT", "/3/like/2");
        HttpResponse<String> response = filmsResponseAdd("GET", "/popular");
        JsonArray jsonArray = JsonParser.parseString(response.body()).getAsJsonArray();
        JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(10, jsonArray.size());
        Assertions.assertEquals(3, jsonObject.get("id").getAsInt());
    }

    @Test
    public void getPopularFilmWithCountTest() {
        usersResponse("POST", giveUser1());
        usersResponse("POST", giveUser1());
        for (int i = 0; i < 13; i++) {
            filmsResponse("POST", giveFilm1());
        }
        filmsResponseAdd("PUT", "/2/like/1");
        filmsResponseAdd("PUT", "/3/like/1");
        filmsResponseAdd("PUT", "/3/like/2");
        HttpResponse<String> response = filmsResponseAdd("GET", "/popular?count=4");
        JsonArray jsonArray = JsonParser.parseString(response.body()).getAsJsonArray();
        JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(4, jsonArray.size());
        Assertions.assertEquals(3, jsonObject.get("id").getAsInt());
    }

    @Test
    public void findUserByIdTest() {
        usersResponse("POST", giveUser1());
        HttpResponse<String> response = usersResponseAdd("GET", "/1");
        JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals("\"Nick Name\"", jsonObject.get("name").toString());
    }

    @Test
    public void findUserByIdNotFoundTest() {
        usersResponse("POST", giveUser1());
        HttpResponse<String> response = usersResponseAdd("GET", "/7");

        Assertions.assertEquals(404, response.statusCode());
    }

    @Test
    public void addFriendTest() {
        usersResponse("POST", giveUser1());
        usersResponse("POST", giveUser2());
        HttpResponse<String> response = usersResponseAdd("PUT", "/1/friends/2");

        HttpResponse<String> response1 = usersResponseAdd("GET", "/1/friends");
        JsonArray jsonArray = JsonParser.parseString(response1.body()).getAsJsonArray();
        JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(1, jsonArray.size());
        Assertions.assertEquals("dolore2", jsonObject.get("login").getAsString());
   }

    @Test
    public void deleteFriendTest() {
        usersResponse("POST", giveUser1());
        usersResponse("POST", giveUser2());
        usersResponseAdd("PUT", "/1/friends/2");

        HttpResponse<String> response = usersResponseAdd("DELETE", "/1/friends/2");

        HttpResponse<String> response1 = usersResponseAdd("GET", "/1/friends");
        JsonArray jsonArray = JsonParser.parseString(response1.body()).getAsJsonArray();

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(0, jsonArray.size());
    }

    @Test
    public void getFriendsTest() {
        usersResponse("POST", giveUser1());
        usersResponse("POST", giveUser2());
        usersResponseAdd("PUT", "/1/friends/2");
        HttpResponse<String> response = usersResponseAdd("GET", "/1/friends");
        JsonArray jsonArray = JsonParser.parseString(response.body()).getAsJsonArray();
        JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(1, jsonArray.size());
        Assertions.assertEquals("dolore2", jsonObject.get("login").getAsString());
    }

    @Test
    public void getCommonFriendsTest() {
        String user2 = "{\n  \"login\": \"user2\",\n  \"name\": \"user2\"," +
                "\n  \"email\": \"mailUser2@mail.ru\",\n  \"birthday\": \"1942-08-20\"\n}";
        String friend = "{\n  \"login\": \"doloresCOMFRIEND\",\n  \"name\": \"Nick COM\"," +
                "\n  \"email\": \"mailCOM@mail.ru\",\n  \"birthday\": \"1944-08-20\"\n}";

        usersResponse("POST", giveUser1());
        usersResponse("POST", user2);
        usersResponse("POST", friend);
        usersResponseAdd("PUT", "/1/friends/3");
        usersResponseAdd("PUT", "/2/friends/3");
        HttpResponse<String> response = usersResponseAdd("GET", "/1/friends/common/2");
        JsonArray jsonArray = JsonParser.parseString(response.body()).getAsJsonArray();
        JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(1, jsonArray.size());
        Assertions.assertEquals(3, jsonObject.get("id").getAsInt());
    }

    public HttpResponse<String> sendText(HttpRequest request) {
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return response;
    }
}