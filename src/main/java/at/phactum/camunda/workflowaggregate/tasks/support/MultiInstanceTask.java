package at.phactum.camunda.workflowaggregate.tasks.support;

import at.phactum.camunda.workflowaggregate.tasks.TaskException;

public interface MultiInstanceTask<O, T> {

    /**
     * Process a task by taking care of business relevant processing
     * 
     * @param object the object the task needs as an input for processing
     * @throws TaskException represents a business problem to be handled by an error
     *                       event in the process
     */
    void process(O object, MultiInstance<T> multiInstanceContext) throws TaskException;

}
