package at.phactum.camunda.workflowaggregate.tasks.support;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

import at.phactum.camunda.workflowaggregate.adapters.JavaDelegateTaskProcessor;
import at.phactum.camunda.workflowaggregate.domain.WorkflowAggregate;

/**
 * A send task may update the process entity and/or invoke external
 * services and wire results into the process entity in a way meaningful to it.
 *
 * @param <P> Specific process entity the service task deals with
 */
public abstract class JavaDelegateTask<P extends WorkflowAggregate> extends Task<P, P>
        implements JavaDelegate {

    private static final ThreadLocal<DelegateExecution> delegateExecution = new ThreadLocal<>();

    @Autowired
    private JavaDelegateTaskProcessor taskProcessor;

    public static DelegateExecution getExecutionContext() {
        return delegateExecution.get();
    }

    @Override
    public void execute(DelegateExecution execution) throws BpmnError {
        delegateExecution.set(execution);
        taskProcessor.process(this, execution);
        delegateExecution.remove();
    }

}
