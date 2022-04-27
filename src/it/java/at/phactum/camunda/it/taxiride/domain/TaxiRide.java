package at.phactum.camunda.it.taxiride.domain;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.LinkedList;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import at.phactum.camunda.workflowaggregate.domain.WorkflowAggregate;

@Entity
@Table(name = "RIDES")
public class TaxiRide extends WorkflowAggregate {

    /* Required for JPA */
    protected TaxiRide() {
    }

    public TaxiRide(final String businessKey) {
        super(businessKey);
    }

    @Column(name = "PICKUP_LOCATION", nullable = false)
    private String pickupLocation;

    @Column(name = "PICKUP_TIME", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime pickupTime;

    @Column(name = "TARGET_LOCATION", nullable = false)
    private String targetLocation;

    @Column(name = "DRIVERS", columnDefinition = "BLOB")
    private LinkedList<String> nearbyDrivers;

    @Column(name = "DRIVER")
    private String driver;

    public LinkedList<String> getNearbyDrivers() {
        return nearbyDrivers;
    }

    public void setNearbyDrivers(LinkedList<String> nearbyDrivers) {
        this.nearbyDrivers = nearbyDrivers;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public OffsetDateTime getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(OffsetDateTime pickupTime) {
        this.pickupTime = pickupTime;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public String getTargetLocation() {
        return targetLocation;
    }

    public void setTargetLocation(String targetLocation) {
        this.targetLocation = targetLocation;
    }

    public Date getConfirmationDeadline() {
        
        if (Duration
                .between(OffsetDateTime.now(), pickupTime)
                .toMinutes() > 20) {
            
            return Date.from(OffsetDateTime
                    .now()
                    .plus(15, ChronoUnit.MINUTES)
                    .toInstant());
                    
        }
        
        return Date.from(OffsetDateTime
                .now()
                .plus(3, ChronoUnit.MINUTES)
                .toInstant());
        
    }

}
