package at.phactum.camunda.workflowaggregate;

import org.camunda.bpm.engine.impl.cfg.AbstractProcessEnginePlugin;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import at.phactum.camunda.workflowaggregate.adapters.WorkflowAggregateAwareExpressionManager;

@Component
public class WorkflowAggregateCamundaPlugin extends AbstractProcessEnginePlugin {

    private final ApplicationContext applicationContext;

    public WorkflowAggregateCamundaPlugin(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void preInit(ProcessEngineConfigurationImpl configuration) {
        configuration.setExpressionManager(
            new WorkflowAggregateAwareExpressionManager(applicationContext)
        );
    }
    
}
