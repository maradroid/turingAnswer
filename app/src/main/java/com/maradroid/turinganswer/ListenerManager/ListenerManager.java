package com.maradroid.turinganswer.ListenerManager;

import android.util.Log;

import com.maradroid.turinganswer.Adapter.DialogAdapter;

/**
 * Created by mara on 4/6/16.
 */
abstract public class ListenerManager {

    private static DialogAdapter.DialogDataInterface dataInterface;

    public static void setDataInterface(DialogAdapter.DialogDataInterface di) {
        dataInterface = di;
    }

    public static DialogAdapter.DialogDataInterface getDataInterface() {
        return dataInterface;
    }
}
