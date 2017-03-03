package nessemis_developments.wakeonline;

/**
 * Created by Dennis Arets on 01-Mar-17.
 */

public final class Device {
    final private String name;

    //Mac adress is stored in hexadecimal form.
    final private int mac;

    final private String wakeUrl;

    final private String sleepUrl;

    public Device(String name, int mac, String wakeUrl, String sleepUrl) {
        this.name = name;
        this.mac = mac;
        this.wakeUrl = wakeUrl;
        this.sleepUrl = sleepUrl;
    }

    public String getName() {
        return name;
    }

    public int getmac() {
        return mac;
    }

    public String getWakeUrl() {
        return wakeUrl;
    }

    public String getSleepUrl() {
        return sleepUrl;
    }
}
