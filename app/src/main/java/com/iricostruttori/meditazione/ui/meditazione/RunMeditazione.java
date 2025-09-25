package com.iricostruttori.meditazione.ui.meditazione;

class RunMeditazione {

    // Tempo totale rimanente in Millisecondi
    // tempo canto iniziale + tempo meditazione
    public long timeLeftInMs ;

    // Tempo di meditazione rimanente
    public long timeLeftAuxInMs ;

    // Tempo Canto Iniziale in millisecondi
    public long timeInitialSongInMs ;

    // Tempo Totale Canto Iniziale e Meditazione in millisecondi
    public long timeInitialSongAndMedInMs;

    // Tempo in minuti
    public int timeInMinutes ;

    // Stato della meditazione (StatusMeditazione)
    public  String strStatus ;

    public RunMeditazione() {

        timeLeftInMs = 0L ; //default value
        timeLeftAuxInMs = 0L ;
        timeInitialSongInMs = 50000L;
        timeInitialSongAndMedInMs = 0L ;
        timeInMinutes = 30 ;

        strStatus = "";

    }

}
