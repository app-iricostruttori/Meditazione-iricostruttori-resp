package com.iricostruttori.meditazione.ui.incontri;

import android.content.res.Resources;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import com.iricostruttori.meditazione.R;

public class IncontriViewModel extends ViewModel {

    private final MutableLiveData<List<String>> mTexts;

    public IncontriViewModel() {
        mTexts = new MutableLiveData<>();
        List<String> texts = new ArrayList<>();
        String strTextMeeting = "";

        //for (int i = 1; i <= 16; i++) {
            //texts.add("This is item # " + i);
       //}<string name="incontro_aosta_1">Valle d’Aosta;Aosta;Mercoledì 19:30</string>
        int id = 1;
        String incontro_biella_1 = "Biella Mercoledì 19:30-";
        texts.add(incontro_biella_1 + id++);

        //String incontro_brillante_1 = "Brillante (TO) Mercoledì 19:30-";
        //texts.add(incontro_brillante_1 + id);

        //String incontro_domodossola_1 = "Domodossola (VB);Mercoledì 19:30-";
        String incontro_torino_1 = "Torino;Mercoledì 19:30-";
        texts.add(incontro_torino_1 + id++);

        String incontro_casalbeltrame_1 = "Casalbeltrame (NO);Mercoledì 19:30-";
        texts.add(incontro_casalbeltrame_1 + id++);

        String incontro_sgiovannicanavese_1 = "S.Giovanni Canavese (TO);Mercoledì 19:30-";
        texts.add(incontro_sgiovannicanavese_1 + id++);

        String incontro_soriso_1 = "Soriso (NO);Mercoledì 19:30-";
        texts.add(incontro_soriso_1 + id++);

        String incontro_sdamiano_1 = "S. Damiano (AT) Mercoledì 19:30-";
        texts.add(incontro_sdamiano_1 + id++);

        String incontro_como_1 = "Como Mercoledì 19:30-";
        texts.add(incontro_como_1 + id++);

        String incontro_gornate_1 = "Gornate Olona (VA);Giovedì 19:30-";
        texts.add(incontro_gornate_1 + id++);

        String incontro_milano_1 = "Milano;Mercoledì 19:30-";
        texts.add(incontro_milano_1 + id++);

        String incontro_castiraga_1 = "Castiraga (LO);Mercoledì 19:30";
        texts.add(incontro_castiraga_1 + id++);

        String incontro_lonato_1 = "Lonato (BS);Mercoledì 19:30-";
        texts.add(incontro_lonato_1 + id++);

        String incontro_castellicaleppio_1 = "Lombardia;Castelli Calepio (BG);Mercoledì 19:30";
        texts.add(incontro_castellicaleppio_1 + id++);

        String incontro_padova_1 = "Padova;Mercoledì 19:30";
        String incontro_rovigo_1 = "Rovigo;Mercoledì 19:30";
        String incontro_verona_1 = "Verona;Mercoledì 19:30";
        String incontro_bologna_1 = "Emilia Romagna;Bologna;Mercoledì 19:30";
        String incontro_scaterina_1 = "Emilia Romagna;S. Caterina (FE);Mercoledì 19:30";
        String incontro_pomposa_1 = "Emilia Romagna;Pomposa Abbazia (FE);Mercoledì 19:30";
        String incontro_genova_1 = "Liguria;Genova;Mercoledì 19:30";
        String incontro_lapspezia_1 = "Liguria;La Spezia;Mercoledì 19:30";
        String incontro_spietrovara_1 = "Liguria;S. Pietro Vara (SP);Mercoledì 19:30";

        String incontro_carrara_1 = "Toscana;Carrara;Mercoledì 19:30";
        String incontro_firenze_1 = "Toscana;Firenze;Mercoledì 19:30";
        String incontro_garfagnana_1 = "Toscana;Garfagnana (LU);Mercoledì 19:30";
        String incontro_livorno_1 = "Toscana;Livorno;Mercoledì 19:30";
        String incontro_lucca_1 = "Toscana;Lucca;Mercoledì 19:30";
        String incontro_pistoia_1 = "Toscana;Pistoia;Mercoledì 19:30";
        String incontro_smariainacone_1 = "Toscana;S. Maria in Acone (FI);Mercoledì 19:30";

        String incontro_ancona_1 = "Marche;Ancona;Mercoledì 19:30";
        String incontro_stroncone_1 = "Umbria;Stroncone (TR);Mercoledì 19:30";
        String incontro_roma_1 = "Lazio;Roma, via Quintili;Mercoledì 19:30" ;
        String incontro_zagarolo_1 = "Lazio;Roma, Zagarolo;Mercoledì 19:30";
        String incontro_aquila_1 = "Abruzzo;L’Aquila ;Mercoledì 19:30";
        String incontro_moscufo_1 = "Abruzzo;Moscufo (PE);Mercoledì 19:30";

        String incontro_montoro_1 = "Campania;Montoro (AV);Mercoledì 19:30";
        String incontro_napoli_1 = "Campania;Napoli;Mercoledì 19:30";
        String incontro_foggia_1 = "Puglia;Foggia;Mercoledì 19:30";
        String incontro_manfredonia_1 = "Puglia;Manfredonia (FG);Mercoledì 19:30";
        String incontro_augusta_1 = "Sicilia;Augusta;Mercoledì 19:30";
        String incontro_messina_1 = "Sicilia;Messina;Mercoledì 19:30";
        String incontro_modica_1 = "Sicilia;Modica (RG);Mercoledì 19:30";
        String incontro_palermo_1 = "Sicilia;Palermo;Mercoledì 19:30";
        String incontro_piedimonte_1 = "Sicilia;Piedimonte Etneo (CT);Mercoledì 19:30";
        String incontro_catania_1 = "Sicilia;Catania;Mercoledì 19:30";
        mTexts.setValue(texts);
    }

    public LiveData<List<String>> getTexts() {
        return mTexts;
    }
}