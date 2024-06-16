package com.example.pokecenter.customer.lam.Model.voucher;

public class BlockVoucher {
    String id;
    int quantity;
    BlockVoucherType type;

    public BlockVoucher(
            String id, int quantity, BlockVoucherType type) {
        this.id = id;
        this.quantity = quantity;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public BlockVoucherType getType() {
        return type;
    }

    public void setType(BlockVoucherType type) {
        this.type = type;
    }
}
