/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class listtest {
    public static void main(String[] args) {
        for(int i = 1; i<= 5000; i++)
            System.out.println(Math.random()*10000);
        List<objtest> list = new ArrayList<objtest>();
        list.add(new objtest("1"));
        list.add(new objtest("2"));
        list.add(new objtest("3"));
        List<objtest> list2 = new ArrayList<objtest>(list);
        
        listtest test = new listtest();
        test.test(list2);
        
        System.out.println(list.toString());
        System.out.println(list2.toString());
        
    }
    
    public void test(List<objtest> list){
        list.get(0).setValue("4");
    }
}
