package com.example.pokecenter.customer.lam.CustomerTab.Home.NextActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pokecenter.R;
import com.example.pokecenter.customer.lam.CustomerActivity;
import com.example.pokecenter.customer.lam.Model.product.Option;
import com.example.pokecenter.customer.lam.Model.product.Product;
import com.example.pokecenter.customer.lam.SliderAdapter;
import com.example.pokecenter.databinding.ActivityProductDetailBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ProductDetailActivity extends AppCompatActivity {

    private ActivityProductDetailBinding binding;
    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    private boolean isFavourite = false;
    View viewDialog;
    BottomSheetDialog dialog;
    ArrayAdapter<String> adapterItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewDialog = getLayoutInflater().inflate(R.layout.lam_bottom_sheet_place_order, null);
        dialog = new BottomSheetDialog(this);
        dialog.setContentView(viewDialog);
        Product receiveProduct = (Product) getIntent().getSerializableExtra("product object");
        adapterItems = new ArrayAdapter<>(this, R.layout.lam_option_list_item, receiveProduct.getAllOptionsName());
        setUpLogicForBottomSheet(receiveProduct);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            // change StatusBarColor
            window.setStatusBarColor(getColor(R.color.light_canvas));
            // change color of icons in status bar
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        binding = ActivityProductDetailBinding.inflate(getLayoutInflater());

        binding.backButton.setOnClickListener(view ->  {
            finish();
        });

        List<String> displayImageUrl = receiveProduct.copyListImage();
        receiveProduct.getOptions().forEach(option -> {
            if (!option.getOptionImage().isEmpty()) {
                displayImageUrl.add(option.getOptionImage());
            }
        });

        SliderAdapter sliderAdapter = new SliderAdapter(displayImageUrl);

        binding.productImageSliderView.setSliderAdapter(sliderAdapter);
        binding.productImageSliderView.setIndicatorAnimation(IndicatorAnimationType.THIN_WORM);
        binding.productImageSliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        binding.productImageSliderView.startAutoCycle();


        binding.productName.setText(receiveProduct.getName());

        if (receiveProduct.getOptions().size() == 1) {
            binding.productPrice.setText(currencyFormatter.format(receiveProduct.getOptions().get(0).getPrice()));
            binding.dropListDownOptions.setVisibility(View.GONE);
        } else {
            binding.productPrice.setText(currencyFormatter.format(receiveProduct.getOptions().get(0).getPrice()) + " - " + currencyFormatter.format(receiveProduct.getOptions().get(receiveProduct.getOptions().size() - 1).getPrice()));
        }


        binding.optionsAutoCompleteTextView.setAdapter(adapterItems);

        binding.optionsAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                getCurrentFocus().clearFocus();
                binding.productPrice.setText(currencyFormatter.format(receiveProduct.getOptions().get(position).getPrice()));
                binding.productImageSliderView.stopAutoCycle();
                if (receiveProduct.getOptions().get(position).getOptionImage().isEmpty()) {
                    binding.productImageSliderView.setCurrentPagePosition(0);
                } else {
                    binding.productImageSliderView.setCurrentPagePosition(receiveProduct.getImages().size() + position);
                }
            }
        });

        binding.favoriteButton.setOnClickListener(view -> {

            isFavourite = !isFavourite;

            if (isFavourite) {
                // Create a color filter with the desired tint color
                ColorFilter colorFilter = new PorterDuffColorFilter(getColor(R.color.dark_secondary), PorterDuff.Mode.SRC_IN);
                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.lam_baseline_favorite_28);
                drawable.setColorFilter(colorFilter);
                binding.favoriteButton.setImageDrawable(drawable);

            } else {
                binding.favoriteButton.setImageDrawable(getDrawable(R.drawable.lam_baseline_favorite_border_28));
            }
        });

        binding.shoppingCartButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, CustomerActivity.class);
            intent.putExtra("targetedFragment", R.id.customerShoppingCardFragment);
            startActivity(intent);
        });

        binding.addToCartButton.setOnClickListener(view -> openAddToCartBottomSheet());

        binding.orderNowButton.setOnClickListener(view -> openOrderNowBottomSheet());

        setContentView(binding.getRoot());
    }

    private void openAddToCartBottomSheet() {
        viewDialog.findViewById(R.id.add_to_cart_button).setVisibility(View.VISIBLE);
        viewDialog.findViewById(R.id.order_now_button).setVisibility(View.GONE);

        dialog.show();
    }

    private void openOrderNowBottomSheet() {
        viewDialog.findViewById(R.id.add_to_cart_button).setVisibility(View.GONE);
        viewDialog.findViewById(R.id.order_now_button).setVisibility(View.VISIBLE);

        dialog.show();
    }

    private void setUpLogicForBottomSheet(Product product) {
        ImageView productImage = viewDialog.findViewById(R.id.product_image);
        Picasso.get().load(product.getImages().get(0)).into(productImage);

        TextView priceTextView = viewDialog.findViewById(R.id.product_price);
        TextView productCurrentQuantity = viewDialog.findViewById(R.id.product_current_quantity);

        TextInputLayout dropListDownOptions = viewDialog.findViewById(R.id.drop_list_down_options);
        AutoCompleteTextView optionsAutoCompleteTextView = viewDialog.findViewById(R.id.options_auto_complete_text_view);

        View spacer = viewDialog.findViewById(R.id.spacer);

        if (product.getOptions().size() == 1) {
            dropListDownOptions.setVisibility(View.GONE);
            spacer.setVisibility(View.GONE);
            priceTextView.setText(currencyFormatter.format(product.getOptions().get(0).getPrice()));
            productCurrentQuantity.setText("Stock: " + product.getOptions().get(0).getCurrentQuantity());
        } else {
            optionsAutoCompleteTextView.setAdapter(adapterItems);
        }


        TextView productCount = viewDialog.findViewById(R.id.product_count);
        ImageButton incButton = viewDialog.findViewById(R.id.inc_button);
        ImageButton decButton = viewDialog.findViewById(R.id.dec_button);

        optionsAutoCompleteTextView.setOnItemClickListener((adapterView, view, position, l) -> {
            optionsAutoCompleteTextView.clearFocus();

            Option selectedOption = product.getOptions().get(position);
            if (!selectedOption.getOptionImage().isEmpty()) {
                Picasso.get().load(selectedOption.getOptionImage()).into(productImage);
            }
            priceTextView.setText(currencyFormatter.format(selectedOption.getPrice()));
            productCurrentQuantity.setText("Stock: " + selectedOption.getCurrentQuantity());

            productCount.setText(
                    String.valueOf(
                            Math.min(selectedOption.getCurrentQuantity(), Integer.parseInt(productCount.getText().toString())
                            )
                    )
            );
        });

        incButton.setOnClickListener(view -> {
            int count = Integer.parseInt(productCount.getText().toString());

            if (!productCurrentQuantity.getText().toString().isEmpty()) {
                String numberStr = productCurrentQuantity.getText().toString().replaceAll("\\D+", "");
                if (count < Integer.parseInt(numberStr)) {
                    productCount.setText(String.valueOf(count + 1));
                }
            } else {
                productCount.setText(String.valueOf(count + 1));
            }


        });

        decButton.setOnClickListener(view -> {
            int count = Integer.parseInt(productCount.getText().toString());
            if (count >= 2) {
                productCount.setText(String.valueOf(count - 1));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}