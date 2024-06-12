package com.example.pokecenter.customer.lam.Model.pokemon;

import com.example.pokecenter.customer.lam.Interface.IterableCollection;
import com.example.pokecenter.customer.lam.Interface.Iterator;

import java.util.ArrayList;

public class PokemonCollection implements IterableCollection<Pokemon> {
    private ArrayList<Pokemon> pokemons;

    public PokemonCollection(ArrayList<Pokemon> pokemons) {
        this.pokemons = pokemons;
    }

    @Override
    public Iterator<Pokemon> getIterator() {
        return new PokemonIterator(pokemons);
    }
}

