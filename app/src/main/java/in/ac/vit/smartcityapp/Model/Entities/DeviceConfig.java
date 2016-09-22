package in.ac.vit.smartcityapp.Model.Entities;


public class DeviceConfig {

    private int deviceId ;
    private String deviceName ;
    private boolean deviceCurrentStatus ;
    private String specialDescription ;

    public DeviceConfig(int deviceId, String deviceName, boolean deviceCurrentStatus, String specialDescription) {
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.deviceCurrentStatus = deviceCurrentStatus;
        this.specialDescription = specialDescription;
    }

    public DeviceConfig() {
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public boolean isDeviceCurrentStatus() {
        return deviceCurrentStatus;
    }

    public void setDeviceCurrentStatus(boolean deviceCurrentStatus) {
        this.deviceCurrentStatus = deviceCurrentStatus;
    }

    public String getSpecialDescription() {
        return specialDescription;
    }

    public void setSpecialDescription(String specialDescription) {
        this.specialDescription = specialDescription;
    }
}
