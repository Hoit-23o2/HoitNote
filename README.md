# HoitNote
This is a No.1 Android Based Note from HITSZ.
# Update at 2020-10-15
## Have Done!!!
1. Change A lot of UI
2. Add Bluetooth part
3. Add Tally part
## To Do
1. Change UI
2. Implement **ActivityAnalysis**
3. Add Chart & CI
4. Accomplish **FragmentSync**

# Update at 2020-10-10

> Check [Protype Here](https://modao.cc/app/6586f50c7704b77aec1c8f04667addcf1b4e3f81/embed/v2)

# Update 2020-09-28
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


# Specification
1. **variable** name: start with lower case, the Camel Style

	- private boolean isEnabled;
	- public int countStar;

2. **class/Interface/... so on** name: Camel Style

	- public class Util{}
	- public static class DataBaseHelper{}
	- public interface IComman{}

	> **Note: interface must start with "I", such as: IOnButtonClickEvent...**

3. **method** name: start with lower case, the Camel Stayle

	- public void onButtonClick(){}
	- private int getRes()

4. **comment** style: Java style
	```java
	/*
	*this is the command
	* */
	```

	> **Note: In Android Studio, press "ctrl + Shift + /" to generate the comment style**

# ProjectRecord
> Check [ProjectRecord.md](https://github.com/Hoit-23o2/HoitNote/blob/master/ProjectRecord.md)



