package com.quangtrung.DriveGuardAI.enums;

public enum CarStandard {

    // Nhóm CRITICAL (Nguy hiểm - Buộc phải dừng xe)
    OVERHEAT("P0217", "CRITICAL", "Nhiệt độ động cơ"),
    OVER_RPM("P0219", "CRITICAL", "Vòng tua máy"),
    DEAD_BATTERY("P0562", "CRITICAL", "Điện áp ắc quy"), // Bình sụt quá sâu, chết máy phát

    // Nhóm WARNING (Cảnh báo - Xe vẫn tiếp tục chạy được)
    LOW_BATTERY("P0514", "WARNING", "Điện áp ắc quy"),   // Bình hơi yếu
    LOW_FUEL("P0148", "WARNING", "Bình nhiên liệu"),
    OVERSPEED("P0214", "WARNING", "Tốc độ di chuyển");

    private final String code;
    private final String dangerLevel;
    private final String component;

    CarStandard(String code, String dangerLevel, String component) {
        this.code = code;
        this.dangerLevel = dangerLevel;
        this.component = component;
    }

    public String getCode() { return code; }
    public String getDangerLevel() { return dangerLevel; }
    public String getComponent() { return component; }

    public static CarStandard evaluateSensorData(double engineTemp, double batteryVolt, int rpm, double fuelPercent, double speed) {
        // 1. Kiểm tra các lỗi CHẾT MÁY (Ưu tiên kiểm tra trước)
        if (engineTemp >= 105.0) return OVERHEAT;
        if (rpm >= 5500) return OVER_RPM;
        if (batteryVolt <= 10.0) return DEAD_BATTERY; // Dưới 10V là tắt đài, không nổ được máy

        // 2. Kiểm tra các lỗi CẢNH BÁO
        if (batteryVolt > 10.0 && batteryVolt < 11.5) return LOW_BATTERY;
        if (fuelPercent <= 10.0) return LOW_FUEL; // Xăng dưới 10% báo động
        if (speed >= 120.0) return OVERSPEED;     // Chạy quá 120km/h

        return null; // Mọi thứ hoàn hảo
    }


}
