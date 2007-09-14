package com.infomancers.tests;

import com.infomancers.collections.concurrent.PriorityBlockingDeque;
import junit.framework.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.*;

/**
 * Created by IntelliJ IDEA.
 * User: aviadbd
 * Date: Sep 14, 2007
 * Time: 8:39:22 AM
 * To change this template use File | Settings | File Templates.
 */
public class PriorityBlockingDequeTests {

    private ExecutorService exec = Executors.newCachedThreadPool();

    @Test(timeout = 5000)
    public void firstConsumeThenProduce() throws ExecutionException, InterruptedException {
        final BlockingDeque<Integer> deque = new PriorityBlockingDeque<Integer>();

        Future<Integer> consumer = exec.submit(new ConsumerFirst<Integer>(deque));
        Future<Integer> producer = exec.submit(new Producer<Integer>(deque, 2));

        Assert.assertEquals("producer", 2, (int) producer.get());
        Assert.assertEquals("consumer", 2, (int) consumer.get());
    }

    @Test(timeout = 5000, expected = InterruptedException.class)
    public void takeBlocks() throws ExecutionException, InterruptedException {
        BlockingDeque<Integer> deque = new PriorityBlockingDeque<Integer>();
        exec.submit(new ConsumerFirst<Integer>(deque)).get();
    }

    @Test(timeout = 5000)
    public void twoConsumersTwoProducers() throws ExecutionException, InterruptedException {
        BlockingDeque<Integer> deque = new PriorityBlockingDeque<Integer>();
        Future<Integer> consumer1 = exec.submit(new ConsumerFirst<Integer>(deque));
        Future<Integer> consumer2 = exec.submit(new ConsumerFirst<Integer>(deque));
        Future<Integer> producer1 = exec.submit(new Producer<Integer>(deque, 2));
        Future<Integer> producer2 = exec.submit(new Producer<Integer>(deque, 2));

        Assert.assertEquals("producer", 2, (int) producer1.get());
        Assert.assertEquals("consumer", 2, (int) consumer1.get());
        Assert.assertEquals("producer", 2, (int) producer2.get());
        Assert.assertEquals("consumer", 2, (int) consumer2.get());
    }

    @Test(timeout = 5000, expected = InterruptedException.class)
    public void twoConsumersOneProducerBlocks() throws ExecutionException, InterruptedException {
        BlockingDeque<Integer> deque = new PriorityBlockingDeque<Integer>();
        exec.submit(new Producer<Integer>(deque, 2)).get();
        exec.submit(new ConsumerFirst<Integer>(deque)).get();
        exec.submit(new ConsumerFirst<Integer>(deque)).get();
    }

    @Test(timeout = 5000)
    public void priority() throws ExecutionException, InterruptedException {
        BlockingDeque<Integer> deque = new PriorityBlockingDeque<Integer>();
        int[] numbers = {5, 7, 1, 3, 2};
        int[] sortedNumbers = numbers.clone();
        Arrays.sort(sortedNumbers);

        for (int number : numbers) {
            exec.submit(new Producer<Integer>(deque, number)).get();
        }

        String array = "array = " + Arrays.toString(sortedNumbers);
        for (int number : sortedNumbers) {
            Assert.assertEquals(array, number, (int) exec.submit(new ConsumerFirst<Integer>(deque)).get());
        }
    }

    @Test(timeout = 5000)
    public void priorityWithComparator() throws ExecutionException, InterruptedException {
        BlockingDeque<Integer> deque = new PriorityBlockingDeque<Integer>(new Comparator<Integer>() {
            public int compare(Integer o1, Integer o2) {
                return o2.compareTo(o1);
            }
        }, Integer.MAX_VALUE);

        int[] numbers = {5, 7, 1, 3, 2};
        int[] sortedNumbers = numbers.clone();
        Arrays.sort(sortedNumbers);


        for (int number : numbers) {
            exec.submit(new Producer<Integer>(deque, number)).get();
        }

        String array = "array = " + Arrays.toString(sortedNumbers);
        for (int i = sortedNumbers.length - 1; i >= 0; i--) {
            Assert.assertEquals(array, sortedNumbers[i], (int) exec.submit(new ConsumerFirst<Integer>(deque)).get());
        }
    }

    @Test(timeout = 5000)
    public void firstConsumeLastThenProduce() throws ExecutionException, InterruptedException {
        final BlockingDeque<Integer> deque = new PriorityBlockingDeque<Integer>();

        Future<Integer> consumer = exec.submit(new ConsumerLast<Integer>(deque));
        Future<Integer> producer = exec.submit(new Producer<Integer>(deque, 2));

        Assert.assertEquals("producer", 2, (int) producer.get());
        Assert.assertEquals("consumer", 2, (int) consumer.get());
    }

    @Test(timeout = 5000, expected = InterruptedException.class)
    public void takeLastBlocks() throws ExecutionException, InterruptedException {
        BlockingDeque<Integer> deque = new PriorityBlockingDeque<Integer>();
        exec.submit(new ConsumerLast<Integer>(deque)).get();
    }

    @Test(timeout = 5000)
    public void twoConsumersLastTwoProducers() throws ExecutionException, InterruptedException {
        BlockingDeque<Integer> deque = new PriorityBlockingDeque<Integer>();
        Future<Integer> consumer1 = exec.submit(new ConsumerLast<Integer>(deque));
        Future<Integer> consumer2 = exec.submit(new ConsumerLast<Integer>(deque));
        Future<Integer> producer1 = exec.submit(new Producer<Integer>(deque, 2));
        Future<Integer> producer2 = exec.submit(new Producer<Integer>(deque, 2));

        Assert.assertEquals("producer", 2, (int) producer1.get());
        Assert.assertEquals("consumer", 2, (int) consumer1.get());
        Assert.assertEquals("producer", 2, (int) producer2.get());
        Assert.assertEquals("consumer", 2, (int) consumer2.get());
    }

    @Test(timeout = 5000, expected = InterruptedException.class)
    public void twoConsumersLastOneProducerBlocks() throws ExecutionException, InterruptedException {
        BlockingDeque<Integer> deque = new PriorityBlockingDeque<Integer>();
        exec.submit(new Producer<Integer>(deque, 2)).get();
        exec.submit(new ConsumerLast<Integer>(deque)).get();
        exec.submit(new ConsumerLast<Integer>(deque)).get();
    }

    @Test(timeout = 5000)
    public void priorityConsumerLast() throws ExecutionException, InterruptedException {
        BlockingDeque<Integer> deque = new PriorityBlockingDeque<Integer>();
        int[] numbers = {5, 7, 1, 3, 2};
        int[] sortedNumbers = numbers.clone();
        Arrays.sort(sortedNumbers);

        for (int number : numbers) {
            exec.submit(new Producer<Integer>(deque, number)).get();
        }

        String array = "array = " + Arrays.toString(sortedNumbers);
        for (int i = sortedNumbers.length - 1; i >= 0; i--) {
            Assert.assertEquals(array, sortedNumbers[i], (int) exec.submit(new ConsumerLast<Integer>(deque)).get());
        }
    }

    @Test(timeout = 5000)
    public void priorityWithComparatorConsumerLast() throws ExecutionException, InterruptedException {
        BlockingDeque<Integer> deque = new PriorityBlockingDeque<Integer>(new Comparator<Integer>() {
            public int compare(Integer o1, Integer o2) {
                return o2.compareTo(o1);
            }
        }, Integer.MAX_VALUE);

        int[] numbers = {5, 7, 1, 3, 2};
        int[] sortedNumbers = numbers.clone();
        Arrays.sort(sortedNumbers);


        for (int number : numbers) {
            exec.submit(new Producer<Integer>(deque, number)).get();
        }

        String array = "array = " + Arrays.toString(sortedNumbers);
        for (int number : sortedNumbers) {
            Assert.assertEquals(array, number, (int) exec.submit(new ConsumerLast<Integer>(deque)).get());
        }
    }

    class ConsumerFirst<E> implements Callable<E> {
        private BlockingDeque<E> deque;

        public ConsumerFirst(BlockingDeque<E> deque) {
            this.deque = deque;
        }

        public E call() throws Exception {
            return deque.takeFirst();
        }
    }

    class ConsumerLast<E> implements Callable<E> {
        private BlockingDeque<E> deque;

        public ConsumerLast(BlockingDeque<E> deque) {
            this.deque = deque;
        }

        public E call() throws Exception {
            return deque.takeLast();
        }
    }

    class Producer<E> implements Callable<E> {
        private BlockingDeque<E> deque;
        private E value;

        public Producer(BlockingDeque<E> deque, E value) {
            this.deque = deque;
            this.value = value;
        }

        public E call() throws Exception {
            deque.put(value);
            return value;
        }
    }
}
