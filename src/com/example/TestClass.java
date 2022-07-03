package com.example;

import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

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
    }
}
