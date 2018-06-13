package net.producer.consumer.demo.first;

/**
 同步函数用的是哪一个锁呢？
 函数需要被对象调用，那么函数都有一个所属对象引用，就是this。
 所以同步函数使用的锁是this。

 通过该程序进行验证：
     使用两个线程来买票。
     一个线程在同步代码块中。
     一个线程在同步函数中。
     都在执行买票动作。


 同理：
 如果同步函数被静态static修饰后，使用的锁是什么呢？

 通过验证，发现不再是this，因为静态方法中不可以定义this。

 静态进内存时，内存中没有本类对象，但是一定有该类对应的字节码文件对象：
 类名.class  该对象的类型是Class

 静态的同步方法，使用的锁是该方法所在类的字节码文件对象：类名.class

 */
public class ThisLockDemo {
    public static void main(String[] args) {
        Ticket t = new Ticket();
        Thread t1 = new Thread(t);
        Thread t2 = new Thread(t);

        t1.start();
        try{Thread.sleep(10);}catch(Exception e){}
        t.flag = false;
        t2.start();
    }
}

class Ticket implements Runnable {
    private int tick = 100;
    boolean flag = true;

    @Override
    public void run() {
        if(flag) {
            while(true) {
                synchronized(this) {
                    if(tick>0) {
                        try{Thread.sleep(10);}catch(Exception e){}
                        System.out.println(Thread.currentThread().getName()+"......show: "+ tick--);
                    }
                }
            }
        }
        else
            while(true)
                show();
    }

    public synchronized void show() {//this
        if(tick>0) {
            try{Thread.sleep(10);}catch(Exception e){}
            System.out.println(Thread.currentThread().getName()+"...show: "+ tick--);
        }
    }
}