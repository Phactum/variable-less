package at.phactum.camunda.test.workflowaggregate.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("MultiInstanceDMN")
public class Process_MultiInstanceDMN extends TestEntity {

    public Process_MultiInstanceDMN() {
        super();
    }

    public Process_MultiInstanceDMN(String businessKey) {
        super(businessKey);
    }

}
