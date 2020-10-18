package com.example.hoitnote.utils.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.core.app.ActivityOptionsCompat;

import com.example.hoitnote.BaseActivity;

import java.io.Serializable;
import java.util.ArrayList;

public class NavigationHelper {

    public static ArrayList<BaseActivity> navigationStack = new ArrayList<>();

    public static void pushActivity(BaseActivity activity){
        navigationStack.add(activity);
    }

    public static void popActivity(BaseActivity activity){
        navigationStack.remove(activity);
    }

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
    * 带参数导航
    * @param
    * tag:标识符
    * serializableObject:参数
    * */
    public static void navigationWithParameter(String tag, Serializable serializableObject,
                                                Context packageContext, Class<?> toClass, boolean isClosedCurrentActivity){
        Intent intent = new Intent(packageContext, toClass);
        intent.putExtra(tag, serializableObject); //where user is an instance of User
        packageContext.startActivity(intent);
        if(isClosedCurrentActivity)
            ((Activity)packageContext).finish();
    }

    /*
    * 封装接受参数方法
    * @param
    * tag:标识符
    * */
    public static Serializable getNavigationParameter(Activity activity, String tag){
        if(activity.getIntent().getExtras() != null) {
            return activity.getIntent().getSerializableExtra(tag);
        }
        return null;
    }

    /*
    * 关闭当前Activity
    * */
    public static void closeCurrentActivity(Context packageContext){
        ((Activity)packageContext).finish();
    }

    /*
    * 过渡元素导航
    * */
    public static void navigationWithTransitionAnim(Context context, View sharedElement, String sharedElementName,Class<?> toClass){
        Intent intent = new Intent(context, toClass);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation((Activity) context, sharedElement, sharedElementName);
        context.startActivity(intent, options.toBundle());
    }
}
