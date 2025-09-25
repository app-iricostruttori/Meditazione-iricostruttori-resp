package com.iricostruttori.meditazione.ui.meditazione;

import android.content.Context;
import android.content.SharedPreferences;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
//import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
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
import java.util.concurrent.TimeUnit;

public class MeditazioneFragment extends Fragment {


    //private FragmentReflowBinding binding;
    private FragmentMeditazioneBinding binding;
    private CountDownTimer countdownTimer;
    private TextView timeLeft;
    private Button startBtn;

    private Button stopBtn;
    private Button subMinutesBtn;
    private Button sumMinutesBtn;
    private EditText editTimeInput;
    private TextView textViewHeader;
    MediaPlayer player_start;
    MediaPlayer player_end;
    boolean isReleasedStartPlayer = false;
    boolean isReleasedEndPlayer = false;

    // Parametrizzare il default nel Fragment settings
    private SharedPreferences mPreferences;
    private final String SHARED_SETTING_VALUE = "SHARED_SETTING_VALUE";
    private final String SHARED_TIME_DEFAULT = "SHARED_TIME_DEFAULT";

    int uiOptionsDefault = 0;
    private LockMeditazione lockMeditazione;
    private RunMeditazione runMed = new RunMeditazione();
    private TouchImageView touchImageView = new TouchImageView();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        set_keep_screen_on_off(true);
        lockMeditazione = new LockMeditazione((PowerManager) getActivity().getBaseContext().getSystemService(Context.POWER_SERVICE));

        getActivity().setTheme(R.style.Theme_Meditazioneiricostruttoriresp);

        uiOptionsDefault = getDefaultScreen();

        getActivity().setTheme(R.style.Theme_Meditazioneiricostruttoriresp);
        //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // remove notifications bar
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        binding = FragmentMeditazioneBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final ImageView imageView = (ImageView)binding.imageViewMeditazione;

        timeLeft = binding.timeLeft;
        startBtn = (Button) binding.startBtn;
        stopBtn = (Button) binding.stopBtn;
        subMinutesBtn = (Button) binding.subMinutesBtn;
        sumMinutesBtn = (Button) binding.sumMinutesBtn;
        editTimeInput = binding.editTimeInput;

        editTimeInput.setVisibility(View.INVISIBLE);
        mPreferences = getContext().getSharedPreferences(SHARED_SETTING_VALUE, Context.MODE_PRIVATE);

        // Initialize the SeekBar in your Activity or Fragment:
        SeekBar seekBarMeditazione = binding.seekBarMeditazione;
        // Get the audio manager to manage volume audio
        AudioManager audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        initTime();

        runMed.strStatus = StatusMeditazione.STOP.name();
        Log.d("onCreateView", " Status Iniziale " + runMed.strStatus);

        // On Click Listeners
        sumMinutesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //setTimeLeftInMs(parseSumSubEqTime(editTimeInput.getText().toString(),'+'));
                // Aggiungo il tempo del canto iniiale
                Log.d("sumMinutesBtn", " before " + runMed.timeLeftInMs + " " + editTimeInput.getText().toString());
                long numParseEditTime = parseSumSubEqTime(editTimeInput.getText().toString(), '+');
                Log.d("sumMinutesBtn", " numParseEditTime " + numParseEditTime);
                Log.d("sumMinutesBtn", " timeInitialSongInMs " + runMed.timeInitialSongInMs);
                setTimeLeftInMs(numParseEditTime + runMed.timeInitialSongInMs);
                runMed.timeInitialSongAndMedInMs = runMed.timeLeftInMs;
                Log.d("sumMinutesBtn", " getTimeLeftInMs " + runMed.timeLeftInMs);
                Log.d("sumMinutesBtn", " getTimeLeftAuxInMs " + runMed.timeLeftAuxInMs);
                Log.d("sumMinutesBtn", " timeInitialSongAndMedInMs " + runMed.timeInitialSongAndMedInMs);

                updateTimerForSumSub();

                Log.d("sumMinutesBtn", " after " + runMed.timeLeftInMs + " " + editTimeInput.getText().toString());


            }
        });

        // On Click Listeners
        subMinutesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("subMinutesBtn", " before " + runMed.timeLeftInMs + " " + editTimeInput.getText().toString());
                long numParseEditTime = parseSumSubEqTime(editTimeInput.getText().toString(), '-');

                Log.d("subMinutesBtn", " numParseEditTime " + numParseEditTime);
                setTimeLeftInMs(numParseEditTime + runMed.timeInitialSongInMs);
                runMed.timeInitialSongAndMedInMs = runMed.timeLeftInMs;

                updateTimerForSumSub();
                Log.d("subMinutesBtn", " after " + runMed.timeLeftInMs + " " + editTimeInput.getText().toString());


            }
        });

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //vibrate.vibrate(50);
                sumMinutesBtn.setVisibility(View.INVISIBLE);
                subMinutesBtn.setVisibility(View.INVISIBLE);
                editTimeInput.setVisibility(View.INVISIBLE);
                startOrPause();

            }
        });

        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("stopBtn.click ", " start button stop ");

                if (player_start != null) {
                    player_start.stop();
                    player_start.release();
                    player_start = null;
                }

                if (player_end != null) {
                    player_end.stop();
                    player_end.release();
                    player_end = null;
                }
                stopTimer();
            }
        });

        // Set the maximum volume of the SeekBar to the maximum volume of the MediaPlayer:
        // mantenere la SeekBar sincronizzata con i valori effettivi del sistema.
        //Non bisogna dimezzare il valore massimo della barra seekBarMeditazione
        // Biosgna invece impostare un volume iniziale che sia la metà del massimo,
        // senza però cambiare il range massimo della barra.
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int mediumVolume = maxVolume / 2 ;
        seekBarMeditazione.setMax(maxVolume);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mediumVolume, 0);

        // Set the current volume of the SeekBar to the current volume of the MediaPlayer:
        int currVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        seekBarMeditazione.setProgress(currVolume);

        // Add a SeekBar.OnSeekBarChangeListener to the SeekBar:
        seekBarMeditazione.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, i, AudioManager.FLAG_SHOW_UI);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Do Nothing
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Do Nothing
            }
        });

        player_start = null;
        player_end = null;
        countdownTimer = null;

        return root;
    }

    // Set on manifest file
    // android:configChanges="keyboardHidden|orientation"
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        boolean isViewProfileOrNot = true;
        if (getActivity() != null) {
            if (isViewProfileOrNot) {
                if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
            }

        }

    }

    @Override
    public void onDestroyView() {

        if (player_start != null) {
            player_start.stop();
            player_start.release();
            player_start = null;
        }

        if (player_end != null) {
            player_end.stop();
            player_end.release();
            player_end = null;
        }

        super.onDestroyView();
        binding = null;
    }


    private void startOrPause() {

        Log.d("startOrPause", " timerRunning ");

        if (runMed.strStatus.equals(StatusMeditazione.RUN_FROM_PAUSE.name()) ||
                runMed.strStatus.equals(StatusMeditazione.RUN_FROM_STOP_INITAL_SONG_NOT_RUNNING.name())) {
            Log.d("startOrPause", " GO PAUSE timerRunning ");

            runMed.strStatus = StatusMeditazione.PAUSE.name();
            pauseTimer();
        } else {
            Log.d("startOrPause", " GO TIMER flagPauseOn ");
            Log.d("startOrPause", " GO TIMER flagInitialSongOn ");
            if (runMed.strStatus.equals(StatusMeditazione.PAUSE.name())) {
                runMed.strStatus = StatusMeditazione.RUN_FROM_PAUSE.name();
                startTimer();
            } else if (runMed.strStatus.equals(StatusMeditazione.STOP.name())) {
                runMed.strStatus = StatusMeditazione.RUN_FROM_STOP_INITAL_SONG_NOT_RUNNING.name();
                startTimer();
            } else
                Log.d("startPause", "Non faccio nulla se viene premuto lo start durante il canto");

        }

    }

    private void startTimer() {

        String strErrLoc = "Errore In Med";

        try {
            hideWhileMeditating();
            lockMeditazione.open_lock();
            setFullScreenCall();

            // Se non arrivo da una pausa e non e' gia' iniziato il canto
            // Solo se arrivo dallo stato di stop
            // STATO STOP
            // STATO PAUSA
            // STATO RUN_COUNTER ARRIVO DALLO STOP
            // STATO RUN_COUNTER ARRIVO DALLA PAURA
            if (runMed.strStatus.equals(StatusMeditazione.RUN_FROM_STOP_INITAL_SONG_NOT_RUNNING.name())) {
                player_start = MediaPlayer.create(getActivity(), R.raw.inizio_med);
                player_start.start();
                //flagInitialSongRunning = true;
                runMed.strStatus = StatusMeditazione.RUN_FROM_STOP_INITAL_SONG_RUNNING.name();

            }

            Log.d("startTimer", " Attenzione start Timer" + runMed.timeLeftInMs);

            // IN ERNTAMBI DEVE ANDARE AVANTI IL COUNTER
            countdownTimer = new CountDownTimer(runMed.timeLeftInMs, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                    setTimeLeftInMs(millisUntilFinished);
                    Log.d("[onTick]", " CountDownTimer -> " + runMed.timeLeftInMs);
                    int seconds = (int) runMed.timeLeftInMs % 60000 / 1000;
                    Log.d("[onTick]", " CountDownTimer -> " + seconds);

                    boolean isMultiple5 = (seconds % 5 == 0) ? true : false;

                    Log.d("[onTick]", " isMultiple5 -> " + isMultiple5);


                    if (isMultiple5) {
                        Log.d("[onTick]", " CountDownTimer Tengo attivo lo schermo ogni 30 sec ");

                        set_keep_screen_on_off(true);

                    }

                    updateTimerForCounter();

                }

                @Override
                public void onFinish() {
                    // some sound, record data, some message
                    //vibrate.vibrate(2000);

                    Log.d("[startTimer] ", "Player Start Canto Finale ");
                    try {
                        player_end = MediaPlayer.create(getActivity(), R.raw.fine_med_gurupuja);
                        player_end.start();

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("[onFinish]", "Exception ");
                    }
                    Log.d("[startTimer]", " Player End Canto Finale ");

                    // Esegue le stesse cose di stopTimer()
                    // a parte updateTime vedere se si puo
                    // chiamare stopTime
                    showAfterMeditating();
                    startBtn.setText("Meditazione");
                    initTime();

                    //flagTimerRunning = false;
                    //flagPauseRunning = false;
                    //flagInitialSongRunning = false;

                    lockMeditazione.close_lock();
                    setDefaultScreen();
                    runMed.strStatus = StatusMeditazione.STOP.name();

                }
            }.start();

            Log.d("[start] ", "timeLeftInMs " + runMed.timeLeftInMs);

            // Se non sono arrivato in fondo
            // se ho superarto il canto iniziale
            Log.d("[onFinish] ", "timeLeftInMs " + runMed.timeLeftInMs);

            if (runMed.strStatus.equals(StatusMeditazione.RUN_FROM_STOP_INITAL_SONG_NOT_RUNNING.name()) &&
                    runMed.timeLeftInMs > 0 &&
                    runMed.timeLeftInMs != runMed.timeInitialSongAndMedInMs) {
                startBtn.setText("Pausa");
                //flagTimerRunning = true;

            }

        } catch (Exception e) {
            e.printStackTrace();
            startBtn.setText(strErrLoc + "-ERR");
        }

    }


    public long parseSumSubEqTime(String input, char op) {

        Log.d("parseSumSubEqTime", " input iniziale-> " + input);

        StringTokenizer st = new StringTokenizer(input, ":");
        String strMinute = st.nextToken();

        Log.d("parseSumSubEqTime", " strMinute-> " + strMinute);
        // op = '+' , '=' , '-'
        if (op == '+')
            runMed.timeInMinutes = Integer.valueOf(strMinute) + 1;
        else if (op == '-')
            runMed.timeInMinutes = Integer.valueOf(strMinute) - 1;
        else
            runMed.timeInMinutes = Integer.valueOf(strMinute);

        Log.d("parseSumSubEqTime", " timeInMinutes-> " + runMed.timeInMinutes);

        long msOutput = runMed.timeInMinutes * 60000;

        Log.d("parseSumSubEqTime", " msOutput finale-> " + msOutput);

        return msOutput;
    }

    private void initTime() {

        String strTimeDefault = mPreferences.getString(SHARED_TIME_DEFAULT, null);
        System.out.println("[MeditazioneFragment.reset] strTimeDefault " + strTimeDefault);

        if (strTimeDefault == null || strTimeDefault.equals(""))
            // KO RUN TIME INSPIEGABILE
            //strTimeDefault = Resources.getSystem().getString( R.string.timeDefaultInitial) ;
            strTimeDefault = "30:00";
        timeLeft.setText(strTimeDefault);
        editTimeInput.setText(strTimeDefault);
        setTimeLeftInMs(parseSumSubEqTime(strTimeDefault, '='));
        // Aggiungo il tempo del canto iniiale
        setTimeLeftInMs(runMed.timeLeftInMs + runMed.timeInitialSongInMs);
        // Salvo il valore iniziale del
        // tempo totale = 50000 + il tempo di meditazione
        runMed.timeInitialSongAndMedInMs = runMed.timeLeftInMs;
    }

    private void setTimeLeftInMs(long milliseconds) {
        runMed.timeLeftInMs = milliseconds; // Tempo del counter effettivo
        runMed.timeLeftAuxInMs = runMed.timeLeftInMs - runMed.timeInitialSongInMs; // tempo senza il canto inziale
    }

    private void updateTimerForCounter() {

        Log.d("updateTimerForCounter", "Tempo canto+meditazione-> " + runMed.timeLeftInMs);
        Log.d("updateTimerForCounter", "Tempo meditazione-> " + runMed.timeLeftAuxInMs);
        Log.d("updateTimerForCounter", "Tempo solo canto-> " + runMed.timeInitialSongAndMedInMs);

        int minutes = (int) runMed.timeLeftInMs / 60000; // in ms
        int seconds = (int) runMed.timeLeftInMs % 60000 / 1000;

        long timeFromStart = runMed.timeInitialSongAndMedInMs - runMed.timeLeftInMs;

        Log.d("updateTimerForCounter", " Tempo tot canto+med-> " + runMed.timeInitialSongAndMedInMs);
        Log.d("updateTimerForCounter", " Tempo rimanennte canto+meditazione-> " + runMed.timeLeftInMs);
        Log.d("updateTimerForCounter", " Tempo rimanennte solo meditazione-> " + runMed.timeLeftAuxInMs);
        Log.d("updateTimerForCounter", " Tempo passato-> " + timeFromStart);
        Log.d("updateTimerForCounter", " Tempo canto iniziale-> " + runMed.timeInitialSongInMs);


        if (timeFromStart > runMed.timeInitialSongInMs) {
            Log.d("updateTimerForCounter", " ATTENZIONE Aggiorno il tempo a video alla fine del canto " + timeFromStart);

            String timeLeftText = "" + minutes;
            timeLeftText += ":";
            if (seconds < 10) {
                timeLeftText += "0";
            }
            timeLeftText += seconds;

            Log.d("", "updateTimer -> ON TIMER TICKER " + timeLeftText);

            timeLeft.setText(timeLeftText);
            editTimeInput.setText(timeLeftText);
            //flagInitialSongRunning = false;
            startBtn.setText("Pausa");
            //flagTimerRunning = true;
            runMed.strStatus = StatusMeditazione.RUN_FROM_STOP_INITAL_SONG_NOT_RUNNING.name();
        } else
            Log.d("updateTimerForCounter", " ATTENZIONE Non modifico il tempo a video durante il canto " + timeFromStart);

    }


    private void updateTimerForSumSub() {

        Log.d("updateTimerForSumSub", "Mio Tempo rimanennte canto+meditazione-> " + runMed.timeLeftInMs);
        Log.d("updateTimerForSumSub", "Mio Tempo rimanennte solo meditazione-> " + runMed.timeLeftAuxInMs);

        int minutes = (int) runMed.timeLeftAuxInMs / 60000; // in ms
        int seconds = (int) runMed.timeLeftAuxInMs % 60000 / 1000;

        String timeLeftText = "" + minutes;
        timeLeftText += ":";
        if (seconds < 10) {
            timeLeftText += "0";
        }
        timeLeftText += seconds;

        timeLeft.setText(timeLeftText);
        editTimeInput.setText(timeLeftText);

    }

    private void updateTimer(boolean onTimerTick) {

        System.out.println("updateTimer Tempo rimanennte canto+meditazione-> " + runMed.timeLeftInMs);
        System.out.println("updateTimer Tempo rimanennte meditazione-> " + runMed.timeLeftAuxInMs);

        int minutes = 0;
        int seconds = 0;
        if (onTimerTick) {
            minutes = (int) runMed.timeLeftInMs / 60000; // in ms
            seconds = (int) runMed.timeLeftInMs % 60000 / 1000;
        } else {
            minutes = (int) runMed.timeLeftAuxInMs / 60000; // in ms
            seconds = (int) runMed.timeLeftAuxInMs % 60000 / 1000;
        }


        String timeLeftText = "" + minutes;
        timeLeftText += ":";
        if (seconds < 10) {
            timeLeftText += "0";
        }
        timeLeftText += seconds;

        // Se il tempo trascorso e' maggiore del
        // tempo della canzone iniziale aggiorno
        // il tempo a video
        if (onTimerTick) {
            System.out.println("updateTimer -> ON TIMER TICKER " + timeLeftText);
            long timeFromStart = runMed.timeInitialSongAndMedInMs - runMed.timeLeftInMs;

            System.out.println("updateTimer -> " + timeLeftText);
            System.out.println("updateTimer Tempo tot canto+med-> " + runMed.timeInitialSongAndMedInMs);
            System.out.println("updateTimer Tempo rimanennte meditazione-> " + runMed.timeLeftInMs);
            System.out.println("updateTimer Tempo rimanennte canto+meditazione-> " + runMed.timeLeftAuxInMs);
            System.out.println("updateTimer Tempo passato-> " + timeFromStart);
            System.out.println("updateTimer Tempo canto iniziale-> " + runMed.timeInitialSongInMs);


            if (timeFromStart > runMed.timeInitialSongInMs) {
                System.out.println("updateTimer ATTENZIONE Aggiorno il tempo a video alla fine del canto " + timeFromStart);
                timeLeft.setText(timeLeftText);
                editTimeInput.setText(timeLeftText);
                startBtn.setText("Pausa");
            } else
                System.out.println("updateTimer ATTENZIONE Non modifico il tempo a video durante il canto " + timeFromStart);
            // Prima della fine del canto non faccio nulla
        } else {
            timeLeft.setText(timeLeftText);
            editTimeInput.setText(timeLeftText);
        }


    }

    // Pression Pulsante Stop
    private void stopTimer() {

        showAfterMeditating();
        startBtn.setText("Meditazione");

        if (countdownTimer != null) {
            countdownTimer.cancel();
            countdownTimer = null;
        }

        initTime();

        lockMeditazione.close_lock();
        setDefaultScreen();
        runMed.strStatus = StatusMeditazione.STOP.name();

    }

    // Pression Pulsante Pause
    private void pauseTimer() {

        showAfterMeditating();
        startBtn.setText("Meditazione");

        countdownTimer.cancel();
        lockMeditazione.close_lock();
        setDefaultScreen();
    }

    private void hideWhileMeditating() {

        // Se fossero richiesti i tasti +/- in pausa
        // ci pensiamo
        // x ora crea solo problemi
        if (!runMed.strStatus.equals(StatusMeditazione.PAUSE.name())) {
            sumMinutesBtn.setVisibility(View.INVISIBLE);
            subMinutesBtn.setVisibility(View.INVISIBLE);
        }

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

        // Se fossero richiesti i tasti +/- in pausa
        // ci pensiamo
        // x ora crea solo problemi
        if (!runMed.strStatus.equals(StatusMeditazione.PAUSE.name())) {
            sumMinutesBtn.setVisibility(View.VISIBLE);
            subMinutesBtn.setVisibility(View.VISIBLE);
        }
        // per ora lo lascio invisibile


    }

    void set_keep_screen_on_off(boolean flagKeepScreenOn) {


        getActivity().getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        if (flagKeepScreenOn) {

            getActivity().getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                            WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        }

    }

    public void setFullScreenCall() {
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = getActivity().getWindow().getDecorView();
            System.out.println("[setFullScreencall] start sdk 11-19 " + v.getSystemUiVisibility());

            v.setSystemUiVisibility(View.GONE);

        } else if (Build.VERSION.SDK_INT >= 19) {
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

    private void time_audio_sleep(int delaySec) {
        // Get time to sleep
        long timeToSleep = delaySec;

        // Create a TimeUnit object
        TimeUnit time = TimeUnit.SECONDS;

        try {

            System.out.println("Going to sleep for "
                    + timeToSleep
                    + " seconds");

            // using sleep() method
            time.sleep(timeToSleep);

            System.out.println("Slept for "
                    + timeToSleep
                    + " seconds");
        } catch (InterruptedException e) {
            System.out.println("Interrupted "
                    + "while Sleeping");
        }
    }

    private void initial_song_wait_thread(long delay, boolean flagThread) throws Exception {

        if (flagThread) {
            Thread.sleep(delay);
        } else {

            int loop = 0;
            int num_rest = 0;
            while (player_start.isPlaying()) {
                loop += 1;
                num_rest = loop % 10000;

                if (num_rest == 0) {
                    System.out.println("[startTimer] Wait Starting song " + loop + " " + num_rest);
                    set_keep_screen_on_off(true);
                }
            }


        }


    }

    private void final_song_wait(long delay, boolean flagThread) throws Exception {
        if (flagThread) {
            Thread.sleep(delay);
        } else {

            int loop = 0;
            int num_rest = 0;
            while (player_end.isPlaying()) {
                loop += 1;
                num_rest = loop % 10000;

                if (num_rest == 0) {
                    System.out.println("[onFinish] Wait Ending song " + loop + " " + num_rest);
                    set_keep_screen_on_off(true);
                }
            }

            System.out.println("[onFinish] Wait End ");

        }

    }

    /*
    https://stackoverflow.com/questions/5216658/pinch-zoom-for-custom-view
    https://android-developers.googleblog.com/2010/06/making-sense-of-multitouch.html
    https://developer.android.com/reference/android/view/GestureDetector
    */
    private class TouchImageView {

        private boolean isMoved = true;
        private Matrix matrix = new Matrix();
        private Matrix savedMatrix = new Matrix();
        private PointF startPoint = new PointF();
        private PointF midPoint = new PointF();
        private float oldDist = 1f;
        private int NONE = 0;
        private int DRAG = 1;
        private int ZOOM = 2;
        private int mode = NONE;
        private String TAG = "OnTouch";
        private GestureDetector gestureDetector;

        public TouchImageView(){

        }

        public boolean onTouchMyImage(View v, MotionEvent event) {

            float scale;
            ImageView imageView = (ImageView) v;
            imageView.setScaleType(ImageView.ScaleType.MATRIX);

            dumpEvent(event);
            // Handle touch events here...

            switch (event.getAction() & 255) {
                case MotionEvent.ACTION_DOWN:   // first finger down only
                    savedMatrix.set(matrix);
                    startPoint.set(event.getX(), event.getY());
                    Log.d(TAG, "mode=DRAG"); // write to LogCat
                    mode = DRAG;
                    break;

                case MotionEvent.ACTION_UP: // first finger lifted

                case 6: // second finger lifted

                    mode = NONE;
                    Log.d(TAG, "mode=NONE");
                    break;

                case 5: // first and second finger down

                    oldDist = spacing(event);
                    Log.d(TAG, "oldDist=" + oldDist);
                    if (oldDist > 5f) {
                        savedMatrix.set(matrix);
                        midPoint(midPoint, event);
                        mode = ZOOM;
                        Log.d(TAG, "mode=ZOOM");
                    }
                    break;

                case MotionEvent.ACTION_MOVE:

                    if (mode == DRAG) {
                        matrix.set(savedMatrix);
                        matrix.postTranslate(event.getX() - startPoint.x, event.getY() - startPoint.y); // create the transformation in the matrix  of points
                    } else if (mode == ZOOM) {
                        // pinch zooming
                        float newDist = spacing(event);
                        Log.d(TAG, "newDist=" + newDist);
                        if (newDist > 5f) {
                            matrix.set(savedMatrix);
                            scale = newDist / oldDist; // setting the scaling of the
                            // matrix...if scale > 1 means
                            // zoom in...if scale < 1 means
                            // zoom out
                            matrix.postScale(scale, scale, midPoint.x, midPoint.y);
                        }
                    }
                    break;
            }

            imageView.setImageMatrix(matrix); // display the transformation on screen

            return true; // indicate event was handled
        }

        private float spacing(MotionEvent event) {
            float x = event.getX(0) - event.getX(1);
            float y = event.getY(0) - event.getY(1);
            return (float) Math.sqrt(x * x + y * y);
        }

        private void midPoint(PointF point, MotionEvent event) {
            float x = event.getX(0) + event.getX(1);
            float y = event.getY(0) + event.getY(1);
            point.set(x / 2, y / 2);
        }

        private void dumpEvent(MotionEvent event) {
            String names[] = {"DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE", "POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?"};
            StringBuilder sb = new StringBuilder();
            int action = event.getAction();
            int actionCode = action & MotionEvent.ACTION_MASK;
            sb.append("event ACTION_").append(names[actionCode]);

            if (actionCode == MotionEvent.ACTION_POINTER_DOWN || actionCode == MotionEvent.ACTION_POINTER_UP) {
                sb.append("(pid ").append(action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
                sb.append(")");
            }

            sb.append("[");
            for (int i = 0; i < event.getPointerCount(); i++) {
                sb.append("#").append(i);
                sb.append("(pid ").append(event.getPointerId(i));
                sb.append(")=").append((int) event.getX(i));
                sb.append(",").append((int) event.getY(i));
                if (i + 1 < event.getPointerCount())
                    sb.append(";");
            }

            sb.append("]");
            Log.d("Touch Events ---------", sb.toString());
        }
    };

}