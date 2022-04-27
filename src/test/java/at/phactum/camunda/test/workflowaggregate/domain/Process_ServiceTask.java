package at.phactum.camunda.test.workflowaggregate.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("ServiceTask")
public class Process_ServiceTask extends TestEntity {

    public Process_ServiceTask() {
        super();
    }

    public Process_ServiceTask(String businessKey) {
        super(businessKey);
    }

}
