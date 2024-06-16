package com.example.pokecenter.customer.lam.Iterator;

import java.util.LinkedList;
import java.util.Queue;

public class BFS<T> implements Iterator<T> {
    private final Vertex<T> startVertex;
    private Queue<Vertex<T>> queue = new LinkedList<>();

    public BFS(Vertex<T> startVertex) {
        this.startVertex = startVertex;
        queue.add(startVertex);
    }

    public boolean hasNext() {
        return !queue.isEmpty();
    }

    public Vertex<T> getNext() {
        if (!hasNext()) {
            return null;
        }
        Vertex<T> current = queue.remove();
        if (!current.isVisited()) {
            current.setVisited(true);
            current.getNeighbors().forEach(neighbor -> {
                if (!neighbor.isVisited()) {
                    queue.add(neighbor);
                }
            });
            return current;
        }

        return getNext();
    }

    public void reset() {
        queue.clear();
        queue.add(startVertex);
        startVertex.setVisited(false);
    }
}
