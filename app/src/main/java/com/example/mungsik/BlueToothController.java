package com.example.mungsik;

import android.bluetooth.BluetoothSocket;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class BlueToothController {
    private static final BlueToothController mBluetoothController = new BlueToothController();

    private BluetoothAdapter mBluetoothAdapter;
    Set<BluetoothDevice> mPairedDevices;
    List<String> mListPairedDevices;
    ConnectedBluetoothThread mThreadConnectedBluetooth;
    BluetoothDevice mBluetoothDevice;
    BluetoothSocket mBluetoothSocket;

    final static UUID BT_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private BlueToothController()
    {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    //스태틱으로 블루투스 컨트롤러 클래스를 선언해줌(다른 클래스에서 객체생성 없이 선언해주기 위해)
    public static BlueToothController getBluetoothController()
    {
        return mBluetoothController;
    }

    //블루투스가 켜졌다면
    public boolean On() {
        if (mBluetoothAdapter.isEnabled()) {
            return true;
        }
        else {
            mBluetoothAdapter.enable();
            return false;
        }
    }

    //블루투스가 꺼져있다면
    public int Off() {
        if (mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.disable();
            if (isConnected()) {
                mThreadConnectedBluetooth.close();
            }
            return 0;
        } else {
            return 1;
        }
    }

    public boolean isConnected() {
        if(mThreadConnectedBluetooth != null)
            return true;

        else
            return false;
    }

    final CharSequence[] listPairedDevices() {
        if (mBluetoothAdapter.isEnabled()) {
            mPairedDevices = mBluetoothAdapter.getBondedDevices();

            if (mPairedDevices.size() > 0) {
                mListPairedDevices = new ArrayList<String>();
                for (BluetoothDevice device : mPairedDevices) {
                    mListPairedDevices.add(device.getName());
                }
                final CharSequence[] items = mListPairedDevices.toArray(new CharSequence[mListPairedDevices.size()]);
                mListPairedDevices.toArray(new CharSequence[mListPairedDevices.size()]);
                return items;
            } else {
                return null;
            }
        }
        else {
            return null;
        }
    }

    void connectSelectedDevice(String selectedDeviceName) {
        for(BluetoothDevice tempDevice : mPairedDevices) {
            if (selectedDeviceName.equals(tempDevice.getName())) {
                mBluetoothDevice = tempDevice;
                break;
            }
        }
        try {
            mBluetoothSocket = mBluetoothDevice.createRfcommSocketToServiceRecord(BT_UUID);
            mBluetoothSocket.connect();
            mThreadConnectedBluetooth = new ConnectedBluetoothThread(mBluetoothSocket);
            mThreadConnectedBluetooth.start();
        } catch (IOException e) {
            Log.i("BlueToothController::connectSelectedDevice", "Not Connect Arduino Bluetooth");
        }
    }

    void execute()
    {
        mThreadConnectedBluetooth.execute();
    }

    private class ConnectedBluetoothThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final OutputStream mmOutStream;

        public ConnectedBluetoothThread(BluetoothSocket socket) {
            mmSocket = socket;
            OutputStream tmpOut = null;

            try {
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.i("BlueToothController::ConnectedBluetoothThread", "Not Connect Bluetooth Thread");
            }

            mmOutStream = tmpOut;
        }

        public void execute() {
            try {
                mmOutStream.write(49);
            } catch (IOException e) {
                Log.i("BlueToothController::execute", "Not execute Bluetooth Thread");
            }
        }

        public void close() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.i("BlueToothController::close", "Not close Bluetooth Thread");
            }
        }
    }
}