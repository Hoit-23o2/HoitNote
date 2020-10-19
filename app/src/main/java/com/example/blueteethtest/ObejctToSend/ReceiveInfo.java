package com.example.blueteethtest.ObejctToSend;

import com.example.blueteethtest.BluetoothActivity;

import java.io.Serializable;
import java.util.Calendar;

public class ReceiveInfo implements Serializable {
    private String timeString;
    private String message;
    private String thisDeviceName;
    public ReceiveInfo(String message) {
        this.message = message;
        thisDeviceName = BluetoothActivity.getBluetoothAdapter().getName();
        Calendar calendar = Calendar.getInstance();
        this.timeString = calendar.get(Calendar.YEAR) +"/"
                        + calendar.get(Calendar.MONTH) +"/"
                        + calendar.get(Calendar.DATE) + "/"
                        + calendar.get(Calendar.HOUR_OF_DAY) + ":"
                        + calendar.get(Calendar.MINUTE) + ":"
                        +calendar.get(Calendar.SECOND);
    }

    public String getTimeString() {
        return timeString;
    }

    public String getMessage() {
        return message;
    }

    public String getThisDeviceName() {
        return thisDeviceName;
    }
}
