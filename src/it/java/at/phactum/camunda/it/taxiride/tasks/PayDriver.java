package at.phactum.camunda.it.taxiride.tasks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import at.phactum.camunda.it.taxiride.DriverService;
import at.phactum.camunda.it.taxiride.domain.TaxiRide;
import at.phactum.camunda.workflowaggregate.tasks.ServiceTask;
import at.phactum.camunda.workflowaggregate.tasks.TaskException;

@Component
public class PayDriver extends ServiceTask<TaxiRide> {

    @Autowired
    private DriverService driverService;

    @Override
    public void process(final TaxiRide taxiRide) throws TaskException {
        
        final var amount = 47.11f; // in this test-case a constant value

        driverService.payDriver(taxiRide.getDriver(), amount);
        
    }
    
}
