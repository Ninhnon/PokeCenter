package com.example.pokecenter.customer.lam.Iterator;

import java.util.ArrayList;
import java.util.List;

public class Vertex<T> {
    private T data;
    private List<Vertex<T>> neighbors;
    private boolean visited;

    public Vertex(T data) {
        this.data = data;
        this.neighbors = new ArrayList<>();
        this.visited = false;
    }

    public T getData() {
        return data;
    }

    public List<Vertex<T>> getNeighbors() {
        return neighbors;
    }

    public void addNeighbor(Vertex<T> neighbor) {
        this.neighbors.add(neighbor);
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }
}
