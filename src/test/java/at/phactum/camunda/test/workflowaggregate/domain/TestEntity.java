package at.phactum.camunda.test.workflowaggregate.domain;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import at.phactum.camunda.workflowaggregate.domain.WorkflowAggregate;

@Entity
@Table(name = "TEST_ENTITY")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE", columnDefinition = "VARCHAR(255)")
public abstract class TestEntity extends WorkflowAggregate {

    @Column(name = "COUNTER")
    private int counter;

    public TestEntity() {
        super();
    }

    public TestEntity(final String businessKey) {
        super(businessKey);
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public void increaseCounter() {
        // always used getter and setter within hibernate-entities because this code may
        // run as part of a hibernate-proxy
        setCounter(getCounter() + 1);
    }

}
