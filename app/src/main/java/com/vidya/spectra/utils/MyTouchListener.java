package com.vidya.spectra.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class MyTouchListener implements RecyclerView.OnItemTouchListener {



    private final OnTouchActionListener mOnTouchActionListener;
    private final GestureDetectorCompat mGestureDetector;

    public interface OnTouchActionListener {
        void onClick(View view, int position);
    }

    public MyTouchListener(Context context, final RecyclerView recyclerView,
                           OnTouchActionListener onTouchActionListener){

        mOnTouchActionListener = onTouchActionListener;
        mGestureDetector = new GestureDetectorCompat(context,new GestureDetector.SimpleOnGestureListener(){

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                // Find the item view that was swiped based on the coordinates
                View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (child!=null){
                int childPosition = recyclerView.getChildPosition(child);
                mOnTouchActionListener.onClick(child, childPosition);
                }
                return false;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
        mGestureDetector.onTouchEvent(e);
        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        // do nothing
    }

}