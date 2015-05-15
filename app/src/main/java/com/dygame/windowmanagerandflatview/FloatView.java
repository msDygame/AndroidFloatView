package com.dygame.windowmanagerandflatview;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.ImageView;

public class FloatView extends ImageView
{
	private float x;
    private float y;

    private float mTouchX;
    private float mTouchY;

    private float mStartX;
    private float mStartY;
    
    private OnClickListener mClickListener; 
    
    protected static String mTAG = "" ;
    protected WindowManager windowManager = null;
    protected WindowManager.LayoutParams windowManagerParams = null;
    public FloatView(Context context)
    {
        super(context);     
    }
    //UI
    public void setTag(String sTAG) { mTAG = sTAG ; } 
    public void SetWindowParams(WindowManager window) 
    { 
    	windowManager = window ;
    	windowManagerParams = new WindowManager.LayoutParams();
   	    //params.type = WindowManager.LayoutParams.TYPE_PHONE; //那麼優先級會降低一些,即拉下通知欄時,不可見
   	    //params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;//system_alert窗口可以獲得焦點，響應操作
   	    //params.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;窗口顯示的時候焦點在後面的Activity上，仍舊可以操作後面的Activity
   	    windowManagerParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
   	    windowManagerParams.format = PixelFormat.RGBA_8888;// 設置圖片格式，效果為背景透明
   	    //懸浮窗不可觸摸，不接受任何事件,同時不影響後面的事件響應。效果形同「鎖定」。
   	    windowManagerParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
   	    windowManagerParams.gravity = Gravity.LEFT | Gravity.TOP;
   	    windowManagerParams.x = 0;
   	    windowManagerParams.y = 0;
   	    windowManagerParams.width = 100 ;//WindowManager.LayoutParams.WRAP_CONTENT;
   	    windowManagerParams.height = 100; //WindowManager.LayoutParams.WRAP_CONTENT;
   	    windowManager.addView(this, windowManagerParams);    	 
    }
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        Rect frame = new Rect();
        getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        Log.i(mTAG, "statusBarHeight:" + statusBarHeight);

        x = event.getRawX();
        y = event.getRawY() - statusBarHeight;
        Log.i(mTAG, "currX" + x + "====currY" + y);

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                mTouchX = event.getX();
                mTouchY = event.getY();
                mStartX = x;
                mStartY = y;
                Log.i(mTAG, "startX" + mTouchX + "====startY" + mTouchY);
                break;
            case MotionEvent.ACTION_MOVE:
                updateViewPosition();
                break;
            case MotionEvent.ACTION_UP:
                updateViewPosition();
                mTouchX = mTouchY = 0;
                if ((x - mStartX) < 5 && (y - mStartY) < 5)
                {
                    if(mClickListener!=null)
                    {
                        mClickListener.onClick(this);
                    }
                }
                break;
        }
        return false;
    }
    
    @Override
    public void setOnClickListener(OnClickListener listener)
    {
    	mClickListener = listener ;    	
    }

    private void updateViewPosition()
    {
        windowManagerParams.x = (int) (x - mTouchX);
        windowManagerParams.y = (int) (y - mTouchY);
        windowManager.updateViewLayout(this, windowManagerParams);
    }
}
