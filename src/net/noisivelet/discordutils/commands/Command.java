/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.noisivelet.discordutils.commands;

import java.util.ArrayList;
import java.util.List;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/**
 * Clase abstracta para la creación de nuevos comandos. El simple hecho de que exista en este paquete un archivo java<br>
 * que herede esta clase hará que se añada, una vez inicializado el bot, el comando al bot.<br>
 * @author Francis
 */
public abstract class Command {
    public final String nombre;
    public final String desc;
    
    public Command(String nombre, String desc){
        this.nombre=nombre;
        this.desc=desc;
    }
    
    /**
     * Este método se ejecutará cuando se escriba este comando en un canal de Discord.
     * @param event Evento que activó esta función y que contiene el mensaje escrito por el usuario en un servidor de Discord
     */
    public abstract void run(MessageReceivedEvent event);
    
    /**
     * Este método devuelve una lista de aliases para el comando<br>
     * Un alias es otra forma de llamar al comando. 
     * @return Una lista de aliases del comando, o null si no hay ninguno.
     */
    public List<String> getAliases(){
        return null;
    }
}
