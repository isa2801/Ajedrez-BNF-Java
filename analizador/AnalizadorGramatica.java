package analizador;

import modelo.Jugada;
import modelo.Partida;
import modelo.ResultadoAnalisis;
import modelo.Turno;
 //Librerías necesarias para la interfaz gráfica
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
//Librerías necesarias para expresiones regulares
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnalizadorGramatica {
    // Patrón para extraer los turnos de la partida
    private static final Pattern PATRON_TURNO = Pattern.compile("(\\d+)\\.\\s*([A-Za-z0-9\\-O\\+#x=]+)(?:\\s+([A-Za-z0-9\\-O\\+#x=]+))?");
    
    public ResultadoAnalisis analizar(String textoPartida) {
        try {
            //Se almacena en el objeto partida de la función extraer Partida
            Partida partida = extraerPartida(textoPartida); 
            // Comprueba si la lista de turnos de la partida (obtenida con getTurnos()) está vacía.
            if (partida.getTurnos().isEmpty()) {
                return new ResultadoAnalisis(false, "No se encontraron turnos válidos en la partida.");
            } // Muestra el árbol de la partida en una interfaz gráfica
            mostrarArbolEnInterfaz(partida); //Se le pasa de parametro la partida
            return new ResultadoAnalisis(true, partida.getTurnos().size());
        } catch (Exception e) {
            return new ResultadoAnalisis(false, "Error: " + e.getMessage());
        }
    }
    //Función que extrae los turnos de la partida
    private Partida extraerPartida(String textoPartida) {
    //Instancia un nuevo objeto Partida vacío que se usará para almacenar los datos extraídos.        
        Partida partida = new Partida();
        // Utiliza el patrón definido para encontrar coincidencias con los turnos en el texto de la partida
        Matcher matcher = PATRON_TURNO.matcher(textoPartida);
        
        while (matcher.find()) { //Funciona mientras encuentre coincidencias
            //Obtiene el numero de turno y lo convierte a entero
            int numeroTurno = Integer.parseInt(matcher.group(1));
            //Se extraen las jugadas blanca y negra, eliminando espacios con el trim
            String jugadaBlanca = matcher.group(2).trim();
            String jugadaNegra = matcher.group(3) != null ? matcher.group(3).trim() : null;
            
            // Crea una insancia de Turno con el número de turno
            Turno turno = new Turno(numeroTurno);
            turno.setJugadaBlanca(new Jugada(jugadaBlanca)); //Crea un objeto Jugada con la jugada blanca y lo asigna al turno
            if (jugadaNegra != null && !jugadaNegra.isEmpty()) {
                turno.setJugadaNegra(new Jugada(jugadaNegra));
            }
            // Agrega el turno a la partida
            partida.agregarTurno(turno);
        }
        return partida;
    }
    
    private void mostrarArbolEnInterfaz(Partida partida) {
        // Crea una lista para almacenar las jugadas como String 
        List<String> jugadas = new ArrayList<>();
        //la lista tiene el nodo raíz "Inicio"
        jugadas.add("Inicio"); 
        //Recorre los turnos de la partida y agrega las jugadas a la lista
        for (Turno turno : partida.getTurnos()) {
            jugadas.add(turno.getJugadaBlanca().getNotacion());
            if (turno.getJugadaNegra() != null) {
                jugadas.add(turno.getJugadaNegra().getNotacion());
            }
        }
        //Usa invokeLater para asegurar que la creación de la interfaz gráfica se ejecute en el hilo de eventos de Swing (EDT).
        SwingUtilities.invokeLater(() -> {
            // Crea un JFrame para mostrar el árbol de la partida
            JFrame frame = new JFrame("Árbol de la Partida");
            // Configura la ventana para que solo se cierre ella misma (no toda la aplicación) cuando el usuario hace clic en la X.
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
           //Establece tamaño de la ventana 
            frame.setSize(800, 600);
            //Coloca la ventana en el centro de la pantalla
            frame.setLocationRelativeTo(null);
            
            ArbolPanel panel = new ArbolPanel(jugadas);
            //Añade un scroll a la pantalla
            frame.add(new JScrollPane(panel));
           //Hace la ventana viisible con todos sus componentes 
            frame.setVisible(true);
        });
    }
    // Utilizamos herencia con la clase JPanel para crear un panel 
    private static class ArbolPanel extends JPanel {
        private List<String> jugadas; //Almacena jugadas
        private int nodoDiametro = 30; // Deine el tamaño de los circulos
        private double escala = 1.0; // Controla el zoom del árbol para ajustarlo a la ventana 
        //Constructor que recibe la lista de jugadas y pone el fondo blanco
        public ArbolPanel(List<String> jugadas) {
            this.jugadas = jugadas;
            setBackground(Color.WHITE);
        }
        
        @Override
        public Dimension getPreferredSize() {
            //Usa logaritmo base 2 para determinar los niveles del árbol binario.
            int profundidad = (int) Math.ceil(Math.log(jugadas.size() + 1) / Math.log(2));
            //Calculo ancho necesario segun la cantidad de ndos 
            int ancho = (int) Math.pow(2, profundidad) * nodoDiametro * 2;
          //Calcula ael alto necesario segun la cantidad de nodos  
            int alto = profundidad * 100 + 50;
            return new Dimension(ancho, alto);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g); //Llama al metodo de la super clase 
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            if (jugadas.isEmpty()) return;
            
            // Calcular los niveles que tendrá el árbol

            int profundidad = (int) Math.ceil(Math.log(jugadas.size() + 1) / Math.log(2));
            
            // Calcular dimensiones estimadas del árbol
            int alturaEstimada = profundidad * 100;
            int anchoEstimado = (int) Math.pow(2, profundidad - 1) * nodoDiametro * 3;
            
            // Calcular escala para ajustar el árbol a la ventana visible
            escala = Math.min(
                //Calcula cuanto reducir el arbol para que quepa
                getWidth() / (double) anchoEstimado,
                getHeight() / (double) alturaEstimada
            );
            
            // Limitar la escala máxima
            escala = Math.min(escala, 1.0);
            
            // Aplicar la escala
            g2d.scale(escala, escala);
            
            // Dibujar el árbol
            dibujarArbol(g2d, 0, (int)(getWidth() / escala / 2), 50);
        }
        
        private int dibujarArbol(Graphics2D g2d, int indice, int x, int y) {
            //Termina si el índice es mayor o igual al tamaño de jugadas
            if (indice >= jugadas.size()) return indice;
            
            // Dibujar el nodo
                Color colorNodo;
                if (indice == 0) {
                    colorNodo = new Color(223, 230, 230);    // Azul acero para "Inicio"
                } else if (indice % 2 == 1) {               // Nodos impares (blancas)
                    colorNodo = new Color(255, 215, 0);     // Amarillo oro
                } else {                                    // Nodos pares (negras)
                    colorNodo = new Color(100, 149, 237);   // Azul claro
                }
            g2d.setColor(colorNodo);
            // Dibuja un círculo para el nodo
            // El nodo se dibuja en la posición (x, y) con un diámetro definido por nodoDiametro
            g2d.fillOval(x - nodoDiametro/2, y - nodoDiametro/2, nodoDiametro, nodoDiametro);
            // Dibuja el borde del nodo
            g2d.setColor(Color.BLACK);
            g2d.drawOval(x - nodoDiametro/2, y - nodoDiametro/2, nodoDiametro, nodoDiametro);
            
            // Dibujar texto
            String jugada = jugadas.get(indice);
            FontMetrics fm = g2d.getFontMetrics(); // Calcula la posicion para centrar 
            g2d.drawString(jugada, x - fm.stringWidth(jugada)/2, y + 5); //Dibuja el otro 5cm abajo
            
            // Calcular hijos
            int hijoIzqIdx = indice * 2 + 1;
            int hijoDerIdx = indice * 2 + 2;
            
            // Calcular desplazamiento y organizacion de los nodos 
            int nivel = (int) (Math.log(indice + 1) / Math.log(2));
            int nivelAltura = 100;
            int desp = (int) (getWidth() / escala / Math.pow(2, nivel + 2));
            desp = Math.max(desp, nodoDiametro * 2);
            
            // Dibujar hijo izquierdo
            if (hijoIzqIdx < jugadas.size()) {
                int hijoX = x - desp;
                int hijoY = y + nivelAltura;
                //Linea que conecta el nodo padre con el hijo izquierdo
                g2d.drawLine(x, y + nodoDiametro/2, hijoX, hijoY - nodoDiametro/2);
               //Dibuja el subarbo que sale del hijo
                hijoIzqIdx = dibujarArbol(g2d, hijoIzqIdx, hijoX, hijoY);
            }
            
            // Dibujar hijo derecho
            if (hijoDerIdx < jugadas.size()) {
                int hijoX = x + desp;
                int hijoY = y + nivelAltura;
                g2d.drawLine(x, y + nodoDiametro/2, hijoX, hijoY - nodoDiametro/2);
                hijoDerIdx = dibujarArbol(g2d, hijoDerIdx, hijoX, hijoY);
            }
            
            return Math.max(hijoIzqIdx, hijoDerIdx);
        }
    }
}

//NOTA: Algunas funciones de esta clase fueron construídas con ayuda de ChatGPT, pero fueron modificadas para adaptarse a la lógica del programa.