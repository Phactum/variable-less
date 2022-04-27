package at.phactum.camunda.workflowaggregate.tasks.support;

import at.phactum.camunda.workflowaggregate.domain.WorkflowAggregate;

/**
 * A Task takes care of an activity (or activity-oriented event) in a process,
 * in other words, it takes care of the business relevant part of the processing
 * @param <O> the specific object type the task accepts as an input for processing
 */
public abstract class Task<O, P extends WorkflowAggregate> {

    /**
     * @return the type (class) of process entity this task handles
     */
    public abstract Class<P> getEntityClass();

}
