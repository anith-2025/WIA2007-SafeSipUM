package com.um.safesip.member4;

import java.util.UUID;

public class NotificationRecord {
    private String notificationId;
    private String reportId;
    private String stationName;
    private String title;
    private String message;
    private long timestampMillis;
    private boolean read;

    public NotificationRecord() {
    }

    public NotificationRecord(String reportId, String stationName, String title, String message, long timestampMillis) {
        this.notificationId = UUID.randomUUID().toString();
        this.reportId = reportId;
        this.stationName = stationName;
        this.title = title;
        this.message = message;
        this.timestampMillis = timestampMillis;
        this.read = false;
    }

    public String getNotificationId() { return notificationId; }
    public String getReportId() { return reportId; }
    public String getStationName() { return stationName; }
    public String getTitle() { return title; }
    public String getMessage() { return message; }
    public long getTimestampMillis() { return timestampMillis; }
    public boolean isRead() { return read; }
    public void setRead(boolean read) { this.read = read; }
}