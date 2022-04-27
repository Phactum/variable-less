package at.phactum.camunda.workflowaggregate.tasks;

import java.lang.reflect.ParameterizedType;

import at.phactum.camunda.workflowaggregate.domain.WorkflowAggregate;
import at.phactum.camunda.workflowaggregate.tasks.support.JavaDelegateTask;
import at.phactum.camunda.workflowaggregate.tasks.support.SingleInstanceTask;

/**
 * A service task may update the process entity and/or invoke external
 * services and wire results into the process entity in a way meaningful to it.
 *
 * @param <P> Specific process entity the service task deals with
 */
public abstract class SendTask<P extends WorkflowAggregate> extends JavaDelegateTask<P> implements SingleInstanceTask<P> {

    @SuppressWarnings("unchecked")
    @Override
    public Class<P> getEntityClass() {

        return (Class<P>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    }

}
