package com.example.hoitnote.utils.helpers;

import com.example.hoitnote.utils.Interfaces.IBlueTooth;
import com.example.hoitnote.utils.commuications.DataPackage;

public class BlueToothHelper implements IBlueTooth {
    @Override
    public boolean match() {
        return false;
    }

    @Override
    public boolean sendAllData(DataPackage dataPackage) {
        return false;
    }

    @Override
    public DataPackage receiveAllData() {
        return null;
    }
}
