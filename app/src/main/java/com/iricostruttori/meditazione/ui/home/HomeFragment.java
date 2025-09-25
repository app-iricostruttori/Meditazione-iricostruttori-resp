package com.iricostruttori.meditazione.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;

import com.iricostruttori.meditazione.R;
import com.iricostruttori.meditazione.databinding.FragmentHomeBinding;

/*
Appunti: sito internet x aumentare il volume audio
https://mp3cut.net/change-volume
*/

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private Button audio_home_btn;
    private Button stop_audio_home_btn;
    private boolean audioRunning = false;
    private boolean audioStop = false;
    private MediaPlayer player_start;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        System.out.println("[onCreateView] start ");
        getActivity().setTheme(R.style.Theme_Meditazioneiricostruttoriresp);
        //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        audio_home_btn = (Button)binding.audioHomeBtn;
        stop_audio_home_btn = (Button)binding.stopAudioHomeBtn;

        // Initialize the SeekBar in your Activity or Fragment:
        //SeekBar seekBarHome = (SeekBar) findViewById(R.id.seekBar);
        SeekBar seekBarHome = binding.seekBarHome;
        // Get the audio manager to manage volume audio
        AudioManager audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        player_start = MediaPlayer.create(getActivity(), R.raw.audio_home_vol_200);

        // https://developer.android.com/reference/android/media/MediaPlayer#state-diagram
        audio_home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //vibrate.vibrate(50);
                try {
                    System.out.println("[onClick] start audioRunning " + audioRunning + " audioStop " + audioStop);
                    if (!audioRunning) {
                        audio_home_btn.setText("Pausa");

                        if (audioStop) {
                            System.out.println("RESET SEEK ");
                            //player_start.seekTo(0);
                            player_start = MediaPlayer.create(getActivity(), R.raw.audio_home_vol_200);
                            audioStop=false;
                        }
                        player_start.start();
                        audioRunning=true;
                    }
                    else {
                        audio_home_btn.setText("Padre Jhon");
                        audioRunning=false;
                        player_start.pause();
                    }
                }
                catch (IllegalStateException  e) {
                    e.printStackTrace();
                }

            }
        });

        stop_audio_home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //vibrate.vibrate(50);

                try {
                        if (player_start != null) {
                            player_start.stop();
                            player_start.release();
                            player_start=null;
                        }

                        audioRunning = false;
                        audioStop=true;
                }
                catch(IllegalStateException  e) {
                    e.printStackTrace();
                }


                audio_home_btn.setText((String)"Padre John");



            }
        });

        // Set the maximum volume of the SeekBar to the maximum volume of the MediaPlayer:
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        seekBarHome.setMax(maxVolume);
        //seekBarHome.setMax(player_start.getDuration());
        int mediumVolume = maxVolume / 2 ;
        // Set the system's volume to the mediumVolume
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mediumVolume, 0);

        // Set the current volume of the SeekBar to the current volume of the MediaPlayer:
        int currVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        seekBarHome.setProgress(currVolume);

        // Add a SeekBar.OnSeekBarChangeListener to the SeekBar:
        seekBarHome.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress_i, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress_i, AudioManager.FLAG_SHOW_UI);
                // In Kotline
                // https://www.daniweb.com/programming/mobile-development/tutorials/537223/android-native-sync-mediaplayer-progress-to-seekbar
                //if (fromUser) {
                //    player_start.seekTo(progress_i);
                //}
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
            player_start=null;
        }

        super.onDestroyView();
        binding = null;
    }

    /* private HomeViewModel mViewModel;

    public static HomeFragment newInstance() {
    return new HomeFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        // TODO: Use the ViewModel
    }
    */
}