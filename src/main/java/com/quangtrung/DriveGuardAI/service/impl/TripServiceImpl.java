package com.quangtrung.DriveGuardAI.service.impl;

import com.quangtrung.DriveGuardAI.dto.trip.TripDTO;
import com.quangtrung.DriveGuardAI.dto.trip.TripResponseDTO;
import com.quangtrung.DriveGuardAI.entity.Car;
import com.quangtrung.DriveGuardAI.entity.Trip;
import com.quangtrung.DriveGuardAI.entity.User;
import com.quangtrung.DriveGuardAI.reponsitory.CarReponsitory;
import com.quangtrung.DriveGuardAI.reponsitory.TripReponsitory;
import com.quangtrung.DriveGuardAI.reponsitory.UserReponsitory;
import com.quangtrung.DriveGuardAI.service.AIService;
import com.quangtrung.DriveGuardAI.service.OpenRouteService;
import com.quangtrung.DriveGuardAI.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TripServiceImpl implements TripService {

    @Autowired
    private TripReponsitory tripReponsitory;

    @Autowired
    private CarReponsitory carReponsitory;

    @Autowired
    private UserReponsitory userReponsitory;

    @Autowired
    private AIService aiService;

    @Autowired
    private OpenRouteService openRouteService;

    @Override
    public Trip saveTrip(TripDTO tripDTO) {
        Car car = carReponsitory.findById(tripDTO.getCarId()).orElseThrow(() ->
                new RuntimeException("Không tìm thấy xe với ID: " + tripDTO.getUserId()));

        User user = userReponsitory.findById(tripDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với ID: " + tripDTO.getUserId()));

        // Gọi OpenRouteService Directions API để lấy polyline
        String routePolyline = openRouteService.getDirectionsPolyline(tripDTO.getStartPoint(), tripDTO.getEndPoint());

        Double calculatedTotalKm = calculateDistanceViaAI(tripDTO.getStartPoint(), tripDTO.getEndPoint());

        Trip trip = new Trip();
        trip.setCar(car);
        trip.setUser(user);
        trip.setStartTime(tripDTO.getStartTime());
        trip.setEndTime(tripDTO.getEndTime());
        trip.setStartPoint(tripDTO.getStartPoint());
        trip.setEndPoint(tripDTO.getEndPoint());
        trip.setTotalKm(calculatedTotalKm);
        trip.setMaxSpeed(tripDTO.getMaxSpeed());
        trip.setSafeScore(tripDTO.getSafeScore());
        trip.setRoutePolyline(routePolyline); // Lưu polyline từ Google Maps

        return tripReponsitory.save(trip);
    }

    @Override
    public void updateTrip(TripDTO tripDTO) {
        Trip trip = tripReponsitory.findById(tripDTO.getTripId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chuyến đi với ID: " + tripDTO.getTripId()));

        Double calculatedTotalKm = calculateDistanceViaAI(tripDTO.getStartPoint(), tripDTO.getEndPoint());

        // Chỉ cho phép sửa lộ trình và thông số hành trình
        trip.setStartTime(tripDTO.getStartTime());
        trip.setEndTime(tripDTO.getEndTime());
        trip.setStartPoint(tripDTO.getStartPoint());
        trip.setEndPoint(tripDTO.getEndPoint());
        trip.setTotalKm(calculatedTotalKm);
        trip.setMaxSpeed(tripDTO.getMaxSpeed());
        trip.setSafeScore(tripDTO.getSafeScore());

        tripReponsitory.save(trip);
    }

    private Double calculateDistanceViaAI(String startPoint, String endPoint) {
        try {
            System.out.println("AI đang tính toán khoảng cách từ " + startPoint + " đến " + endPoint + "...");
            String prompt = "Tính toán quãng đường lái xe từ " + startPoint + " đến " + endPoint + ". Chỉ trả về số km (ví dụ: 25.5), không có đơn vị hay text khác.";
            String aiResponse = aiService.askAi(prompt);
            
            String cleanedResponse = aiResponse.replaceAll("[^0-9.]", "");
            if (cleanedResponse.isEmpty()) {
                throw new RuntimeException("AI không trả về kết quả hợp lệ");
            }
            
            return Double.parseDouble(cleanedResponse);
        } catch (Exception e) {
            System.err.println("Lỗi khi tính toán khoảng cách bằng AI: " + e.getMessage());
            throw new RuntimeException("Không thể tính toán khoảng cách: " + e.getMessage());
        }
    }

    @Override
    public Page<TripResponseDTO> findByStartPoint(Pageable pageable, String startPoint) {
        Page<Trip> trips = tripReponsitory.findByStartPointContainingIgnoreCase(startPoint, pageable);
        List<TripResponseDTO> dtoList = trips.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(dtoList, pageable, trips.getTotalElements());
    }

    @Override
    public Page<TripResponseDTO> findWithPageAble(Pageable pageable) {
        Page<Trip> trips = tripReponsitory.findAll(pageable);
        List<TripResponseDTO> dtoList = trips.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(dtoList, pageable, trips.getTotalElements());
    }

    private TripResponseDTO convertToDTO(Trip trip) {
        TripResponseDTO dto = new TripResponseDTO();
        dto.setTripId(trip.getTripId());
        dto.setCarId(trip.getCar().getCarId());
        dto.setUserId(trip.getUser().getUserId());
        dto.setFullName(trip.getUser().getFullName());
        dto.setStartTime(trip.getStartTime());
        dto.setEndTime(trip.getEndTime());
        dto.setStartPoint(trip.getStartPoint());
        dto.setEndPoint(trip.getEndPoint());
        dto.setTotalKm(trip.getTotalKm());
        dto.setMaxSpeed(trip.getMaxSpeed());
        dto.setSafeScore(trip.getSafeScore());
        return dto;
    }
}
