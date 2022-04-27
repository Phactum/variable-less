package at.phactum.camunda.it.taxiride.tasks;

import org.springframework.stereotype.Component;

import at.phactum.camunda.it.taxiride.domain.TaxiRide;
import at.phactum.camunda.workflowaggregate.tasks.SendTask;
import at.phactum.camunda.workflowaggregate.tasks.TaskException;

@Component
public class RideAvailable extends SendTask<TaxiRide> {

    @Override
    public void process(final TaxiRide taxiRide) throws TaskException {
        
        // send confirmation email to passenger
        
    }
    
}
