package com.example.hoitnote.models;

import androidx.databinding.ObservableField;

import java.util.Observable;

public class Sample{
    private String sampleText;


    public Sample(String sampleText){
        this.sampleText = sampleText;
    }

    public String getSampleText() {
        return sampleText;
    }

    public void setSampleText(String sampleText) {
        this.sampleText = sampleText;
    }
}
