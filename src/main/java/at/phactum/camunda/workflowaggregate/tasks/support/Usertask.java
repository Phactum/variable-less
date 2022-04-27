package at.phactum.camunda.workflowaggregate.tasks.support;

import java.util.Collection;
import java.util.Date;
import java.util.Set;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.task.IdentityLink;

public class Usertask {

    private final DelegateTask delegate;

    public Usertask(DelegateTask delegate) {
        this.delegate = delegate;
    }

    public String getId() {
        return delegate.getId();
    }

    public String getName() {
        return delegate.getName();
    }

    public void setName(String name) {
        delegate.setName(name);
    }

    public String getDescription() {
        return delegate.getDescription();
    }

    public void setDescription(String description) {
        delegate.setDescription(description);
    }

    public int getPriority() {
        return delegate.getPriority();
    }

    public void setPriority(int priority) {
        delegate.setPriority(priority);
    }

    public String getProcessInstanceId() {
        return delegate.getProcessInstanceId();
    }

    public String getProcessDefinitionId() {
        return delegate.getProcessDefinitionId();
    }

    public Date getCreateTime() {
        return delegate.getCreateTime();
    }

    public String getTaskDefinitionKey() {
        return delegate.getTaskDefinitionKey();
    }

    public String getEventName() {
        return delegate.getEventName();
    }

    public void addCandidateUser(String userId) {
        delegate.addCandidateUser(userId);
    }

    public void addCandidateUsers(Collection<String> candidateUsers) {
        delegate.addCandidateUsers(candidateUsers);
    }

    public void addCandidateGroup(String groupId) {
        delegate.addCandidateGroup(groupId);
    }

    public void addCandidateGroups(Collection<String> candidateGroups) {
        delegate.addCandidateGroups(candidateGroups);
    }

    public String getOwner() {
        return delegate.getOwner();
    }

    public void setOwner(String owner) {
        delegate.setOwner(owner);
    }

    public String getAssignee() {
        return delegate.getAssignee();
    }

    public void setAssignee(String assignee) {
        delegate.setAssignee(assignee);
    }

    public Date getDueDate() {
        return delegate.getDueDate();
    }

    public void setDueDate(Date dueDate) {
        delegate.setDueDate(dueDate);
    }

    public void addUserIdentityLink(String userId, String identityLinkType) {
        delegate.addUserIdentityLink(userId, identityLinkType);
    }

    public void addGroupIdentityLink(String groupId, String identityLinkType) {
        delegate.addGroupIdentityLink(groupId, identityLinkType);
    }

    public void deleteCandidateUser(String userId) {
        delegate.deleteCandidateUser(userId);
    }

    public void deleteCandidateGroup(String groupId) {
        delegate.deleteCandidateGroup(groupId);
    }

    public void deleteUserIdentityLink(String userId, String identityLinkType) {
        delegate.deleteUserIdentityLink(userId, identityLinkType);
    }

    public void deleteGroupIdentityLink(String groupId, String identityLinkType) {
        delegate.deleteGroupIdentityLink(groupId, identityLinkType);
    }

    public Set<IdentityLink> getCandidates() {
        return delegate.getCandidates();
    }

    public String getTenantId() {
        return delegate.getTenantId();
    }

    public Date getFollowUpDate() {
        return delegate.getFollowUpDate();
    }

    public void setFollowUpDate(Date followUpDate) {
        delegate.setFollowUpDate(followUpDate);
    }

    public void complete() {
        delegate.complete();
    }

}
