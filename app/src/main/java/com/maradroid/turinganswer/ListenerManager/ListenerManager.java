package com.maradroid.turinganswer.ListenerManager;

import com.maradroid.turinganswer.Dialog.RuleDialog;
import com.maradroid.turinganswer.ListenerManager.Listeners.InputSettingsListener;
import com.maradroid.turinganswer.ListenerManager.Listeners.SimulationListener;

/**
 * Created by mara on 4/6/16.
 */
abstract public class ListenerManager {

    private static RuleDialog.DialogDataInterface dataInterface;
    private static SimulationListener simulationListener;
    private static InputSettingsListener inputSettingsListener;

    public static void setDataInterface(RuleDialog.DialogDataInterface di) {
        ListenerManager.dataInterface = di;
    }

    public static RuleDialog.DialogDataInterface getDataInterface() {
        return dataInterface;
    }

    ///// SIMULATION INTERFACE

    public static SimulationListener getSimulationListener() {
        return simulationListener;
    }

    public static void setSimulationListener(SimulationListener simulationListener) {
        ListenerManager.simulationListener = simulationListener;
    }

    public static void removeSimulationListener() {
        ListenerManager.simulationListener = null;
    }

    ///// INPUT SETTINGS LISTENER

    public static void setInputSettingsListener(InputSettingsListener listener) {
        ListenerManager.inputSettingsListener = listener;
    }

    public static InputSettingsListener getInputSettingsListener() {
        return ListenerManager.inputSettingsListener;
    }

    public static void removeInputSettingsListener() {
        ListenerManager.inputSettingsListener = null;
    }
}
