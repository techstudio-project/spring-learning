package com.techstudio.socket.core.support.ds;

/**
 * 带优先级的节点, 可用于构成链表
 */
public class BytePriorityNode<E> {

    private byte priority;
    private E item;
    private BytePriorityNode<E> next;

    public BytePriorityNode(E item) {
        this.item = item;
    }

    /**
     * 按优先级追加到当前链表中
     *
     * @param node Node
     */
    public void appendWithPriority(BytePriorityNode<E> node) {
        if (next == null) {
            next = node;
        } else {
            BytePriorityNode<E> after = this.next;
            if (after.priority < node.priority) {
                // 中间位置插入
                this.next = node;
                node.next = after;
            } else {
                after.appendWithPriority(node);
            }
        }
    }

    public byte getPriority() {
        return priority;
    }

    public void setPriority(byte priority) {
        this.priority = priority;
    }

    public E getItem() {
        return item;
    }

    public void setItem(E item) {
        this.item = item;
    }

    public BytePriorityNode<E> getNext() {
        return next;
    }

    public void setNext(BytePriorityNode<E> next) {
        this.next = next;
    }
}
