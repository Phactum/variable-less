package at.phactum.camunda.it.taxiride;

import java.time.OffsetDateTime;
import java.util.Collection;

public interface DriverService {

    Collection<String> determineNearbyDrivers(String pickupLocation, OffsetDateTime pickupTime, String targetLocation);

    void requestRide(String driver, String rideId);

    void payDriver(String driver, float amount);

}
