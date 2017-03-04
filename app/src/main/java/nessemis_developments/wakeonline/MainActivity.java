package nessemis_developments.wakeonline;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Message;
import android.support.test.espresso.core.deps.guava.collect.BiMap;
import android.support.test.espresso.core.deps.guava.collect.HashBiMap;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    final String DEVICES_FILENAME = "devices";

    public Handler deviceUpdateHandler;

    private CardView cardview_devices;

    //Hasmap linking the mac addresses to a device, is synchronized
    public List<Device> devices;

    //Hasmap linking the cards to devices addresses
    private BiMap<View, Device> cards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        cardview_devices = (CardView) findViewById(R.id.cardview_devices);

        LoadDevices();

        CreateDeviceCards();

        StartDeviceStatusUpdater();
    }

    private void LoadDevices() {
        List deviceList = (List < Device >) Utility.ReadPrivateFile(DEVICES_FILENAME, this);
        if(deviceList == null)
            deviceList = new ArrayList<Device>();
        devices = Collections.synchronizedList(deviceList);
    }

    private void CreateDeviceCards() {
        cards = HashBiMap.create();

        for (Device device : devices) {
            AddCard(device);
        }
    }

    private void AddCard(Device device){
        final View card = getLayoutInflater().inflate(R.layout.card_device, cardview_devices);
        ((TextView) card.findViewById(R.id.device_name)).setText(device.getName());
        cards.put(card, device);
        card.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                WakeDevice(cards.get(v));
            }
        });

    }

    private void WakeDevice(Device d){

    }

    public void AddDevice(View v){
        View dialogLayout = getLayoutInflater().inflate(R.layout.dialog_add, null);
        final AlertDialog d =  new AlertDialog.Builder(this).create();
        d.setView(dialogLayout);
        d.setButton(DialogInterface.BUTTON_POSITIVE, "Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Device device = new Device(((EditText)d.findViewById(R.id.description)).getText().toString(),
                        1,
                        ((EditText)d.findViewById(R.id.wake_url)).getText().toString(),
                        ((EditText)d.findViewById(R.id.sleep_url)).getText().toString());
                devices.add(device);
                AddCard(device);
            }
        });
        d.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new Message());
        d.show();
    }

    private void StartDeviceStatusUpdater(){
        deviceUpdateHandler = new Handler(getMainLooper()){
            @Override
            public void handleMessage(Message inputMessage) {
                Map<Device, Boolean> deviceStatus = (Map<Device, Boolean>) inputMessage.obj;
                Map<Device, View> deviceToView = cards.inverse();

                for (Map.Entry<Device, Boolean> pair : deviceStatus.entrySet()){
                    View v = deviceToView.get(pair.getKey());
                    if(v != null){
                        if(pair.getValue()){
                            v.setBackgroundColor(getResources().getColor(R.color.onlineGreen));
                        }
                        else{
                            v.setBackgroundColor(getResources().getColor(R.color.offlineRed));
                        }

                    }
                }
            }
        };

        DeviceStatusUpdater updater = new DeviceStatusUpdater(this);
        updater.start();
    }
}
