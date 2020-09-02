package com.example.projectlayout.ui.Alarm;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AlarmViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AlarmViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Alarm fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}