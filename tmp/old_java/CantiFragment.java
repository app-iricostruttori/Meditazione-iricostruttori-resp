package com.iricostruttori.meditazione.ui.canti;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.iricostruttori.meditazione.R;
import com.iricostruttori.meditazione.databinding.FragmentCantiBinding;

import java.util.StringTokenizer;

// Attenzione a non dimenticare public
// altrimenti non trova il costruttore della classe
public class CantiFragment extends Fragment {

    private FragmentCantiBinding binding;
    private TextView textViewCantoInizialeFinaleSansIta;
    private Spinner dropdown_canti;
    //private TextView textViewCantoFinaleSansIta;
    //private TextView textViewTestataCantoIniziale;
    private Button listenAudioBtn ;
    //private Button listenAudioEndBtn ;
    private MediaPlayer player_start;
    private MediaPlayer player_end;
    private String strCurrentSong = "" ;

    public CantiFragment() {

    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentCantiBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        player_start = MediaPlayer.create(getActivity(), R.raw.inizio_med);
        player_end = MediaPlayer.create(getActivity(), R.raw.fine_med);

        textViewCantoInizialeFinaleSansIta = binding.textCantoInizialeFinaleSansIta;
        listenAudioBtn = binding.listenAudioBtn;

        // In caso di aggiunta dello spinner devo togliere
        // i seguenti e inserire il codice dello spinner
        String[] strSelectSpinnerInizio = getResources().getStringArray(R.array.canto_iniziale);
        String[] strSelectSpinnerFine = getResources().getStringArray(R.array.canto_finale);

        String[] items = new String[]{"Seleziona un canto",
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
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                strCurrentSong = (String) parent.getItemAtPosition(position);
                String strFullCanto = getTextFullCanto(strCurrentSong,
                                                        "Seleziona un canto");


                //textViewCantoInizialeFinaleSansIta.setText(strFullCanto);
                //textViewCantoInizialeFinaleSansIta.setVisibility(View.VISIBLE);










            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        listenAudioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String[] strSelectSpinnerInizio = getResources().getStringArray(R.array.canto_iniziale);
                String[] strSelectSpinnerFine = getResources().getStringArray(R.array.canto_finale);

                if (strCurrentSong.equals(strSelectSpinnerInizio[0])) {
                    player_start.start();

                    try {
                        Thread.sleep(50000);
                    } catch (InterruptedException e) {

                    }
                }
                else if (strCurrentSong.equals(strSelectSpinnerFine[0])) {
                    player_end.start();

                    try {
                        Thread.sleep(25000);
                    } catch (InterruptedException e) {

                    }
                }







            }
        });

        //textViewCantoInizialeFinaleSansIta.setVisibility(View.INVISIBLE);
        return root;
    }

    //}

    @Override
    public void onDestroyView () {
        super.onDestroyView();
        binding = null;
    }

    String getTextFullCanto (String strSelectOnSpinner , String strDefault) {

        System.out.println("[getTextFullCanto] START " + strSelectOnSpinner);

        String strFullCanto = "";

        if ( !strSelectOnSpinner.equals(strDefault)) {
            String stTextCantoSans[] = getTextCantoSanscrito(strSelectOnSpinner);
            String stTextCantoIta[] = getTextCantoItaliano(strSelectOnSpinner);

            int len_sans = stTextCantoSans.length;
            int len_ita = stTextCantoIta.length;

            if  (len_sans == len_ita &&
                 len_sans > 0 )
            {
                int len_max_inizio_sans = 27 ;
                for (int i = 0; i < len_sans; i++) {
                   // DEVO FARE TRIM E PAD SULLA LUNGHEZZA MAX DELLA PRIMA PARTE
                    String strTemp1 = padRight(stTextCantoSans[i],len_max_inizio_sans," ");
                    String strTemp2 = "     " + stTextCantoIta[i];
                    //System.out.println("MIO:" + strTemp1 + ":" + strTemp1.length());
                    System.out.println("MIO:" + strTemp1 + strTemp2);
                    //System.out.println("MIO:" + strTemp2 + ":" + strTemp2.length());
                    String strTemp = strTemp1 + strTemp2 + "\n";
                   //System.out.println("Temp:" + strTemp + ":" + i );
                   strFullCanto = strFullCanto + strTemp;
                   //System.out.println("All:" + strFullCanto + ":" + i );
                }

            }
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


}
