package com.taksycraft.testapplicatons.kotlin

fun main(arr:Array<String>){
    var N:String  = readLine()!!
    var arr : MutableList<Int> = mutableListOf()
    var temp : MutableList<Int> = mutableListOf()
    var arr2 : MutableList<Int> = mutableListOf()
//
    for (i in 0 until N.toInt()){
         arr.add((readLine()!!).toInt())

    }
    for (i in 0 until N.toInt()){
         arr2.add((readLine()!!).toInt())
    }
    arr.sort()
    println(arr.indexOf(arr[0]))

//    var sum = 1
//    arr.sort()
//    for (i in 0 until N.toInt()){
//        sum  = sum and (arr[i])
//    }
//    println(sum)

}