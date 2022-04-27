package at.phactum.camunda.test.workflowaggregate;

import org.camunda.bpm.engine.RuntimeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.support.TransactionTemplate;

import at.phactum.camunda.test.workflowaggregate.domain.Process_SendTask;
import at.phactum.camunda.test.workflowaggregate.domain.TestEntityRepository;
import at.phactum.camunda.workflowaggregate.WorkflowService;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfiguration.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        properties = { "server.port=8080" }
    )
public class SendTaskTest {

    @Autowired
    private WorkflowService processService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TestEntityRepository repository;

    @Autowired
    private TransactionTemplate transaction;

    @Test
    public void testSendTask() throws Exception {

        final var businessKey = "testSendTask";

        transaction.executeWithoutResult(tx -> {

            final var entityAtStartOfWorkflow = processService.startWorkflow(new Process_SendTask(businessKey));
            Assertions.assertNotNull(entityAtStartOfWorkflow);
            Assertions.assertEquals(businessKey, entityAtStartOfWorkflow.getId());
            Assertions.assertEquals(0, entityAtStartOfWorkflow.getCounter(), "Unexpected number of executions counted!");

        });

        // due to async-before on start-event
        Thread.sleep(500);

        transaction.executeWithoutResult(tx -> {

            final var processInstance = runtimeService
                    .createProcessInstanceQuery()
                    .processInstanceBusinessKey(businessKey)
                    .singleResult();
            Assertions.assertNull(processInstance, "Process was not completed within a second");

            final var entityAtEndOfWorkflow = repository.getById(businessKey);

            Assertions.assertNotNull(entityAtEndOfWorkflow);
            Assertions.assertEquals(1, entityAtEndOfWorkflow.getCounter(), "Unexpected number of executions counted!");

        });

    }

}
