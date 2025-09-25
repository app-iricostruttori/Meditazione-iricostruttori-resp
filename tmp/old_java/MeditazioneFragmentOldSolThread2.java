package com.iricostruttori.meditazione.ui.meditazione;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

//import com.iricostruttori.meditazione.databinding.FragmentReflowBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.iricostruttori.meditazione.R;
import com.iricostruttori.meditazione.databinding.FragmentMeditazioneBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

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
    boolean isReleasedStartPlayer = false;
    boolean isReleasedEndPlayer = false;

    // Parametrizzare il default nel Fragment settings
    private long timeLeftInMs = 1 * 60000 ; //default value
    private long initTimeLeftInMs = 50000;
    //private long timeLeftInMsOriginal = 1 ;
    private int timeInMinutes = 30 ;
    private boolean timerRunning = false;
    private boolean flagPauseOn = false;

    private SharedPreferences mPreferences;
    //private SharedPreferences.Editor mEditor;
    private final String SHARED_SETTING_VALUE = "SHARED_SETTING_VALUE" ;
    private final String SHARED_TIME_DEFAULT = "SHARED_TIME_DEFAULT" ;

    int uiOptionsDefault = 0 ;
    LockMeditazione lockMeditazione;
    protected WaitProcess waitProcess = new WaitProcess(50000);
    protected SharedWait sharedWait = new SharedWait();
    //Thread threadWaitSong = new Thread();
    //SyncronizeObj sync = new SyncronizeObj();

    //SleepTaskMeditazione myTimerTask ;
    //TimerTask myTimerTask;
    //Timer timer;

    /*
    public void startMyTimerTask() {


        System.out.println("myTimerTask begin ");
        myTimerTask = new SleepTaskMeditazione();
        myTimerTask.start_task();
        System.out.println("myTimerTask terminate");
    }
    */

    /*
    Timer timerWait;
    TimerTask timerWaitTask;
    //we are going to use a handler to be able to run in our TimerTask
    final Handler handlerWait = new Handler();

    public void startTimerWaitTask() {
        //set a new Timer
        timerWait = new Timer();

        //initialize the TimerTask's job
        initializeTimerWaitTask();

        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
        //timerWait.schedule(timerWaitTask, 5000, 10000);
        timerWait.schedule(timerWaitTask,50000);
        //
    }

    public void stopTimerWaitTask() {
        //stop the timer, if it's not already null
        if (timerWait != null) {
            timerWait.cancel();
            timerWait = null;
        }
    }

    public void initializeTimerWaitTask() {

        timerWaitTask = new TimerTask() {
            public void run() {

                //use a handler to run a toast that shows the current timestamp
                handlerWait.post(new Runnable() {
                    public void run() {
                        System.out.println("START TIMER WAIT SCHEDULER ");
                        //get the current timeStamp
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd:MMMM:yyyy HH:mm:ss a");
                        final String strDate = simpleDateFormat.format(calendar.getTime());

                        //show the toast
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(getActivity().getApplicationContext(), strDate, duration);
                        toast.show();
                    }
                });
            }
        };
    }
    */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        set_keep_screen_on_off(true);
        lockMeditazione = new LockMeditazione((PowerManager) getActivity().getBaseContext().getSystemService(Context.POWER_SERVICE));

        getActivity().setTheme(R.style.Theme_Meditazioneiricostruttoriresp);

        uiOptionsDefault = getDefaultScreen();

        // remove notifications bar
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();

        binding = FragmentMeditazioneBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final ImageView imageView = binding.imageViewMeditazione;

        timeLeft = binding.timeLeft;
        startBtn = binding.startBtn;
        stopBtn = binding.stopBtn;
        subMinutesBtn = binding.subMinutesBtn;
        sumMinutesBtn = binding.sumMinutesBtn;
        editTimeInput = binding.editTimeInput;

        editTimeInput.setVisibility(View.INVISIBLE);
        mPreferences = getContext().getSharedPreferences(SHARED_SETTING_VALUE, Context.MODE_PRIVATE);

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
                sumMinutesBtn.setVisibility(View.INVISIBLE);
                subMinutesBtn.setVisibility(View.INVISIBLE);
                editTimeInput.setVisibility(View.INVISIBLE);
                startOrPause();

            }
        });

        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Provare sia con il canto sia senza
                // https://stackoverflow.com/questions/4264355/interrupting-or-stopping-a-sleeping-thread#:~:text=Call%20the%20interrupt()%20method,an%20InterruptedException%20will%20be%20thrown.
                //threadWaitSong.notify();


                //sync.doNotify();
                //stopTimerWaitTask();

                Log.d("stopBtn.click "," start button stop ");
                // https://stackoverflow.com/questions/25267571/illegalmonitorstateexception-object-not-locked-by-thread-before-notify
                //waitProcess.stopWait();

                //waitProcess.doNotify();
                // Produce thread to stop
                if (player_start.isPlaying())
                    player_start.stop();
                new Thread(() -> { sharedWait.produce(1);
                }).start();

                if (player_start != null) {
                    player_start.stop();
                    player_start.release();
                    player_start=null;
                }

                if (player_end != null) {
                    player_end.stop();
                    player_end.release();
                    player_end=null;
                }

                stopTimer();

            }
        });


        player_start = null;
        player_end = null;
        countdownTimer = null;

        return root;
    }

    @Override
    public void onDestroyView() {

        //player_start.release();
        //player_end.release();

        super.onDestroyView();
        //threadWaitSong = null;
        //sync = null;
        binding = null;
    }


    private void startOrPause() {

        if (timerRunning) {
            pauseTimer();
        } else {
            startTimer();
        }

    }

    private void startTimer() {

        String strErrLoc = "Errore In Med" ;

        try {
            hideWhileMeditating();
            lockMeditazione.open_lock();
            setFullScreenCall();

            if (!flagPauseOn) {

                player_start = MediaPlayer.create(getActivity(), R.raw.inizio_med);
                player_start.start();
                // Se lo metto qui parte tutto dopo a parte il player
                //initial_song_wait(50000,true);
                //initial_song_wait(50000,false);
                //initial_song_wait(50);
                // Aspetto 50 secondo con un countdown
                //initial_countdown_timer(50000);
                //time_audio_sleep(50);
                //threadWaitSong.wait(50000L);

                //sync.doWait(50000L);
                //startTimerWaitTask();
                //startMyTimerTask();
                //waitProcess.goWait();
                //waitProcess.doWait();
                // Consumer thread
                new Thread(() -> { sharedWait.consume();
                }).start();

                while(player_start.isPlaying())
                        ;
                // Produce thread 1
                new Thread(() -> { sharedWait.produce(1);
                }).start();

            }

            Log.d("startTimer", " isWaiting " + String.valueOf(waitProcess.isWaiting));

            //while(waitProcess.isWaiting)
            //        ;

            // Da qui anche in caso di pausa
            countdownTimer = new CountDownTimer(timeLeftInMs, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                    startBtn.setText("Silenzio");
                    timeLeftInMs = millisUntilFinished;
                    Log.d("[onTick]"," CountDownTimer -> " + timeLeftInMs);
                    int seconds = (int) timeLeftInMs % 60000 / 1000;
                    Log.d("[onTick]"," CountDownTimer -> " + seconds);

                    boolean isMultiple5 = ( seconds%5 == 0 ) ? true : false;

                    Log.d("[onTick]"," isMultiple5 -> " + isMultiple5);


                    if (isMultiple5) {
                        Log.d("[onTick]"," CountDownTimer Tengo attivo lo schermo ogni 30 sec " );

                        set_keep_screen_on_off(true);

                    }

                    updateTimer();

                }

                @Override
                public void onFinish() {
                    // some sound, record data, some message
                    //vibrate.vibrate(2000);

                    Log.d("[startTimer] ", "Player Start Canto Finale ");
                    try {
                        player_end = MediaPlayer.create(getActivity(), R.raw.fine_med_gurupuja);
                        player_end.start();

                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        Log.e("[onFinish]","Exception ");
                    }
                    Log.d("[startTimer]"," Player End Canto Finale ");

                    // Esegue le stesse cose di stopTimer()
                    // a parte updateTime
                    showAfterMeditating();
                    startBtn.setText("Meditazione");
                    initTime();
                    timerRunning = false;
                    flagPauseOn = false;

                    lockMeditazione.close_lock();
                    setDefaultScreen();

                    if (player_end != null) {
                        player_end.stop();
                        player_end.release();
                        player_end=null;
                    }

                }
            }.start();

            Log.d("[onFinish] " , "timeLeftInMs " + timeLeftInMs);

            if (timeLeftInMs > 0) {
                startBtn.setText("Pausa");
                timerRunning = true;
                flagPauseOn = true;
                lockMeditazione.close_lock();
                setDefaultScreen();
            }


        }
        catch (Exception e) {
            e.printStackTrace();
            startBtn.setText(strErrLoc + "-ERR");
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
            // KO RUN TIME INSPIEGABILE
            //strTimeDefault = Resources.getSystem().getString( R.string.timeDefaultInitial) ;
            strTimeDefault = "30:00" ;
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

    // Pression Pulsante Stop
    private void stopTimer() {

        showAfterMeditating();
        startBtn.setText("Meditazione");
        timerRunning = false;
        flagPauseOn = false;
        if ( countdownTimer != null ) {
            countdownTimer.cancel();
            countdownTimer = null;
        }

        initTime();

        lockMeditazione.close_lock();
        setDefaultScreen();

    }

    // Pression Pulsante Pause
    private void pauseTimer() {

        showAfterMeditating();
        startBtn.setText("Meditazione");
        timerRunning = false;
        countdownTimer.cancel();

        lockMeditazione.close_lock();
        setDefaultScreen();
    }

    private void hideWhileMeditating() {

        //settingsBtn.setVisibility(View.INVISIBLE);
        sumMinutesBtn.setVisibility(View.INVISIBLE);
        subMinutesBtn.setVisibility(View.INVISIBLE);


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

        sumMinutesBtn.setVisibility(View.VISIBLE);
        subMinutesBtn.setVisibility(View.VISIBLE);
        // per ora lo lascio invisibile


    }

    void set_keep_screen_on_off(boolean flagKeepScreenOn) {


        getActivity().getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON );

        if (flagKeepScreenOn) {

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

    private void initial_countdown_timer(long initTime) {

        CountDownTimer countdownTimer = new CountDownTimer(initTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                initTimeLeftInMs = millisUntilFinished;
                Log.d("[onTick]"," CountDownTimer -> " + initTimeLeftInMs);

            }

            @Override
            public void onFinish() {
                // some sound, record data, some message
                //vibrate.vibrate(2000);
                Log.d("[initial_countdown_timer] ", "Finish");

            }
        }.start();

        Log.d("[initial_countdown_timer] ", " Before While " + initTimeLeftInMs);
        while ( initTimeLeftInMs != 0 )
               ;
        Log.d("[initial_countdown_timer] ", " After While " + initTimeLeftInMs);
    }

    private void time_audio_sleep (int delaySec) {
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
        }

        catch (InterruptedException e) {
            System.out.println("Interrupted "
                    + "while Sleeping");
        }
    }

    private void initial_song_wait_thread(long delay , boolean flagThread ) throws Exception
    {

        if (flagThread) {
            Thread.sleep(delay);
        }
        else {

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

    private void final_song_wait(long delay , boolean flagThread ) throws Exception
    {
        if (flagThread) {
            Thread.sleep(delay);
        }
        else {

            int loop=0 ;
            int num_rest=0 ;
            while (player_end.isPlaying()) {
                loop+=1 ;
                num_rest = loop%10000;

                if (num_rest == 0 ) {
                    System.out.println("[onFinish] Wait Ending song " + loop + " " + num_rest);
                    set_keep_screen_on_off(true);
                }
            }

            System.out.println("[onFinish] Wait End ");

        }

    }

    // Da RmiAppletWm di webmaster bennet
    /*
    protected class WaitProcess implements Runnable {

        private boolean isWaiting = false ;

        final private long numTimeWait ;

        private Thread threadMed ;

        protected WaitProcess(long numTimeWait) {
            this.numTimeWait = numTimeWait;
        }

        public void goWait()
        {
            this.start();
            //wait.setVisible(true);
        }

        public void start(){
            threadMed=new Thread(this);
            threadMed.start();
        }

        public void stopWait(){
            synchronized(this) {
                Log.d("WaitProcess.stopWait", " START WAIT " + player_start.isPlaying());
                if (player_start.isPlaying())
                    player_start.stop();
                threadMed.notify();

                Log.d("WaitProcess.stopWait", " END WAIT " + player_start.isPlaying());
                isWaiting = false;
            }
        }

        public void stop(){
            //wait.setVisible(false);
            Thread t2 = threadMed;
            isWaiting = false;
            threadMed=null;
            t2.interrupt();
        }

        public void run()
        {
            //runningProcess();
            synchronized(this){
                try {
                    Log.d("WaitProcess.run", " START PLAYER " );
                    player_start = MediaPlayer.create(getActivity(), R.raw.inizio_med);
                    player_start.start();
                    isWaiting = true;
                    Log.d("WaitProcess.run", " START WAIT " );
                    threadMed.wait(numTimeWait);
                } catch(InterruptedException e) {
                }
            }
            Log.d("WaitProcess.run", " END WAIT " );
            this.stop();
        }
    }
    */
    // https://stackoverflow.com/questions/886722/how-to-use-wait-and-notify-in-java-without-illegalmonitorstateexception/886799#886799
    public class WaitProcess extends Thread {

        private boolean isWaiting = false ;
        final long numTimeWait ;

        public WaitProcess(long numTimeWait) {
            this.numTimeWait = numTimeWait;
        }

        public void doWait(){
            synchronized(this){
                try {
                    isWaiting = true;
                    Log.d("WaitProcess.run", " BEFORE PLAYER " );
                    player_start = MediaPlayer.create(getActivity(), R.raw.inizio_med);
                    player_start.start();
                    Log.d("WaitProcess.run", " AFTER PLAYER " );
                    //while (player_start.isPlaying() ) ;
                          Log.d("WaitProcess.run", " WAIT RUNNING" + numTimeWait);
                    this.wait(numTimeWait);
                } catch(InterruptedException e) {
                    e.printStackTrace();
                    Log.d("[doWait]","Errore in doWait");
                }
                catch(Exception e) {
                    e.printStackTrace();
                    Log.d("[doWait]","Errore in doWait");
                }
            }
            if (player_start.isPlaying())
                player_start.stop();
            //while(player_start.isPlaying())

            isWaiting = false;
        }

        public void doNotify() {
            synchronized(this) {
                Log.d("WaitProcess.doNotify", " END WAIT " );
                player_start.stop();
                isWaiting = false;
                this.notify();

            }
        }


    }

    // https://codingnomads.com/java-threads-wait-notify
    public class SharedWait {
        private int itemWait;
        private boolean hasItemWait = false;

        public synchronized void produce(int newItemWait) {
            while (hasItemWait) {
                try {
                    // Producer waits if the item is already present
                    System.out.println("Produced: Loop Producer waits if the item is already present " + itemWait);
                    // Rimane in attesa di un eventuale stop
                    wait(50000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            itemWait = newItemWait;
            hasItemWait = true;
            System.out.println("Produced: " + itemWait);
            System.out.println("Produced: Notify the consumer that a new item is available " + itemWait);
            // Notify the consumer that a new item is available
            notify();
        }

        public synchronized void consume() {
            while (!hasItemWait) {
                try {
                    // Consumer waits if there is no item
                    System.out.println("Consumed: Loop Consumer waits if there is no item " + itemWait);
                    wait(50000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            System.out.println("Consumed: " + itemWait);
            System.out.println("Consumed: Notify the producer that the item has been consumed " + itemWait);
            hasItemWait = false;
            // Notify the producer that the item has been consumed
            notify();
        }
    }
    // https://ranmal-b-dewage.medium.com/concurrency-and-synchronization-of-java-threads-8807c59a8217
}