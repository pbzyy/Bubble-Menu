package wp.gestures.Models;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import wp.gestures.R;
import wp.gestures.Models.BubbleCloud.CloudPosition;

/** Created by orrieshannon on 2013-08-13. */
public class Bubble extends LinearLayout{
    private CloudPosition cloudPosition;
    private BubbleCallback callback;
    private Drawable icon;
    private String hint;
    private ImageView bubbleIcon;

    public Bubble(Context context) {
        super(context);
    }

    public Bubble(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public String getHint() {
        return hint;
    }

    public interface BubbleCallback {
        void onBubbleChosen(Bubble bubble);
    }

    public void createBubble(Context context, CloudPosition cloudPosition, Drawable icon, String hint, BubbleCallback callback) {
        this.cloudPosition = cloudPosition;
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.addView(mInflater.inflate(R.layout.bubble, null));
        bubbleIcon = ((ImageView)this.findViewById(R.id.bubbleImage));
        bubbleIcon.setImageResource(R.drawable.circley);
        ((TextView)this.findViewById(R.id.hint)).setText(hint);
        this.callback = callback;
        this.icon = icon;
        this.hint = hint;
    }

    public void startAnimate(int xStart, int yStart, int maxHeight, int maxWidth) {
        xStart = xStart + ((this.bubbleIcon.getHeight() / 2) * - 1);
        yStart = yStart + ((this.bubbleIcon.getWidth() / 2) * - 1);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.leftMargin = xStart;
        layoutParams.topMargin = yStart;
        this.setLayoutParams(layoutParams);
        Log.d("Bubble", "startX: " + xStart + ", startY" + yStart);
        this.setAnimation(getTranslation(xStart, yStart));
    }

    public void stopAnimate() {
        this.clearAnimation();
        //this.setVisibility(View.GONE);
    }

    int straightDist = 200;
    int diagonalDist = 160;

    private TranslateAnimation getTranslation(int xStart, int yStart) {

        int toXDelta = 0;
        int toYDelta = 0;

        if (cloudPosition == CloudPosition.MR) {
            toXDelta = straightDist;
        } else if (cloudPosition == CloudPosition.ML) {
            toXDelta = straightDist * -1;
        } else if (cloudPosition == CloudPosition.TM) {
            toYDelta = straightDist * -1;
        } else if (cloudPosition == CloudPosition.BM) {
            toYDelta = straightDist;
        } else if (cloudPosition == CloudPosition.TL) {
            toXDelta = diagonalDist * -1;
            toYDelta = diagonalDist * -1;
        } else if (cloudPosition == CloudPosition.TR) {
            toXDelta = diagonalDist;
            toYDelta = diagonalDist * -1;
        } else if (cloudPosition == CloudPosition.BL) {
            toXDelta = diagonalDist * -1;
            toYDelta = diagonalDist;
        } else if (cloudPosition == CloudPosition.BR) {
            toXDelta = diagonalDist;
            toYDelta = diagonalDist;
        }
        TranslateAnimation anim = new TranslateAnimation(0, toXDelta, 0, toYDelta);
        anim.setDuration(50);
        anim.setFillAfter(false);
        anim.setAnimationListener(new BubbleAnimationListener(xStart + toXDelta, yStart + toYDelta));
        return anim;
    }

    private class BubbleAnimationListener implements Animation.AnimationListener {
        private int finalX;
        private int finalY;

        private BubbleAnimationListener(int finalX, int finalY) {
            this.finalX = finalX;
            this.finalY = finalY;
        }
        public void onAnimationStart(Animation animation) {}

        public void onAnimationEnd(Animation animation) {
            Bubble.this.clearAnimation();
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.leftMargin = finalX;
            layoutParams.topMargin = finalY;
            Bubble.this.setLayoutParams(layoutParams);
            Bubble.this.setVisibility(VISIBLE);

            Log.d("Bubble", "finalX: " + finalX + ", finalY" + finalY);
        }

        public void onAnimationRepeat(Animation animation) {}
    }


    public CloudPosition getCloudPosition() {
        return this.cloudPosition;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                this.callback.onBubbleChosen(this);
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        return false;
    }

}
