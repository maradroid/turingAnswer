package com.maradroid.turinganswer.ListenerManager;

import android.util.Log;

import com.maradroid.turinganswer.Adapter.DialogAdapter;
import com.maradroid.turinganswer.ListenerManager.Listeners.SimulationListener;

/**
 * Created by mara on 4/6/16.
 */
abstract public class ListenerManager {

    private static DialogAdapter.DialogDataInterface dataInterface;
    private static SimulationListener simulationListener;

    public static void setDataInterface(DialogAdapter.DialogDataInterface di) {
        ListenerManager.dataInterface = di;
    }

    public static DialogAdapter.DialogDataInterface getDataInterface() {
        return dataInterface;
    }

    public static SimulationListener getSimulationListener() {
        return simulationListener;
    }

    public static void setSimulationListener(SimulationListener simulationListener) {
        ListenerManager.simulationListener = simulationListener;
    }

    public static void removeSimulationListener() {
        ListenerManager.simulationListener = null;
    }
}
