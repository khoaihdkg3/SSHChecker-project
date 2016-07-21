/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

/**
 *
 * @author Administrator
 */
public class objtest {
    private String value;
    public objtest(String value){
        this.value = value;
    }
    public void setValue(String value){
        this.value = value;
    }
    public String getValue(){
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
    
}
