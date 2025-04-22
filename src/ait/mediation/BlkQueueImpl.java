package ait.mediation;

import java.util.LinkedList;
import java.util.Queue;

public class BlkQueueImpl<T> implements BlkQueue<T> {
    private final Queue<T> queue;
    private final int maxSize;

    public BlkQueueImpl(int maxSize) {
        this.maxSize = maxSize;
        this.queue = new LinkedList<>();
    }

    @Override
    public void push(T message) {
        synchronized (this) {
            while (queue.size() >= maxSize) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
            queue.add(message);
            notifyAll();
        }
    }

    @Override
    public T pop() {
        synchronized (this) {
            while (queue.isEmpty()) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return null;
                }
            }
            T result = queue.poll();
            notifyAll();
            return result;
        }
    }
}
