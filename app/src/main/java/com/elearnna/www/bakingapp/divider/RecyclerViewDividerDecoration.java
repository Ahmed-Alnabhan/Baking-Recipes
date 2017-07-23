package com.elearnna.www.bakingapp.divider;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.elearnna.www.bakingapp.R;

/**
 * Created by Ahmed on 7/1/2017.
 * This divider is taken form N J's answer at https://stackoverflow.com/questions/31242812/how-can-a-divider-line-be-added-in-an-android-recyclerview
 */

public class RecyclerViewDividerDecoration extends RecyclerView.ItemDecoration {
    private Drawable mDivider;

    public RecyclerViewDividerDecoration(Context context) {
        this.mDivider = ContextCompat.getDrawable(context, R.drawable.divider);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int paddingLeft = parent.getPaddingLeft();
        int paddingRight = parent.getWidth() - parent.getPaddingRight();
        int childCount = parent.getChildCount();

        for (int i = 1; i < childCount; i++) {
            View view = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
            int paddingTop = view.getTop() + params.bottomMargin;
            int paddingBottom = paddingTop + mDivider.getIntrinsicHeight();

            mDivider.setBounds(paddingLeft, paddingTop, paddingRight, paddingBottom);
            mDivider.draw(c);
        }


    }
}
