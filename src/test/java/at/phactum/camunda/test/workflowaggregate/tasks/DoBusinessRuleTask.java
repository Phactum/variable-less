package at.phactum.camunda.test.workflowaggregate.tasks;

import org.springframework.stereotype.Component;

import at.phactum.camunda.test.workflowaggregate.domain.TestEntity;
import at.phactum.camunda.workflowaggregate.tasks.BusinessRuleTask;
import at.phactum.camunda.workflowaggregate.tasks.TaskException;

@Component
public class DoBusinessRuleTask extends BusinessRuleTask<TestEntity> {

    @Override
    public void process(TestEntity object) throws TaskException {

        object.increaseCounter();

    };

}
