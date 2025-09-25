package com.iricostruttori.meditazione.ui.meditazione;

public enum StatusMeditazione {
        STOP("STOP"),
        PAUSE("PAUSE"),
        RUN_FROM_STOP_INITAL_SONG_RUNNING("RUN_FROM_STOP_INITAL_SONG_RUNNING"),
        RUN_FROM_STOP_INITAL_SONG_NOT_RUNNING("RUN_FROM_STOP_INITAL_SONG_NOT_RUNNING"),
        RUN_FROM_PAUSE("RUN_FROM_PAUSE");

    private String strStatus;

    StatusMeditazione(String strStatus) {
        this.strStatus = strStatus;
    }

    public void setStrStatus(String strStatus) {
        this.strStatus = strStatus;
    }

    public String getStrStatus() {
        return strStatus;
    }
}

    /*
    //private Meditazione status;
    //public enum Meditazione {
    private Meditazione getStatus() {
        return status;
    }
    public boolean isStopped() {
        if (getStatus() == Meditazione.STOP) {
            return true;
        }
        return false;
    }

    public boolean isPaused() {
        if (getStatus() == Meditazione.PAUSE) {
            return true;
        }
        return false;
    }

    public boolean isRunFromStop() {
        if (getStatus() == Meditazione.RUN_FROM_STOP) {
            return true;
        }
        return false;
    }

    public boolean isRunFromPause() {
        if (getStatus() == Meditazione.RUN_FROM_PAUSE) {
            return true;
        }
        return false;
    }
}
*/