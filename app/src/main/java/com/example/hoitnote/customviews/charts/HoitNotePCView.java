package com.example.hoitnote.customviews.charts;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import com.example.hoitnote.R;
import com.example.hoitnote.models.charts.TallyAnalysisPC;
import com.example.hoitnote.utils.managers.ChartAnalysisManager;

import java.util.ArrayList;
import java.util.Stack;

public class HoitNotePCView extends androidx.appcompat.widget.AppCompatImageView {

    private Context mContext;
    public ChartAnalysisManager chartAnalysisManager;

    //控制相关
    public boolean ifAct;      //是否激活，未激活下绘制基础统计图，不接受任何按键信息，最高优先级
    public boolean ifAnimation;     //是否正在绘制动画

    //视图大小相关
    int viewWidth,viewHeight;
    int viewCenterx,viewCentery;
    Rect viewRect;

    //统计图相关
    private static final int EVERY_DRAW_TIME_INTERVAL = 15;      //每次绘画时间间隔
    private static final float EVERY_FAN_DIVISION = 15;          //分割线宽度
    ArrayList<TallyAnalysisPC> tallyAnalysisPCArrayList;        //外部传来的数据List
    Rect chartRect;               //绘制统计图的外框
    float chartOutsideRadius;       //绘制统计图的外半径
    float chartInsideRadius;        //绘制统计图的内半径
    MyCanvas myCanvasUlt;           //用于绘制最终统计图
    MyCanvas myCanvasUltNoShadow;   //无阴影图的
    boolean ifDrawnUlt;             //是否已经绘制了最终图
    ArrayList<TallyAnalysisPC> nowDrawList;                 //当前正在绘制的List
    Stack<ArrayList<TallyAnalysisPC>> tallyAnalysisPCArrayListStack;            //回退栈
    MyBitmapS myBitmapSGoBack;          //回退位图

    //动画相关
    private static final float ANIMATION_1_LAST = 1000;            //第1动画持续时间毫秒
    private static final float ANIMATION_2_LAST = 500;            //第2动画持续时间毫秒
    private static final float ANIMATION_3_LAST = 500;            //第3动画持续时间毫秒

    private static final float ANIMATION_4_LAST = 500;             //第4动画持续时间毫秒
    private static final float ANIMATION_GOBACK_IMAGE_ANI_TIME_EXTEND = 100;       //回退按钮波纹扩展时间
    private static final float ANIMATION_GOBACK_IMAGE_ANI_TIME_DISMISS = 100;       //回退按钮波纹消退时间

    private static final float ANIMATION_5_LAST = 500;            //第5动画持续时间毫秒

    private static final float ANIMATION_6_LAST = 500;            //第6动画持续时间毫秒
    private static final float ANIMATION_6_SHADOW_X_OFFSET =30;
    private static final float ANIMATION_6_SHADOW_Y_OFFSET = 30;
    private static final float ANIMATION_6_SHADOW_INSIDE_RADIUS_P = 0.8f;    //与统计图的外半径相乘后为阴影的内半径
    private static final float ANIMATION_6_SHADOW_OUTSIDE_RADIUS_P = 1.07f;    //与统计图的外半径相乘后为阴影的外半径
    private static final float ANIMATION_6_SHADOW_TRANSPARENT_NUM = 13;
    private static final float ANIMATION_6_SHADOW_INITIAL_TRANS = 150;

    MyAnimationS myAnimationS;
    MyCanvas myCanvasAni;           //动画用画布
    MyCanvas myCanvasAniLast;       //存放上一个ult
    int nowAnimation;               //当前动画，无动画用ifAnimation来判断，
    //0: 自身为0也表示无动画，
    //1：从0度开始顺时针扫过一圈
    //2：消去多余扇形动画
    //3：扇形展开动画
    //4：回退动画收回扇形
    //5：回退动画展开其他的扇形
    //6：绘制阴影
    float nowAngle;                 //当前绘制的角度
    float nowAngleSpeed;            //当前绘制角度度数增加量
    float nowAngleSpeedChange;      //当前绘制角度的速度的加速度
    Paint aniPaint1;                //动画画笔1

    //颜色相关
    int selfBackgroundColor;
    int selfCuttingLineColor;


    public HoitNotePCView(@NonNull Context context) {
        super(context);
        mContext = context;
        initializeMyImageView(null);
    }

    public HoitNotePCView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initializeMyImageView(attrs);
    }

    public HoitNotePCView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initializeMyImageView(attrs);
    }

    public void initializeMyImageView(@Nullable AttributeSet attrs){
        TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.HoitNotePCView);

        /*selfBackgroundColor = Color.rgb(255,255,255);
        selfCuttingLineColor = Color.rgb(255,255,255);*/
        selfBackgroundColor =  ta.getColor(R.styleable.HoitNotePCView_pcBackgroundColor,
                Color.WHITE);
        selfCuttingLineColor =  ta.getColor(R.styleable.HoitNotePCView_pcDividerColor,
                Color.WHITE);;
        ta.recycle();

        ifAct = false;
        aniPaint1 = new Paint();
        myAnimationS = new MyAnimationS();
        tallyAnalysisPCArrayListStack = new Stack<>();

    }

    public void actSelf(){
        ifAct = true;
        setAnimation(1,null);
        reDraw();
        myBitmapSGoBack = loadMyBitmapSTrans(getResources(), R.drawable.analy_back, Color.rgb(255,255,255));
    }

    public void setSelfBackgroundColor(int selfBackgroundColor) {
        this.selfBackgroundColor = selfBackgroundColor;
    }

    public void setSelfCuttingLineColor(int selfCuttingLineColor) {
        this.selfCuttingLineColor = selfCuttingLineColor;
    }

    public void setTallyAnalysisPCArrayList(ArrayList<TallyAnalysisPC> tallyAnalysisPCArrayList) {
        this.tallyAnalysisPCArrayList = tallyAnalysisPCArrayList;
        setAnimation(1,null);
        reDraw();
    }

    public void setManager(ChartAnalysisManager chartAnalysisManager){
        this.chartAnalysisManager = chartAnalysisManager;
    }

    public void enterTallyAnalysisPC(TallyAnalysisPC tallyAnalysisPC){
        tallyAnalysisPCArrayListStack.push(nowDrawList);
        nowDrawList = tallyAnalysisPC.nextScreen;
        ifDrawnUlt = false;
        setAnimation(2,tallyAnalysisPC);
        postInvalidateDelayed(EVERY_DRAW_TIME_INTERVAL);
    }

    public void goBack(){
        ArrayList<TallyAnalysisPC> tallyAnalysisPCArrayList;
        tallyAnalysisPCArrayList = nowDrawList;
        nowDrawList = tallyAnalysisPCArrayListStack.pop();
        int i,len = nowDrawList.size();
        for(i = 0; i < len;i++){
            if(nowDrawList.get(i).nextScreen == tallyAnalysisPCArrayList){
                break;
            }
        }
        TallyAnalysisPC tallyAnalysisPC = nowDrawList.get(i);
        setAnimation(4,tallyAnalysisPC);
        ifDrawnUlt = false;
        postInvalidateDelayed(EVERY_DRAW_TIME_INTERVAL);
    }

    public void reDraw(){
        ifDrawnUlt = false;
        nowDrawList = this.tallyAnalysisPCArrayList;
        while(!this.tallyAnalysisPCArrayListStack.empty()){
            this.tallyAnalysisPCArrayListStack.pop();
        }
        postInvalidateDelayed(EVERY_DRAW_TIME_INTERVAL);
    }

    public MyBitmapS loadMyBitmapSTrans(Resources resources, int drawAbleId, int transColor){
        MyBitmapS myBitmapS = new MyBitmapS();
        myBitmapS.bitmap = loadBitmapTrans(resources,drawAbleId,transColor);
        myBitmapS.width = myBitmapS.bitmap.getWidth();
        myBitmapS.height = myBitmapS.bitmap.getHeight();
        myBitmapS.rect.left = 0;
        myBitmapS.rect.top = 0;
        myBitmapS.rect.right = myBitmapS.width;
        myBitmapS.rect.bottom = myBitmapS.height;
        return myBitmapS;
    }

    public Bitmap loadBitmapTrans(Resources resources, int drawAbleId, int transColor){
        Bitmap bitmap1 = null,bitmap2 = null;
       /* SVG svg = null;
        try {
            svg = SVG.getFromResource(resources, drawAbleId);
        } catch (SVGParseException e) {
            e.printStackTrace();
        }
        if (svg != null && svg.getDocumentWidth() != -1F) {

            svg.setDocumentHeight(300*0.35f);
            svg.setDocumentWidth(300*0.35f);
            bitmap1 = Bitmap.createBitmap((int) (300*0.35f), (int) (300*0.35f),
                    Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap1);
            canvas.drawARGB(0, 255, 255, 255);
            svg.renderToCanvas(canvas);
        }*/

        int width,height;
        int x, y;
        int red,blue,green,pixel,alpha;
        int [] oriPixels;
        bitmap2 = BitmapFactory.decodeResource(resources,drawAbleId);
        width = bitmap2.getWidth();
        height = bitmap2.getHeight();
        oriPixels = new int[width * height];
        bitmap2.getPixels(oriPixels,0,width,0,0,width,height);
        for(x = 0;x < height; x++){
            for(y = 0; y < width; y++){
                pixel = oriPixels[x * width + y];
                red = (pixel & 0x00ff0000) >> 16;
                green = (pixel & 0x0000ff00) >> 8;
                blue = pixel & 0x000000ff;
                //alpha = pixel & 0xff000000 >> 24;
                int newColor = Color.rgb(red,green,blue);
                if(newColor == transColor){
                    oriPixels[x * width + y] = 0;
                }else{
                    oriPixels[x * width + y] = newColor;
                }
            }
        }
        bitmap1 = Bitmap.createBitmap(oriPixels,0,width,width,height, Bitmap.Config.ARGB_8888);
        return bitmap1;
    }

    
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(ifAct){
            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                //深入，或回退Screen事件逻辑
                if (!ifAnimation && nowDrawList != null && tallyAnalysisPCArrayList != null) {
                    float x = event.getX();
                    float y = event.getY();
                    float disx = x - viewCenterx;
                    float disy = y - viewCentery;
                    double allDis = Math.sqrt(disx * disx + disy * disy);
                    float angle;
                    if (allDis >= chartInsideRadius && allDis <= chartOutsideRadius) {
                        if (disx == 0) {
                            if (disy >= 0) {
                                angle = 90;
                            } else {
                                angle = -90;
                            }
                        } else {
                            double atanAns = Math.atan(disy / disx);
                            angle = (float) (atanAns * 180.0D / Math.PI);
                            if (disx < 0) {
                                angle += 180.0f;
                            }
                            while (angle >= 360) {
                                angle -= 360;
                            }
                            while (angle < 0) {
                                angle += 360;
                            }
                        }
                        int len = nowDrawList.size();
                        int i;
                        for (i = 0; i < len; i++) {
                            TallyAnalysisPC tallyAnalysisPC = nowDrawList.get(i);
                            if (tallyAnalysisPC.fanStart <= angle && tallyAnalysisPC.fanEnd >= angle) {
                                if (tallyAnalysisPC.nextScreen != null) {
//                                Log.d("point","======="+angle+"========="+disx+"=="+disy);
                                    chartAnalysisManager.notifyEnterTallyAnalysisPC(tallyAnalysisPC);
                                }
                                break;
                            }
                        }
                    } else if (allDis <= chartInsideRadius) {
                        if (!tallyAnalysisPCArrayListStack.empty()) {
                            chartAnalysisManager.notifyGoBackPc();
                        }
                    }
                } else if (ifAnimation) {
                    ifAnimation = false;
                    nowAnimation = 0;
                }
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(ifAct){
            if(!ifDrawnUlt){
                drawUlt();
            }
            if(ifAnimation && nowAnimation != 0){
                showAnimation(canvas);
            }else{
                canvas.drawBitmap(myCanvasUlt.bitmap,myCanvasUlt.selfRect,chartRect,null);
            }
        }
    }

    private void drawUlt(){
        myCanvasUlt.canvas.drawColor(selfBackgroundColor);
        myCanvasUltNoShadow.canvas.drawColor(selfBackgroundColor);
        if(nowDrawList != null){
            //绘制阴影
            Paint paint = new Paint();
            int i;
            paint.setColor(Color.rgb(150,150,150));
            paint.setAlpha(0);
            float shaDowCenterX = myCanvasUlt.centerx + ANIMATION_6_SHADOW_X_OFFSET;
            float shaDowCenterY = myCanvasUlt.centery + ANIMATION_6_SHADOW_Y_OFFSET;
            int nowTrans = 0;
            int everyTransChange = (int)(ANIMATION_6_SHADOW_INITIAL_TRANS / ANIMATION_6_SHADOW_TRANSPARENT_NUM);
            float nowShadowRadius = chartOutsideRadius * ANIMATION_6_SHADOW_OUTSIDE_RADIUS_P;
            float everyShadowRadiusChange = ((chartOutsideRadius * ANIMATION_6_SHADOW_INSIDE_RADIUS_P) - nowShadowRadius) / ANIMATION_6_SHADOW_TRANSPARENT_NUM;
            for(i = 0; i < ANIMATION_6_SHADOW_TRANSPARENT_NUM; i++){
                nowTrans += everyTransChange;
                paint.setAlpha(nowTrans);
                myCanvasUlt.canvas.drawCircle(shaDowCenterX,shaDowCenterY,nowShadowRadius,paint);
                nowShadowRadius += everyShadowRadiusChange;
            }

            //绘制扇形
            int len = nowDrawList.size();
            for(i = 0;i < len; i++){
                TallyAnalysisPC tallyAnalysisPC = nowDrawList.get(i);
                paint.setColor(tallyAnalysisPC.selfColor);
                myCanvasUlt.canvas.drawArc(myCanvasUlt.rectFOut,tallyAnalysisPC.fanStart,tallyAnalysisPC.fanEnd - tallyAnalysisPC.fanStart,
                        true,paint);
                myCanvasUltNoShadow.canvas.drawArc(myCanvasUltNoShadow.rectFOut,tallyAnalysisPC.fanStart,tallyAnalysisPC.fanEnd - tallyAnalysisPC.fanStart,
                        true,paint);
            }

            //设置白色画笔
            paint.setColor(selfCuttingLineColor);
            //绘制分割线
            paint.setStrokeWidth(EVERY_FAN_DIVISION);
            if(len > 1){
                for(i = 0;i < len; i++){
                    TallyAnalysisPC tallyAnalysisPC = nowDrawList.get(i);
                    double angle = tallyAnalysisPC.fanStart * Math.PI /180;
                    myCanvasUlt.canvas.drawLine(myCanvasUlt.centerx,myCanvasUlt.centery,
                            (float)((chartOutsideRadius) * Math.cos(angle)) + myCanvasUlt.centerx,
                            (float)((chartOutsideRadius) * Math.sin(angle)) + myCanvasUlt.centery,
                            paint);
                    myCanvasUltNoShadow.canvas.drawLine(myCanvasUltNoShadow.centerx,myCanvasUltNoShadow.centery,
                            (float)((chartOutsideRadius) * Math.cos(angle)) + myCanvasUltNoShadow.centerx,
                            (float)((chartOutsideRadius) * Math.sin(angle)) + myCanvasUltNoShadow.centery,
                            paint);
                }
            }
            //绘制中空园
            paint.setColor(selfBackgroundColor);
            myCanvasUlt.canvas.drawArc(myCanvasUlt.rectFIn,0,360, true,paint);
            myCanvasUltNoShadow.canvas.drawArc(myCanvasUltNoShadow.rectFIn,0,360, true,paint);
            //绘制回退图片
            paint.setColor(selfBackgroundColor);
            myCanvasUlt.canvas.drawCircle(myCanvasUlt.centerx,myCanvasUlt.centery,chartInsideRadius,paint);
            myCanvasUltNoShadow.canvas.drawCircle(myCanvasUltNoShadow.centerx,myCanvasUltNoShadow.centery,chartInsideRadius,paint);
            //myCanvasUlt.canvas.drawBitmap(myBitmapSGoBack.bitmap,myBitmapSGoBack.rect,myCanvasUlt.rectFIn,null);
            //myCanvasUltNoShadow.canvas.drawBitmap(myBitmapSGoBack.bitmap,myBitmapSGoBack.rect,myCanvasUltNoShadow.rectFIn,null);
        }else{
            //基础扇形数据
            float [] floats = new float[]{160,120,80};

            //绘制扇形
            Paint paint = new Paint();
            paint.setColor(Color.rgb(230,230,230));
            myCanvasUlt.canvas.drawArc(myCanvasUlt.rectFOut,0,360, true,paint);

            //设置白色画笔
            paint.setColor(selfBackgroundColor);
            //绘制分割线
            int len = floats.length;
            int i;
            paint.setStrokeWidth(EVERY_FAN_DIVISION);
            float nowAngled = 0;
            float nowAngler;
            for (i = 0; i < len; i++) {
                nowAngler = (float) (nowAngled * Math.PI / 180);
                myCanvasUlt.canvas.drawLine(myCanvasUlt.centerx, myCanvasUlt.centery,
                        (float) ((chartOutsideRadius) * Math.cos(nowAngler)) + myCanvasUlt.centerx,
                        (float) ((chartOutsideRadius) * Math.sin(nowAngler)) + myCanvasUlt.centery,
                        paint);
                nowAngled += floats[i];
            }
            //绘制中空园
            paint.setColor(selfBackgroundColor);
            myCanvasUlt.canvas.drawArc(myCanvasUlt.rectFIn,0,360, true,paint);
            myCanvasUltNoShadow.canvas.drawBitmap(myCanvasUlt.bitmap,myCanvasUlt.selfRect,myCanvasUltNoShadow.selfRect,null);
        }
        ifDrawnUlt = true;
    }

    public void setAnimation(int animation,TallyAnalysisPC mainTallyAnalysisPC){
        //chartAnalysisManager已经截断了动画过程事件
        //set之后并不会激活绘画，手动postInvalidate
        //0：无动画
        //1：从0度开始顺时针扫过一圈
        ifAnimation = true;
        nowAnimation = animation;
        if(animation == 1){
            nowAngle = 0;
            float timeS = ANIMATION_1_LAST / 1000;
            nowAngleSpeedChange = 720.0f / (timeS * timeS);
            nowAngleSpeed = nowAngleSpeedChange * timeS;
            nowAngleSpeedChange = -nowAngleSpeedChange;
            aniPaint1.setColor(selfBackgroundColor);
        }else if(animation == 2){
            myAnimationS.nowAngle = mainTallyAnalysisPC.fanStart;
            float timeS = ANIMATION_2_LAST / 1000;
            myAnimationS.nowAngleSpeedChange = 720.0f / (timeS * timeS);
            myAnimationS.nowAngleSpeed = myAnimationS.nowAngleSpeedChange * timeS;
            myAnimationS.nowAngleSpeedChange = -myAnimationS.nowAngleSpeedChange;
            myAnimationS.nowAngleCount = 0;

            myAnimationS.nowAngle1Start = mainTallyAnalysisPC.fanStart;
            myAnimationS.nowAngle1End = mainTallyAnalysisPC.fanEnd;
            myAnimationS.nowAngle1Size = mainTallyAnalysisPC.fanEnd - mainTallyAnalysisPC.fanStart;
            myAnimationS.nowAngle2Start = mainTallyAnalysisPC.fanEnd;
            myAnimationS.nowAngle2End = mainTallyAnalysisPC.fanStart;
            myAnimationS.nowAngle2Size = 360 - myAnimationS.nowAngle1Size;
            myAnimationS.tallyAnalysisPC1 = mainTallyAnalysisPC;
            myAnimationS.paint1.setColor(Color.argb(150,50,50,50));
            myAnimationS.paint2.setColor(selfBackgroundColor);
            myAnimationS.paint3.setStrokeWidth(EVERY_FAN_DIVISION);
            myAnimationS.paint3.setColor(selfCuttingLineColor);
            myCanvasAniLast.canvas.drawBitmap(myCanvasUltNoShadow.bitmap,myCanvasUltNoShadow.selfRect,myCanvasAni.selfRect,null);
        }else if(animation == 3) {
            myAnimationS.tallyAnalysisPCArrayList1 = myAnimationS.tallyAnalysisPC1.nextScreen;

            float rotateAngle;
            float timeS = ANIMATION_3_LAST / 1000;
            rotateAngle = 360 - myAnimationS.nowAngle1Start;
            if (rotateAngle < 30) {
                rotateAngle += 360;
            }
            myAnimationS.nowAngle1SpeedChange = (rotateAngle * 2) / (timeS * timeS);
            myAnimationS.nowAngle = 0;
            myAnimationS.nowAngleCount = rotateAngle;
            myAnimationS.nowAngle1Speed = myAnimationS.nowAngle1SpeedChange * timeS;
            myAnimationS.nowAngle1SpeedChange = -myAnimationS.nowAngle1SpeedChange;
            myAnimationS.nowAngleSpeed = myAnimationS.nowAngle1Speed;       //此处用于存放起始速度

            float endAngle = rotateAngle + myAnimationS.nowAngle2Start;
            while (endAngle > 360) {
                endAngle -= 360;
            }
            endAngle = 360 - endAngle;
            rotateAngle += endAngle;
            myAnimationS.nowAngle2SpeedChange = (rotateAngle * 2) / (timeS * timeS);
            myAnimationS.nowAngle2Speed = myAnimationS.nowAngle2SpeedChange * timeS;
            myAnimationS.nowAngle2SpeedChange = -myAnimationS.nowAngle2SpeedChange;

            myAnimationS.paint1.setColor(myAnimationS.tallyAnalysisPC1.selfColor);
            myAnimationS.paint1.setAlpha(255);
        }else if(animation == 4) {
            myAnimationS.radiusSpeed = chartInsideRadius / (ANIMATION_GOBACK_IMAGE_ANI_TIME_EXTEND / 1000.0f);
            myAnimationS.goBackTimeCount = ANIMATION_GOBACK_IMAGE_ANI_TIME_EXTEND + ANIMATION_GOBACK_IMAGE_ANI_TIME_DISMISS;
            myAnimationS.radius = 0;

            //此处的度数都为反向旋转的度数
            myAnimationS.tallyAnalysisPC1 = mainTallyAnalysisPC;
            myAnimationS.tallyAnalysisPCArrayList1 = mainTallyAnalysisPC.nextScreen;
            myAnimationS.nowAngle1Start = 0;
            myAnimationS.nowAngle1End = 360;
            myAnimationS.nowAngle1Size = 360;

            float rotateAngle;
            float timeS = ANIMATION_4_LAST / 1000;
            rotateAngle = 360 - mainTallyAnalysisPC.fanStart;
            if (rotateAngle < 30) {
                rotateAngle += 360;
            }
            myAnimationS.nowAngle1SpeedChange = -(rotateAngle * 2) / (timeS * timeS);
            myAnimationS.nowAngle = 0;
            myAnimationS.nowAngleCount = -rotateAngle;
            myAnimationS.nowAngle1Speed = myAnimationS.nowAngle1SpeedChange * timeS;
            myAnimationS.nowAngle1SpeedChange = -myAnimationS.nowAngle1SpeedChange;
            myAnimationS.nowAngleSpeed = myAnimationS.nowAngle1Speed;       //此处用于存放起始速度

            float endAngle;
            endAngle = 360 - (mainTallyAnalysisPC.fanEnd - mainTallyAnalysisPC.fanStart);
            rotateAngle += endAngle;
            myAnimationS.nowAngle2SpeedChange = -(rotateAngle * 2) / (timeS * timeS);
            myAnimationS.nowAngle2Speed = myAnimationS.nowAngle2SpeedChange * timeS;
            myAnimationS.nowAngle2SpeedChange = -myAnimationS.nowAngle2SpeedChange;

            myAnimationS.paint1.setColor(myAnimationS.tallyAnalysisPC1.selfColor);
            myAnimationS.paint1.setAlpha(255);

            myAnimationS.paint3.setColor(Color.argb(150, 50, 50, 50));
        }else if(animation == 5){
            myAnimationS.nowAngle = myAnimationS.tallyAnalysisPC1.fanEnd;
            float timeS = ANIMATION_5_LAST / 1000;
            myAnimationS.nowAngleSpeedChange = -720.0f / (timeS * timeS);
            myAnimationS.nowAngleSpeed = myAnimationS.nowAngleSpeedChange * timeS;
            myAnimationS.nowAngleSpeedChange = -myAnimationS.nowAngleSpeedChange;
            myAnimationS.nowAngleCount = 0;

            myAnimationS.nowAngle1Start = myAnimationS.tallyAnalysisPC1.fanStart;
            myAnimationS.nowAngle1End = myAnimationS.tallyAnalysisPC1.fanEnd;
            myAnimationS.nowAngle1Size = myAnimationS.tallyAnalysisPC1.fanEnd - myAnimationS.tallyAnalysisPC1.fanStart;
            myAnimationS.paint1.setColor(Color.argb(150,50,50,50));
            myAnimationS.paint2.setColor(selfBackgroundColor);
            myAnimationS.paint3.setStrokeWidth(EVERY_FAN_DIVISION);
            myAnimationS.paint3.setColor(selfCuttingLineColor);
            myCanvasAniLast.canvas.drawBitmap(myCanvasUltNoShadow.bitmap,myCanvasUltNoShadow.selfRect,myCanvasAni.selfRect,null);
        }else if(animation == 6) {
            myAnimationS.timeCount = 0;
            myAnimationS.nowAngle = 255;        //此处用作透明度计数
            myAnimationS.nowAngleSpeed = -255.0f / (ANIMATION_6_LAST / EVERY_DRAW_TIME_INTERVAL);  //此处用作透明度变化速度计数
        } else{
            ifAnimation = false;
            nowAnimation = 0;
        }
    }

    private void showAnimation(Canvas canvas) {
        if (nowAnimation == 1) {
            myCanvasAni.canvas.drawBitmap(myCanvasUltNoShadow.bitmap, myCanvasUltNoShadow.selfRect, myCanvasAni.selfRect, null);
            if (nowAngle < 360) {
                float changeFactor = EVERY_DRAW_TIME_INTERVAL / 1000.0f;
                nowAngle += nowAngleSpeed * changeFactor;
                nowAngleSpeed += nowAngleSpeedChange * changeFactor;
                if (nowAngle < 360) {
                    myCanvasAni.canvas.drawArc(myCanvasAni.rectFOut, nowAngle, 360 - nowAngle, true, aniPaint1);
                }
            } else {
                if(nowDrawList != null){
                    setAnimation(6,null);
                }else{
                    ifAnimation = false;
                    nowAnimation = 0;
                }
            }
            myAnimationS.paint2.setColor(selfBackgroundColor);
            myCanvasAni.canvas.drawCircle(myCanvasAni.centerx,myCanvasAni.centery,chartInsideRadius,myAnimationS.paint2);
            canvas.drawBitmap(myCanvasAni.bitmap, myCanvasAni.selfRect, chartRect, null);
            postInvalidateDelayed(EVERY_DRAW_TIME_INTERVAL);
        } else if (nowAnimation == 2) {
            if (myAnimationS.nowAngleCount < 360) {
                float changeFactor = EVERY_DRAW_TIME_INTERVAL / 1000.0f;
                float angleChange = myAnimationS.nowAngleSpeed * changeFactor;
                myAnimationS.nowAngle += angleChange;
                while (myAnimationS.nowAngle >= 360) {
                    myAnimationS.nowAngle -= 360;
                }
                myAnimationS.nowAngleCount += angleChange;
                if(myAnimationS.nowAngleCount > 360){
                    myAnimationS.nowAngleCount = 360;
                }
                myAnimationS.nowAngleSpeed += myAnimationS.nowAngleSpeedChange * changeFactor;
                myAnimationS.paint1.setAlpha((int) (150 * (1.0f - myAnimationS.nowAngleCount / 360.0f)));

                myCanvasAni.canvas.drawBitmap(myCanvasAniLast.bitmap, myCanvasAniLast.selfRect, myCanvasAniLast.selfRect, null);
                if (myAnimationS.nowAngleCount <= myAnimationS.nowAngle1Size) {
                    myCanvasAni.canvas.drawArc(myCanvasAni.rectFOut, myAnimationS.nowAngle1Start, myAnimationS.nowAngleCount, true, myAnimationS.paint1);
                } else {
                    myCanvasAni.canvas.drawArc(myCanvasAni.rectFOut, myAnimationS.nowAngle1Start, myAnimationS.nowAngle1Size, true, myAnimationS.paint1);
                    myCanvasAni.canvas.drawArc(myCanvasAni.rectFOut, myAnimationS.nowAngle2Start,
                            myAnimationS.nowAngleCount - myAnimationS.nowAngle1Size, true, myAnimationS.paint2);
                }

                //绘制分割线
                if (myAnimationS.nowAngle1Size < 360) {
                    float nowAngler = (float) (myAnimationS.nowAngle1Start * Math.PI / 180);
                    myCanvasAni.canvas.drawLine(myCanvasAni.centerx, myCanvasAni.centery,
                            (float) ((chartOutsideRadius) * Math.cos(nowAngler)) + myCanvasAni.centerx,
                            (float) ((chartOutsideRadius) * Math.sin(nowAngler)) + myCanvasAni.centery,
                            myAnimationS.paint3);
                    nowAngler = (float) (myAnimationS.nowAngle1End * Math.PI / 180);
                    myCanvasAni.canvas.drawLine(myCanvasAni.centerx, myCanvasAni.centery,
                            (float) ((chartOutsideRadius) * Math.cos(nowAngler)) + myCanvasAni.centerx,
                            (float) ((chartOutsideRadius) * Math.sin(nowAngler)) + myCanvasAni.centery,
                            myAnimationS.paint3);
                }
                myAnimationS.paint2.setColor(selfBackgroundColor);
                myCanvasAni.canvas.drawCircle(myCanvasAni.centerx,myCanvasAni.centery,chartInsideRadius,myAnimationS.paint2);
            } else {
                myCanvasAniLast.canvas.drawBitmap(myCanvasAni.bitmap, myCanvasAni.selfRect, myCanvasAniLast.selfRect, null);
                setAnimation(3, null);
            }
            canvas.drawBitmap(myCanvasAni.bitmap, myCanvasAni.selfRect, chartRect, null);
            postInvalidateDelayed(EVERY_DRAW_TIME_INTERVAL);
        } else if (nowAnimation == 3) {
            if (myAnimationS.nowAngle1Speed > 0) {
                //Log.d("message", "==============" + myAnimationS.nowAngle1Start);
                myCanvasAni.canvas.drawColor(selfBackgroundColor);
                //绘制扇形
                ArrayList<TallyAnalysisPC> tmpTallyAnalysisPCArrayList = myAnimationS.tallyAnalysisPCArrayList1;
                int len = tmpTallyAnalysisPCArrayList.size();
                int i;
                float offsetAngle = 0;
                float sweepAngle;
                float scaling = myAnimationS.nowAngle1Size / 360.0f;
                float penWidthScaling;
                for (i = 0; i < len; i++) {
                    TallyAnalysisPC tallyAnalysisPC = tmpTallyAnalysisPCArrayList.get(i);
                    myAnimationS.paint2.setColor(tallyAnalysisPC.selfColor);
                    sweepAngle = (tallyAnalysisPC.fanEnd - tallyAnalysisPC.fanStart) * scaling;
                    myCanvasAni.canvas.drawArc(myCanvasAni.rectFOut, myAnimationS.nowAngle1Start + offsetAngle,
                            sweepAngle, true, myAnimationS.paint2);
                    offsetAngle += sweepAngle;
                }
                //绘制上一层的扇形
                penWidthScaling = (myAnimationS.nowAngleSpeed - myAnimationS.nowAngle1Speed) / myAnimationS.nowAngleSpeed;
                myAnimationS.paint1.setAlpha((int) ((1 - penWidthScaling) * 255.0f));
                myCanvasAni.canvas.drawArc(myCanvasAni.rectFOut, myAnimationS.nowAngle1Start, myAnimationS.nowAngle1Size, true, myAnimationS.paint1);
                //绘制分割线
                if (len > 1) {
                    myAnimationS.paint2.setStrokeWidth(EVERY_FAN_DIVISION * penWidthScaling);
                    //Log.d("message",EVERY_FAN_DIVISION * scaling + " ");
                    myAnimationS.paint2.setColor(selfCuttingLineColor);
                    offsetAngle = 0;
                    //绘制第一条
                    float nowAngler = (float) (myAnimationS.nowAngle1Start * Math.PI / 180);
                    myCanvasAni.canvas.drawLine(myCanvasAni.centerx, myCanvasAni.centery,
                            (float) ((chartOutsideRadius) * Math.cos(nowAngler)) + myCanvasAni.centerx,
                            (float) ((chartOutsideRadius) * Math.sin(nowAngler)) + myCanvasAni.centery,
                            myAnimationS.paint2);

                    for (i = 0; i < len; i++) {
                        TallyAnalysisPC tallyAnalysisPC = tmpTallyAnalysisPCArrayList.get(i);
                        sweepAngle = (tallyAnalysisPC.fanEnd - tallyAnalysisPC.fanStart) * scaling;
                        offsetAngle += sweepAngle;
                        nowAngler = (float) ((myAnimationS.nowAngle1Start + offsetAngle) * Math.PI / 180);
                        myCanvasAni.canvas.drawLine(myCanvasAni.centerx, myCanvasAni.centery,
                                (float) ((chartOutsideRadius) * Math.cos(nowAngler)) + myCanvasAni.centerx,
                                (float) ((chartOutsideRadius) * Math.sin(nowAngler)) + myCanvasAni.centery,
                                myAnimationS.paint2);
                    }
                }

                //更新角度
                float changeFactor = EVERY_DRAW_TIME_INTERVAL / 1000.0f;
                float angle1Change = myAnimationS.nowAngle1Speed * changeFactor;
                myAnimationS.nowAngle1Start += angle1Change;
                while (myAnimationS.nowAngle1Start >= 360) {
                    myAnimationS.nowAngle1Start -= 360;
                }
                float angle2Change = myAnimationS.nowAngle2Speed * changeFactor;
                myAnimationS.nowAngle1Size += angle2Change - angle1Change;
                myAnimationS.nowAngle += angle1Change;
                if (myAnimationS.nowAngle >= myAnimationS.nowAngleCount) {
                    myAnimationS.nowAngle1Size = 360;
                    myAnimationS.nowAngle1Start = 0;
                }

                myAnimationS.nowAngle1Speed += myAnimationS.nowAngle1SpeedChange * changeFactor;
                myAnimationS.nowAngle2Speed += myAnimationS.nowAngle2SpeedChange * changeFactor;

                myAnimationS.paint2.setColor(selfBackgroundColor);
                myCanvasAni.canvas.drawCircle(myCanvasAni.centerx,myCanvasAni.centery,chartInsideRadius,myAnimationS.paint2);
            } else {
                setAnimation(6,null);
            }
            canvas.drawBitmap(myCanvasAni.bitmap, myCanvasAni.selfRect, chartRect, null);
            postInvalidateDelayed(EVERY_DRAW_TIME_INTERVAL);
        } else if (nowAnimation == 4) {
            if (myAnimationS.nowAngle1Speed < 0) {
                //Log.d("message", "==============" + myAnimationS.nowAngle1Start);
                myCanvasAni.canvas.drawColor(selfBackgroundColor);
                //绘制扇形
                ArrayList<TallyAnalysisPC> tmpTallyAnalysisPCArrayList = myAnimationS.tallyAnalysisPCArrayList1;
                int len = tmpTallyAnalysisPCArrayList.size();
                int i;
                float offsetAngle = 0;
                float sweepAngle;
                float scaling = myAnimationS.nowAngle1Size / 360.0f;
                float penWidthScaling;
                for (i = 0; i < len; i++) {
                    TallyAnalysisPC tallyAnalysisPC = tmpTallyAnalysisPCArrayList.get(i);
                    myAnimationS.paint2.setColor(tallyAnalysisPC.selfColor);
                    sweepAngle = (tallyAnalysisPC.fanEnd - tallyAnalysisPC.fanStart) * scaling;
                    myCanvasAni.canvas.drawArc(myCanvasAni.rectFOut, myAnimationS.nowAngle1Start + offsetAngle,
                            sweepAngle, true, myAnimationS.paint2);
                    offsetAngle += sweepAngle;
                }
                //绘制上一层的扇形
                penWidthScaling = (myAnimationS.nowAngleSpeed - myAnimationS.nowAngle1Speed) / myAnimationS.nowAngleSpeed;
                myAnimationS.paint1.setAlpha((int) ((penWidthScaling) * 255.0f));
                myCanvasAni.canvas.drawArc(myCanvasAni.rectFOut, myAnimationS.nowAngle1Start, myAnimationS.nowAngle1Size, true, myAnimationS.paint1);
                //绘制分割线
                if (len > 1) {
                    myAnimationS.paint2.setStrokeWidth(EVERY_FAN_DIVISION * (1 - penWidthScaling));
                    //Log.d("message",EVERY_FAN_DIVISION * scaling + " ");
                    myAnimationS.paint2.setColor(selfCuttingLineColor);
                    offsetAngle = 0;
                    //绘制第一条
                    float nowAngler = (float) (myAnimationS.nowAngle1Start * Math.PI / 180);
                    myCanvasAni.canvas.drawLine(myCanvasAni.centerx, myCanvasAni.centery,
                            (float) ((chartOutsideRadius) * Math.cos(nowAngler)) + myCanvasAni.centerx,
                            (float) ((chartOutsideRadius) * Math.sin(nowAngler)) + myCanvasAni.centery,
                            myAnimationS.paint2);

                    for (i = 0; i < len; i++) {
                        TallyAnalysisPC tallyAnalysisPC = tmpTallyAnalysisPCArrayList.get(i);
                        sweepAngle = (tallyAnalysisPC.fanEnd - tallyAnalysisPC.fanStart) * scaling;
                        offsetAngle += sweepAngle;
                        nowAngler = (float) ((myAnimationS.nowAngle1Start + offsetAngle) * Math.PI / 180);
                        myCanvasAni.canvas.drawLine(myCanvasAni.centerx, myCanvasAni.centery,
                                (float) ((chartOutsideRadius) * Math.cos(nowAngler)) + myCanvasAni.centerx,
                                (float) ((chartOutsideRadius) * Math.sin(nowAngler)) + myCanvasAni.centery,
                                myAnimationS.paint2);
                    }
                }

                //更新角度
                float changeFactor = EVERY_DRAW_TIME_INTERVAL / 1000.0f;
                float angle1Change = myAnimationS.nowAngle1Speed * changeFactor;
                myAnimationS.nowAngle1Start += angle1Change;
                while (myAnimationS.nowAngle1Start < 0) {
                    myAnimationS.nowAngle1Start += 360;
                }
                float angle2Change = myAnimationS.nowAngle2Speed * changeFactor;
                myAnimationS.nowAngle1Size += angle2Change - angle1Change;
                myAnimationS.nowAngle += angle1Change;
                if (myAnimationS.nowAngle <= myAnimationS.nowAngleCount) {
                    myAnimationS.nowAngle1Size = myAnimationS.tallyAnalysisPC1.fanEnd - myAnimationS.tallyAnalysisPC1.fanStart;
                    myAnimationS.nowAngle1Start = myAnimationS.tallyAnalysisPC1.fanStart;
                }

                myAnimationS.nowAngle1Speed += myAnimationS.nowAngle1SpeedChange * changeFactor;
                myAnimationS.nowAngle2Speed += myAnimationS.nowAngle2SpeedChange * changeFactor;


                myAnimationS.paint2.setColor(selfBackgroundColor);
                myCanvasAni.canvas.drawCircle(myCanvasAni.centerx,myCanvasAni.centery,chartInsideRadius,myAnimationS.paint2);
                //绘制回退按钮动画
                if (myAnimationS.goBackTimeCount > 0) {
                    myAnimationS.goBackTimeCount -= EVERY_DRAW_TIME_INTERVAL;
                    if (myAnimationS.goBackTimeCount < 0) {
                        myAnimationS.goBackTimeCount = 0;
                    }
                    if (myAnimationS.goBackTimeCount >= ANIMATION_GOBACK_IMAGE_ANI_TIME_EXTEND) {
                        myAnimationS.radius += myAnimationS.radiusSpeed * changeFactor;
                    }
                    myAnimationS.paint3.setAlpha((int) (myAnimationS.goBackTimeCount / (ANIMATION_GOBACK_IMAGE_ANI_TIME_EXTEND + ANIMATION_GOBACK_IMAGE_ANI_TIME_DISMISS) * 150.0f));
                    myCanvasAni.canvas.drawCircle(myCanvasAni.centerx, myCanvasAni.centery, myAnimationS.radius, myAnimationS.paint3);
                }
            } else {
                setAnimation(5, null);
            }
            canvas.drawBitmap(myCanvasAni.bitmap, myCanvasAni.selfRect, chartRect, null);
            postInvalidateDelayed(EVERY_DRAW_TIME_INTERVAL);
        }else if(nowAnimation == 5){
            if (myAnimationS.nowAngleSpeed < 0) {
                float changeFactor = EVERY_DRAW_TIME_INTERVAL / 1000.0f;
                float angleChange = myAnimationS.nowAngleSpeed * changeFactor;
                myAnimationS.nowAngle += angleChange;
                while (myAnimationS.nowAngle < 0) {
                    myAnimationS.nowAngle += 360;
                }
                myAnimationS.nowAngleCount += -angleChange;
                if(myAnimationS.nowAngleCount > 360){
                    myAnimationS.nowAngleCount = 360;
                }
                myAnimationS.nowAngleSpeed += myAnimationS.nowAngleSpeedChange * changeFactor;
                myAnimationS.paint1.setAlpha((int) (150 * (1.0f - myAnimationS.nowAngleCount / 360.0f)));

                //绘制遮挡扇形
                myCanvasAni.canvas.drawBitmap(myCanvasAniLast.bitmap, myCanvasAniLast.selfRect, myCanvasAniLast.selfRect, null);
                if (myAnimationS.nowAngleCount <= myAnimationS.nowAngle1Size) {
                    myCanvasAni.canvas.drawArc(myCanvasAni.rectFOut, myAnimationS.nowAngle, myAnimationS.nowAngleCount, true, myAnimationS.paint1);
                    myCanvasAni.canvas.drawArc(myCanvasAni.rectFOut,myAnimationS.tallyAnalysisPC1.fanEnd,
                            360 - (myAnimationS.nowAngle1Size),true,myAnimationS.paint2);
                } else {
                    myCanvasAni.canvas.drawArc(myCanvasAni.rectFOut, myAnimationS.nowAngle, myAnimationS.nowAngleCount, true, myAnimationS.paint1);
                    myCanvasAni.canvas.drawArc(myCanvasAni.rectFOut, myAnimationS.tallyAnalysisPC1.fanEnd,
                            360 - myAnimationS.nowAngleCount, true, myAnimationS.paint2);
                }

                //绘制分割线
                if (myAnimationS.nowAngle1Size < 360) {
                    float nowAngler = (float) (myAnimationS.nowAngle1Start * Math.PI / 180);
                    myCanvasAni.canvas.drawLine(myCanvasAni.centerx, myCanvasAni.centery,
                            (float) ((chartOutsideRadius) * Math.cos(nowAngler)) + myCanvasAni.centerx,
                            (float) ((chartOutsideRadius) * Math.sin(nowAngler)) + myCanvasAni.centery,
                            myAnimationS.paint3);
                    nowAngler = (float) (myAnimationS.nowAngle1End * Math.PI / 180);
                    myCanvasAni.canvas.drawLine(myCanvasAni.centerx, myCanvasAni.centery,
                            (float) ((chartOutsideRadius) * Math.cos(nowAngler)) + myCanvasAni.centerx,
                            (float) ((chartOutsideRadius) * Math.sin(nowAngler)) + myCanvasAni.centery,
                            myAnimationS.paint3);
                }
                myAnimationS.paint2.setColor(selfBackgroundColor);
                myCanvasAni.canvas.drawCircle(myCanvasAni.centerx,myCanvasAni.centery,chartInsideRadius,myAnimationS.paint2);
            } else {
                setAnimation(6,null);
            }
            canvas.drawBitmap(myCanvasAni.bitmap, myCanvasAni.selfRect, chartRect, null);
            postInvalidateDelayed(EVERY_DRAW_TIME_INTERVAL);
        }else if(nowAnimation == 6){
            if(myAnimationS.timeCount < ANIMATION_6_LAST){
                myAnimationS.timeCount += EVERY_DRAW_TIME_INTERVAL;
                myAnimationS.nowAngle += myAnimationS.nowAngleSpeed;
                if(myAnimationS.nowAngle < 0){
                    myAnimationS.nowAngle = 0;
                }
                myAnimationS.paint1.setAlpha((int)myAnimationS.nowAngle);
                myCanvasAni.canvas.drawBitmap(myCanvasUlt.bitmap,myCanvasUlt.selfRect,myCanvasAni.selfRect,null);
                myCanvasAni.canvas.drawBitmap(myCanvasUltNoShadow.bitmap,myCanvasUltNoShadow.selfRect,myCanvasAni.selfRect,myAnimationS.paint1);
            }else{
                ifAnimation = false;
                nowAnimation = 0;
            }
            canvas.drawBitmap(myCanvasAni.bitmap, myCanvasAni.selfRect, chartRect, null);
            postInvalidateDelayed(EVERY_DRAW_TIME_INTERVAL);
        } else {
            ifAnimation = false;
            nowAnimation = 0;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //获取整个控件的空间大小
        viewWidth = w;
        viewHeight = h;
        viewCenterx = w / 2;
        viewCentery = h / 2;
        this.viewRect = new Rect();
        this.viewRect.left = 0;
        this.viewRect.right = w;
        this.viewRect.top = 0;
        this.viewRect.bottom = h;

        //获取统计图绘制空间
        this.chartRect = new Rect();
        this.chartRect.left = this.viewRect.left;
        this.chartRect.right = this.viewRect.right;
        this.chartRect.bottom = this.viewRect.bottom;
        this.chartRect.top = this.viewRect.top;

        float radius = Math.min(viewWidth, viewHeight);
        chartInsideRadius = (int)(radius * 0.35D) >> 1;
        chartOutsideRadius = (int)(radius * 0.8D) >> 1;
        //设置最终绘制统计图的MyCanvas
        this.myCanvasUlt = new MyCanvas();
        this.myCanvasUlt.createEmptyCanvas((int)(viewWidth),(int)(viewHeight), Bitmap.Config.ARGB_8888);
        fillRectFByMid(this.myCanvasUlt.rectFOut,this.myCanvasUlt.centerx,myCanvasUlt.centery,chartOutsideRadius,chartOutsideRadius);
        fillRectFByMid(this.myCanvasUlt.rectFIn,this.myCanvasUlt.centerx,myCanvasUlt.centery,chartInsideRadius,chartInsideRadius);
        //无阴影画布
        this.myCanvasUltNoShadow = new MyCanvas();
        this.myCanvasUltNoShadow.createEmptyCanvas((int)(viewWidth),(int)(viewHeight), Bitmap.Config.ARGB_8888);
        fillRectFByMid(this.myCanvasUltNoShadow.rectFOut,this.myCanvasUltNoShadow.centerx,myCanvasUltNoShadow.centery,chartOutsideRadius,chartOutsideRadius);
        fillRectFByMid(this.myCanvasUltNoShadow.rectFIn,this.myCanvasUltNoShadow.centerx,myCanvasUltNoShadow.centery,chartInsideRadius,chartInsideRadius);
        //第一动画画布
        this.myCanvasAni = new MyCanvas();
        this.myCanvasAni.createEmptyCanvas((int)(viewWidth),(int)(viewHeight), Bitmap.Config.ARGB_8888);
        fillRectFByMid(this.myCanvasAni.rectFOut,this.myCanvasAni.centerx,myCanvasAni.centery,chartOutsideRadius,chartOutsideRadius);
        fillRectFByMid(this.myCanvasAni.rectFIn,this.myCanvasAni.centerx,myCanvasAni.centery,chartInsideRadius,chartInsideRadius);
        //存放上一次的ult的画布
        this.myCanvasAniLast = new MyCanvas();
        this.myCanvasAniLast.createEmptyCanvas((int)(viewWidth),(int)(viewHeight), Bitmap.Config.ARGB_8888);
        fillRectFByMid(this.myCanvasAniLast.rectFOut,this.myCanvasAniLast.centerx,myCanvasAniLast.centery,chartOutsideRadius,chartOutsideRadius);
        fillRectFByMid(this.myCanvasAniLast.rectFIn,this.myCanvasAniLast.centerx,myCanvasAniLast.centery,chartInsideRadius,chartInsideRadius);
    }

    private void fillRectFByMid(RectF rectF, float centerx, float centery, float wd2, float hd2){
        rectF.left = centerx - wd2;
        rectF.right = centerx + wd2;
        rectF.top = centery - hd2;
        rectF.bottom = centery + hd2;
    }

    private void fillRectByMid(RectF rect, float centerx, float centery, float wd2, float hd2){
        rect.left = centerx - wd2;
        rect.right = centerx + wd2;
        rect.top = centery - hd2;
        rect.bottom = centery + hd2;
    }

    public void endAnimation(){
        nowAnimation = 0;
        ifAnimation = false;
    }

    static class MyBitmapS{
        public Bitmap bitmap;
        public int width;
        public int height;
        public Rect rect;

        public MyBitmapS() {
            this.bitmap = null;
            this.width = 0;
            this.height = 0;
            this.rect = new Rect();
        }
    }

    static class MyCanvas{
        public Bitmap bitmap;
        public Canvas canvas;
        public int centerx;
        public int centery;
        public Rect selfRect;
        public RectF rectFOut;
        public RectF rectFIn;


        public MyCanvas() {
            this.bitmap = null;
            this.canvas = null;
        }

        public void createEmptyCanvas(int width, int height, Bitmap.Config config){
            this.bitmap = Bitmap.createBitmap(width, height,config);
            this.canvas = new Canvas(this.bitmap);
            width = this.canvas.getWidth();
            height = this.canvas.getHeight();
            centerx = width >> 1;
            centery = height >> 1;
            this.selfRect = new Rect();
            this.selfRect.left = 0;
            this.selfRect.top = 0;
            this.selfRect.right = width;
            this.selfRect.bottom = height;
            this.rectFOut = new RectF();
            this.rectFOut.left = 0;
            this.rectFOut.top = 0;
            this.rectFOut.right = width;
            this.rectFOut.bottom = height;
            this.rectFIn = new RectF();
            this.rectFIn.left = 0;
            this.rectFIn.top = 0;
            this.rectFIn.right = width;
            this.rectFIn.bottom = height;
        }
    }

    static class MyAnimationS{
        float timeCount;
        float goBackTimeCount;
        float radiusSpeed;
        float radius;
        float nowAngle;
        float nowAngleSpeed;
        float nowAngleSpeedChange;
        float nowAngleCount;
        float nowAngle1Start;
        float nowAngle1End;
        float nowAngle1Speed;
        float nowAngle1SpeedChange;
        float nowAngle1Size;
        float nowAngle2Start;
        float nowAngle2End;
        float nowAngle2Size;
        float nowAngle2Speed;
        float nowAngle2SpeedChange;
        TallyAnalysisPC tallyAnalysisPC1;
        ArrayList<TallyAnalysisPC> tallyAnalysisPCArrayList1;
        Paint paint1,paint2,paint3;

        public MyAnimationS() {
            tallyAnalysisPC1 = null;
            tallyAnalysisPCArrayList1 = null;
            paint1 = new Paint();
            paint2 = new Paint();
            paint3 = new Paint();
        }
    }
}
