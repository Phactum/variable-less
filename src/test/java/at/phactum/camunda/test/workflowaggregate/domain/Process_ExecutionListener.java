package at.phactum.camunda.test.workflowaggregate.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("ExecutionListener")
public class Process_ExecutionListener extends TestEntity {

    public Process_ExecutionListener() {
        super();
    }

    public Process_ExecutionListener(String businessKey) {
        super(businessKey);
    }

}
