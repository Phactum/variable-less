package at.phactum.camunda.workflowaggregate.adapters;


import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import at.phactum.camunda.workflowaggregate.domain.WorkflowAggregate;
import at.phactum.camunda.workflowaggregate.tasks.support.MultiInstanceTask;
import at.phactum.camunda.workflowaggregate.tasks.support.SingleInstanceTask;
import at.phactum.camunda.workflowaggregate.tasks.support.Task;
import at.phactum.camunda.workflowaggregate.utils.SpringDataTool;

@Component
public class BusinessRuleDmnBasedTaskProcessor extends MultiInstanceTaskProcessor {

    private static final Logger log = LoggerFactory.getLogger(BusinessRuleDmnBasedTaskProcessor.class);

    @Autowired
    private SpringDataTool springDataTool;

    @SuppressWarnings("unchecked")
    public <P extends WorkflowAggregate, R> void process(Task<Pair<P, R>, P> task, DelegateExecution execution)
            throws Exception {

        var businessKey = execution.getProcessBusinessKey();

        var eventName = execution.getEventName();
        var eventLogName = eventName.substring(0, 1).toUpperCase() + eventName.substring(1) + "ing";

        log.trace("{} {} for ProcessInstance{id={}, businessKey={}}",
            eventLogName,
            getClass().getSimpleName(),
            execution.getProcessInstanceId(),
            businessKey
        );

        var resultVariable = execution.getBpmnModelElementInstance()
            .getAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "resultVariable");

        log.trace("About to {} {}", eventName, task.getClass().getSimpleName());

        var entityClass = task.getEntityClass();
        var repository = springDataTool.getJpaRepository(entityClass);
        var entity = (P) repository.findById(businessKey).orElseThrow();

        log.trace("{} {} for {}", eventLogName, task.getClass().getSimpleName(), entity);

        var result = (R) execution.getVariable(resultVariable);
        if (task instanceof SingleInstanceTask) {
            ((SingleInstanceTask<Pair<P, R>>) task).process(Pair.of(entity, result));
        } else if (task instanceof MultiInstanceTask) {
            final var multiInstanceContext = getMultiInstanceContext(execution);
            ((MultiInstanceTask<Pair<P, R>, Object>) task).process(Pair.of(entity, result), multiInstanceContext);
        } else {
            throw new RuntimeException("Unsupported task type '" + task.getClass().getName() + "'");
        }

        repository.saveAndFlush(entity);

        execution.removeVariable(resultVariable);

    }

}