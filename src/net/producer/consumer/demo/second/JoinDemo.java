package net.producer.consumer.demo.second;

/**
join:
当A线程执行到了B线程的.join()方法时，A就会等待。等B线程都执行完，A才会执行。

join可以用来临时加入线程执行。

 yield：暂停当前正在执行的线程对象，并执行其他线程。即放弃执行权。

*/

class Demo implements Runnable {
    public void run() {
        for(int x=0; x<70; x++) {
            System.out.println(Thread.currentThread().getName()+"....."+x);
            //Thread.yield();
        }
    }
}


class JoinDemo {
    public static void main(String[] args) throws Exception {
        Demo d = new Demo();
        Thread t1 = new Thread(d);
        Thread t2 = new Thread(d);
        t1.start();
        t1.join();//main线程等待t1执行完才会继续执行

        //t1.setPriority(Thread.MAX_PRIORITY);

        t2.start();


        for(int x=0; x<80; x++) {
            System.out.println("main....."+x);
        }
        System.out.println("over");
    }
}

