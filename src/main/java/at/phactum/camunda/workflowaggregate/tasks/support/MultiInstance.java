package at.phactum.camunda.workflowaggregate.tasks.support;

public class MultiInstance<T> {

    private T item;

    private int totalCount;

    private int itemNo;

    public MultiInstance(T item, int totalCount, int itemNo) {
        this.item = item;
        this.totalCount = totalCount;
        this.itemNo = itemNo;
    }

    public T getItem() {
        return item;
    }

    public int getItemNo() {
        return itemNo;
    }

    public int getTotalCount() {
        return totalCount;
    }

}
