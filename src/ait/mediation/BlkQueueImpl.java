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
    public synchronized void push(T message) {
            while (queue.size() >= maxSize) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            queue.add(message);
            notify();

    }

    @Override
    public synchronized T pop() {
            while (queue.isEmpty()) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            T result = queue.poll();
            notifyAll();
            return result;
        }
    }
