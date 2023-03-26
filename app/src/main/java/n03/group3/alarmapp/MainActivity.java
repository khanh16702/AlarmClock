package n03.group3.alarmapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static String activeAlarm  = "";
    private ListView listView;
    private Button btnAdd;
    private Button btnTimer;
    private Button btnCountDown;
    private TextView txtWelcome;
    private int SelectedAlarmId;
    private static final int REQUEST_CODE = 1000;

    public static List<Alarm> alarmList = new ArrayList<>();
    private CustomAdapter customAdapter;
    private DatabaseHelper db = new DatabaseHelper(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtWelcome = findViewById(R.id.txtWelcome);
        btnTimer = findViewById(R.id.timer);
        btnAdd = findViewById(R.id.add);
        btnCountDown = findViewById(R.id.countdown);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        listView = findViewById(R.id.listView);
        List<Alarm> list = db.getAllAlarms();
        alarmList.addAll(list);
        customAdapter = new CustomAdapter(getApplicationContext(), alarmList);
        listView.setAdapter(customAdapter);

        if (listView.getCount() > 0) {
            txtWelcome.setText("");
        }
        else {
            txtWelcome.setText("Welcome To Alarm App");
        }

        registerForContextMenu(listView);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                SelectedAlarmId = i;
                return false;
            }
        });

        createNotificationChannel();

        // Timer
        btnTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toTimer = new Intent(MainActivity.this, TimerActivity.class);
                startActivityForResult(toTimer, REQUEST_CODE);
            }
        });

        btnCountDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toCountDown = new Intent(MainActivity.this, CountDownActivity.class);
                startActivityForResult(toCountDown, REQUEST_CODE);
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnuChangeTime:
                Intent intent = new Intent(MainActivity.this, AddActivity.class);

                Alarm a = alarmList.get(SelectedAlarmId);
                Bundle b = new Bundle();
                b.putInt("id", a.getId());
                b.putString("name", a.getName());
                b.putInt("hour", a.getHour());
                b.putInt("minute", a.getMinute());
                b.putBoolean("status", a.getStatus());
                intent.putExtras(b);
                startActivityForResult(intent, 200);
                break;

            case R.id.mnuDelete:
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Confirm");
                alert.setMessage("Delete this alarm?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Clear all previous alarm service
                        Context context = getApplicationContext();
                        final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                        final Intent serviceIntent = new Intent(context, AlarmReceiver.class);
//                        serviceIntent.putExtra("alarmName", alarmList.get(i).getName());
//                        serviceIntent.putExtra("extra", "off");
//                        serviceIntent.putExtra("active", "");
                        Log.d("index = ", i + "");
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, SelectedAlarmId, serviceIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT);
                        alarmManager.cancel(pendingIntent);

                        Alarm a = alarmList.get(SelectedAlarmId);
                        db.deleteAlarm(a.getId());
                        alarmList.clear();
                        List<Alarm> list = db.getAllAlarms();
                        alarmList.addAll(list);
                        customAdapter.notifyDataSetChanged();
                        if (listView.getCount() > 0) {
                            txtWelcome.setText("");
                        }
                        else {
                            txtWelcome.setText("Welcome To Alarm App");
                        }
                    }
                });
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog al = alert.create();
                al.show();
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            boolean needRefresh = data.getExtras().getBoolean("needRefresh");
            Log.d("refresh", "refresh called");
            if (needRefresh) {
                alarmList.clear();
                List<Alarm> list = db.getAllAlarms();
                alarmList.addAll(list);
                customAdapter.notifyDataSetChanged();
                if (listView.getCount() > 0) {
                    txtWelcome.setText("");
                }
                else {
                    txtWelcome.setText("Welcome To Alarm App");
                }
            }
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "name";
            String desc = "Channel for Alarm Manager";
            int imp = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("alarmChannel", name, imp);
            channel.setDescription(desc);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}