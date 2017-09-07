package com.example.liujianglong.testapp;

import android.content.Context;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * Created by liujianglong on 2017/9/6.
 */

public class ScrollItemView extends ViewGroup {

    private Scroller mScroller;
    private int mTouchSlop;
    private float mXDown;
    private float mXMove;
    private float mXLastMove;
    private int mMaxDistance;

    public ScrollItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
        ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledTouchSlop();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            int childCount = getChildCount();
            if (childCount != 2) {
                throw new IllegalStateException("child count must be exactly 2");
            }
            View first = getChildAt(0);
            View second = getChildAt(1);
            int firstWidth = first.getMeasuredWidth();
            int firstHeight = first.getMeasuredHeight();
            int secondWidth = second.getMeasuredWidth();
            int secondHeight = second.getMeasuredHeight();
            first.layout(0, 0, firstWidth, firstHeight);
            second.layout(firstWidth, 0, firstWidth + secondWidth, secondHeight);
            mMaxDistance = secondWidth;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mXDown = ev.getRawX();
                mXLastMove = mXDown;
                break;
            case MotionEvent.ACTION_MOVE:
                mXMove = ev.getRawX();
                float diff = Math.abs(mXMove - mXDown);
                mXLastMove = mXMove;
                if (diff > mTouchSlop) {
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean result = super.dispatchTouchEvent(ev);
        if (result) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_MOVE:
                mXMove = event.getRawX();
                int scrolledX = (int) (mXLastMove - mXMove);
                if (getScrollX() + scrolledX <= 0) {
                    scrollTo(0, 0);
                    return true;
                } else if (getScrollX() + scrolledX >= mMaxDistance) {
                    scrollTo(mMaxDistance, 0);
                    return true;
                }
                scrollBy(scrolledX, 0);
                mXLastMove = mXMove;
                break;
            case MotionEvent.ACTION_UP:
                int scrollX = getScrollX();
                if (getScrollX() > mMaxDistance / 3) {
                    int delta = mMaxDistance - scrollX;
                    mScroller.startScroll(scrollX, 0, delta, 0, 300);
                } else {
                    int delta = 0 - scrollX;
                    mScroller.startScroll(scrollX, 0, delta, 0, 300);
                }
                invalidate();
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }
}
