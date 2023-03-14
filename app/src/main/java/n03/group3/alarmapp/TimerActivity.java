package n03.group3.alarmapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class TimerActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView tvTime;
    private Button btnStart;
    private Button btnStop;
    private Button btnReset;
    private Button btnBack;
    private static final int REQUEST_CODE = 1000;

    private boolean isRunning = false;
    private long startTime = 0L;

    private long elapsedTime = 0;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        tvTime = findViewById(R.id.tvTime);
        btnStart = findViewById(R.id.btnStart);
        btnStop = findViewById(R.id.btnStop);
        btnReset = findViewById(R.id.btnReset);
        btnBack = findViewById(R.id.btnBack);

        btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnReset.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }

    private void updateTimerText(long elapsedTime) {
        long minutes = (elapsedTime / 1000) / 60;
        long seconds = (elapsedTime / 1000) % 60;
        long milliseconds = elapsedTime % 1000;
        tvTime.setText(String.format(Locale.getDefault(), "%02d:%02d:%03d", minutes, seconds, milliseconds));
    }

    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            elapsedTime = System.currentTimeMillis() - startTime;
            updateTimerText(elapsedTime);
            handler.postDelayed(this, 10);
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnStart:
                if (!isRunning) {
                    startTime = System.currentTimeMillis() - elapsedTime;
                    handler.postDelayed(timerRunnable, 0);
                    isRunning = true;
                }
                break;
            case R.id.btnStop:
                if (isRunning) {
                    handler.removeCallbacks(timerRunnable);
                    isRunning = false;
                    elapsedTime = System.currentTimeMillis() - startTime;
                }
                break;
            case R.id.btnReset:
                if (!isRunning) {
                    elapsedTime = 0;
                    updateTimerText(elapsedTime);
                }
                break;
            case R.id.btnBack:
                if (!isRunning) {
                    elapsedTime = 0;
                    updateTimerText(elapsedTime);
                }
                onBackPressed();
//                Intent toMain = new Intent(TimerActivity.this, MainActivity.class);
//                startActivityForResult(toMain, REQUEST_CODE);
                break;
            default:
                break;
        }
    }
}
