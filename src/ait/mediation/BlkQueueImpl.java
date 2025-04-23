package ait.mediation;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BlkQueueImpl<T> implements BlkQueue<T> {
    private final Queue<T> queue;
    private final int maxSize;
    private final Lock mutex = new ReentrantLock();
    private final Condition producer = mutex.newCondition();
    private final Condition consumer = mutex.newCondition();

    public BlkQueueImpl(int maxSize) {
        this.maxSize = maxSize;
        this.queue = new LinkedList<>();
    }

    @Override
    public  void push(T message) {
        mutex.lock();
        try {
            while (queue.size() >= maxSize) {
                try {
                    producer.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            queue.add(message);
            consumer.signal();
        } finally {
            mutex.unlock();
        }

    }

    @Override
    public  T pop() {
        mutex.lock();
        try {
            while (queue.isEmpty()) {
                try {
                    consumer.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            T result = queue.poll();
            producer.signal();
            return result;
        } finally {
            mutex.unlock();
        }
    }
    }
