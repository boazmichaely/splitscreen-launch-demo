package com.test.splitscreen;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class DummyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        TextView textView = new TextView(this);
        textView.setText("🔄 Half Screen Mode\n\nTap here to close");
        textView.setTextSize(18);
        textView.setPadding(40, 40, 40, 40);
        textView.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
        textView.setBackgroundColor(0xFFE0E0E0);
        textView.setOnClickListener(v -> finish());
        setContentView(textView);
    }
}
