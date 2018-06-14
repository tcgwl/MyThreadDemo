package net.producer.consumer.demo.second;

/**
stop方法已经过时。

如何停止线程？
只有一种，run方法结束。
开启多线程运行，运行代码通常是循环结构。
只要控制住循环，就可以让run方法结束，也就是线程结束。


特殊情况：
当线程处于了冻结状态。
就不会读取到标记。那么线程就不会结束。

当没有指定的方式让冻结的线程恢复到运行状态时，这时需要对冻结进行清除。
强制让线程恢复到运行状态中来。这样就可以操作标记让线程结束。

Thread类提供该方法 interrupt();

 setDaemon(true):将该线程标记为守护线程。当正在运行的线程都是守护线程时，Java虚拟机退出。
 该方法必须在启动线程前调用。
 经典例子：圣斗士和雅典娜
*/
class StopThread implements Runnable {
    private boolean flag =true;

    public void run() {
        while(flag) {
            System.out.println(Thread.currentThread().getName()+"....run");
        }
    }

    public void changeFlag() {
        flag = false;
    }
}

class StopThreadDemo {
    public static void main(String[] args) {
        StopThread st = new StopThread();

        Thread t1 = new Thread(st);
        Thread t2 = new Thread(st);

//        t1.setDaemon(true); // true:守护线程, false:用户线程(默认)
//        t2.setDaemon(true);
        t1.start();
        t2.start();

        int num = 0;

        while(true) {
            if(num++ == 500) {
                st.changeFlag();
//                t1.interrupt();//run方法中捕获InterruptedException后，将flag设为false，停止线程
//                t2.interrupt();
                break;
            }
            System.out.println(Thread.currentThread().getName()+"......."+num);
        }
        System.out.println("over");
    }
}

