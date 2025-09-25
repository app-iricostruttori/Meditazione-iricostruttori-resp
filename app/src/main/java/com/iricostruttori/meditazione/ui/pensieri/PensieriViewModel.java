package com.iricostruttori.meditazione.ui.pensieri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PensieriViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public PensieriViewModel() {
        mText = new MutableLiveData<>();
        //java.util.Date objDate = new java.util.Date();
        //String strDateDay = PensieriUtility.dateToString(objDate, "dd-MM");
        mText.setValue("Pensiero di oggi " );
    }

    public LiveData<String> getText() {
        return mText;
    }
}