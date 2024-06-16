package com.example.pokecenter.customer.lam.Model.voucher;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BlockVoucherTypeFactory {
    static List<BlockVoucherType> cache = new ArrayList<>();

    public BlockVoucherType GetBlockVoucherType(
            String name, Date startDate, Date endDate, double value) {
        for (BlockVoucherType type : cache) {
            if (type.getName().equals(name)
                && type.getStartDate().equals(startDate)
                && type.getEndDate().equals(endDate)
                && type.getValue() == value) {
                return type;
            }
        }

        return new BlockVoucherType(name, startDate, endDate, value);
    }
}
