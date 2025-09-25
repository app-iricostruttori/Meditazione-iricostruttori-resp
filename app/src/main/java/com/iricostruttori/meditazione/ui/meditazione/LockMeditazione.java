package com.iricostruttori.meditazione.ui.meditazione;

import android.os.PowerManager;
import android.view.WindowManager;

public class LockMeditazione {

    final PowerManager powerManager;
    PowerManager.WakeLock mWakeLock;

    public LockMeditazione(PowerManager powerManager) {

        this.powerManager = powerManager;
    }


    public void open_lock() {
        mWakeLock = powerManager.newWakeLock(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                "iricostruttori-app:med-wavelocking");
        mWakeLock.acquire();

        /*
         if (android.os.Build.VERSION.SDK_INT < 13) {
            wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP
                | PowerManager.ON_AFTER_RELEASE, "**StartupReceiver**");
            KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
            final KeyguardManager.KeyguardLock kl;
            if (km != null) {
                kl = km.newKeyguardLock("MyKeyguardLock");
                kl.disableKeyguard();
            }

        } else {wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP
            | PowerManager.ON_AFTER_RELEASE, "**StartupReceiver**");
        }
        wl.acquire();
        * */
    }

    public void close_lock() {

        if ( mWakeLock != null) {
            mWakeLock.release();
            mWakeLock = null;
        }
    }






}
