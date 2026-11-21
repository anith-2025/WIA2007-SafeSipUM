package com.um.safesip.member4;

import java.util.UUID;

public class WaterReport {
    private String reportId;
    private String stationId;
    private String userId;
    private String issueCategory;
    private String photoUriString;
    private double latitude;
    private double longitude;
    private long timestampMillis;
    private float cleanlinessRating;
    private String reviewText;
    private String status;

    public WaterReport() {
    }

    public WaterReport(String stationId,
                       String userId,
                       String issueCategory,
                       String photoUriString,
                       double latitude,
                       double longitude,
                       long timestampMillis,
                       float cleanlinessRating,
                       String reviewText) {
        this.reportId = UUID.randomUUID().toString();
        this.stationId = stationId;
        this.userId = userId;
        this.issueCategory = issueCategory;
        this.photoUriString = photoUriString;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestampMillis = timestampMillis;
        this.cleanlinessRating = cleanlinessRating;
        this.reviewText = reviewText;
        this.status = "OPEN";
    }

    public String getReportId() { return reportId; }
    public void setReportId(String reportId) { this.reportId = reportId; }

    public String getStationId() { return stationId; }
    public void setStationId(String stationId) { this.stationId = stationId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getIssueCategory() { return issueCategory; }
    public void setIssueCategory(String issueCategory) { this.issueCategory = issueCategory; }

    public String getPhotoUriString() { return photoUriString; }
    public void setPhotoUriString(String photoUriString) { this.photoUriString = photoUriString; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public long getTimestampMillis() { return timestampMillis; }
    public void setTimestampMillis(long timestampMillis) { this.timestampMillis = timestampMillis; }

    public float getCleanlinessRating() { return cleanlinessRating; }
    public void setCleanlinessRating(float cleanlinessRating) { this.cleanlinessRating = cleanlinessRating; }

    public String getReviewText() { return reviewText; }
    public void setReviewText(String reviewText) { this.reviewText = reviewText; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}