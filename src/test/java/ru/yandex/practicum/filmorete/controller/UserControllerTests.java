package ru.yandex.practicum.filmorete.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class UserControllerTests {

    private String baseUrl = "http://localhost:8081";
    private String url = String.format("%s/films", baseUrl);
    private final HttpClient client = HttpClient.newHttpClient();

    @AfterEach
    public void afterEach() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(url))
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        assertEquals(200, response.statusCode());
    }

    @Nested
    @DisplayName("GET")
    class MethodGet {

        @Test
        @DisplayName("Запрос всех фильмов")
        void getAllFilmTest() throws IOException, InterruptedException {

        }
    }

    @Nested
    @DisplayName("POST")
    class MethodPost {

        @Test
        @DisplayName("Добавление нового фильма")
        void postFilmTest() throws IOException, InterruptedException {

        }
    }

    @Nested
    @DisplayName("PUT")
    class MethodPut {

    }
}
