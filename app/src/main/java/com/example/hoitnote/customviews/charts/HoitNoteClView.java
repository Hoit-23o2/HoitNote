package com.example.hoitnote.customviews.charts;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Shader;
import android.icu.util.Calendar;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.hoitnote.R;
import com.example.hoitnote.models.charts.TallyAnalysisCl;
import com.example.hoitnote.models.charts.TallyAnalysisClTimeDivision;
import com.example.hoitnote.utils.managers.ChartAnalysisManager;

import java.util.ArrayList;

public class HoitNoteClView extends androidx.appcompat.widget.AppCompatImageView {

    private Context mContext;
    private ChartAnalysisManager chartAnalysisManager;

    //控制相关
    public boolean ifAct;      //是否激活，未激活下绘制基础统计图，不接受任何按键信息，最高优先级

    //视图大小相关
    int viewWidth,viewHeight;
    int viewCenterx,viewCentery;
    Rect viewRect;

    //统计图相关
    private static final int EVERY_DRAW_TIME_INTERVAL = 15;      //每次绘画时间间隔
    private static final int Chart_Padding_Width = 70;        //统计图左右padding
    private static final int Chart_Padding_Height = 100;       //统计图上下padding
    private static final int Min_Span = 10;

    ArrayList<TallyAnalysisCl> tallyAnalysisClArrayList;        //外部传来的数据List
    String nowTimeDivision;                                     //当前以何种时间跨度统计
    int nowSpan;                    //数据之间的间隔，即一个点与另一个点之间的水平距离，限制在MyCanvas.RectIn的宽度内
    ArrayList<TallyAnalysisClTimeDivision> nowDrawList;       //当前绘制

    Rect chartRect;                 //绘制统计图的外框
    float chartOutsideRadius;       //绘制统计图的外半径
    float chartInsideRadius;        //绘制统计图的内半径
    MyCanvas myCanvasUlt;           //用于绘制最终统计图
    MyCanvas myCanvasInsideChart;   //用于绘制折线统计图内部矩形中的内容
    boolean ifDrawnUlt;             //是否已经绘制了最终图
    //绘图相关
    private static final float Chart_Dot_OutsideRadius = 20;
    private static final float Chart_Dot_InsideRadius = 10;
    private static final float Chart_Line_Width = 6;
    Paint paint = new Paint();
    Paint paintText = new Paint();
    Paint paintLine = new Paint();

    Rect viewPort;                  //视口
    Rect allImage;                  //整个图片窗口的应有大小
    //事件信息相关
    boolean ifMove;
    boolean ifShowLine;
    float lastDis;
    float DownX,DownY;
    long currentMS;

    //动画相关
    private static final float ANIMATION_1_LAST = 1000;       //第一动画持续时间

    private static final float ANIMATION_2_LAST = 500;         //第二动画持续时间
    private static final float ANIMATION_2_TRANS_LENGTH_P = 0.8f;   //第二动画透明过度长度百分比
    private static final float ANIMATION_2_TRANS_NUM = 10;      //第二动画透明过度段数
    MyCanvas myCanvasAni;           //动画画布
    private MyAnimationS myAnimationS;
    private int nowAnimation;
    //0：无动画
    //1：绘制横轴和纵轴，如果检测到有内容，自动跳转到2
    //2：绘制内容
    public boolean ifAnimation;     //是否正在绘制动画

    private int selfTextColor;
    private int selfLineColor;
    private int selfBackgroundColor;
    private int selfInsideBackgroundColor;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public HoitNoteClView(@NonNull Context context) {
        super(context);
        mContext = context;
        initializeMyImageView(null);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public HoitNoteClView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initializeMyImageView(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public HoitNoteClView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initializeMyImageView(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void initializeMyImageView(@Nullable AttributeSet attrs){
        ifAct = false;
        nowDrawList = new ArrayList<>();
        allImage = new Rect();
        viewPort = new Rect();
        myAnimationS = new MyAnimationS();

        TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.HoitNoteClView);

        /*selfBackgroundColor = Color.rgb(255,255,255);
        selfCuttingLineColor = Color.rgb(255,255,255);*/
        selfTextColor =  ta.getColor(R.styleable.HoitNoteClView_clTextColor,
                Color.BLACK);
        selfLineColor =  ta.getColor(R.styleable.HoitNoteClView_clGridColor,
                Color.GRAY);
        selfBackgroundColor = ta.getColor(R.styleable.HoitNoteClView_clBackgroundColor,
                Color.WHITE);
        selfInsideBackgroundColor = ta.getColor(R.styleable.HoitNoteClView_clBackgroundInsideColor,
                Color.WHITE);
        ta.recycle();
        /*selfTextColor = Color.rgb(80,80,80);
        selfLineColor = Color.rgb(150,150,150);
        selfBackgroundColor = Color.rgb(255,255,255);
        selfInsideBackgroundColor = Color.rgb(255,255,255);*/

        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    public void setSelfTextColor(int selfTextColor) {
        this.selfTextColor = selfTextColor;
    }

    public void setSelfLineColor(int selfLineColor) {
        this.selfLineColor = selfLineColor;
    }

    public void setSelfBackgroundColor(int selfBackgroundColor) {
        this.selfBackgroundColor = selfBackgroundColor;
    }

    public void setSelfInsideBackgroundColor(int selfInsideBackgroundColor) {
        this.selfInsideBackgroundColor = selfInsideBackgroundColor;
    }

    public void actSelf(){
        ifAct = true;
        ifDrawnUlt = false;
        ifMove = false;
        ifShowLine = true;
        setAnimation(1);
        postInvalidateDelayed(EVERY_DRAW_TIME_INTERVAL);
    }

    public void setTallyAnalysisClArrayList(ArrayList<TallyAnalysisCl> tallyAnalysisClArrayList) {
        //获取数据列表
        this.tallyAnalysisClArrayList = tallyAnalysisClArrayList;

        //设置时间分隔
        nowTimeDivision = "日";
        ifShowLine = true;
        setAnimation(1);
        reDraw();
    }

    public void setManager(ChartAnalysisManager chartAnalysisManager){
        this.chartAnalysisManager = chartAnalysisManager;
    }

    public void changeTimeDivision(String newTimeDivision){
        nowTimeDivision = newTimeDivision;
        reDraw();
    }

    public void changeMainSign(TallyAnalysisCl tallyAnalysisCl){
        TallyAnalysisClTimeDivision tallyAnalysisClTimeDivision;
        int len = tallyAnalysisCl.tallyAnalysisClTimeDivisionArrayList.size();
        int i;
        tallyAnalysisClArrayList.remove(tallyAnalysisCl);
        tallyAnalysisClArrayList.add(tallyAnalysisCl);
        for(i = 0; i < len;i++){
            tallyAnalysisClTimeDivision = tallyAnalysisCl.tallyAnalysisClTimeDivisionArrayList.get(i);
            if(tallyAnalysisClTimeDivision.divisionName.equals(nowTimeDivision)){
                nowDrawList.remove(tallyAnalysisClTimeDivision);
                nowDrawList.add(tallyAnalysisClTimeDivision);
                ifDrawnUlt = false;
                postInvalidateDelayed(EVERY_DRAW_TIME_INTERVAL);
                break;
            }
        }
    }

    public void reDraw(){
//        ifAnimation = true;
        nowDrawList.clear();
        ifDrawnUlt = false;
        int len = tallyAnalysisClArrayList.size();
        int i;
        for(i = 0;i < len;i++){
            TallyAnalysisCl tallyAnalysisCl = tallyAnalysisClArrayList.get(i);
            int len2 = tallyAnalysisCl.tallyAnalysisClTimeDivisionArrayList.size();
            int j;
            for(j = 0;j < len2; j++){
                TallyAnalysisClTimeDivision tallyAnalysisClTimeDivision = tallyAnalysisCl.tallyAnalysisClTimeDivisionArrayList.get(j);
                if(tallyAnalysisClTimeDivision.divisionName.equals(nowTimeDivision)){
                    nowDrawList.add(tallyAnalysisClTimeDivision);
                    break;
                }
            }
        }
        initializeViewPort();
        initializeAllImage();
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
        int width,height;
        int x, y;
        int red,blue,green,pixel;
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

    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(ifAct){
            if(!ifAnimation && nowDrawList != null && nowDrawList.size() != 0 && tallyAnalysisClArrayList != null) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        DownX = event.getX();//float DownX
                        DownY = event.getY();//float DownY
                        currentMS = System.currentTimeMillis();//long currentMS 获取du系统时间
                        ifMove = false;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        ifMove = true;
                        if (event.getPointerCount() == 2) {
                            float x = event.getX(0) - event.getX(1);
                            float value = (float) Math.sqrt(x * x);
                            if(value != lastDis){
                                nowSpan += (value - lastDis);
                                if(nowSpan > viewPort.right - viewPort.left){
                                    nowSpan = viewPort.right - viewPort.left - 1;
                                }else if(nowSpan < Min_Span){
                                    nowSpan = Min_Span;
                                }
                                lastDis = value;
                                initializeAllImage();
                                int moveX;
                                if(viewPort.right > allImage.right){
                                    moveX = allImage.right - viewPort.right;
                                    moveRect(viewPort,moveX,0);
                                }
                                if(viewPort.left < allImage.left){
                                    moveX = allImage.left - viewPort.left;
                                    moveRect(viewPort,moveX,0);
                                }
                                ifDrawnUlt = false;
                                postInvalidateDelayed(EVERY_DRAW_TIME_INTERVAL);
                            }
                        }else{
                            int moveX = (int)(event.getX() - DownX);//X轴距离
                            DownX = event.getX();//float DownX
                            DownY = event.getY();//float DownY
                            moveRect(viewPort,-1 * moveX,0);
                            if(viewPort.right > allImage.right){
                                moveX = allImage.right - viewPort.right;
                                moveRect(viewPort,moveX,0);
                            }
                            if(viewPort.left < allImage.left){
                                moveX = allImage.left - viewPort.left;
                                moveRect(viewPort,moveX,0);
                            }
                            ifDrawnUlt = false;
                            postInvalidateDelayed(EVERY_DRAW_TIME_INTERVAL);
                            //long moveTime = System.currentTimeMillis() - currentMS;//移动时间
                        }
                        break;

                    case MotionEvent.ACTION_UP:
                        if(!ifMove){
                            ifShowLine = !ifShowLine;
                            ifDrawnUlt = false;
                            postInvalidateDelayed(EVERY_DRAW_TIME_INTERVAL);
                        }
                        break;

                    case MotionEvent.ACTION_POINTER_DOWN:
                        if (event.getPointerCount() == 2) {
                            float x = event.getX(0) - event.getX(1);
                            lastDis = (float) Math.sqrt(x * x);// 计算两点的距离
                        }
                        break;
                }
            }else if(ifAnimation){
                nowAnimation = 0;
                ifAnimation = false;
            }
        }
        return super.onTouchEvent(event);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(ifAct){
            if(!ifDrawnUlt){
                drawUlt();
            }
            if(ifAnimation){
                showAnimation(canvas);
            }else{
                canvas.drawBitmap(myCanvasUlt.bitmap,myCanvasUlt.selfRect,chartRect,null);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void drawUlt(){
        if(nowDrawList.size() != 0){
            int len;
            int len2;
            int i,j;
            len = nowDrawList.size();
            double maxMoney = 0;
            //找最大值
            for(i = 0;i < len;i++){
                TallyAnalysisClTimeDivision tallyAnalysisClTimeDivision = nowDrawList.get(i);
                if(tallyAnalysisClTimeDivision.maxMoney > maxMoney){
                    maxMoney = tallyAnalysisClTimeDivision.maxMoney;
                }
            }
            maxMoney *= 1.05;

            paint.setStrokeWidth(Chart_Line_Width);
            paint.setAntiAlias(true);

            paintText.setColor(selfTextColor);
            paintText.setTextSize(26);

            paintLine.setColor(selfLineColor);


            //清空
            myCanvasUlt.canvas.drawColor(selfBackgroundColor);
            myCanvasInsideChart.canvas.drawColor(selfInsideBackgroundColor);

            //绘制横向条
            float pointX,pointY;
            double everyMove = maxMoney / 5;
            double nowLoca = 0;
            int onceOffset = myCanvasUlt.rectIn.left >> 2;
            for(i = 0;i < 5;i++){
                nowLoca += everyMove;
                pointY = (float)((maxMoney - nowLoca) / maxMoney
                        * (myCanvasInsideChart.selfRect.bottom - myCanvasInsideChart.selfRect.top));
                if(ifShowLine){
                    //绘制横向条
                    myCanvasInsideChart.canvas.drawLine(0,pointY,myCanvasInsideChart.selfRect.right,pointY,paintLine);
                }
                String text = (int)nowLoca+"";
                int offset = text.length() * onceOffset;
                //绘制竖轴数据
//                myCanvasUlt.canvas.drawText((int)nowLoca+"",
//                        myCanvasUlt.rectIn.left - offset,
//                        pointY + myCanvasUlt.rectIn.top + 10,paintText);
            }
            myCanvasInsideChart.canvas.drawLine(0,myCanvasInsideChart.selfRect.bottom - 1,
                    myCanvasInsideChart.selfRect.right,myCanvasInsideChart.selfRect.bottom - 1,paintLine);

            //绘制渐变色背景
            int max = 0,min = 0;
            if(ifShowLine){
                ArrayList<TallyAnalysisClTimeDivision.TimeAndMoney> timeAndMoneyArrayList = nowDrawList.get(len - 1).timeAndMoneyArrayList;
                len2 = timeAndMoneyArrayList.size();
                paint.setColor(tallyAnalysisClArrayList.get(len - 1).selfColor);
                if(len2 > 1){
                    for(j = 0; j < len2;j++) {
                        pointX = j * nowSpan;
                        if(pointX >= viewPort.left){
                            min = j - 1;
                            if(min < 0){
                                min = j;
                            }
                            break;
                        }
                    }
                    for(j = len2 - 1; j >= 0;j--) {
                        pointX = j * nowSpan;
                        if(pointX <= viewPort.right){
                            max = j + 1;
                            if(max > len2 - 1){
                                max = j;
                            }
                            break;
                        }
                    }
                    if(max > min){
                        Path path = new Path();

                        pointX = min * nowSpan - viewPort.left;
                        pointY = myCanvasInsideChart.selfRect.bottom;
                        path.moveTo(pointX,pointY);
                        pointX = max * nowSpan - viewPort.left;
                        path.lineTo(pointX,pointY);
                        for(j = max;j >= min;j--){
                            pointY = (float)((maxMoney - timeAndMoneyArrayList.get(j).Money) / maxMoney
                                    * (myCanvasInsideChart.selfRect.bottom - myCanvasInsideChart.selfRect.top));
                            pointX = j * nowSpan - viewPort.left;
                            path.lineTo(pointX,pointY);
                        }
                        path.close();

                        myCanvasInsideChart.canvas.save();
                        myCanvasInsideChart.canvas.clipPath(path);

                        Paint mPaint = new Paint();
                        mPaint.setColor(Color.BLUE);
                        mPaint.setAntiAlias(true);
                        mPaint.setStrokeWidth(3);
                        mPaint.setStyle(Paint.Style.FILL);
                        mPaint.setTextSize(20);

                        LinearGradient linearGradient = new LinearGradient(myCanvasInsideChart.selfRect.right,
                                myCanvasInsideChart.selfRect.bottom,0,0,selfInsideBackgroundColor,
                                tallyAnalysisClArrayList.get(len - 1).selfColor,
                                Shader.TileMode.CLAMP);
                        mPaint.setShader(linearGradient);
                        myCanvasInsideChart.canvas.drawRect(0,0,myCanvasInsideChart.selfRect.right,
                                myCanvasInsideChart.selfRect.bottom,mPaint);

                        myCanvasInsideChart.canvas.restore();
                    }
                }
            }

            //绘制线，底部标签
            boolean ifDrawTime = true;
            for(i = 0; i < len; i++){
                ArrayList<TallyAnalysisClTimeDivision.TimeAndMoney> timeAndMoneyArrayList = nowDrawList.get(i).timeAndMoneyArrayList;
                len2 = timeAndMoneyArrayList.size();
                paint.setColor(tallyAnalysisClArrayList.get(i).selfColor);
                for(j = 0; j < len2;j++){
                    pointX = j * nowSpan;
                    if(pointX >= viewPort.left && pointX <= viewPort.right){
                        pointY = (float)((maxMoney - timeAndMoneyArrayList.get(j).Money) / maxMoney
                                * (myCanvasInsideChart.selfRect.bottom - myCanvasInsideChart.selfRect.top));
                        pointX = pointX - viewPort.left;

                        //显示文字,同时绘制背景竖轴
                        if(ifDrawTime){
                            myCanvasUlt.canvas.drawText(timeAndMoneyArrayList.get(j).timeName,
                                    pointX - 20 + (myCanvasUlt.rectIn.left),
                                    myCanvasUlt.rectIn.bottom + 40,
                                    paintText);

                        }
                        //绘制连线
                        if(j > 0){
                            float lastX,lastY;
                            lastY = (float)((maxMoney - timeAndMoneyArrayList.get(j - 1).Money) / maxMoney
                                    * (myCanvasInsideChart.selfRect.bottom - myCanvasInsideChart.selfRect.top));
                            lastX = (j - 1) * nowSpan - viewPort.left;
                            myCanvasInsideChart.canvas.drawLine(lastX,lastY,pointX,pointY,paint);
                        }
                        if(j < len2 - 1){
                            float BeforeX,BeforeY;
                            BeforeY = (float)((maxMoney - timeAndMoneyArrayList.get(j + 1).Money) / maxMoney
                                    * (myCanvasInsideChart.selfRect.bottom - myCanvasInsideChart.selfRect.top));
                            BeforeX = (j + 1) * nowSpan - viewPort.left;
                            myCanvasInsideChart.canvas.drawLine(pointX,pointY,BeforeX,BeforeY,paint);
                        }
                    }
                }
                ifDrawTime = false;
            }
            //绘制点，具体金额
            for(i = 0; i < len; i++){
                ArrayList<TallyAnalysisClTimeDivision.TimeAndMoney> timeAndMoneyArrayList = nowDrawList.get(i).timeAndMoneyArrayList;
                len2 = timeAndMoneyArrayList.size();
                paint.setColor(tallyAnalysisClArrayList.get(i).selfColor);
                for(j = 0; j < len2;j++){
                    pointX = j * nowSpan;
                    if(pointX >= viewPort.left && pointX <= viewPort.right){
                        pointY = (float)((maxMoney - timeAndMoneyArrayList.get(j).Money) / maxMoney
                                * (myCanvasInsideChart.selfRect.bottom - myCanvasInsideChart.selfRect.top));
                        pointX = pointX - viewPort.left;
                        //画竖轴
                        if(ifShowLine && i == len - 1){
                            paint.setStrokeWidth(3);
                            myCanvasInsideChart.canvas.drawLine(pointX,myCanvasInsideChart.selfRect.bottom,pointX,pointY,paint);
                            paint.setStrokeWidth(Chart_Line_Width);
                        }
                        //绘画一个点
                        paintText.setColor(selfInsideBackgroundColor);
                        myCanvasInsideChart.canvas.drawCircle(pointX,pointY,Chart_Dot_OutsideRadius,paint);
                        myCanvasInsideChart.canvas.drawCircle(pointX,pointY,Chart_Dot_InsideRadius,paintText);
                        paintText.setColor(selfTextColor);

                        //绘制当前显示标号对应的钱
                        if(ifShowLine && i == len - 1){
                            myCanvasInsideChart.canvas.drawText((int)timeAndMoneyArrayList.get(j).Money+"",
                                    pointX + Chart_Dot_OutsideRadius,
                                    pointY,
                                    paintText);
                        }
                    }
                }
            }
            myCanvasUlt.canvas.drawBitmap(myCanvasInsideChart.bitmap,myCanvasInsideChart.selfRect,myCanvasUlt.rectIn,null);
            //myCanvasUlt.canvas.drawLine(myCanvasUlt.rectIn.left,myCanvasUlt.rectIn.top,myCanvasUlt.rectIn.left,myCanvasUlt.rectIn.bottom,paintLine);
            myCanvasUlt.canvas.drawLine(myCanvasUlt.rectIn.left,myCanvasUlt.rectIn.bottom,myCanvasUlt.rectIn.right,myCanvasUlt.rectIn.bottom,paintLine);
        }else{
            Paint paintLine = new Paint();
            paintLine.setColor(selfLineColor);
            myCanvasUlt.canvas.drawColor(selfBackgroundColor);
            //myCanvasUlt.canvas.drawLine(myCanvasUlt.rectIn.left,myCanvasUlt.rectIn.top,myCanvasUlt.rectIn.left,myCanvasUlt.rectIn.bottom,paintLine);
            myCanvasUlt.canvas.drawLine(myCanvasUlt.rectIn.left,myCanvasUlt.rectIn.bottom,myCanvasUlt.rectIn.right,myCanvasUlt.rectIn.bottom,paintLine);
//
//            float everyChange = (myCanvasUlt.rectIn.bottom - myCanvasUlt.rectIn.top) / 5.0f;
//            float nowLoca = myCanvasUlt.rectIn.top;
//            int count;
//            for(count = 0; count < 5; count++){
//                nowLoca += everyChange;
//                myCanvasUlt.canvas.drawLine(myCanvasUlt.rectIn.left,nowLoca,myCanvasUlt.rectIn.right,nowLoca,paintLine);
//            }
        }
        ifDrawnUlt = true;
    }

    private void setAnimation(int animation){
        nowAnimation = animation;
        ifAnimation = true;
        if(animation == 1){
            myAnimationS.ifInitial = true;
            myAnimationS.paint1.setColor(selfLineColor);
        }else if(animation == 2){
            myAnimationS.XAxisEnd = myCanvasAni.selfRect.right + ANIMATION_2_TRANS_LENGTH_P * (myCanvasAni.selfRect.right);
            myAnimationS.XAxis = 0;
            myAnimationS.XAxisSpeed = myCanvasAni.selfRect.right / (ANIMATION_2_LAST / 1000.0f);
            myAnimationS.paint1.setColor(selfBackgroundColor);
            myAnimationS.paint2.setColor(selfLineColor);
        }
    }

    private void showAnimation(Canvas canvas){
        if(nowAnimation == 1) {
            if (myAnimationS.ifInitial) {
                myAnimationS.YAxis = myCanvasAni.rectIn.top;
                myAnimationS.XAxis = myCanvasAni.rectIn.left;
                myAnimationS.YAxisSpeed = ((myCanvasAni.rectIn.bottom - myCanvasAni.rectIn.top) + (myCanvasAni.rectIn.right - myCanvasAni.rectIn.left)) / (ANIMATION_1_LAST / 1000.0f);
                myAnimationS.XAxisSpeed = myAnimationS.YAxisSpeed;
                myAnimationS.ifInitial = false;
                myCanvasAni.canvas.drawColor(selfBackgroundColor);
            }
            if (myAnimationS.XAxis < myCanvasAni.rectIn.right) {
                float lastY, lastX;
                float time = EVERY_DRAW_TIME_INTERVAL / 1000.0f;
//                if (myAnimationS.YAxis < myCanvasAni.rectIn.bottom) {
//                    lastY = myAnimationS.YAxis;
//                    myAnimationS.YAxis += myAnimationS.YAxisSpeed * time;
//                    if (myAnimationS.YAxis > myCanvasAni.rectIn.bottom) {
//                        myAnimationS.YAxis = myCanvasAni.rectIn.bottom;
//                    }
//                    float tmpX = myCanvasAni.rectIn.left;
//                    myCanvasAni.canvas.drawLine(tmpX, lastY, tmpX, myAnimationS.YAxis, myAnimationS.paint1);
//                } else {
//                    lastX = myAnimationS.XAxis;
//                    myAnimationS.XAxis += myAnimationS.XAxisSpeed * time;
//                    if (myAnimationS.XAxis > myCanvasAni.rectIn.right) {
//                        myAnimationS.XAxis = myCanvasAni.rectIn.right;
//                    }
//                    float tmpY = myCanvasAni.rectIn.bottom;
//                    myCanvasAni.canvas.drawLine(lastX, tmpY, myAnimationS.XAxis, tmpY, myAnimationS.paint1);
//                }
                lastX = myAnimationS.XAxis;
                myAnimationS.XAxis += myAnimationS.XAxisSpeed * time;
                if (myAnimationS.XAxis > myCanvasAni.rectIn.right) {
                    myAnimationS.XAxis = myCanvasAni.rectIn.right;
                }
                float tmpY = myCanvasAni.rectIn.bottom;
                myCanvasAni.canvas.drawLine(lastX, tmpY, myAnimationS.XAxis, tmpY, myAnimationS.paint1);
            } else if (nowDrawList.size() != 0) {
                setAnimation(2);
            } else {
                ifAnimation = false;
                nowAnimation = 0;
            }
            canvas.drawBitmap(myCanvasAni.bitmap, myCanvasAni.selfRect, myCanvasUlt.selfRect, null);
            postInvalidateDelayed(EVERY_DRAW_TIME_INTERVAL);
        }else if(nowAnimation == 2){
            myCanvasAni.canvas.drawBitmap(myCanvasUlt.bitmap,myCanvasUlt.selfRect,myCanvasAni.selfRect,null);
            if(myAnimationS.XAxis < myAnimationS.XAxisEnd){
                float time = EVERY_DRAW_TIME_INTERVAL / 1000.0f;
                myAnimationS.XAxis += myAnimationS.XAxisSpeed * time;
                if(myAnimationS.XAxis > myAnimationS.XAxisEnd){
                    myAnimationS.XAxis = myAnimationS.XAxisEnd;
                }
                float everyTransLength = ANIMATION_2_TRANS_LENGTH_P * (myCanvasAni.selfRect.right) / ANIMATION_2_TRANS_NUM;
                int everyTransChange = (int)(255 / ANIMATION_2_TRANS_NUM);
                myAnimationS.paint1.setAlpha(255);
                myCanvasAni.canvas.drawRect(myAnimationS.XAxis,myCanvasAni.selfRect.top,myAnimationS.XAxisEnd,myCanvasAni.selfRect.bottom,myAnimationS.paint1);
                for(int i = 0; i < ANIMATION_2_TRANS_NUM;i++){
                    myAnimationS.paint1.setAlpha(255 - everyTransChange * i);
                    float startX = myAnimationS.XAxis - i * everyTransLength;
                    myCanvasAni.canvas.drawRect(startX,
                            myCanvasAni.selfRect.top,
                            startX - everyTransLength,
                            myCanvasAni.selfRect.bottom,
                            myAnimationS.paint1);
                }
                //myCanvasAni.canvas.drawLine(myCanvasAni.rectIn.left,myCanvasAni.rectIn.top,myCanvasAni.rectIn.left,myCanvasAni.rectIn.bottom,myAnimationS.paint2);
                myCanvasAni.canvas.drawLine(myCanvasAni.rectIn.left,myCanvasAni.rectIn.bottom,myCanvasAni.rectIn.right,myCanvasAni.rectIn.bottom,myAnimationS.paint2);
            }else{
                nowAnimation = 0;
                ifAnimation = false;
            }
            canvas.drawBitmap(myCanvasAni.bitmap, myCanvasAni.selfRect, myCanvasUlt.selfRect, null);
            postInvalidateDelayed(EVERY_DRAW_TIME_INTERVAL);
        }else{
            nowAnimation = 0;
            ifAnimation = false;
        }
    }

    public void endAnimation(){
        nowAnimation = 0;
        ifAnimation = false;
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
        this.myCanvasUlt.createEmptyCanvas((chartRect.right - chartRect.left),(chartRect.bottom - chartRect.top), Bitmap.Config.ARGB_8888);
        this.myCanvasInsideChart = new MyCanvas();
        this.myCanvasInsideChart.createEmptyCanvas((myCanvasUlt.rectIn.right - myCanvasUlt.rectIn.left),
                (myCanvasUlt.rectIn.bottom - myCanvasUlt.rectIn.top), Bitmap.Config.ARGB_8888);
        //初始化动画画布
        this.myCanvasAni = new MyCanvas();
        this.myCanvasAni.createEmptyCanvas((chartRect.right - chartRect.left),(chartRect.bottom - chartRect.top), Bitmap.Config.ARGB_8888);
        initializeViewPort();
        initializeAllImage();
    }

    private void initializeViewPort(){
        if(myCanvasUlt != null){
            viewPort.top = 0;
            viewPort.bottom = myCanvasUlt.rectIn.bottom - myCanvasUlt.rectIn.top;
            viewPort.left = 0;
            viewPort.right = myCanvasUlt.rectIn.right - myCanvasUlt.rectIn.left;
            nowSpan = (viewPort.right - viewPort.left) / 3 - 10;
            moveRect(viewPort,-nowSpan,0);
        }
    }

    private void initializeAllImage(){
        int len = nowDrawList.size();
        int i;
        int allCount = 0;
        int len2 = 1;
        for(i = 0; i < len;i++){
            len2 = nowDrawList.get(i).timeAndMoneyArrayList.size();
            if(len2 > allCount){
                allCount = len2;
            }
        }
        allImage.top = viewPort.top;
        allImage.bottom = viewPort.bottom;
        allImage.left =  -(nowSpan);
        allImage.right = (len2 - 1) * nowSpan + (nowSpan);
    }


    private void moveRect(Rect rect, int changex, int changey){
        rect.left += changex;
        rect.right += changex;
        rect.top += changey;
        rect.bottom += changey;
    }

    public ChartAnalysisManager getChartAnalysisManager() {
        return chartAnalysisManager;
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
        public Rect selfRect;       //指明自身的大小
        public Rect rectIn;         //指明统计图的内容区域

        public MyCanvas() {
            this.bitmap = null;
            this.canvas = null;
        }

        public void createEmptyCanvas(int width, int height, Bitmap.Config config){
            this.bitmap = Bitmap.createBitmap(width, height,config);
            this.canvas = new Canvas(this.bitmap);
            width = this.canvas.getWidth();
            height = this.canvas.getHeight();
            this.selfRect = new Rect();
            this.selfRect.left = 0;
            this.selfRect.top = 0;
            this.selfRect.right = width;
            this.selfRect.bottom = height;
            this.rectIn = new Rect();
            this.rectIn.top = Chart_Padding_Width;
            this.rectIn.left = Chart_Padding_Height;
            this.rectIn.right = width - Chart_Padding_Width;
            this.rectIn.bottom = height - Chart_Padding_Height;
        }
    }

    static class MyAnimationS{
        public boolean ifInitial;
        public float XAxis,YAxis;
        public float XAxisEnd;
        public float XAxisSpeed,YAxisSpeed;
        public Paint paint1,paint2;

        public MyAnimationS() {
            paint1 = new Paint();
            paint2 = new Paint();
        }
    }
}
