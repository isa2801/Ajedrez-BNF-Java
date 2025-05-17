package analizador;

import modelo.Jugada;
import modelo.Partida;
import modelo.ResultadoAnalisis;
import modelo.Turno;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnalizadorGramatica {
    private static final Pattern PATRON_TURNO = Pattern.compile("(\\d+)\\.\\s*([A-Za-z0-9\\-O\\+#x=]+)(?:\\s+([A-Za-z0-9\\-O\\+#x=]+))?");
    
    public ResultadoAnalisis analizar(String textoPartida) {
        try {
            Partida partida = extraerPartida(textoPartida);
            if (partida.getTurnos().isEmpty()) {
                return new ResultadoAnalisis(false, "No se encontraron turnos válidos en la partida.");
            }
            mostrarArbolEnInterfaz(partida);
            return new ResultadoAnalisis(true, partida.getTurnos().size());
        } catch (Exception e) {
            return new ResultadoAnalisis(false, "Error: " + e.getMessage());
        }
    }
    
    private Partida extraerPartida(String textoPartida) {
        Partida partida = new Partida();
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
            partida.agregarTurno(turno);
        }
        return partida;
    }
    
    private void mostrarArbolEnInterfaz(Partida partida) {
        List<String> jugadas = new ArrayList<>();
        jugadas.add("Inicio");
        
        for (Turno turno : partida.getTurnos()) {
            jugadas.add(turno.getJugadaBlanca().getNotacion());
            if (turno.getJugadaNegra() != null) {
                jugadas.add(turno.getJugadaNegra().getNotacion());
            }
        }
        
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Árbol de la Partida");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            
            ArbolPanel panel = new ArbolPanel(jugadas);
            frame.add(new JScrollPane(panel));
            frame.setVisible(true);
        });
    }
    
    private static class ArbolPanel extends JPanel {
        private List<String> jugadas;
        private int nodoDiametro = 30;
        private double escala = 1.0;
        
        public ArbolPanel(List<String> jugadas) {
            this.jugadas = jugadas;
            setBackground(Color.WHITE);
        }
        
        @Override
        public Dimension getPreferredSize() {
            int profundidad = (int) Math.ceil(Math.log(jugadas.size() + 1) / Math.log(2));
            int ancho = (int) Math.pow(2, profundidad) * nodoDiametro * 2;
            int alto = profundidad * 100 + 50;
            return new Dimension(ancho, alto);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            if (jugadas.isEmpty()) return;
            
            // Calcular la profundidad del árbol
            int profundidad = (int) Math.ceil(Math.log(jugadas.size() + 1) / Math.log(2));
            
            // Calcular dimensiones estimadas del árbol
            int alturaEstimada = profundidad * 100;
            int anchoEstimado = (int) Math.pow(2, profundidad - 1) * nodoDiametro * 3;
            
            // Calcular escala para ajustar el árbol a la ventana visible
            escala = Math.min(
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
            if (indice >= jugadas.size()) return indice;
            
            // Dibujar el nodo
            Color colorNodo = indice == 0 ? new Color(135, 206, 250) : new Color(255, 215, 0);
            g2d.setColor(colorNodo);
            g2d.fillOval(x - nodoDiametro/2, y - nodoDiametro/2, nodoDiametro, nodoDiametro);
            g2d.setColor(Color.BLACK);
            g2d.drawOval(x - nodoDiametro/2, y - nodoDiametro/2, nodoDiametro, nodoDiametro);
            
            // Dibujar texto
            String jugada = jugadas.get(indice);
            FontMetrics fm = g2d.getFontMetrics();
            g2d.drawString(jugada, x - fm.stringWidth(jugada)/2, y + 5);
            
            // Calcular hijos
            int hijoIzqIdx = indice * 2 + 1;
            int hijoDerIdx = indice * 2 + 2;
            
            // Calcular desplazamiento
            int nivel = (int) (Math.log(indice + 1) / Math.log(2));
            int nivelAltura = 100;
            int desp = (int) (getWidth() / escala / Math.pow(2, nivel + 2));
            desp = Math.max(desp, nodoDiametro * 2);
            
            // Dibujar hijo izquierdo
            if (hijoIzqIdx < jugadas.size()) {
                int hijoX = x - desp;
                int hijoY = y + nivelAltura;
                g2d.drawLine(x, y + nodoDiametro/2, hijoX, hijoY - nodoDiametro/2);
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