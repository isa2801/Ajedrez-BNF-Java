package modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa una partida de ajedrez
 */
public class Partida {
    //Creo la lista de turnos 
    private List<Turno> turnos;
    
    /**
     * Constructor
     */
    public Partida() { // Digo que la partida se compondrá de la lista d turno 
        this.turnos = new ArrayList<>();
    }
    
    /**
     * Agrega un turno a la partida
     * @param turno Turno a agregar
     */

     //Funcion para agrepgar un turno a la lista de turnos
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
        //Eficiente para construir cadenas de texto concatenadas
        StringBuilder sb = new StringBuilder();
        for (Turno turno : turnos) {
            sb.append(turno).append(" ");
        }
        return sb.toString().trim();
    }
}
