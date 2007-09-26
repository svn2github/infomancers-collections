package com.infomancers.tests;

import com.infomancers.collections.graph.GraphAdapter;
import com.infomancers.collections.iterators.GraphIterators;
import com.infomancers.collections.yield.Yielder;
import junit.framework.Assert;
import org.junit.Test;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: aviadbd
 * Date: Jul 23, 2007
 * Time: 8:57:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class GraphTests {

    private class GraphNode {
        private final int value;
        private Collection<GraphNode> neighbours = new LinkedList<GraphNode>();


        public int getValue() {
            return value;
        }

        GraphNode(int i) {
            this.value = i;
        }

        void addNeighbours(GraphNode... nodes) {
            for (GraphNode node : nodes) {
                neighbours.add(node);
            }
        }


        @Override
        public String toString() {
            return "node " + value;
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            GraphNode graphNode = (GraphNode) o;

            if (value != graphNode.value) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return value;
        }
    }

    private class Graph implements GraphAdapter {
        private final GraphNode[] nodes;

        public Graph(GraphNode... nodes) {

            this.nodes = nodes;
        }


        /**
         * Retrieves a node at a certain index.
         * <p/>
         * As long as nodes are not being added or deleted,
         * it is assured that a certain index will retrieve
         * the same node.
         *
         * @param index The index of the node.
         * @return A node.
         */
        public Object getNode(int index) {
            return nodes[index];
        }

        /**
         * Retreves the list of a node's neighbours.
         *
         * @param node The node to retrieve its neighbours.
         * @return The neighbour nodes.
         */
        public Iterable<Object> getNodeNeighbours(final Object node) {
            return new Yielder<Object>() {

                @Override
                protected void yieldNextCore() {
                    for (GraphNode neighbour : ((GraphNode) node).neighbours) {
                        yieldReturn(neighbour);
                    }
                }
            };
        }
    }

    @Test
    public void dfsOneElement() {
        GraphNode node = new GraphNode(1);
        Graph graph = new Graph(node);

        Iterator<Object> it = GraphIterators.depthFirstSearch(graph).iterator();

        Assert.assertEquals(node, it.next());
        Assert.assertFalse("Too many elements", it.hasNext());
    }

    @Test
    public void dfsOneRecursiveElement() {
        GraphNode node = new GraphNode(1);
        node.addNeighbours(node);
        Graph graph = new Graph(node);

        Iterator<Object> it = GraphIterators.depthFirstSearch(graph).iterator();

        Assert.assertEquals(node, it.next());
        Assert.assertFalse("Too many elements", it.hasNext());
    }

    @Test(timeout = 2000)
    public void dfsTwoElements() {
        GraphNode node1 = new GraphNode(1);
        GraphNode node2 = new GraphNode(2);

        node1.addNeighbours(node2);
        Graph graph = new Graph(node1, node2);

        Iterator<Object> it = GraphIterators.depthFirstSearch(graph).iterator();

        Assert.assertEquals(node1, it.next());
        Assert.assertEquals(node2, it.next());
        Assert.assertFalse("Too many elements", it.hasNext());
    }

    @Test
    public void dfsTwoLoopingElements() {
        GraphNode node1 = new GraphNode(1);
        GraphNode node2 = new GraphNode(2);

        node1.addNeighbours(node2);
        node2.addNeighbours(node1);
        Graph graph = new Graph(node1, node2);

        Iterator<Object> it = GraphIterators.depthFirstSearch(graph).iterator();

        Assert.assertEquals(node1, it.next());
        Assert.assertEquals(node2, it.next());
        Assert.assertFalse("Too many elements", it.hasNext());
    }

    @Test
    public void dfsFiveFullMeshElements() {
        GraphNode node1 = new GraphNode(1);
        GraphNode node2 = new GraphNode(2);
        GraphNode node3 = new GraphNode(3);
        GraphNode node4 = new GraphNode(4);
        GraphNode node5 = new GraphNode(5);

        node1.addNeighbours(node2, node3, node4, node5);
        node2.addNeighbours(node1, node3, node4, node5);
        node3.addNeighbours(node1, node2, node4, node5);
        node4.addNeighbours(node1, node2, node3, node5);
        node5.addNeighbours(node1, node2, node3, node4);
        Graph graph = new Graph(node1, node2, node3, node4, node5);

        Iterable<Object> it = GraphIterators.depthFirstSearch(graph);
        List<GraphNode> nodes = new LinkedList(Arrays.asList(graph.nodes));
        for (Object item : it) {
            Assert.assertTrue("Element " + item + " doesn't really exist", nodes.remove(item));
        }
        Assert.assertEquals("Not all elements yielded", 0, nodes.size());
    }

    @Test
    public void dfsFiveRecursiveFullMeshElements() {
        GraphNode node1 = new GraphNode(1);
        GraphNode node2 = new GraphNode(2);
        GraphNode node3 = new GraphNode(3);
        GraphNode node4 = new GraphNode(4);
        GraphNode node5 = new GraphNode(5);

        node1.addNeighbours(node1, node2, node3, node4, node5);
        node2.addNeighbours(node1, node2, node3, node4, node5);
        node3.addNeighbours(node1, node2, node3, node4, node5);
        node4.addNeighbours(node1, node2, node3, node4, node5);
        node5.addNeighbours(node1, node2, node3, node4, node5);
        Graph graph = new Graph(node1, node2, node3, node4, node5);

        Iterable<Object> it = GraphIterators.depthFirstSearch(graph);
        List<GraphNode> nodes = new LinkedList(Arrays.asList(graph.nodes));
        for (Object item : it) {
            Assert.assertTrue("Element " + item + " doesn't really exist", nodes.remove(item));
        }
        Assert.assertEquals("Not all elements yielded", 0, nodes.size());
    }
}
