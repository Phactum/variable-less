package at.phactum.camunda.it.taxiride.tasks;

import java.util.LinkedList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import at.phactum.camunda.it.taxiride.DriverService;
import at.phactum.camunda.it.taxiride.domain.TaxiRide;
import at.phactum.camunda.workflowaggregate.tasks.ServiceTask;
import at.phactum.camunda.workflowaggregate.tasks.TaskException;

@Component
public class DetermineNearbyDrivers extends ServiceTask<TaxiRide> {

    @Autowired
    private DriverService driverService;
    
    @Override
    public void process(final TaxiRide taxiRide) throws TaskException {
        
        final var drivers = driverService.determineNearbyDrivers(
                taxiRide.getPickupLocation(),
                taxiRide.getPickupTime(),
                taxiRide.getTargetLocation());
        
        taxiRide.setNearbyDrivers(
                new LinkedList<>(drivers));
        
    }
    
}
