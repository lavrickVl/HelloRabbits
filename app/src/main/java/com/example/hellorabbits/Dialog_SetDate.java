package com.example.hellorabbits;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
public class Dialog_SetDate extends AppCompatActivity {
    SharedPreferences shPref;
    static String key_enter = "first_enter";
    EditText day_ed;
    EditText month_ed;
    EditText year_ed;
    Switch sex_switch;
    Intent intent; // to next activity
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_birhday);
        shPref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean first_start = shPref.getBoolean(key_enter, true);

        intent = new Intent(Dialog_SetDate.this, MainActivity.class);
        final TextView message = findViewById(R.id.message);
        Button save_btn = findViewById(R.id.save);
         day_ed = findViewById(R.id.enter_day);
         month_ed = findViewById(R.id.enter_month);
         year_ed = findViewById(R.id.enter_year);
         sex_switch = findViewById(R.id.sex_switch);

         sex_switch.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if (sex_switch.isChecked()) {
                     sex_switch.setText("female");
                 } else {
                     sex_switch.setText("male");
                 }
             }
         });


        if (!first_start) startActivity(intent); // not first start, go to next Activity

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int day = 0;
                int month = 0;
                int year = 0;
                boolean sex = true; //female or male(false)
                try {
                    day = Integer.parseInt(day_ed.getText().toString());
                    month = Integer.parseInt(month_ed.getText().toString());
                    year = Integer.parseInt(year_ed.getText().toString());
                    sex = sex_switch.isChecked();
                } catch (NumberFormatException ex) {
                    ex.getMessage();
                }

                String msg = day + "." + month + "." + year;
                if (checkDate(day, month, year)) {
                    message.setText(msg);

                    SharedPreferences.Editor editor = shPref.edit();
                    editor.putBoolean(key_enter, false);
                    editor.putInt("day_ed", day);
                    editor.putInt("month_ed", month);
                    editor.putInt("year_ed", year);
                    editor.putBoolean("sex", sex);
                    editor.apply();
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(),"Incorrect input", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    public boolean checkDate(int day , int month, int year) {
        boolean checkDay = false;
        boolean checkMonth = false;
        boolean checkYear = false;
        if (day > 0 && day <= 31) checkDay = true;
        if (month > 0 && month <= 12) checkMonth = true;
        if (year > 1900 && year <= 2020) checkYear = true;
        return checkDay && checkMonth && checkYear;
    }
}


