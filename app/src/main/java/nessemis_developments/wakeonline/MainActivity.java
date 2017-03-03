package nessemis_developments.wakeonline;

import android.annotation.SuppressLint;
import android.support.test.espresso.core.deps.guava.collect.BiMap;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    final String DEVICES_FILENAME = "devices";

    private CardView cardview_devices;

    //Hasmap linking the mac addresses to a device
    private BiMap<Integer, Device> devices;

    //Hasmap linking the cards to mac addresses
    private Map<View, Device> cards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        cardview_devices = (CardView) findViewById(R.id.cardview_devices);

        LoadDevices();

        CreateDeviceCards();
    }

    private void LoadDevices() {
        devices = (List<Device>) Utility.ReadPrivateFile(DEVICES_FILENAME, this);
    }

    public void UpdateDeviceStatusses(Map<Integer, Boolean> status){
        for (Map.Entry<Integer, Boolean> entry : status.entrySet())
            if(entry.getValue())
                cards.entrySet().
    }

    private void CreateDeviceCards() {
        LayoutInflater inflator = getLayoutInflater();

        for (Device device : devices.values()) {
            View card = inflator.inflate(R.layout.card_device, cardview_devices);
            ((TextView) card.findViewById(R.id.device_name)).setText(device.getName());
            cards.put(card, device.getmac());
        }
    }
}
