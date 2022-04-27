package at.phactum.camunda.workflowaggregate.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * A process entity represents a process' business state and holds its data in a
 * type-safe and more maintainable manner than process variables do.
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class WorkflowAggregate implements Persistable<String> {

    /**
     * Unique identifier of the process entity. It's called "business key" to better
     * align its name with the same property usable with process instances.
     */
    @Id
    @Column(name = "BUSINESS_KEY")
    private String businessKey;

    /**
     * Unique identifier of the process instance started for this entity instance.
     */
    @Column(name = "PROCESS_INSTANCE_ID")
    private String processInstanceId;

    /**
     * JPA Version attribute designed to enable optimistic locking functionality.
     */
    @Version
    @Column(name = "VERSION")
    private int version;

    /**
     * Moment in time at which this entity instance was created.
     */
    @CreatedDate
    @Column(name = "CREATED_AT", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Date createdAt;

    /**
     * Moment in time at which this entity instance was updated.
     */
    @LastModifiedDate
    @Column(name = "UPDATED_AT", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Date updatedAt;

    /*
     * Enables primary key constraint violations by preventing spring data
     * repositories to merge unknown objects with the object's state in the
     * underlying data store.
     */
    @Transient
    private boolean update;

    /* Required for JPA */
    protected WorkflowAggregate() {
    }

    protected WorkflowAggregate(String businessKey) {
        this.businessKey = businessKey;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        if (this.processInstanceId != null)
            throw new IllegalArgumentException(String
                    .format("Process instance id is already set to %s and must not be updated afterwards.",
                            processInstanceId));
        this.processInstanceId = processInstanceId;
    }

    @Override
    public String getId() {
        return getBusinessKey();
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    /*
     * Enables primary key constraint violations by preventing spring data
     * repositories to merge unknown objects with the object's state in the
     * underlying data store.
     */
    @Override
    public final boolean isNew() {
        return !this.update;
    }

    public boolean isUpdate() {
        return this.update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    @PrePersist
    @PostLoad
    private void markUpdated() {
        this.update = true;
    }

    @Override
    public String toString() {
        String process = processInstanceId != null
                ? String
                        .format(" and %s{id='%s', businessKey='%s'}",
                                getClass().getSimpleName(),
                                getProcessInstanceId(),
                                getBusinessKey())
                : "";
        return String.format("%s{id='%s'}%s", getClass().getSimpleName(), getBusinessKey(), process);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        var process = (WorkflowAggregate) o;
        return businessKey.equals(process.businessKey);
    }

    @Override
    public int hashCode() {
        return businessKey.hashCode();
    }

}
