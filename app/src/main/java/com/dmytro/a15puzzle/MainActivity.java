package com.dmytro.a15puzzle;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private GridLayout gridLayout;
    private TextView timerTextView, winMessage;
    private Button startButton, restartButton;
    private CountDownTimer countDownTimer;
    private boolean isGameStarted = false;
    private long startTime;
    private List<Button> buttonList = new ArrayList<>();
    private Integer[] puzzle = new Integer[16];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridLayout = findViewById(R.id.gridLayout);
        timerTextView = findViewById(R.id.timerTextView);
        winMessage = findViewById(R.id.winMessage);
        startButton = findViewById(R.id.startButton);
        restartButton = findViewById(R.id.restartButton);
        startButton.setOnClickListener(v -> startNewGame());
        restartButton.setOnClickListener(v -> startNewGame());
        restartButton.setVisibility(View.INVISIBLE);
        winMessage.setVisibility(View.INVISIBLE);
        createGridButtons();
    }
    private void createGridButtons() {
        for (int i = 0; i < 16; i++) {
            Button button = new Button(this);
            button.setTextSize(24);
            button.setPadding(8, 8, 8, 8);
            button.setAllCaps(false);
            button.setOnClickListener(this::onTileClick);
            buttonList.add(button);
            gridLayout.addView(button, new GridLayout.LayoutParams());
        }
    }
    private void startNewGame() {
        winMessage.setVisibility(View.INVISIBLE);
        restartButton.setVisibility(View.INVISIBLE);
        for (int i = 0; i < 16; i++) {
            puzzle[i] = i + 1;
        }
        puzzle[15] = 0;
        shufflePuzzle();
        updateButtons();
        startTime = System.currentTimeMillis();
        startTimer();
        isGameStarted = true;
        startButton.setVisibility(View.INVISIBLE);
    }
    private void shufflePuzzle() {
        List<Integer> puzzleList = new ArrayList<>();
        for (int num : puzzle) {
            puzzleList.add(num);
        }
        Collections.shuffle(puzzleList);
        puzzleList.toArray(puzzle);
    }
    private void updateButtons() {
        for (int i = 0; i < 16; i++) {
            Button button = buttonList.get(i);
            if (puzzle[i] == 0) {
                button.setText("");
                button.setEnabled(false);
            } else {
                button.setText(String.valueOf(puzzle[i]));
                button.setEnabled(true);
            }
        }
    }
    private void onTileClick(View view) {
        if (!isGameStarted) return;
        int clickedIndex = buttonList.indexOf(view);
        int emptyIndex = getEmptyTilePosition();
        if (isAdjacent(clickedIndex, emptyIndex)) {
            puzzle[emptyIndex] = puzzle[clickedIndex];
            puzzle[clickedIndex] = 0;
            updateButtons();
            if (isWinningState()) {
                stopGame();
                winMessage.setVisibility(View.VISIBLE);
                restartButton.setVisibility(View.VISIBLE);
            }
        }
    }
    private int getEmptyTilePosition() {
        for (int i = 0; i < puzzle.length; i++) {
            if (puzzle[i] == 0) return i;
        }
        return -1;
    }
    private boolean isAdjacent(int index1, int index2) {
        int row1 = index1 / 4, col1 = index1 % 4;
        int row2 = index2 / 4, col2 = index2 % 4;
        return (Math.abs(row1 - row2) + Math.abs(col1 - col2)) == 1;
    }
    private boolean isWinningState() {
        for (int i = 0; i < 15; i++) {
            if (puzzle[i] != i + 1) return false;
        }
        return puzzle[15] == 0;
    }
    private void startTimer() {
        countDownTimer = new CountDownTimer(Long.MAX_VALUE, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long elapsed = System.currentTimeMillis() - startTime;
                int seconds = (int) (elapsed / 1000) % 60;
                int minutes = (int) ((elapsed / (1000 * 60)) % 60);
                timerTextView.setText(String.format("Time: %02d:%02d", minutes, seconds));
            }
            @Override
            public void onFinish() {
            }
        }.start();
    }
    private void stopGame() {
        isGameStarted = false;
        countDownTimer.cancel();
    }
}