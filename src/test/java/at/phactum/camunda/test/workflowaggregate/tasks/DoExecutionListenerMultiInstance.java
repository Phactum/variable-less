package at.phactum.camunda.test.workflowaggregate.tasks;

import org.springframework.stereotype.Component;

import at.phactum.camunda.test.workflowaggregate.domain.TestEntity;
import at.phactum.camunda.workflowaggregate.tasks.ExecutionListenerMultiInstance;
import at.phactum.camunda.workflowaggregate.tasks.TaskException;
import at.phactum.camunda.workflowaggregate.tasks.support.MultiInstance;

@Component
public class DoExecutionListenerMultiInstance extends ExecutionListenerMultiInstance<TestEntity, Object> {

    @Override
    public void process(TestEntity object, MultiInstance<Object> multiInstance) throws TaskException {

        object.setCounter(object.getCounter() + multiInstance.getItemNo());

    };

}
