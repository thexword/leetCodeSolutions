package com.example;

import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class TestClass {
    @Test
    public void testJDBC() {
        ResourceBundle resource = ResourceBundle.getBundle("jdbc");
        String url = "jdbc:mysql://localhost/testdb";
        String user = resource.getString("user");
        String password = resource.getString("password");

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            connection = JDBCUtils.getConnection(url, user, password);
            String sql = "select * from t_student";
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                String age = rs.getString(3);

                Student student = new Student();
                student.setId(id);
                student.setName(name);
                student.setAge(age);
                System.out.println(student);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void testBinarySearch2() {
        BinarySearch2 binarySearch2 = new BinarySearch2();

        int[] numbs = new int[] {
                5, 7, 7, 8, 8, 10
        };
        int target = 6;

        int[] res = binarySearch2.searchRange(numbs, target);
        for (int re : res) {
            System.out.println(re + ", ");
        }
    }

    @Test
    public void testIsBipartite() {
        int[][] graph = new int[][] {
                {1,3},
                {0,2},
                {1,3},
                {0,2}
        };

        int[][] graph1 = new int[][] {
                {1,2},
                {0,2},
                {1, 0},
        };

        System.out.println(new IsBipartite().isBipartite(graph));
    }

    @Test
    public void testThreadPoolExecutor() throws ExecutionException, InterruptedException {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(3,
                5, 1000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(5));

        threadPoolExecutor.execute(()->{
            System.out.println(1);
        });

        Future<?> submit = threadPoolExecutor.submit(() -> {
            System.out.println(2);
            return 3;
        });

        System.out.println(submit.get());

        threadPoolExecutor.shutdown();
    }

    @Test
    public void testCallable() {
        FutureTask<String> futureTask = new FutureTask<>(()->{
            return "test";
        });

        new Thread(futureTask).start();

        try {
            System.out.println(futureTask.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAtomic() {
        AtomicInteger atomicInteger = new AtomicInteger(1);
        System.out.println(atomicInteger.get());
        System.out.println(atomicInteger.compareAndSet(1, 2));
        System.out.println(atomicInteger.compareAndSet(1, 3));
        System.out.println(atomicInteger.get());

        System.out.println("-----");
        AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(new int[] {1, 2});
        System.out.println(atomicIntegerArray.getAndAdd(0, 1));
        System.out.println(atomicIntegerArray.get(0));

        System.out.println("-----");
        AtomicBoolean atomicBoolean = new AtomicBoolean();
        System.out.println(atomicBoolean);
        atomicBoolean.set(true);
        System.out.println(atomicBoolean);

        System.out.println("-----");
        AtomicReference<Person> objectAtomicReference = new AtomicReference<>();
        Person tom = new Person("tom", 16);
        objectAtomicReference.set(tom);
        System.out.println(objectAtomicReference.get());

        Person alice = new Person("alice", 15);
        objectAtomicReference.compareAndSet(tom, alice);
        System.out.println(objectAtomicReference.get());

        System.out.println("-----");
        AtomicIntegerFieldUpdater<Person> age = AtomicIntegerFieldUpdater.newUpdater(Person.class, "age");
        System.out.println(age.get(tom));
        System.out.println(age.getAndIncrement(tom));
        System.out.println(age.get(tom));
    }

    static class Person {
        String name;
        volatile int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }
    
    @Test
    public void testSemaphore() {
//        AbstractQueuedSynchronizer
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 100, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<>(5));
        Semaphore semaphore = new Semaphore(5, true);

        for (int i = 0; i < 50; ++i) {
            int threadNum = i;
            threadPoolExecutor.execute(()->{
                try {
                    semaphore.acquire();
                    Thread.sleep(1000);
                    System.out.println("Thread: "+ threadNum + ", " + Thread.currentThread().getName());
                    Thread.sleep(1000);
                    semaphore.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        threadPoolExecutor.shutdown();
    }

    @Test
    public void testCountDownLatch() {
        CountDownLatch countDownLatch = new CountDownLatch(5);

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 100, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<>(5));

        for (int i = 0; i < 5; i++) {
            threadPoolExecutor.execute(() -> {
                try {
                    Thread.sleep(1000);
                    System.out.println(Thread.currentThread().getName());
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        threadPoolExecutor.shutdown();
        System.out.println("finish");
    }

    @Test
    public void testCyclicBarrier() {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                5, 100, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<>(5));

        CyclicBarrier cyclicBarrier = new CyclicBarrier(5, () -> {
            System.out.println("------当线程数达到之后，优先执行------");
        });

        for (int i = 0; i < 10; i++) {
            int threadNum = i;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            threadPoolExecutor.execute(() -> {
                try {
                    System.out.println("threadNum:" + threadNum + " is ready");
                    // 等待60秒，保证子线程完全执行结束
                    cyclicBarrier.await(60, TimeUnit.SECONDS);
                    System.out.println("threadNum:" + threadNum + " is finish");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException | TimeoutException e) {
                    e.printStackTrace();
                }
            });
        }

        try {
            Thread.sleep(100 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        threadPoolExecutor.shutdown();
    }

    @Test
    public void testReentrantLock() {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                5, 100, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<>(5));

        ReentrantLock reentrantLock = new ReentrantLock();
        Condition condition = reentrantLock.newCondition();

        threadPoolExecutor.execute(() -> {
            try {
                reentrantLock.lock();

                System.out.println("await时间为：" + System.currentTimeMillis());
                condition.await();
                System.out.println("await等待结束");

            } catch (Exception e) {

            } finally {
                reentrantLock.unlock();
            }
        });

        threadPoolExecutor.execute(() -> {
            try {
                reentrantLock.lock();

                Thread.sleep(2000);
                System.out.println("signal时间为：" + System.currentTimeMillis());
                condition.signal();
            } catch (Exception e) {

            } finally {
                reentrantLock.unlock();
            }
        });

        try {
            Thread.sleep(100*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        threadPoolExecutor.shutdown();

//        ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
//        ReentrantReadWriteLock.ReadLock readLock = reentrantReadWriteLock.readLock();
//        readLock.lock();
//        readLock.unlock();
//        ReentrantReadWriteLock.WriteLock writeLock = reentrantReadWriteLock.writeLock();
//        writeLock.lock();
//        writeLock.unlock();
    }
}
