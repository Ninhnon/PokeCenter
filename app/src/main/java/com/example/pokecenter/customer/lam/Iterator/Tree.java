package com.example.pokecenter.customer.lam.Iterator;

public interface Tree <T> {
    Iterator<T> createBFSIterator(Vertex<T> vertex);
    Iterator<T> createDFSIterator(Vertex<T> vertex);
}
