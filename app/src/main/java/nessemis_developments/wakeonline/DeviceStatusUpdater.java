package nessemis_developments.wakeonline;

import android.os.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by 0612377574 on 3/3/2017.
 */

public class DeviceStatusUpdater extends Thread{
    MainActivity activity;

    public DeviceStatusUpdater(MainActivity activity){
        this.activity = activity;
    }

    @Override
    public void run() {
        ExecutorService executor = Executors.newCachedThreadPool();
        Callable<Boolean> callable = null;
        while(true){
            Map<Device, Future<Boolean>> futureMap = CreateFutureMap(callable, executor);
            try {
                //We now wait 5 seconds. If we did not get a response we presume the device is turned off
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
                //TODO allow sleep time to be changed
            }
            DispatchMessage(ConvertToStatus(futureMap));
        }
    }

    private void DispatchMessage(Map<Device, Boolean> status){
        Message message = new Message();

        message.obj = status;

        activity.deviceUpdateHandler.dispatchMessage(message);
    }

    private Map<Device, Future<Boolean>> CreateFutureMap(Callable callable, ExecutorService executor){
        Map<Device, Future<Boolean>> futureMap = new HashMap<>();
        for (Device d : activity.devices){
            callable = new DeviceOnlineDetector(d);
            Future<Boolean> future = executor.submit(callable);
            futureMap.put(d, future);
        }
        return futureMap;
    }

    private Map<Device, Boolean> ConvertToStatus(Map<Device, Future<Boolean>> futureMap){
        Map<Device, Boolean> status = new HashMap<Device, Boolean>();

        for (Map.Entry<Device, Future<Boolean>> entry : futureMap.entrySet()){
            if(entry.getValue().isDone())
                try {
                    status.put(entry.getKey(), entry.getValue().get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            else
                status.put(entry.getKey(), false);
        }
        return status;
    }
}
