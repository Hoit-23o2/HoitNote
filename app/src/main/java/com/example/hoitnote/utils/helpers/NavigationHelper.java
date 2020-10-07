package com.example.hoitnote.utils.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class NavigationHelper {
    /*
    * 正常导航
    * */
    public static void navigationNormally(Context packageContext, Class<?> toClass){
        if(packageContext != null){
            Intent intent = new Intent(packageContext, toClass);
            packageContext.startActivity(intent);
        }
    }
    /*
    * 导航之后关闭当前页面
    * */
    public static void navigationClosedCurrentActivity(Context packageContext, Class<?> toClass){
        if(packageContext != null){
            Intent intent = new Intent(packageContext, toClass);
            packageContext.startActivity(intent);
            ((Activity)packageContext).finish();
        }
    }
    /*
    * 待实现
    * */
    public static void navigationWithParameters(Bundle bundle, Activity fromActivity, Class<?> toClass){

    }
}
