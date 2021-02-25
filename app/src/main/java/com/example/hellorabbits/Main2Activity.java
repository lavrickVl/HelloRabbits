package com.example.hellorabbits;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class Main2Activity extends AppCompatActivity {

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
    String gender;
    int todayYear;
    int trueAge;
    int birthdayDate;
    int birthdayMonth;
    int birthdayYear;
    long result;

    CountDownTimer mCountDownTimer;  // timer initializing

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        errorPlayer = MediaPlayer.create(this, R.raw.error);
        birthdayPlayer = MediaPlayer.create(this, R.raw.birthday);
        afListenerMusic = new AFListener(birthdayPlayer);
        audioManager.requestAudioFocus(afListenerMusic, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

        SharedPreferences shPref = PreferenceManager.getDefaultSharedPreferences(this);
        birthdayDate = shPref.getInt("day_ed", 0);
        birthdayMonth = shPref.getInt("month_ed", 0) - 1; // Calendar start with 0
        birthdayYear = shPref.getInt("year_ed", 0);

        if (shPref.getBoolean("sex",true)) gender = " sister ";   // or bro
        else gender =  "bro";

        todayYear = currentTime.get(Calendar.YEAR);
        trueAge = currentTime.get(Calendar.YEAR) - birthdayYear-1;

        displayOk();
    }

    public void displayOk() {
        time = (TextView) findViewById(R.id.realTime);
        ftime = (TextView) findViewById(R.id.futureTime);
        compareView = (TextView) findViewById(R.id.comparing_days);
        headTxt = (TextView) findViewById(R.id.headMessage);
        imageView = (ImageView) findViewById(R.id.imageView);
        age = (TextView) findViewById(R.id.your_age);
        fDate.set(todayYear, birthdayMonth, birthdayDate, 0, 0, 0);  // set your birthday time
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("y, EEE, d MMMM, kk : mm", Locale.getDefault());  //for formatting and parsing dates in a locale-sensitive manner
        ftimeTxt = simpleDateFormat.format(fDate.getTime()); //  getTime(); // milliSec before now
        todayTimeTxt = simpleDateFormat.format(currentTime.getTime());
        result = fDate.getTimeInMillis() - currentTime.getTimeInMillis();
        time.setText(todayTimeTxt);

        if (currentTime.get(Calendar.DAY_OF_YEAR) == fDate.get(Calendar.DAY_OF_YEAR)) {  // +
            compareView.setTextSize(20);
            compareView.setTypeface(Typeface.create("cursive",Typeface.NORMAL)); // a big riddle ?
            trueAge = (currentTime.get(Calendar.YEAR) - birthdayYear);
            headTxt.setText(String.format("Today is your birthday , %s !" , gender));
            age.setText("" + trueAge);
            age.setTextColor(getResources().getColor(R.color.colorAccent2));
            compareView.setTextColor(getResources().getColor(R.color.colorFontAct1));
            time.setText("");
            ftime.setText("");
            compareView.setText("");
            imageView.setImageResource(R.drawable.androidparty);
            compareView.setTypeface(Typeface.create("cursive",Typeface.NORMAL));
            compareView.setTextSize(34);
            compareView.setText(R.string.wish_birthday);
            birthdayPlayer.start();
        } else {
            age.setText("" + trueAge);
            imageView.setImageResource(R.drawable.error404);
            errorPlayer.start();

            if (result < 0 && currentTime.get(Calendar.DAY_OF_YEAR) != fDate.get(Calendar.DAY_OF_YEAR)) {
                fDate.set(Calendar.YEAR, todayYear + 1);
                age.setText("" + (trueAge+1));
                ftimeTxt = simpleDateFormat.format(fDate.getTime());
                result = fDate.getTimeInMillis() - currentTime.getTimeInMillis();
                ftime.setText(" your birthday in next year ");
            }

            startTimer();
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
        birthdayPlayer.pause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
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
        currentTime = Calendar.getInstance();
        int days = fDate.get(Calendar.DAY_OF_YEAR) - currentTime.get(Calendar.DAY_OF_YEAR);
        //  Log.d("myLog", ""+(fDate.get(Calendar.DAY_OF_YEAR)+" - "+currentTime.get(Calendar.DAY_OF_YEAR)));
        int hour =  0;
        int min  =  0;
        int sec  =  0;

        if (days >= 0) {                               // today than birthday, in this year -> var_1
            days -= 1;
            hour =  23 - currentTime.get(Calendar.HOUR_OF_DAY);
            min  =  59 - currentTime.get(Calendar.MINUTE);
            sec  =  59 - currentTime.get(Calendar.SECOND);
        } else  {                                      // birthday in next year -> var_2
                days = fDate.get(Calendar.DAY_OF_YEAR) + currentTime.getActualMaximum(Calendar.DAY_OF_YEAR) - currentTime.get(Calendar.DAY_OF_YEAR);
                hour =  23 - currentTime.get(Calendar.HOUR_OF_DAY);
                min  =  59 - currentTime.get(Calendar.MINUTE);
                sec  =  59 - currentTime.get(Calendar.SECOND);
        }
        String updatingTimer = days + " days : " + hour + " hours " + min + " minutes " + sec + " sec ";
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
                compareView.setText(R.string.wish_birthday);
                age.setText(trueAge + 1);
                birthdayPlayer.start();
            }
        };
        mCountDownTimer.start();
    }
}








