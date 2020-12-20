/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.noisivelet.discordutils.listener;

import java.util.logging.Level;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import static net.noisivelet.discordutils.DiscordUtils.COMANDOS;
import static net.noisivelet.discordutils.DiscordUtils.error;
import static net.noisivelet.discordutils.DiscordUtils.exception;
import static net.noisivelet.discordutils.DiscordUtils.log;
import net.noisivelet.discordutils.commands.Command;

/**
 *
 * @author Francis
 */
public class CommandListener extends ListenerAdapter{
    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        if(!isCommand(event))
            return;
        
        Command command=getCommand(event);
        if(command==null)
            return;
        
        //Mandar comando ejecutado a consola (Estos son los únicos mensajes que se envían a consola con el bot)
        log(event.getAuthor().getName()+"#"+event.getAuthor().getDiscriminator()+ " @ #" + event.getChannel().getName() + " (Guild " + event.getGuild().getId() + "): " + event.getMessage().getContentRaw());
        try{
            command.run(event);
        } catch (Exception e){
            error("Ha ocurrido un error no capturado ejecutando un comando.");
            error("Comando: "+event.getMessage().getContentRaw());
            exception(e, Level.SEVERE);
        }
        
    }
    
    /**
     * Mira si el mensaje ejecutado es un comando (empieza por "!")
     * @param event Evento que contiene el mensaje
     * @return True si el mensaje comienza por "!", false si no.
     */
    private boolean isCommand(MessageReceivedEvent event){
        return (event.getMessage().getContentRaw().startsWith("!"));
    }
    
    /**
     * Mira si el comando ejecutado está registrado por el bot. Si es así, lo devuelve
     * @param event Evento que contiene el mensaje
     * @return El comando escrito en el evento, o null si no es un comando.
     */
    private Command getCommand(MessageReceivedEvent event){
        String comando=getCommandName(event);
        return COMANDOS.get(comando);
    }

    /**
     * Quita la primera letra de un texto, y devuelve únicamente la primera palabra del mismo.
     * @param event Evento que contiene el texto que se quiere editar
     * @return La primera palabra del texto, sin su primera letra.
     */
    private String getCommandName(MessageReceivedEvent event) {
        String text=event.getMessage().getContentRaw();
        String text_1stword;
        int spaceIndex=text.indexOf(" ");
        if(spaceIndex==-1){
            text_1stword=text.toLowerCase().substring(1);
        } else {
            text_1stword=text.substring(1,spaceIndex);
        }
        return text_1stword;
    }
}
