package com.iricostruttori.meditazione.ui.pensieri;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;

//import com.iricostruttori.meditazione.databinding.FragmentSlideshowBinding;
import com.iricostruttori.meditazione.MainActivity;
import com.iricostruttori.meditazione.R;
import com.iricostruttori.meditazione.databinding.FragmentPensieriBinding;
import com.iricostruttori.meditazione.ui.incontri.IncontriFragment;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;

// https://stackoverflow.com/questions/30941803/how-to-get-items-from-a-string-array-using-an-id-based-mechanism
// https://stackoverflow.com/questions/64006140/how-to-read-and-parse-json-file-from-assets-in-fragment
public class PensieriFragment extends Fragment {

    //private FragmentSlideshowBinding binding;
    private FragmentPensieriBinding binding;
    private Spinner dropdown_pensieri_mese;
    private Spinner dropdown_pensieri_giorno;
    private TextView textViewDayAfterDay;
    private String strDateMonthSpinner ;
    private String strDateDaySpinner ;

    private SharedPreferences mPreferences;

    private final String SHARED_SETTING_VALUE = "SHARED_SETTING_VALUE" ;
    private final String SHARED_ENABLE_ALL_DAY_AFTER_DAY = "SHARED_ENABLE_ALL_DAY_AFTER_DAY" ;
    private boolean flagEnableSpinner = false ;

    private TouchTextView touchTextView = new TouchTextView(1.5f);

    public String getStrFullPensiero(String strDefaultDate,String strSelectedDate) {

        String strFullPensiero = "" ;
        System.out.println("[getStrFullPensiero]  START " + strSelectedDate );
        StringTokenizer stToken = new StringTokenizer(strSelectedDate," ");

        if (strSelectedDate.equals("") ||
                stToken.countTokens() != 2 ) {
            String[] strTextPensiero = getTextPensieroGiorno(strDefaultDate);

            for (int i=0 ; i<strTextPensiero.length ; i++) {
                strFullPensiero = strFullPensiero + strTextPensiero[i] + " ";
                System.out.println(" default date pensiero  " + strTextPensiero[i] );
            }

        }
        else {
            String strGiorno = stToken.nextToken();
            String strDescMese = stToken.nextToken();
            String strMese = "";
            System.out.println("[getStrFullPensiero]  strGiorno " + strGiorno );
            System.out.println("[getStrFullPensiero]  strDescMese " + strDescMese );
            if (strDescMese.equalsIgnoreCase("Gennaio"))
                strMese = "01";
            else if (strDescMese.equalsIgnoreCase("Febbraio"))
                strMese = "02";
            else if (strDescMese.equalsIgnoreCase("Marzo"))
                strMese = "03";
            else if (strDescMese.equalsIgnoreCase("Aprile"))
                strMese = "04";
            else if (strDescMese.equalsIgnoreCase("Maggio"))
                strMese = "05";
            else if (strDescMese.equalsIgnoreCase("Giugno"))
                strMese = "06";
            else if (strDescMese.equalsIgnoreCase("Luglio"))
                strMese = "07";
            else if (strDescMese.equalsIgnoreCase("Agosto"))
                strMese = "08";
            else if (strDescMese.equalsIgnoreCase("Settembre"))
                strMese = "09";
            else if (strDescMese.equalsIgnoreCase("Ottobre"))
                strMese = "10";
            else if (strDescMese.equalsIgnoreCase("Novembre"))
                strMese = "11";
            else
                strMese = "12";

            System.out.println("[getStrFullPensiero]  strGiorno+Mese " + strGiorno+strMese );
            String[] strTextPensiero = getTextPensieroGiorno(strGiorno+strMese);

            for (int i=0 ; i<strTextPensiero.length ; i++) {
                strFullPensiero = strFullPensiero + strTextPensiero[i] + " ";
                System.out.println(" Selected date Pensiero " + strTextPensiero[i] );
            }




        }

        return strFullPensiero;
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //PensieriViewModel pensieriViewModel =
        //        new ViewModelProvider(this).get(PensieriViewModel.class);

        getActivity().setTheme(R.style.Theme_Meditazioneiricostruttoriresp);
        //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();

        binding = FragmentPensieriBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        textViewDayAfterDay = binding.textPensiero;

        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        //dropdown = getActivity().findViewById(R.id.spinner_incontri);

        Locale locale = new Locale("it", "IT");
        java.util.Date objDate = new java.util.Date();
        String strDateDay = PensieriUtility.dateToString(objDate, "ddMM");

        strDateDaySpinner = PensieriUtility.dateToString(objDate, "EEEE dd MMMM");
        strDateMonthSpinner = PensieriUtility.dateToString(objDate, "MMMM");


        System.out.println("[PensieriFragment.onCreateView] strDateDay " + strDateDay);
        System.out.println("[PensieriFragment.onCreateView] strDateDaySpinner " + strDateDaySpinner);
        System.out.println("[PensieriFragment.onCreateView] strDateMonthSpinner " + strDateMonthSpinner);

        mPreferences = getContext().getSharedPreferences(SHARED_SETTING_VALUE, Context.MODE_PRIVATE);
        String strEnableAllDay = mPreferences.getString(SHARED_ENABLE_ALL_DAY_AFTER_DAY, null);
        System.out.println("[PensieriFragment.onCreateView] strEnableAllDay " + strEnableAllDay);
        flagEnableSpinner = false;
        if (strEnableAllDay != null && strEnableAllDay.equalsIgnoreCase("ON"))
            flagEnableSpinner = true;
        System.out.println("[PensieriFragment.onCreateView] flagEnableSpinner mese " + flagEnableSpinner);



        String[] items_pensieri_mese ;
        String[] items_pensieri_giorno ;

        if ( flagEnableSpinner ) {

            // START SPINNER MESE DELL'ANNO
            items_pensieri_mese = new String[]{strDateMonthSpinner,
                    PensieriConstants.strDescMese_01,
                    PensieriConstants.strDescMese_02,
                    PensieriConstants.strDescMese_03,
                    PensieriConstants.strDescMese_04,
                    PensieriConstants.strDescMese_05,
                    PensieriConstants.strDescMese_06,
                    PensieriConstants.strDescMese_07,
                    PensieriConstants.strDescMese_08,
                    PensieriConstants.strDescMese_09,
                    PensieriConstants.strDescMese_10,
                    PensieriConstants.strDescMese_11,
                    PensieriConstants.strDescMese_12};

            dropdown_pensieri_mese = root.findViewById(R.id.spinner_pensieri_mese);
            ArrayAdapter<String> adapter_month = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, items_pensieri_mese);
            dropdown_pensieri_mese.setEnabled(true);
            dropdown_pensieri_mese.setClickable(true);
            dropdown_pensieri_mese.setAdapter(adapter_month);
            dropdown_pensieri_mese.setVisibility(View.VISIBLE);



            dropdown_pensieri_mese.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    String strSelected = (String) parent.getItemAtPosition(position);
                    System.out.println("[onItemSelected] strSelectedMonth " + strSelected);
                    strDateMonthSpinner = strSelected;
                    // Quando cambio mese riparto dal primo del mese
                    //if (flagEnableSpinner)
                    strDateDaySpinner = "01 " + strDateMonthSpinner;

                    textViewDayAfterDay.setText("");
                    String[] items_pensieri_giorno =  PensieriUtility.getItemsPensieriGiorno(strDateDaySpinner,
                            strDateMonthSpinner);
                    ArrayAdapter<String> adapter_day_new = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, items_pensieri_giorno);
                    dropdown_pensieri_giorno.setAdapter(adapter_day_new);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // TODO Auto-generated method stub
                }

            });
            // END SPINNER MESE DELL'ANNO

            // START SPINNER GIORNO DEL MESE
            items_pensieri_giorno =  PensieriUtility.getItemsPensieriGiorno(strDateDaySpinner,
                    strDateMonthSpinner);
            dropdown_pensieri_giorno = root.findViewById(R.id.spinner_pensieri_giorno);
            ArrayAdapter<String> adapter_day = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, items_pensieri_giorno);
            //set the spinners adapter to the previously created one.
            dropdown_pensieri_giorno.setAdapter(adapter_day);
            //System.out.println("[PensieriFragment.onCreateView] flagEnableSpinner giorno " + flagEnableSpinner);
            dropdown_pensieri_giorno.setEnabled(true);
            dropdown_pensieri_giorno.setClickable(true);
            dropdown_pensieri_giorno.setVisibility(View.VISIBLE);

            dropdown_pensieri_giorno.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    String strSelected = (String) parent.getItemAtPosition(position);
                    System.out.println("[onItemSelected] strSelectedDay " + strSelected);
                    textViewDayAfterDay.setText(getStrFullPensiero(strDateDay,strSelected));

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // TODO Auto-generated method stub
                }

            });
            // END SPINNER GIORNO DEL MESE

        }
        else {

            // START SPINNER MESE DELL'ANNO
            items_pensieri_mese= new String[]{strDateMonthSpinner};

            dropdown_pensieri_mese = root.findViewById(R.id.spinner_pensieri_mese);
            ArrayAdapter<String> adapter_month = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, items_pensieri_mese);
            //dropdown_pensieri_mese.setBackgroundColor(Color.BLUE);
            dropdown_pensieri_mese.setEnabled(true);
            //dropdown_pensieri_mese.setClickable(false);

            dropdown_pensieri_mese.setAdapter(adapter_month);
            dropdown_pensieri_mese.setVisibility(View.VISIBLE);



            dropdown_pensieri_mese.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    String[] items_pensieri_giorno = new String[1];
                    items_pensieri_giorno[0] = strDateDaySpinner;

                    ArrayAdapter<String> adapter_day_new = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, items_pensieri_giorno);
                    dropdown_pensieri_giorno.setAdapter(adapter_day_new);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // TODO Auto-generated method stub
                }

            });
            // END SPINNER MESE DELL'ANNO

            // START SPINNER GIORNO DEL MESE
            items_pensieri_giorno = new String[1];
            items_pensieri_giorno[0] = strDateDaySpinner;

            dropdown_pensieri_giorno = root.findViewById(R.id.spinner_pensieri_giorno);
            ArrayAdapter<String> adapter_day = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, items_pensieri_giorno);
            //set the spinners adapter to the previously created one.
            dropdown_pensieri_giorno.setAdapter(adapter_day);

            dropdown_pensieri_giorno.setEnabled(true);

            dropdown_pensieri_giorno.setVisibility(View.VISIBLE);

            dropdown_pensieri_giorno.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    System.out.println("[onItemSelected]  strDateDay " + strDateDay);
                    textViewDayAfterDay.setText(getStrFullPensiero(strDateDay,"" ));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // TODO Auto-generated method stub
                    System.out.println("[onNothingSelected]  " );
                }

            });
            // END SPINNER GIORNO DEL MESE

        }

        textViewDayAfterDay.setText(getStrFullPensiero(strDateDay,""));
        textViewDayAfterDay.setTextSize(touchTextView.ratio + 10);
        textViewDayAfterDay.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                // Respond to touch events.
                //touchTextView.onTouchMyText(event);
                touchTextView.onTouchMyText(v,event);
                return true;
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

    //@Override

    private String getStringResourceByName(String aString) {
        String packageName = getActivity().getPackageName();
        int resId = getResources()
                .getIdentifier(aString, "string", packageName);
        if (resId == 0) {
            return aString;
        } else {
            return getString(resId);
        }
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open("giorno_dopo_giorno.json"); // your file name
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    String[] getTextPensieroGiorno(String strDateDay) {

        String[] strText = null;

        System.out.println("[getTextPensieroGiorno] strDateDay " + strDateDay );
        if (strDateDay.equals("0101"))
            strText = getResources().getStringArray(R.array.giorno_0101);
        else if (strDateDay.equals("0201"))
            strText = getResources().getStringArray(R.array.giorno_0201);
        else if (strDateDay.equals("0301"))
            strText = getResources().getStringArray(R.array.giorno_0301);
        else if (strDateDay.equals("0401"))
            strText = getResources().getStringArray(R.array.giorno_0401);
        else if (strDateDay.equals("0501"))
            strText = getResources().getStringArray(R.array.giorno_0501);
        else if (strDateDay.equals("0601"))
            strText = getResources().getStringArray(R.array.giorno_0601);
        else if (strDateDay.equals("0701"))
            strText = getResources().getStringArray(R.array.giorno_0701);
        else if (strDateDay.equals("0801"))
            strText = getResources().getStringArray(R.array.giorno_0801);
        else if (strDateDay.equals("0901"))
            strText = getResources().getStringArray(R.array.giorno_0901);
        else if (strDateDay.equals("1001"))
            strText = getResources().getStringArray(R.array.giorno_1001);
        else if (strDateDay.equals("1101"))
            strText = getResources().getStringArray(R.array.giorno_1101);
        else if (strDateDay.equals("1201"))
            strText = getResources().getStringArray(R.array.giorno_1201);
        else if (strDateDay.equals("1301"))
            strText = getResources().getStringArray(R.array.giorno_1301);
        else if (strDateDay.equals("1401"))
            strText = getResources().getStringArray(R.array.giorno_1401);
        else if (strDateDay.equals("1501"))
            strText = getResources().getStringArray(R.array.giorno_1501);
        else if (strDateDay.equals("1601"))
            strText = getResources().getStringArray(R.array.giorno_1601);
        else if (strDateDay.equals("1701"))
            strText = getResources().getStringArray(R.array.giorno_1701);
        else if (strDateDay.equals("1801"))
            strText = getResources().getStringArray(R.array.giorno_1801);
        else if (strDateDay.equals("1901"))
            strText = getResources().getStringArray(R.array.giorno_1901);
        else if (strDateDay.equals("2001"))
            strText = getResources().getStringArray(R.array.giorno_2001);
        else if (strDateDay.equals("2101"))
            strText = getResources().getStringArray(R.array.giorno_2101);
        else if (strDateDay.equals("2201"))
            strText = getResources().getStringArray(R.array.giorno_2201);
        else if (strDateDay.equals("2301"))
            strText = getResources().getStringArray(R.array.giorno_2301);
        else if (strDateDay.equals("2401"))
            strText = getResources().getStringArray(R.array.giorno_2401);
        else if (strDateDay.equals("2501"))
            strText = getResources().getStringArray(R.array.giorno_2501);
        else if (strDateDay.equals("2601"))
            strText = getResources().getStringArray(R.array.giorno_2601);
        else if (strDateDay.equals("2701"))
            strText = getResources().getStringArray(R.array.giorno_2701);
        else if (strDateDay.equals("2801"))
            strText = getResources().getStringArray(R.array.giorno_2801);
        else if (strDateDay.equals("2901"))
            strText = getResources().getStringArray(R.array.giorno_2901);
        else if (strDateDay.equals("3001"))
            strText = getResources().getStringArray(R.array.giorno_3001);
        else if (strDateDay.equals("3101"))
            strText = getResources().getStringArray(R.array.giorno_3101);
        else if (strDateDay.equals("0102"))
            strText = getResources().getStringArray(R.array.giorno_0102);
        else if (strDateDay.equals("0202"))
            strText = getResources().getStringArray(R.array.giorno_0202);
        else if (strDateDay.equals("0302"))
            strText = getResources().getStringArray(R.array.giorno_0302);
        else if (strDateDay.equals("0402"))
            strText = getResources().getStringArray(R.array.giorno_0402);
        else if (strDateDay.equals("0502"))
            strText = getResources().getStringArray(R.array.giorno_0502);
        else if (strDateDay.equals("0602"))
            strText = getResources().getStringArray(R.array.giorno_0602);
        else if (strDateDay.equals("0702"))
            strText = getResources().getStringArray(R.array.giorno_0702);
        else if (strDateDay.equals("0802"))
            strText = getResources().getStringArray(R.array.giorno_0802);
        else if (strDateDay.equals("0902"))
            strText = getResources().getStringArray(R.array.giorno_0902);
        else if (strDateDay.equals("1002"))
            strText = getResources().getStringArray(R.array.giorno_1002);
        else if (strDateDay.equals("1102"))
            strText = getResources().getStringArray(R.array.giorno_1102);
        else if (strDateDay.equals("1202"))
            strText = getResources().getStringArray(R.array.giorno_1202);
        else if (strDateDay.equals("1302"))
            strText = getResources().getStringArray(R.array.giorno_1302);
        else if (strDateDay.equals("1402"))
            strText = getResources().getStringArray(R.array.giorno_1402);
        else if (strDateDay.equals("1502"))
            strText = getResources().getStringArray(R.array.giorno_1502);
        else if (strDateDay.equals("1602"))
            strText = getResources().getStringArray(R.array.giorno_1602);
        else if (strDateDay.equals("1702"))
            strText = getResources().getStringArray(R.array.giorno_1702);
        else if (strDateDay.equals("1802"))
            strText = getResources().getStringArray(R.array.giorno_1802);
        else if (strDateDay.equals("1902"))
            strText = getResources().getStringArray(R.array.giorno_1902);
        else if (strDateDay.equals("2002"))
            strText = getResources().getStringArray(R.array.giorno_2002);
        else if (strDateDay.equals("2102"))
            strText = getResources().getStringArray(R.array.giorno_2102);
        else if (strDateDay.equals("2202"))
            strText = getResources().getStringArray(R.array.giorno_2202);
        else if (strDateDay.equals("2302"))
            strText = getResources().getStringArray(R.array.giorno_2302);
        else if (strDateDay.equals("2402"))
            strText = getResources().getStringArray(R.array.giorno_2402);
        else if (strDateDay.equals("2502"))
            strText = getResources().getStringArray(R.array.giorno_2502);
        else if (strDateDay.equals("2602"))
            strText = getResources().getStringArray(R.array.giorno_2602);
        else if (strDateDay.equals("2702"))
            strText = getResources().getStringArray(R.array.giorno_2702);
        else if (strDateDay.equals("2802"))
            strText = getResources().getStringArray(R.array.giorno_2802);
        else if (strDateDay.equals("2902"))
            strText = getResources().getStringArray(R.array.giorno_2902);

        else if (strDateDay.equals("0103"))
            strText = getResources().getStringArray(R.array.giorno_0103);
        else if (strDateDay.equals("0203"))
            strText = getResources().getStringArray(R.array.giorno_0203);
        else if (strDateDay.equals("0303"))
            strText = getResources().getStringArray(R.array.giorno_0303);
        else if (strDateDay.equals("0403"))
            strText = getResources().getStringArray(R.array.giorno_0403);
        else if (strDateDay.equals("0503"))
            strText = getResources().getStringArray(R.array.giorno_0503);
        else if (strDateDay.equals("0603"))
            strText = getResources().getStringArray(R.array.giorno_0603);
        else if (strDateDay.equals("0703"))
            strText = getResources().getStringArray(R.array.giorno_0703);
        else if (strDateDay.equals("0803"))
            strText = getResources().getStringArray(R.array.giorno_0803);
        else if (strDateDay.equals("0903"))
            strText = getResources().getStringArray(R.array.giorno_0903);
        else if (strDateDay.equals("1003"))
            strText = getResources().getStringArray(R.array.giorno_1003);
        else if (strDateDay.equals("1103"))
            strText = getResources().getStringArray(R.array.giorno_1103);
        else if (strDateDay.equals("1203"))
            strText = getResources().getStringArray(R.array.giorno_1203);
        else if (strDateDay.equals("1303"))
            strText = getResources().getStringArray(R.array.giorno_1303);
        else if (strDateDay.equals("1403"))
            strText = getResources().getStringArray(R.array.giorno_1403);
        else if (strDateDay.equals("1503"))
            strText = getResources().getStringArray(R.array.giorno_1503);
        else if (strDateDay.equals("1603"))
            strText = getResources().getStringArray(R.array.giorno_1603);
        else if (strDateDay.equals("1703"))
            strText = getResources().getStringArray(R.array.giorno_1703);
        else if (strDateDay.equals("1803"))
            strText = getResources().getStringArray(R.array.giorno_1803);
        else if (strDateDay.equals("1903"))
            strText = getResources().getStringArray(R.array.giorno_1903);
        else if (strDateDay.equals("2003"))
            strText = getResources().getStringArray(R.array.giorno_2003);
        else if (strDateDay.equals("2103"))
            strText = getResources().getStringArray(R.array.giorno_2103);
        else if (strDateDay.equals("2203"))
            strText = getResources().getStringArray(R.array.giorno_2203);
        else if (strDateDay.equals("2303"))
            strText = getResources().getStringArray(R.array.giorno_2303);
        else if (strDateDay.equals("2403"))
            strText = getResources().getStringArray(R.array.giorno_2403);
        else if (strDateDay.equals("2503"))
            strText = getResources().getStringArray(R.array.giorno_2503);
        else if (strDateDay.equals("2603"))
            strText = getResources().getStringArray(R.array.giorno_2603);
        else if (strDateDay.equals("2703"))
            strText = getResources().getStringArray(R.array.giorno_2703);
        else if (strDateDay.equals("2803"))
            strText = getResources().getStringArray(R.array.giorno_2803);
        else if (strDateDay.equals("2903"))
            strText = getResources().getStringArray(R.array.giorno_2903);
        else if (strDateDay.equals("3003"))
            strText = getResources().getStringArray(R.array.giorno_3003);
        else if (strDateDay.equals("3103"))
            strText = getResources().getStringArray(R.array.giorno_3103);
        else if (strDateDay.equals("0104"))
            strText = getResources().getStringArray(R.array.giorno_0104);
        else if (strDateDay.equals("0204"))
            strText = getResources().getStringArray(R.array.giorno_0204);
        else if (strDateDay.equals("0304"))
            strText = getResources().getStringArray(R.array.giorno_0304);
        else if (strDateDay.equals("0404"))
            strText = getResources().getStringArray(R.array.giorno_0404);
        else if (strDateDay.equals("0504"))
            strText = getResources().getStringArray(R.array.giorno_0504);
        else if (strDateDay.equals("0604"))
            strText = getResources().getStringArray(R.array.giorno_0604);
        else if (strDateDay.equals("0704"))
            strText = getResources().getStringArray(R.array.giorno_0704);
        else if (strDateDay.equals("0804"))
            strText = getResources().getStringArray(R.array.giorno_0804);
        else if (strDateDay.equals("0904"))
            strText = getResources().getStringArray(R.array.giorno_0904);
        else if (strDateDay.equals("1004"))
            strText = getResources().getStringArray(R.array.giorno_1004);
        else if (strDateDay.equals("1104"))
            strText = getResources().getStringArray(R.array.giorno_1104);
        else if (strDateDay.equals("1204"))
            strText = getResources().getStringArray(R.array.giorno_1204);
        else if (strDateDay.equals("1304"))
            strText = getResources().getStringArray(R.array.giorno_1304);
        else if (strDateDay.equals("1404"))
            strText = getResources().getStringArray(R.array.giorno_1404);
        else if (strDateDay.equals("1504"))
            strText = getResources().getStringArray(R.array.giorno_1504);
        else if (strDateDay.equals("1604"))
            strText = getResources().getStringArray(R.array.giorno_1604);
        else if (strDateDay.equals("1704"))
            strText = getResources().getStringArray(R.array.giorno_1704);
        else if (strDateDay.equals("1804"))
            strText = getResources().getStringArray(R.array.giorno_1804);
        else if (strDateDay.equals("1904"))
            strText = getResources().getStringArray(R.array.giorno_1904);
        else if (strDateDay.equals("2004"))
            strText = getResources().getStringArray(R.array.giorno_2004);
        else if (strDateDay.equals("2104"))
            strText = getResources().getStringArray(R.array.giorno_2104);
        else if (strDateDay.equals("2204"))
            strText = getResources().getStringArray(R.array.giorno_2204);
        else if (strDateDay.equals("2304"))
            strText = getResources().getStringArray(R.array.giorno_2304);
        else if (strDateDay.equals("2404"))
            strText = getResources().getStringArray(R.array.giorno_2404);
        else if (strDateDay.equals("2504"))
            strText = getResources().getStringArray(R.array.giorno_2504);
        else if (strDateDay.equals("2604"))
            strText = getResources().getStringArray(R.array.giorno_2604);
        else if (strDateDay.equals("2704"))
            strText = getResources().getStringArray(R.array.giorno_2704);
        else if (strDateDay.equals("2804"))
            strText = getResources().getStringArray(R.array.giorno_2804);
        else if (strDateDay.equals("2904"))
            strText = getResources().getStringArray(R.array.giorno_2904);
        else if (strDateDay.equals("3004"))
            strText = getResources().getStringArray(R.array.giorno_3004);
        else if (strDateDay.equals("0105"))
            strText = getResources().getStringArray(R.array.giorno_0105);
        else if (strDateDay.equals("0205"))
            strText = getResources().getStringArray(R.array.giorno_0205);
        else if (strDateDay.equals("0305"))
            strText = getResources().getStringArray(R.array.giorno_0305);
        else if (strDateDay.equals("0405"))
            strText = getResources().getStringArray(R.array.giorno_0405);
        else if (strDateDay.equals("0505"))
            strText = getResources().getStringArray(R.array.giorno_0505);
        else if (strDateDay.equals("0605"))
            strText = getResources().getStringArray(R.array.giorno_0605);
        else if (strDateDay.equals("0705"))
            strText = getResources().getStringArray(R.array.giorno_0705);
        else if (strDateDay.equals("0805"))
            strText = getResources().getStringArray(R.array.giorno_0805);
        else if (strDateDay.equals("0905"))
            strText = getResources().getStringArray(R.array.giorno_0905);
        else if (strDateDay.equals("1005"))
            strText = getResources().getStringArray(R.array.giorno_1005);
        else if (strDateDay.equals("1105"))
            strText = getResources().getStringArray(R.array.giorno_1105);
        else if (strDateDay.equals("1205"))
            strText = getResources().getStringArray(R.array.giorno_1205);
        else if (strDateDay.equals("1305"))
            strText = getResources().getStringArray(R.array.giorno_1305);
        else if (strDateDay.equals("1405"))
            strText = getResources().getStringArray(R.array.giorno_1405);
        else if (strDateDay.equals("1505"))
            strText = getResources().getStringArray(R.array.giorno_1505);
        else if (strDateDay.equals("1605"))
            strText = getResources().getStringArray(R.array.giorno_1605);
        else if (strDateDay.equals("1705"))
            strText = getResources().getStringArray(R.array.giorno_1705);
        else if (strDateDay.equals("1805"))
            strText = getResources().getStringArray(R.array.giorno_1805);
        else if (strDateDay.equals("1905"))
            strText = getResources().getStringArray(R.array.giorno_1905);
        else if (strDateDay.equals("2005"))
            strText = getResources().getStringArray(R.array.giorno_2005);
        else if (strDateDay.equals("2105"))
            strText = getResources().getStringArray(R.array.giorno_2105);
        else if (strDateDay.equals("2205"))
            strText = getResources().getStringArray(R.array.giorno_2205);
        else if (strDateDay.equals("2305"))
            strText = getResources().getStringArray(R.array.giorno_2305);
        else if (strDateDay.equals("2405"))
            strText = getResources().getStringArray(R.array.giorno_2405);
        else if (strDateDay.equals("2505"))
            strText = getResources().getStringArray(R.array.giorno_2505);
        else if (strDateDay.equals("2605"))
            strText = getResources().getStringArray(R.array.giorno_2605);
        else if (strDateDay.equals("2705"))
            strText = getResources().getStringArray(R.array.giorno_2705);
        else if (strDateDay.equals("2805"))
            strText = getResources().getStringArray(R.array.giorno_2805);
        else if (strDateDay.equals("2905"))
            strText = getResources().getStringArray(R.array.giorno_2905);
        else if (strDateDay.equals("3005"))
            strText = getResources().getStringArray(R.array.giorno_3005);
        else if (strDateDay.equals("3105"))
            strText = getResources().getStringArray(R.array.giorno_3105);
        else if (strDateDay.equals("0106"))
            strText = getResources().getStringArray(R.array.giorno_0106);
        else if (strDateDay.equals("0206"))
            strText = getResources().getStringArray(R.array.giorno_0206);
        else if (strDateDay.equals("0306"))
            strText = getResources().getStringArray(R.array.giorno_0306);
        else if (strDateDay.equals("0406"))
            strText = getResources().getStringArray(R.array.giorno_0406);
        else if (strDateDay.equals("0506"))
            strText = getResources().getStringArray(R.array.giorno_0506);
        else if (strDateDay.equals("0606"))
            strText = getResources().getStringArray(R.array.giorno_0606);
        else if (strDateDay.equals("0706"))
            strText = getResources().getStringArray(R.array.giorno_0706);
        else if (strDateDay.equals("0806"))
            strText = getResources().getStringArray(R.array.giorno_0806);
        else if (strDateDay.equals("0906"))
            strText = getResources().getStringArray(R.array.giorno_0906);
        else if (strDateDay.equals("1006"))
            strText = getResources().getStringArray(R.array.giorno_1006);
        else if (strDateDay.equals("1106"))
            strText = getResources().getStringArray(R.array.giorno_1106);
        else if (strDateDay.equals("1206"))
            strText = getResources().getStringArray(R.array.giorno_1206);
        else if (strDateDay.equals("1306"))
            strText = getResources().getStringArray(R.array.giorno_1306);
        else if (strDateDay.equals("1406"))
            strText = getResources().getStringArray(R.array.giorno_1406);
        else if (strDateDay.equals("1506"))
            strText = getResources().getStringArray(R.array.giorno_1506);
        else if (strDateDay.equals("1606"))
            strText = getResources().getStringArray(R.array.giorno_1606);
        else if (strDateDay.equals("1706"))
            strText = getResources().getStringArray(R.array.giorno_1706);
        else if (strDateDay.equals("1806"))
            strText = getResources().getStringArray(R.array.giorno_1806);
        else if (strDateDay.equals("1906"))
            strText = getResources().getStringArray(R.array.giorno_1906);
        else if (strDateDay.equals("2006"))
            strText = getResources().getStringArray(R.array.giorno_2006);
        else if (strDateDay.equals("2106"))
            strText = getResources().getStringArray(R.array.giorno_2106);
        else if (strDateDay.equals("2206"))
            strText = getResources().getStringArray(R.array.giorno_2206);
        else if (strDateDay.equals("2306"))
            strText = getResources().getStringArray(R.array.giorno_2306);
        else if (strDateDay.equals("2406"))
            strText = getResources().getStringArray(R.array.giorno_2406);
        else if (strDateDay.equals("2506"))
            strText = getResources().getStringArray(R.array.giorno_2506);
        else if (strDateDay.equals("2606"))
            strText = getResources().getStringArray(R.array.giorno_2606);
        else if (strDateDay.equals("2706"))
            strText = getResources().getStringArray(R.array.giorno_2706);
        else if (strDateDay.equals("2806"))
            strText = getResources().getStringArray(R.array.giorno_2806);
        else if (strDateDay.equals("2906"))
            strText = getResources().getStringArray(R.array.giorno_2906);
        else if (strDateDay.equals("3006"))
            strText = getResources().getStringArray(R.array.giorno_3006);
        else if (strDateDay.equals("0107"))
            strText = getResources().getStringArray(R.array.giorno_0107);
        else if (strDateDay.equals("0207"))
            strText = getResources().getStringArray(R.array.giorno_0207);
        else if (strDateDay.equals("0307"))
            strText = getResources().getStringArray(R.array.giorno_0307);
        else if (strDateDay.equals("0407"))
            strText = getResources().getStringArray(R.array.giorno_0407);
        else if (strDateDay.equals("0507"))
            strText = getResources().getStringArray(R.array.giorno_0507);
        else if (strDateDay.equals("0607"))
            strText = getResources().getStringArray(R.array.giorno_0607);
        else if (strDateDay.equals("0707"))
            strText = getResources().getStringArray(R.array.giorno_0707);
        else if (strDateDay.equals("0807"))
            strText = getResources().getStringArray(R.array.giorno_0807);
        else if (strDateDay.equals("0907"))
            strText = getResources().getStringArray(R.array.giorno_0907);
        else if (strDateDay.equals("1007"))
            strText = getResources().getStringArray(R.array.giorno_1007);
        else if (strDateDay.equals("1107"))
            strText = getResources().getStringArray(R.array.giorno_1107);
        else if (strDateDay.equals("1207"))
            strText = getResources().getStringArray(R.array.giorno_1207);
        else if (strDateDay.equals("1307"))
            strText = getResources().getStringArray(R.array.giorno_1307);
        else if (strDateDay.equals("1407"))
            strText = getResources().getStringArray(R.array.giorno_1407);
        else if (strDateDay.equals("1507"))
            strText = getResources().getStringArray(R.array.giorno_1507);
        else if (strDateDay.equals("1607"))
            strText = getResources().getStringArray(R.array.giorno_1607);
        else if (strDateDay.equals("1707"))
            strText = getResources().getStringArray(R.array.giorno_1707);
        else if (strDateDay.equals("1807"))
            strText = getResources().getStringArray(R.array.giorno_1807);
        else if (strDateDay.equals("1907"))
            strText = getResources().getStringArray(R.array.giorno_1907);
        else if (strDateDay.equals("2007"))
            strText = getResources().getStringArray(R.array.giorno_2007);
        else if (strDateDay.equals("2107"))
            strText = getResources().getStringArray(R.array.giorno_2107);
        else if (strDateDay.equals("2207"))
            strText = getResources().getStringArray(R.array.giorno_2207);
        else if (strDateDay.equals("2307"))
            strText = getResources().getStringArray(R.array.giorno_2307);
        else if (strDateDay.equals("2407"))
            strText = getResources().getStringArray(R.array.giorno_2407);
        else if (strDateDay.equals("2507"))
            strText = getResources().getStringArray(R.array.giorno_2507);
        else if (strDateDay.equals("2607"))
            strText = getResources().getStringArray(R.array.giorno_2607);
        else if (strDateDay.equals("2707"))
            strText = getResources().getStringArray(R.array.giorno_2707);
        else if (strDateDay.equals("2807"))
            strText = getResources().getStringArray(R.array.giorno_2807);
        else if (strDateDay.equals("2907"))
            strText = getResources().getStringArray(R.array.giorno_2907);
        else if (strDateDay.equals("3007"))
            strText = getResources().getStringArray(R.array.giorno_3007);
        else if (strDateDay.equals("3107"))
            strText = getResources().getStringArray(R.array.giorno_3107);
        else if (strDateDay.equals("0108"))
            strText = getResources().getStringArray(R.array.giorno_0108);
        else if (strDateDay.equals("0208"))
            strText = getResources().getStringArray(R.array.giorno_0208);
        else if (strDateDay.equals("0308"))
            strText = getResources().getStringArray(R.array.giorno_0308);
        else if (strDateDay.equals("0408"))
            strText = getResources().getStringArray(R.array.giorno_0408);
        else if (strDateDay.equals("0508"))
            strText = getResources().getStringArray(R.array.giorno_0508);
        else if (strDateDay.equals("0608"))
            strText = getResources().getStringArray(R.array.giorno_0608);
        else if (strDateDay.equals("0708"))
            strText = getResources().getStringArray(R.array.giorno_0708);
        else if (strDateDay.equals("0808"))
            strText = getResources().getStringArray(R.array.giorno_0808);
        else if (strDateDay.equals("0908"))
            strText = getResources().getStringArray(R.array.giorno_0908);
        else if (strDateDay.equals("1008"))
            strText = getResources().getStringArray(R.array.giorno_1008);
        else if (strDateDay.equals("1108"))
            strText = getResources().getStringArray(R.array.giorno_1108);
        else if (strDateDay.equals("1208"))
            strText = getResources().getStringArray(R.array.giorno_1208);
        else if (strDateDay.equals("1308"))
            strText = getResources().getStringArray(R.array.giorno_1308);
        else if (strDateDay.equals("1408"))
            strText = getResources().getStringArray(R.array.giorno_1408);
        else if (strDateDay.equals("1508"))
            strText = getResources().getStringArray(R.array.giorno_1508);
        else if (strDateDay.equals("1608"))
            strText = getResources().getStringArray(R.array.giorno_1608);
        else if (strDateDay.equals("1708"))
            strText = getResources().getStringArray(R.array.giorno_1708);
        else if (strDateDay.equals("1808"))
            strText = getResources().getStringArray(R.array.giorno_1808);
        else if (strDateDay.equals("1908"))
            strText = getResources().getStringArray(R.array.giorno_1908);
        else if (strDateDay.equals("2008"))
            strText = getResources().getStringArray(R.array.giorno_2008);
        else if (strDateDay.equals("2108"))
            strText = getResources().getStringArray(R.array.giorno_2108);
        else if (strDateDay.equals("2208"))
            strText = getResources().getStringArray(R.array.giorno_2208);
        else if (strDateDay.equals("2308"))
            strText = getResources().getStringArray(R.array.giorno_2308);
        else if (strDateDay.equals("2408"))
            strText = getResources().getStringArray(R.array.giorno_2408);
        else if (strDateDay.equals("2508"))
            strText = getResources().getStringArray(R.array.giorno_2508);
        else if (strDateDay.equals("2608"))
            strText = getResources().getStringArray(R.array.giorno_2608);
        else if (strDateDay.equals("2708"))
            strText = getResources().getStringArray(R.array.giorno_2708);
        else if (strDateDay.equals("2808"))
            strText = getResources().getStringArray(R.array.giorno_2808);
        else if (strDateDay.equals("2908"))
            strText = getResources().getStringArray(R.array.giorno_2908);
        else if (strDateDay.equals("3008"))
            strText = getResources().getStringArray(R.array.giorno_3008);
        else if (strDateDay.equals("3108"))
            strText = getResources().getStringArray(R.array.giorno_3108);
        else if (strDateDay.equals("0109"))
            strText = getResources().getStringArray(R.array.giorno_0109);
        else if (strDateDay.equals("0209"))
            strText = getResources().getStringArray(R.array.giorno_0209);
        else if (strDateDay.equals("0309"))
            strText = getResources().getStringArray(R.array.giorno_0309);
        else if (strDateDay.equals("0409"))
            strText = getResources().getStringArray(R.array.giorno_0409);
        else if (strDateDay.equals("0509"))
            strText = getResources().getStringArray(R.array.giorno_0509);
        else if (strDateDay.equals("0609"))
            strText = getResources().getStringArray(R.array.giorno_0609);
        else if (strDateDay.equals("0709"))
            strText = getResources().getStringArray(R.array.giorno_0709);
        else if (strDateDay.equals("0809"))
            strText = getResources().getStringArray(R.array.giorno_0809);
        else if (strDateDay.equals("0909"))
            strText = getResources().getStringArray(R.array.giorno_0909);
        else if (strDateDay.equals("1009"))
            strText = getResources().getStringArray(R.array.giorno_1009);
        else if (strDateDay.equals("1109"))
            strText = getResources().getStringArray(R.array.giorno_1109);
        else if (strDateDay.equals("1209"))
            strText = getResources().getStringArray(R.array.giorno_1209);
        else if (strDateDay.equals("1309"))
            strText = getResources().getStringArray(R.array.giorno_1309);
        else if (strDateDay.equals("1409"))
            strText = getResources().getStringArray(R.array.giorno_1409);
        else if (strDateDay.equals("1509"))
            strText = getResources().getStringArray(R.array.giorno_1509);
        else if (strDateDay.equals("1609"))
            strText = getResources().getStringArray(R.array.giorno_1609);
        else if (strDateDay.equals("1709"))
            strText = getResources().getStringArray(R.array.giorno_1709);
        else if (strDateDay.equals("1809"))
            strText = getResources().getStringArray(R.array.giorno_1809);
        else if (strDateDay.equals("1909"))
            strText = getResources().getStringArray(R.array.giorno_1909);
        else if (strDateDay.equals("2009"))
            strText = getResources().getStringArray(R.array.giorno_2009);
        else if (strDateDay.equals("2109"))
            strText = getResources().getStringArray(R.array.giorno_2109);
        else if (strDateDay.equals("2209"))
            strText = getResources().getStringArray(R.array.giorno_2209);
        else if (strDateDay.equals("2309"))
            strText = getResources().getStringArray(R.array.giorno_2309);
        else if (strDateDay.equals("2409"))
            strText = getResources().getStringArray(R.array.giorno_2409);
        else if (strDateDay.equals("2509"))
            strText = getResources().getStringArray(R.array.giorno_2509);
        else if (strDateDay.equals("2609"))
            strText = getResources().getStringArray(R.array.giorno_2609);
        else if (strDateDay.equals("2709"))
            strText = getResources().getStringArray(R.array.giorno_2709);
        else if (strDateDay.equals("2809"))
            strText = getResources().getStringArray(R.array.giorno_2809);
        else if (strDateDay.equals("2909"))
            strText = getResources().getStringArray(R.array.giorno_2909);
        else if (strDateDay.equals("3009"))
            strText = getResources().getStringArray(R.array.giorno_3009);
        else if (strDateDay.equals("0110"))
            strText = getResources().getStringArray(R.array.giorno_0110);
        else if (strDateDay.equals("0210"))
            strText = getResources().getStringArray(R.array.giorno_0210);
        else if (strDateDay.equals("0310"))
            strText = getResources().getStringArray(R.array.giorno_0310);
        else if (strDateDay.equals("0410"))
            strText = getResources().getStringArray(R.array.giorno_0410);
        else if (strDateDay.equals("0510"))
            strText = getResources().getStringArray(R.array.giorno_0510);
        else if (strDateDay.equals("0610"))
            strText = getResources().getStringArray(R.array.giorno_0610);
        else if (strDateDay.equals("0710"))
            strText = getResources().getStringArray(R.array.giorno_0710);
        else if (strDateDay.equals("0810"))
            strText = getResources().getStringArray(R.array.giorno_0810);
        else if (strDateDay.equals("0910"))
            strText = getResources().getStringArray(R.array.giorno_0910);
        else if (strDateDay.equals("1010"))
            strText = getResources().getStringArray(R.array.giorno_1010);
        else if (strDateDay.equals("1110"))
            strText = getResources().getStringArray(R.array.giorno_1110);
        else if (strDateDay.equals("1210"))
            strText = getResources().getStringArray(R.array.giorno_1210);
        else if (strDateDay.equals("1310"))
            strText = getResources().getStringArray(R.array.giorno_1310);
        else if (strDateDay.equals("1410"))
            strText = getResources().getStringArray(R.array.giorno_1410);
        else if (strDateDay.equals("1510"))
            strText = getResources().getStringArray(R.array.giorno_1510);
        else if (strDateDay.equals("1610"))
            strText = getResources().getStringArray(R.array.giorno_1610);
        else if (strDateDay.equals("1710"))
            strText = getResources().getStringArray(R.array.giorno_1710);
        else if (strDateDay.equals("1810"))
            strText = getResources().getStringArray(R.array.giorno_1810);
        else if (strDateDay.equals("1910"))
            strText = getResources().getStringArray(R.array.giorno_1910);
        else if (strDateDay.equals("2010"))
            strText = getResources().getStringArray(R.array.giorno_2010);
        else if (strDateDay.equals("2110"))
            strText = getResources().getStringArray(R.array.giorno_2110);
        else if (strDateDay.equals("2210"))
            strText = getResources().getStringArray(R.array.giorno_2210);
        else if (strDateDay.equals("2310"))
            strText = getResources().getStringArray(R.array.giorno_2310);
        else if (strDateDay.equals("2410"))
            strText = getResources().getStringArray(R.array.giorno_2410);
        else if (strDateDay.equals("2510"))
            strText = getResources().getStringArray(R.array.giorno_2510);
        else if (strDateDay.equals("2610"))
            strText = getResources().getStringArray(R.array.giorno_2610);
        else if (strDateDay.equals("2710"))
            strText = getResources().getStringArray(R.array.giorno_2710);
        else if (strDateDay.equals("2810"))
            strText = getResources().getStringArray(R.array.giorno_2810);
        else if (strDateDay.equals("2910"))
            strText = getResources().getStringArray(R.array.giorno_2910);
        else if (strDateDay.equals("3010"))
            strText = getResources().getStringArray(R.array.giorno_3010);
        else if (strDateDay.equals("3110"))
            strText = getResources().getStringArray(R.array.giorno_3110);
        else if (strDateDay.equals("0111"))
            strText = getResources().getStringArray(R.array.giorno_0111);
        else if (strDateDay.equals("0211"))
            strText = getResources().getStringArray(R.array.giorno_0211);
        else if (strDateDay.equals("0311"))
            strText = getResources().getStringArray(R.array.giorno_0311);
        else if (strDateDay.equals("0411"))
            strText = getResources().getStringArray(R.array.giorno_0411);
        else if (strDateDay.equals("0511"))
            strText = getResources().getStringArray(R.array.giorno_0511);
        else if (strDateDay.equals("0611"))
            strText = getResources().getStringArray(R.array.giorno_0611);
        else if (strDateDay.equals("0711"))
            strText = getResources().getStringArray(R.array.giorno_0711);
        else if (strDateDay.equals("0811"))
            strText = getResources().getStringArray(R.array.giorno_0811);
        else if (strDateDay.equals("0911"))
            strText = getResources().getStringArray(R.array.giorno_0911);
        else if (strDateDay.equals("1011"))
            strText = getResources().getStringArray(R.array.giorno_1011);
        else if (strDateDay.equals("1111"))
            strText = getResources().getStringArray(R.array.giorno_1111);
        else if (strDateDay.equals("1211"))
            strText = getResources().getStringArray(R.array.giorno_1211);
        else if (strDateDay.equals("1311"))
            strText = getResources().getStringArray(R.array.giorno_1311);
        else if (strDateDay.equals("1411"))
            strText = getResources().getStringArray(R.array.giorno_1411);
        else if (strDateDay.equals("1511"))
            strText = getResources().getStringArray(R.array.giorno_1511);
        else if (strDateDay.equals("1611"))
            strText = getResources().getStringArray(R.array.giorno_1611);
        else if (strDateDay.equals("1711"))
            strText = getResources().getStringArray(R.array.giorno_1711);
        else if (strDateDay.equals("1811"))
            strText = getResources().getStringArray(R.array.giorno_1811);
        else if (strDateDay.equals("1911"))
            strText = getResources().getStringArray(R.array.giorno_1911);
        else if (strDateDay.equals("2011"))
            strText = getResources().getStringArray(R.array.giorno_2011);
        else if (strDateDay.equals("2111"))
            strText = getResources().getStringArray(R.array.giorno_2111);
        else if (strDateDay.equals("2211"))
            strText = getResources().getStringArray(R.array.giorno_2211);
        else if (strDateDay.equals("2311"))
            strText = getResources().getStringArray(R.array.giorno_2311);
        else if (strDateDay.equals("2411"))
            strText = getResources().getStringArray(R.array.giorno_2411);
        else if (strDateDay.equals("2511"))
            strText = getResources().getStringArray(R.array.giorno_2511);
        else if (strDateDay.equals("2611"))
            strText = getResources().getStringArray(R.array.giorno_2611);
        else if (strDateDay.equals("2711"))
            strText = getResources().getStringArray(R.array.giorno_2711);
        else if (strDateDay.equals("2811"))
            strText = getResources().getStringArray(R.array.giorno_2811);
        else if (strDateDay.equals("2911"))
            strText = getResources().getStringArray(R.array.giorno_2911);
        else if (strDateDay.equals("3011"))
            strText = getResources().getStringArray(R.array.giorno_3011);
        else if (strDateDay.equals("0112"))
            strText = getResources().getStringArray(R.array.giorno_0112);
        else if (strDateDay.equals("0212"))
            strText = getResources().getStringArray(R.array.giorno_0212);
        else if (strDateDay.equals("0312"))
            strText = getResources().getStringArray(R.array.giorno_0312);
        else if (strDateDay.equals("0412"))
            strText = getResources().getStringArray(R.array.giorno_0412);
        else if (strDateDay.equals("0512"))
            strText = getResources().getStringArray(R.array.giorno_0512);
        else if (strDateDay.equals("0612"))
            strText = getResources().getStringArray(R.array.giorno_0612);
        else if (strDateDay.equals("0712"))
            strText = getResources().getStringArray(R.array.giorno_0712);
        else if (strDateDay.equals("0812"))
            strText = getResources().getStringArray(R.array.giorno_0812);
        else if (strDateDay.equals("0912"))
            strText = getResources().getStringArray(R.array.giorno_0912);
        else if (strDateDay.equals("1012"))
            strText = getResources().getStringArray(R.array.giorno_1012);
        else if (strDateDay.equals("1112"))
            strText = getResources().getStringArray(R.array.giorno_1112);
        else if (strDateDay.equals("1212"))
            strText = getResources().getStringArray(R.array.giorno_1212);
        else if (strDateDay.equals("1312"))
            strText = getResources().getStringArray(R.array.giorno_1312);
        else if (strDateDay.equals("1412"))
            strText = getResources().getStringArray(R.array.giorno_1412);
        else if (strDateDay.equals("1512"))
            strText = getResources().getStringArray(R.array.giorno_1512);
        else if (strDateDay.equals("1612"))
            strText = getResources().getStringArray(R.array.giorno_1612);
        else if (strDateDay.equals("1712"))
            strText = getResources().getStringArray(R.array.giorno_1712);
        else if (strDateDay.equals("1812"))
            strText = getResources().getStringArray(R.array.giorno_1812);
        else if (strDateDay.equals("1912"))
            strText = getResources().getStringArray(R.array.giorno_1912);
        else if (strDateDay.equals("2012"))
            strText = getResources().getStringArray(R.array.giorno_2012);
        else if (strDateDay.equals("2112"))
            strText = getResources().getStringArray(R.array.giorno_2112);
        else if (strDateDay.equals("2212"))
            strText = getResources().getStringArray(R.array.giorno_2212);
        else if (strDateDay.equals("2312"))
            strText = getResources().getStringArray(R.array.giorno_2312);
        else if (strDateDay.equals("2412"))
            strText = getResources().getStringArray(R.array.giorno_2412);
        else if (strDateDay.equals("2512"))
            strText = getResources().getStringArray(R.array.giorno_2512);
        else if (strDateDay.equals("2612"))
            strText = getResources().getStringArray(R.array.giorno_2612);
        else if (strDateDay.equals("2712"))
            strText = getResources().getStringArray(R.array.giorno_2712);
        else if (strDateDay.equals("2812"))
            strText = getResources().getStringArray(R.array.giorno_2812);
        else if (strDateDay.equals("2912"))
            strText = getResources().getStringArray(R.array.giorno_2912);
        else if (strDateDay.equals("3012"))
            strText = getResources().getStringArray(R.array.giorno_3012);
        else if (strDateDay.equals("3112"))
            strText = getResources().getStringArray(R.array.giorno_3112);

        return strText;
    }

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

        public boolean onTouchMyText(View v, MotionEvent event ) {
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
                    //textViewDayAfterDay.setTextSize(ratio + 10);
                    TextView textView = (TextView)v;
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