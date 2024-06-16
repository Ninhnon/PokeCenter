package com.example.pokecenter.customer.lam.CustomerTab.Home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokecenter.R;
import com.example.pokecenter.customer.lam.CustomerActivity;
import com.example.pokecenter.customer.lam.API.FirebaseSupportCustomer;
import com.example.pokecenter.customer.lam.API.PokeApiFetcher;
import com.example.pokecenter.customer.lam.CustomerTab.Home.NextActivity.PokedexActivity;
import com.example.pokecenter.customer.lam.CustomerTab.Home.NextActivity.ProductByPokemonActivity;
import com.example.pokecenter.customer.lam.CustomerTab.Home.NextActivity.ProductDetailActivity;
import com.example.pokecenter.customer.lam.CustomerTab.Home.NextActivity.SearchProductActivity;
import com.example.pokecenter.customer.lam.CustomerTab.Home.NextActivity.SearchProductByCategoryActivity;
import com.example.pokecenter.customer.lam.CustomerTab.Home.NextActivity.TrendingProductsActivity;
import com.example.pokecenter.customer.lam.CustomerTab.Profile.CustomerProfileFragment;
import com.example.pokecenter.customer.lam.Iterator.Iterator;
import com.example.pokecenter.customer.lam.Interface.PokemonRecyclerViewInterface;
import com.example.pokecenter.customer.lam.Iterator.PokemonTree;
import com.example.pokecenter.customer.lam.Iterator.Vertex;
import com.example.pokecenter.customer.lam.Model.account.Account;
import com.example.pokecenter.customer.lam.Model.pokemon.Pokemon;
import com.example.pokecenter.customer.lam.Model.pokemon.PokemonAdapter;
import com.example.pokecenter.customer.lam.Model.pokemon.PokemonCollection;
import com.example.pokecenter.customer.lam.Model.product.Product;
import com.example.pokecenter.customer.lam.Model.product.ProductAdapter;
import com.example.pokecenter.customer.lam.Provider.ProductData;
import com.example.pokecenter.customer.lam.Singleton.UserInfo;
import com.example.pokecenter.databinding.FragmentCustomerHomeBinding;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class CustomerHomeFragment extends Fragment implements PokemonRecyclerViewInterface {

    private FragmentCustomerHomeBinding binding;
    private RecyclerView rcvPokemon;
    private PokemonAdapter pokemonAdapter;

    private RecyclerView rcvProduct;
    private ProductAdapter productAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = FragmentCustomerHomeBinding.inflate(inflater, container, false);

        Account currentAccount = UserInfo.getInstance().getAccount();

        binding.username.setText(currentAccount.getUsername());
        Picasso.get().load(currentAccount.getAvatar()).into(binding.avatarImage);

        // Move to Profile Fragment when User click on avatarImage
        binding.avatarImage.setOnClickListener(view -> {
            // Set selectedItem in Bottom Nav Bar
            CustomerActivity.customerBottomNavigationView.setSelectedItemId(R.id.customerProfileFragment);

        });

        /* search bar logic */
        binding.searchProductBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    String searchText = binding.searchProductBar.getText().toString();
                    if (!searchText.isEmpty()) {
                        goToSearchActivity(searchText);
                    }
                    return true;
                }
                return false;
            }
        });

        binding.searchByTextButton.setOnClickListener(view -> {
            String searchText = binding.searchProductBar.getText().toString();
            if (!searchText.isEmpty()) {
                goToSearchActivity(searchText);
            }
        });

        binding.viewAllPokedex.setOnClickListener(view -> {

            startActivity(new Intent(getActivity(), PokedexActivity.class));
        });

        // _______Pokedex________
        rcvPokemon = binding.rcvHorizontalPokemon;
        pokemonAdapter = new PokemonAdapter(getContext(), this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        rcvPokemon.setLayoutManager(linearLayoutManager);
        rcvPokemon.setAdapter(pokemonAdapter);

        if (PokeApiFetcher.pokemonHomeDemoData.isEmpty()) {
            // Chỗ này là để set Data cho Adapter là những cái loading Card
            ArrayList<Pokemon> loadingPokemons = new ArrayList<>();
            for (int i = 1; i <= 10; ++i) {
                loadingPokemons.add(new Pokemon(01, "", "", ""));
            }

            pokemonAdapter.setData(loadingPokemons);


            ExecutorService executor = Executors.newCachedThreadPool();
            Handler handler = new Handler(Looper.getMainLooper());

            for (int i = 0; i < loadingPokemons.size(); ++i) {
                Pokemon poke = loadingPokemons.get(i);

                int finalI = i;
                executor.execute(() -> {
                    Pokemon fetchedPokemon = PokeApiFetcher.fetchPokemonRandom();
                    handler.post(() -> {
                        poke.setName(fetchedPokemon.getName());
                        poke.setImageUrl(fetchedPokemon.getImageUrl());
                        poke.setType(fetchedPokemon.getType());
                        pokemonAdapter.updateItem(finalI);
                    });
                });
            }

            Pokemon eevee = new Pokemon(1, "Eevee", "url1", "Normal");
            Pokemon vaporeon = new Pokemon(2, "Vaporeon", "url2", "Water");
            Pokemon jolteon = new Pokemon(3, "Jolteon", "url3", "Electric");
            Pokemon flareon = new Pokemon(4, "Flareon", "url4", "Fire");

            Pokemon tyrogue = new Pokemon(5, "Tyrogue", "url5", "Fighting");
            Pokemon hitmonlee = new Pokemon(6, "Hitmonlee", "url6", "Fighting");
            Pokemon hitmonchan = new Pokemon(7, "Hitmonchan", "url7", "Fighting");
            Pokemon hitmontop = new Pokemon(8, "Hitmontop", "url8", "Fighting");

            Pokemon wurmple = new Pokemon(9, "Wurmple", "url9", "Bug");
            Pokemon silcoon = new Pokemon(10, "Silcoon", "url10", "Bug");
            Pokemon beautifly = new Pokemon(11, "Beautifly", "url11", "Bug");
            Pokemon cascoon = new Pokemon(12, "Cascoon", "url12", "Bug");
            Pokemon dustox = new Pokemon(13, "Dustox", "url13", "Bug");

            // Khởi tạo đỉnh
            Vertex<Pokemon> eeveeVertex = new Vertex<>(eevee);
            Vertex<Pokemon> vaporeonVertex = new Vertex<>(vaporeon);
            Vertex<Pokemon> jolteonVertex = new Vertex<>(jolteon);
            Vertex<Pokemon> flareonVertex = new Vertex<>(flareon);

            Vertex<Pokemon> tyrogueVertex = new Vertex<>(tyrogue);
            Vertex<Pokemon> hitmonleeVertex = new Vertex<>(hitmonlee);
            Vertex<Pokemon> hitmonchanVertex = new Vertex<>(hitmonchan);
            Vertex<Pokemon> hitmontopVertex = new Vertex<>(hitmontop);

            Vertex<Pokemon> wurmpleVertex = new Vertex<>(wurmple);
            Vertex<Pokemon> silcoonVertex = new Vertex<>(silcoon);
            Vertex<Pokemon> beautiflyVertex = new Vertex<>(beautifly);
            Vertex<Pokemon> cascoonVertex = new Vertex<>(cascoon);
            Vertex<Pokemon> dustoxVertex = new Vertex<>(dustox);

            // Thiết lập mối liên hệ tiến hóa
            eeveeVertex.addNeighbor(vaporeonVertex);
            eeveeVertex.addNeighbor(jolteonVertex);
            eeveeVertex.addNeighbor(flareonVertex);

            tyrogueVertex.addNeighbor(hitmonleeVertex);
            tyrogueVertex.addNeighbor(hitmonchanVertex);
            tyrogueVertex.addNeighbor(hitmontopVertex);

            wurmpleVertex.addNeighbor(silcoonVertex);
            silcoonVertex.addNeighbor(beautiflyVertex);
            wurmpleVertex.addNeighbor(cascoonVertex);
            cascoonVertex.addNeighbor(dustoxVertex);

            // Tạo root
            Pokemon rootPokemon = new Pokemon(0, "Root", "url0", "None");
            Vertex<Pokemon> root = new Vertex<>(rootPokemon);

            root.addNeighbor(eeveeVertex);
            root.addNeighbor(tyrogueVertex);
            root.addNeighbor(wurmpleVertex);

            Stack<Vertex<Pokemon>> stack = new Stack<>();
            stack.push(root);

            while (!stack.isEmpty()) {
                Vertex<Pokemon> current = stack.pop();

                if (!current.isVisited()) {
                    current.setVisited(true);
                    pokemonAdapter.addData(new ArrayList<>(Collections.singletonList(current.getData())));
                    System.out.println(current.getData().getName());

                    // Thêm tất cả các đỉnh kề chưa được thăm vào stack
                    List<Vertex<Pokemon>> neighbors = current.getNeighbors();
                    for (Vertex<Pokemon> neighbor : neighbors) {
                        if (!neighbor.isVisited()) {
                            stack.push(neighbor);
                        }
                    }
                }
            }

/*            // Tạo root
            Pokemon rootPokemon = new Pokemon(0, "Root", "url0", "None");
            Vertex<Pokemon> root = new Vertex<>(rootPokemon);

            // Thêm các đỉnh gốc của từng nhóm vào root giả
            root.addNeighbor(eeveeVertex);
            root.addNeighbor(tyrogueVertex);
            root.addNeighbor(wurmpleVertex);*/

            /*Code mới sử dụng PokemonTree*/
/*            PokemonTree<Pokemon> pokemonTree = new PokemonTree<>();

            // Tạo và sử dụng DFS Iterator
            Iterator<Pokemon> dfsIterator = pokemonTree.createDFSIterator(root);
            System.out.println("DFS Traversal:");
            while (dfsIterator.hasNext()) {
                Vertex<Pokemon> pokemon = dfsIterator.getNext();
                System.out.println(pokemon.getData().getName());
//                pokemonAdapter.addData(new ArrayList<>(Collections.singletonList(pokemon.getData())));
            }

            // Tạo và sử dụng BFS Iterator
            Iterator<Pokemon> bfsIterator = pokemonTree.createBFSIterator(root);
            System.out.println("BFS Traversal:");
            while (bfsIterator.hasNext()) {
                Vertex<Pokemon> pokemon = bfsIterator.getNext();
                System.out.println(pokemon.getData().getName());
//                pokemonAdapter.addData(new ArrayList<>(Collections.singletonList(pokemon.getData())));
            }*/
        }
        else {
            pokemonAdapter.setData(PokeApiFetcher.pokemonHomeDemoData);
        }

        // _______Trending________
        binding.viewAllTrending.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(), TrendingProductsActivity.class));
        });

        rcvProduct = binding.rcvGridProduct;

        ViewGroup.LayoutParams layoutParams = rcvProduct.getLayoutParams();
        layoutParams.height = 732 * 2;

        productAdapter = new ProductAdapter(getActivity(), this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        rcvProduct.setLayoutManager(gridLayoutManager);

        // Setup Loading Trending Product (UX)
        productAdapter.setData(mockTrendingData());
        rcvProduct.setAdapter(productAdapter);

        setTrendingProducts();

        binding.viewAllCategory.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(), SearchProductByCategoryActivity.class));
        });


        return binding.getRoot();
    }

    private void setTrendingProducts() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            boolean isSuccessful = true;
            try {
                new FirebaseSupportCustomer().fetchingTrendingProductId();
            } catch (IOException e) {
                isSuccessful = false;
            }
            boolean finalIsSuccessful = isSuccessful;
            handler.post(() -> {
                if (finalIsSuccessful) {
                    List<Product> trendingProducts = ProductData.trendingProductsId.subList(0,4).stream().map(item -> ProductData.fetchedProducts.get(item)).collect(Collectors.toList());
                    productAdapter.setData(trendingProducts);
                } else {
                    Toast.makeText(getActivity(), "Failed to load trending products", Toast.LENGTH_SHORT)
                            .show();
                }
            });
        });

    }

    private void goToSearchActivity(String searchText) {
        Intent intent = new Intent(getActivity(), SearchProductActivity.class);
        intent.putExtra("searchText", searchText);
        startActivity(intent);
    }

    private ArrayList<Product> mockTrendingData() {
        ArrayList<Product> products = new ArrayList<>();
        for (int i = 1;i <= 4; ++i) {
            products.add(new Product(
                    null,
                    null,
                    null,
                    null,
                    null
            ));
        }
        return products;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onPokemonCardClick(Pokemon pokemon) {
        if (!pokemon.getName().isEmpty()) {
            Intent intent = new Intent(getActivity(), ProductByPokemonActivity.class);
            intent.putExtra("pokemonName", pokemon.getName());
            startActivity(intent);
        }
    }

    @Override
    public void onProductCardClick(Product product) {
        if (product.getName() != null) {
            Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
            intent.putExtra("product object", product);
            startActivity(intent);
        }
    }


}