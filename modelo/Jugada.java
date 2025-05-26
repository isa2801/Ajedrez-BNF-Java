package modelo;

/**
 * Clase que representa una jugada en una partida de ajedrez
 */
public class Jugada {
    //Almacena la notación de la jugada, por ejemplo "e4", "Nf3", "O-O", etc.
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