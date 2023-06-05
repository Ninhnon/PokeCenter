package com.example.pokecenter.admin.Quan.AdminTab.Tabs.Home.ProductsManagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.pokecenter.R;
import com.example.pokecenter.admin.Quan.AdminTab.Model.AdminProduct.AdminOption.AdminOption;
import com.example.pokecenter.admin.Quan.AdminTab.Model.AdminProduct.AdminOption.AdminOptionAdapter;
import com.example.pokecenter.admin.Quan.AdminTab.Model.AdminProduct.AdminProduct;
import com.example.pokecenter.admin.Quan.AdminTab.Model.AdminProduct.AdminProductReview.AdminProductReviewAdapter;
import com.example.pokecenter.admin.Quan.AdminTab.Utils.ItemSpacingDecoration;
import com.example.pokecenter.databinding.ActivityAdminProductDetailReviewsBinding;
import com.example.pokecenter.databinding.ActivityAdminProductOptionBinding;

import java.util.ArrayList;

public class AdminProductOptionActivity extends AppCompatActivity {

    private ActivityAdminProductOptionBinding binding;
    private AdminProduct adminProduct;
    private ArrayList<AdminOption> adminOptionList;
    private AdminOptionAdapter adminOptionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getColor(R.color.light_primary));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        binding = ActivityAdminProductOptionBinding.inflate(getLayoutInflater());
        getSupportActionBar().setTitle("Product's Options");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        adminProduct = (AdminProduct)intent.getSerializableExtra("AdminProduct");

        adminOptionList = adminProduct.getOptions();

        Log.e("SIZE", String.valueOf(adminOptionList.size()));

        //Check if option has image
        //If not, set productImage as OptionImage
        for (AdminOption option : adminOptionList) {
            Log.e("ITEM", option.getId());
            if (option.getId().equals("null")) {
                option.setId(adminProduct.getName());
            }
            if (option.getOptionImage().equals("")) {
                option.setOptionImage(adminProduct.getImages().get(0));
            }
        }

        setUpRecyclerView();

        setContentView(binding.getRoot());
    }

    public void setUpRecyclerView() {

        adminOptionAdapter = new AdminOptionAdapter(adminOptionList, AdminProductOptionActivity.this, R.layout.quan_product_option_item);
        //Add spacing to RecyclerView
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.item_spacing);
        ItemSpacingDecoration itemSpacingDecoration = new ItemSpacingDecoration(spacingInPixels);
        binding.rvAdminProductOption.addItemDecoration(itemSpacingDecoration);
        binding.rvAdminProductOption.setLayoutManager(new LinearLayoutManager(AdminProductOptionActivity.this));
        binding.rvAdminProductOption.setAdapter(adminOptionAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}