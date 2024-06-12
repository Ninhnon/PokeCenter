package com.example.pokecenter.customer.lam.Model.pokemon;

import com.example.pokecenter.customer.lam.Interface.Iterator;

import java.util.ArrayList;

public class PokemonIterator implements Iterator<Pokemon> {
    private ArrayList<Pokemon> pokemons;
    private int position = 0;

    public PokemonIterator(ArrayList<Pokemon> pokemons) {
        this.pokemons = pokemons;
    }

    @Override
    public boolean hasNext() {
        return position < pokemons.size();
    }

    @Override
    public Pokemon next() {
        if (this.hasNext()) {
            return pokemons.get(position++);
        }
        return null;
    }

    public int getPosition() {
        return position - 1; // Trả về vị trí hiện tại sau khi gọi next()
    }
}
