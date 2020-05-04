package com.example.hellorabbits;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {

    Button button;
    Switch switch2;
    Main2Activity main2Activity = new Main2Activity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);
        switch2 = (Switch) findViewById(R.id.switch1);

       int a = main2Activity.birthdayMonth;   // setings on M2Activity
        int b = main2Activity.birthdayDate;
       String dateSwitch = a+"."+b ;
        switch2.setText(dateSwitch);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (switch2.isChecked()) {
                    Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                    startActivity(intent);
                }
            }
        });

    }
}
