# HoitNote

This is a No.1 Android Based Note from HITSZ.

![<img src="hoit_logo.png" width="100" height="100"/>](./hoit_logo.png)

## Features

Please Check our vedio [Intro Video Here](./HoitNote.mp4)

### Uncentralized

1. With no Third party, you can totally control your own note.

### Basic local Login/Register logic

1. Login/register with traditional password
2. Login/register with pattern
3. Login with fingerprint

### Swipe Account Card

1. Swipe Account Card to swith Account
2. Add account in MainPage

### Draggable panel & Draggable fab

1. a little code

### Awesome anim using simple logic

1. Roation happens when you click the account card
2. Draw a long line when you click expandable arrow

### Recycle Bin

1. recover deleted tally from recycle bin
2. delete completely the tally in recycle bin

### Data sync

1. sync data if you want to change a phone to use
2. we use bluetooth to achieve that

## Dependency

1. [picture loading] com.github.bumptech.glide:glide:4.11.0
2. [change color with ripple effects] com.wuyr:rippleanimation:1.0.0
3. [pattern lock] com.andrognito.patternlockview:patternlockview:1.0.0
4. [lottie anim] com.airbnb.android:lottie:3.4.3
5. [picker view] com.contrarywind:Android-PickerView:4.1.9
6. [swipe left layout] com.daimajia.swipelayout:library:1.2.0@aar
7. [expandable layout] net.cachapa.expandablelayout:expandablelayout:2.9.2
8. [slide tab layout] com.flyco.tablayout:FlycoTabLayout_Lib:2.1.2@aar
9. [vertical marqueeview] com.sunfusheng:marqueeview:1.3.3
10. [expandable fab] com.nambimobile.widgets:expandable-fab:1.0.0
11. [guide mask] com.github.huburt-Hu:NewbieGuide:v2.4.0

## Update at 2020-10-31: Final

With our efforts, we've accomplished "HoitNote". Below is a href to our Demo
> Check [Intro Video Here](./HoitNote.mp4)

## Update at 2020-10-15: MillStone

This is a middle millstone of HoitNote.  

### Have Done

1. Change A lot of UI
2. Add Bluetooth part
3. Add Tally part

### To Do

1. Change UI
2. Implement **ActivityAnalysis**
3. Add Chart & CI
4. Accomplish **FragmentSync**

## Update at 2020-10-10

> Check [Protype Here](https://modao.cc/app/6586f50c7704b77aec1c8f04667addcf1b4e3f81/embed/v2)

## Update 2020-09-28

1. Architecture
2. Notice:

 ```java
 /*将下面代码中的SampleActivity改为MainActivity即可到MainActivity*/
 new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashActivity.this, SampleActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        }, Constants.delayDuration);

 ```

## Specification

1.**variable** name: start with lower case, the Camel Style

- private boolean isEnabled;
- public int countStar;

2.**class/Interface/... so on** name: Camel Style

- public class Util{}
- public static class DataBaseHelper{}
- public interface IComman{}

> **Note: interface must start with "I", such as: IOnButtonClickEvent...**

3.**method** name: start with lower case, the Camel Stayle

- public void onButtonClick(){}
- private int getRes()

4.**comment** style: Java style

 ```java
 /*
 *this is the command
 * */
 ```

> **Note: In Android Studio, press "ctrl + Shift + /" to generate the comment style**

## ProjectRecord

> Check [ProjectRecord.md](https://github.com/Hoit-23o2/HoitNote/blob/master/ProjectRecord.md)
