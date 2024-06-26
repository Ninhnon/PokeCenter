package com.example.pokecenter.vender.API;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.pokecenter.admin.AdminTab.Utils.DateUtils;
import com.example.pokecenter.customer.lam.Model.account.Account;
import com.example.pokecenter.customer.lam.Model.option.Option;
import com.example.pokecenter.customer.lam.Model.order.DetailOrder;
import com.example.pokecenter.customer.lam.Model.order.Order;
import com.example.pokecenter.customer.lam.Model.product.Product;
import com.example.pokecenter.vender.Model.Notification.NotificationData;
import com.example.pokecenter.vender.Model.Notification.PushNotification;
import com.example.pokecenter.vender.Model.Notification.RetrofitInstance;
import com.example.pokecenter.vender.Model.Vender.Vender;
import com.example.pokecenter.vender.Model.VenderOrder.VenderDetailOrder;
import com.example.pokecenter.vender.Model.VenderOrder.VenderOrder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;


public class FirebaseSupportVender {

    private String urlDb = "https://pokecenter-ae954-default-rtdb.firebaseio.com/";
    private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    public void addNewProduct(Product newProduct) throws IOException {

        // create OkHttpClient instance
        OkHttpClient client = new OkHttpClient();
        String venderId = FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", ",");

        /* create pushData */
        Map<String, Object> pushData = new HashMap<>();
        pushData.put("name", newProduct.getName());
        pushData.put("desc", newProduct.getDesc());
        pushData.put("venderId", venderId);
        pushData.put("images", newProduct.getImages());
        /* convert pushData to Json string */
        String userJsonData = new Gson().toJson(pushData);

        // create request body
        RequestBody body = RequestBody.create(userJsonData, JSON);

        // create POST request

        Request request = new Request.Builder()
                .url(urlDb + "/products.json")
                .post(body)
                .build();


        Response response = client.newCall(request).execute();

        if (response.isSuccessful()) {
            Type type = new TypeToken<Map<String, Object>>() {
            }.getType();
            Map<String, String> extractedData = new Gson().fromJson(response.body().string(), type);
            newProduct.setId(extractedData.get("name"));
        }

        //Post Options
        for (int i = 0; i < newProduct.getOptions().size(); i++) {
            pushData = new HashMap<>();
            pushData.put("currentQuantity", newProduct.getOptions().get(i).getInputQuantity());
            pushData.put("inputQuantity", newProduct.getOptions().get(i).getInputQuantity());
            pushData.put("optionImage", newProduct.getOptions().get(i).getOptionImage());
            pushData.put("price", newProduct.getOptions().get(i).getPrice());
            /* convert pushData to Json string */
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference usersRef = database.getReference("products/" + newProduct.getId() + "/options/");
            usersRef.child(newProduct.getOptions().get(i).getOptionName()).setValue(pushData);
        }
    }

    public List<VenderOrder> fetchingVenderOrdersData() throws IOException {
        List<VenderOrder> fetchedVenderOrders = new ArrayList<>();

        OkHttpClient client = new OkHttpClient();

        // Construct the URL for the Firebase Realtime Database endpoint
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://pokecenter-ae954-default-rtdb.firebaseio.com/orders.json").newBuilder();

        String emailWithCurrentUser = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        urlBuilder.addQueryParameter("orderBy", "\"venderId\"")
                .addQueryParameter("equalTo", "\"" + emailWithCurrentUser.replace(".", ",") + "\"");

        String url = urlBuilder.build().toString();

        // Create an HTTP GET request
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();

        if (response.isSuccessful()) {
            String responseString = response.body().string();

            if (responseString.equals("null")) {
                return new ArrayList<>();
            }

            Type type = new TypeToken<Map<String, VenderOrder>>() {
            }.getType();
            Map<String, VenderOrder> fetchedData = new Gson().fromJson(responseString, type);

            for (Map.Entry<String, VenderOrder> entry : fetchedData.entrySet()) {
                String orderKey = entry.getKey();
                VenderOrder venderOrder = entry.getValue();

                // Set the order key if necessary
                //venderOrder.setOrderKey(orderKey);

                fetchedVenderOrders.add(venderOrder);
            }
        }

        return fetchedVenderOrders;
    }

    public List<VenderOrder> fetchingOrdersData2() throws IOException {
        List<VenderOrder> fetchedOrders = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();

        // Construct the URL for the Firebase Realtime Database endpoint for orders
        HttpUrl.Builder ordersUrlBuilder = HttpUrl.parse("https://pokecenter-ae954-default-rtdb.firebaseio.com/orders.json").newBuilder();

        String emailWithCurrentUser = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        ordersUrlBuilder.addQueryParameter("orderBy", "\"venderId\"")
                .addQueryParameter("equalTo", "\"" + emailWithCurrentUser.replace(".", ",") + "\"");

        String ordersUrl = ordersUrlBuilder.build().toString();

        // Create an HTTP GET request for orders
        Request ordersRequest = new Request.Builder()
                .url(ordersUrl)
                .build();

        Response ordersResponse = client.newCall(ordersRequest).execute();

        if (ordersResponse.isSuccessful()) {
            String ordersResponseString = ordersResponse.body().string();

            if (ordersResponseString.equals("null")) {
                return new ArrayList<>();
            }

            Type ordersType = new TypeToken<Map<String, Map<String, Object>>>() {
            }.getType();
            Map<String, Map<String, Object>> fetchedOrdersData = new Gson().fromJson(ordersResponseString, ordersType);

            // Fetching product data separately
            String productsUrl = "https://pokecenter-ae954-default-rtdb.firebaseio.com/products.json";

            // Create an HTTP GET request for products
            Request productsRequest = new Request.Builder()
                    .url(productsUrl)
                    .build();

            Response productsResponse = client.newCall(productsRequest).execute();

            if (productsResponse.isSuccessful()) {
                String productsResponseString = productsResponse.body().string();

                if (!productsResponseString.equals("null")) {
                    Type productsType = new TypeToken<Map<String, Map<String, Object>>>() {
                    }.getType();
                    Map<String, Map<String, Object>> fetchedProductsData = new Gson().fromJson(productsResponseString, productsType);

                    fetchedOrdersData.forEach((key, value) -> {
                        List<Map<String, Object>> detailOrderData = (List<Map<String, Object>>) value.get("details");

                        List<VenderDetailOrder> details = new ArrayList<>();
                        detailOrderData.forEach(detailOrder -> {
                            String productId = (String) detailOrder.get("productId");
                            int selectedOption = ((Double) detailOrder.get("selectedOption")).intValue();
                            int quantity = ((Double) detailOrder.get("quantity")).intValue();

                            // Retrieve product details using productId
                            Map<String, Object> productData = fetchedProductsData.get(productId);

                            if (productData != null) {
                                String createDate = (String) value.get("createDate");
                                String name = (String) productData.get("name");
                                List<Option> options = new ArrayList<>();

                                Map<String, Map<String, Object>> optionsData = (Map<String, Map<String, Object>>) productData.get("options");
                                if (optionsData != null) {
                                    for (Map.Entry<String, Map<String, Object>> entry : optionsData.entrySet()) {
                                        String optionName = entry.getKey();
                                        Map<String, Object> optionData = entry.getValue();
                                        int price = ((Double) optionData.get("price")).intValue();

                                        Option option = new Option(price);
                                        options.add(option);
                                    }
                                }
                                int price = options.get(selectedOption).getPrice();
                                details.add(new VenderDetailOrder(productId, selectedOption, quantity, createDate, name, price));
                            }
                        });

                        fetchedOrders.add(new VenderOrder(
                                ((Double) value.get("totalAmount")).intValue(),
                                (String) value.get("createDate"),
                                details
                        ));
                    });
                }
            }
        }

        return fetchedOrders;
    }

    public void updateTotalProduct(int totalProduct) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Map<String, Integer> pushData = new HashMap<>();
        pushData.put("totalProduct", totalProduct);

        String jsonData = new Gson().toJson(pushData);

        RequestBody body = RequestBody.create(jsonData, JSON);

        Request request = new Request.Builder()
                .url(urlDb + "venders/" + FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", ",") + ".json")
                .patch(body)
                .build();

        client.newCall(request).execute();
    }
    public void DeleteProduct(String productId) throws IOException {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference productsRef = database.getReference("products");

// Listen for changes to the specified product
        ValueEventListener productListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Get the product snapshot
                    DataSnapshot productSnapshot = dataSnapshot.child(productId);

                    // Check if the product exists
                    if (productSnapshot.exists()) {
                        // Get the options snapshot
                        DataSnapshot optionsSnapshot = productSnapshot.child("options");

                        // Iterate over all options and update currentQuantity to -1
                        for (DataSnapshot optionSnapshot : optionsSnapshot.getChildren()) {
                            optionSnapshot.getRef().child("currentQuantity").setValue(-1);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database error
            }
        };

// Attach the listener to the products reference
        productsRef.addListenerForSingleValueEvent(productListener);
    }
    public void updateProduct(Product updatedProduct) throws IOException {

        // create OkHttpClient instance
        OkHttpClient client = new OkHttpClient();
        String venderId = FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", ",");

        /* create pushData */
        Map<String, Object> pushData = new HashMap<>();
        pushData.put("name", updatedProduct.getName());
        pushData.put("desc", updatedProduct.getDesc());
        pushData.put("venderId", venderId);
        pushData.put("images", updatedProduct.getImages());
        /* convert pushData to Json string */
        String productJsonData = new Gson().toJson(pushData);

        // create request body
        RequestBody body = RequestBody.create(productJsonData, JSON);

        // create PUT request
        String url = urlDb + "/products/" + updatedProduct.getId() + ".json";
        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .build();

        Response response = client.newCall(request).execute();

        if (response.isSuccessful()) {
            // Update successful
        } else {
            // Update failed
        }

        // Update options
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference optionsRef = database.getReference("products/" + updatedProduct.getId() + "/options/");
        for (int i = 0; i < updatedProduct.getOptions().size(); i++) {
            Option option = updatedProduct.getOptions().get(i);
            pushData = new HashMap<>();
            pushData.put("currentQuantity", option.getCurrentQuantity());
            pushData.put("inputQuantity", option.getInputQuantity());
            pushData.put("optionImage", option.getOptionImage());
            pushData.put("price", option.getPrice());
            optionsRef.child(option.getOptionName()).setValue(pushData);
        }
    }
    public List<String> fetchingAllCategoryTag() throws IOException {
        List<String> fetchedCategoryTag = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(urlDb + "category.json")
                .build();

        Response response = client.newCall(request).execute();

        if (response.isSuccessful()) {
            String responseString = response.body().string();

            if (responseString.equals("null")) {
                return new ArrayList<>();
            }

            Type type = new TypeToken<Map<String, Object>>() {
            }.getType();
            Map<String, Object> fetchedData = new Gson().fromJson(responseString, type);

            fetchedCategoryTag.addAll(fetchedData.keySet());
        }

        Collections.reverse(fetchedCategoryTag);

        return fetchedCategoryTag;
    }

    public void updatePokemonAfterAddProduct(String productId, List<String> myPokemon) throws IOException {
        DatabaseReference Ref = FirebaseDatabase.getInstance().getReference("pokemons");

// Push the productId and Pokemon list to the "pokemons" structure in Firebase
        for (String s : myPokemon) {
            Ref.child(s).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        List<String> existingPokemonList = new ArrayList<>();
                        Object value = dataSnapshot.getValue();

                        if (value instanceof List) {
                            existingPokemonList = (List<String>) value;
                        } else if (value instanceof String) {
                            existingPokemonList.add((String) value);
                        }

                        existingPokemonList.add(productId);

                        Ref.child(s).setValue(existingPokemonList)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            // Data successfully updated in Firebase
                                            notifyResult(true);
                                        } else {
                                            // Failed to update data in Firebase
                                            notifyResult(false);
                                        }
                                    }
                                });
                    } else {
                        // Handle the case where the product ID doesn't exist in Firebase
                        List<String> newPokemonList = new ArrayList<>();
                        newPokemonList.add(productId);

                        Ref.child(s).setValue(newPokemonList)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            // Data successfully added to Firebase
                                            notifyResult(true);
                                        } else {
                                            // Failed to add data to Firebase
                                            notifyResult(false);
                                        }
                                    }
                                });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle any errors
                    notifyResult(false);
                }

                private void notifyResult(boolean success) {
                    // Notify the result using a callback or any other mechanism
                    if (success) {
                        // Data was successfully updated/added in Firebase
                    } else {
                        // Failed to update/add data in Firebase
                    }
                }
            });

        }

    }

    public void updateCategoryAfterAddProduct(String productId, List<String> myCategory) throws IOException {
        DatabaseReference Ref = FirebaseDatabase.getInstance().getReference("category");

// Push the productId and Pokemon list to the "pokemons" structure in Firebase
        for (String s : myCategory) {
            Ref.child(s).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        List<String> existingPokemonList = new ArrayList<>();
                        Object value = dataSnapshot.getValue();

                        if (value instanceof List) {
                            existingPokemonList = (List<String>) value;
                        } else if (value instanceof String) {
                            existingPokemonList.add((String) value);
                        }

                        existingPokemonList.add(productId);

                        Ref.child(s).setValue(existingPokemonList)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            // Data successfully updated in Firebase
                                            notifyResult(true);
                                        } else {
                                            // Failed to update data in Firebase
                                            notifyResult(false);
                                        }
                                    }
                                });
                    } else {
                        // Handle the case where the product ID doesn't exist in Firebase
                        List<String> newPokemonList = new ArrayList<>();
                        newPokemonList.add(productId);

                        Ref.child(s).setValue(newPokemonList)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            // Data successfully added to Firebase
                                            notifyResult(true);
                                        } else {
                                            // Failed to add data to Firebase
                                            notifyResult(false);
                                        }
                                    }
                                });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle any errors
                    notifyResult(false);
                }

                private void notifyResult(boolean success) {
                    // Notify the result using a callback or any other mechanism
                    if (success) {
                        // Data was successfully updated/added in Firebase
                    } else {
                        // Failed to update/add data in Firebase
                    }
                }
            });

        }

    }

    public Account fetchingCurrentAccount(String id) throws IOException {

        Account fetchedAccount = new Account();

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(urlDb + "accounts/" + id + ".json")
                .build();

        Response response = client.newCall(request).execute();

        if (response.isSuccessful()) {

            String responseBody = response.body().string();

            Type type = new TypeToken<Map<String, Object>>() {
            }.getType();
            Map<String, Object> fetchedData = new Gson().fromJson(responseBody, type);

            fetchedAccount.setAvatar((String) fetchedData.get("avatar"));
            fetchedAccount.setUsername((String) fetchedData.get("username"));
//            fetchedAccount.setGender((String) fetchedData.get("gender"));
//            fetchedAccount.setPhoneNumber((String) fetchedData.get("phoneNumber"));
//            fetchedAccount.setRegistrationDate((String) fetchedData.get("registrationDate"));
            fetchedAccount.setId(id);
            fetchedAccount.setRole((Integer) fetchedData.get("role"));
        }

        return fetchedAccount;
    }

    public Vender fetchingVenderById(String venderId) throws IOException {
        Vender fetchedVender = new Vender();

        fetchedVender.setVenderId(venderId);

        OkHttpClient client = new OkHttpClient();
        Request request1 = new Request.Builder()
                .url(urlDb + "venders/" + venderId + ".json")
                .build();

        Response response1 = client.newCall(request1).execute();

        if (response1.isSuccessful()) {
            assert response1.body() != null;
            String responseString = response1.body().string();

            Type type = new TypeToken<Map<String, Object>>() {
            }.getType();
            Map<String, Object> fetchedVenderData = new Gson().fromJson(responseString, type);
            fetchedVender.setShopName((String) fetchedVenderData.get("shopName"));
            fetchedVender.setFollowCount(((Double) fetchedVenderData.get("followCount")).intValue());

        } else {
            return null;
        }

        return fetchedVender;
    }

    SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy 'at' HH:mm");
    //    public List<Order> fetchingOrdersWithStatus(String status) throws IOException {
//
//        List<Order> fetchedOrders = new ArrayList<>();
//
//        OkHttpClient client = new OkHttpClient();
//
//        // Construct the URL for the Firebase Realtime Database endpoint
//        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://pokecenter-ae954-default-rtdb.firebaseio.com/orders.json").newBuilder();
//
//        String emailWithCurrentUser = FirebaseAuth.getInstance().getCurrentUser().getEmail();
//        urlBuilder.addQueryParameter("orderBy", "\"venderId\"")
//                .addQueryParameter("equalTo", "\"" + emailWithCurrentUser.replace(".", ",") + "\"");
//
////        urlBuilder.addQueryParameter("orderBy", "\"status\"")
////                .addQueryParameter("equalTo", "\"" + status + "\"");
//
//        String url = urlBuilder.build().toString();
//
//        // Create an HTTP GET request
//        Request request = new Request.Builder()
//                .url(url)
//                .build();
//
//        Response response = client.newCall(request).execute();
//
//        if (response.isSuccessful()) {
//            String responseString = response.body().string();
//
//            if (responseString.equals("null")) {
//                return new ArrayList<>();
//            }
//
//            Type type = new TypeToken<Map<String, Map<String, Object>>>(){}.getType();
//            Map<String, Map<String, Object>> fetchedData = new Gson().fromJson(responseString, type);
//
//            fetchedData.forEach((key, value) -> {
//
//                List<Map<String, Object>> detailOrderData = (List<Map<String, Object>>) value.get("details");
//
//                List<DetailOrder> details = new ArrayList<>();
//                detailOrderData.forEach(detailOrder -> {
//                    details.add(new DetailOrder(
//                            (String) detailOrder.get("productId"),
//                            ((Double) detailOrder.get("selectedOption")).intValue(),
//                            ((Double) detailOrder.get("quantity")).intValue()
//                    ));
//                });
//
//                Order order = null;
//                try {
//                    order = new Order(
//                            key,
//                            ((Double) value.get("totalAmount")).intValue(),
//                            outputFormat.parse((String) value.get("createDate")),
//                            details,
//                            (String) value.get("status")
//                    );
//
//                } catch (java.text.ParseException e) {
//                    throw new RuntimeException(e);
//                }
//
//
//                String stringDeliveryDate = (String) value.get("deliveryDate");
//
//                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//                if (stringDeliveryDate.isEmpty()) {
//
//                } else {
//
//                    try {
//                        order.setDeliveryDate(dateFormat.parse(stringDeliveryDate));
//                    } catch (java.text.ParseException e) {
//                        throw new RuntimeException(e);
//                    }
//
//                }
//
//                order.setExpand(true);
//                fetchedOrders.add(order);
//
//            });
//        }
//
//        fetchedOrders.removeIf(order -> !order.getStatus().contains(status));
//        fetchedOrders.sort(Comparator.comparing(Order::getCreateDateTime));
//        return fetchedOrders;
//
//    }
    public List<Order> fetchingOrdersWithStatus(String status) throws IOException {
        List<Order> fetchedOrders = new ArrayList<>();

        OkHttpClient client = new OkHttpClient();

        // Construct the URL for the Firebase Realtime Database endpoint
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://pokecenter-ae954-default-rtdb.firebaseio.com/orders.json").newBuilder();

        String emailWithCurrentUser = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        urlBuilder.addQueryParameter("orderBy", "\"venderId\"")
                .addQueryParameter("equalTo", "\"" + emailWithCurrentUser.replace(".", ",") + "\"");

        String url = urlBuilder.build().toString();

        // Create an HTTP GET request
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();

        if (response.isSuccessful()) {
            String responseString = response.body().string();

            if (responseString.equals("null")) {
                return new ArrayList<>();
            }

            Type type = new TypeToken<Map<String, Map<String, Object>>>() {}.getType();
            Map<String, Map<String, Object>> fetchedData = new Gson().fromJson(responseString, type);

            AtomicInteger counter = new AtomicInteger(fetchedData.size());
            CompletableFuture<Void> fetchOrdersFuture = new CompletableFuture<>();

            fetchedData.forEach((key, value) -> {
                String customerId = (String) value.get("customerId");

                // Retrieve the customer details
                DatabaseReference accountsRef = FirebaseDatabase.getInstance().getReference("accounts");
                Query customerQuery = accountsRef.orderByKey().equalTo(customerId);
                customerQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot accountSnapshot : dataSnapshot.getChildren()) {
                            // Fetch and process the order details
                            String customerName = accountSnapshot.child("username").getValue(String.class);
                            String customerPhoneNumber = accountSnapshot.child("phoneNumber").getValue(String.class);

                            // Retrieve the delivery address with isDeliveryAddress=true
                            DataSnapshot addressesSnapshot = accountSnapshot.child("addresses");
                            String deliveryAddress = "";
                            boolean foundDeliveryAddress = false;
                            for (DataSnapshot addressSnapshot : addressesSnapshot.getChildren()) {
                                boolean isDeliveryAddress = addressSnapshot.child("isDeliveryAddress").getValue(Boolean.class);
                                if (isDeliveryAddress) {
                                    String numberStreetAddress = addressSnapshot.child("numberStreetAddress").getValue(String.class);
                                    String address2 = addressSnapshot.child("address2").getValue(String.class);
                                    // Combine the address components
                                    deliveryAddress = numberStreetAddress + ", " + address2;
                                    foundDeliveryAddress = true;
                                    break; // Assuming there's only one delivery address
                                }
                            }

                            // If no delivery address found, use the address field
                            if (!foundDeliveryAddress) {
                                String address = accountSnapshot.child("address").getValue(String.class);
                                deliveryAddress = address;
                            }

                            List<Map<String, Object>> detailOrderData = (List<Map<String, Object>>) value.get("details");

                            List<DetailOrder> details = new ArrayList<>();
                            detailOrderData.forEach(detailOrder -> {
                                details.add(new DetailOrder(
                                        (String) detailOrder.get("productId"),
                                        ((Double) detailOrder.get("selectedOption")).intValue(),
                                        ((Double) detailOrder.get("quantity")).intValue()
                                ));
                            });

                            Order order = null;
                            try {
                                order = new Order(
                                        key,
                                        ((Double) value.get("totalAmount")).intValue(),
                                        outputFormat.parse((String) value.get("createDate")),
                                        details,
                                        (String) value.get("status")
                                );
                            } catch (java.text.ParseException e) {
                                throw new RuntimeException(e);
                            }

                            String stringDeliveryDate = (String) value.get("deliveryDate");

                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            if (stringDeliveryDate.isEmpty()) {
                                // Set default delivery date if not provided
                                order.setDeliveryDate(new Date());
                            } else {
                                try {
                                    order.setDeliveryDate(dateFormat.parse(stringDeliveryDate));
                                } catch (java.text.ParseException e) {
                                    throw new RuntimeException(e);
                                }
                            }

                            order.setExpand(true);
                            order.setCustomerName(customerName);
                            order.setCustomerPhoneNumber(customerPhoneNumber);
                            order.setDeliveryAddress(deliveryAddress);
                            // ...

                            fetchedOrders.add(order);

                            if (counter.decrementAndGet() == 0) {
                                fetchedOrders.removeIf(o -> !o.getStatus().contains(status));
                                fetchedOrders.sort(Comparator.comparing(Order::getCreateDateTime));
                                fetchOrdersFuture.complete(null); // Notify the future that orders are fetched
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle the error
                        fetchOrdersFuture.completeExceptionally(databaseError.toException());
                    }
                });
            });

            try {
                fetchOrdersFuture.get(); // Wait for the orders to be fetched
            } catch (Exception e) {
                throw new IOException("Failed to fetch orders", e);
            }
        }

        return fetchedOrders;
    }


    public void ChangeOrderStatus(String orderId, String status) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Map<String, Object> patchData = new HashMap<>();
        patchData.put("status", status);

        String jsonData = new Gson().toJson(patchData);
        RequestBody body = RequestBody.create(jsonData, JSON);

        Request request = new Request.Builder()
                .url(urlDb + "orders/" + orderId + ".json")
                .patch(body)
                .build();

        Response response = client.newCall(request).execute();
    }


    public void updateRegistrationToken(@NonNull String email, String token, int role) throws IOException {

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference usersRef;

        switch (role) {
            case 0:
                usersRef = database.getReference("customers/" + email.replace(".", ","));
                break;
            case 1:
                usersRef = database.getReference("venders/" + email.replace(".", ","));
                break;
            case 2:
                usersRef = database.getReference("admins/" + email.replace(".", ","));
                break;
            default:
                return;
        }

        Map<String, Object> user = new HashMap<>();
        user.put("token", token);

        usersRef.updateChildren(user);
    }

    public CompletableFuture<ArrayList<NotificationData>> fetchingAllNotifications() {
        CompletableFuture<ArrayList<NotificationData>> future = new CompletableFuture<>();

        DatabaseReference notificationsRef = FirebaseDatabase.getInstance().getReference("venders");

        String emailWithCurrentUser = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String notificationsPath = emailWithCurrentUser.replace(".", ",") + "/notifications";

        notificationsRef.child(notificationsPath).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot dataSnapshot = task.getResult();

                ArrayList<NotificationData> fetchedNotifications = new ArrayList<>();

                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String notificationId = childSnapshot.getKey();
                    Map<String, Object> value = (Map<String, Object>) childSnapshot.getValue();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

                    try {
                        fetchedNotifications.add(new NotificationData(
                                notificationId,
                                (String) value.get("title"),
                                (String) value.get("content"),
                                (String) value.get("type"),
                                (Boolean) value.get("read"),
                                dateFormat.parse((String) value.get("sentDate"))
                        ));
                    } catch (ParseException e) {
                        future.completeExceptionally(new IOException(e.getMessage()));
                        return;
                    }
                }

                Collections.reverse(fetchedNotifications);

                future.complete(fetchedNotifications);
            } else {
                future.completeExceptionally(new IOException("Failed to fetch notifications: " + task.getException().getMessage()));
            }
        });

        return future;
    }

    public Task<Void> changeStatusNotification(String notificationId) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String emailWithCurrentUser = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        DatabaseReference notificationRef = database.getReference("venders/" + emailWithCurrentUser.replace(".", ",") + "/notifications/" + notificationId);

        Map<String, Object> updateData = new HashMap<>();
        updateData.put("read", true);

        return notificationRef.updateChildren(updateData);
    }

    public void pushNotificationForPackaged(String orderId, boolean isCancel) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("orders");
        final String[] customerId = new String[1];
        final String[] token = new String[1];
        final String[] notificationId = new String[1];

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (orderId.equals(snapshot.getKey())) {
                        customerId[0] = snapshot.child("customerId").getValue(String.class);
                        Log.e("TAG", "Receiver id: " + customerId[0]);
                        break;
                    }
                }

                DatabaseReference reference1 = database.getReference("customers");
                reference1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            if (snapshot.getKey().equals(customerId[0])) {
                                token[0] = snapshot.child("token").getValue(String.class);
                                Log.e("TAG", "Token: " + token[0]);

                                DatabaseReference reference2 = reference1.child(customerId[0]).child("notifications").push();
                                notificationId[0] = reference2.getKey();
                                Log.e("TAG", "Notification id: " + notificationId[0]);

                                String title = "Order Packaged";
                                String content1;
                                if(isCancel) content1="Your order "  +
                                orderId + "is Cancelled";
                                else content1 =
                                        "Your order with tracking number " +
                                        orderId +
                                        " has been packaged and is ready for delivery." +
                                        " We will notify you once it's out for delivery";
                                String content = content1;
                                HashMap<String, Object> notificationNode = new HashMap<>();
                                notificationNode.put("content", content);
                                notificationNode.put("read", false);
                                notificationNode.put("sentDate", DateUtils.getCurrentDateString());
                                notificationNode.put("title", title);
                                notificationNode.put("type", "orders");

                                reference2.updateChildren(notificationNode);

                                PushNotification notification = new PushNotification(
                                        new NotificationData(notificationId[0], title, content, "orders",
                                                false, DateUtils.getCurrentDate()), token[0]);

                                RetrofitInstance.getApi().postNotification(notification).enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {
                                        if (response.isSuccessful()) {
                                            Log.e("TAG", "Post success");
                                        } else {
                                            Log.e("TAG", "Post failed");
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        Log.e("TAG", t.toString());
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("TAG", error.getMessage());
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("TAG", databaseError.getMessage());
            }
        });
    }

    public com.example.pokecenter.customer.lam.Model.vender.Vender fetchingCurrentAccount() throws IOException {

        com.example.pokecenter.customer.lam.Model.vender.Vender fetchedAccount = new com.example.pokecenter.customer.lam.Model.vender.Vender();

        OkHttpClient client = new OkHttpClient();

        String emailWithCurrentUser = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        Request request = new Request.Builder()
                .url(urlDb + "accounts/" + emailWithCurrentUser.replace(".", ",") + ".json")
                .build();

        Response response = client.newCall(request).execute();

        if (response.isSuccessful()) {

            String responseBody = response.body().string();

            Type type = new TypeToken<Map<String, Object>>(){}.getType();
            Map<String, Object> fetchedData = new Gson().fromJson(responseBody, type);

            fetchedAccount.setAvatar((String) fetchedData.get("avatar"));
            fetchedAccount.setUsername((String) fetchedData.get("username"));
            fetchedAccount.setGender((String) fetchedData.get("gender"));
            fetchedAccount.setPhoneNumber((String) fetchedData.get("phoneNumber"));
            fetchedAccount.setRegistrationDate((String) fetchedData.get("registrationDate"));
            if(fetchedData.get("background")!=null && !fetchedData.get("background").equals(""))
            {
                fetchedAccount.setBackground((String) fetchedData.get("background"));
            }

        }

        return fetchedAccount;
    }
    public void updateVenderInfo(com.example.pokecenter.customer.lam.Model.vender.Vender account) throws IOException {

        OkHttpClient client = new OkHttpClient();

        Map<String, Object> updateData = new HashMap<>();
        updateData.put("avatar", account.getAvatar());
        updateData.put("username", account.getUsername());
        updateData.put("gender", account.getGender());
        updateData.put("phoneNumber", account.getPhoneNumber());
        if(account.getBackground()!=null && !account.getBackground().equals(""))
        {
            updateData.put("background", account.getBackground());
        }

        String jsonData = new Gson().toJson(updateData);

        RequestBody body = RequestBody.create(jsonData, JSON);

        String emailWithCurrentUser = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        Request request = new Request.Builder()
                .url(urlDb + "accounts/" + emailWithCurrentUser.replace(".", ",") + ".json")
                .patch(body)
                .build();

        client.newCall(request).execute();

    }
}