package com.dmytro.a15puzzle;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
public class GameInfoActivity extends AppCompatActivity {

    private TextView timeTextView, moveCountTextView;
    private long elapsedTime;
    private int moveCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_info);
        timeTextView = findViewById(R.id.timeTextView);
        moveCountTextView = findViewById(R.id.moveCountTextView);
        elapsedTime = getIntent().getLongExtra("elapsedTime", 0);
        moveCount = getIntent().getIntExtra("moveCount", 0);
        updateInfo();
    }
    private void updateInfo() {
        int seconds = (int) (elapsedTime / 1000) % 60;
        int minutes = (int) ((elapsedTime / (1000 * 60)) % 60);
        timeTextView.setText(String.format("Time: %02d:%02d", minutes, seconds));
        moveCountTextView.setText("Moves: " + moveCount);
    }
}