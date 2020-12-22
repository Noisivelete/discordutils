/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.noisivelet.discordutils.utils;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.logging.Level;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.noisivelet.discordutils.DiscordUtils;
import static net.noisivelet.discordutils.DiscordUtils.debug;
import static net.noisivelet.discordutils.DiscordUtils.exception;
import static net.noisivelet.discordutils.DiscordUtils.warning;

/**
 *
 * @author Francis
 */
public final class Artefacto {

    public static final HashSet<String> 
            NOMBRES_FLOR=getNombresFlor(),
            NOMBRES_PLUMA=getNombresPluma(),
            NOMBRES_RELOJ=getNombresReloj(),
            NOMBRES_CALIZ=getNombresCaliz(),
            NOMBRES_CABEZA=getNombresCabeza();
    
    private static HashSet<String> getNombresFlor() {
        HashSet<String> _ret=new HashSet<>();
        _ret.add("Flor de la Vida");
        _ret.add("Flower of Life");
        return _ret;
    }
    private static HashSet<String> getNombresPluma(){
        HashSet<String> _ret=new HashSet<>();
        _ret.add("Pluma de la Muerte");
        _ret.add("Plume of Death");
        return _ret;
    }
    private static HashSet<String> getNombresReloj(){
        HashSet<String> _ret=new HashSet<>();
        _ret.add("Sands of Eon");
        _ret.add("Arenas de Eón");
        return _ret;
    }
    private static HashSet<String> getNombresCabeza(){
        HashSet<String> _ret=new HashSet<>();
        _ret.add("Circlet of Logos");
        _ret.add("Tiara de Logos");
        return _ret;
    }
    private static HashSet<String> getNombresCaliz(){
        HashSet<String> _ret=new HashSet<>();
        _ret.add("Goblet of Eonothem");
        _ret.add("Cáliz de Eonothem");
        return _ret;
    }
    
    /**
     * Devuelve el rango (Min-máx) de un stat cuando actua como substat.
     * @param stat Stat que se va a calcular
     * @return Un {@link net.noisivelet.discordutils.utils.Pair}, donde el primer elemento es el valor mínimo de ese stat, y el segundo elemento el valor máximo. Si el stat elegido no puede actuar como substat, devuelve null.
     */
    public static Pair<Double, Double> getSubstatRange(Stat stat){
        if(stat==null)
            return null;
        
        switch(stat){
            default:
                return null;
            case ATK:
                return new Pair<>(14.0,19.0);
            case DEF:
            case ELEMENTAL_MASTERY:
                return new Pair<>(16.0,23.0);
            case HP:
                return new Pair<>(209.0,299.0);
            case ATK_PERCENT:
            case HP_PERCENT:
                return new Pair<>(4.1,5.8);
            case DEF_PERCENT:
                return new Pair<>(5.1,7.3);
            case CRIT_RATE:
                return new Pair<>(2.7,3.9);
            case CRIT_DMG:
                return new Pair<>(5.4,7.8);
            case ENERGY_RECHARGE:
                return new Pair<>(4.5,6.5);
        }
    }
    
    /**
     * Calcula el multiplicador de puntuación de artefacto para su stat principal<br>
     * Por defecto, los substats orientados a daño se puntúan más altos en la calculación del valor del artefacto.<br>
     * <br>
     * <u><b>Pluma y flor:</b></u> Siempre 1.0
     * <br><br>
     * <b><u>Reloj:</u></b>
     * <ul>
     *  <li><b>Ataque%:</b> 1.0</li>
     *  <li><b>Recarga de energía:</b> 0.5</li>
     *  <li><b>DEF%, HP%, Maestría elemental:</b> 0.2</li>
     * </ul>
     * <br>
     * <b><u>Cáliz:</u></b>
     * <ul>
     *  <li><b>Bono de daño elemental/físico:</b> 1.0</li>
     *  <li><b>Ataque%:</b> 0.8</li>
     *  <li><b>Recarga de energía:</b> 0.5</li>
     *  <li><b>DEF%, HP%, Maestría elemental:</b> 0.2</li>
     * </ul>
     * <br>
     * <b><u>Cabeza:</u></b>
     * <ul>
     *  <li><b>Daño crítico, Prob. crítico:</b> 1.0</li>
     *  <li><b>Ataque%:</b> 0.8</li>
     *  <li><b>DEF%, HP%, Maestría elemental:</b> 0.2</li>
     *  <li><b>Bono de curación</b> 0.0</li>
     * </ul>
     * 
     * @param stat Stat del cual se quiere saber su multiplicador.
     * @param tipo Tipo de artefacto principal del cual se quiere saber su multiplicador.
     * @return El multiplicador de puntuación para el stat elegido, o null si uno de los dos argumentos es nulo.
     */
    public static Float getMainStatScoreMultiplier(Stat stat, TipoArtefacto tipo){
        if(stat==null||tipo==null)
            return null;
        switch(tipo){
            case PLUMA:
            case FLOR:
                return 1.0F;
            case RELOJ:
                switch(stat){
                    case ATK_PERCENT:
                        return 1.0F;
                    case ENERGY_RECHARGE:
                        return 0.5F;
                    case DEF_PERCENT:
                    case HP_PERCENT:
                    case ELEMENTAL_MASTERY:
                        return 0.2F;
                    default:
                        return null;
                }
            case CÁLIZ:
                if(Stat.isDmgBonus(stat))
                    return 1.0F;
                else switch(stat){
                    case ATK_PERCENT:
                        return 0.8F;
                    case ENERGY_RECHARGE:
                        return 0.5F;
                    case DEF_PERCENT:
                    case HP_PERCENT:
                    case ELEMENTAL_MASTERY:
                        return 0.2F;
                    default:
                        return null;
                }
            case CABEZA:
                switch(stat){
                    case CRIT_DMG:
                    case CRIT_RATE:
                        return 1.0F;
                    case ATK_PERCENT:
                        return 0.8F;
                    case DEF_PERCENT:
                    case HP_PERCENT:
                    case ELEMENTAL_MASTERY:
                        return 0.2F;
                    case HEALING_BONUS:
                        return 0F;
                    default:
                        return null;
                }
            default:
                return null;
        }
    }
    
    /**
     * Calcula el multiplicador de puntuación de artefacto para el substat elegido.<br>
     * Por defecto, los substats orientados a daño se puntúan más altos en la calculación del valor del artefacto. La recarga de energía va después, seguida de la maestría elemental.:<br>
     * <li><b>Daño Crít%, Prob. Crít%:</b> 1.0</li>
     * <li><b>Ataque%</b>: 0.95</li>
     * <li><b>Recarga de energía:</b> 0.85</li>
     * <li><b>Maestría elemental:</b> 0.7</li>
     * <li><b>Vida%, Def%:</b> 0.6</li>
     * <li><b>Ataque:</b> 0.5
     * <li><b>Defensa, Vida:</b> 0.4</li>
     * @param stat Stat del cual se quiere saber su multiplicador
     * @return El multiplicador de puntuación para el stat elegido
     */
    public static double getSubstatScoreMultiplier(Stat stat){
        if(stat==null)
            return 0;
        
        switch(stat){
            default:
                return 0;
            case CRIT_DMG:
            case CRIT_RATE:
                return 1;
            case ATK_PERCENT:
                return 0.95;
            case ENERGY_RECHARGE:
                return 0.85;
            case ELEMENTAL_MASTERY:
                return 0.7;
            case HP_PERCENT:
            case DEF_PERCENT:
                return 0.6;
            case ATK:
                return 0.5;
            case DEF:
            case HP:
                return 0.4;
        }
    }
    
    private TipoArtefacto tipo=null;
    private Integer nivel=null;
    private ArtifactStat principal=null;
    private ArrayList<ArtifactStat> substats=new ArrayList<>();
    private String buffer_stat=null;
    
    public Artefacto(String datos) throws UnsupportedOperationException{
        try (Scanner scanner = new Scanner(datos)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                try{
                    process(line);
                } catch (Exception ex){
                    warning("Error procesando imagen: El texto no se puede convertir a un artefacto.");
                    exception(ex, Level.FINE);
                    throw new UnsupportedOperationException("No se puede convertir este tipo de imagen a un artefacto.");
                }
                
            }
        }
        if(faltanDatos()){
            throw new UnsupportedOperationException("No se puede convertir este tipo de imagen a un artefacto.");
        }
        debug("Tipo: "+tipo.name());
        debug("Nivel: "+nivel);
        debug("Stat principal: "+principal.stat.name()+" +"+principal.cantidad);
        debug("Stats secundarios ("+substats.size()+"): ");
        substats.forEach((substat) -> {
            debug("• "+substat.stat.name()+" +"+substat.cantidad);
        });
        
    }
    

    /**
     * Cambia el stat principal del artefacto.
     * @see #setStat(String, String, boolean, boolean) 
     * @param principal String que determina el stat principal del artefacto
     * @param principal_cantidad Cantidad de stat que tendrá el mismo.
     * @param percent Si el artefacto es porcentual o no
     * @throws IllegalArgumentException Si alguno de los dos primeros parámetros no se puede convertir en un enum Stat o un float, respectivamente.
     */
    private void setPrincipal(String principal, String principal_cantidad, boolean percent) throws IllegalArgumentException{
        setStat(principal, principal_cantidad, percent, true);
    }
    
    /**
     * Cambia el stat principal o añade un stat secundario.
     * @param stat String que determina el stat que vamos a editar o añadir
     * @param cantidad La cantidad de stat que tendrá el mismo
     * @param percent Si este stat es porcentual
     * @param esPrincipal Si es TRUE, se editará el stat principal. De no serlo, se añadirá un stat secundario.
     * @throws IllegalArgumentException Si alguno de los dos primeros parámetros no se puede convertir en un enum Stat o un float, respectivamente.
     */
    private void setStat(String stat, String cantidad, boolean percent, boolean esPrincipal) throws IllegalArgumentException{
        Stat st=Stat.getStat(stat, percent);
        if(st==null)
            throw new IllegalArgumentException("[setStat@Artefacto] \""+principal+"\" no es un Stat válido.");
        try{
            float cant=Float.parseFloat(cantidad);
            ArtifactStat as=new ArtifactStat(st,cant);
            if(esPrincipal){
                this.principal=as;
            } else {
                substats.add(as);
            }
            
        } catch(NumberFormatException ex){
            throw new IllegalArgumentException("[setStat@Artefacto] \""+cantidad+"\" no es un float válido.");
        }
    };
    
    /**
     * Procesa una línea del texto detectado por el OCR.
     * @param line línea que se va a procesar
     */
    private void process(String line) {
        if(line.endsWith("0/0")){
            line=line.substring(0, line.length()-3)+"%";
        }
        //Genshin en español usa la coma para separar. Por ejemplo, 1000 lo muestra como 1,000. La coma da errores al convertir en floats, así que se elimina.
        if(line.contains(",")){ 
            line=line.replace(",", "");
        }
        
        //Posible error de lectura del OCR, que pone algún punto al final si hay ruido en la imagen. Se elimina.
        if(line.endsWith(".")){
            line=line.substring(0, line.length()-1);
        }
        
        //Error común del OCR, reemplaza un % por 96. Lo cambiamos antes de continuar.
        if(line.endsWith("96")){ 
            line=line.substring(0, line.length()-2)+"%";
        }
        
        
        //Procesamiento de la linea tras arreglar posibles errores
        if(line.startsWith("+") && nivel==null){ //Si empieza por + y aún no hemos determinado su nivel, lo es. Será algo así como +20. Quitamos el + y lo asignamos. El primer +<número> es siempre el nivel.
            line=line.substring(1);
            if(line.startsWith(" ")){
                line=line.substring(1);
            }
            nivel=Integer.parseInt(line);
        }
        else if(buffer_stat!=null){ //Si esto es cierto, es porque tenemos la mitad de un substat guardada.
            if(buffer_stat.equals("Bono de Daño")){ //Bonos de daño se separan en 2 líneas en español. 
                buffer_stat+=" "+line;
            } else { //Tenemos el nombre de un substat, pero nos falta el incremento.
                boolean percent=line.endsWith("%");
                boolean plus=line.startsWith("+");

                //Quitar el + del inicio y el % del final, si los tenían.
                if(plus)
                    line=line.substring(1);
                if(percent){
                    line=line.substring(0, line.length()-1);
                }
                //-----

                if(principal==null){ //El primer stat es siempre el principal, así que si no lo hemos cogido aún, es el principal.
                    setPrincipal(buffer_stat, line, percent);
                } else {
                    addSubstat(buffer_stat, line, percent);
                }
                buffer_stat=null;
            }
        }
        //Los siguientes 5 ifs son para determinar si la línea corresponde al tipo del artefacto, comparándolo con los sets de strings. Si lo es, lo asignamos.
        else if(NOMBRES_FLOR.contains(line)){
            tipo=TipoArtefacto.FLOR;
        }
        else if(NOMBRES_PLUMA.contains(line)){
            tipo=TipoArtefacto.PLUMA;
        }
        else if(NOMBRES_RELOJ.contains(line)){
            tipo=TipoArtefacto.RELOJ;
        }
        else if(NOMBRES_CALIZ.contains(line)){
            tipo=TipoArtefacto.CÁLIZ;
        }
        else if(NOMBRES_CABEZA.contains(line)){
            tipo=TipoArtefacto.CABEZA;
        }
        //-------------------------------------------
        
        
        else {
            if(line.startsWith("•") || line.startsWith(". ")){ //Los stats secundarios empiezan por esos caracteres
                line=line.substring(2); //Quitamos lo siguiente: "• "
            }
            if(line.contains("+")){ //Si contiene un +, tenemos tanto el stat como su cantidad.
                String[] split=line.split("\\+");
                if(split[0].endsWith(" "))
                    split[0]=split[0].substring(0, split[0].length()-1);
                boolean percent=split[1].endsWith("%");
                if(percent){
                    split[1]=split[1].substring(0, split[1].length()-1);
                }
                if(Stat.getStat(split[0]) != null)
                    addSubstat(split[0], split[1], percent);
            } else { //No tenemos la cantidad en esta línea, así que hay que buscarla en la siguiente
                if(Stat.getStat(line) != null){
                    buffer_stat=line;
                }
            }
            
        }
    }
    
    /**
     * Realiza una valoración del artefacto.
     * @return Una clase Message de JDA para mandar al canal que pidió la valoración
     */
    public Message getValoracion() {
        if(nivel==null)
            nivel=0;
        MessageBuilder mb=new MessageBuilder();
        String subst="";
        subst = substats.stream().map((stat) -> "• "+stat.stat.name()+" +"+(float)stat.cantidad+"\n").reduce(subst, String::concat);
        String val="";
        double total=0;
        Pair<Double, Double> range;
        double msm=getMainStatScoreMultiplier(principal.stat, tipo);
        val+="• **"+principal.stat.name()+"**: "+(int)(msm*100)+" puntos\n\n";
        ArrayList<Double> score_ss=new ArrayList<>(), multiplier_ss=new ArrayList<>(), perfection_score=new ArrayList<>();
        for(ArtifactStat substat : substats){
            debug("=== SUBSTAT: "+substat.stat.name()+" +"+substat.cantidad+" ===");
            range=getSubstatRange(substat.stat);
            double sm=getSubstatScoreMultiplier(substat.stat);
            debug("Mult x"+sm);
            int upgrade_times=(int)Math.floor(nivel/4);
            debug(substat.cantidad +"/("+upgrade_times+"+1)/"+range.value+"*"+sm);
            double puntos=substat.cantidad/(upgrade_times+1)/range.value*sm;
            puntos*=(1+upgrade_times)/6.0;
            debug("Puntos: "+puntos);
            score_ss.add(puntos);
            multiplier_ss.add(sm);
            double perf_sc=substat.cantidad/((upgrade_times+1)*range.value);
            debug("Perfection score: "+perf_sc);
            perfection_score.add(perf_sc);
            val+="• **"+substat.stat.name()+"**: "+(int)(puntos*100)+" puntos\n";
            total+=puntos;
        }
        total=total*msm*100;
        val+="\nPuntuación final: __**"+(int)total+"**__/146.";
        
        String ver="";
        if(msm < 0.99){ //0.99 porque los puntos flotantes no son buenos siendo comparados
            ver+="- Tu stat principal no es el más adecuado para el tipo de artefacto. Eso afecta mucho a la puntuación.\n";
        }
        if(nivel < 20)
            ver+="- Tu artefacto no está a nivel 20. Puedes comparar este artefacto con otros de su mismo nivel, pero la puntuación variará conforme lo subas.\n";
        for(int i=0;i<perfection_score.size();i++){
            double score=perfection_score.get(i);
            double multiplier=multiplier_ss.get(i);
            if(multiplier<0.69){
                ver+="- El substat #"+(i+1)+" no es un substat muy adecuado para un artefacto DPS.\n";
            }
            if(score>0.98){
                if(nivel==20)
                    ver+="- Tienes un artefacto con rolls perfectos en el substat #"+(i+1)+". Increible.\n";
                else
                    ver+="- El substat #"+(i+1)+" tiene, por el momento, rolls perfectos. Para un artefacto nivel "+nivel+", es perfecto.\n";
            }
            else if(score>0.9){
                ver+="- El substat #"+(i+1)+" está cerca de ser perfecto. GG.\n";
            }
            else if(score>0.66){
                ver+="- El substat #"+(i+1)+" es especialmente bueno. Enhorabuena.\n";
            }
            else if(score>0.33){
                ver+="- El substat #"+(i+1)+" tiene unos rolls decentes.\n";
            }
        }
        
        mb.setEmbed(new EmbedBuilder()
                .setAuthor(DiscordUtils.jda.getSelfUser().getName(), null, DiscordUtils.jda.getSelfUser().getAvatarUrl())
                .setThumbnail("https://cdn.discordapp.com/attachments/653597084318171169/789212078069252156/unknown.png")
                .setFooter("Bot creado por Noi#0288")
                .setColor(Color.GREEN)
                .setDescription("La puntuación de un artefacto se calcula con respecto al porcentaje de perfección del artefacto. Cuanto más cerca del 100%, mejor stat es.")
                .setTitle("Valoración de artefacto")
                .addField("Tipo:", tipo.name(), true)
                .addField("Nivel:", nivel+"", true)
                .addField("Stat principal:", principal.stat.name()+": "+(float)principal.cantidad, false)
                .addField("Substats:", subst, false)
                .addBlankField(false)
                .addField("Valoración:", val, false)
                .addField("Observaciones:", ver,false)
        .build());
        return mb.build();
    }

    /**
     * Añade un nuevo stat secundario al artefacto
     * @param stat Stat que se añadirá, en String
     * @param cantidad Cantidad de stat que se añadirá, en String
     * @param percent Si el stat que se va a añadir es o no porcentual
     * @throws IllegalArgumentException Si el stat o la cantidad de stat que se pasan como argumento no se pueden convertir a un {@link #Stat} o a un float, respectivamente.
     */
    private void addSubstat(String stat, String cantidad, boolean percent) throws IllegalArgumentException{
        if(substats.size()<4)
            setStat(stat, cantidad, percent, false);
    }

    private boolean faltanDatos() {
        boolean _ret=false;
        if(nivel==null){
            debug("Falta el nivel.");
            _ret=true;
        }
        if(principal==null){
            debug("Falta el stat principal.");
            _ret=true;
        }
        if(tipo==null){
            debug("Falta el tipo de artefacto.");
            _ret=true;
        }
        if(substats.isEmpty()){
            debug("No hay substats.");
            _ret=true;
        }
        return _ret;
            
    }
    
    /**
     * Representa el tipo de un artefacto de Genshin Impact.
     */
    public static enum TipoArtefacto {FLOR, PLUMA, RELOJ, CÁLIZ, CABEZA}
    
    /**
     * Representa un stat de un artefacto de Genshin Impact
     */
    public static enum Stat {
        HP, HP_PERCENT, ATK, ATK_PERCENT, DEF, DEF_PERCENT, ENERGY_RECHARGE, ELEMENTAL_MASTERY,
        CRIT_DMG, CRIT_RATE, HEALING_BONUS, HYDRO_DMG, PYRO_DMG, DENDRO_DMG, CRYO_DMG, ELECTRO_DMG,
        GEO_DMG, ANEMO_DMG, PHYSICAL_DMG, DMG_BONUS;
        
        public static Stat getStat(String stat){
            return getStat(stat, false);
        }
        
        public static boolean isDmgBonus(Stat stat){
            return  stat.equals(HYDRO_DMG) ||
                    stat.equals(PYRO_DMG) ||
                    stat.equals(DENDRO_DMG) ||
                    stat.equals(CRYO_DMG) ||
                    stat.equals(ELECTRO_DMG) ||
                    stat.equals(GEO_DMG) ||
                    stat.equals(ANEMO_DMG) ||
                    stat.equals(PHYSICAL_DMG);
        }
        
        public static Stat getStat(String stat, boolean percent){
            switch(stat){
                case "HP":
                case "Hp":
                case "Vida":
                    return percent? HP_PERCENT:HP;
                    
                case "ATK":
                case "ATQ":
                    return percent? ATK_PERCENT:ATK;
                    
                case "DEF":
                    return percent? DEF_PERCENT:DEF;
                    
                case "Energy Recharge":
                case "Recarga de Energía":
                case "Recarga de Energia":
                    return ENERGY_RECHARGE;
                    
                case "Maestría Elemental":
                case "Maestria Elemental":
                case "Elemental Mastery":
                    return ELEMENTAL_MASTERY;
                    
                case "CRIT DMG":
                case "Daño CRIT":
                    return CRIT_DMG;
                    
                case "CRIT Rate":
                case "Prob. CRIT":
                    return CRIT_RATE;
                    
                case "Healing Bonus":
                case "Bono de Curación":
                case "Bono de Curacion":
                    return HEALING_BONUS;
                    
                case "Hydro DMG Bonus":
                case "Bono de Daño Hydro":
                    return HYDRO_DMG;
                        
                case "Pyro DMG Bonus":
                case "Bono de Daño Pyro":
                    return PYRO_DMG;
                        
                case "Electro DMG Bonus":
                case "Bono de Daño Electro":
                    return ELECTRO_DMG;
                        
                case "Geo DMG Bonus":
                case "Bono de Daño Geo":
                    return GEO_DMG;
                        
                case "Cryo DMG Bonus":
                case "Bono de Daño Cryo":
                    return CRYO_DMG;
                        
                case "Dendro DMG Bonus":
                case "Bono de Daño Dendro":
                    return DENDRO_DMG;
                        
                case "Anemo DMG Bonus":
                case "Bono de Daño Anemo":
                    return ANEMO_DMG;
                    
                case "Physical DMG Bonus":
                case "Bono de Daño Físico":
                    return PHYSICAL_DMG;
                    
                case "Bono de Daño": //Solo para detectar que "bono de daño" forma parte del nombre de un stat (estos se suelen separar en 2 líneas en español). No es un stat válido en sí.
                    return DMG_BONUS;
            }
            return null;
        }
    }
    
    /**
     * Representa un stat en un artefacto de Genshin Impact. Contiene un {@link #Stat}, y un float con la cantidad del mismo.
     */
    public class ArtifactStat{
        public final double cantidad;
        public final Stat stat;
        public ArtifactStat(Stat stat, float cantidad){
            this.stat=stat;
            this.cantidad=cantidad;
        }
    }
    
}
