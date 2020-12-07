/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.noisivelet.discordutils.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/**
 *
 * @author Francis
 */
public class LimpiarPins extends Command{

    public LimpiarPins() {
        super("limpiarpins", "Borra todos los pins del canal en el que se escribió el comando.");
    }

    @Override
    public void run(MessageReceivedEvent event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
