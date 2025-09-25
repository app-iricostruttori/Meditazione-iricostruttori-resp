package com.iricostruttori.meditazione.ui.meditazione;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
//import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
//import android.widget.Toast;
import android.os.CountDownTimer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

//import com.iricostruttori.meditazione.databinding.FragmentReflowBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.iricostruttori.meditazione.R;
import com.iricostruttori.meditazione.databinding.FragmentMeditazioneBinding;

import java.util.StringTokenizer;

public class MeditazioneFragment extends Fragment {

    //private FragmentReflowBinding binding;
    private FragmentMeditazioneBinding binding;
    private CountDownTimer countdownTimer;
    private TextView timeLeft;
    private  Button startBtn;

    private Button stopBtn ;
    private Button subMinutesBtn;
    private Button sumMinutesBtn;
    private EditText editTimeInput;
    private TextView textViewHeader;
    MediaPlayer player_start;
    MediaPlayer player_end;

    // Parametrizzare il default nel Fragment settings
    private long timeLeftInMs = 1 * 60000 ; //default value
    //private long timeLeftInMsOriginal = 1 ;
    private int timeInMinutes = 1 ;
    private boolean timerRunning = false;
    private boolean flagPauseOn = false;

    private SharedPreferences mPreferences;
    //private SharedPreferences.Editor mEditor;
    private final String SHARED_SETTING_VALUE = "SHARED_SETTING_VALUE" ;
    private final String SHARED_TIME_DEFAULT = "SHARED_TIME_DEFAULT" ;
    //private Window win ;

    int uiOptionsDefault = 0 ;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Start Presi da master sample
        getActivity().setTheme(R.style.Theme_Meditazioneiricostruttoriresp);
        // remove notifications bar
        uiOptionsDefault = getDefaultScreen();

        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        // End Presi da master sample

        binding = FragmentMeditazioneBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //textViewHeader = binding.textMeditazione;
        //textViewHeader.setText("I Ricostruttori");

        final ImageView imageView = binding.imageViewMeditazione;

        //meditazioneViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        timeLeft = binding.timeLeft;
        startBtn = binding.startBtn;
        stopBtn = binding.stopBtn;
        subMinutesBtn = binding.subMinutesBtn;
        sumMinutesBtn = binding.sumMinutesBtn;
        editTimeInput = binding.editTimeInput;

        editTimeInput.setVisibility(View.INVISIBLE);

        mPreferences = getContext().getSharedPreferences(SHARED_SETTING_VALUE, Context.MODE_PRIVATE);



        // Lo uso anche nel reset
        // ma non nei pulsanti  + e -
        initTime();
        updateTimer();

        // On Click Listeners
        sumMinutesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setTime(parseSumSubEqTime(editTimeInput.getText().toString(),'+'));
                updateTimer();

            }
        });

        // On Click Listeners
        subMinutesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setTime(parseSumSubEqTime(editTimeInput.getText().toString(),'-'));
                updateTimer();

            }
        });

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //vibrate.vibrate(50);

                startStop();
                sumMinutesBtn.setVisibility(View.INVISIBLE);
                subMinutesBtn.setVisibility(View.INVISIBLE);
                editTimeInput.setVisibility(View.INVISIBLE);


            }
        });

        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (player_start.isPlaying())
                    player_start.stop();
                if (player_end.isPlaying())
                    player_end.stop();
                stopTimer();

            }
        });

        player_start = MediaPlayer.create(getActivity(), R.raw.inizio_med);
        player_end = MediaPlayer.create(getActivity(), R.raw.fine_med_gurupuja);

        return root;
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
        binding = null;
    }

    private void startStop() {

        if (timerRunning) {
            stopTimer();
        } else {
            startTimer();
        }

    }

    private void startTimer() {

        // Solo se non è una pausa
        if (!flagPauseOn) {

            System.out.println("[startTimer] Start ");
            set_keep_screen_on_off(true);
            setFullScreenCall();

            System.out.println("[startTimer] Player Start ");
            player_start.start();
            while (player_start.isPlaying()) {
                System.out.println("[startTimer] Wait Starting song ");
            }

            System.out.println("[startTimer] Wait End ");
            ;
            /* Da grossi problemi relativi alla disattivazione dello
               schermo
            try {
                Thread.sleep(50000);
                //TimeUnit.SECONDS.sleep(50);
            } catch (InterruptedException e) {

            }
            */
        }

        System.out.println("[startTimer] countTimer Start ");
        countdownTimer = new CountDownTimer(timeLeftInMs, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                hideWhileMeditating();
                timeLeftInMs = millisUntilFinished;
                System.out.println("[onTick] CountDownTimer -> " + timeLeftInMs);
                int seconds = (int) timeLeftInMs % 60000 / 1000;
                System.out.println("[onTick] CountDownTimer -> " + seconds);

                boolean isMultiple5 = ( seconds%5 == 0 ) ? true : false;

                System.out.println("[onTick] isMultiple5 -> " + isMultiple5);

                // Deve essere < del tempo di sospensione dello schermo
                // sul mio telefono settato a 30 ma può essere anche 15
                if (isMultiple5) {
                    System.out.println("[onTick] CountDownTimer Tengo attivo lo schermo ogni 30 sec " );

                    set_keep_screen_on_off(true);

                }

                updateTimer();

            }

            @Override
            public void onFinish() {
                // some sound, record data, some message
                //vibrate.vibrate(2000);

                System.out.println("[onFinish] Player start " );
                player_end.start();

                while (player_end.isPlaying()) {
                    System.out.println("[onFinish] Wait Ending Song ");
                }

                System.out.println("[onFinish] Wait End ");

                /* da problemi
                try {
                    Thread.sleep(50000);
                }
                catch(InterruptedException e) {

                }
                */
                System.out.println("[onFinish] Wait finish " );

                showAfterMeditating();
                startBtn.setText("Meditazione");
                initTime();
                timerRunning = false;
                flagPauseOn = false;

                set_keep_screen_on_off(false);
                setDefaultScreen();
            }
        }.start();

        if (timeLeftInMs > 0) {
            startBtn.setText("Pausa");
            timerRunning = true;
            flagPauseOn = true;

        }

    }

    public long parseSumSubEqTime (String input , char op) {

        StringTokenizer st = new StringTokenizer(input,":");
        String strMinute = st.nextToken();
        // op = '+' , '=' , '-'
        if (op == '+')
            timeInMinutes = Integer.valueOf(strMinute) + 1;
        else if (op == '-')
            timeInMinutes = Integer.valueOf(strMinute) - 1;
        else
            timeInMinutes = Integer.valueOf(strMinute) ;

        long msInput = timeInMinutes * 60000;
        return msInput;
    }
    private void initTime() {

        String strTimeDefault = mPreferences.getString(SHARED_TIME_DEFAULT, null);
        System.out.println("[MeditazioneFragment.reset] strTimeDefault " + strTimeDefault);
        if (strTimeDefault == null || strTimeDefault.equals(""))
            strTimeDefault = Resources.getSystem().getString( R.string.timeDefaultInitial) ;

        timeLeft.setText(strTimeDefault);
        editTimeInput.setText(strTimeDefault);

        setTime(parseSumSubEqTime(strTimeDefault,'='));
    }
    private void setTime(long milliseconds) {
        timeLeftInMs = milliseconds;
    }

    private void updateTimer() {
        int minutes = (int) timeLeftInMs / 60000; // in ms
        int seconds = (int) timeLeftInMs % 60000 / 1000;

        String timeLeftText = "" + minutes;
        timeLeftText += ":";
        if (seconds < 10) {
            timeLeftText += "0";
        }
        timeLeftText += seconds;
        System.out.println("updateTimer -> " + timeLeftText);

        timeLeft.setText(timeLeftText);
        editTimeInput.setText(timeLeftText);
    }

    private void stopTimer() {

        showAfterMeditating();
        startBtn.setText("Meditazione");
        // Attenzione alla gestione della pausa
        timerRunning = false;

        countdownTimer.cancel();

    }

    private void hideWhileMeditating() {

        //settingsBtn.setVisibility(View.INVISIBLE);
        sumMinutesBtn.setVisibility(View.INVISIBLE);
        subMinutesBtn.setVisibility(View.INVISIBLE);
        //stopBtn.setVisibility(View.INVISIBLE);
        //textViewHeader.setVisibility(View.INVISIBLE);

        BottomNavigationView bottomNavigationView =
                (BottomNavigationView) getActivity().findViewById(R.id.bottom_nav_view);

        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(View.INVISIBLE);
        }
    }

    private void showAfterMeditating() {

        BottomNavigationView bottomNavigationView =
                (BottomNavigationView) getActivity().findViewById(R.id.bottom_nav_view);

        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(View.VISIBLE);
        }

        //settingsBtn.setVisibility(View.VISIBLE);
        sumMinutesBtn.setVisibility(View.VISIBLE);
        subMinutesBtn.setVisibility(View.VISIBLE);
        // per ora lo lascio invisibile
        //resetBtn.setVisibility(View.VISIBLE);

    }

    void set_keep_screen_on_off(boolean flagKeepScreenOn) {

        /*
                getActivity().getWindow().clearFlags( WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON );
                */
        getActivity().getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON );

        if (flagKeepScreenOn) {
            /* getActivity().getWindow().addFlags( WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                            WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                            WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                            WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON );
            */
            getActivity().getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                            WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON );
        }

    }

    public void setFullScreenCall() {
        if(Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = getActivity().getWindow().getDecorView();
            System.out.println("[setFullScreencall] start sdk 11-19 " + v.getSystemUiVisibility());

            v.setSystemUiVisibility(View.GONE);

        } else if(Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getActivity().getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            System.out.println("[setFullScreencall] start sdk >=19 " + decorView.getSystemUiVisibility());
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    public int getDefaultScreen() {
        View decorView = getActivity().getWindow().getDecorView();
        System.out.println("[getDefaultScreen] " + decorView.getSystemUiVisibility());
        return decorView.getSystemUiVisibility();
    }

    public void setDefaultScreen() {

            View v = getActivity().getWindow().getDecorView();
            System.out.println("[fullScreencall] uiOptionsDefault " + uiOptionsDefault);
            v.setSystemUiVisibility(uiOptionsDefault);


    }

}