/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.noisivelet.discordutils.tasks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import static net.noisivelet.discordutils.DiscordUtils.OCR_KEY;
import static net.noisivelet.discordutils.DiscordUtils.debug;
import static net.noisivelet.discordutils.DiscordUtils.debug2;
import static net.noisivelet.discordutils.DiscordUtils.log;
import static net.noisivelet.discordutils.DiscordUtils.warning;
import net.noisivelet.discordutils.utils.Artefacto;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Francis
 */
public class OCRProcessTask implements Runnable{

    MessageReceivedEvent event;
    String lang;
    String url;

    public OCRProcessTask(MessageReceivedEvent event, String lang, String url) {
        this.event = event;
        this.lang = lang;
        this.url = url;
    }
    
    
    @Override
    public void run() {
        StringBuilder content;
        try{
            //URL a la que hacer la consulta
            URL request=new URL("https://api.ocr.space/parse/imageurl?apikey="+OCR_KEY+"&url="+url+"&language="+lang+"&OCREngine=2");
            
            debug("[OCRProcessTask] Realizando una consulta GET a "+request.toString());
            HttpURLConnection con = (HttpURLConnection) request.openConnection();
            con.setRequestMethod("GET");
            
            //5 segundos antes de tirar la conexión
            con.setConnectTimeout(5000); 
            con.setReadTimeout(35000);
            
            //Realizamos la consulta
            con.getResponseCode();
            
            //Almacenar los datos de la consulta en un string
            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                String inputLine;
                content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
            }
            con.disconnect();
            
        } catch (IOException e){ 
            event.getChannel().sendMessage("El servidor no responde. Normalmente, repetir el mensaje debería arreglar el problema. Si persiste, puede ser que el servidor de análisis de imágenes esté caído.").submit();
            warning("No se han recibido datos de la respuesta. Error: "+e.getMessage());
            return;
        }
        
        //El resultado es un array, almacenarlo en un objeto para obtener datos
        debug("[OCRProcessTask] Respuesta del servidor: " + content);
        JSONParser parser = new JSONParser();
        JSONObject jo;
        try {
            jo=(JSONObject) parser.parse(content.toString());
        } catch (ParseException ex) {
            ex.printStackTrace();
            return;
        }
        
        //Obtener código de error
        //1 - Todo bien
        //3 - Archivo demasiado grande
        //Cualquier otro código es un error desconocido
        int errorCode=((Long)jo.get("OCRExitCode")).intValue();
        switch(errorCode){
            case 1:
                String texto=((JSONObject)((JSONArray)jo.get("ParsedResults")).get(0)).get("ParsedText").toString();
                //event.getChannel().sendMessage(texto).submit();
                
                //Crear un objeto Artefacto con los datos obtenidos y obtener la valoración
                Artefacto a;
                try{
                    a=new Artefacto(texto);
                    debug2(texto);
                } catch(UnsupportedOperationException ex){
                    event.getChannel().sendMessage("Error: Formato no soportado. Por favor, asegúrate de que NO has cogido la imagen de la ventana de subida de nivel de artefacto, pues no están soportadas.").submit();
                    return;
                }
                
                event.getChannel().sendMessage(a.getValoracion()).submit();
                
                break;
            case 3:
                event.getChannel().sendMessage("La imagen es demasiado grande. Solo se permiten imágenes de un máximo de 1024KB (1MB).").submit();
                break;
            default:
                event.getChannel().sendMessage("Error procesando la imagen. Código de error: "+errorCode).submit();
        }
        
        
    }
    
}
