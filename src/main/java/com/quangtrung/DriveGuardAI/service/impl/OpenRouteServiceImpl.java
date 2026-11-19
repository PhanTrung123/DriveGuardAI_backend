package com.quangtrung.DriveGuardAI.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quangtrung.DriveGuardAI.service.OpenRouteService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class OpenRouteServiceImpl implements OpenRouteService {

    @Value("${openroute.api.key}")
    private String apiKey;

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public OpenRouteServiceImpl() {
        this.webClient = WebClient.builder().build();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public String getDirectionsPolyline(String origin, String destination) {
        try {
            // OpenRouteService yêu cầu tọa độ dạng: lon,lat
            String url = String.format(
                "https://api.openrouteservice.org/v2/directions/driving-car?start=%s&end=%s",
                origin, destination
            );

            String response = webClient.get()
                .uri(url)
                .header(HttpHeaders.AUTHORIZATION, apiKey)
                .retrieve()
                .bodyToMono(String.class)
                .block();

            JsonNode root = objectMapper.readTree(response);
            JsonNode routes = root.path("routes");
            
            if (routes.isArray() && routes.size() > 0) {
                JsonNode geometry = routes.get(0).path("geometry");
                String encodedPolyline = geometry.asText();
                return encodedPolyline;
            }
            
            // Fallback: trả về polyline mặc định nếu API lỗi
            System.err.println("OpenRouteService không trả về polyline, sử dụng mặc định");
            return "izsbBq~_jSa@`@g@v@i@b@w@f@_A~@k@r@m@r@w@n@w@p@c@j@Yl@Wx@Mr@E|@?v@Bx@Ft@Jv@Nf@Ph@Xj@x@z@f@b@f@`@f@`@";
            
        } catch (Exception e) {
            System.err.println("Lỗi khi gọi OpenRouteService Directions API: " + e.getMessage());
            // Fallback polyline mặc định (Đà Nẵng)
            return "izsbBq~_jSa@`@g@v@i@b@w@f@_A~@k@r@m@r@w@n@w@p@c@j@Yl@Wx@Mr@E|@?v@Bx@Ft@Jv@Nf@Ph@Xj@x@z@f@b@f@`@f@`@";
        }
    }

    @Override
    public String findNearestGarage(String location) {
        try {
            // OpenRouteService POI API - tìm garage/sửa xe
            String url = String.format(
                "https://api.openrouteservice.org/v2/pois?categories=service.vehicle.repair&radius=5000&filter=%s",
                location
            );

            String response = webClient.get()
                .uri(url)
                .header(HttpHeaders.AUTHORIZATION, apiKey)
                .retrieve()
                .bodyToMono(String.class)
                .block();

            JsonNode root = objectMapper.readTree(response);
            JsonNode features = root.path("features");
            
            if (features.isArray() && features.size() > 0) {
                JsonNode firstGarage = features.get(0);
                String name = firstGarage.path("properties").path("name").asText();
                JsonNode coords = firstGarage.path("geometry").path("coordinates");
                String lon = coords.get(0).asText();
                String lat = coords.get(1).asText();
                
                return String.format("%s | %s,%s", name, lat, lon);
            }
            
            return "Không tìm thấy garage gần";
            
        } catch (Exception e) {
            System.err.println("Lỗi khi tìm garage: " + e.getMessage());
            return "Garage gần nhất: Tiệm sửa xe ABC (demo)";
        }
    }
}
