import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Invoca el código de la interfaz gráfica en el hilo de eventos de Swing
        SwingUtilities.invokeLater(() -> {
            // Crea una instancia de la clase Form (ventana principal)
            Form form = new Form();
            
            // Hace visible la ventana
            form.setVisible(true);
        });
    }
}

