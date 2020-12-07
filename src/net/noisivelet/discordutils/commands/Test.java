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
public class Test extends Command{

    public Test() {
        super("test", "Prueba el funcionamiento del bot.");
    }

    @Override
    public void run(MessageReceivedEvent event) {
        event.getChannel().sendMessage("Test").submit();
    }
    
}
