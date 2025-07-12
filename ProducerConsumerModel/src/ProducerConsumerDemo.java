import java.util.LinkedList;
import java.util.Queue;

public class ProducerConsumerDemo {
    private static final int MAX_SIZE = 5;
    private final Queue<Integer> queue = new LinkedList<>();


    public static void main(String[] args) {
        Thread thread = new Thread();
        ProducerConsumerDemo demo = new ProducerConsumerDemo();
        // 同一个对ProducerConsumerDemo象创建的Consumer, Producer才会共享成员属性
        Consumer consumer = demo.new Consumer();
        Producer producer = demo.new Producer();

        consumer.start();
        producer.start();
    }

    // 一个Producer, 一个Consumer
    // Producer总是在队列满的时候调用wait()进入等待状态
    // Consumer总是在队列为空的时候调用wait()进入等待状态
    class Producer extends Thread {
        @Override
        public void run() {
            int value = 0;
            while (true) {
                /**
                 * 一个线程可以从挂起状态变为可以运行状态(也就是被唤醒)，
                 * 即使该线程没有被其他线程调用 notify（）、 notifyAll（）方法进行通知, 或者被中断, 或者等
                 * 待超时, 这就是所谓的虚假唤醒(发生概率很低)
                 */
                synchronized (queue) {
                    // 总是循环的去调用wait()方法, 防止被虚假唤醒发生
                    while (queue.size() == MAX_SIZE) {
                        try {
                            queue.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("生产者生产: " + value);
                    queue.add(value++);
                    queue.notifyAll();
                }
                try {
                    Thread.sleep(500); // 模拟生产耗时
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 消费者线程
    class Consumer extends Thread {
        @Override
        public void run() {
            while (true) {
                synchronized (queue) {
                    while (queue.isEmpty()) {
                        try {
                            queue.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    int val = queue.poll();
                    System.out.println("消费者消费: " + val);
                    queue.notifyAll();
                }
                try {
                    Thread.sleep(800); // 模拟消费耗时
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

