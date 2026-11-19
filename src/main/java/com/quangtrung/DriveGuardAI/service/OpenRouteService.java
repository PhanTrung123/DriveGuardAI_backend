package com.quangtrung.DriveGuardAI.service;

public interface OpenRouteService {
    /**
     * Lấy polyline từ OpenRouteService Directions API
     * @param origin Tọa độ xuất phát (lat,lng)
     * @param destination Tọa độ đến (lat,lng)
     * @return Chuỗi polyline đã encode
     */
    String getDirectionsPolyline(String origin, String destination);

    /**
     * Tìm garage gần nhất từ một vị trí
     * @param location Tọa độ hiện tại (lat,lng)
     * @return Thông tin garage gần nhất
     */
    String findNearestGarage(String location);
}
