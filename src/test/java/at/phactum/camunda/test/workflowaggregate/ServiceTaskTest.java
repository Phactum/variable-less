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

import at.phactum.camunda.test.workflowaggregate.domain.Process_MultiInstanceCallActivityServiceTask;
import at.phactum.camunda.test.workflowaggregate.domain.Process_MultiInstanceEmbeddedSubprocessServiceTask;
import at.phactum.camunda.test.workflowaggregate.domain.Process_MultiInstanceServiceTask;
import at.phactum.camunda.test.workflowaggregate.domain.Process_ServiceTask;
import at.phactum.camunda.test.workflowaggregate.domain.TestEntityRepository;
import at.phactum.camunda.workflowaggregate.WorkflowService;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfiguration.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        properties = { "server.port=8080" }
    )
public class ServiceTaskTest {

    @Autowired
    private WorkflowService processService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TestEntityRepository repository;

    @Autowired
    private TransactionTemplate transaction;

    @Test
    public void testServiceTask() throws Exception {

        final var businessKey = "testServiceTask";

        transaction.executeWithoutResult(tx -> {

            final var entityAtStartOfWorkflow = processService.startWorkflow(new Process_ServiceTask(businessKey));
            Assertions.assertNotNull(entityAtStartOfWorkflow);
            Assertions.assertEquals(businessKey, entityAtStartOfWorkflow.getId());

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

    @Test
    public void testMultiInstanceServiceTask() throws Exception {

        final var businessKey = "testMultiInstanceServiceTask";

        transaction.executeWithoutResult(tx -> {

            final var entityAtStartOfWorkflow = processService
                    .startWorkflow(new Process_MultiInstanceServiceTask(businessKey));
            Assertions.assertNotNull(entityAtStartOfWorkflow);
            Assertions.assertEquals(businessKey, entityAtStartOfWorkflow.getId());

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
            Assertions.assertEquals(0 + 1 + 2, entityAtEndOfWorkflow.getCounter(), "Unexpected counter value!");

        });

    }

    @Test
    public void testMultiInstanceEmbeddedSubprocessServiceTask() throws Exception {

        final var businessKey = "testMultiInstanceEmbeddedSubprocessServiceTask";

        transaction.executeWithoutResult(tx -> {

            final var entityAtStartOfWorkflow = processService
                    .startWorkflow(new Process_MultiInstanceEmbeddedSubprocessServiceTask(businessKey));
            Assertions.assertNotNull(entityAtStartOfWorkflow);
            Assertions.assertEquals(businessKey, entityAtStartOfWorkflow.getId());

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
            Assertions.assertEquals(0 + 1 + 2 + 3, entityAtEndOfWorkflow.getCounter(), "Unexpected counter value!");

        });

    }

    @Test
    public void testMultiInstanceCallActivityServiceTask() throws Exception {

        final var businessKey = "testMultiInstanceCallActivityServiceTask";

        transaction.executeWithoutResult(tx -> {

            final var entityAtStartOfWorkflow = processService
                    .startWorkflow(new Process_MultiInstanceCallActivityServiceTask(businessKey));
            Assertions.assertNotNull(entityAtStartOfWorkflow);
            Assertions.assertEquals(businessKey, entityAtStartOfWorkflow.getId());

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
            Assertions.assertEquals(0 + 1 + 2 + 3 + 4, entityAtEndOfWorkflow.getCounter(), "Unexpected counter value!");

        });

    }

}
