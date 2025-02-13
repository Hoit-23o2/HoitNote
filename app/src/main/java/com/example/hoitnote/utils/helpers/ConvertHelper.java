package com.example.hoitnote.utils.helpers;

import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;

import com.example.hoitnote.utils.constants.Constants;
import com.example.hoitnote.utils.enums.ActionType;

public class ConvertHelper {
    public static String bluetoothDevice2Icon(BluetoothDevice bluetoothDevice){
        /*public static final int AUDIO_VIDEO_CAMCORDER = 1076;
        public static final int AUDIO_VIDEO_CAR_AUDIO = 1056;
        public static final int AUDIO_VIDEO_HANDSFREE = 1032;
        public static final int AUDIO_VIDEO_HEADPHONES = 1048;
        public static final int AUDIO_VIDEO_HIFI_AUDIO = 1064;
        public static final int AUDIO_VIDEO_LOUDSPEAKER = 1044;
        public static final int AUDIO_VIDEO_MICROPHONE = 1040;
        public static final int AUDIO_VIDEO_PORTABLE_AUDIO = 1052;
        public static final int AUDIO_VIDEO_SET_TOP_BOX = 1060;
        public static final int AUDIO_VIDEO_UNCATEGORIZED = 1024;
        public static final int AUDIO_VIDEO_VCR = 1068;
        public static final int AUDIO_VIDEO_VIDEO_CAMERA = 1072;
        public static final int AUDIO_VIDEO_VIDEO_CONFERENCING = 1088;
        public static final int AUDIO_VIDEO_VIDEO_DISPLAY_AND_LOUDSPEAKER = 1084;
        public static final int AUDIO_VIDEO_VIDEO_GAMING_TOY = 1096;
        public static final int AUDIO_VIDEO_VIDEO_MONITOR = 1080;
        public static final int AUDIO_VIDEO_WEARABLE_HEADSET = 1028;
        public static final int COMPUTER_DESKTOP = 260;
        public static final int COMPUTER_HANDHELD_PC_PDA = 272;
        public static final int COMPUTER_LAPTOP = 268;
        public static final int COMPUTER_PALM_SIZE_PC_PDA = 276;
        public static final int COMPUTER_SERVER = 264;
        public static final int COMPUTER_UNCATEGORIZED = 256;
        public static final int COMPUTER_WEARABLE = 280;
        public static final int HEALTH_BLOOD_PRESSURE = 2308;
        public static final int HEALTH_DATA_DISPLAY = 2332;
        public static final int HEALTH_GLUCOSE = 2320;
        public static final int HEALTH_PULSE_OXIMETER = 2324;
        public static final int HEALTH_PULSE_RATE = 2328;
        public static final int HEALTH_THERMOMETER = 2312;
        public static final int HEALTH_UNCATEGORIZED = 2304;
        public static final int HEALTH_WEIGHING = 2316;
        public static final int PHONE_CELLULAR = 516;
        public static final int PHONE_CORDLESS = 520;
        public static final int PHONE_ISDN = 532;
        public static final int PHONE_MODEM_OR_GATEWAY = 528;
        public static final int PHONE_SMART = 524;
        public static final int PHONE_UNCATEGORIZED = 512;
        public static final int TOY_CONTROLLER = 2064;
        public static final int TOY_DOLL_ACTION_FIGURE = 2060;
        public static final int TOY_GAME = 2068;
        public static final int TOY_ROBOT = 2052;
        public static final int TOY_UNCATEGORIZED = 2048;
        public static final int TOY_VEHICLE = 2056;
        public static final int WEARABLE_GLASSES = 1812;
        public static final int WEARABLE_HELMET = 1808;
        public static final int WEARABLE_JACKET = 1804;
        public static final int WEARABLE_PAGER = 1800;
        public static final int WEARABLE_UNCATEGORIZED = 1792;
        public static final int WEARABLE_WRIST_WATCH = 1796;*/
        String deviceClassStr = Constants.IconPhone;
        BluetoothClass bluetoothClass = bluetoothDevice.getBluetoothClass();
        int deviceClass = bluetoothClass.getDeviceClass();
        if(1024 <= deviceClass && deviceClass <= 1096){
            deviceClassStr = Constants.IconEarphone;
        }
        else if(260 <= deviceClass && deviceClass <= 280){
            deviceClassStr = Constants.IconComputer;
        }
        else if(512 <= deviceClass && deviceClass <= 532){
            deviceClassStr = Constants.IconPhone;
        }
        return deviceClassStr;
    }
    /*
    * @params
    * ActionType：枚举类型
    * @returns
    * 该类型对应的字符串
    * */
    public static String actionType2String(ActionType actionType){
        String actionTypeStr= "";
        switch (actionType){
            case INCOME:
                actionTypeStr = "收入";
                break;
            case OUTCOME:
                actionTypeStr = "支出";
                break;
        }
        return actionTypeStr;
    }

    /*
     * @params
     * String：枚举类型
     * @returns
     * 该类型对应的字符串
     * */
    public static String cutoffAccountCode(String accountCode){
        if(accountCode == null)
            return "";
        if(accountCode.length() < 4)
            return accountCode;
        accountCode = accountCode.substring(accountCode.length() - 4);
        return accountCode;
    }
}
