package at.phactum.camunda.workflowaggregate.adapters;


import org.camunda.bpm.engine.impl.javax.el.CompositeELResolver;
import org.camunda.bpm.engine.impl.javax.el.ELResolver;
import org.camunda.bpm.engine.spring.SpringExpressionManager;
import org.springframework.context.ApplicationContext;

/*
 * Custom expression manager to resolve process entities
 * by using correspondingly named spring data repositories.
 */
public class WorkflowAggregateAwareExpressionManager extends SpringExpressionManager {

    public WorkflowAggregateAwareExpressionManager(ApplicationContext applicationContext) {
        super(applicationContext, null);
    }

    @Override
    protected ELResolver createElResolver() {
        var compositeElResolver = (CompositeELResolver) super.createElResolver();
        compositeElResolver.add(new WorkflowAggregateELResolver(applicationContext));
        return compositeElResolver;
    }

}
