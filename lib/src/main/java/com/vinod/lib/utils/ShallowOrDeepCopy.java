package com.vinod.lib.utils;

import java.util.LinkedList;

public class ShallowOrDeepCopy {
    public static void main(String args[]) throws CloneNotSupportedException {
        PersonDO person = new PersonDO(1,"Vinod",'M');


        person.addressDO = new AddressDO();
        person.addressDO.address1 = "Address1";
        person.addressDO.Street = "street";

        PersonDO p2 = (PersonDO) person.clone();
        p2.id = 2;
        p2.name= "vinod2";
        p2.gender = 'F';

        person.addressDO.address1 = "Address2";
        person.addressDO.Street = "street2";

        System.out.println(person.id+" "+person.name+" "+person.gender+" "+person.addressDO.address1+" "+person.addressDO.Street);
        System.out.println(p2.id+" "+p2.name+" "+p2.gender+" "+p2.addressDO.address1+", "+p2.addressDO.Street);
        LinkedList<PersonDO> ll = new LinkedList<PersonDO>();
        ll.add(new PersonDO(1,"",'a'));

    }
}
