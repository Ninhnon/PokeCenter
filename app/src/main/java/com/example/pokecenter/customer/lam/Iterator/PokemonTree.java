package com.example.pokecenter.customer.lam.Iterator;

public class PokemonTree<T> implements Tree<T> {
    @Override
    public Iterator<T> createBFSIterator(Vertex<T> vertex) {
        return new BFS<T>(vertex);
    }

    @Override
    public Iterator<T> createDFSIterator(Vertex<T> vertex) {
        return new DFS<T>(vertex);
    }
}
