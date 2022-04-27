package at.phactum.camunda.workflowaggregate.tasks;

import java.lang.reflect.ParameterizedType;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.beans.factory.annotation.Autowired;

import at.phactum.camunda.workflowaggregate.adapters.JavaDelegateTaskProcessor;
import at.phactum.camunda.workflowaggregate.domain.WorkflowAggregate;
import at.phactum.camunda.workflowaggregate.tasks.support.SingleInstanceTask;
import at.phactum.camunda.workflowaggregate.tasks.support.Task;

public abstract class ExecutionListener<P extends WorkflowAggregate> extends Task<P, P>
        implements org.camunda.bpm.engine.delegate.ExecutionListener, SingleInstanceTask<P> {

    private static final ThreadLocal<DelegateExecution> delegateExecution = new ThreadLocal<>();

    @Autowired
    private JavaDelegateTaskProcessor taskProcessor;

    public static DelegateExecution getExecutionContext() {
        return delegateExecution.get();
    }

    @Override
    public void notify(DelegateExecution execution) throws BpmnError {
        delegateExecution.set(execution);
        taskProcessor.process(this, execution);
        delegateExecution.remove();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<P> getEntityClass() {

        return (Class<P>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    }

}
