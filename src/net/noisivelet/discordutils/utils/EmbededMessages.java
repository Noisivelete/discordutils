/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.noisivelet.discordutils.utils;

import java.awt.Color;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.noisivelet.discordutils.DiscordUtils;
import static net.noisivelet.discordutils.DiscordUtils.jda;

/**
 *
 * @author Francis
 */
public final class EmbededMessages {
    public static Message defaultMessage(String title, String description, Color color){
        return new MessageBuilder()
                .setEmbed(new EmbedBuilder()
                        .setAuthor(jda.getSelfUser().getName(), null, jda.getSelfUser().getAvatarUrl())
                        .setColor(color)
                        .setTitle(title)
                        .setDescription(description)
                        .build()
                ).build();
    }
    public static Message errorMessage(String title, String description){
        return defaultMessage(title, description, Color.RED);
    }
    
    public static Message successMessage(String title, String description){
        return defaultMessage(title, description, Color.GREEN);
    }
    
    public static Message infoMessage(String title, String description){
        return defaultMessage(title, description, Color.CYAN);
    }
    
    public static MessageEmbed defaultArtifactEmbed(){
        return new EmbedBuilder()
            .setAuthor(DiscordUtils.jda.getSelfUser().getName(), null, DiscordUtils.jda.getSelfUser().getAvatarUrl())
            .setThumbnail("https://cdn.discordapp.com/attachments/653597084318171169/789212078069252156/unknown.png")
            .setFooter("Bot creado por Noi#0288")
            .setColor(Color.GREEN)
            .setDescription("Se está analizando tu imagen. Por favor, espera unos segundos...")
            .setTitle("Valoración de artefacto")
            .build();
    }
    
}
