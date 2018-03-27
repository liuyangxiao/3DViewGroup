package com.burning.foethedog;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
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

    public Rota3DSwithView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initRoat3D();
    }

    public Rota3DSwithView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initRoat3D();
    }

    public Rota3DSwithView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initRoat3D();
    }

    int childWith;

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        childWith = getChildAt(0).getMeasuredWidth();
        super.onLayout(changed, left, top, right, bottom);
    }

    //摄像机 为点光源  正真的直角  反而看起来 并不是直角
    static int rotation = 40;// 设定外角 根据需要自行设定
    int rotationX = 00;
    int index = 0;

    @Override
    protected void dispatchDraw(Canvas canvas) {
        int indexleft = getWidth() / 2;//中间显示视图 ----左边的位置
        int postTranslateX = rotationX * childWith / 2 / rotation;//设-----定边移动 距离
        //定点  又称顶点
        //  chilDrawforCamera(canvas, postTranslateX, indexleft, 3);//预绘制 的 县绘制  防止遮挡
        for (int i = 0; i < 4; i++)
            chilDrawforCamera(canvas, postTranslateX, indexleft, i);
        if (!isTouch)
            handler.sendEmptyMessageDelayed(1, 100);
    }

    private void setCameraChange(int translate, int roat, int i) {
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

    boolean isright = false;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (isTouch)
                        return;
                    if (isright)
                        rotationX++;
                    else
                        rotationX--;
                    if (Math.abs(rotationX) == rotation) {
                        rotationX = 0;
                        index = index % getChildCount();
                        if (isright)
                            index--;
                        else
                            index++;
                    }
                    Rota3DSwithView.this.invalidate();
                    break;
            }
        }
    };


    private void chilDrawforCamera(Canvas canvas, int postTranslateX, int indexleft, int i) {
        canvas.save();
        mCamera.save();
        mMaxtrix.reset();
        mCamera.translate(postTranslateX, 0, 0);
        mCamera.rotateY(rotationX);
        mCamera.translate(postTranslateX, 0, 0);
        if (postTranslateX == 0) {
            if (isright)
                setCameraChange(childWith, rotation, i);
            else
                setCameraChange(-childWith, -rotation, i);
        } else if (postTranslateX > 0) {
            setCameraChange(childWith, rotation, i);
        } else if (postTranslateX < 0) {
            setCameraChange(-childWith, -rotation, i);
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

    private int swithView(int i) {
        int k = 0;

        switch (i) {
            case 0:
                if (isright)
                    k = index - 2;
                else
                    k = index + 2;
                break;
            case 1:
                if (isright)
                    k = index + 1;
                else
                    k = index - 1;
                break;
            case 2:
                if (isright)
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

    boolean isTouch = true;
    int downX = 0;

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
    int thisindex;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getX();
                thisindex = index;
                thisRx = rotationX;
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(event.getX() - downX) > 50) {
                    return true /*onTouchEvent(event)*/;
                }
                break;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int moveX = 0;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                handler.removeCallbacksAndMessages(null);
                //     isTouch = true;
                moveX = (int) event.getX() - downX;
                int moveRx = thisRx + moveX * rotation * 2 / (getWidth() + 100);
                isright = (moveRx > 0) ? true : false;
                int addindex = moveRx / rotation;
                index = thisindex - addindex;
                rotationX = moveRx % rotation;
                System.out.println("===thisRx===" + thisRx + "===moveRx===" + moveRx + "=========addindex=" + addindex);
                Rota3DSwithView.this.invalidate();
                break;
            case MotionEvent.ACTION_UP:
                System.out.println("===ACTION_UP===");
                //  isTouch = false;
                handler.removeCallbacksAndMessages(null);
                Rota3DSwithView.this.invalidate();
                break;
        }
        return true;
    }

    public void destory() {
        handler.removeCallbacksAndMessages(null);
        handler = null;
    }
}