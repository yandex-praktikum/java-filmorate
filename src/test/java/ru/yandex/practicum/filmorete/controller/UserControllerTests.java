package ru.yandex.practicum.filmorete.controller;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class USerControllerTests {

    private String baseUrl = "http://localhost:8081";
    private String url = String.format("%s/users", baseUrl);
    private final HttpClient client = HttpClient.newHttpClient();

    private HttpRequest request;
    private HttpResponse<String> response;
    private HttpResponse.BodyHandler<String> handler;
    private String body;

    private HttpRequest buildRequestPutJson(String body) {
        return HttpRequest.newBuilder()
                .PUT(HttpRequest.BodyPublishers.ofString(body))
                .uri(URI.create(url))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "application/json")
                .build();
    }

    @AfterEach
    public void afterEach() throws IOException, InterruptedException {
        request = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(url))
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        handler = HttpResponse.BodyHandlers.ofString();
        response = client.send(request, handler);
        assertEquals(200, response.statusCode());
    }

    @Nested
    @DisplayName("GET")
    class MethodGet {

        @Test
        @DisplayName("Запрос всех пользователей - Пустой список")
        void getAllUsersEmptyTest() throws IOException, InterruptedException {

            request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(url))
                    .build();

            handler = HttpResponse.BodyHandlers.ofString();
            response = client.send(request, handler);

            assertEquals(200, response.statusCode());
            assertEquals("[]", response.body());
        }

        @Test
        @DisplayName("Запрос всех пользователей - После добавления пользователя")
        void getAllUsersTest() throws IOException, InterruptedException {

            body =
                    "{" +
                    "\"login\":\"Bogdan\"," +
                    "\"name\":\"SinitsaBogdan\"," +
                    "\"email\":\"mail@mail.ru\"," +
                    "\"birthday\":\"1997-04-11\"" +
                    "}";

            request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .uri(URI.create(url))
                    .version(HttpClient.Version.HTTP_1_1)
                    .header("Content-Type", "application/json")
                    .build();

            handler = HttpResponse.BodyHandlers.ofString();
            client.send(request, handler);

            request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(url))
                    .build();

            handler = HttpResponse.BodyHandlers.ofString();
            response = client.send(request, handler);

            assertEquals(200, response.statusCode());
            assertEquals(
                    "[{" +
                            "\"id\":1," +
                            "\"name\":\"SinitsaBogdan\"," +
                            "\"birthday\":\"1997-04-11\"," +
                            "\"login\":\"Bogdan\"," +
                            "\"email\":\"mail@mail.ru\"" +
                            "}]",
                    response.body());
        }
    }

    @Nested
    @DisplayName("POST")
    class MethodPost {

        @Test
        @DisplayName("Добавление нового пользователя - с проверкой добавления дубликата")
        void postUsersTest() throws IOException, InterruptedException {

            body =
                    "{" +
                    "\"login\":\"Bogdan\"," +
                    "\"name\":\"SinitsaBogdan\"," +
                    "\"email\":\"mail@mail.ru\"," +
                    "\"birthday\":\"1997-04-11\"" +
                    "}";


            request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .uri(URI.create(url))
                    .version(HttpClient.Version.HTTP_1_1)
                    .header("Content-Type", "application/json")
                    .build();

            handler = HttpResponse.BodyHandlers.ofString();

            response = client.send(request, handler);
            assertEquals(200, response.statusCode());

            response = client.send(request, handler);
            assertEquals(500, response.statusCode());
        }

        @Test
        @DisplayName("Добавление нового пользователя - name : null")
        void postUsersCheckValidNameNullTest() throws IOException, InterruptedException {

            body =
                    "{" +
                    "\"login\":\"Bogdan\"," +
                    "\"email\":\"mail@mail.ru\"," +
                    "\"birthday\":\"1997-04-11\"" +
                    "}";

            request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .uri(URI.create(url))
                    .version(HttpClient.Version.HTTP_1_1)
                    .header("Content-Type", "application/json")
                    .build();

            handler = HttpResponse.BodyHandlers.ofString();

            response = client.send(request, handler);
            assertEquals(200, response.statusCode());
        }

        @Test
        @DisplayName("Добавление нового пользователя - birthday : null")
        void postUsersCheckValidDescriptionNullTest() throws IOException, InterruptedException {

            String body =
                    "{" +
                    "\"login\":\"Bogdan\"," +
                    "\"name\":\"SinitsaBogdan\"," +
                    "\"email\":\"mail@mail.ru\"," +
                    "}";

            request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .uri(URI.create(url))
                    .version(HttpClient.Version.HTTP_1_1)
                    .header("Content-Type", "application/json")
                    .build();

            handler = HttpResponse.BodyHandlers.ofString();

            response = client.send(request, handler);
            assertEquals(400, response.statusCode());
        }

        @Test
        @DisplayName("Добавление нового пользователя - login : null")
        void postUsersCheckValidReleaseDateNullTest() throws IOException, InterruptedException {

            String body =
                    "{" +
                    "\"name\":\"SinitsaBogdan\"," +
                    "\"email\":\"mail@mail.ru\"," +
                    "\"birthday\":\"1997-04-11\"" +
                    "}";

            request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .uri(URI.create(url))
                    .version(HttpClient.Version.HTTP_1_1)
                    .header("Content-Type", "application/json")
                    .build();

            handler = HttpResponse.BodyHandlers.ofString();

            response = client.send(request, handler);
            assertEquals(500, response.statusCode());
        }

        @Test
        @DisplayName("Добавление нового пользователя - email : null")
        void postUsersCheckValidDurationNullTest() throws IOException, InterruptedException {

            String body =
                    "{" +
                    "\"login\":\"Bogdan\"," +
                    "\"name\":\"SinitsaBogdan\"," +
                    "\"birthday\":\"1997-04-11\"" +
                    "}";

            request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .uri(URI.create(url))
                    .version(HttpClient.Version.HTTP_1_1)
                    .header("Content-Type", "application/json")
                    .build();

            handler = HttpResponse.BodyHandlers.ofString();

            response = client.send(request, handler);
            assertEquals(500, response.statusCode());
        }

        @Test
        @DisplayName("Добавление нового пользователя - name : empty")
        void postUsersCheckValidNameEmptyTest() throws IOException, InterruptedException {

            String body =
                    "{" +
                    "\"login\":\"Bogdan\"," +
                    "\"name\":\"\"," +
                    "\"email\":\"mail@mail.ru\"," +
                    "\"birthday\":\"1997-04-11\"" +
                    "}";

            request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .uri(URI.create(url))
                    .version(HttpClient.Version.HTTP_1_1)
                    .header("Content-Type", "application/json")
                    .build();

            handler = HttpResponse.BodyHandlers.ofString();

            response = client.send(request, handler);
            assertEquals(200, response.statusCode());
        }

        @Test
        @DisplayName("Добавление нового пользователя - birthday : empty")
        void postUsersCheckValidBirthdayEmptyTest() throws IOException, InterruptedException {

            String body =
                    "{" +
                    "\"login\":\"Bogdan\"," +
                    "\"name\":\"SinitsaBogdan\"," +
                    "\"email\":\"mail@mail.ru\"," +
                    "\"birthday\":\"\"" +
                    "}";

            request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .uri(URI.create(url))
                    .version(HttpClient.Version.HTTP_1_1)
                    .header("Content-Type", "application/json")
                    .build();

            handler = HttpResponse.BodyHandlers.ofString();

            response = client.send(request, handler);
            assertEquals(500, response.statusCode());
        }

        @Test
        @DisplayName("Добавление нового пользователя - login : empty")
        void postUsersCheckValidDescriptionEmptyTest() throws IOException, InterruptedException {

            body =
                    "{" +
                    "\"login\":\"\"," +
                    "\"name\":\"SinitsaBogdan\"," +
                    "\"email\":\"mail@mail.ru\"," +
                    "\"birthday\":\"1997-04-11\"" +
                    "}";

            request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .uri(URI.create(url))
                    .version(HttpClient.Version.HTTP_1_1)
                    .header("Content-Type", "application/json")
                    .build();

            handler = HttpResponse.BodyHandlers.ofString();

            response = client.send(request, handler);
            assertEquals(500, response.statusCode());
        }

        @Test
        @DisplayName("Добавление нового пользователя - email : empty")
        void postUsersCheckValidReleaseDateEmptyTest() throws IOException, InterruptedException {

            body =
                    "{" +
                    "\"login\":\"Bogdan\"," +
                    "\"name\":\"SinitsaBogdan\"," +
                    "\"email\":\"\"," +
                    "\"birthday\":\"1997-04-11\"" +
                    "}";

            request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .uri(URI.create(url))
                    .version(HttpClient.Version.HTTP_1_1)
                    .header("Content-Type", "application/json")
                    .build();

            handler = HttpResponse.BodyHandlers.ofString();

            response = client.send(request, handler);
            assertEquals(500, response.statusCode());
        }
    }

    @Nested
    @DisplayName("PUT")
    class MethodPut {

        @BeforeEach
        public void beforeEach() throws IOException, InterruptedException {
            body =
                    "{" +
                    "\"login\":\"Bogdan\"," +
                    "\"name\":\"SinitsaBogdan\"," +
                    "\"email\":\"mail@mail.ru\"," +
                    "\"birthday\":\"1997-04-11\"" +
                    "}";

            request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .uri(URI.create(url))
                    .version(HttpClient.Version.HTTP_1_1)
                    .header("Content-Type", "application/json")
                    .build();

            handler = HttpResponse.BodyHandlers.ofString();
            client.send(request, handler);
        }

        @Test
        @DisplayName("Обновление существующего пользователя")
        void putUsersUpdateTest() throws IOException, InterruptedException {

            body =
                    "{" +
                    "\"id\":1," +
                    "\"login\":\"BogdanUpdate\"," +
                    "\"name\":\"SinitsaBogdanUpdate\"," +
                    "\"email\":\"mailUpdate@mail.ru\"," +
                    "\"birthday\":\"2010-04-11\"" +
                    "}";

            request = buildRequestPutJson(body);
            handler = HttpResponse.BodyHandlers.ofString();
            response = client.send(request, handler);

            assertEquals(200, response.statusCode());
            assertEquals("{" +
                    "\"id\":1," +
                    "\"name\":\"SinitsaBogdanUpdate\"," +
                    "\"birthday\":\"2010-04-11\"," +
                    "\"login\":\"BogdanUpdate\"," +
                    "\"email\":\"mailUpdate@mail.ru\"" +
                    "}", response.body());
        }

        @Test
        @DisplayName("Обновление существующего пользователя - id : null")
        void putUsersUpdateCheckValidIdNullTest() throws IOException, InterruptedException {

            body =
                    "{" +
                    "\"login\":\"BogdanUpdate\"," +
                    "\"name\":\"SinitsaBogdanUpdate\"," +
                    "\"email\":\"mailUpdate@mail.ru\"," +
                    "\"birthday\":\"2010-04-11\"" +
                    "}";

            request = buildRequestPutJson(body);
            handler = HttpResponse.BodyHandlers.ofString();
            response = client.send(request, handler);

            assertEquals(500, response.statusCode());
        }

        @Test
        @DisplayName("Обновление существующего пользователя - name : null")
        void putUsersUpdateCheckValidNameNullTest() throws IOException, InterruptedException {

            body =
                    "{" +
                    "\"id\":1," +
                    "\"login\":\"BogdanUpdate\"," +
                    "\"email\":\"mailUpdate@mail.ru\"," +
                    "\"birthday\":\"2010-04-11\"" +
                    "}";

            request = buildRequestPutJson(body);
            handler = HttpResponse.BodyHandlers.ofString();
            response = client.send(request, handler);

            assertEquals(400, response.statusCode());
        }

        @Test
        @DisplayName("Обновление существующего пользователя - birthday : null")
        void putUsersUpdateCheckValidDescriptionsNullTest() throws IOException, InterruptedException {

            body =
                    "{" +
                    "\"id\":1," +
                    "\"login\":\"BogdanUpdate\"," +
                    "\"name\":\"SinitsaBogdanUpdate\"," +
                    "\"email\":\"mailUpdate@mail.ru\"," +
                    "}";

            request = buildRequestPutJson(body);
            handler = HttpResponse.BodyHandlers.ofString();
            response = client.send(request, handler);

            assertEquals(400, response.statusCode());
        }

        @Test
        @DisplayName("Обновление существующего пользователя - login : null")
        void putUsersUpdateCheckValidReleaseDateNullTest() throws IOException, InterruptedException {

            body =
                    "{" +
                    "\"id\":1," +
                    "\"name\":\"SinitsaBogdanUpdate\"," +
                    "\"email\":\"mailUpdate@mail.ru\"," +
                    "\"birthday\":\"2010-04-11\"" +
                    "}";

            request = buildRequestPutJson(body);
            handler = HttpResponse.BodyHandlers.ofString();
            response = client.send(request, handler);

            assertEquals(400, response.statusCode());
        }

        @Test
        @DisplayName("Обновление существующего пользователя - email : null")
        void putUsersUpdateCheckValidDurationNullTest() throws IOException, InterruptedException {

            body =
                    "{" +
                    "\"id\":1," +
                    "\"login\":\"BogdanUpdate\"," +
                    "\"name\":\"SinitsaBogdanUpdate\"," +
                    "\"birthday\":\"2010-04-11\"" +
                    "}";

            request = buildRequestPutJson(body);
            handler = HttpResponse.BodyHandlers.ofString();
            response = client.send(request, handler);

            assertEquals(400, response.statusCode());
        }

        @Test
        @DisplayName("Обновление существующего пользователя - name : empty")
        void putUsersUpdateCheckValidNameEmptyTest() throws IOException, InterruptedException {

            body =
                    "{" +
                    "\"id\":1," +
                    "\"login\":\"BogdanUpdate\"," +
                    "\"name\":\"\"," +
                    "\"email\":\"mailUpdate@mail.ru\"," +
                    "\"birthday\":\"2010-04-11\"" +
                    "}";

            request = buildRequestPutJson(body);
            handler = HttpResponse.BodyHandlers.ofString();
            response = client.send(request, handler);

            assertEquals(400, response.statusCode());
        }

        @Test
        @DisplayName("Обновление существующего пользователя - birthday : empty")
        void putUsersUpdateCheckValidDescriptionEmptyTest() throws IOException, InterruptedException {

            body =
                    "{" +
                    "\"id\":1," +
                    "\"login\":\"BogdanUpdate\"," +
                    "\"name\":\"SinitsaBogdanUpdate\"," +
                    "\"email\":\"mailUpdate@mail.ru\"," +
                    "\"birthday\":\"\"" +
                    "}";

            request = buildRequestPutJson(body);
            handler = HttpResponse.BodyHandlers.ofString();
            response = client.send(request, handler);

            assertEquals(400, response.statusCode());
        }

        @Test
        @DisplayName("Обновление существующего пользователя - login : empty")
        void putUsersUpdateCheckValidReleaseDateEmptyTest() throws IOException, InterruptedException {

            body =
                    "{" +
                    "\"id\":1," +
                    "\"login\":\"\"," +
                    "\"name\":\"SinitsaBogdanUpdate\"," +
                    "\"email\":\"mailUpdate@mail.ru\"," +
                    "\"birthday\":\"2010-04-11\"" +
                    "}";

            request = buildRequestPutJson(body);
            handler = HttpResponse.BodyHandlers.ofString();
            response = client.send(request, handler);

            assertEquals(400, response.statusCode());
        }

        @Test
        @DisplayName("Обновление существующего пользователя - email : empty")
        void putUsersUpdateCheckValidEmailEmptyTest() throws IOException, InterruptedException {

            body =
                    "{" +
                    "\"id\":1," +
                    "\"login\":\"BogdanUpdate\"," +
                    "\"name\":\"SinitsaBogdanUpdate\"," +
                    "\"email\":\"\"," +
                    "\"birthday\":\"2010-04-11\"" +
                    "}";

            request = buildRequestPutJson(body);
            handler = HttpResponse.BodyHandlers.ofString();
            response = client.send(request, handler);

            assertEquals(400, response.statusCode());
        }
    }
}
