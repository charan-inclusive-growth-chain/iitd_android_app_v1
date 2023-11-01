package com.example.appv1;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import androidx.core.content.res.ResourcesCompat;
import android.widget.ProgressBar;


public class CheckmarkProgressBar extends ProgressBar {
    private int numCheckmarks = 4; // Set the number of checkmarks here
    private Drawable checkmarkDrawable;

    public CheckmarkProgressBar(Context context) {
        super(context);
        init(context);
    }

    public CheckmarkProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CheckmarkProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        checkmarkDrawable = ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_checkmark, null); // Use your checkmark icon
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        int progress = getProgress();
        int max = getMax();

        for (int i = 1; i <= numCheckmarks; i++) {
            float x = (i * width) / (numCheckmarks + 1);
            float y = height / 2;

            if (i * max / (numCheckmarks + 1) <= progress) {
                int iconWidth = checkmarkDrawable.getIntrinsicWidth();
                int iconHeight = checkmarkDrawable.getIntrinsicHeight();

                int left = (int) (x - iconWidth / 2);
                int top = (int) (y - iconHeight / 2);
                int right = left + iconWidth;
                int bottom = top + iconHeight;

                checkmarkDrawable.setBounds(left, top, right, bottom);
                checkmarkDrawable.draw(canvas);
            }
        }
    }
}
