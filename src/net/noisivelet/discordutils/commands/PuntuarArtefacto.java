/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.noisivelet.discordutils.commands;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Message.Attachment;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import static net.noisivelet.discordutils.DiscordUtils.EXECUTOR;
import static net.noisivelet.discordutils.DiscordUtils.strToArray;
import static net.noisivelet.discordutils.DiscordUtils.log;
import net.noisivelet.discordutils.tasks.OCRProcessTask;
import net.noisivelet.discordutils.utils.EmbededMessages;


/**
 *
 * @author Francis
 */
public class PuntuarArtefacto extends Command{
    /**
     * Obtiene el idioma que usar para la consulta a la API del OCR
     * @param arg Parámetro idioma pasado en el comando
     * @return "spa" si el idioma a usar es español, "eng" si es inglés
     */
    private static String getLang(String arg){
        switch(arg.toLowerCase()){
            default:
            case "es":
                return "spa";
            case "en":
                return "eng";
        }
    }

    public PuntuarArtefacto() {
        super("puntuarartefacto", "Puntúa un artefacto de Genshin Impact, adjuntando una imagen. Máx. 1MB.");
    }

    @Override
    public List<String> getAliases() {
        ArrayList<String> _ret=new ArrayList<>();
        _ret.add("partefacto");
        _ret.add("artefacto");
        _ret.add("part");
        return _ret;
    }
    
    @Override
    public void run(MessageReceivedEvent event) {
        Message msg=event.getMessage();
        List<Attachment> attachments=msg.getAttachments();
        String url;
        String lang="spa";
        String[] msg_array=strToArray(msg.getContentRaw());
        if(!attachments.isEmpty()){
            url=attachments.get(0).getUrl();
            
            //Comprobar si se ha especificado un idioma como argumento, para pasarlo a la API
            if(msg_array.length>1)
                lang=getLang(msg_array[1]);
            
        } else {
            if(msg_array.length==1){
                event.getChannel().sendMessage(EmbededMessages.infoMessage("!PuntuarArtefacto - Uso", "**Uso correcto: `!puntuarartefacto <url a una imagen> [idioma=es]`**\n\n- Puedes adjuntar un archivo al mensaje en lugar de poner una URL.\n- Para idioma, los valores admitidos son `en` (ingés) o `es` (español).\n- Tamaño máximo de la imagen: 1MB.\n\n*------*\n**Ejemplo:** `!puntuarartefacto https://cdn.discordapp.com/attachments/653597084318171169/790009265561010186/unknown.png en`")).submit();
                return;
            }
            
            if(msg_array.length>2)
                lang=getLang(msg_array[2]);
            
            url=strToArray(msg.getContentRaw())[1];
            try {
                URL imageUrl=new URL(url);
            } catch (MalformedURLException ex) {
                log("Comando no ejecutado - URL malformada.");
                event.getChannel().sendMessage(EmbededMessages.errorMessage("URL incorrecta", "La URL que has introducido no es una URL correcta. Comprueba la escritura y vuelve a intentarlo.")).submit();
                return;
            }
        }
        
        OCRProcessTask task=new OCRProcessTask(event, lang, url);
        EXECUTOR.submit(task);
    }
    
}
