package com.quangtrung.DriveGuardAI.utils;

import java.util.ArrayList;
import java.util.List;

public class MapUtils {

    // Lớp chứa tọa độ Lat, Lng
    public static class Location {
        public double lat;
        public double lng;

        public Location() {}

        public Location(double lat, double lng) {
            this.lat = lat;
            this.lng = lng;
        }

        public double getLat() { return lat; }
        public void setLat(double lat) { this.lat = lat; }
        public double getLng() { return lng; }
        public void setLng(double lng) { this.lng = lng; }

        @Override
        public String toString() { return lat + ", " + lng; }
    }

    /**
     * 1. Decode polyline sử dụng THUẬT TOÁN CHUẨN CỦA GOOGLE (Bỏ AI)
     * Đảm bảo chính xác 100% và không bị trễ API
     */
    public static List<Location> decodePolyline(String encoded) {
        List<Location> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0; result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            poly.add(new Location((lat / 1E5), (lng / 1E5)));
        }
        return poly;
    }

    /**
     * 2. Tính khoảng cách sử dụng CÔNG THỨC HAVERSINE (Bỏ AI)
     */
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Bán kính trái đất (km)
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    /**
     * 3. Tính tổng chiều dài tuyến đường (Bổ sung để sửa lỗi)
     */
    public static double getTotalRouteDistance(List<Location> path) {
        double total = 0;
        if (path == null || path.size() < 2) return 0;
        for (int i = 0; i < path.size() - 1; i++) {
            total += calculateDistance(path.get(i).lat, path.get(i).lng, path.get(i + 1).lat, path.get(i + 1).lng);
        }
        return total;
    }

    /**
     * 4. Thuật toán nội suy: Tìm tọa độ hiện tại của xe dựa trên số km đã chạy (Bổ sung để sửa lỗi)
     */
    public static Location getCoordinateAtDistance(List<Location> path, double targetDistanceKm) {
        if (path == null || path.isEmpty()) return null;
        if (targetDistanceKm <= 0) return path.get(0);

        double traveled = 0.0;
        for (int i = 0; i < path.size() - 1; i++) {
            Location p1 = path.get(i);
            Location p2 = path.get(i + 1);

            double segmentDist = calculateDistance(p1.lat, p1.lng, p2.lat, p2.lng);

            if (traveled + segmentDist >= targetDistanceKm) {
                double remain = targetDistanceKm - traveled;
                double ratio = (segmentDist == 0) ? 0 : remain / segmentDist;

                double currentLat = p1.lat + (p2.lat - p1.lat) * ratio;
                double currentLng = p1.lng + (p2.lng - p1.lng) * ratio;

                return new Location(currentLat, currentLng);
            }
            traveled += segmentDist;
        }
        return path.get(path.size() - 1); // Đã tới đích
    }
}