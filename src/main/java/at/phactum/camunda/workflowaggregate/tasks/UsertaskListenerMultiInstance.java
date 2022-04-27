package at.phactum.camunda.workflowaggregate.tasks;

import java.lang.reflect.ParameterizedType;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;

import at.phactum.camunda.workflowaggregate.adapters.UsertaskTaskProcessor;
import at.phactum.camunda.workflowaggregate.domain.WorkflowAggregate;
import at.phactum.camunda.workflowaggregate.tasks.support.MultiInstance;
import at.phactum.camunda.workflowaggregate.tasks.support.MultiInstanceTask;
import at.phactum.camunda.workflowaggregate.tasks.support.Task;
import at.phactum.camunda.workflowaggregate.tasks.support.Usertask;

public abstract class UsertaskListenerMultiInstance<P extends WorkflowAggregate, T> extends Task<Pair<P, Usertask>, P>
        implements org.camunda.bpm.engine.delegate.TaskListener, MultiInstanceTask<Pair<P, Usertask>, T> {

    private static final ThreadLocal<DelegateTask> task = new ThreadLocal<>();

    @Autowired
    private UsertaskTaskProcessor taskProcessor;

    public static DelegateTask getTaskContext() {
        return task.get();
    }

    @Override
    public void notify(final DelegateTask delegateTask) {
        task.set(delegateTask);
        taskProcessor.process(this, delegateTask);
        task.remove();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<P> getEntityClass() {

        return (Class<P>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    }

    @Override
    public void process(Pair<P, Usertask> object, MultiInstance<T> multiInstanceContext) throws TaskException {
        process(object.getFirst(), object.getSecond(), multiInstanceContext);
    }

    /**
     * Process a task by taking care of business relevant processing
     * 
     * @param object the object the task needs as an input for processing
     * @throws TaskException represents a business problem to be handled by an error
     *                       event in the process
     */
    public abstract void process(P entity, Usertask task, MultiInstance<T> multiInstanceContext)
            throws TaskException;

}
