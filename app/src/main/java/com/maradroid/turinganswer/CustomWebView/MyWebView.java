package com.maradroid.turinganswer.CustomWebView;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * Created by mara on 8/7/16.
 */
public class MyWebView extends WebView {

    private boolean setScroll = false;

    public MyWebView(Context context) {
        super(context);
    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setScroll(boolean zoom) {
        setScroll = zoom;
    }

    @Override
    public void invalidate() {
        super.invalidate();

        if (setScroll) {
            scrollTo(getWidth()/2, 0);
            setScroll = false;
        }
    }
}
