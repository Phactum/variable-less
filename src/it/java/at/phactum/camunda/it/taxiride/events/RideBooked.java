package at.phactum.camunda.it.taxiride.events;

import java.util.Date;

public class RideBooked {

    private String pickupLocation;

    private Date pickupTime;

    private String targetLocation;

    public RideBooked pickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
        return this;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public RideBooked pickupTime(Date pickupTime) {
        this.pickupTime = pickupTime;
        return this;
    }

    public Date getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(Date pickupTime) {
        this.pickupTime = pickupTime;
    }

    public RideBooked targetLocation(String targetLocation) {
        this.targetLocation = targetLocation;
        return this;
    }

    public String getTargetLocation() {
        return targetLocation;
    }

    public void setTargetLocation(String targetLocation) {
        this.targetLocation = targetLocation;
    }

}
