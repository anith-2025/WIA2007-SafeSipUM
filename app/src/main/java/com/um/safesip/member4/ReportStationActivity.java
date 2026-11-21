package com.um.safesip.member4;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.um.safesip.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReportStationActivity extends AppCompatActivity {

    private static final String TAG = "SafeSip_Member4";
    private static final int REQ_PERMISSIONS = 201;

    private EditText etStationName;
    private Spinner spinnerIssue;
    private ImageView ivPhotoPreview;
    private TextView tvGpsInfo;
    private TextView tvTimestampInfo;
    private RatingBar ratingBarCleanliness;
    private EditText etReviewText;

    private Uri selectedPhotoUri;
    private double currentLat = 3.1209;
    private double currentLng = 101.6538;
    private long currentTimestampMillis;

    private final ActivityResultLauncher<String> pickPhotoLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    try {
                        selectedPhotoUri = uri;
                        ivPhotoPreview.setImageURI(uri);
                        ivPhotoPreview.setVisibility(View.VISIBLE);
                        Toast.makeText(this, "Photo attached", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Log.e(TAG, "Failed to load selected photo", e);
                        Toast.makeText(this, "Could not preview photo", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_station);
        Log.d(TAG, "ReportStationActivity setContentView completed");

        View contentView = findViewById(android.R.id.content);
        if (contentView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(contentView, (v, insets) -> {
                Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
                return insets;
            });
        }

        etStationName = findViewById(R.id.etStationName);
        spinnerIssue = findViewById(R.id.spinnerIssue);
        ivPhotoPreview = findViewById(R.id.ivPhotoPreview);
        ivPhotoPreview.setVisibility(View.GONE);
        tvGpsInfo = findViewById(R.id.tvGpsInfo);
        tvTimestampInfo = findViewById(R.id.tvTimestampInfo);
        ratingBarCleanliness = findViewById(R.id.ratingBarCleanliness);
        etReviewText = findViewById(R.id.etReviewText);

        Button btnAttachPhoto = findViewById(R.id.btnAttachPhoto);
        Button btnRefreshLocation = findViewById(R.id.btnRefreshLocation);
        Button btnSubmitReport = findViewById(R.id.btnSubmitReport);
        Button btnOpenNotifications = findViewById(R.id.btnOpenNotifications);

        String[] issues = {
                "Broken Cooler",
                "Low Water Pressure",
                "Dirty Filter",
                "Leaking Tap"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, issues
        );
        spinnerIssue.setAdapter(adapter);

        refreshTimestamp();
        requestRequiredPermissions();
        fetchCurrentLocation();

        btnAttachPhoto.setOnClickListener(v -> pickPhotoLauncher.launch("image/*"));
        btnRefreshLocation.setOnClickListener(v -> {
            refreshTimestamp();
            fetchCurrentLocation();
        });

        btnSubmitReport.setOnClickListener(v -> submitReport());

        btnOpenNotifications.setOnClickListener(v -> {
            Intent intent = new Intent(ReportStationActivity.this, NotificationHistoryActivity.class);
            startActivity(intent);
        });
    }

    private void requestRequiredPermissions() {
        List<String> needed = new ArrayList<>();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            needed.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                        != PackageManager.PERMISSION_GRANTED) {
            needed.add(Manifest.permission.POST_NOTIFICATIONS);
        }
        if (!needed.isEmpty()) {
            ActivityCompat.requestPermissions(this, needed.toArray(new String[0]), REQ_PERMISSIONS);
        }
    }

    private void refreshTimestamp() {
        currentTimestampMillis = System.currentTimeMillis();
        String formatted = new SimpleDateFormat("dd MMM yyyy, HH:mm:ss", Locale.getDefault())
                .format(new Date(currentTimestampMillis));
        tvTimestampInfo.setText("Timestamp: " + formatted);
    }

    private void fetchCurrentLocation() {
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                tvGpsInfo.setText(String.format(Locale.getDefault(),
                        "GPS (Default UM): %.5f, %.5f", currentLat, currentLng));
                return;
            }

            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            if (locationManager != null) {
                Location lastKnown = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (lastKnown == null) {
                    lastKnown = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }
                if (lastKnown != null) {
                    currentLat = lastKnown.getLatitude();
                    currentLng = lastKnown.getLongitude();
                }
            }
            tvGpsInfo.setText(String.format(Locale.getDefault(),
                    "GPS: %.5f, %.5f", currentLat, currentLng));
        } catch (Exception e) {
            Log.e(TAG, "Location lookup failed, using UM fallback", e);
            tvGpsInfo.setText(String.format(Locale.getDefault(),
                    "GPS (Fallback UM): %.5f, %.5f", currentLat, currentLng));
        }
    }

    private void submitReport() {
        String stationName = etStationName.getText().toString().trim();
        if (stationName.isEmpty()) {
            etStationName.setError("Please enter or select a water station");
            return;
        }

        refreshTimestamp();
        String issue = spinnerIssue.getSelectedItem().toString();
        float rating = ratingBarCleanliness.getRating();
        String review = etReviewText.getText().toString().trim();
        String photoStr = selectedPhotoUri != null ? selectedPhotoUri.toString() : "";

        WaterReport report = new WaterReport(
                stationName,
                "current_um_user",
                issue,
                photoStr,
                currentLat,
                currentLng,
                currentTimestampMillis,
                rating,
                review
        );

        Toast.makeText(
                this,
                "Report submitted for " + report.getStationId() + " (" + report.getStatus() + ")",
                Toast.LENGTH_LONG
        ).show();

        // TODO (Member 2 / Backend integration): Replace immediate "repaired" alert
        // with a report-submitted confirmation, and trigger station-serviced alerts
        // only when a station's status changes to serviced/repaired.
        NotificationHelper.triggerStationRepairedAlert(
                this,
                report.getReportId(),
                report.getStationId(),
                report.getIssueCategory()
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_PERMISSIONS) {
            fetchCurrentLocation();
        }
    }
}