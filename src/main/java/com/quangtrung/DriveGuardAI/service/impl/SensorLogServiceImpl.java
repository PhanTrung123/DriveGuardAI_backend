package com.quangtrung.DriveGuardAI.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quangtrung.DriveGuardAI.dto.trip.TripDTO;
import com.quangtrung.DriveGuardAI.entity.Car;
import com.quangtrung.DriveGuardAI.entity.SensorLog;
import com.quangtrung.DriveGuardAI.entity.Trip;
import com.quangtrung.DriveGuardAI.enums.TripStatus;
import com.quangtrung.DriveGuardAI.reponsitory.CarReponsitory;
import com.quangtrung.DriveGuardAI.reponsitory.SensorLogRepository;
import com.quangtrung.DriveGuardAI.reponsitory.TripReponsitory;
import com.quangtrung.DriveGuardAI.service.AIService;
import com.quangtrung.DriveGuardAI.service.OpenRouteService;
import com.quangtrung.DriveGuardAI.service.SensorLogService;
import com.quangtrung.DriveGuardAI.utils.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class SensorLogServiceImpl implements SensorLogService {

    @Autowired
    private CarReponsitory carReponsitory;

    @Autowired
    private SensorLogRepository sensorLogRepository;

    @Autowired
    private TripReponsitory tripReponsitory;

    @Autowired
    private AIService aiService;

    @Autowired
    private OpenRouteService openRouteService;

    private Random random = new Random();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void generateFakeSensorData(TripDTO tripDTO) {
        Car car = carReponsitory.findById(tripDTO.getCarId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy xe"));

        Trip trip = tripReponsitory.findById(tripDTO.getTripId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chuyến đi"));

        System.out.println("DEBUG - Trip ID: " + trip.getTripId() + ", Status: " + trip.getStatus());

        // 1. NẾU XE ĐÃ HỎNG TỪ TRƯỚC (Đóng băng hành trình)
        if (trip.getStatus() == TripStatus.BROKEN_DOWN) {
            handleBrokenDownTrip(car, trip);
            return; // Dừng tại đây, không gọi AI nữa để tiết kiệm tài nguyên
        }

        // 2. NẾU CHUYẾN ĐI ĐÃ HOÀN THÀNH
        if (trip.getStatus() == TripStatus.COMPLETED) {
            System.out.println("Chuyến đi đã hoàn thành, không sinh thêm dữ liệu.");
            return;
        }

        // 3. CODE JAVA TÍNH TOÁN TỌA ĐỘ (Không cho AI bịa tọa độ để tránh xe bay lung tung)
        long stepCount = sensorLogRepository.countByTrip_TripId(trip.getTripId());
        String polyline = trip.getRoutePolyline();
        if (polyline == null || polyline.isEmpty()) {
            polyline = "izsbBq~_jSa@`@g@v@i@b@w@f@_A~@k@r@m@r@w@n@w@p@c@j@Yl@Wx@Mr@E|@?v@Bx@Ft@Jv@Nf@Ph@Xj@x@z@f@b@f@`@f@`@"; // Mặc định ĐN
        }

        List<MapUtils.Location> coordinates = MapUtils.decodePolyline(polyline);
        double distancePerTickKm = 0.07; // Giả sử mỗi 5s xe đi được 70 mét
        double totalTraveledKm = stepCount * distancePerTickKm;
        double maxRouteDistance = MapUtils.getTotalRouteDistance(coordinates);

        System.out.println("DEBUG - StepCount: " + stepCount + ", Traveled: " + totalTraveledKm + "km, MaxRoute: " + maxRouteDistance + "km");

        // Kiểm tra nếu đã tới đích
        if (totalTraveledKm >= maxRouteDistance) {
            trip.setStatus(TripStatus.COMPLETED);
            tripReponsitory.save(trip);
            System.out.println("Xe đã tới đích an toàn!");
            return;
        }

        // Lấy tọa độ chuẩn xác bám mép đường
        String exactLocation = MapUtils.getCoordinateAtDistance(coordinates, totalTraveledKm).toString();

        // 4. LẤY TRÍ NHỚ CHO AI VÀ GỌI AI
        SensorLog lastLog = sensorLogRepository.findFirstByTrip_TripIdOrderByCreatedAtDesc(trip.getTripId()).orElse(null);
        String prompt = buildSensorDataPrompt(tripDTO, lastLog, exactLocation);
        String aiResponse = aiService.askAi(prompt);
        SensorData sensorData = parseSensorDataFromAI(aiResponse, lastLog, exactLocation);

        // 5. NẾU AI PHÁT HIỆN LỖI (Vượt ngưỡng) -> ĐỔI TRẠNG THÁI CHUYẾN ĐI THÀNH HỎNG
        if (sensorData.errorCode != null && !sensorData.errorCode.trim().isEmpty() && !sensorData.errorCode.equalsIgnoreCase("null")) {
            trip.setStatus(TripStatus.BROKEN_DOWN);
            trip.setEndPoint(exactLocation); // Chốt vị trí xe hỏng
            tripReponsitory.save(trip);

            // Tìm garage gần nhất
            String nearestGarage = openRouteService.findNearestGarage(exactLocation);
            System.out.println("CẢNH BÁO KHẨN CẤP: Xe gặp sự cố " + sensorData.errorCode + " tại " + exactLocation);
            System.out.println("GARAGE GẦN NHẤT: " + nearestGarage);
        }

        // 6. LƯU LOG VÀO DATABASE
        SensorLog log = new SensorLog();
        log.setCar(car);
        log.setTrip(trip);
        log.setSpeed(sensorData.speed);
        log.setRpm(sensorData.rpm);
        log.setEngineTemp(sensorData.engineTemp);
        log.setBatteryVolt(sensorData.batteryVolt);
        log.setFuelPercent(sensorData.fuelPercent);
        log.setCurrentLocation(exactLocation); // Ép dùng tọa độ chuẩn của Java, bỏ qua tọa độ AI bịa
        log.setErrorCode(sensorData.errorCode);

        sensorLogRepository.save(log);
    }

    // --- HÀM PHỤ: XỬ LÝ ĐÓNG BĂNG KHI XE HỎNG ---
    private void handleBrokenDownTrip(Car car, Trip trip) {
        SensorLog lastLog = sensorLogRepository.findFirstByTrip_TripIdOrderByCreatedAtDesc(trip.getTripId()).orElse(null);

        SensorLog frozenLog = new SensorLog();
        frozenLog.setCar(car);
        frozenLog.setTrip(trip);
        frozenLog.setSpeed(0.0); // Bắt buộc bằng 0
        frozenLog.setRpm(0);     // Tắt máy
        frozenLog.setCurrentLocation(lastLog != null ? lastLog.getCurrentLocation() : trip.getStartPoint());
        frozenLog.setErrorCode(lastLog != null ? lastLog.getErrorCode() : "UNKNOWN");
        frozenLog.setBatteryVolt(12.0);
        frozenLog.setFuelPercent(lastLog != null ? lastLog.getFuelPercent() : 50.0);

        // Nhiệt độ giảm dần đến 40 độ
        double cooledTemp = (lastLog != null && lastLog.getEngineTemp() > 40) ? lastLog.getEngineTemp() - 1.5 : 40.0;
        frozenLog.setEngineTemp(Math.round(cooledTemp * 10.0) / 10.0);

        sensorLogRepository.save(frozenLog);
        System.out.println("Xe đang nằm chờ cứu hộ, đã lưu log đóng băng.");
    }

    // --- HÀM PHỤ: XÂY DỰNG PROMPT CHO AI ---
    private String buildSensorDataPrompt(TripDTO tripDTO, SensorLog lastLog, String exactLocation) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Bạn là ECU giám sát động cơ ô tô. Hãy xuất JSON thông số kỹ thuật xe tại thời điểm hiện tại.\n");
        prompt.append("Tọa độ hiện tại (Đã được hệ thống định vị chốt cứng): ").append(exactLocation).append("\n\n");

        if (lastLog == null) {
            prompt.append("Xe vừa xuất phát. Tốc độ từ từ tăng, Xăng 100%.\n");
        } else {
            prompt.append("THÔNG SỐ TRƯỚC ĐÓ 5 GIÂY (Dùng làm gốc):\n");
            prompt.append("- Tốc độ: ").append(lastLog.getSpeed()).append(" km/h, Xăng: ").append(lastLog.getFuelPercent()).append(" %\n");
            prompt.append("- RPM: ").append(lastLog.getRpm()).append(", Nhiệt độ: ").append(lastLog.getEngineTemp()).append(" độ C\n");
            prompt.append("Ràng buộc BẮT BUỘC:\n");
            prompt.append("- Tốc độ không chênh lệch quá 10km/h so với trước. Xăng chỉ được giảm.\n");
            // Cài cắm tỉ lệ lỗi để demo
            prompt.append("- Có 2% tỉ lệ bạn tự sinh ra mã lỗi P0217 (Quá nhiệt, nhiệt độ > 105 độ) hoặc P0562 (Lỗi ắc quy). Nếu không có lỗi, errorCode = null.\n\n");
        }

        prompt.append("Định dạng JSON (Không bọc Markdown, chỉ xuất JSON):\n");
        prompt.append("{\"speed\": <double>, \"rpm\": <int>, \"engineTemp\": <double>, \"batteryVolt\": <double>, \"fuelPercent\": <double>, \"errorCode\": <string hoặc null>}");
        return prompt.toString();
    }

    // --- HÀM PHỤ: PARSE JSON AI ---
    private SensorData parseSensorDataFromAI(String aiResponse, SensorLog lastLog, String exactLocation) {
        SensorData data = new SensorData();
        try {
            String cleanJson = aiResponse.replaceAll("```json", "").replaceAll("```", "").trim();
            data = objectMapper.readValue(cleanJson, SensorData.class);
        } catch (Exception e) {
            if (lastLog != null) {
                data.speed = Math.max(0, lastLog.getSpeed() + (random.nextInt(10) - 5));
                data.rpm = 1500 + random.nextInt(500);
                data.engineTemp = lastLog.getEngineTemp();
                data.batteryVolt = 13.5 + random.nextDouble();
                data.fuelPercent = Math.max(0, lastLog.getFuelPercent() - 0.1);
                data.errorCode = null;
            } else {
                data.speed = 10.0; data.rpm = 1200; data.engineTemp = 60.0;
                data.batteryVolt = 14.0; data.fuelPercent = 100.0; data.errorCode = null;
            }
        }
        data.currentLocation = exactLocation; // Luôn dùng tọa độ chuẩn
        return data;
    }

    private static class SensorData {
        public double speed;
        public int rpm;
        public double engineTemp;
        public double batteryVolt;
        public double fuelPercent;
        public String currentLocation;
        public String errorCode;
    }
}