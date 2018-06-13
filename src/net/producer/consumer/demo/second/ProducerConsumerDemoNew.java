package net.producer.consumer.demo.second;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * jdk 1.5 之后：
 * 将同步 synchronized 替换成显式 Lock 操作，
 * Condition 替代了 Object 监视器方法(wait/notify/notifyAll)的使用，
 * Condition 对象可以通过 Lock 获取，一个 Lock 可以对应多个 Condition。
 * 通过多个 Condition 可以实现本方只唤醒对方线程的操作。
 */
public class ProducerConsumerDemoNew {
    public static void main(String[] args) {
        ResourceNew resource = new ResourceNew();
        ProducerNew producer = new ProducerNew(resource);
        ConsumerNew consumer = new ConsumerNew(resource);

        Thread t1 = new Thread(producer);
        Thread t2 = new Thread(producer);
        Thread t3 = new Thread(consumer);
        Thread t4 = new Thread(consumer);

        t1.start();
        t2.start();
        t3.start();
        t4.start();
    }
}

/**
 * 资源
 */
class ResourceNew {
    private String name;
    private int count = 1;
    private boolean flag = false;
    private Lock lock = new ReentrantLock();
    private Condition producerCondition = lock.newCondition();
    private Condition consumerCondition = lock.newCondition();

    public void put(String name) throws InterruptedException {
        lock.lock();
        try {
            while (flag) {
                producerCondition.await();
            }

            this.name = name + "-" + count++;
            System.out.println(Thread.currentThread().getName()+"...生产者..."+this.name);

            flag = true;
            consumerCondition.signal();
        } finally {
            lock.unlock();
        }
    }

    public void take() throws InterruptedException {
        lock.lock();
        try {
            while (!flag) {
                consumerCondition.await();
            }

            System.out.println(Thread.currentThread().getName()+"......消费者......"+this.name);
            flag = false;

            producerCondition.signal();
        } finally {
            lock.unlock();
        }
    }
}

class ProducerNew implements Runnable {
    private ResourceNew resource;

    public ProducerNew(ResourceNew resource) {
        this.resource = resource;
    }

    @Override
    public void run() {
        while (true) {
            try {
                resource.put("商品");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class ConsumerNew implements Runnable {
    private ResourceNew resource;

    public ConsumerNew(ResourceNew resource) {
        this.resource = resource;
    }

    @Override
    public void run() {
        while (true) {
            try {
                resource.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
