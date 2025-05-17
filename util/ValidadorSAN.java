package util;

import java.util.regex.Pattern;

/**
 * Clase de utilidad para validar notación SAN
 */
public class ValidadorSAN {
    
    // Patrones para validar diferentes tipos de jugadas
    private static final Pattern PATRON_ENROQUE = Pattern.compile("^O-O(-O)?$");
    private static final Pattern PATRON_MOVIMIENTO_PIEZA = Pattern.compile("^[KQRBN][a-h1-8]?[a-h1-8]?x?[a-h][1-8](=[QRBN])?[+#]?$");
    private static final Pattern PATRON_MOVIMIENTO_PEON = Pattern.compile("^([a-h]x)?[a-h][1-8](=[QRBN])?[+#]?$");
    
    /**
     * Verifica si una jugada es válida según la gramática BNF
     * @param jugada Notación de la jugada
     * @return true si la jugada es válida, false en caso contrario
     */
    public static boolean esJugadaValida(String jugada) {
        // Verificar si es un enroque
        if (PATRON_ENROQUE.matcher(jugada).matches()) {
            return true;
        }
        
        // Verificar si es un movimiento de pieza
        if (PATRON_MOVIMIENTO_PIEZA.matcher(jugada).matches()) {
            return true;
        }
        
        // Verificar si es un movimiento de peón
        if (PATRON_MOVIMIENTO_PEON.matcher(jugada).matches()) {
            return true;
        }
        
        return false;
    }
    
    /**
     * Verifica si una casilla es válida
     * @param casilla Notación de la casilla
     * @return true si la casilla es válida, false en caso contrario
     */
    public static boolean esCasillaValida(String casilla) {
        if (casilla.length() != 2) {
            return false;
        }
        
        char columna = casilla.charAt(0);
        char fila = casilla.charAt(1);
        
        return columna >= 'a' && columna <= 'h' && fila >= '1' && fila <= '8';
    }
    
    /**
     * Verifica si una pieza es válida
     * @param pieza Notación de la pieza
     * @return true si la pieza es válida, false en caso contrario
     */
    public static boolean esPiezaValida(char pieza) {
        return pieza == 'K' || pieza == 'Q' || pieza == 'R' || pieza == 'B' || pieza == 'N';
    }
}
