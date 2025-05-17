package modelo;

/**
 * Clase que representa una jugada en una partida de ajedrez
 */
public class Jugada {
    
    private String notacion;
    
    /**
     * Constructor
     * @param notacion Notación de la jugada
     */
    public Jugada(String notacion) {
        this.notacion = notacion;
    }
    
    /**
     * Obtiene la notación de la jugada
     * @return Notación de la jugada
     */
    public String getNotacion() {
        return notacion;
    }
    
   
}