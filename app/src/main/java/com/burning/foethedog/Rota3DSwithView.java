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
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;


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

public class Rota3DSwithView extends FrameLayout {
    Camera mCamera;
    Matrix mMaxtrix;

    public Rota3DSwithView(Context context) {
        super(context);
        initRoat3D();
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
        rotation = typedArray.getInt(R.styleable.rota3DVSwithView_rotation, 40);
        heightRatio = typedArray.getFloat(R.styleable.rota3DVSwithView_heightRatio, 0.7f);
        widthRatio = typedArray.getFloat(R.styleable.rota3DVSwithView_widthRatio, 0.7f);

    }

    private void setChildCenter() {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
            layoutParams.width = (int) (getWidth() * widthRatio);
            layoutParams.height = (int) (getWidth() * heightRatio);
            layoutParams.gravity = Gravity.CENTER;
            child.setLayoutParams(layoutParams);
            child.setClickable(true);
        }
    }

    float widthRatio = 0.7f;
    float heightRatio = 0.7f;

    public Rota3DSwithView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initRoat3DStyle(attrs);
        initRoat3D();
    }

    public Rota3DSwithView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initRoat3DStyle(attrs);
        initRoat3D();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Rota3DSwithView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initRoat3DStyle(attrs);
        initRoat3D();
    }


    //获取子View的宽或者高--作为旋转和移动依据
    int childHeight;
    int childWith;

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        childHeight = getChildAt(0).getMeasuredHeight();
        childWith = getChildAt(0).getMeasuredWidth();
        super.onLayout(changed, left, top, right, bottom);
        setChildCenter();
    }

    //摄像机 为点光源  正真的直角  反而看起来 并不是直角
    int rotation = 40;// 设定外角
    //  static int rotation = 30;// 设定外角
    int moveRotation = 00;
    int index = 0;

    private void disDrawrX(Canvas canvas) {
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
    }

    boolean rotateV = false;

    public boolean isRotateV() {
        return rotateV;
    }

    public void setRotateV(boolean rotateV) {
        this.rotateV = rotateV;
    }

    public int getmoveRotation() {
        return moveRotation;
    }

    public void setmoveRotation(int moveRotation) {
        this.moveRotation = moveRotation;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (rotateV) {
            disDrawrY(canvas);
        } else {
            disDrawrX(canvas);
        }

        if (!isTouch && isAutoscroll())
            handler.sendEmptyMessageDelayed(1, 100);
    }

    boolean autoscroll = true;

    public boolean isAutoscroll() {
        return autoscroll;
    }

    public void setAutoscroll(boolean autoscroll) {
        this.autoscroll = autoscroll;
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

    boolean isrightortop = false;
    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (isTouch || !isAutoscroll())
                        return;
                    if (isrightortop)
                        moveRotation++;
                    else
                        moveRotation--;
                    if (Math.abs(moveRotation) == rotation) {
                        moveRotation = 0;
                        int position = index % getChildCount();
                        reSetIndex(position);
                        if (isrightortop) {
                            position = index - 1;
                        } else {
                            position = index + 1;
                        }
                        reSetIndex(position);
                    }
                    Rota3DSwithView.this.invalidate();
                    break;
            }
        }
    };


    private void chilDrawforCameraY(Canvas canvas, int postTranslateX, int indexleft, int i) {
        canvas.save();
        mCamera.save();
        mMaxtrix.reset();
        mCamera.translate(0, postTranslateX, 0);
        mCamera.rotateX(moveRotation);
        mCamera.translate(0, postTranslateX, 0);
        if (postTranslateX == 0) {
            if (isrightortop)
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
        View childAt = getChildAt((swithView(i) + 2 * getChildCount()) % getChildCount());
        drawChild(canvas, childAt, 0);
        canvas.restore();
    }

    private int swithView(int i) {
        int k = 0;

        switch (i) {
            case 0:
                if (isrightortop)
                    k = index - 2;
                else
                    k = index + 2;
                break;
            case 1:
                if (isrightortop)
                    k = index + 1;
                else
                    k = index - 1;
                break;
            case 2:
                if (isrightortop)
                    k = index - 1;
                else
                    k = index + 1;
                break;
            case 3:
                k = index;
                break;
        }
        return k;
    }

    boolean isTouch = false;
    int downXorY = 0;

    public boolean dispatchTouchEvent(MotionEvent event) {
        //这里我们就 就只分发给当前index子View
        isTouch = event.getAction() == MotionEvent.ACTION_MOVE;
        if (!onInterceptTouchEvent(event)) {
            index = index % getChildCount();
            return getChildAt((index + getChildCount()) % getChildCount()).dispatchTouchEvent(event);
        }
        return super.dispatchTouchEvent(event);
    }

    int thisRx = 0;
    int showIndex;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
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
                    if (Math.abs(event.getY() - downXorY) > 50) {
                        return true /*onTouchEvent(event)*/;
                    }
                } else {
                    if (Math.abs(event.getX() - downXorY) > 50) {
                        return true /*onTouchEvent(event)*/;
                    }
                }
                break;
        }
        return false;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // int moveX = 0;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isRotateV()) {
                    downXorY = (int) event.getY();
                } else {
                    downXorY = (int) event.getX();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                handler.removeCallbacksAndMessages(null);
                ontouchView(event);
              /*  int moveRxory = 0;
                if (isRotateV()) {
                    moveX = -((int) event.getY() - downXorY);
                    moveRxory = thisRx + moveX * rotation * 2 / (getHeight() + 100);
                } else {
                    moveX = (int) event.getX() - downXorY;
                    moveRxory = thisRx + moveX * rotation * 2 / (getWidth() + 100);
                }
                isrightortop = (moveRxory > 0) ? true : false;
                int removeItem = moveRxory / rotation;
                int position = showIndex - removeItem;
                reSetIndex(position);
                //  showDataPage();
                moveRotation = moveRxory % rotation;
                this.invalidate();*/
                break;
            case MotionEvent.ACTION_UP:
                handler.removeCallbacksAndMessages(null);
                this.invalidate();
                break;
        }
        return true;
    }

    private int ontouchView(MotionEvent event) {
        int movedistance = 0;
        int moveRxory = 0;
        if (isRotateV()) {
            movedistance = -((int) event.getY() - downXorY);
            moveRxory = thisRx + movedistance * rotation * 2 / (getHeight() + 100);
        } else {
            movedistance = (int) event.getX() - downXorY;
            moveRxory = thisRx + movedistance * rotation * 2 / (getWidth() + 100);
        }

        rotaViewtangle(moveRxory);
        return movedistance;
    }

    private int rotaViewtangle(int moveRxory) {
        System.out.println("-nextPage--==2=moveRotation===" + moveRotation);
        isrightortop = (moveRxory > 0) ? true : false;
        int removeItem = moveRxory / rotation;
        int position = showIndex - removeItem;
        // int position = index - removeItem;
        reSetIndex(position);
        moveRotation = moveRxory % rotation;
        this.invalidate();
        System.out.println("-nextPage--==3=moveRotation===" + moveRotation);
        return moveRotation;
    }

    private void rotaViewtangleAnimation(int moveRxory) {
       /* int removeItem = moveRxory / rotation;
        int position = showIndex - removeItem;
        reSetIndex(position);*/
        moveRotation = moveRxory % rotation;
        this.invalidate();
    }

    int testObj = 0;

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
            if (isrightortop)
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
        View childAt = getChildAt((swithView(i) + 2 * getChildCount()) % getChildCount());
        drawChild(canvas, childAt, 0);
        canvas.restore();
    }


    public void destory() {
        handler.removeCallbacksAndMessages(null);
        handler = null;
    }

    private void reSetIndex(int position) {
        index = position;
        showDataPage(position);
/*
        if (position != index) {
            if (position < 0) {
                int rindex = position % getChildCount();
                index = rindex + getChildCount() + 1;
                showDataPage();
            } else {
                index = position % getChildCount();
                showDataPage();
            }

        }
*/
    }

    int texta = 0;

    @SuppressLint("ObjectAnimatorBinding")
    public void nextPage() {
        isrightortop = true;
        int texta = moveRotation;
        showIndex = index;
        Interpolator interpolator = new AccelerateInterpolator();
        ObjectAnimator mAnimator = ObjectAnimator.ofInt(this, "texta", moveRotation, rotation);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animatedValue = (int) animation.getAnimatedValue();
                rotaViewtangle(animatedValue);
            }
        });
        mAnimator.setInterpolator(interpolator);
        mAnimator.setDuration(200);
        mAnimator.start();
    }


    @SuppressLint("ObjectAnimatorBinding")
    public void returnPage() {
        isrightortop = false;
        int texta = moveRotation;
        showIndex = index;
        Interpolator interpolator = new AccelerateInterpolator();
        ObjectAnimator mAnimator = ObjectAnimator.ofInt(this, "texta", moveRotation, -rotation);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animatedValue = (int) animation.getAnimatedValue();
                rotaViewtangle(animatedValue);
            }
        });
        mAnimator.setInterpolator(interpolator);
        mAnimator.setDuration(200);
        mAnimator.start();
    }


    int showPage = 0;

    private void showDataPage(int position) {
        int i = position % getChildCount();
        int mathpage = 0;
        if (i > 0) {
            mathpage = i;
        } else {
            mathpage = i + 1 + getChildCount();
        }
        if (mathpage != showPage) {
            showPage = mathpage;
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
}