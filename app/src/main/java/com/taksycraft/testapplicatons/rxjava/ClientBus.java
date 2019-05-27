package com.taksycraft.testapplicatons.rxjava;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class ClientBus {
    private ClientBus clientBus;
    private  static  Observable observable;

    public static  Observable  getObservable()
   {
       if (observable == null) {
           observable = BehaviorSubject.create();
       }
       return observable;
   }
}
