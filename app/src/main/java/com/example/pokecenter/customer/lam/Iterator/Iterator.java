package com.example.pokecenter.customer.lam.Iterator;

import java.util.List;

public interface Iterator<T> {
    boolean hasNext();
    Vertex<T> getNext();
    void reset();
}
