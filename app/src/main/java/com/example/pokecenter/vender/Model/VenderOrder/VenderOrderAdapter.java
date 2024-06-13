//package com.example.pokecenter.vender.Model.VenderOrder;
//
//import android.content.Context;
//import android.os.Handler;
//import android.os.Looper;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.LinearLayout;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.pokecenter.R;
//
//import com.example.pokecenter.vender.API.FirebaseSupportVenderDP;
//
//import java.io.IOException;
//import java.text.NumberFormat;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//import java.util.Locale;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//public class VenderOrderAdapter extends RecyclerView.Adapter<ReceiveOrderAdapter.ReceiveOrderViewHolder> {
//
//    private Context mContext;
//    private List<Order> mOrders;
//
//    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
//    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy 'at' HH:mm");
//
//    public VenderOrderAdapter(Context context, List<Order> orders) {
//        this.mContext = context;
//        this.mOrders = orders;
//    }
//
//    public void setData(List<Order> orders) {
//        this.mOrders = orders;
//        notifyDataSetChanged();
//    }
//
//
//    @NonNull
//    @Override
//    public ReceiveOrderAdapter.ReceiveOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return null;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ReceiveOrderAdapter.ReceiveOrderViewHolder holder, int position) {
//
//    }
//
//
//
//    @Override
//    public int getItemCount() {
//        if (mOrders != null) {
//            return mOrders.size();
//        }
//        return 0;
//    }
//
//    public class ReceiveOrderViewHolder extends RecyclerView.ViewHolder {
//
//        private TextView totalAmount;
//        private TextView createDateTime;
//        private TextView name;
//        private TextView phoneNumber;
//        private TextView address;
//
//
//        private LinearLayout expandableLayout;
//        private LinearLayout listOrders;
//
//        private Button packaged;
//
//        private ProgressBar progressBar;
//
//
//        public ReceiveOrderViewHolder(@NonNull View itemView) {
//            super(itemView);
//
//            totalAmount = itemView.findViewById(R.id.total_amount);
//            createDateTime = itemView.findViewById(R.id.createDateTime);
//            name = itemView.findViewById(R.id.name);
//            phoneNumber = itemView.findViewById(R.id.phoneNumber);
//            address = itemView.findViewById(R.id.address);
//
//            expandableLayout = itemView.findViewById(R.id.expandable_part);
//            listOrders = itemView.findViewById(R.id.list_orders);
//
//            packaged = itemView.findViewById(R.id.packedOrder);
//
//            progressBar = itemView.findViewById(R.id.progress_bar);
//
//
//
//            packaged.setOnClickListener(view -> {
//
//                packaged.setVisibility(View.GONE);
//                progressBar.setVisibility(View.VISIBLE);
//
//                int pos = getAbsoluteAdapterPosition();
//                Order order = mOrders.get(pos);
//
//                ExecutorService executor = Executors.newSingleThreadExecutor();
//                Handler handler = new Handler(Looper.getMainLooper());
//
//                String newStatus = "Packaged - " + dateFormat.format(new Date());
//
//                executor.execute(() -> {
//
//                    boolean isSuccess = true;
//
//                    try {
//                        new FirebaseSupportVenderDP().changeOrderStatus(order.getId(), newStatus);
//                        new FirebaseSupportVenderDP().pushNotificationForPackaged(order.getId());
//
//                    } catch (IOException e) {
//                        isSuccess = false;
//                    }
//                    ;
//
//                    boolean finalIsSuccess = isSuccess;
//                    handler.post(() -> {
//                        if (finalIsSuccess) {
//                            order.setStatus(newStatus);
//                            order.acceptState();
//                            mOrders.remove(pos);
//                            notifyItemRemoved(pos);
//
//                            Toast.makeText(mContext, "Order status changed", Toast.LENGTH_SHORT)
//                                    .show();
//
//                            progressBar.setVisibility(View.GONE);
//
//                        } else {
//
//                            packaged.setVisibility(View.VISIBLE);
//                            progressBar.setVisibility(View.GONE);
//
//                            Toast.makeText(mContext, "Failed to connect server!", Toast.LENGTH_SHORT)
//                                    .show();
//
//                        }
//                    });
//                });
//            });
//
//            itemView.setOnClickListener(view -> {
//
//                int pos = getAbsoluteAdapterPosition();
//                Order order = mOrders.get(pos);
//                order.toggleExpand();
//                if (order.isExpand()) {
//                    expandableLayout.setVisibility(View.VISIBLE);
//                } else {
//                    expandableLayout.setVisibility(View.GONE);
//                }
//
//            });
//        }
//    }
//
//}
