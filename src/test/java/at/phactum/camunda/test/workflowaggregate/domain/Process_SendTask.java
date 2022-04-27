package at.phactum.camunda.test.workflowaggregate.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("SendTask")
public class Process_SendTask extends TestEntity {

    public Process_SendTask() {
        super();
    }

    public Process_SendTask(String businessKey) {
        super(businessKey);
    }

}
