package net.producer.consumer.demo.first;


/**
需求：简单的卖票程序。
多个窗口同时买票。

 -------第一重点：创建线程的两种方式--------

 进程：正在执行的程序。
 线程：是进程中用于控制程序执行的控制单元(执行路径，执行情景)

 进程中至少有一个线程。

 对于JVM，启动时，至少有两个线程：jvm的主线程，jvm的垃圾回收线程。


 如何在程序中自定义线程呢？

 Java给我们提供了对象线程这类事物的描述，该类是Thread

 该类中定义了，
 创建线程对象的方法(构造函数).
 提供了要被线程执行的代码存储的位置（run()）

 还定义了开启线程运行的方法(start()：该方法两个作用：启动线程，调用run方法。).
 同时还有一些其他的方法用于操作线程：
 static Thread currentThead():
 String getName():
 static void sleep(time)throws InterruptedException:


 要运行的代码都是后期定义的。
 所以创建线程的第一种方式是：继承Thread类。原因：要覆盖run方法，定义线程要运行的代码。

 步骤：
 1，继承Thread类。
 2，覆盖run方法。将线程要运行的代码定义其中。
 3，创建Thread类的子类对象，其实就是在创建线程，调用start方法。


 如果自定义的类中有多线程要运行的代码。但是该类有自己的父类。
 那么就不可以在继承Thread。怎么办呢？

 Java给我们提供了一个规则。Runnable接口。
 如果自定义类不继承Thread，也可以实现Runnable接口。并将多线程要运行的代码存放在Runnable的run方法中。
 这样多线程也可以帮助该类运行。
 这样的操作有一个好处：避免了单继承的局限性。

 创建线程的第二种方式：实现Runnable接口。

 步骤：
 1，定义了实现Runnable接口。
 2，覆盖接口的run方法。将多线程要运行的代码存入其中。
 3，创建Thread类的对象(创建线程),并将Runnable接口的子类对象作为参数传递给Thread的构造函数。
 为什么要传递？因为线程要运行的代码都在Runnable子类的run方法中存储。所以要将该run方法所属的对象
 传递给Thread。让Thread线程去使用该对象调用其run方法。
 4，调用Thread对象的start方法。开启线程。


 两种方式的特点：
 实现方式，因为避免了单继承的局限性，所以创建线程建议使用第二种方式。




 -------第二重点：同步的所有特性-------

 作为了解：
 线程的状态。
 1，被创建。
 2，运行。
 3，冻结。
 4，消亡。

 其时还有一种特殊的状态：临时状态。
 该临时状态的特点：具备了执行资格，但不具备执行权。
 冻结状态的特点：放弃了执行资格。

 多线程具备随机性。因为是由cpu不断的快速切换造成的。
 就有可能会产生多线程的安全问题。

 问题的产生的原因：
 几个关键点：
 1，多线程代码中有操作共享数据。
 2，多条语句操作该共享数据。

 当具备两个关键点时，
 有一个线程对多条操作共享数据的代码执行的一部分。还没有执行完，另一个线程开始参与执行。
 就会发生数据错误。


 解决方法：
 当一个线程在执行多条操作共享数据代码时，其他线程即使获取了执行权，也不可以参与操作。

 Java就对这种解决方式提供了专业的代码。
 同步
 同步的原理：就是将部分操作功能数据的代码进行加锁。

 示例：火车上的卫生间。

 同步的表现形式：
 1，同步代码块。
 2，同步函数。
 两者有什么不同：
 同步代码块使用的锁是任意对象。
 同步函数使用的锁是this。

 注意：对于static的同步函数，使用的锁不是this。是 类名.class 是该类的字节码文件对象。
 涉及到了单例设计模式的懒汉式。


 同步的好处：解决了线程的安全问题。
 弊端：
    较为消耗资源。
    同步嵌套后，容易死锁。

 同步使用的前提：
 1，必须是两个或者两个以上的线程。
 2，必须是多个线程使用同一个锁。
 这时才可以称为这些线程被同步了。

 死锁代码一定会写。但开发时一定注意避免。

*/

class TicketDemo {
    public static void main(String[] args) {
        MyTicket t = new MyTicket();

        Thread t1 = new Thread(t);//创建了一个线程
        Thread t2 = new Thread(t);
        Thread t3 = new Thread(t);
        Thread t4 = new Thread(t);
        t1.start();
        t2.start();
        t3.start();
        t4.start();


		/*
		//extends Thread
		Ticket t1 = new Ticket();
		//Ticket t2 = new Ticket();
		//Ticket t3 = new Ticket();
		//Ticket t4 = new Ticket();

		t1.start();
		t1.start();
		t1.start();
		t1.start();
		*/

    }

}

class MyTicket implements Runnable {//extends Thread
    private  int tick = 1000;
    Object obj = new Object();
    public void run()
    {
        while(true)
        {
            synchronized(obj)
            {
                if(tick>0)
                {
                    //try{Thread.sleep(10);}catch(Exception e){}
                    System.out.println(Thread.currentThread().getName()+"....sale : "+ tick--);
                }
            }
        }
    }
}