package at.phactum.camunda.it.taxiride.tasks;

import org.springframework.stereotype.Component;

import at.phactum.camunda.it.taxiride.domain.TaxiRide;
import at.phactum.camunda.workflowaggregate.tasks.ServiceTask;
import at.phactum.camunda.workflowaggregate.tasks.TaskException;

@Component
public class ChargeCustomer extends ServiceTask<TaxiRide> {

    @Override
    public void process(final TaxiRide taxiRide) throws TaskException {
        
        // use payment-service-provider to charge customer
        
    }
    
}
