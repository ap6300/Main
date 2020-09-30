package com.example.projectlayout.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("AN APP THAT MAKES A DIFFERENCE TO YOUR LIFE");
    }

    public LiveData<String> getText() {
        return mText;
    }
}