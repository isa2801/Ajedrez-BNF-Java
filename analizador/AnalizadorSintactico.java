package analizador;

import modelo.Jugada;
import modelo.Partida;
import modelo.ResultadoAnalisis;
import modelo.Turno;
import util.ValidadorSAN;
// Librerías para el manejo de listas
import java.util.ArrayList;
import java.util.List;
//Librerias para el análisis de expresiones regulares
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Clase que implementa el analizador sintáctico para notación SAN
 */
public class AnalizadorSintactico {
        // Patrón para que los turnos esten en formato "1. e4 e5" o "1. e4 e5 2. Nf3 Nc6"....
    private static final Pattern PATRON_TURNO = Pattern.compile("(\\d+)\\.\\s*([A-Za-z0-9\\-O\\+#x=]+)(?:\\s+([A-Za-z0-9\\-O\\+#x=]+))?");
    
    /**
     * Analiza una partida en notación SAN
     * @param textoPartida Texto de la partida en notación SAN
     * @return Resultado del análisis
     */
    public ResultadoAnalisis analizar(String textoPartida) {
        try {
            //Hace una instancia de Partida y extrae los turnos
            Partida partida = new Partida();
            List<Turno> turnos = extraerTurnos(textoPartida);
            //Si turnos está vacío, retorna un resultado de análisis con error
            if (turnos.isEmpty()) {
                return new ResultadoAnalisis(false, "No se encontraron turnos válidos en la partida.");
            }
            //Crea una variable de tipo Turno y valida cada turno
            for (Turno turno : turnos) {
                validarTurno(turno);
                //La funcion agrregaTurno esta en la clase Partida 
                partida.agregarTurno(turno);
            }
            
            return new ResultadoAnalisis(true, partida.getTurnos().size());
            
        } catch (IllegalArgumentException e) {
            return new ResultadoAnalisis(false, e.getMessage());
        }
    }
    
    /**
     * Extrae los turnos de la partida
     * @param textoPartida Texto de la partida
     * @return Lista de turnos
     */

     //Función para extraer los turnos de la partida
    private List<Turno> extraerTurnos(String textoPartida) {
       // Crea una lista para almacenar los turnos
        List<Turno> turnos = new ArrayList<>();
        // Utiliza el patrón para encontrar los turnos 
        Matcher matcher = PATRON_TURNO.matcher(textoPartida);
        //Itera mientras los turnos coincidan con el patron
        while (matcher.find()) {
            //los groups del matcher contienen el número de turno, la jugada blanca y la jugada negra 
            int numeroTurno = Integer.parseInt(matcher.group(1));
            String jugadaBlanca = matcher.group(2).trim();
            String jugadaNegra = matcher.group(3) != null ? matcher.group(3).trim() : null;
            // Crea una insancia de Turno con el número de turno
            Turno turno = new Turno(numeroTurno);
            //Crea el objeto jugda y almacena la jugada blanca
            turno.setJugadaBlanca(new Jugada(jugadaBlanca));
            
            if (jugadaNegra != null && !jugadaNegra.isEmpty()) {
                turno.setJugadaNegra(new Jugada(jugadaNegra));
            }
            // Va agregando los turnos a la lista
            turnos.add(turno);
        }
        
        return turnos;
    }
    
    /**
     * Valida un turno según las reglas BNF
     * @param turno Turno a validar
     * @throws IllegalArgumentException Si el turno no es válido
     */
    private void validarTurno(Turno turno) throws IllegalArgumentException { //Tiene de parmetro el turno, el metodo tambien puede lanzar una excepcion
        // Validar número de turno
        if (turno.getNumero() <= 0) { //Verifica si el turno es menor o igual a 0, esto no se puede, los turnos empiezan en 1 
            throw new IllegalArgumentException("Número de turno inválido: " + turno.getNumero());
        }
        
        // Validar jugada blanca 
        //Se obtiene la jugada blanca del turno y se verifica que no sea nula o vacía
        Jugada jugadaBlanca = turno.getJugadaBlanca();
        if (jugadaBlanca == null || jugadaBlanca.getNotacion().isEmpty()) {
            throw new IllegalArgumentException("Falta la jugada blanca en el turno " + turno.getNumero());
        }
        //Verifica si la notacio de la jugada blanca es válida usando el validador SAN
        if (!ValidadorSAN.esJugadaValida(jugadaBlanca.getNotacion())) {
            throw new IllegalArgumentException("Jugada blanca inválida en turno " + turno.getNumero() + ": " + jugadaBlanca.getNotacion());
        }
        
        // Validar jugada negra (si existe)
        Jugada jugadaNegra = turno.getJugadaNegra();
        if (jugadaNegra != null) {
            //Si la jugada negra no es válida lanza una excepción
            if (!ValidadorSAN.esJugadaValida(jugadaNegra.getNotacion())) {
                throw new IllegalArgumentException("Jugada negra inválida en turno " + turno.getNumero() + ": " + jugadaNegra.getNotacion());
            }
        }
    }
}
