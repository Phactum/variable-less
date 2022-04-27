package at.phactum.camunda.test.workflowaggregate.tasks;

import org.springframework.stereotype.Component;

import at.phactum.camunda.test.workflowaggregate.domain.TestEntity;
import at.phactum.camunda.workflowaggregate.tasks.BusinessRuleDmnBasedTask;

@Component
public class DoBusinessRuleTaskDmn extends BusinessRuleDmnBasedTask<TestEntity, String> {

    @Override
    public void process(TestEntity entity, String result) {

        entity.setCounter(result.hashCode());

    }

}
