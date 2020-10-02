package com.example.hoitnote.viewmodels;

import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;

import com.example.hoitnote.BR;
import com.example.hoitnote.models.Sample;

public class SampleViewModel extends BaseViewModel {

    public SampleViewModel(){

    }
    /*
    * 实现方式一，利用NotifyPropertyChanged
    * */
    private Sample sample;
    @Bindable
    public Sample getSample() {
        return sample;
    }

    public void setSample(Sample sample) {
        this.sample = sample;
        notifyPropertyChanged(BR.sample);
    }

    /*
    * 实现方式二，利用ObservableField
    * */
    public ObservableField<Sample> sample2 = new ObservableField<>();

}
