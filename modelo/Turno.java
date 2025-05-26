package modelo;

/**
 * Clase que representa un turno en una partida de ajedrez
 */
public class Turno {
    
    private int numero;
    private Jugada jugadaBlanca;
    private Jugada jugadaNegra;
    
    /**
     * Constructor
     * @param numero Número del turno
     */
    public Turno(int numero) {
        this.numero = numero;
    }
    
    /**
     * Obtiene el número del turno
     * @return Número del turno
     */
    public int getNumero() {
        return numero;
    }
    
    /**
     * Establece la jugada blanca
     * @param jugadaBlanca Jugada blanca
     */
    public void setJugadaBlanca(Jugada jugadaBlanca) {
        this.jugadaBlanca = jugadaBlanca;
    }
    
    /**
     * Obtiene la jugada blanca
     * @return Jugada blanca
     */
    public Jugada getJugadaBlanca() {
        return jugadaBlanca;
    }
    
    /**
     * Establece la jugada negra
     * @param jugadaNegra Jugada negra
     */
    public void setJugadaNegra(Jugada jugadaNegra) {
        this.jugadaNegra = jugadaNegra;
    }
    
    /**
     * Obtiene la jugada negra
     * @return Jugada negra
     */
    public Jugada getJugadaNegra() {
        return jugadaNegra;
    }
    
    @Override
    //Metodo para representar el turno como una cadena de texto
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(numero).append(". ").append(jugadaBlanca);
        
        if (jugadaNegra != null) {
            sb.append(" ").append(jugadaNegra);
        }
        
        return sb.toString();
    }
}
