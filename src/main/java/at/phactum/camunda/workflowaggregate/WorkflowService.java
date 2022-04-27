package at.phactum.camunda.workflowaggregate;

import org.camunda.bpm.engine.MismatchingMessageCorrelationException;
import org.camunda.bpm.engine.RuntimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import at.phactum.camunda.workflowaggregate.domain.WorkflowAggregate;
import at.phactum.camunda.workflowaggregate.utils.SpringDataTool;

/**
 * The process service is a facade in front of the infrastructure code needed
 * - to create process entities and start the related process instances and
 * - to update process entities and correlate messages to related process instances
 */
@Service
public class WorkflowService {

    private static final Logger log = LoggerFactory.getLogger(WorkflowService.class);

    private final RuntimeService runtimeService;

    private final SpringDataTool springDataTool;

    public WorkflowService(SpringDataTool springDataTool, RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
        this.springDataTool = springDataTool;
    }

    @Transactional
    public <P extends WorkflowAggregate> P startWorkflow(P workflowAggregate) {

        var processInstance = runtimeService
                .createProcessInstanceByKey(workflowAggregate.getClass().getSimpleName())
                .businessKey(workflowAggregate.getBusinessKey())
                .execute();

        workflowAggregate.setProcessInstanceId(processInstance.getId());

        var processRepository = springDataTool.getJpaRepository(workflowAggregate);

        processRepository.saveAndFlush(workflowAggregate);
        log.info("Created {}.", workflowAggregate);

        return workflowAggregate;

    }

    @Transactional
    public void correlateMessage(WorkflowAggregate processEntity, String messageName) {

        final var isNewEntity = processEntity.isNew();
        
        if (log.isTraceEnabled())
            log.trace("About to correlate message '{}' to {}", messageName, processEntity);

        var processRepository = springDataTool.getJpaRepository(processEntity);
        var storedEntity = processRepository.saveAndFlush(processEntity);

        try {

            if (isNewEntity) {
                
                final var processInstance = runtimeService
                        .createMessageCorrelation(messageName)
                        .processInstanceBusinessKey(storedEntity.getId())
                        .correlateStartMessage();
                storedEntity.setProcessInstanceId(processInstance.getId());
                processRepository.saveAndFlush(storedEntity);
                
            } else {
                
                runtimeService
                        .createMessageCorrelation(messageName)
                        .processInstanceId(storedEntity.getProcessInstanceId())
                        .correlate();
                
            }

            log.info("Correlated message '{}' to {}.", messageName, storedEntity);

        } catch (MismatchingMessageCorrelationException exception) {

            if (log.isDebugEnabled()) {
                log
                        .debug("Cannot correlate message '{}' as {} do not (currently) process it.",
                                messageName,
                                processEntity,
                                exception);
            }

        }

    }

}
