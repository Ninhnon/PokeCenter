package com.example.pokecenter.customer.lam.Model.cart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokecenter.R;
import com.example.pokecenter.customer.lam.Interface.AddressRecyclerViewInterface;
import com.example.pokecenter.customer.lam.Interface.CartRecyclerViewInterface;
import com.example.pokecenter.customer.lam.Model.address.AddressAdapter;
import com.example.pokecenter.customer.lam.Model.product.Option;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context mContext;
    private List<Cart> mCarts;
    private final CartRecyclerViewInterface cartRecyclerViewInterface;

    public CartAdapter(Context context, List<Cart> carts, CartRecyclerViewInterface cartRecyclerViewInterface) {
        this.mContext = context;
        this.mCarts = carts;
        this.cartRecyclerViewInterface = cartRecyclerViewInterface;
    }

    public void setData(List<Cart> carts) {
        this.mCarts = carts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lam_cart_item, parent, false);
        return new CartViewHolder(view, cartRecyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Cart cart = mCarts.get(position);

        holder.productName.setText(cart.getProduct().getName());

        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        if (cart.getSelectedOption() == -1) {
            Picasso.get().load(cart.getProduct().getImages().get(0)).into(holder.productImage);
            holder.selectOptionsButton.setVisibility(View.GONE);
            holder.productPrice.setText(currencyFormatter.format(cart.getProduct().getOptions().get(0).getPrice()));
        } else {
            Option selectedOption = cart.getProduct().getOptions().get(cart.getSelectedOption());

            if (selectedOption.getOptionImage().isEmpty()) {
                Picasso.get().load(cart.getProduct().getImages().get(0)).into(holder.productImage);
            } else {
                Picasso.get().load(selectedOption.getOptionImage()).into(holder.productImage);
            }

            holder.selectedOption.setText(selectedOption.getOptionName());
            holder.productPrice.setText(currencyFormatter.format(selectedOption.getPrice()));
        }
        holder.quantity.setText(String.valueOf(cart.getQuantity()));
    }

    @Override
    public int getItemCount() {
        if (mCarts == null ) {
            return 0;
        }
        return mCarts.size();
    }

    public class CartViewHolder  extends RecyclerView.ViewHolder {
        private ImageView productImage;
        private TextView productName;
        private LinearLayout selectOptionsButton;
        private TextView selectedOption;
        private TextView productPrice;
        private TextView quantity;

        public CartViewHolder(@NonNull View itemView, CartRecyclerViewInterface cartRecyclerViewInterface) {
            super(itemView);

            productImage = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.product_name);
            selectOptionsButton = itemView.findViewById(R.id.select_options_button);
            selectedOption = itemView.findViewById(R.id.selectedOption);
            productPrice = itemView.findViewById(R.id.product_price);
            quantity = itemView.findViewById(R.id.quantity);

            ImageButton deleteButton = itemView.findViewById(R.id.delete_button);
            deleteButton.setOnClickListener(view -> {
                if (cartRecyclerViewInterface != null) {
                    int pos = getAbsoluteAdapterPosition();

                    if (pos != RecyclerView.NO_POSITION) {
                        cartRecyclerViewInterface.onDeleteButtonClick(pos);
                    }
                }
            });
        }
    }
}
