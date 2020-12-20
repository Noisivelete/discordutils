/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.noisivelet.discordutils.utils;

/**
 * Un par tiene dos valores. Se puede acceder a ellos usando {@link #key} y {@link #value}, respectivamente. 
 * @param <T> Tipo del primer valor.
 * @param <U> Tipo del segundo valor
 */
public class Pair<T,U> {
    /**
     * Primer valor del par
     */
    public final T key;
    /**
     * Segundo valor del par
     */
    public final U value;
    
    public Pair(T key, U value){
        this.key=key;
        this.value=value;
    }
}
