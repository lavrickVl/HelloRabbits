package com.example.hellorabbits;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


public class Main2Activity extends AppCompatActivity {

    MediaPlayer mediaPlayer = null;
    MediaPlayer errorPlayer = null;
    MediaPlayer birthdayPlayer = null;
    AudioManager audioManager;
    AFListener afListenerMusic;

    Calendar fDate = new GregorianCalendar();
    Calendar currentTime = Calendar.getInstance();

    TextView time;
    TextView ftime;
    TextView compareView;
    TextView headTxt;
    TextView age;
    ImageView imageView;

    String ftimeTxt;
    String todayTimeTxt;
    int birthdayMonth = Calendar.DECEMBER;
    int birthdayDate = 31;
    int birthdayYear = 1949;

    String gender = " sister ";   // or bro

    int todayYear = currentTime.get(Calendar.YEAR);
    int trueAge = currentTime.get(Calendar.YEAR) - birthdayYear-1;
    long result;

    CountDownTimer mCountDownTimer;  // timer initializing

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mediaPlayer = MediaPlayer.create(this, R.raw.song);
        errorPlayer = MediaPlayer.create(this, R.raw.error);
        birthdayPlayer = MediaPlayer.create(this, R.raw.birthday);
        afListenerMusic = new AFListener(mediaPlayer);
        audioManager.requestAudioFocus(afListenerMusic, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

        displayOk();
        startTimer();
    }

    public void displayOk() {

        time = (TextView) findViewById(R.id.realTime);
        ftime = (TextView) findViewById(R.id.futureTime);
        compareView = (TextView) findViewById(R.id.comparing_days);
        headTxt = (TextView) findViewById(R.id.headMessage);
        imageView = (ImageView) findViewById(R.id.imageView);
        age = (TextView) findViewById(R.id.your_age);

        // TextView testRes = (TextView) findViewById(R.id.lvl_now);
        //   date.set(y,m,1,12,0,0);

        fDate.set(todayYear, birthdayMonth, birthdayDate, 16, 20 , 00);  // set your birthday

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("y, EEE, d MMMM, kk : mm", Locale.getDefault());  //for formatting and parsing dates in a locale-sensitive manner

//        Date date = fDate.getTime();
//        testRes.setText(" "+ date);

        // fDate.add(Calendar.MONTH, -2); // for any field you can change + -
        // fDate.roll();  for changing only in your field
        // boolean dayTrue = currentTime.equals(fDate);

        ftimeTxt = simpleDateFormat.format(fDate.getTime()); //  getTime(); // milliSec before now
        todayTimeTxt = simpleDateFormat.format(currentTime.getTime());

        result = fDate.getTimeInMillis() - currentTime.getTimeInMillis();
        time.setText(todayTimeTxt);


        if (currentTime.get(Calendar.DAY_OF_YEAR) == fDate.get(Calendar.DAY_OF_YEAR)) {

            compareView.setTextSize(20);
            compareView.setTypeface(Typeface.create("cursive",Typeface.NORMAL)); // a big riddle ?
            trueAge = (currentTime.get(Calendar.YEAR) - birthdayYear) - 1;
            headTxt.setText(String.format("Today is your birthday , %s !" , gender));
            age.setText(trueAge + "");
            age.setTextColor(getResources().getColor(R.color.colorAccent2));
            compareView.setTextColor(getResources().getColor(R.color.colorFontAct1));
            time.setText("");
            ftime.setText("");


            imageView.setImageResource(R.drawable.androidparty);

            mediaPlayer.start();
        } else {

            //       ftime.setText("time is now");
            age.setText(trueAge + "");
            imageView.setImageResource(R.drawable.error404);
            errorPlayer.start();
        }


        if (result < 0 && currentTime.get(Calendar.DAY_OF_YEAR) != fDate.get(Calendar.DAY_OF_YEAR)) {
            fDate.set(Calendar.YEAR, todayYear + 1);
            age.setText((trueAge+1) + "");
            ftimeTxt = simpleDateFormat.format(fDate.getTime());
            result = fDate.getTimeInMillis() - currentTime.getTimeInMillis();
            ftime.setText(" your birthday in next year ");


        }


    }


    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.pause();
        birthdayPlayer.pause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mediaPlayer.pause();
        birthdayPlayer.start();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (afListenerMusic != null)
            audioManager.abandonAudioFocus(afListenerMusic);
    }


    class AFListener implements AudioManager.OnAudioFocusChangeListener {

        MediaPlayer mp;

        public AFListener(MediaPlayer mp) {

            this.mp = mp;
        }

        @Override
        public void onAudioFocusChange(int focusChange) {
            String event = "";
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_LOSS:
                    mp.pause();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    mp.pause();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    mp.setVolume(0.3f, 0.3f);
                    break;
                case AudioManager.AUDIOFOCUS_GAIN:
                    if (!mp.isPlaying())
                        mp.start();
                    mp.setVolume(1.0f, 1.0f);
                    break;
            }
        }
    }


    public void updateCountDownText() {
//        int hours= (int)((result/1000)/60)/60;
//        int minutes= (int)(result/1000)/60;
//        int seconds= (int)(result/1000)%60;
//        String timeLeftFormatted = String.format(Locale.getDefault(), "%d : %02d : %02d", hours, minutes, seconds );

        currentTime = Calendar.getInstance();

        int days = (fDate.get(Calendar.DAY_OF_YEAR) - currentTime.get(Calendar.DAY_OF_YEAR));

        if (days < 0) {
            Calendar dec31 = new GregorianCalendar(2020, Calendar.DECEMBER, 31);
            days = fDate.get(Calendar.DAY_OF_YEAR) + (dec31.get(Calendar.DAY_OF_YEAR) - (currentTime.get(Calendar.DAY_OF_YEAR)));
        }   // for situation when birthday have finished

        //  Log.d("myLog", ""+(fDate.get(Calendar.DAY_OF_YEAR)+" - "+currentTime.get(Calendar.DAY_OF_YEAR)));
        int hour = (fDate.get(Calendar.HOUR) - currentTime.get(Calendar.HOUR));
        int min = (fDate.get(Calendar.MINUTE) - (currentTime.get(Calendar.MINUTE)));
        int sec = (fDate.get(Calendar.SECOND) - currentTime.get(Calendar.SECOND));

        int a, b, c, d;
        a = days;
        b = hour;
        c = min;
        d = sec;

        if (d < 0) {
            d = 60 + d;
            c = c - 1;
        }

        if (c < 0) {
            c = 60 + c;
            b = b - 1;
        }
        if (b < 0) {
            a = a - 1;
            b = 24 + b;
        }

        String updatingTimer = a + " days : " + b + " hours " + c + " minutes " + d + " sec ";
        compareView.setText(updatingTimer);
    }

    private void startTimer() {
        mCountDownTimer = new CountDownTimer(result, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                result = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                compareView.setTypeface(Typeface.create("cursive",Typeface.NORMAL));
                compareView.setTextSize(34);
                compareView.setText(" Wish you good health, happiness and success !!! ");
                age.setText(trueAge + 1 + "");
                mediaPlayer.stop();
                birthdayPlayer.start();
            }
        };
        mCountDownTimer.start();
    }


}








