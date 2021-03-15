package com.vinod.lib.utils;

public class PersonDO implements Cloneable{
    int id = 0;
    String name="" ;
    char gender = 'M';
    AddressDO addressDO;

    public PersonDO(int id, String name, char gender) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        addressDO = new AddressDO();
    }
    public PersonDO() {
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Object x = super.clone();
        this.addressDO = (AddressDO) (((PersonDO) x).addressDO).clone();
        return  x ;
    }
}
