package com.burning.foethedog;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Created by burning on 2017/5/2.
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * -------------------------//┏┓　　　┏┓
 * -------------------------//┏┛┻━━━┛┻┓
 * -------------------------//┃　　　　　　　┃
 * -------------------------//┃　　　━　　　┃
 * -------------------------//┃　┳┛　┗┳　┃
 * -------------------------//┃　　　　　　　┃
 * -------------------------//┃　　　┻　　　┃
 * -------------------------//┃　　　　　　　┃
 * -------------------------//┗━┓　　　┏━┛
 * -------------------------//┃　　　┃  神兽保佑
 * -------------------------//┃　　　┃  代码无BUG！
 * -------------------------//┃　　　┗━━━┓
 * -------------------------//┃　　　　　　　┣┓
 * -------------------------//┃　　　　　　　┏┛
 * -------------------------//┗┓┓┏━┳┓┏┛
 * -------------------------// ┃┫┫　┃┫┫
 * -------------------------// ┗┻┛　┗┻┛
 */

public class Rota3DSwithViewList extends FrameLayout {
    Camera mCamera;
    Matrix mMaxtrix;

    public Rota3DSwithViewList(Context context) {
        super(context);
    }

    private void initRoat3D() {
        mCamera = new Camera();
        mMaxtrix = new Matrix();
        setWillNotDraw(false);
    }

    private void initRoat3DStyle(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.rota3DVSwithView);
        rotateV = typedArray.getBoolean(R.styleable.rota3DVSwithView_rotateV, false);
        autoscroll = typedArray.getBoolean(R.styleable.rota3DVSwithView_autoscroll, true);
        socallspeed = typedArray.getInt(R.styleable.rota3DVSwithView_speed, 80);
        autoRebackandnext = typedArray.getBoolean(R.styleable.rota3DVSwithView_autoresetpage, true);
        setAutoscroll(autoscroll);
        rotation = typedArray.getInt(R.styleable.rota3DVSwithView_rotation, 40);
        heightRatio = typedArray.getFloat(R.styleable.rota3DVSwithView_heightRatio, 0.7f);
        widthRatio = typedArray.getFloat(R.styleable.rota3DVSwithView_widthRatio, 0.7f);

    }
    public boolean isAutoRebackandnext() {
        return autoRebackandnext;
    }

    public void setAutoRebackandnext(boolean autoRebackandnext) {
        this.autoRebackandnext = autoRebackandnext;
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    float widthRatio = 0.7f;
    float heightRatio = 0.7f;

    public Rota3DSwithViewList(Context context, AttributeSet attrs) {
        super(context, attrs);
        initRoat3DStyle(attrs);
        initRoat3D();
    }

    public Rota3DSwithViewList(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initRoat3DStyle(attrs);
        initRoat3D();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Rota3DSwithViewList(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initRoat3DStyle(attrs);
        initRoat3D();
    }

    //获取子View的宽或者高--作为旋转和移动依据
    int childHeight;
    int childWith;

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int rx = (int) ((right - left) * (1 - widthRatio) / 2);
        int ry = (int) ((bottom - top) * (1 - heightRatio) / 2);
        childHeight = (int) ((bottom - top) * heightRatio);
        childWith = (int) ((right - left) * widthRatio);
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            child.layout(rx, ry, right - left - rx, bottom - top - ry);
            child.setClickable(true);
            ViewGroup.LayoutParams layoutParams = child.getLayoutParams();
            if (layoutParams.width != childWith) {
                layoutParams.width = childWith;
                layoutParams.height = childHeight;
                child.setLayoutParams(layoutParams);
            }
        }

    }

    //摄像机 为点光源  正真的直角  反而看起来 并不是直角
    int rotation = 40;// 设定外角
    int moveRotation = 00;
    int index = 0;


    private void disDrawrX(Canvas canvas) {
        int indexleft = getWidth() / 2;//中间显示视图 ----左边的位置
        int postTranslateX = moveRotation * childWith / 2 / rotation;//设-----定边移动 距离

        chilDrawforCameraX(canvas, postTranslateX, indexleft, 0);
        chilDrawforCameraX(canvas, postTranslateX, indexleft, 1);
        if (Math.abs(moveRotation) > (rotation / 2)) {
            chilDrawforCameraX(canvas, postTranslateX, indexleft, 3);
            chilDrawforCameraX(canvas, postTranslateX, indexleft, 2);
        } else {
            chilDrawforCameraX(canvas, postTranslateX, indexleft, 2);
            chilDrawforCameraX(canvas, postTranslateX, indexleft, 3);
        }
     /*   for (int i = 0; i < 4; i++)
            chilDrawforCameraX(canvas, postTranslateX, indexleft, i);*/
    }

    private void disDrawrY(Canvas canvas) {
        int indexleft = getHeight() / 2;//中间显示视图 ----左边的位置
        int postTranslateX = moveRotation * childHeight / 2 / rotation;//设-----定边移动 距离
        //定点  又称顶点
      /*  for (int i = 0; i < 4; i++) {

            chilDrawforCameraY(canvas, postTranslateX, indexleft, i);
        }*/
        chilDrawforCameraY(canvas, postTranslateX, indexleft, 0);
        chilDrawforCameraY(canvas, postTranslateX, indexleft, 1);
        if (Math.abs(moveRotation) > (rotation / 2)) {
            chilDrawforCameraY(canvas, postTranslateX, indexleft, 3);
            chilDrawforCameraY(canvas, postTranslateX, indexleft, 2);
        } else {
            chilDrawforCameraY(canvas, postTranslateX, indexleft, 2);
            chilDrawforCameraY(canvas, postTranslateX, indexleft, 3);
        }


    }


  /*  private void disDrawrX(Canvas canvas) {
        int indexleft = getWidth() / 2;//中间显示视图 ----左边的位置
        int postTranslateX = moveRotation * childWith / 2 / rotation;//设-----定边移动 距离
        for (int i = 0; i < 4; i++)
            chilDrawforCameraX(canvas, postTranslateX, indexleft, i);
    }

    private void disDrawrY(Canvas canvas) {
        int indexleft = getHeight() / 2;//中间显示视图 ----左边的位置
        int postTranslateX = moveRotation * childHeight / 2 / rotation;//设-----定边移动 距离
        //定点  又称顶点
        for (int i = 0; i < 4; i++) {
            chilDrawforCameraY(canvas, postTranslateX, indexleft, i);
        }
    }*/

    boolean rotateV = false;

    public boolean isRotateV() {
        return rotateV;
    }

    public void setRotateV(boolean rotateV) {
        this.rotateV = rotateV;
        this.invalidate();
    }

    public int getmoveRotation() {
        return moveRotation;
    }

    public void setmoveRotation(int moveRotation) {
        this.moveRotation = moveRotation;
    }

    Set<Integer> showset = new HashSet<>();

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (getChildCount() == 0) {
            return;
        }
        Set<Integer> reset = new HashSet<>();
        for (int i = 0; i < 4; i++) {
            int k = swithView(i);
            int j = swithViewAdapter(i);
            reset.add(j);
            if (!showset.contains(j)) {
                adapter.bindViewHolder(holders.get(k), j);
            }
        }
        showset.clear();
        showset.addAll(reset);
        if (rotateV) {
            disDrawrY(canvas);
        } else {
            disDrawrX(canvas);
        }
    }


    boolean autoscroll = true;

    public boolean isAutoscroll() {
        return autoscroll;
    }

    public void setAutoscroll(boolean autoscroll) {
        if (autoscroll) {
            senMessageStart();
        }
        this.autoscroll = autoscroll;
    }

    ExecutorService executorService = Executors.newSingleThreadExecutor();

    private void senMessageStart() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                while (autoscroll) {
                    try {
                        Thread.sleep(1000 / socallspeed);
                        socallAnim();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (isTouch) {
                        return;
                    }
                }
            }
        });

    }

    private void setCameraChangeY(int translate, int roat, int i) {
        switch (i) {
            case 0:
                //预绘制 的VIEW
                mCamera.translate(0, -translate / 2, 0);
                mCamera.rotateX(-roat);
                mCamera.translate(0, -translate / 2, 0);

                mCamera.translate(0, -translate / 2, 0);
                mCamera.rotateX(-roat);
                mCamera.translate(0, -translate / 2, 0);
                break;
            //当前位置两侧的View
            case 1:
                mCamera.translate(0, translate / 2, 0);
                mCamera.rotateX(roat);
                mCamera.translate(0, translate / 2, 0);
                break;

            case 2:
                mCamera.translate(0, -translate / 2, 0);
                mCamera.rotateX(-roat);
                mCamera.translate(0, -translate / 2, 0);
                break;
            //最后绘制 当前显示位置 防止 被遮挡
            case 3:
                mCamera.rotateX(0);
                break;
        }


    }

    private boolean isleftortop = false;

    public boolean isIsleftortop() {
        return isleftortop;
    }

    public void setIsleftortop(boolean isleftortop) {
        startAnimation(isleftortop);
    }


    int socallspeed = 80;

    private void socallAnim() {
        if (getChildCount() == 0) {
            return;
        }
        if (isTouch) {
            return;
        }
        if (isleftortop)
            moveRotation++;
        else
            moveRotation--;
        showIndex = index;
        rotaViewtangle(moveRotation);
        if (isAutoscroll())
            senMessageStart();
    }

    private void chilDrawforCameraY(Canvas canvas, int postTranslateX, int indexleft, int i) {
        canvas.save();
        mCamera.save();
        mMaxtrix.reset();
        mCamera.translate(0, postTranslateX, 0);
        mCamera.rotateX(moveRotation);
        mCamera.translate(0, postTranslateX, 0);
        if (postTranslateX == 0) {
            if (isleftortop)
                setCameraChangeY(childHeight, rotation, i);
            else
                setCameraChangeY(-childHeight, -rotation, i);
        } else if (postTranslateX > 0) {
            setCameraChangeY(childHeight, rotation, i);
        } else if (postTranslateX < 0) {
            setCameraChangeY(-childHeight, -rotation, i);
        }
        mCamera.getMatrix(mMaxtrix);
        mCamera.restore();
        mMaxtrix.preTranslate(-getWidth() / 2, -indexleft);//指定在 屏幕上 运行的棱 是哪一条
        mMaxtrix.postTranslate(getWidth() / 2, indexleft);//运行路径
        canvas.concat(mMaxtrix);
        //绘制
        View childAt = getChildAt(swithView(i)/*(swithView(i) + 2 * getChildCount()) % getChildCount()*/);
        if (childAt != null)
            drawChild(canvas, childAt, 0);
        canvas.restore();
    }

    private int swithView(int i) {
        int k = 0;

        switch (i) {
            case 0:
                if (isleftortop)
                    k = index - 2;
                else
                    k = index + 2;
                break;
            case 1:
                if (isleftortop)
                    k = index + 1;
                else
                    k = index - 1;
                break;
            case 2:
                if (isleftortop)
                    k = index - 1;
                else
                    k = index + 1;
                break;
            case 3:
                k = index;
                break;
        }
        int j = k % getChildCount();
        if (j >= 0) {
            return j;
        } else {
            return (j + getChildCount());
        }
//        return k;
    }

    private int swithViewAdapter(int i) {
        int k = 0;
        switch (i) {
            case 0:
                if (isleftortop)
                    k = index - 2;
                else
                    k = index + 2;
                break;
            case 1:
                if (isleftortop)
                    k = index + 1;
                else
                    k = index - 1;
                break;
            case 2:
                if (isleftortop)
                    k = index - 1;
                else
                    k = index + 1;
                break;
            case 3:
                k = index;
                break;
        }
        int j = k % getAdapterCount();
        if (j >= 0) {
            return j;
        } else {
            return (j + getAdapterCount());
        }
//        return k;
    }


    boolean isTouch = false;
    int downXorY = 0;

    public boolean dispatchTouchEvent(MotionEvent event) {
        if (getChildCount() == 0) {
            return super.dispatchTouchEvent(event);
        }
        //这里我们就 就只分发给当前index子View
        if (!onInterceptTouchEvent(event)) {
            return getChildAt(swithView(3)).dispatchTouchEvent(event);
        }
        return super.dispatchTouchEvent(event);
    }

    int thisRx = 0;
    int showIndex;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (getChildCount() == 0) {
            return super.onInterceptTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isRotateV()) {
                    downXorY = (int) event.getY();
                } else {
                    downXorY = (int) event.getX();
                }
                showIndex = index;
                thisRx = moveRotation;
                break;
            case MotionEvent.ACTION_MOVE:
                if (isRotateV()) {
                    if (Math.abs(event.getY() - downXorY) > 10) {
                        return true /*onTouchEvent(event)*/;
                    }
                } else {
                    if (Math.abs(event.getX() - downXorY) > 10) {
                        return true /*onTouchEvent(event)*/;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (autoRebackandnext && !autoscroll) {
                    //自动回调 上一个和下一个
                    setTouchUpNextView();
                }
            case MotionEvent.ACTION_CANCEL:
                return isTouch;
        }
        return super.onInterceptTouchEvent(event);
    }
    //是否自动复原
    private boolean autoRebackandnext = true;
    public void resetPageBack() {
        setTouchUpNextView();
    }
    private void setTouchUpNextView() {
        if (Math.abs(moveRotation) == rotation || moveRotation == 0) {
            return;
        }
        ObjectAnimator mAnimator;
        if (Math.abs(moveRotation) * 2 < rotation) {
            mAnimator = ObjectAnimator.ofInt(this, Name, moveRotation, 0);
        } else {
            mAnimator = ObjectAnimator.ofInt(this, Name, moveRotation, moveRotation > 0 ? rotation : -rotation);
        }
        animationStart(mAnimator);
    }

    boolean touching_auto = false;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (getChildCount() == 0) {
            return super.onTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                isTouch = true;
                ontouchView(event);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isTouch = false;
                senMessageStart();
                break;
        }
        return true;
    }

    private int ontouchView(MotionEvent event) {
        int movedistance = 0;
        int moveRxory = 0;
        if (isRotateV()) {
            movedistance = -((int) event.getY() - downXorY);
            moveRxory = thisRx + movedistance * rotation / (getHeight());
        } else {
            movedistance = (int) event.getX() - downXorY;
            moveRxory = thisRx + movedistance * rotation / (getWidth());
        }
        isleftortop = (moveRxory > 0) ? true : false;
        rotaViewtangle(moveRxory);
        return movedistance;
    }

    private int rotaViewtangle(int moveRxory) {

        int removeItem = moveRxory / rotation;
        int position = showIndex - removeItem;
        reSetIndex(position);
        moveRotation = moveRxory % rotation;
        this.invalidate();
        return moveRotation;
    }


    private void setCameraChangeX(int translate, int roat, int i) {
        switch (i) {
            case 0:
                //预绘制 的VIEW
                mCamera.translate(-translate / 2, 0, 0);
                mCamera.rotateY(-roat);
                mCamera.translate(-translate / 2, 0, 0);

                mCamera.translate(-translate / 2, 0, 0);
                mCamera.rotateY(-roat);
                mCamera.translate(-translate / 2, 0, 0);
                break;
            //当前位置两侧的View
            case 1:
                mCamera.translate(translate / 2, 0, 0);
                mCamera.rotateY(roat);
                mCamera.translate(translate / 2, 0, 0);
                break;

            case 2:
                mCamera.translate(-translate / 2, 0, 0);
                mCamera.rotateY(-roat);
                mCamera.translate(-translate / 2, 0, 0);
                break;
            //最后绘制 当前显示位置 防止 被遮挡
            case 3:
                mCamera.rotateY(0);
                break;
        }


    }

    private void chilDrawforCameraX(Canvas canvas, int postTranslateX, int indexleft, int i) {
        canvas.save();
        mCamera.save();
        mMaxtrix.reset();
        mCamera.translate(postTranslateX, 0, 0);
        mCamera.rotateY(moveRotation);
        mCamera.translate(postTranslateX, 0, 0);
        if (postTranslateX == 0) {
            if (isleftortop)
                setCameraChangeX(childWith, rotation, i);
            else
                setCameraChangeX(-childWith, -rotation, i);
        } else if (postTranslateX > 0) {
            setCameraChangeX(childWith, rotation, i);
        } else if (postTranslateX < 0) {
            setCameraChangeX(-childWith, -rotation, i);
        }
        mCamera.getMatrix(mMaxtrix);
        mCamera.restore();
        mMaxtrix.preTranslate(-indexleft, -getHeight() / 2);//指定在 屏幕上 运行的棱 是哪一条
        mMaxtrix.postTranslate(indexleft, getHeight() / 2);//运行路径
        canvas.concat(mMaxtrix);
        //绘制
        View childAt = getChildAt(swithView(i) /*( + 2 * getChildCount()) % getChildCount()*/);
        if (childAt != null)
            drawChild(canvas, childAt, 0);
        canvas.restore();
    }


    private void reSetIndex(int position) {
        showDataPage(position);
    }


    @SuppressLint("ObjectAnimatorBinding")
    public void returnPage() {
        startAnimation(true);
    }

    private boolean isAnimationStarting = false;

    public void nextPage() {
        startAnimation(false);
    }

    private void startAnimation(boolean rightortop) {
        if (isAnimationStarting) {
            return;
        }
        isAnimationStarting = true;
        isleftortop = rightortop;
        showIndex = index;
        ObjectAnimator mAnimator = ObjectAnimator.ofInt(this, Name, moveRotation, rightortop ? rotation : -rotation);
        animationStart(mAnimator);
    }
    String Name = "sfasfaxxsf";
    private void animationStart(ObjectAnimator mAnimator) {
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animatedValue = (int) animation.getAnimatedValue();
                rotaViewtangle(animatedValue);
            }
        });
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isAnimationStarting = false;
            }
        });
        Interpolator interpolator = new AccelerateInterpolator();
        mAnimator.setInterpolator(interpolator);
        mAnimator.setDuration(120);
        mAnimator.start();
    }


    int showPage = 0;

    private void showDataPage(int position) {
        index = position;
        int isSEshowpage = 0;
        if (Math.abs(moveRotation) > (rotation / 2)) {
            isSEshowpage = swithView(2);
        } else {
            isSEshowpage = swithView(3);
        }
        if (showPage != isSEshowpage) {
            showPage = isSEshowpage;
            if (r3DPagechange != null) {
                r3DPagechange.onPageChanged(showPage);
            }
        }
    }


    R3DPagechange r3DPagechange;

    public void setR3DPagechange(R3DPagechange r3DPagechange) {
        this.r3DPagechange = r3DPagechange;
    }

    public interface R3DPagechange {
        void onPageChanged(int position);
    }

    RecyclerView.Adapter adapter;
    ArrayList<RecyclerView.ViewHolder> holders = new ArrayList<>();

    public void setAdapter(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
        for (int i = 0; i < 4; i++) {
            RecyclerView.ViewHolder viewHolder = adapter.createViewHolder(this, 0);
            holders.add(viewHolder);
            addView(viewHolder.itemView);
        }
    }

    public int getAdapterCount() {
        return adapter.getItemCount();
    }
}