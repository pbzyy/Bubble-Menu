package wp.gestures.Models;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import wp.gestures.Models.Bubble.BubbleCallback;
import wp.gestures.R;

import java.util.ArrayList;

/** Created by orrieshannon on 2013-08-13. */
public class BubbleCloud extends LinearLayout{
    // 8 different bubble positions, top left (TL), top middle (TM), etc.
    public enum CloudPosition {TL, TM, TR, ML, MR, BL, BM, BR}
    private ArrayList<Bubble> bubbles;
    private RelativeLayout container;
    private Context parentContext;

    public BubbleCloud(Context context) {
        super(context);
        createCloud(context);
    }

    public BubbleCloud(Context context, AttributeSet attrs) {
        super(context, attrs);
        createCloud(context);
    }

    public void createCloud(Context context) {
        if (isInEditMode()) {
            return;
        }
        bubbles = new ArrayList<Bubble>();
        this.container = (RelativeLayout)((Activity)context).getWindow().getDecorView().findViewById(R.id.main_layout);
        //this.container = (RelativeLayout)this.getRootView().findViewById(wp.gestures.R.id.main_layout);
        this.parentContext = context;
    }

    public void addBubble(CloudPosition position, Drawable icon, String hint, BubbleCallback parentCallback) {
        Bubble bubble = new Bubble(this.parentContext);
        BubbleCallback callback = new BubbleCallback() {
            public void onBubbleChosen(Bubble bubble) {
                stopAnimate();
                Toast.makeText(parentContext, bubble.getHint(), Toast.LENGTH_SHORT).show();
            }
        };
        bubble.createBubble(parentContext, position, icon, hint, callback);
        bubble.setVisibility(View.INVISIBLE);
        bubbles.add(bubble);
        container.addView(bubble);
    }

    int startX;
    int startY;
    public void startAnimate(int x, int y) {
        startX = x;
        startY = y;
        moveHandled = false;
        for(Bubble bubble : bubbles) {
            bubble.startAnimate(startX, startY, container.getHeight(), container.getWidth());
        }
    }

    public void stopAnimate() {
        for(Bubble bubble : bubbles) {
            bubble.stopAnimate();
            bubble.setVisibility(View.INVISIBLE);
        }
    }

    boolean moveHandled;
    public void propagateMoveEvent(MotionEvent ev) {
        if (Math.abs(ev.getX() - startX) < 50 && Math.abs(ev.getY() - startY) < 50) {
            return;
        }
        for(Bubble bubble : bubbles) {
            if (isPointInsideView(ev.getRawX(), ev.getRawY(), bubble) && !moveHandled) {
                bubble.dispatchTouchEvent(ev);
                moveHandled = true;
                stopAnimate();
            }
        }
    }

    public boolean isPointInsideView(float x, float y, View view) {
        if (view == null) {
            return false;
        }
        int[] location = new int[2];
        view.getLocationOnScreen(location);


        location[1] = location[1] - getTopOffset();

        int viewX = location[0];
        int viewY = location[1];

        Log.d("BubbleCloud", "x: " + x + ", y:" + y + ", xloc:" + location[0] + ", yloc: " + location[1]);
        //point is inside view bounds
        if ((x > viewX && x < (viewX + view.getWidth()))
                && (y > viewY && y < (viewY + view.getHeight()))) {
            int a = 1;
            return true;
        } return false;
    }

    private int getTopOffset() {
        // Account for status bar
        DisplayMetrics dm = parentContext.getResources().getDisplayMetrics();
        return dm.heightPixels - this.container.getMeasuredHeight();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startAnimate((int) (ev.getRawX()), (int) (ev.getRawY() - getTopOffset()));
                break;
            case MotionEvent.ACTION_MOVE:
                propagateMoveEvent(ev);
                break;
            case MotionEvent.ACTION_UP:
                stopAnimate();
                break;
            default:
                break;
        }
        return true;
    }
}
