package com.um.safesip.member4;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.um.safesip.R;

import java.util.List;

public class NotificationHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_history);

        RecyclerView rvNotifications = findViewById(R.id.rvNotifications);
        TextView tvEmptyState = findViewById(R.id.tvEmptyNotifications);

        List<NotificationRecord> records = NotificationHelper.getHistory();

        if (records.isEmpty()) {
            tvEmptyState.setVisibility(View.VISIBLE);
            rvNotifications.setVisibility(View.GONE);
        } else {
            tvEmptyState.setVisibility(View.GONE);
            rvNotifications.setVisibility(View.VISIBLE);
            rvNotifications.setLayoutManager(new LinearLayoutManager(this));
            rvNotifications.setAdapter(new NotificationAdapter(records));
        }
    }
}