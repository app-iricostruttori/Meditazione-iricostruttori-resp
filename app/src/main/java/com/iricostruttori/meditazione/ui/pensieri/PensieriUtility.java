package com.iricostruttori.meditazione.ui.pensieri;

import java.text.SimpleDateFormat;

class PensieriUtility {

    public static String dateToString(java.util.Date objInputDate,
                               String strFormatDate)
    {

        SimpleDateFormat parser = new SimpleDateFormat(strFormatDate);

        try
        {
            return parser.format(objInputDate);
        }
        catch (Exception e)
        {
            return " ";
        }
    }

    public static int getNumDayMonth(String strDateMonthSpinner) {

        int numDayMonth = 0 ;

        if (strDateMonthSpinner.equals(PensieriConstants.strDescMese_01))
            numDayMonth = 31;
        else if (strDateMonthSpinner.equals(PensieriConstants.strDescMese_02))
            //numDayMonth = 29;
            numDayMonth = 28; // giorno dopo giorno non ha il 29 febbraio
        else if (strDateMonthSpinner.equals(PensieriConstants.strDescMese_03))
            numDayMonth = 31;
        else if (strDateMonthSpinner.equals(PensieriConstants.strDescMese_04))
            numDayMonth = 30;
        else if (strDateMonthSpinner.equals(PensieriConstants.strDescMese_05))
            numDayMonth = 31;
        else if (strDateMonthSpinner.equals(PensieriConstants.strDescMese_06))
            numDayMonth = 30;
        else if (strDateMonthSpinner.equals(PensieriConstants.strDescMese_07))
            numDayMonth = 31;
        else if (strDateMonthSpinner.equals(PensieriConstants.strDescMese_08))
            numDayMonth = 31;
        else if (strDateMonthSpinner.equals(PensieriConstants.strDescMese_09))
            numDayMonth = 30;
        else if (strDateMonthSpinner.equals(PensieriConstants.strDescMese_10))
            numDayMonth = 31;
        else if (strDateMonthSpinner.equals(PensieriConstants.strDescMese_11))
            numDayMonth = 30;
        else // dicembre oppure inglese da vedere il caso
            numDayMonth = 31;

        return numDayMonth;

    }
    public static String[] getItemsPensieriGiorno (String strDateDaySpinner ,
                                                   String strDateMonthSpinner ) {


        System.out.println("[getItemsPensieriGiorno] START strDateDaySpinner " + strDateDaySpinner);
        System.out.println("[getItemsPensieriGiorno] strDateMonthSpinner " + strDateMonthSpinner);

        // Determino il numero di giorni del mese
        int numDayMonth = getNumDayMonth(strDateMonthSpinner)+1 ;
        //String[] items_pensieri_giorno = new String[]{strDateDaySpinner} ;
        String[] items_pensieri_giorno = new String[numDayMonth];
        String strTempDescDay = "";


        for (int i=0 ; i<numDayMonth; i++) {
                if (i==0)
                    strTempDescDay = strDateDaySpinner;
                else if ( i<10)
                    strTempDescDay = "0" + String.valueOf(i) + " " + strDateMonthSpinner;
                else
                    strTempDescDay = String.valueOf(i) + " " + strDateMonthSpinner;

                System.out.println("[getItemsPensieriGiorno] MIO " + strTempDescDay + "-"  + i);
                items_pensieri_giorno[i] = strTempDescDay;
            }


        return items_pensieri_giorno;

    }

}
