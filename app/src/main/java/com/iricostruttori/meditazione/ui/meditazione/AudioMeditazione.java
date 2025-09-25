package com.iricostruttori.meditazione.ui.meditazione;

import android.media.MediaPlayer;

public class AudioMeditazione {
    final String strAudioName;
    final MediaPlayer player;

    public AudioMeditazione(String strAudioName, MediaPlayer player) {
        this.strAudioName = strAudioName;
        this.player = player;
    }

    public String getStrAudioName() {
        return strAudioName;
    }
}

/*
class AudioPlayer : MediaPlayer(), MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnInfoListener, MediaPlayer.OnPreparedListener {

    enum class AudioPlayerState {
        IDLE,
        INITIALIZED,
        PREPARING,
        PREPARED,
        STARTED,
        STOPPED,
        PAUSED,
        PLAYBACK_COMPLETED,
        ERROR,
        END
    }

    var playerState: AudioPlayerState? = null
        set(value) {
            Log.d(javaClass.name, "Setting new player state $value")
            field = value
        }

    init {
        playerState = AudioPlayerState.IDLE
        setOnPreparedListener(this)
        setOnBufferingUpdateListener(this)
        setOnCompletionListener(this)
        setOnErrorListener(this)
        setOnInfoListener(this)
    }

    override fun setDataSource(context: Context, uri: Uri) {
        if (playerState != AudioPlayerState.IDLE) Log.e(javaClass.name, "Trying to set data source on player state $playerState")
        super.setDataSource(context, uri)
        playerState = AudioPlayerState.INITIALIZED
    }

    override fun prepare() {
        if (playerState != AudioPlayerState.INITIALIZED && playerState != AudioPlayerState.STOPPED) Log.e(javaClass.name, "Trying to prepare on player state $playerState")
        playerState = try {
            super.prepare()
            AudioPlayerState.PREPARED
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(javaClass.name, "Exception on audio player prepare ${e.message} with state $playerState")
            AudioPlayerState.ERROR
        }
    }

    override fun prepareAsync() {
        playerState = try {
            super.prepareAsync()
            AudioPlayerState.PREPARING
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(javaClass.name, "Exception on audio player prepare async ${e.message} with state $playerState")
            AudioPlayerState.ERROR
        }
    }

    override fun start() {
        if (playerState != AudioPlayerState.PREPARED &&
            playerState != AudioPlayerState.STARTED &&
            playerState != AudioPlayerState.PAUSED &&
            playerState != AudioPlayerState.PLAYBACK_COMPLETED
        ) Log.e(javaClass.name, "Trying to start on player state $playerState")
        playerState = try {
            super.start()
            AudioPlayerState.STARTED
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(javaClass.name, "Exception on audio player start ${e.message} with state $playerState")
            AudioPlayerState.ERROR
        }
    }

    override fun stop() {
        if (playerState != AudioPlayerState.PREPARED &&
            playerState != AudioPlayerState.STARTED &&
            playerState != AudioPlayerState.STOPPED &&
            playerState != AudioPlayerState.PAUSED &&
            playerState != AudioPlayerState.PLAYBACK_COMPLETED
        ) Log.e(javaClass.name, "Trying to stop on player state $playerState")
        playerState = try {
            super.stop()
            AudioPlayerState.STOPPED
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(javaClass.name, "Exception on audio player stop ${e.message} with state $playerState")
            AudioPlayerState.ERROR
        }
    }

    override fun pause() {
        if (playerState != AudioPlayerState.STARTED &&
            playerState != AudioPlayerState.PAUSED &&
            playerState != AudioPlayerState.PLAYBACK_COMPLETED
        ) Log.e(javaClass.name, "Trying to pause on player state $playerState")
        playerState = try {
            super.pause()
            AudioPlayerState.PAUSED
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(javaClass.name, "Exception on audio player pause ${e.message} with state $playerState")
            AudioPlayerState.ERROR
        }
    }

    override fun seekTo(msec: Int) {
        if (playerState != AudioPlayerState.PREPARED &&
            playerState != AudioPlayerState.STARTED &&
            playerState != AudioPlayerState.PAUSED &&
            playerState != AudioPlayerState.PLAYBACK_COMPLETED
        ) Log.e(javaClass.name, "Trying to seek to on player state $playerState")
        try {
            super.seekTo(msec)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(javaClass.name, "Exception on audio player seek to ${e.message} with state $playerState")
            playerState = AudioPlayerState.ERROR
        }
    }

    override fun seekTo(msec: Long, mode: Int) {
        if (playerState != AudioPlayerState.PREPARED &&
            playerState != AudioPlayerState.STARTED &&
            playerState != AudioPlayerState.PAUSED &&
            playerState != AudioPlayerState.PLAYBACK_COMPLETED
        ) Log.e(javaClass.name, "Trying to seek to with mode on player state $playerState")
        try {
            super.seekTo(msec, mode)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(javaClass.name, "Exception on audio player seek to with mode ${e.message} with state $playerState")
            playerState = AudioPlayerState.ERROR
        }
    }

    override fun getCurrentPosition(): Int {
        if (playerState == null) return 0
        if (playerState != AudioPlayerState.IDLE &&
            playerState != AudioPlayerState.INITIALIZED &&
            playerState != AudioPlayerState.PREPARED &&
            playerState != AudioPlayerState.STARTED &&
            playerState != AudioPlayerState.PAUSED &&
            playerState != AudioPlayerState.STOPPED &&
            playerState != AudioPlayerState.PLAYBACK_COMPLETED
        ) Log.e(javaClass.name, "Trying to get current position on player state $playerState")
        try {
            return super.getCurrentPosition()
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(javaClass.name, "Exception on audio player current position ${e.message} with state $playerState")
            playerState = AudioPlayerState.ERROR
        }
        return 0
    }

    override fun isPlaying(): Boolean {
        if (playerState == null) return false
        if (playerState != AudioPlayerState.IDLE &&
            playerState != AudioPlayerState.INITIALIZED &&
            playerState != AudioPlayerState.PREPARED &&
            playerState != AudioPlayerState.STARTED &&
            playerState != AudioPlayerState.PAUSED &&
            playerState != AudioPlayerState.STOPPED &&
            playerState != AudioPlayerState.PLAYBACK_COMPLETED
        ) Log.e(javaClass.name, "Trying to get is playing on player state $playerState")
        try {
            return super.isPlaying()
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(javaClass.name, "Exception on audio player is playing ${e.message} with state $playerState")
            playerState = AudioPlayerState.ERROR
        }
        return false
    }

    override fun reset() {
        playerState = try {
            super.reset()
            AudioPlayerState.IDLE
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(javaClass.name, "Exception on audio player reset ${e.message} with state $playerState")
            AudioPlayerState.ERROR
        }
    }

    override fun release() {
        playerState = try {
            super.release()
            AudioPlayerState.END
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(javaClass.name, "Exception on audio player release ${e.message} with state $playerState")
            AudioPlayerState.ERROR
        }
    }

    override fun onPrepared(mp: MediaPlayer?) {
        if (playerState != AudioPlayerState.STARTED) {
            playerState = AudioPlayerState.PREPARED
        }
    }

    override fun onInfo(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        return false
    }

    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        playerState = AudioPlayerState.ERROR
        return false
    }

    override fun onCompletion(mp: MediaPlayer?) {
        playerState = AudioPlayerState.PLAYBACK_COMPLETED
        seekTo(0)
    }

    override fun onBufferingUpdate(mp: MediaPlayer?, percent: Int) {

    }
}

*/