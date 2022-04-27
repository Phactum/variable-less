package at.phactum.camunda.workflowaggregate.tasks;

import java.lang.reflect.ParameterizedType;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;

import at.phactum.camunda.workflowaggregate.adapters.BusinessRuleDmnBasedTaskProcessor;
import at.phactum.camunda.workflowaggregate.domain.WorkflowAggregate;
import at.phactum.camunda.workflowaggregate.tasks.support.MultiInstance;
import at.phactum.camunda.workflowaggregate.tasks.support.MultiInstanceTask;
import at.phactum.camunda.workflowaggregate.tasks.support.Task;

/**
 * A DMN-based business rule task processes the result of a DMN evaluation and
 * wires it into the process entity in a way meaningful to it.
 * 
 * @param <P> Specific process entity the business rule task deals with
 * @param <R> Decision result type, e.g. String, List<Object>, Map<String,
 *            Object>, List<Map<String, Object>>
 * @param <T> T of elements in case of collection-based multi-instances
 */
public abstract class BusinessRuleDmnBasedTaskMultiInstance<P extends WorkflowAggregate, R, T> extends Task<Pair<P, R>, P>
        implements ExecutionListener, MultiInstanceTask<Pair<P, R>, T> {

    @Autowired
    private BusinessRuleDmnBasedTaskProcessor businessRuleTaskProcessor;

    private static final ThreadLocal<DelegateExecution> delegateExecution = new ThreadLocal<>();

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        delegateExecution.set(execution);
        businessRuleTaskProcessor.process(this, execution);
        delegateExecution.remove();
    }

    @Override
    public void process(Pair<P, R> object, MultiInstance<T> multiInstanceContext) throws TaskException {
        process(object.getFirst(), object.getSecond(), multiInstanceContext);
    }

    public abstract void process(P entity, R result, MultiInstance<T> multiInstanceContext);

    public static DelegateExecution getExecutionContext() {
        return delegateExecution.get();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<P> getEntityClass() {

        return (Class<P>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    }

}
