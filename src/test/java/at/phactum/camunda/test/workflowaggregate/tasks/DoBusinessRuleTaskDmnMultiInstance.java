package at.phactum.camunda.test.workflowaggregate.tasks;

import org.springframework.stereotype.Component;

import at.phactum.camunda.test.workflowaggregate.domain.TestEntity;
import at.phactum.camunda.workflowaggregate.tasks.BusinessRuleDmnBasedTaskMultiInstance;
import at.phactum.camunda.workflowaggregate.tasks.support.MultiInstance;

@Component
public class DoBusinessRuleTaskDmnMultiInstance
        extends BusinessRuleDmnBasedTaskMultiInstance<TestEntity, String, Object> {

    @Override
    public void process(TestEntity entity, String result, MultiInstance<Object> multiInstance) {

        entity.setCounter(entity.getCounter() + result.hashCode());

    }

}
