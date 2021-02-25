package com.example.hellorabbits;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {

    Button button;
    Switch switch_main;
    String dateSwitch;
    SharedPreferences shPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.button);
        switch_main = (Switch) findViewById(R.id.switch_main);
        shPref = PreferenceManager.getDefaultSharedPreferences(this);
        int a = shPref.getInt("day_ed", 0);
        int b = shPref.getInt("month_ed", 0);
        dateSwitch = a + "." + b;
        switch_main.setText(dateSwitch);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switch_main.isChecked()) {
                    Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
