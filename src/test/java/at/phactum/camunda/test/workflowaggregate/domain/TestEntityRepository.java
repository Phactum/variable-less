package at.phactum.camunda.test.workflowaggregate.domain;

import org.springframework.stereotype.Repository;

import at.phactum.camunda.workflowaggregate.domain.WorkflowAggregateRepository;

@Repository
public interface TestEntityRepository extends WorkflowAggregateRepository<TestEntity> {

}
