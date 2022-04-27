package at.phactum.camunda.test.workflowaggregate.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("MultiInstanceExecutionListener")
public class Process_MultiInstanceExecutionListener extends TestEntity {

    public Process_MultiInstanceExecutionListener() {
        super();
    }

    public Process_MultiInstanceExecutionListener(String businessKey) {
        super(businessKey);
    }

}
