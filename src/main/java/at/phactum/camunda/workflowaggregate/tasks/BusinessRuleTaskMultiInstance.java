package at.phactum.camunda.workflowaggregate.tasks;

import java.lang.reflect.ParameterizedType;

import at.phactum.camunda.workflowaggregate.domain.WorkflowAggregate;
import at.phactum.camunda.workflowaggregate.tasks.support.JavaDelegateTask;
import at.phactum.camunda.workflowaggregate.tasks.support.MultiInstanceTask;

/**
 * A business-rule task may update the process entity and/or invoke external
 * services and wire results into the process entity in a way meaningful to it.
 *
 * @param <P> Specific process entity the service task deals with
 * @param <T> T of elements in case of collection-based multi-instances
 */
public abstract class BusinessRuleTaskMultiInstance<P extends WorkflowAggregate, T> extends JavaDelegateTask<P>
        implements MultiInstanceTask<P, T> {

    @SuppressWarnings("unchecked")
    @Override
    public Class<P> getEntityClass() {

        return (Class<P>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    }

}
