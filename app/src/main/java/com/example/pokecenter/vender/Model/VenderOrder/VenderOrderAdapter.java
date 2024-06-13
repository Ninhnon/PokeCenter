package com.example.pokecenter.vender.Model.VenderOrder;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokecenter.R;
import com.example.pokecenter.customer.lam.Interface.OrderState;
import com.example.pokecenter.customer.lam.State.Order;

import com.example.pokecenter.customer.lam.State.PackagedState;
import com.example.pokecenter.vender.API.FirebaseSupportVenderDP;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VenderOrderAdapter extends RecyclerView.Adapter<ReceiveOrderAdapter.ReceiveOrderViewHolder> {

    private Context mContext;
    private List<Order> mOrders;

    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy 'at' HH:mm");

    public VenderOrderAdapter(Context context, List<Order> orders) {
        this.mContext = context;
        this.mOrders = orders;
    }

    public void setData(List<Order> orders) {
        this.mOrders = orders;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ReceiveOrderAdapter.ReceiveOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ReceiveOrderAdapter.ReceiveOrderViewHolder holder, int position) {

    }



    @Override
    public int getItemCount() {
        if (mOrders != null) {
            return mOrders.size();
        }
        return 0;
    }

    public class ReceiveOrderViewHolder extends RecyclerView.ViewHolder {

        private TextView totalAmount;
        private TextView createDateTime;
        private TextView name;
        private TextView phoneNumber;
        private TextView address;


        private LinearLayout expandableLayout;
        private LinearLayout listOrders;

        private Button packagedBtn;
        private Button cancelBtn;

        private ProgressBar progressBar;


        public ReceiveOrderViewHolder(@NonNull View itemView) {
            super(itemView);

            totalAmount = itemView.findViewById(R.id.total_amount);
            createDateTime = itemView.findViewById(R.id.createDateTime);
            name = itemView.findViewById(R.id.name);
            phoneNumber = itemView.findViewById(R.id.phoneNumber);
            address = itemView.findViewById(R.id.address);

            expandableLayout = itemView.findViewById(R.id.expandable_part);
            listOrders = itemView.findViewById(R.id.list_orders);

            packagedBtn = itemView.findViewById(R.id.packedOrder);
            cancelBtn = itemView.findViewById(R.id.cancelOrder);

            progressBar = itemView.findViewById(R.id.progress_bar);



            packagedBtn.setOnClickListener(view -> {

                packagedBtn.setVisibility(View.GONE);
                cancelBtn.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);

                int pos = getAbsoluteAdapterPosition();
                Order order = mOrders.get(pos);

                ExecutorService executor = Executors.newSingleThreadExecutor();
                Handler handler = new Handler(Looper.getMainLooper());


                executor.execute(() -> {


                    order.acceptState();

                    handler.post(() -> {

                            order.acceptState();
                            mOrders.remove(pos);
                            notifyItemRemoved(pos);

                            Toast.makeText(mContext, "Order status changed", Toast.LENGTH_SHORT)
                                    .show();

                            progressBar.setVisibility(View.GONE);

                        });
                });
            });

            cancelBtn.setOnClickListener(view -> {

                packagedBtn.setVisibility(View.GONE);
                cancelBtn.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);

                int pos = getAbsoluteAdapterPosition();
                Order order = mOrders.get(pos);

                ExecutorService executor = Executors.newSingleThreadExecutor();
                Handler handler = new Handler(Looper.getMainLooper());


                executor.execute(() -> {
                    handler.post(() -> {
                    order.cancelState();
                    mOrders.remove(pos);
                    notifyItemRemoved(pos);
                    Toast.makeText(mContext, "Order status changed", Toast.LENGTH_SHORT)
                    .show();
                    progressBar.setVisibility(View.GONE);
                    });

                });
            });

            itemView.setOnClickListener(view -> {
                int pos = getAbsoluteAdapterPosition();
                Order order = mOrders.get(pos);
                order.toggleExpand();
                if (order.isExpand()) {
                    expandableLayout.setVisibility(View.VISIBLE);
                } else {
                    expandableLayout.setVisibility(View.GONE);
                }

            });
        }
    }

}
