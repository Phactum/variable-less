package at.phactum.camunda.it.taxiride.tasks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import at.phactum.camunda.it.taxiride.DriverService;
import at.phactum.camunda.it.taxiride.domain.TaxiRide;
import at.phactum.camunda.workflowaggregate.tasks.ServiceTaskMultiInstance;
import at.phactum.camunda.workflowaggregate.tasks.TaskException;
import at.phactum.camunda.workflowaggregate.tasks.support.MultiInstance;

@Component
public class RequestRide extends ServiceTaskMultiInstance<TaxiRide, Object> {

    @Autowired
    private DriverService driverService;
    
    @Override
    public void process(
            final TaxiRide taxiRide,
            final MultiInstance<Object> driverInformation) throws TaskException {
        
        var driver = taxiRide.getNearbyDrivers().get(driverInformation.getItemNo());
        
        driverService.requestRide(driver, taxiRide.getId());
        
    }
    
}
