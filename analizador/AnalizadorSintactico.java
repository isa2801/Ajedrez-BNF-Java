package analizador;

import modelo.Jugada;
import modelo.Partida;
import modelo.ResultadoAnalisis;
import modelo.Turno;
import util.ValidadorSAN;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Clase que implementa el analizador sintáctico para notación SAN
 */
public class AnalizadorSintactico {
    
    private static final Pattern PATRON_TURNO = Pattern.compile("(\\d+)\\.\\s*([A-Za-z0-9\\-O\\+#x=]+)(?:\\s+([A-Za-z0-9\\-O\\+#x=]+))?");
    
    /**
     * Analiza una partida en notación SAN
     * @param textoPartida Texto de la partida en notación SAN
     * @return Resultado del análisis
     */
    public ResultadoAnalisis analizar(String textoPartida) {
        try {
            Partida partida = new Partida();
            List<Turno> turnos = extraerTurnos(textoPartida);
            
            if (turnos.isEmpty()) {
                return new ResultadoAnalisis(false, "No se encontraron turnos válidos en la partida.");
            }
            
            for (Turno turno : turnos) {
                validarTurno(turno);
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
    private List<Turno> extraerTurnos(String textoPartida) {
        List<Turno> turnos = new ArrayList<>();
        Matcher matcher = PATRON_TURNO.matcher(textoPartida);
        
        while (matcher.find()) {
            int numeroTurno = Integer.parseInt(matcher.group(1));
            String jugadaBlanca = matcher.group(2).trim();
            String jugadaNegra = matcher.group(3) != null ? matcher.group(3).trim() : null;
            
            Turno turno = new Turno(numeroTurno);
            turno.setJugadaBlanca(new Jugada(jugadaBlanca));
            
            if (jugadaNegra != null && !jugadaNegra.isEmpty()) {
                turno.setJugadaNegra(new Jugada(jugadaNegra));
            }
            
            turnos.add(turno);
        }
        
        return turnos;
    }
    
    /**
     * Valida un turno según las reglas BNF
     * @param turno Turno a validar
     * @throws IllegalArgumentException Si el turno no es válido
     */
    private void validarTurno(Turno turno) throws IllegalArgumentException {
        // Validar número de turno
        if (turno.getNumero() <= 0) {
            throw new IllegalArgumentException("Número de turno inválido: " + turno.getNumero());
        }
        
        // Validar jugada blanca
        Jugada jugadaBlanca = turno.getJugadaBlanca();
        if (jugadaBlanca == null || jugadaBlanca.getNotacion().isEmpty()) {
            throw new IllegalArgumentException("Falta la jugada blanca en el turno " + turno.getNumero());
        }
        
        if (!ValidadorSAN.esJugadaValida(jugadaBlanca.getNotacion())) {
            throw new IllegalArgumentException("Jugada blanca inválida en turno " + turno.getNumero() + ": " + jugadaBlanca.getNotacion());
        }
        
        // Validar jugada negra (si existe)
        Jugada jugadaNegra = turno.getJugadaNegra();
        if (jugadaNegra != null) {
            if (!ValidadorSAN.esJugadaValida(jugadaNegra.getNotacion())) {
                throw new IllegalArgumentException("Jugada negra inválida en turno " + turno.getNumero() + ": " + jugadaNegra.getNotacion());
            }
        }
    }
}
