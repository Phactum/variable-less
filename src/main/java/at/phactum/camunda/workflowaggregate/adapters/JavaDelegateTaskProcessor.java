package at.phactum.camunda.workflowaggregate.adapters;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import at.phactum.camunda.workflowaggregate.domain.WorkflowAggregate;
import at.phactum.camunda.workflowaggregate.tasks.TaskException;
import at.phactum.camunda.workflowaggregate.tasks.support.MultiInstanceTask;
import at.phactum.camunda.workflowaggregate.tasks.support.SingleInstanceTask;
import at.phactum.camunda.workflowaggregate.tasks.support.Task;
import at.phactum.camunda.workflowaggregate.utils.SpringDataTool;

@Component
public class JavaDelegateTaskProcessor extends MultiInstanceTaskProcessor {

    private static final Logger log = LoggerFactory.getLogger(JavaDelegateTaskProcessor.class);

    private final PlatformTransactionManager transactionManager;

    private final SpringDataTool springDataTool;
    
    public JavaDelegateTaskProcessor(
            PlatformTransactionManager transactionManager,
            SpringDataTool springDataTool) {
        this.transactionManager = transactionManager;
        this.springDataTool = springDataTool;
    }

    @SuppressWarnings("unchecked")
    public <P extends WorkflowAggregate> void process(Task<P, P> task, DelegateExecution execution) throws BpmnError {

        var businessKey = execution.getProcessBusinessKey();

        log.trace("About to execute {} for task {} of  ProcessInstance{id={}, businessKey={}}",
                getClass().getSimpleName(),
                task.getClass().getSimpleName(),
                execution.getProcessInstanceId(),
                businessKey);

        var entityClass = task.getEntityClass();

        var transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

        var bpmnError = transactionTemplate.execute(status -> {

            var repository = springDataTool.getJpaRepository(entityClass);
            var entity = (P) repository.findById(businessKey).orElseThrow();
            log.trace("Executing {} for {}", task.getClass().getSimpleName(), entity);

            try {
                if (task instanceof SingleInstanceTask) {
                    ((SingleInstanceTask<P>) task).process(entity);
                } else if (task instanceof MultiInstanceTask) {
                    final var multiInstanceContext = getMultiInstanceContext(execution);
                    ((MultiInstanceTask<P, Object>) task).process(entity, multiInstanceContext);
                } else {
                    throw new RuntimeException("Unsupported task type '" + task.getClass().getName() + "'");
                }
                log.debug("Executed {} for {}", task.getClass().getSimpleName(), entity);
                return null;
            } catch (TaskException taskException) {
                log
                        .debug("Experiencing {} while executing {} for {}",
                                taskException.getErrorCode(),
                                task.getClass().getSimpleName(),
                                entity);
                return new BpmnError(taskException.getErrorCode());
            } finally {
                repository.saveAndFlush(entity);
            }

        });

        if (bpmnError != null) {
            throw bpmnError;
        }

    }

}
