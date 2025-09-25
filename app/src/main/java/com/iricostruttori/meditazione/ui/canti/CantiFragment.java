package com.iricostruttori.meditazione.ui.canti;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.iricostruttori.meditazione.R;
import com.iricostruttori.meditazione.databinding.FragmentCantiBinding;

// Attenzione a non dimenticare public
// altrimenti non trova il costruttore della classe
public class CantiFragment extends Fragment  {

    private FragmentCantiBinding binding;

    private TableRow trText;
    private TextView textViewSans;
    private TextView textViewIta;

    private Spinner dropdown_canti;

    private Button listenAudioBtn ;
    private Button stopAudioBtn ;
    //private Button zoomInBtn;
    //private Button zoomOutBtn;
    private MediaPlayer player_start;
    private MediaPlayer player_end;
    private String strCurrentSong = "" ;

    // ZOOM IN OUT
    // The 3 states (events) which the user is trying to perform
    //Animation animZoomIn;
    //Animation animZoomOut;
    // ZOOM IN OUT
    //Magnifier magnifier_sans = null ;
    //Magnifier magnifier_ita = null ;

    private TouchTextView touchTextView = new TouchTextView();
    int startOrientation ;
    public CantiFragment() {

    }

    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        System.out.println("[onCreateView ] START " );

        getActivity().setTheme(R.style.Theme_Meditazioneiricostruttoriresp);
        //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //
        // https://medium.com/hootsuite-engineering/handling-orientation-changes-on-android-41a6b62cb43f
        // setRetainInstance(true);

        // remove notifications bar
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        binding = FragmentCantiBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



        trText = binding.rowText;
        textViewSans = binding.textViewSans;
        textViewIta = binding.textViewIta;

        listenAudioBtn = (Button)binding.listenAudioBtn;
        stopAudioBtn = (Button)binding.stopAudioBtn;

        /*
        zoomInBtn = binding.zoomBtnIn;
        zoomOutBtn = binding.zoomBtnOut;
        */

        // Initialize the SeekBar in your Activity or Fragment:
        SeekBar seekBarCanti = binding.SeekBarCanti;
        // Get the audio manager to manage volume audio
        AudioManager audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        // In caso di aggiunta dello spinner devo togliere
        // i seguenti e inserire il codice dello spinner
        String[] strSelectSpinnerInizio = getResources().getStringArray(R.array.canto_iniziale);
        String[] strSelectSpinnerFine = getResources().getStringArray(R.array.canto_finale);

        String[] items = new String[]{"Seleziona il canto",
                strSelectSpinnerInizio[0],
                strSelectSpinnerFine[0]};

        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        //dropdown = getActivity().findViewById(R.id.spinner_incontri);
        dropdown_canti = root.findViewById(R.id.spinner_canti);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, items);
        //set the spinners adapter to the previously created one.
        dropdown_canti.setAdapter(adapter);

        dropdown_canti.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strCurrentSong = (String) parent.getItemAtPosition(position);

                String strFullCantoSans = getTextFullCantoSans(strCurrentSong,
                        "Seleziona il canto");

                String strFullCantoIta = getTextFullCantoIta(strCurrentSong,
                        "Seleziona il canto");

                textViewSans.setText(strFullCantoSans);
                textViewIta.setText(strFullCantoIta);

                if (strCurrentSong.equals("Seleziona il canto")) {
                    textViewSans.setVisibility(View.INVISIBLE);
                    textViewIta.setVisibility(View.INVISIBLE);
                    trText.setVisibility(View.INVISIBLE);
                } else {
                    textViewSans.setVisibility(View.VISIBLE);
                    textViewIta.setVisibility(View.VISIBLE);
                    trText.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        stopAudioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] strSelectSpinnerInizio = getResources().getStringArray(R.array.canto_iniziale);
                String[] strSelectSpinnerFine = getResources().getStringArray(R.array.canto_finale);

                if (strCurrentSong.equals(strSelectSpinnerInizio[0])) {

                    try {
                        if (player_start != null) {
                            player_start.stop();
                            player_start.release();
                            player_start = null;
                        }

                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    }

                } else if (strCurrentSong.equals(strSelectSpinnerFine[0])) {

                    try {
                        if (player_end != null) {
                            player_end.stop();
                            player_end.release();
                            player_end = null;
                        }

                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    }

                }

            }
        });

        listenAudioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] strSelectSpinnerInizio = getResources().getStringArray(R.array.canto_iniziale);
                String[] strSelectSpinnerFine = getResources().getStringArray(R.array.canto_finale);

                if (strCurrentSong.equals(strSelectSpinnerInizio[0])) {

                    if (player_start == null) {
                        player_start = MediaPlayer.create(getActivity(), R.raw.inizio_med);
                        player_start.start();

                        //while (player_start.isPlaying()) {
                        //    System.out.println("[onClick] Wait Starting Song ");
                        //}
                    }

                } else if (strCurrentSong.equals(strSelectSpinnerFine[0])) {

                    if (player_end == null) {
                        player_end = MediaPlayer.create(getActivity(), R.raw.fine_med);
                        player_end.start();
                    }

                }

            }
        });



        // Set the maximum volume of the SeekBar to the maximum volume of the MediaPlayer:
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int mediumVolume = maxVolume / 2 ;
        seekBarCanti.setMax(maxVolume);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mediumVolume, 0);

        // Set the current volume of the SeekBar to the current volume of the MediaPlayer:
        int currVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        seekBarCanti.setProgress(currVolume);

        // Add a SeekBar.OnSeekBarChangeListener to the SeekBar:
        seekBarCanti.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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

        textViewSans.setVisibility(View.INVISIBLE);
        textViewIta.setVisibility(View.INVISIBLE);
        trText.setVisibility(View.INVISIBLE);
        textViewSans.setTextSize(touchTextView.ratio + 10);
        textViewIta.setTextSize(touchTextView.ratio + 10);
        // onTouchMyText
        textViewSans.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                // Respond to touch events.
                touchTextView.onTouchMyText(v,event);
                return true;
            }
        });

        textViewIta.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                touchTextView.onTouchMyText(v,event);
                return true;
            }
        });
        // onTouchMyText



        // https://stackoverflow.com/questions/28735084/how-to-scale-move-zoom-in-zoom-out-textview
        //https://developer.android.com/develop/ui/views/animations/zoom
        //Animation zoomAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.zoom_animation);

        /* ROTATE
        Animation zoomAnim = AnimationUtils.loadAnimation(getActivity(), androidx.navigation.ui.R.anim.nav_default_enter_anim);
        textViewSans.startAnimation(zoomAnim);
        textViewIta.startAnimation(zoomAnim);
        */

        /* ZOOM IN OUT WITH BUTTON */
        /*
        animZoomIn = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),
                R.anim.zoom_in);
        // Zoom In
        zoomInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewSans.setVisibility(View.VISIBLE);
                textViewSans.startAnimation(animZoomIn);

                textViewIta.setVisibility(View.VISIBLE);
                textViewIta.startAnimation(animZoomIn);
            }
        });

        animZoomOut = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),
                R.anim.zoom_out);
        // Zoom Out

        zoomOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewSans.setVisibility(View.VISIBLE);
                textViewSans.startAnimation(animZoomOut);

                textViewIta.setVisibility(View.VISIBLE);
                textViewIta.startAnimation(animZoomOut);
            }
        });
        */

        // DA MIN SDK 29 IN POI
        // https://medium.com/@rmkrishnasacoe/magnifier-in-android-36a976430731
        // https://developer.android.com/develop/ui/views/text-and-emoji/magnifier#java
        //final Magnifier magnifier_sans = null;
        /*
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q)
        {
            magnifier_sans = new Magnifier.Builder(textViewSans).build();

            magnifier_sans.show(textViewSans.getWidth() / 2, textViewSans.getHeight() / 2);

            textViewSans.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getActionMasked()) {
                        case MotionEvent.ACTION_DOWN:
                            // Fall through.
                        case MotionEvent.ACTION_MOVE: {
                            final int[] viewPosition = new int[2];
                            v.getLocationOnScreen(viewPosition);
                            magnifier_sans.show(event.getRawX() - viewPosition[0],
                                    event.getRawY() - viewPosition[1]);
                            break;
                        }
                        case MotionEvent.ACTION_CANCEL:
                            // Fall through.
                        case MotionEvent.ACTION_UP: {
                            magnifier_sans.dismiss();
                        }
                    }
                    return true;
                }
            });


        }
        */


        startOrientation = getResources().getConfiguration().orientation;
        System.out.println("[onCreateView ] startOrientation " + startOrientation);

        /*
        if(orientation == Configuration.ORIENTATION_LANDSCAPE) {

            System.out.println("[orientation ] ORIENTATION_LANDSCAPE");

        } else {

            System.out.println("[orientation ] ORIENTATION_PORTRAIT");
        }
        */
        //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE);
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

    // get distance between the touch event
    /* private int getDistance(MotionEvent event) {
        int dx = (int) (event.getX(0) - event.getX(1));
        int dy = (int) (event.getY(0) - event.getY(1));
        return (int) Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getPointerCount() == 2) {
            int action = event.getAction();
            int mainaction = action & MotionEvent.ACTION_MASK;
            if (mainaction == MotionEvent.ACTION_POINTER_DOWN) {
                bastDst = getDistance(event);
                baseratio = ratio;
            } else {
                // if ACTION_POINTER_UP then after finding the distance
                // we will increase the text size by 15
                float scale = (getDistance(event) - bastDst) / move;
                float factor = (float) Math.pow(2, scale);
                ratio = Math.min(1024.0f, Math.max(0.1f, baseratio * factor));
                text.setTextSize(ratio + 15);
            }
        }
        return true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
    */
    //}

    @Override
    public void onDestroyView () {
        /*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            magnifier_sans.dismiss();
        }
        */
        //magnifier_ita.dismiss();
        //getActivity().setRequestedOrientation(startOrientation);
        super.onDestroyView();
        binding = null;
    }

    String getTextFullCantoIta (String strSelectOnSpinner ,
                                 String strDefault) {

        System.out.println("[getTextFullCantoIta] START " + strSelectOnSpinner);
        String strFullCanto = "";

        if ( !strSelectOnSpinner.equals(strDefault)) {

            String stTextCanto[] = getTextCantoItaliano(strSelectOnSpinner);

            for (int i=0 ; i<stTextCanto.length ; i++ )
                strFullCanto = strFullCanto + " " + stTextCanto[i] + "\n";
        }
        System.out.println(":" + strFullCanto + ":" );
        return strFullCanto;


    }

    String getTextFullCantoSans (String strSelectOnSpinner ,
                                 String strDefault) {

        System.out.println("[getTextFullCantoSans] START " + strSelectOnSpinner);
        System.out.println("[getTextFullCantoSans] START " + strDefault);
        String strFullCanto = "";

        if ( !strSelectOnSpinner.equals(strDefault)) {

            String stTextCanto[] = getTextCantoSanscrito(strSelectOnSpinner);

            for (int i=0 ; i<stTextCanto.length ; i++ )
                strFullCanto = strFullCanto + stTextCanto[i] + "\n";
        }
        System.out.println(":" + strFullCanto + ":" );
        return strFullCanto;


    }

    String[] getTextCantoItaliano(String strCantoSelezionato ) {

        String[] strCantoIniziale = getResources().getStringArray(R.array.canto_iniziale);
        String[] strCantoFinale = getResources().getStringArray(R.array.canto_finale);

        String[] strText = null;
        if ( strCantoSelezionato.equals(strCantoIniziale[0]))
        {
            System.out.println("set Canto iniziale ita");
            strText = getResources().getStringArray(R.array.canto_iniziale_italiano);
        }
        else if ( strCantoSelezionato.equals(strCantoFinale[0]))
        {
            System.out.println("set Canto finale ita");
            strText = getResources().getStringArray(R.array.canto_finale_italiano) ;
        }

        return strText;

    }

    String[] getTextCantoSanscrito(String strCantoSelezionato ) {

        String[] strCantoIniziale = getResources().getStringArray(R.array.canto_iniziale);
        String[] strCantoFinale = getResources().getStringArray(R.array.canto_finale);

        System.out.print("[getTextCantoSanscrito] START " + strCantoSelezionato);
        String[] strText = null;
        if ( strCantoSelezionato.equals(strCantoIniziale[0]))
        {
            //System.out.println("set Canto iniziale sans ");
            strText = getResources().getStringArray(R.array.canto_iniziale_sancrito);
            //System.out.println("set Canto iniziale sans strText " + strText[0]);
        }
        else if ( strCantoSelezionato.equals(strCantoFinale[0]))
        {
            //System.out.println("set Canto finale sans ");
            strText = getResources().getStringArray(R.array.canto_finale_sancrito) ;
            //System.out.println("set Canto inale sans strText " + strText[0]);
        }

        return strText;

    }

    public static String padRight(String stringa,
                                  int lunghezza,
                                  String carattere)
    {

        while (stringa.length() < lunghezza)
        {
            stringa = stringa + carattere;
        }
        return stringa;

    }

    // https://stackoverflow.com/questions/42361055/handle-onclick-and-ontouch-both-android
    /*
    https://stackoverflow.com/questions/27730154/android-zoom-out-in-of-a-gridlayout
    https://developer.android.com/develop/ui/views/animations/zoom
    https://gist.github.com/Antarix/65cfc180c05524ce616f
    https://www.tutlane.com/tutorial/android/android-zoom-in-out-animations-with-examples
    https://www.geeksforgeeks.org/zoom-in-and-out-to-textview-in-android/
    https://stackoverflow.com/questions/5216658/pinch-zoom-for-custom-view
    https://android-developers.googleblog.com/2010/06/making-sense-of-multitouch.html
    https://developer.android.com/reference/android/view/GestureDetector
    */

    private class TouchTextView {

        final static float move = 200;
        float ratio = 1.0f;
        int bastDst;
        float baseratio;

        public TouchTextView() {
            ratio = 1.0f;
        }

        public TouchTextView(float ratio_) {
            ratio = ratio_ ;
        }
        //  public boolean onTouchMyText(MotionEvent event ,boolean flagSansText ) {
        public boolean onTouchMyText(View v , MotionEvent event ) {
            if (event.getPointerCount() == 2) {
                int action = event.getAction();
                int mainaction = action & MotionEvent.ACTION_MASK;
                if (mainaction == MotionEvent.ACTION_POINTER_DOWN) {
                    bastDst = getDistance(event);
                    baseratio = ratio;
                } else {
                    // if ACTION_POINTER_UP then after finding the distance
                    // we will increase the text size by 15
                    float scale = (getDistance(event) - bastDst) / move;
                    float factor = (float) Math.pow(2, scale);
                    ratio = Math.min(1024.0f, Math.max(0.1f, baseratio * factor));
                    //text.setTextSize(ratio + 15);
                    //if (flagSansText)
                    //    textViewSans.setTextSize(ratio + 10);
                    //else
                    //    textViewIta.setTextSize(ratio + 10);
                    TextView textView = (TextView) v;
                    textView.setTextSize(ratio + 10);
                }
            }
            return true;
        }

        // get distance between the touch event
        private int getDistance(MotionEvent event) {
            int dx = (int) (event.getX(0) - event.getX(1));
            int dy = (int) (event.getY(0) - event.getY(1));
            return (int) Math.sqrt(dx * dx + dy * dy);
        }

    };

}
