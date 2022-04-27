package at.phactum.camunda.workflowaggregate.tasks;

/**
 * A TaskException represents a business problem to be handled by an error event in the process
 */
public abstract class TaskException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Error code used to identify this exception in the process model's
     * error event handling it. By default it is the simple name of the
     * class, but the getter can ofc be overwritten by inheriting classes.
     */
    private String errorCode = getClass().getSimpleName();

    public TaskException() {
    }

    public TaskException(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

}
