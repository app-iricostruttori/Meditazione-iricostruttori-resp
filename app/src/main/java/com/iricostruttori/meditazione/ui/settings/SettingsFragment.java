package com.iricostruttori.meditazione.ui.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
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

        editTimeInput.setText(timeLeftText);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //SettingsViewModel settingsViewModel =
        //        new ViewModelProvider(this).get(SettingsViewModel.class);


        // Start Presi da master sample
        getActivity().setTheme(R.style.Theme_Meditazioneiricostruttoriresp);
        //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        // remove notifications bar
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        editTimeInput = binding.writeTimeDefault;
        readTimeOutput = binding.readTimeDefault;

        mPreferences = getContext().getSharedPreferences(SHARED_SETTING_VALUE, Context.MODE_PRIVATE);
        String strTimeDefault = mPreferences.getString(SHARED_TIME_DEFAULT, null);


        if (strTimeDefault == null || strTimeDefault.equals("")) {
            // KO RUN TIME INSPIEGABILE
            //strTimeDefault = Resources.getSystem().getString( R.string.timeDefaultIniti;
            strTimeDefault = "30:00" ;
            editTimeInput.setText(strTimeDefault) ;
            readTimeOutput.setText("Tempo Meditazione " + strTimeDefault);
        }
        else {
            editTimeInput.setText(strTimeDefault);
            readTimeOutput.setText("Tempo Meditazione " + strTimeDefault);
        }

        Button saveBtn = binding.saveBtnSettings;
        Button subMinutesBtn = binding.subMinutesBtnSettings;
        Button sumMinutesBtn = binding.sumMinutesBtnSettings;
        ImageButton versionImgBtn = binding.imageButtonVersion;


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
                //try {

                    displayToastAboveButton(v);
                    /*
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
                */
                System.out.println(" numShowVersion " + numShowVersion);

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
        super.onDestroyView();
        binding = null;
    }

    // v is the Button view that you want the Toast to appear above
    // and messageId is the id of your string resource for the message
    //private void displayToastAboveButton(View v, int messageId)
    private void displayToastAboveButton(View v )
    {
        int xOffset = 0;
        int yOffset = 0;
        String strMessageInfo = "" ;
        Rect gvr = new Rect();

        View parent = (View) v.getParent();
        int parentHeight = parent.getHeight();
        boolean flagDisable = false;

        try {
                PackageManager manager = getActivity().getPackageManager();
                PackageInfo info = manager.getPackageInfo(getActivity().getPackageName(), PackageManager.GET_ACTIVITIES);

                //if (v.getGlobalVisibleRect(gvr))
                if (v.getGlobalVisibleRect(gvr) && !flagDisable )
                {
                    View root = v.getRootView();

                    int halfWidth = root.getRight() / 2;
                    int halfHeight = root.getBottom() / 2;

                    int parentCenterX = ((gvr.right - gvr.left) / 2) + gvr.left;

                    int parentCenterY = ((gvr.bottom - gvr.top) / 2) + gvr.top;

                    if (parentCenterY <= halfHeight)
                    {
                        yOffset = -(halfHeight - parentCenterY) - parentHeight;
                    }
                    else
                    {
                        yOffset = (parentCenterY - halfHeight) - parentHeight;
                    }

                    if (parentCenterX < halfWidth)
                    {
                        xOffset = -(halfWidth - parentCenterX);
                    }

                    if (parentCenterX >= halfWidth)
                    {
                        xOffset = parentCenterX - halfWidth;
                    }

                    strMessageInfo = //"PackageName = " + info.packageName +
                                    "Auto-SDK_INT = " + Build.VERSION.SDK_INT + "_" + info.versionName +
                                    "\nVersionCode = " + info.versionCode +
                                    "\nNumConfig   = " + numShowVersion +
                                    "\nData Versione   = " + strDateVersion +
                                    "\nVersionName = " + info.versionName +
                                    "\nPermissions = " + info.permissions ;

                    Toast toast = Toast.makeText(getActivity(), strMessageInfo, Toast.LENGTH_LONG);
                    //toast.setGravity(Gravity.CENTER, xOffset, yOffset);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, xOffset, yOffset);
                    toast.show();
                }
                else {

                    strMessageInfo = //"PackageName = " + info.packageName +
                                   "0-1040-SDK_INT = " + Build.VERSION.SDK_INT +
                                    "\nVersionCode = " + info.versionCode +
                                    "\nNumConfig   = " + numShowVersion +
                                    "\nData Versione   = " + strDateVersion +
                                    "\nVersionName = " + info.versionName +
                                    "\nPermissions = " + info.permissions ;
                    Toast toast = Toast.makeText(getActivity(), strMessageInfo, Toast.LENGTH_LONG);
                    //toast.setGravity(Gravity.START | Gravity.CENTER, 20, 0);
                    //toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    //toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.setGravity(Gravity.TOP, 0, 1040);
                    //toast.setGravity(Gravity.FILL_HORIZONTAL, 0, 0);
                    //toast.setGravity(Gravity.FILL_VERTICAL, 0, 0);
                    toast.show();
                }

        }
        catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}