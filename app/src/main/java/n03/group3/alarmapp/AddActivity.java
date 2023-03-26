package n03.group3.alarmapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class AddActivity extends AppCompatActivity {
    private TimePicker timePicker;
    private EditText editText;
    private Button buttonSave, buttonCancel;
    private Alarm alarm;
    private boolean needRefresh;
    private boolean isUpdating;
    Bundle extras;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        isUpdating = false;
        timePicker = findViewById(R.id.timePicker);
        editText = findViewById(R.id.name);
        buttonSave = findViewById(R.id.button_save);
        buttonCancel = findViewById(R.id.button_cancel);

        extras = getIntent().getExtras();
        if (extras != null) {
            timePicker.setHour(extras.getInt("hour"));
            timePicker.setMinute(extras.getInt("minute"));
            editText.setText(extras.getString("name"));
            isUpdating = true;
        }

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int hour = timePicker.getCurrentHour();
                int minute = timePicker.getCurrentMinute();
                String name = editText.getText().toString();

                boolean status = true;
                if (extras != null)
                    status = extras.getBoolean("status");

                DatabaseHelper db = new DatabaseHelper(getApplicationContext());
                alarm = new Alarm(hour, minute, status, name);

                if (isUpdating) {
                    alarm.setId(extras.getInt("id"));
                    int row = db.updateAlarm(alarm);
                    Log.d("update db", row + "");
                }
                else {
                    db.addAlarm(alarm);
                }
                needRefresh = true;
                onBackPressed();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void finish() {
        Log.d("finish called", "finish Called");
        Intent data = new Intent();
        data.putExtra("needRefresh", needRefresh);
        this.setResult(RESULT_OK, data);
        super.finish();
    }
}
