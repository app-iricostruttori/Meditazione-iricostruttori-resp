package com.iricostruttori.meditazione.ui.incontri;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.iricostruttori.meditazione.R;
//import com.iricostruttori.meditazione.databinding.FragmentTransformBinding;
import com.iricostruttori.meditazione.databinding.FragmentIncontriBinding;
import com.iricostruttori.meditazione.databinding.ItemTransformBinding;

import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Fragment that demonstrates a responsive layout pattern where the format of the content
 * transforms depending on the size of the screen. Specifically this Fragment shows items in
 * the [RecyclerView] using LinearLayoutManager in a small screen
 * and shows items using GridLayoutManager in a large screen.
 */
public class IncontriFragment extends Fragment {

    //private FragmentTransformBinding binding;
    private FragmentIncontriBinding binding;
    private Spinner dropdown_incontri;

    private TableRow trText;
    private TextView textViewMeeting;
    private TextView textViewMapsLink;
    private TouchTextView touchTextView = new TouchTextView();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             @Nullable  Bundle savedInstanceState) {

        //super.onCreate(savedInstanceState);
        // alla pressione di un pulsante salvo cosi
        //getParentFragmentManager().setFragmentResult("",savedInstanceState);

        //IncontriViewModel incontriViewModel =
        //        new ViewModelProvider(this).get(IncontriViewModel.class);
        getActivity().setTheme(R.style.Theme_Meditazioneiricostruttoriresp);
        //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();

        binding = FragmentIncontriBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        trText = binding.rowText;

        textViewMeeting = binding.textIncontroContent;
        textViewMapsLink = binding.textIncontroMapsLink;

        // Ensure these views are not null before trying to set their visibility or properties.
        if (textViewMeeting != null && !textViewMeeting.equals("")) { // Add null check
            //textViewMeeting.setVisibility(View.INVISIBLE); // This line caused the error
            textViewMeeting.setTextSize(touchTextView.ratio + 10);
            //textViewMeeting.setText("Giorno e Orario dell'incontro");
        } else {
            Log.e("IncontriFragment", "textViewMeeting is null!");
        }

        if (textViewMapsLink != null && !textViewMapsLink.equals("") ) { // Add null check
            //textViewMapsLink.setVisibility(View.INVISIBLE); // This line caused the error
            textViewMapsLink.setTextSize(touchTextView.ratio + 10);
            //textViewMapsLink.setText("Maps Link");
        } else {
            Log.e("IncontriFragment", "textViewMapsLink is null!");
        }


        //create a list of items for the spinner.
        //String[] items = new String[]{"Valle d'Aosta", "Piemonte", "Lombardia"};
        String[] strRegioneValdaosta = getResources().getStringArray(R.array.regione_valdaosta);
        String[] strRegionePiemonte = getResources().getStringArray(R.array.regione_piemonte);
        String[] strRegioneLombardia = getResources().getStringArray(R.array.regione_lombardia);
        String[] strRegioneVeneto = getResources().getStringArray(R.array.regione_veneto);
        String[] strRegioneEmiliaRomagna = getResources().getStringArray(R.array.regione_emiliaromagna);
        String[] strRegioneLiguria = getResources().getStringArray(R.array.regione_liguria);
        String[] strRegioneToscana = getResources().getStringArray(R.array.regione_toscana);
        String[] strRegioneMarche = getResources().getStringArray(R.array.regione_marche);
        String[] strRegioneUmbria = getResources().getStringArray(R.array.regione_umbria);
        String[] strRegioneLazio = getResources().getStringArray(R.array.regione_lazio);
        String[] strRegioneAbruzzo = getResources().getStringArray(R.array.regione_abruzzo);
        String[] strRegioneCampania = getResources().getStringArray(R.array.regione_campania);
        String[] strRegionePuglia = getResources().getStringArray(R.array.regione_puglia);
        String[] strRegioneSicilia = getResources().getStringArray(R.array.regione_sicilia);

        String[] items = new String[]{"Seleziona una regione",
                                      strRegioneValdaosta[0],
                                      strRegionePiemonte[0],
                                      strRegioneLombardia[0],
                                      strRegioneVeneto[0],
                                      strRegioneEmiliaRomagna[0],
                                      strRegioneLiguria[0],
                                      strRegioneToscana[0],
                                      strRegioneMarche[0],
                                      strRegioneUmbria[0],
                                      strRegioneLazio[0],
                                      strRegioneAbruzzo[0],
                                      strRegioneCampania[0],
                                      strRegionePuglia[0],
                                      strRegioneSicilia[0]};

        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        //dropdown = getActivity().findViewById(R.id.spinner_incontri);
        dropdown_incontri = root.findViewById(R.id.spinner_incontri);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, items);
        //set the spinners adapter to the previously created one.
        dropdown_incontri.setAdapter(adapter);


        dropdown_incontri.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {

                String strCurrentMeeting = (String) parent.getItemAtPosition(position);
                // String stTextIncontro[] = getTextIncontro((String) parent.getItemAtPosition(position));
                String stTextIncontro[] = getTextIncontro(strCurrentMeeting);

                String strFullIncontro = "" ;
                String strFullMapsLink = "" ;

                if (stTextIncontro.length > 0 )
                {
                    String strTempMeeting = "";
                    String strTempLink = "";
                    String strHtmlLink = "";
                    for (int i = 0; i < stTextIncontro.length; i++)
                    {
                        StringTokenizer stToken = new StringTokenizer(stTextIncontro[i], ";");
                        //System.out.println(" ciclo strTemp  " + stToken.countTokens());
                        int count = stToken.countTokens();
                        if (count > 0  )
                        {
                            strTempMeeting = stToken.nextToken();
                            //if (count == 2 && strTempMeeting.equalsIgnoreCase("Maps"))
                            if (strTempMeeting.equalsIgnoreCase("Maps"))
                            {
                                // Se c'è il link devono esserci due token
                                if (count == 2 ) {
                                   strTempLink = stToken.nextToken();

                                   strHtmlLink = "<a href='" + strTempLink + "'>  Maps  </a>";

                                   System.out.println(" ciclo strTempLink  " + strTempLink);

                                   strFullMapsLink = strFullMapsLink + strHtmlLink + "<br><br>";


                                }
                                else
                                    strFullMapsLink = strFullMapsLink + "<br><br>";

                                System.out.println(" ciclo  link dopo strFullMapsLink  " + strFullMapsLink);


                            }
                            else {

                                System.out.println(" ciclo giorno prima strFullIncontro  " + strFullIncontro);
                                System.out.println(" ciclo strTempMeeting  " + strTempMeeting);

                                strFullIncontro = strFullIncontro + strTempMeeting + "\n\n";
                                // strFullIncontro = strFullIncontro + strTempMeeting;
                                System.out.println(" ciclo dopo prima strFullIncontro  " + strFullIncontro);


                                //String strTempMeeting = "<string name=\"agree_terms_privacy\">By continuing, you agree to our &lt;a href=http://link1/terms>Terms of Use&lt;/a> </string>";

                                System.out.println(" ciclo  " + stTextIncontro[i]);

                            }
                        }

                    }

                    Log.d("IncontriFragment"," strFullIncontro  " + strFullIncontro);
                    Log.d("IncontriFragment"," strFullMapsLink  " + strFullMapsLink);

                    if (textViewMeeting != null && !textViewMeeting.equals("")) {
                        textViewMeeting.setHighlightColor(Color.BLACK);
                        textViewMeeting.setHintTextColor(Color.BLACK);
                        // textViewMeeting.setText(Html.fromHtml(strFullIncontro));
                        textViewMeeting.setText(strFullIncontro);
                        textViewMeeting.setVisibility(View.VISIBLE);
                    } else {
                        Log.e("IncontriFragment.onItemSelected", "textViewMeeting is null!");
                    }

                    if (textViewMapsLink != null && !textViewMapsLink.equals("") ) {
                        textViewMapsLink.setHighlightColor(Color.BLACK);
                        textViewMapsLink.setHintTextColor(Color.BLACK);
                        textViewMapsLink.setClickable(true);         
                        textViewMapsLink.setVisibility(View.VISIBLE);
                        
                        
                        //textViewMapsLink.setMovementMethod(LinkMovementMethod.getInstance());
                        //textViewMapsLink.setText(strFullMapsLink);
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            textViewMapsLink.setText(Html.fromHtml(strFullMapsLink, Html.FROM_HTML_MODE_COMPACT));
                        } else {
                            textViewMapsLink.setText(Html.fromHtml(strFullMapsLink));
                        }
                        textViewMapsLink.setMovementMethod(LinkMovementMethod.getInstance()); // Rende i link cliccabili

                    }
                    else {
                        Log.e("IncontriFragment.onItemSelected", "textViewMapsLink is null!");
                    }



                }

                if (strCurrentMeeting.equals("Seleziona una regione")) {
                    textViewMeeting.setVisibility(View.INVISIBLE);
                    textViewMapsLink.setVisibility(View.INVISIBLE);
                    trText.setVisibility(View.INVISIBLE);
                } else {
                    textViewMeeting.setVisibility(View.VISIBLE);
                    textViewMapsLink.setVisibility(View.VISIBLE);
                    trText.setVisibility(View.VISIBLE);
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        textViewMeeting.setVisibility(View.INVISIBLE);
        textViewMapsLink.setVisibility(View.INVISIBLE);
        trText.setVisibility(View.INVISIBLE);

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


    private static class TransformAdapter extends ListAdapter<String, TransformViewHolder> {

        private final List<Integer> listTextIncontri = Arrays.asList() ;
        //private final List<Integer> listLinkIncontri = Arrays.asList() ;

        /*
        private final List<Integer> drawables = Arrays.asList(
                R.drawable.sindone,
                R.drawable.sindone,
                R.drawable.sindone,
                R.drawable.sindone,
                R.drawable.sindone,
                R.drawable.sindone,
                R.drawable.sindone,
                R.drawable.sindone,
                R.drawable.sindone,
                R.drawable.sindone,
                R.drawable.sindone,
                R.drawable.sindone,
                R.drawable.sindone,
                R.drawable.sindone,
                R.drawable.sindone,
                R.drawable.sindone);
        */


        protected TransformAdapter() {
            super(new DiffUtil.ItemCallback<String>() {
                @Override
                public boolean areItemsTheSame(@NonNull String oldItem, @NonNull String newItem) {
                    return oldItem.equals(newItem);
                }

                @Override
                public boolean areContentsTheSame(@NonNull String oldItem, @NonNull String newItem) {
                    return oldItem.equals(newItem);
                }
            });
        }

        @NonNull
        @Override
        public TransformViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemTransformBinding binding = ItemTransformBinding.inflate(LayoutInflater.from(parent.getContext()));
            return new TransformViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull TransformViewHolder holder, int position) {
            holder.textView.setText(getItem(position));

            /* holder.imageView.setImageDrawable(
                    ResourcesCompat.getDrawable(holder.imageView.getResources(),
                            drawables.get(position),
                            null));
            */
        }
    }

    private static class TransformViewHolder extends RecyclerView.ViewHolder {

        //private final ImageView imageView;
        private final TextView textView;
        //private final TextView linkView;

        public TransformViewHolder(ItemTransformBinding binding) {
            super(binding.getRoot());
            //imageView = binding.imageViewItemTransform;
            textView = binding.textViewItemTransform;
            //linkView = binding.linkViewItemTransform;
        }
    }

    String[] getTextIncontro(String strRegione ) {

        String[] strText = null;
        System.out.println("sel strRegione :" + strRegione + ":");

        String[] strRegioneValdaosta = getResources().getStringArray(R.array.regione_valdaosta);
        String[] strRegionePiemonte = getResources().getStringArray(R.array.regione_piemonte);
        String[] strRegioneLombardia = getResources().getStringArray(R.array.regione_lombardia);
        String[] strRegioneVeneto = getResources().getStringArray(R.array.regione_veneto);
        String[] strRegioneEmiliaRomagna = getResources().getStringArray(R.array.regione_emiliaromagna);
        String[] strRegioneLiguria = getResources().getStringArray(R.array.regione_liguria);
        String[] strRegioneToscana = getResources().getStringArray(R.array.regione_toscana);
        String[] strRegioneMarche = getResources().getStringArray(R.array.regione_marche);
        String[] strRegioneUmbria = getResources().getStringArray(R.array.regione_umbria);
        String[] strRegioneLazio = getResources().getStringArray(R.array.regione_lazio);
        String[] strRegioneAbruzzo = getResources().getStringArray(R.array.regione_abruzzo);
        String[] strRegioneCampania = getResources().getStringArray(R.array.regione_campania);
        String[] strRegionePuglia = getResources().getStringArray(R.array.regione_puglia);
        String[] strRegioneSicilia = getResources().getStringArray(R.array.regione_sicilia);

        if ( strRegione.equals(strRegioneValdaosta[0]))
        {
            System.out.println("set Val d'aosta ");
            strText = getResources().getStringArray(R.array.incontri_valdaosta);
        }
        else if ( strRegione.equals(strRegionePiemonte[0]))
        {
            System.out.println("set Piemonte");
            strText = getResources().getStringArray(R.array.incontri_piemonte) ;
        }
        else if ( strRegione.equals(strRegioneLombardia[0]))
        {
            System.out.println("set lombardia");
            strText = getResources().getStringArray(R.array.incontri_lombardia);
        }
        else if ( strRegione.equals(strRegioneVeneto[0]))
        {
            System.out.println("set Veneto");
            strText = getResources().getStringArray(R.array.incontri_veneto);
        }
        else if ( strRegione.equals(strRegioneEmiliaRomagna[0]))
        {
            System.out.println("set emilia romagna");
            strText = getResources().getStringArray(R.array.incontri_emiliaromagna);
        }
        else if ( strRegione.equals(strRegioneLiguria[0]))
        {
            System.out.println("set liguria");
            strText = getResources().getStringArray(R.array.incontri_liguria);
        }
        else if ( strRegione.equals(strRegioneToscana[0]))
        {
            System.out.println("set Toscana");
            strText = getResources().getStringArray(R.array.incontri_toscana);
        }
        /*
        else if ( strRegione.equals(strRegioneMarche[0]))
        {
            System.out.println("set Marche");
            strText = getResources().getStringArray(R.array.incontri_marche);
        }
        */
        else if ( strRegione.equals(strRegioneUmbria[0]))
        {
            System.out.println("set Umbria");
            strText = getResources().getStringArray(R.array.incontri_umbria);
        }
        else if ( strRegione.equals(strRegioneLazio[0]))
        {
            System.out.println("set lazio");
            strText = getResources().getStringArray(R.array.incontri_lazio);
        }
        else if ( strRegione.equals(strRegioneAbruzzo[0]))
        {
            System.out.println("set abruzzo");
            strText = getResources().getStringArray(R.array.incontri_abruzzo);
        }
        else if ( strRegione.equals(strRegioneCampania[0]))
        {
            System.out.println("set campania");
            strText = getResources().getStringArray(R.array.incontri_campania);
        }
        else if ( strRegione.equals(strRegionePuglia[0]))
        {
            System.out.println("set puglia");
            strText = getResources().getStringArray(R.array.incontri_puglia);
        }
        else if ( strRegione.equals(strRegioneSicilia[0]))
        {
            System.out.println("set Sicilia");
            strText = getResources().getStringArray(R.array.incontri_sicilia);
        }
        else
            strText =   new String[]{" "};

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

        public boolean onTouchMyText(View v,MotionEvent event ) {
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
                    //textViewMeeting.setTextSize(ratio + 10);
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