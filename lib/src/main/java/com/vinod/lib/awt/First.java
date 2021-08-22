package com.vinod.lib.awt;

import java.awt.Button;
import java.awt.Frame;

public class First extends Frame {
    First(){
        Button b=new Button("click me");
        b.setBounds(30,100,80,30);// setting button position
        add(b);//adding button into frame
        setSize(300,300);//frame size 300 width and 300 height
        setLayout(null);//no layout manager
        setVisible(true);//now frame will be visible, by default not visible
        System.out.println(new B(10).index);
    }
    public static void main(String args[]){
        First f=new First();
    }
    class A{
        public int index = 0 ;

    }
    class B extends  A{
        B(int index ){
            index =index;
        }
    }
}