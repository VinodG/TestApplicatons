package com.vinod.lib.utils;

public class AddressDO implements Cloneable {
    String country ="";
    String Street ="";
    String address1 ="";

    public AddressDO(String country, String street, String address1) {
        this.country = country;
        Street = street;
        this.address1 = address1;
    }
    public AddressDO() {
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
