package net.producer.consumer.demo.first;

/**
 * 死锁：
 * 同步中嵌套同步。
 */
public class DeadLockDemo {
    public static void main(String[] args) {
        Thread t1 = new Thread(new Test(true));
        Thread t2 = new Thread(new Test(false));

        t1.start();
        t2.start();
    }
}

class MyLock {
    static Object lockA = new Object();
    static Object lockB = new Object();
}

class Test implements Runnable {
    private boolean flag;

    public Test(boolean flag) {
        this.flag = flag;
    }

    @Override
    public void run() {
        if (flag) {
            while (true) {
                synchronized (MyLock.lockA) {
                    System.out.println(Thread.currentThread().getName()+"...if lockA");
                    synchronized (MyLock.lockB) {
                        System.out.println(Thread.currentThread().getName()+"...if lockB");
                    }
                }
            }
        } else {
            while (true) {
                synchronized (MyLock.lockB) {
                    System.out.println(Thread.currentThread().getName()+"...else lockB");
                    synchronized (MyLock.lockA) {
                        System.out.println(Thread.currentThread().getName()+"...else lockA");
                    }
                }
            }
        }
    }
}
