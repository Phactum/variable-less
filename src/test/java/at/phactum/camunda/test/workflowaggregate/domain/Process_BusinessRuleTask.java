package at.phactum.camunda.test.workflowaggregate.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("BusinessRuleTask")
public class Process_BusinessRuleTask extends TestEntity {

    public Process_BusinessRuleTask() {
        super();
    }

    public Process_BusinessRuleTask(String businessKey) {
        super(businessKey);
    }

}
