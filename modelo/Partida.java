package modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa una partida de ajedrez
 */
public class Partida {
    
    private List<Turno> turnos;
    
    /**
     * Constructor
     */
    public Partida() {
        this.turnos = new ArrayList<>();
    }
    
    /**
     * Agrega un turno a la partida
     * @param turno Turno a agregar
     */
    public void agregarTurno(Turno turno) {
        turnos.add(turno);
    }
    
    /**
     * Obtiene los turnos de la partida
     * @return Lista de turnos
     */
    public List<Turno> getTurnos() {
        return turnos;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Turno turno : turnos) {
            sb.append(turno).append(" ");
        }
        return sb.toString().trim();
    }
}
