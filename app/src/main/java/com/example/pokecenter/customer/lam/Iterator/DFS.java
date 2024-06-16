package com.example.pokecenter.customer.lam.Iterator;

import java.util.Deque;
import java.util.LinkedList;

public class DFS<T> implements Iterator<T> {
    private final Vertex<T> startVertex;
    private Deque<Vertex<T>> stack = new LinkedList<>();

    public DFS(Vertex<T> startVertex) {
        this.startVertex = startVertex;
        stack.push(startVertex);
    }

    public boolean hasNext() {
        return !stack.isEmpty();
    }

    public Vertex<T> getNext() {
        if (!hasNext()) {
            return null;
        }
        Vertex<T> current = stack.pop();
        if (!current.isVisited()) {
            current.setVisited(true);
            current.getNeighbors().forEach(neighbor -> {
                if (!neighbor.isVisited()) {
                    stack.push(neighbor);
                }
            });
            return current;
        }

        return getNext();
    }

    @Override
    public void reset() {
        stack.clear();
        stack.push(startVertex);
        startVertex.setVisited(false);
    }
}
