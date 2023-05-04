package ru.yandex.practicum.filmorate.controller;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ClientRequests {
    private static final String TASK_HOST_NAME = "localhost";
    private static final int TASK_PORT = 8080;
    protected static HttpClient client;
    protected static final String url = "http://" + TASK_HOST_NAME + ":" + TASK_PORT;

    protected HttpResponse<String> responseToGET(String path) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url + path))
                .header("Accept", "application/json")
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    protected HttpResponse<String> responseToPOST(String json, String postPath)
            throws IOException, InterruptedException {
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(body)
                .uri(URI.create(url + postPath))
                .header("Accept", "application/json")
                .header("Content-type", "application/json")
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    protected HttpResponse<String> responseToPUT(String json, String postPath)
            throws IOException, InterruptedException {
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder()
                .PUT(body)
                .uri(URI.create(url + postPath))
                .header("Accept", "application/json")
                .header("Content-type", "application/json")
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
