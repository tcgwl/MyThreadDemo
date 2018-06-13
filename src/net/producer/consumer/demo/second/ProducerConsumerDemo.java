package net.producer.consumer.demo.second;

/**
 * 对于多个生产者和消费者
 * 1. 为什么要定义while判断标记？
 *  原因：让被唤醒的线程再次判断标记。
 *
 * 2. 为什么使用notifyAll？
 *  原因：需要唤醒对方线程。如果只用notify，容易出现只唤醒本方线程的情况，导致程序中的所有线程都等待。
 */
public class ProducerConsumerDemo {
    public static void main(String[] args) {
        Resource resource = new Resource();
        Producer producer = new Producer(resource);
        Consumer consumer = new Consumer(resource);

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
class Resource {
    private String name;
    private int count = 1;
    private boolean flag = false;

    public synchronized void put(String name) {
        while (flag) { //if->while: 线程被唤醒后先判断标记再进行操作，这样可能会导致线程全部等待，notify->notifyAll
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.name = name + "---" + count++;
        System.out.println(Thread.currentThread().getName()+"...生产者..."+this.name);

        flag = true;
        this.notifyAll();
    }

    public synchronized void take() {
        while (!flag) { //if->while: 线程被唤醒后先判断标记再进行操作，这样可能会导致线程全部等待，notify->notifyAll
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println(Thread.currentThread().getName()+"......消费者......"+this.name);

        flag = false;
        this.notify();
    }
}

class Producer implements Runnable {
    private Resource resource;

    public Producer(Resource resource) {
        this.resource = resource;
    }

    @Override
    public void run() {
        while (true) {
            resource.put("+商品+");
        }
    }
}

class Consumer implements Runnable {
    private Resource resource;

    public Consumer(Resource resource) {
        this.resource = resource;
    }

    @Override
    public void run() {
        while (true) {
            resource.take();
        }
    }
}
