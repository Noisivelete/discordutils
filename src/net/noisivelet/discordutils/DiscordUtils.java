/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.noisivelet.discordutils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.noisivelet.discordutils.commands.Command;
import net.noisivelet.discordutils.listener.CommandListener;
import org.reflections.Reflections;

/**
 *
 * @author Francis
 */
public class DiscordUtils {

    /**
     * @param args the command line arguments
     */
    static{
        System.setProperty("java.util.logging.SimpleFormatter.format",
              "[%1$tF %1$tT] [%4$-7s] %5$s %n");
    }
    public static void log(String str){logger.log(Level.INFO, str);}
    public static void error(String str){logger.log(Level.SEVERE, str);}
    public static void warning(String str){logger.log(Level.WARNING, str);}
    public static final Logger logger=Logger.getLogger("DiscordBot");
    public static JDA jda=null;
    public static final HashMap<String, Command> COMANDOS=new HashMap<>();
    public static void main(String[] args) {
        log("Iniciando DiscordUtils...");
        try {
            jda=buildJDA();
        } catch (IOException | LoginException | InterruptedException | IllegalStateException ex) {
            error("La construcción del JDA ha fallado: "+ex.getMessage());
            warning("Cerrando servidor...");
            return;
        }
        log("Cargando comandos...");
        loadCommands();
        
        log("Implementando listeners...");
        jda.addEventListener(new CommandListener());
        
    }

    private static JDA buildJDA() throws IOException, LoginException, FileNotFoundException, InterruptedException{
        JDA _jda;
        File tokenFile=new File("token.discordbot");
        
        if (!tokenFile.exists() || !tokenFile.isFile()){
            log("The token file \"token.discordbot\" doesn't exist. Generating a new one.");
            tokenFile.createNewFile();
            throw new IllegalStateException("El bot debe configurarse antes de usarse.");
        }
        
        if(!tokenFile.canRead())
            throw new IOException("No se puede leer el archivo del token del bot.");
        
        if(tokenFile.length() == 0)
            throw new IllegalStateException("El archivo \"token.discordbot\" no es un archivo de token válido.");
        
        String token=loadTokenFromFile(tokenFile);
        JDABuilder jdab=JDABuilder.create(token, GatewayIntent.GUILD_BANS, GatewayIntent.GUILD_EMOJIS, GatewayIntent.GUILD_INVITES, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.GUILD_MESSAGE_TYPING, GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_VOICE_STATES);
        _jda=jdab.build().awaitReady();
        
        return _jda;
    }
    
    private static String loadTokenFromFile(File file) throws FileNotFoundException, IOException{
        try (BufferedReader is = new BufferedReader(new FileReader(file))) {    
            return is.readLine();
        }
    }
    
    private static void loadCommands(){
        //Inicializar reflections para que actue en el paquete que contiene los comandos
        Reflections reflections = new Reflections("net.noisivelet.discordutils.commands");
        //Coger toda clase dentro del paquete que herede de la superclase Command
        Set<Class<? extends Command>> commands = reflections.getSubTypesOf(Command.class);
        
        //Añadir cada clase a nuestro mapa de comandos
        commands.forEach((_class) -> {
            try {
                //Crear una nueva instancia de este comando y colocarla en el mapa de comandos
                Command c=_class.newInstance();
                COMANDOS.put(c.nombre, c);
                
                //Para cada alias, si tiene alguno, colocar ese alias como un nuevo comando que ejecuta la misma clase
                List<String> aliases=c.getAliases();
                if(aliases!=null){
                    aliases.forEach((alias) -> {
                        COMANDOS.put(alias,c);
                    });
                }
            } catch (InstantiationException | IllegalAccessException ex) {
                ex.printStackTrace();
            }
        });
    }
}
