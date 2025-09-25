package com.iricostruttori.meditazione.ui.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.iricostruttori.meditazione.R;
import com.iricostruttori.meditazione.databinding.FragmentSettingsBinding;

import java.util.StringTokenizer;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;
    private TextView editTimeInput ;
    private TextView readTimeOutput ;

    private long timeLeftInMs = 1 * 60000 ; //default value
    private int timeInMinutes = 1 ;
    private String strTimeDefault = "" ;

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private final String SHARED_SETTING_VALUE = "SHARED_SETTING_VALUE" ;
    private final String SHARED_TIME_DEFAULT = "SHARED_TIME_DEFAULT" ;
    private final String SHARED_ENABLE_ALL_DAY_AFTER_DAY = "SHARED_ENABLE_ALL_DAY_AFTER_DAY";
    private boolean flagShowVersion = false ;
    private int numShowVersion = 0 ;
    private String strDateVersion = "30/08/2024";
    /*
    String stringToSave = "Save me!";

    // To save data to SP
    SharedPreferences.Editor editor = getContext().getSharedPreferences(SHARED_NAME_STRING, MODE_PRIVATE).edit();
    editor.putString(USER_NAME_STRING, stringToSave);
    editor.apply();

    // To load the data at a later time
    SharedPreferences prefs = getContext().getSharedPreferences(SHARED_NAME_STRING, MODE_PRIVATE);
    String loadedString = prefs.getString(USER_NAME_STRING, null);

    * */
    private void setTime(long milliseconds) {
        timeLeftInMs = milliseconds;

        System.out.println("[setTime] milliseconds " + milliseconds) ;
        // Questo è quello che fa updateTimer
        int minutes = (int) timeLeftInMs / 60000; // in ms
        int seconds = (int) timeLeftInMs % 60000 / 1000;

        String timeLeftText = "" + minutes;
        timeLeftText += ":";
        if (seconds < 10) {
            timeLeftText += "0";
        }
        timeLeftText += seconds;
        System.out.println("[setTime] timeLeftText " + timeLeftText) ;

        //readTimeOutput.setText(timeLeftText);
        editTimeInput.setText(timeLeftText);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //SettingsViewModel settingsViewModel =
        //        new ViewModelProvider(this).get(SettingsViewModel.class);


        // Start Presi da master sample
        getActivity().setTheme(R.style.Theme_Meditazioneiricostruttoriresp);
        // remove notifications bar
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        // End Presi da master sample

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        editTimeInput = binding.writeTimeDefault;
        readTimeOutput = binding.readTimeDefault;
        mPreferences = getContext().getSharedPreferences(SHARED_SETTING_VALUE, Context.MODE_PRIVATE);
        String strTimeDefault = mPreferences.getString(SHARED_TIME_DEFAULT, null);

        if (strTimeDefault == null || strTimeDefault.equals("")) {
            strTimeDefault = Resources.getSystem().getString( R.string.timeDefaultInitial);
            editTimeInput.setText(strTimeDefault) ;
            readTimeOutput.setText("Tempo Meditazione " + strTimeDefault);
        }
        else {
            editTimeInput.setText(strTimeDefault);
            readTimeOutput.setText("Tempo Meditazione " + strTimeDefault);
        }


        //readTimeOutput.setText("Tempo Meditazione");
        Button saveBtn = binding.saveBtnSettings;
        Button subMinutesBtn = binding.subMinutesBtnSettings;
        Button sumMinutesBtn = binding.sumMinutesBtnSettings;
        ImageButton versionImgBtn = binding.imageButtonVersion;
        //TextView versionTextView = binding.textViewVersion;
        //versionTextView.setVisibility(View.INVISIBLE);
        //versionTextView.setText();

        // On Click Listeners
        sumMinutesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String input = editTimeInput.getText().toString();
                System.out.println("[sumMinutesBtn.click] input" + input);
                StringTokenizer st = new StringTokenizer(input,":");
                String strMinute = st.nextToken();
                timeInMinutes = Integer.valueOf(strMinute) + 1 ;

                long msInput = timeInMinutes * 60000;
                setTime(msInput);
                //updateTimer();

            }
        });

        // On Click Listeners
        subMinutesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(MainActivity.this, "Valore precedente", Toast.LENGTH_SHORT).show();

                String input = editTimeInput.getText().toString();
                StringTokenizer st = new StringTokenizer(input,":");
                String strMinute = st.nextToken();
                timeInMinutes = Integer.valueOf(strMinute) - 1 ;

                long msInput = timeInMinutes * 60000;
                setTime(msInput);

            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle result = new Bundle();
                // Quando ci saranno altri param faremo una
                // String Tokenizer
                System.out.println(" editTimeInput " + editTimeInput.getText().toString());
                String strResult = editTimeInput.getText().toString();
                result.putString("timeDefaultBundleKey",strResult);
                readTimeOutput.setText("Tempo Meditazione " + strResult);

                // To save data to SP
                mEditor = getContext().getSharedPreferences(SHARED_SETTING_VALUE,Context.MODE_PRIVATE).edit();
                mEditor.putString(SHARED_TIME_DEFAULT, strResult);
                mEditor.apply();

                if (numShowVersion >= 3 ) {
                    // To save data to SP
                    //mEditor = getContext().getSharedPreferences(SHARED_SETTING_VALUE, Context.MODE_PRIVATE).edit();

                    mEditor.putString(SHARED_ENABLE_ALL_DAY_AFTER_DAY,"ON");
                    mEditor.apply();
                    numShowVersion=0;
                }
                else {
                    mEditor.putString(SHARED_ENABLE_ALL_DAY_AFTER_DAY,"OFF");
                    mEditor.apply();
                }
            }
        });

        versionImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                numShowVersion++;
                try {
                    PackageManager manager = getActivity().getPackageManager();
                    PackageInfo info = manager.getPackageInfo(getActivity().getPackageName(), PackageManager.GET_ACTIVITIES);
                    Toast.makeText(getActivity(),
                            "PackageName = " + info.packageName +
                                    "\nVersionCode = " + info.versionCode +
                                    "\nNumConfig   = " + numShowVersion +
                                    "\nData Versione   = " + strDateVersion +
                                    "\nVersionName = " + info.versionName +
                                    "\nSDK_INT = " + Build.VERSION.SDK_INT +
                                    "\nPermissions = " + info.permissions, Toast.LENGTH_LONG).show();
                }
                catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

                System.out.println(" numShowVersion " + numShowVersion);


                //strTimeDefault = strResult;
                //getParentFragmentManager().setFragmentResult("settingParamRequestKey", result);
            }
        });




        /*
        String[] strTextCantoIniziale = getTextCantoIniziale();

        String strFullCantoIniziale = "" ;
        for (int i=0 ; i<strTextCantoIniziale.length ; i++) {
            strFullCantoIniziale = strFullCantoIniziale + strTextCantoIniziale[i] + "\n";
            System.out.println("  " + strTextCantoIniziale[i] );
        }




        final TextView textViewDayAfterDay = new TextView(getContext());
        textViewDayAfterDay.setText(strFullCantoIniziale);
        scrollViewDayAfterDay.addView(textViewDayAfterDay);
        */


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    String[] getTextCantoIniziale() {

        String[] strText = getResources().getStringArray(R.array.giorno_0808);

        return strText;
    }

    String[] getTextCantoFinale() {

        String[] strText = getResources().getStringArray(R.array.giorno_0808);

        return strText;
    }
}