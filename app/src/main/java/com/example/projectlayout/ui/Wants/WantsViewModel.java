package com.example.projectlayout.ui.Wants;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WantsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public WantsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}