package com.iricostruttori.meditazione.ui.meditazione;

import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

//https://www.digitalocean.com/community/tutorials/java-timer-timertask-example
// https://stackoverflow.com/questions/11550789/android-simple-how-to-stop-mediaplayer-after-given-time
class SleepTaskMeditazione  {

    /* final MediaPlayer audio ;
    public SleepTaskMeditazione(MediaPlayer audio) {

        this.audio = audio;
    }


    Handler handlerWait = new Handler(){
        @Override
        public void handleMessage(Message msg){
            audio.stop();
        }
    };

    @Override
    public void run() {
        handlerWait.sendEmptyMessage(0);
    }
    */

    //final long numTimeToSleep ;
    //private static boolean flagRun = true;
    /*
    SleepTaskMeditazione(long numTimeToSleep) {
        this.numTimeToSleep = numTimeToSleep;
    }


    @Override
    public void run() {
        System.out.println("Timer task started at:"+new Date());
        completeTask();
        System.out.println("Timer task finished at:"+new Date());
    }

    private void completeTask() {
        if (flagRun) {
            try {
                //assuming it takes numTime secs to complete the task
                Thread.sleep(numTimeToSleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else {

        }

    }
    */
    boolean boolRun = true;
    SyncronizeObj syncronizeObj;

    private void completeTask() {

            try {
                //assuming it takes numTime secs to complete the task
                //Thread.sleep(50000);
                syncronizeObj.doWait(50000L);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    private void terminateTask() {

        try {
            //assuming it takes numTime secs to complete the task
            //Thread.sleep(50000);
            syncronizeObj.doNotify();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    public void start_task() {
        syncronizeObj = new SyncronizeObj();
        long delay;
        final Timer timer = new Timer();

        final TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if(boolRun) {
                    //Toolkit.getDefaultToolkit().beep();
                    completeTask();
                    System.out.println("Timer task started at:"+new Date());
                } else {
                    System.out.println("Timer task finished at:"+new Date());
                    terminateTask();
                    timer.cancel();
                    timer.purge();
                }
            }
        };

        //timer.schedule(task,50000);
        timer.schedule(task,0);

        // set run to false here to stop the timer.
        boolRun = false;
    }
}
