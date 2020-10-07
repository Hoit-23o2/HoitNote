package com.example.hoitnote.utils.Interfaces;

import com.example.hoitnote.utils.commuications.DataPackage;

public interface IBlueTooth {
    /*
    * 蓝牙配对
    * */
    boolean match();



    boolean sendAllData(DataPackage dataPackage);

    DataPackage receiveAllData();
}
