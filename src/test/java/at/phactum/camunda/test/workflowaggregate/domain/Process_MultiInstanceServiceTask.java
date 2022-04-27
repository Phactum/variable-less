package at.phactum.camunda.test.workflowaggregate.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("MultiInstanceServiceTask")
public class Process_MultiInstanceServiceTask extends TestEntity {

    public Process_MultiInstanceServiceTask() {
        super();
    }

    public Process_MultiInstanceServiceTask(String businessKey) {
        super(businessKey);
    }

}
