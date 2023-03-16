package n03.group3.alarmapp;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CountDownActivity extends AppCompatActivity {

    TextView timerTextView;
    SeekBar timerSeekBar;
    Boolean counterIsActive = true;
    Button goBtn;
    Button stopBtn, backBtn;
    Button resetBtn;
    CountDownTimer countDownTimer;
    long timeRemaining;
    int timeStart;
    int timeStartCurrent;
    CharSequence currentTimerStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);

        timerSeekBar = findViewById(R.id.timeSeekBar);
        timerTextView = findViewById(R.id.timerTextView);
        goBtn = findViewById(R.id.goBtn);
        backBtn = findViewById(R.id.backBtn);
        timerSeekBar.setMax(60 * 60);
        timerSeekBar.setProgress(30);
        timeStart = timerSeekBar.getProgress();
        timeStartCurrent = timerSeekBar.getProgress();
        stopBtn = findViewById(R.id.stopBtn);
        resetBtn = findViewById(R.id.resetBtn);


        timerSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                updateTimer(i);
                timeStart = timerSeekBar.getProgress();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counterIsActive) {
                    currentTimerStr = timerTextView.getText();
                    timeStartCurrent = timerSeekBar.getProgress();
                    startCountdown(v);
                    stopBtn.setVisibility(View.VISIBLE);
                    resetBtn.setVisibility(View.VISIBLE);
                } else {
                    pauseCountdown();
                    stopBtn.setVisibility(View.GONE);
                    resetBtn.setVisibility(View.GONE);
                }
            }
        });
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetCurrentTimer();
            }
        });
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void resetTimer() {
        timerTextView.setText("00:00:30");
        timerSeekBar.setProgress(30);
        timerSeekBar.setEnabled(true);
        countDownTimer.cancel();
        goBtn.setText("GO");
        counterIsActive = true;
        timeRemaining = 0;
    }

    public void startCountdown(View view) {
        timerSeekBar.setEnabled(false);
        goBtn.setText("Pause");
        stopBtn.setText("Cancel");
        countDownTimer = new CountDownTimer(timeStart * 1000 + 100, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeRemaining = millisUntilFinished / 1000; // lưu giữ thời gian đã trôi qua
                updateTimer(timeRemaining); // cập nhật thời gian đếm ngược
            }

            @Override
            public void onFinish() {
                MediaPlayer mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.airhorn);
                mPlayer.start();
                resetCurrentTimer();
            }
        }.start();
        counterIsActive = false;
    }

    public void pauseCountdown() {
        countDownTimer.cancel();
        goBtn.setText("Continue");// cập nhật thời gian còn lại
        timeStart = (int) timeRemaining;
        counterIsActive = true;
    }

    public void updateTimer(long Time) {
        long hours = (Time / 3600);
        long minutes = ((Time % 3600) / 60);
        long seconds =  (Time % 60);
        String hourString = Long.toString(hours);
        String minuteString = Long.toString(minutes);
        String secondString = Long.toString(seconds);
        if (hours <= 9) {
            hourString = "0" + hourString;
        }
        if (minutes <= 9) {
            minuteString = "0" + minuteString;
        }
        if (seconds <= 9) {
            secondString = "0" + secondString;
        }
        timerTextView.setText(hourString + ":" + minuteString + ":" + secondString);
    }

    public void resetCurrentTimer() {
        timerTextView.setText(currentTimerStr);
        timerSeekBar.setProgress(timeStartCurrent);
        countDownTimer.cancel();
        goBtn.setText("GO");
        counterIsActive = true;
    }
}
