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
class FilmControllerTests {

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
		@DisplayName("Запрос всех фильмов - Пустой список")
		void getAllFilmEmptyTest() throws IOException, InterruptedException {

			HttpRequest request = HttpRequest.newBuilder()
					.GET()
					.uri(URI.create(url))
					.build();

			HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
			HttpResponse<String> response = client.send(request, handler);

			assertEquals(200, response.statusCode());
			assertEquals("[]", response.body());
		}

		@Test
		@DisplayName("Запрос всех фильмов - После добавления фильма")
		void getAllFilmTest() throws IOException, InterruptedException {

			HttpRequest request;
			HttpResponse<String> response;
			HttpResponse.BodyHandler<String> handler;

			String body =
					"{" +
					"\"name\":\"nisi eiusmod\"," +
					"\"description\":\"adipisicing\"," +
					"\"releaseDate\":\"1967-03-25\"," +
					"\"duration\":100" +
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
					"\"name\":\"nisi eiusmod\"," +
					"\"description\":\"adipisicing\"," +
					"\"releaseDate\":\"1967-03-25\"," +
					"\"duration\":100" +
					"}]", response.body());
		}
	}

	@Nested
	@DisplayName("POST")
	class MethodPost {

		@Test
		@DisplayName("Добавление нового фильма - с проверкой добавления дубликата")
		void postFilmTest() throws IOException, InterruptedException {

			HttpResponse<String> response;

			String body =
					"{" +
					"\"name\":\"nisi eiusmod\"," +
					"\"description\":\"adipisicing\"," +
					"\"releaseDate\":\"1967-03-25\"," +
					"\"duration\":100" +
					"}";

			HttpRequest request = HttpRequest.newBuilder()
					.POST(HttpRequest.BodyPublishers.ofString(body))
					.uri(URI.create(url))
					.version(HttpClient.Version.HTTP_1_1)
					.header("Content-Type", "application/json")
					.build();

			HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

			response = client.send(request, handler);
			assertEquals(200, response.statusCode());

			response = client.send(request, handler);
			assertEquals(500, response.statusCode());
		}

		@Test
		@DisplayName("Добавление нового фильма - name : null")
		void postFilmCheckValidNameNullTest() throws IOException, InterruptedException {

			HttpResponse<String> response;

			String body =
					"{" +
					"\"description\":\"adipisicing\"," +
					"\"releaseDate\":\"1967-03-25\"," +
					"\"duration\":100" +
					"}";

			HttpRequest request = HttpRequest.newBuilder()
					.POST(HttpRequest.BodyPublishers.ofString(body))
					.uri(URI.create(url))
					.version(HttpClient.Version.HTTP_1_1)
					.header("Content-Type", "application/json")
					.build();

			HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

			response = client.send(request, handler);
			assertEquals(400, response.statusCode());
		}

		@Test
		@DisplayName("Добавление нового фильма - description : null")
		void postFilmCheckValidDescriptionNullTest() throws IOException, InterruptedException {

			HttpResponse<String> response;

			String body =
					"{" +
					"\"name\":\"nisi eiusmod\"," +
					"\"releaseDate\":\"1967-03-25\"," +
					"\"duration\":100" +
					"}";

			HttpRequest request = HttpRequest.newBuilder()
					.POST(HttpRequest.BodyPublishers.ofString(body))
					.uri(URI.create(url))
					.version(HttpClient.Version.HTTP_1_1)
					.header("Content-Type", "application/json")
					.build();

			HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

			response = client.send(request, handler);
			assertEquals(400, response.statusCode());
		}

		@Test
		@DisplayName("Добавление нового фильма - releaseDate : null")
		void postFilmCheckValidReleaseDateNullTest() throws IOException, InterruptedException {

			HttpResponse<String> response;

			String body =
					"{" +
					"\"name\":\"nisi eiusmod\"," +
					"\"description\":\"adipisicing\"," +
					"\"duration\":100" +
					"}";

			HttpRequest request = HttpRequest.newBuilder()
					.POST(HttpRequest.BodyPublishers.ofString(body))
					.uri(URI.create(url))
					.version(HttpClient.Version.HTTP_1_1)
					.header("Content-Type", "application/json")
					.build();

			HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

			response = client.send(request, handler);
			assertEquals(400, response.statusCode());
		}

		@Test
		@DisplayName("Добавление нового фильма - duration : null")
		void postFilmCheckValidDurationNullTest() throws IOException, InterruptedException {

			HttpResponse<String> response;

			String body =
					"{" +
					"\"name\":\"nisi eiusmod\"," +
					"\"description\":\"adipisicing\"," +
					"\"releaseDate\":\"1967-03-25\"," +
					"}";

			HttpRequest request = HttpRequest.newBuilder()
					.POST(HttpRequest.BodyPublishers.ofString(body))
					.uri(URI.create(url))
					.version(HttpClient.Version.HTTP_1_1)
					.header("Content-Type", "application/json")
					.build();

			HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

			response = client.send(request, handler);
			assertEquals(400, response.statusCode());
		}

		@Test
		@DisplayName("Добавление нового фильма - name : empty")
		void postFilmCheckValidNameEmptyTest() throws IOException, InterruptedException {

			HttpResponse<String> response;

			String body =
					"{" +
					"\"name\":\"\"," +
					"\"description\":\"adipisicing\"," +
					"\"releaseDate\":\"1967-03-25\"," +
					"\"duration\":100" +
					"}";

			HttpRequest request = HttpRequest.newBuilder()
					.POST(HttpRequest.BodyPublishers.ofString(body))
					.uri(URI.create(url))
					.version(HttpClient.Version.HTTP_1_1)
					.header("Content-Type", "application/json")
					.build();

			HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

			response = client.send(request, handler);
			assertEquals(500, response.statusCode());
		}

		@Test
		@DisplayName("Добавление нового фильма - description : empty")
		void postFilmCheckValidDescriptionEmptyTest() throws IOException, InterruptedException {

			HttpResponse<String> response;

			String body =
					"{" +
					"\"description\":\"nisi eiusmod\"," +
					"\"description\":\"adipisicing\"," +
					"\"releaseDate\":\"1967-03-25\"," +
					"\"duration\":100" +
					"}";

			HttpRequest request = HttpRequest.newBuilder()
					.POST(HttpRequest.BodyPublishers.ofString(body))
					.uri(URI.create(url))
					.version(HttpClient.Version.HTTP_1_1)
					.header("Content-Type", "application/json")
					.build();

			HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

			response = client.send(request, handler);
			assertEquals(400, response.statusCode());
		}

		@Test
		@DisplayName("Добавление нового фильма - releaseDate : empty")
		void postFilmCheckValidReleaseDateEmptyTest() throws IOException, InterruptedException {

			HttpResponse<String> response;

			String body =
					"{" +
					"\"name\":\"nisi eiusmod\"," +
					"\"description\":\"adipisicing\"," +
					"\"releaseDate\":\"\"," +
					"\"duration\":100" +
					"}";

			HttpRequest request = HttpRequest.newBuilder()
					.POST(HttpRequest.BodyPublishers.ofString(body))
					.uri(URI.create(url))
					.version(HttpClient.Version.HTTP_1_1)
					.header("Content-Type", "application/json")
					.build();

			HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

			response = client.send(request, handler);
			assertEquals(400, response.statusCode());
		}
	}

	@Nested
	@DisplayName("PUT")
	class MethodPut {

		@Test
		@DisplayName("Обновление существующего фильма")
		void putFilmUpdateTest() throws IOException, InterruptedException {

			HttpRequest request;
			HttpResponse<String> response;
			HttpResponse.BodyHandler<String> handler;
			String body;

			body =
					"{" +
					"\"name\":\"nisi eiusmod\"," +
					"\"description\":\"adipisicing\"," +
					"\"releaseDate\":\"1967-03-25\"," +
					"\"duration\":100" +
					"}";

			request = HttpRequest.newBuilder()
					.POST(HttpRequest.BodyPublishers.ofString(body))
					.uri(URI.create(url))
					.version(HttpClient.Version.HTTP_1_1)
					.header("Content-Type", "application/json")
					.build();

			handler = HttpResponse.BodyHandlers.ofString();
			client.send(request, handler);

			body =
					"{" +
					"\"id\":1," +
					"\"name\":\"Один Дома\"," +
					"\"description\":\"Комедия\"," +
					"\"releaseDate\":\"2005-07-16\"," +
					"\"duration\":135" +
					"}";

			request = HttpRequest.newBuilder()
					.PUT(HttpRequest.BodyPublishers.ofString(body))
					.uri(URI.create(url))
					.version(HttpClient.Version.HTTP_1_1)
					.header("Content-Type", "application/json")
					.build();

			handler = HttpResponse.BodyHandlers.ofString();

			response = client.send(request, handler);
			assertEquals(200, response.statusCode());
			assertEquals("{" +
					"\"id\":1," +
					"\"name\":\"Один Дома\"," +
					"\"description\":\"Комедия\"," +
					"\"releaseDate\":\"2005-07-16\"," +
					"\"duration\":135" +
					"}", response.body());
		}

		@Test
		@DisplayName("Обновление существующего фильма - id : null")
		void putFilmUpdateCheckValidIdNullTest() throws IOException, InterruptedException {

			HttpRequest request;
			HttpResponse<String> response;
			HttpResponse.BodyHandler<String> handler;
			String body;

			body =
					"{" +
					"\"name\":\"nisi eiusmod\"," +
					"\"description\":\"adipisicing\"," +
					"\"releaseDate\":\"1967-03-25\"," +
					"\"duration\":100" +
					"}";

			request = HttpRequest.newBuilder()
					.POST(HttpRequest.BodyPublishers.ofString(body))
					.uri(URI.create(url))
					.version(HttpClient.Version.HTTP_1_1)
					.header("Content-Type", "application/json")
					.build();

			handler = HttpResponse.BodyHandlers.ofString();
			client.send(request, handler);

			body =
					"{" +
					"\"name\":\"Один Дома\"," +
					"\"description\":\"Комедия\"," +
					"\"releaseDate\":\"2005-07-16\"," +
					"\"duration\":135" +
					"}";

			request = HttpRequest.newBuilder()
					.PUT(HttpRequest.BodyPublishers.ofString(body))
					.uri(URI.create(url))
					.version(HttpClient.Version.HTTP_1_1)
					.header("Content-Type", "application/json")
					.build();

			handler = HttpResponse.BodyHandlers.ofString();

			response = client.send(request, handler);
			assertEquals(500, response.statusCode());
		}

		@Test
		@DisplayName("Обновление существующего фильма - name : null")
		void putFilmUpdateCheckValidNameNullTest() throws IOException, InterruptedException {

			HttpRequest request;
			HttpResponse<String> response;
			HttpResponse.BodyHandler<String> handler;
			String body;

			body =
					"{" +
					"\"name\":\"nisi eiusmod\"," +
					"\"description\":\"adipisicing\"," +
					"\"releaseDate\":\"1967-03-25\"," +
					"\"duration\":100" +
					"}";

			request = HttpRequest.newBuilder()
					.POST(HttpRequest.BodyPublishers.ofString(body))
					.uri(URI.create(url))
					.version(HttpClient.Version.HTTP_1_1)
					.header("Content-Type", "application/json")
					.build();

			handler = HttpResponse.BodyHandlers.ofString();
			client.send(request, handler);

			body =
					"{" +
					"\"id\":1," +
					"\"description\":\"Комедия\"," +
					"\"releaseDate\":\"2005-07-16\"," +
					"\"duration\":135" +
					"}";

			request = HttpRequest.newBuilder()
					.PUT(HttpRequest.BodyPublishers.ofString(body))
					.uri(URI.create(url))
					.version(HttpClient.Version.HTTP_1_1)
					.header("Content-Type", "application/json")
					.build();

			handler = HttpResponse.BodyHandlers.ofString();

			response = client.send(request, handler);
			assertEquals(400, response.statusCode());
		}

		@Test
		@DisplayName("Обновление существующего фильма - description : null")
		void putFilmUpdateCheckValidDescriptionsNullTest() throws IOException, InterruptedException {

			HttpRequest request;
			HttpResponse<String> response;
			HttpResponse.BodyHandler<String> handler;
			String body;

			body =
					"{" +
					"\"name\":\"nisi eiusmod\"," +
					"\"description\":\"adipisicing\"," +
					"\"releaseDate\":\"1967-03-25\"," +
					"\"duration\":100" +
					"}";

			request = HttpRequest.newBuilder()
					.POST(HttpRequest.BodyPublishers.ofString(body))
					.uri(URI.create(url))
					.version(HttpClient.Version.HTTP_1_1)
					.header("Content-Type", "application/json")
					.build();

			handler = HttpResponse.BodyHandlers.ofString();
			client.send(request, handler);

			body =
					"{" +
					"\"id\":1," +
					"\"name\":\"Один Дома\"," +
					"\"releaseDate\":\"2005-07-16\"," +
					"\"duration\":135" +
					"}";

			request = HttpRequest.newBuilder()
					.PUT(HttpRequest.BodyPublishers.ofString(body))
					.uri(URI.create(url))
					.version(HttpClient.Version.HTTP_1_1)
					.header("Content-Type", "application/json")
					.build();

			handler = HttpResponse.BodyHandlers.ofString();

			response = client.send(request, handler);
			assertEquals(400, response.statusCode());
		}

		@Test
		@DisplayName("Обновление существующего фильма - releaseDate : null")
		void putFilmUpdateCheckValidReleaseDateNullTest() throws IOException, InterruptedException {

			HttpRequest request;
			HttpResponse<String> response;
			HttpResponse.BodyHandler<String> handler;
			String body;

			body =
					"{" +
					"\"name\":\"nisi eiusmod\"," +
					"\"description\":\"adipisicing\"," +
					"\"releaseDate\":\"1967-03-25\"," +
					"\"duration\":100" +
					"}";

			request = HttpRequest.newBuilder()
					.POST(HttpRequest.BodyPublishers.ofString(body))
					.uri(URI.create(url))
					.version(HttpClient.Version.HTTP_1_1)
					.header("Content-Type", "application/json")
					.build();

			handler = HttpResponse.BodyHandlers.ofString();
			client.send(request, handler);

			body =
					"{" +
					"\"id\":1," +
					"\"name\":\"Один Дома\"," +
					"\"description\":\"adipisicing\"," +
					"\"duration\":135" +
					"}";

			request = HttpRequest.newBuilder()
					.PUT(HttpRequest.BodyPublishers.ofString(body))
					.uri(URI.create(url))
					.version(HttpClient.Version.HTTP_1_1)
					.header("Content-Type", "application/json")
					.build();

			handler = HttpResponse.BodyHandlers.ofString();

			response = client.send(request, handler);
			assertEquals(400, response.statusCode());
		}

		@Test
		@DisplayName("Обновление существующего фильма - duration : null")
		void putFilmUpdateCheckValidDurationNullTest() throws IOException, InterruptedException {

			HttpRequest request;
			HttpResponse<String> response;
			HttpResponse.BodyHandler<String> handler;
			String body;

			body =
					"{" +
					"\"name\":\"nisi eiusmod\"," +
					"\"description\":\"adipisicing\"," +
					"\"releaseDate\":\"1967-03-25\"," +
					"\"duration\":100" +
					"}";

			request = HttpRequest.newBuilder()
					.POST(HttpRequest.BodyPublishers.ofString(body))
					.uri(URI.create(url))
					.version(HttpClient.Version.HTTP_1_1)
					.header("Content-Type", "application/json")
					.build();

			handler = HttpResponse.BodyHandlers.ofString();
			client.send(request, handler);

			body =
					"{" +
					"\"id\":1," +
					"\"name\":\"Один Дома\"," +
					"\"description\":\"adipisicing\"," +
					"\"releaseDate\":\"1967-03-25\"," +
					"}";

			request = HttpRequest.newBuilder()
					.PUT(HttpRequest.BodyPublishers.ofString(body))
					.uri(URI.create(url))
					.version(HttpClient.Version.HTTP_1_1)
					.header("Content-Type", "application/json")
					.build();

			handler = HttpResponse.BodyHandlers.ofString();

			response = client.send(request, handler);
			assertEquals(400, response.statusCode());
		}

		@Test
		@DisplayName("Обновление существующего фильма - name : empty")
		void putFilmUpdateCheckValidNameEmptyTest() throws IOException, InterruptedException {

			HttpRequest request;
			HttpResponse<String> response;
			HttpResponse.BodyHandler<String> handler;
			String body;

			body =
					"{" +
					"\"name\":\"nisi eiusmod\"," +
					"\"description\":\"adipisicing\"," +
					"\"releaseDate\":\"1967-03-25\"," +
					"\"duration\":100" +
					"}";

			request = HttpRequest.newBuilder()
					.POST(HttpRequest.BodyPublishers.ofString(body))
					.uri(URI.create(url))
					.version(HttpClient.Version.HTTP_1_1)
					.header("Content-Type", "application/json")
					.build();

			handler = HttpResponse.BodyHandlers.ofString();
			client.send(request, handler);

			body =
					"{" +
					"\"id\":1," +
					"\"name\":\"\"," +
					"\"description\":\"adipisicing\"," +
					"\"releaseDate\":\"1967-03-25\"," +
					"}";

			request = HttpRequest.newBuilder()
					.PUT(HttpRequest.BodyPublishers.ofString(body))
					.uri(URI.create(url))
					.version(HttpClient.Version.HTTP_1_1)
					.header("Content-Type", "application/json")
					.build();

			handler = HttpResponse.BodyHandlers.ofString();

			response = client.send(request, handler);
			assertEquals(400, response.statusCode());
		}

		@Test
		@DisplayName("Обновление существующего фильма - description : empty")
		void putFilmUpdateCheckValidDescriptionEmptyTest() throws IOException, InterruptedException {

			HttpRequest request;
			HttpResponse<String> response;
			HttpResponse.BodyHandler<String> handler;
			String body;

			body =
					"{" +
					"\"name\":\"nisi eiusmod\"," +
					"\"description\":\"adipisicing\"," +
					"\"releaseDate\":\"1967-03-25\"," +
					"\"duration\":100" +
					"}";

			request = HttpRequest.newBuilder()
					.POST(HttpRequest.BodyPublishers.ofString(body))
					.uri(URI.create(url))
					.version(HttpClient.Version.HTTP_1_1)
					.header("Content-Type", "application/json")
					.build();

			handler = HttpResponse.BodyHandlers.ofString();
			client.send(request, handler);

			body =
					"{" +
					"\"id\":1," +
					"\"name\":\"Один Дома\"," +
					"\"description\":\"\"," +
					"\"releaseDate\":\"1967-03-25\"," +
					"}";

			request = HttpRequest.newBuilder()
					.PUT(HttpRequest.BodyPublishers.ofString(body))
					.uri(URI.create(url))
					.version(HttpClient.Version.HTTP_1_1)
					.header("Content-Type", "application/json")
					.build();

			handler = HttpResponse.BodyHandlers.ofString();

			response = client.send(request, handler);
			assertEquals(400, response.statusCode());
		}

		@Test
		@DisplayName("Обновление существующего фильма - releaseDate : empty")
		void putFilmUpdateCheckValidReleaseDateEmptyTest() throws IOException, InterruptedException {

			HttpRequest request;
			HttpResponse<String> response;
			HttpResponse.BodyHandler<String> handler;
			String body;

			body =
					"{" +
					"\"name\":\"nisi eiusmod\"," +
					"\"description\":\"adipisicing\"," +
					"\"releaseDate\":\"1967-03-25\"," +
					"\"duration\":100" +
					"}";

			request = HttpRequest.newBuilder()
					.POST(HttpRequest.BodyPublishers.ofString(body))
					.uri(URI.create(url))
					.version(HttpClient.Version.HTTP_1_1)
					.header("Content-Type", "application/json")
					.build();

			handler = HttpResponse.BodyHandlers.ofString();
			client.send(request, handler);

			body =
					"{" +
					"\"id\":1," +
					"\"name\":\"Один Дома\"," +
					"\"description\":\"adipisicing\"," +
					"\"releaseDate\":\"\"," +
					"}";

			request = HttpRequest.newBuilder()
					.PUT(HttpRequest.BodyPublishers.ofString(body))
					.uri(URI.create(url))
					.version(HttpClient.Version.HTTP_1_1)
					.header("Content-Type", "application/json")
					.build();

			handler = HttpResponse.BodyHandlers.ofString();

			response = client.send(request, handler);
			assertEquals(400, response.statusCode());
		}
	}
}
