package modelo;

/**
 * Clase que representa el resultado del análisis sintáctico
 */
public class ResultadoAnalisis {
    
    private boolean valido;
    private String mensajeError;
    private int numeroTurnos;
    
    /**
     * Constructor para resultado inválido
     * @param valido Indica si el análisis es válido
     * @param mensajeError Mensaje de error
     */
    public ResultadoAnalisis(boolean valido, String mensajeError) {
        this.valido = valido;
        this.mensajeError = mensajeError;
        this.numeroTurnos = 0;
    }
    
    /**
     * Constructor para resultado válido
     * @param valido Indica si el análisis es válido
     * @param numeroTurnos Número de turnos analizados
     */
    public ResultadoAnalisis(boolean valido, int numeroTurnos) {
        this.valido = valido;
        this.mensajeError = "";
        this.numeroTurnos = numeroTurnos;
    }
    
    /**
     * Verifica si el análisis es válido
     * @return true si el análisis es válido, false en caso contrario
     */
    public boolean esValido() {
        return valido;
    }
    
    /**
     * Obtiene el mensaje de error
     * @return Mensaje de error
     */
    public String getMensajeError() {
        return mensajeError;
    }
    
    /**
     * Obtiene el número de turnos analizados
     * @return Número de turnos analizados
     */
    public int getNumeroTurnos() {
        return numeroTurnos;
    }
}
