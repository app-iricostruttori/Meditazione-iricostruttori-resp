package com.iricostruttori.meditazione.ui.meditazione;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MeditazioneViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public MeditazioneViewModel() {
        mText = new MutableLiveData<>();
        //mText.setValue("This is meditazione fragment");
        mText.setValue("");
    }

    public LiveData<String> getText() {
        return mText;
    }
}