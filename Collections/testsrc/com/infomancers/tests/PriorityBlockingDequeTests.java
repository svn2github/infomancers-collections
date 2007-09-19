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

    @Test(timeout = 5000)
    public void firstConsumeThenProduce() throws ExecutionException, InterruptedException {
        final ExecutorService exec = createExec();
        final BlockingDeque<Integer> deque = new PriorityBlockingDeque<Integer>();

        Future<Integer> consumer = exec.submit(new ConsumerFirst<Integer>(deque));
        Future<Integer> producer = produce(exec, deque, 2);

        Assert.assertEquals("producer", 2, (int) producer.get());
        Assert.assertEquals("consumer", 2, (int) consumer.get());
    }

    private Future<Integer> produce(ExecutorService exec, BlockingDeque<Integer> deque, int value) {
        return exec.submit(new Producer<Integer>(deque, value));
    }

    @Test(timeout = 5000, expected = InterruptedException.class)
    public void takeBlocks() throws ExecutionException, InterruptedException {
        final ExecutorService exec = createExec();
        BlockingDeque<Integer> deque = new PriorityBlockingDeque<Integer>();
        consumeFirst(exec, deque);
    }

    @Test(timeout = 5000)
    public void twoConsumersTwoProducers() throws ExecutionException, InterruptedException {
        final ExecutorService exec = createExec();
        BlockingDeque<Integer> deque = new PriorityBlockingDeque<Integer>();
        Future<Integer> consumer1 = exec.submit(new ConsumerFirst<Integer>(deque));
        Future<Integer> consumer2 = exec.submit(new ConsumerFirst<Integer>(deque));
        Future<Integer> producer1 = produce(exec, deque, 2);
        Future<Integer> producer2 = produce(exec, deque, 2);

        Assert.assertEquals("producer", 2, (int) producer1.get());
        Assert.assertEquals("consumer", 2, (int) consumer1.get());
        Assert.assertEquals("producer", 2, (int) producer2.get());
        Assert.assertEquals("consumer", 2, (int) consumer2.get());
    }

    @Test(timeout = 5000, expected = InterruptedException.class)
    public void twoConsumersOneProducerBlocks() throws ExecutionException, InterruptedException {
        final ExecutorService exec = createExec();
        BlockingDeque<Integer> deque = new PriorityBlockingDeque<Integer>();
        produce(exec, deque, 2).get();
        consumeFirst(exec, deque);
        consumeFirst(exec, deque);
    }

    @Test(timeout = 5000)
    public void priority() throws ExecutionException, InterruptedException {
        final ExecutorService exec = createExec();
        BlockingDeque<Integer> deque = new PriorityBlockingDeque<Integer>();
        int[] numbers = {5, 7, 1, 3, 2};
        int[] sortedNumbers = numbers.clone();
        Arrays.sort(sortedNumbers);

        for (int number : numbers) {
            produce(exec, deque, number).get();
        }

        String array = "array = " + Arrays.toString(sortedNumbers);
        for (int number : sortedNumbers) {
            Assert.assertEquals(array, number, consumeFirst(exec, deque));
        }
    }

    @Test(timeout = 5000)
    public void priorityWithComparator() throws ExecutionException, InterruptedException {
        final ExecutorService exec = createExec();
        BlockingDeque<Integer> deque = new PriorityBlockingDeque<Integer>(new Comparator<Integer>() {
            public int compare(Integer o1, Integer o2) {
                return o2.compareTo(o1);
            }
        }, Integer.MAX_VALUE);

        int[] numbers = {5, 7, 1, 3, 2};
        int[] sortedNumbers = numbers.clone();
        Arrays.sort(sortedNumbers);


        for (int number : numbers) {
            produce(exec, deque, number).get();
        }

        String array = "array = " + Arrays.toString(sortedNumbers);
        for (int i = sortedNumbers.length - 1; i >= 0; i--) {
            Assert.assertEquals(array, sortedNumbers[i], consumeFirst(exec, deque));
        }
    }

    @Test(timeout = 5000)
    public void firstConsumeLastThenProduce() throws ExecutionException, InterruptedException {
        final ExecutorService exec = createExec();
        final BlockingDeque<Integer> deque = new PriorityBlockingDeque<Integer>();

        Future<Integer> consumer = exec.submit(new ConsumerLast<Integer>(deque));
        Future<Integer> producer = produce(exec, deque, 2);

        Assert.assertEquals("producer", 2, (int) producer.get());
        Assert.assertEquals("consumer", 2, (int) consumer.get());
    }

    @Test(timeout = 5000, expected = InterruptedException.class)
    public void takeLastBlocks() throws ExecutionException, InterruptedException {
        final ExecutorService exec = createExec();
        BlockingDeque<Integer> deque = new PriorityBlockingDeque<Integer>();
        consumeLast(exec, deque);
    }

    @Test(timeout = 5000)
    public void twoConsumersLastTwoProducers() throws ExecutionException, InterruptedException {
        final ExecutorService exec = createExec();
        BlockingDeque<Integer> deque = new PriorityBlockingDeque<Integer>();
        Future<Integer> consumer1 = exec.submit(new ConsumerLast<Integer>(deque));
        Future<Integer> consumer2 = exec.submit(new ConsumerLast<Integer>(deque));
        Future<Integer> producer1 = produce(exec, deque, 2);
        Future<Integer> producer2 = produce(exec, deque, 2);

        Assert.assertEquals("producer", 2, (int) producer1.get());
        Assert.assertEquals("consumer", 2, (int) consumer1.get());
        Assert.assertEquals("producer", 2, (int) producer2.get());
        Assert.assertEquals("consumer", 2, (int) consumer2.get());
    }

    @Test(timeout = 5000, expected = InterruptedException.class)
    public void twoConsumersLastOneProducerBlocks() throws ExecutionException, InterruptedException {
        final ExecutorService exec = createExec();
        BlockingDeque<Integer> deque = new PriorityBlockingDeque<Integer>();
        produce(exec, deque, 2).get();
        exec.submit(new ConsumerLast<Integer>(deque)).get();
        exec.submit(new ConsumerLast<Integer>(deque)).get();
    }

    @Test(timeout = 5000)
    public void priorityConsumerLast() throws ExecutionException, InterruptedException {
        final ExecutorService exec = createExec();
        BlockingDeque<Integer> deque = new PriorityBlockingDeque<Integer>();
        int[] numbers = {5, 7, 1, 3, 2};
        int[] sortedNumbers = numbers.clone();
        Arrays.sort(sortedNumbers);

        for (int number : numbers) {
            produce(exec, deque, number).get();
        }

        String array = "array = " + Arrays.toString(sortedNumbers);
        for (int i = sortedNumbers.length - 1; i >= 0; i--) {
            Assert.assertEquals(array, sortedNumbers[i], consumeLast(exec, deque));
        }
    }

    @Test(timeout = 5000)
    public void priorityWithComparatorConsumerLast() throws ExecutionException, InterruptedException {
        final ExecutorService exec = createExec();
        BlockingDeque<Integer> deque = new PriorityBlockingDeque<Integer>(new InvertedComparator(), Integer.MAX_VALUE);

        int[] numbers = {5, 7, 1, 3, 2};
        int[] sortedNumbers = numbers.clone();
        Arrays.sort(sortedNumbers);


        for (int number : numbers) {
            produce(exec, deque, number).get();
        }

        String array = "array = " + Arrays.toString(sortedNumbers);
        for (int number : sortedNumbers) {
            Assert.assertEquals(array, number, consumeLast(exec, deque));
        }
    }

    @Test(timeout = 5000)
    public void onePollFirstConsumerOneProducer() throws ExecutionException, InterruptedException {
        final ExecutorService exec = createExec();
        BlockingDeque<Integer> deque = new PriorityBlockingDeque<Integer>();

        produce(exec, deque, 2).get();

        Assert.assertEquals(2, (int) pollFirst(exec, deque));
    }

    @Test(timeout = 5000)
    public void onePollFirstReturnsNull() throws ExecutionException, InterruptedException {
        final ExecutorService exec = createExec();
        BlockingDeque<Integer> deque = new PriorityBlockingDeque<Integer>();

        Assert.assertNull(pollFirst(exec, deque));
    }

    @Test(timeout = 5000)
    public void twoPollFirstOneProducerReturnsNull() throws ExecutionException, InterruptedException {
        final ExecutorService exec = createExec();
        BlockingDeque<Integer> deque = new PriorityBlockingDeque<Integer>();
        produce(exec, deque, 2).get();

        Assert.assertEquals(2, (int) pollFirst(exec, deque));
        Assert.assertNull(pollFirst(exec, deque));
    }

    @Test(timeout = 5000)
    public void onePollLastConsumerOneProducer() throws ExecutionException, InterruptedException {
        final ExecutorService exec = createExec();
        BlockingDeque<Integer> deque = new PriorityBlockingDeque<Integer>();

        produce(exec, deque, 2).get();

        Assert.assertEquals(2, (int) pollLast(exec, deque));
    }

    @Test(timeout = 5000)
    public void onePollLastReturnsNull() throws ExecutionException, InterruptedException {
        final ExecutorService exec = createExec();
        BlockingDeque<Integer> deque = new PriorityBlockingDeque<Integer>();

        Assert.assertNull(pollLast(exec, deque));
    }

    @Test(timeout = 5000)
    public void twoPollLastOneProducerReturnsNull() throws ExecutionException, InterruptedException {
        final ExecutorService exec = createExec();
        BlockingDeque<Integer> deque = new PriorityBlockingDeque<Integer>();
        produce(exec, deque, 2).get();

        Assert.assertEquals(2, (int) pollFirst(exec, deque));
        Assert.assertNull(pollLast(exec, deque));
    }

    @Test(timeout = 5000)
    public void allowDuplicateValues() throws ExecutionException, InterruptedException {
        final ExecutorService exec = createExec();
        BlockingDeque<Integer> deque = new PriorityBlockingDeque<Integer>();
        produce(exec, deque, 5).get();
        produce(exec, deque, 5).get();
        produce(exec, deque, 5).get();

        Assert.assertEquals(5, consumeFirst(exec, deque));
        Assert.assertEquals(5, consumeFirst(exec, deque));
        Assert.assertEquals(5, consumeFirst(exec, deque));
    }


    static class ConsumerFirst<E> implements Callable<E> {
        private BlockingDeque<E> deque;

        public ConsumerFirst(BlockingDeque<E> deque) {
            this.deque = deque;
        }

        public E call() throws Exception {
            return deque.takeFirst();
        }
    }

    static class ConsumerLast<E> implements Callable<E> {
        private BlockingDeque<E> deque;

        public ConsumerLast(BlockingDeque<E> deque) {
            this.deque = deque;
        }

        public E call() throws Exception {
            return deque.takeLast();
        }
    }

    static class Producer<E> implements Callable<E> {
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

    private static class InvertedComparator implements Comparator<Integer> {
        public int compare(Integer o1, Integer o2) {
            return o2.compareTo(o1);
        }
    }

    private int consumeFirst(ExecutorService exec, BlockingDeque<Integer> deque) throws InterruptedException, ExecutionException {
        return (int) exec.submit(new ConsumerFirst<Integer>(deque)).get();
    }

    private int consumeLast(ExecutorService exec, BlockingDeque<Integer> deque) throws InterruptedException, ExecutionException {
        return (int) exec.submit(new ConsumerLast<Integer>(deque)).get();
    }

    private Integer pollFirst(ExecutorService exec, final BlockingDeque<Integer> deque) throws ExecutionException, InterruptedException {
        return exec.submit(new Callable<Integer>() {
            public Integer call() throws Exception {
                return deque.pollFirst();
            }
        }).get();
    }

    private Integer pollLast(ExecutorService exec, final BlockingDeque<Integer> deque) throws ExecutionException, InterruptedException {
        return exec.submit(new Callable<Integer>() {
            public Integer call() throws Exception {
                return deque.pollLast();
            }
        }).get();
    }

    private ExecutorService createExec() {
        return Executors.newFixedThreadPool(5);
    }
}
