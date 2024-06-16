package com.example.pokecenter.customer.lam.Model.voucher;

public class Voucher {
    String key;
    Boolean isUsed;
    BlockVoucher blockVoucher;

    public Voucher(
            String key, Boolean isUsed, BlockVoucher blockVoucher) {
        this.key = key;
        this.isUsed = isUsed;
        this.blockVoucher = blockVoucher;
    }

    public Voucher clone(){
        return new Voucher(this.key, this.isUsed, this.blockVoucher);
    }

    public String getKey() {
        return key;
    }

    public Boolean getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(Boolean isUsed) {
        this.isUsed = isUsed;
    }

    public BlockVoucher getBlockVoucher() {
        return blockVoucher;
    }

    public void setBlockVoucherType(BlockVoucher blockVoucher) {
        this.blockVoucher = blockVoucher;
    }
}
