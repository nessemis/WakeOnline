package nessemis_developments.wakeonline;

import java.util.concurrent.Callable;

/**
 * Created by 0612377574 on 3/3/2017.
 */

public class DeviceOnlineDetector implements Callable<Boolean>{
    Device device;

    public DeviceOnlineDetector(Device device){
        this.device = device;
    }

    @Override
    public Boolean call() throws Exception {
        return true;
    }
}
