package at.phactum.camunda.test.workflowaggregate.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("MultiInstanceEmbeddedSubprocessServiceTask")
public class Process_MultiInstanceEmbeddedSubprocessServiceTask extends TestEntity {

    public Process_MultiInstanceEmbeddedSubprocessServiceTask() {
        super();
    }

    public Process_MultiInstanceEmbeddedSubprocessServiceTask(String businessKey) {
        super(businessKey);
    }

}
