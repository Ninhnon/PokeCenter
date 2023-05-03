package com.example.pokecenter.customer;

import static androidx.core.content.ContextCompat.getColor;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.pokecenter.R;
import com.example.pokecenter.customer.lam.CustomerTab.CustomerNotificationsFragment;
import com.example.pokecenter.customer.lam.CustomerTab.CustomerOrdersFragment;
import com.example.pokecenter.customer.lam.CustomerTab.CustomerShoppingCartFragment;
import com.example.pokecenter.customer.lam.CustomerTab.Home.CustomerHomeFragment;
import com.example.pokecenter.customer.lam.CustomerTab.Profile.CustomerProfileFragment;
import com.example.pokecenter.databinding.ActivityCustomerBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class CustomerActivity extends AppCompatActivity {

    private ActivityCustomerBinding binding;

    public static BottomNavigationView customerBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            window.setStatusBarColor(getColor(R.color.light_canvas));
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        binding = ActivityCustomerBinding.inflate(getLayoutInflater());
        customerBottomNavigationView = binding.bottomNavView;

        Fragment home = new CustomerHomeFragment();
        Fragment orders = new CustomerOrdersFragment();
        Fragment notifications = new CustomerNotificationsFragment();
        Fragment shoppingCart = new CustomerShoppingCartFragment();
        Fragment profile = new CustomerProfileFragment();

        // Move between fragments
        customerBottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.customerHomeFragment:
                    replaceFragment(home);
                    break;
                case R.id.customerOrdersFragment:
                    replaceFragment(orders);
                    break;
                case R.id.customerNotificationsFragment:
                    replaceFragment(notifications);
                    break;
                case R.id.customerShoppingCardFragment:
                    replaceFragment(shoppingCart);
                    break;
                case R.id.customerProfileFragment:
                    replaceFragment(profile);
                    break;
            }
            return true;
        });

        /*
        Set HomeFragment is default
        Đồng thời lúc này customerBottomNavigationView.setOnItemSelectedListener() ở dòng 80
        cũng sẽ được trigger
        => Thực thi lệnh "replaceFragment(new CustomerHomePlaceholderFragment());"
        => Nội dung page cũng sẽ thay đổi theo
         */

        customerBottomNavigationView.setSelectedItemId(R.id.customerHomeFragment);
        setContentView(binding.getRoot());
    }

    private void replaceFragment(Fragment selectedFragment) {

        /*
        Notes:
            getFragmentManager() was deprecated in API level 28.

            we will use getChildFragmentManager() Return a private FragmentManager for placing and managing Fragments inside of this Fragment.

            we will use getParentFragmentManager() Return the FragmentManager for interacting with fragments associated with this fragment's activity.

            so, if you deal with fragments inside a fragment you will use the first one and if you deal with fragments inside an activity you will use the second one.

            you can find them here package androidx.fragment.app;
         */

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentCustomer, selectedFragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}