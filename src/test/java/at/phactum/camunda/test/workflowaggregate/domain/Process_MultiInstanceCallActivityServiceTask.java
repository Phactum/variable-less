package at.phactum.camunda.test.workflowaggregate.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("MultiInstanceCallActivityServiceTask")
public class Process_MultiInstanceCallActivityServiceTask extends TestEntity {

    public Process_MultiInstanceCallActivityServiceTask() {
        super();
    }

    public Process_MultiInstanceCallActivityServiceTask(String businessKey) {
        super(businessKey);
    }

}
