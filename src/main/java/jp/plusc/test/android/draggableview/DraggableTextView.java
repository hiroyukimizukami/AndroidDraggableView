package jp.plusc.test.android.draggableview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * Created by majestaaa on 7/31/13.
 */
public class DraggableTextView extends TextView {
    private static final Paint BORDER;
    static {
        BORDER = new Paint();
        BORDER.setColor(Color.RED);
        BORDER.setStyle(Paint.Style.STROKE);
        BORDER.setStrokeWidth(2.f);
    }
    private GestureDetector detector = null;
    private boolean isInTouchState = false;

    public DraggableTextView(Context context) {
        this(context, null, 0);
    }

    public DraggableTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DraggableTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (isInEditMode()) {
            return ;
        }

        detector = new GestureDetector(context, new DragListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);
        final int action = event.getAction() & MotionEvent.ACTION_MASK;
        switch (action) {
            case MotionEvent.ACTION_DOWN : {
                isInTouchState = true;
                invalidate();
                break;
            }
            case MotionEvent.ACTION_UP : {
                isInTouchState = false;
                invalidate();
                break;
            }
            case MotionEvent.ACTION_CANCEL : {
                isInTouchState = false;
                invalidate();
                break;
            }
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isInTouchState) {
            canvas.drawPaint(BORDER);
        }
    }

    private class DragListener extends GestureDetector.SimpleOnGestureListener {
        private float latestDiffX = 0.f;
        private float latestDiffY = 0.f;
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            final float totalDiffX = e2.getRawX() - e1.getRawX();
            final float totalDiffY = e2.getRawY() - e1.getRawY();
            final int moveX = Math.round(totalDiffX - latestDiffX);
            final int moveY = Math.round(totalDiffY - latestDiffY);
            final int left = getLeft() + moveX;
            final int top = getTop() + moveY;
            final int right = getRight() + moveX;
            final int bottom = getBottom() + moveY;

            layout(left, top, right, bottom);
            latestDiffX = totalDiffX;
            latestDiffY = totalDiffY;

            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }
}
