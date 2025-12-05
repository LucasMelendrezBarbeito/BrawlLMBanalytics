package com.brawllmbanalytics.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Service
public class BrawlStarsService {

    
    private static final String API_TOKEN =
            "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiIsImtpZCI6IjI4YTMxOGY3LTAwMDAtYTFlYi03ZmExLTJjNzQzM2M2Y2NhNSJ9.eyJpc3MiOiJzdXBlcmNlbGwiLCJhdWQiOiJzdXBlcmNlbGw6Z2FtZWFwaSIsImp0aSI6IjIwMTNkYWJjLTEzMzQtNGIyYS05M2ExLWE1NDg0YzM2NjFiYSIsImlhdCI6MTc2NDcxMzQzNSwic3ViIjoiZGV2ZWxvcGVyLzgyNDFkZGUzLWRjN2MtMTc3Yy1lOWNmLTEzN2Q4MzAyNjczNyIsInNjb3BlcyI6WyJicmF3bHN0YXJzIl0sImxpbWl0cyI6W3sidGllciI6ImRldmVsb3Blci9zaWx2ZXIiLCJ0eXBlIjoidGhyb3R0bGluZyJ9LHsiY2lkcnMiOlsiMTM5LjQ3LjUyLjQ2Il0sInR5cGUiOiJjbGllbnQifV19.R3htXyxc8Ng5PwV-liflnyofttzjcfHn8V6fk4pbhTi2jbFWE7faA2P6zv4sy8pKNFdg-OPDZQDwqCXgFyPvGw";

    
    private static final String API_URL = "https://api.brawlstars.com/v1/players/";

    
    private static final String API_URL_EVENTS_ROTATION = "https://api.brawlstars.com/v1/events/rotation";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public BrawlStarsService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

   
    public Map<String, Object> getPlayerData(String tag) {

        String cleanTag = tag.replace("#", "").trim().toUpperCase();
        String encodedTag = URLEncoder.encode("#" + cleanTag, StandardCharsets.UTF_8);
        String url = API_URL + encodedTag;

        System.out.println("===== LLAMADA SUPERCELL PLAYER =====");
        System.out.println("TAG recibido    : [" + tag + "]");
        System.out.println("TAG limpio      : [" + cleanTag + "]");
        System.out.println("TAG codificado  : [" + encodedTag + "]");
        System.out.println("URL final       : [" + url + "]");
        System.out.println("====================================");

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Bearer " + API_TOKEN)
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(
                    request, HttpResponse.BodyHandlers.ofString()
            );

            int statusCode = response.statusCode();
            String body = response.body();

            System.out.println("===== RESPUESTA SUPERCELL PLAYER =====");
            System.out.println("Status: " + statusCode);
            System.out.println("Body  : " + body);
            System.out.println("======================================");

            if (statusCode == 200) {
                return objectMapper.readValue(body, new TypeReference<Map<String, Object>>() {});
            }

            if (statusCode == 404) {
                throw new RuntimeException("Supercell 404 NOT_FOUND");
            }

            throw new RuntimeException("Supercell error status " + statusCode + ": " + body);

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al llamar a la API de Brawl Stars: " + e.getMessage());
        }
    }

    
    public List<Map<String, Object>> getEventsRotation() {

        System.out.println("===== LLAMADA SUPERCELL ROTATION =====");
        System.out.println("URL final       : [" + API_URL_EVENTS_ROTATION + "]");
        System.out.println("======================================");

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL_EVENTS_ROTATION))
                    .header("Authorization", "Bearer " + API_TOKEN)
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(
                    request, HttpResponse.BodyHandlers.ofString()
            );

            int statusCode = response.statusCode();
            String body = response.body();

            System.out.println("===== RESPUESTA SUPERCELL ROTATION =====");
            System.out.println("Status: " + statusCode);
            System.out.println("Body  : " + body);
            System.out.println("========================================");

            if (statusCode == 200) {
                
                return objectMapper.readValue(
                        body,
                        new TypeReference<List<Map<String, Object>>>() {}
                );
            }

            throw new RuntimeException("Supercell rotation error " + statusCode + ": " + body);

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al llamar a /events/rotation: " + e.getMessage());
        }
    }
}
