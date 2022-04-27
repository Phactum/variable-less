package at.phactum.camunda.it.taxiride.domain;

import org.springframework.stereotype.Repository;

import at.phactum.camunda.workflowaggregate.domain.WorkflowAggregateRepository;

@Repository
public interface TaxiRideRepository extends WorkflowAggregateRepository<TaxiRide> {

}
