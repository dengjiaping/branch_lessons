package com.yidiankeyan.science.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by 北哥 on 2016/5/30.
 */
public class ShowAllListView extends ListView {
    public ShowAllListView(Context context) {
        super(context);
    }

    public ShowAllListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ShowAllListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
