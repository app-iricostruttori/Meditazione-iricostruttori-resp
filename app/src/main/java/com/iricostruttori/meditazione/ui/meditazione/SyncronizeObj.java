package com.iricostruttori.meditazione.ui.meditazione;

// https://stackoverflow.com/questions/886722/how-to-use-wait-and-notify-in-java-without-illegalmonitorstateexception/886799#886799
public class SyncronizeObj extends Thread {
    public void doWait(long time){
        synchronized(this){
            try {
                this.wait(time);
            } catch(InterruptedException e) {
            }
        }
    }

    public void doNotify() {
        synchronized(this) {
            this.notify();
        }
    }

    public void doWait() {
        synchronized(this){
            try {
                this.wait();
            } catch(InterruptedException e) {
            }
        }
    }
}