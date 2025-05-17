package principal;

import analizador.AnalizadorSintactico;
import analizador.AnalizadorGramatica;
import modelo.ResultadoAnalisis;

import java.util.Scanner;

/**
 * Clase principal que ejecuta el analizador sintáctico
 */
public class Principal {

public static void main(String[] args) {
     Scanner scanner = new Scanner(System.in);
     boolean continuar = true;
     
     System.out.println("=== ANALIZADOR SINTÁCTICO DE AJEDREZ ===");
     
     while (continuar) {
          System.out.println("\nOpciones:");
          System.out.println("1. Analizar una partida");
          System.out.println("2. Salir");
          System.out.print("\nSeleccione una opción (1-2): ");
          
          String opcion = scanner.nextLine().trim();
          
          switch (opcion) {
               case "1":
                    analizarPartida(scanner);
                    break;
               case "2":
                    continuar = false;
                    System.out.println("\n¡Gracias por usar el Analizador Sintáctico de Ajedrez!");
                    break;
               default:
                    System.out.println("\nOpción no válida. Por favor, seleccione 1 o 2.");
                    break;
          }
     }
     
     scanner.close();
}

/**
     * Método para analizar una partida
     * @param scanner Scanner para leer la entrada del usuario
     */
private static void analizarPartida(Scanner scanner) {
     System.out.println("\nIngrese la partida completa en notación algebraica estándar (SAN):");
     System.out.println("Ejemplo: 1. d4 d5 2. Bf4 Nf6 3. e3 e6...");
     
     String partidaTexto = scanner.nextLine().trim();
     
     if (partidaTexto.isEmpty()) {
          System.out.println("No se ingresó ninguna partida.");
          return;
     }
     
        // Analizar la partida
     AnalizadorSintactico analizador = new AnalizadorSintactico();
     ResultadoAnalisis resultado = analizador.analizar(partidaTexto);
     
        // Mostrar resultado
     if (resultado.esValido()) {
          System.out.println("\nLa partida es sintácticamente válida.");
          System.out.println("Número de turnos analizados: " + resultado.getNumeroTurnos());
          
          // Mostrar el árbol de derivación
          AnalizadorGramatica analizadorGramatica = new AnalizadorGramatica();
          analizadorGramatica.analizar(partidaTexto);
     } else {
          System.out.println("\nLa partida contiene errores sintácticos:");
          System.out.println(resultado.getMensajeError());
     }
     
     System.out.println("\nPresione Enter para continuar...");
     scanner.nextLine();
     }
}