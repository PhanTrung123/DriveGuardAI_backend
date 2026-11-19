package com.quangtrung.DriveGuardAI.controller;

import com.quangtrung.DriveGuardAI.utils.MapUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/map")
public class MapController {

    /**
     * Endpoint decode polyline sử dụng thuật toán chuẩn Google Maps
     * @param encoded Chuỗi polyline đã encode
     * @return Danh sách tọa độ
     */
    @GetMapping("/decode")
    public ResponseEntity<?> decodePolyline(@RequestParam String encoded) {
        List<MapUtils.Location> locations = MapUtils.decodePolyline(encoded);
        return ResponseEntity.ok(locations);
    }

    /**
     * Endpoint tính khoảng cách sử dụng công thức Haversine
     * @param lat1 Vĩ độ điểm 1
     * @param lon1 Kinh độ điểm 1
     * @param lat2 Vĩ độ điểm 2
     * @param lon2 Kinh độ điểm 2
     * @return Khoảng cách tính bằng km
     */
    @GetMapping("/distance")
    public ResponseEntity<?> calculateDistance(
            @RequestParam double lat1,
            @RequestParam double lon1,
            @RequestParam double lat2,
            @RequestParam double lon2) {
        double distance = MapUtils.calculateDistance(lat1, lon1, lat2, lon2);
        return ResponseEntity.ok(distance);
    }
}
