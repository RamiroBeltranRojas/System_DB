package DB;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BarraMenu extends JPanel {
    private JComboBox<String> menuDesplegable1;
    private JComboBox<String> menuDesplegable2;
    private JComboBox<String> menuDesplegable3;
    private static contInfo panelDerecho; // Agregamos una referencia al PanelDerecho

    public BarraMenu(contInfo panelDerecho) {
        this.panelDerecho = panelDerecho; // Asignamos la referencia
        setPreferredSize(new Dimension(800, 40)); // Personaliza las dimensiones según tu necesidad
        setBackground(Color.decode("#E5E5E5")); // Establecer el color de fondo a blanco
        
        // Configurar un layout con alineación a la izquierda
        setLayout(new FlowLayout(FlowLayout.LEFT));

        // Crear ComboBoxes
        String[] opciones = {"Conectar", "Crear base de datos"};
        menuDesplegable1 = new JComboBox<>(opciones);
        menuDesplegable2 = new JComboBox<>(opciones);
        menuDesplegable3 = new JComboBox<>(opciones);
        menuDesplegable1.setUI(new CustomComboBoxUI());
        menuDesplegable2.setUI(new CustomComboBoxUI());
        menuDesplegable3.setUI(new CustomComboBoxUI());
        // Agregar ComboBoxes al panel
        add(menuDesplegable1);
        add(menuDesplegable2);
        add(menuDesplegable3);
        menuDesplegable1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String seleccion = (String) menuDesplegable1.getSelectedItem();
                
                if (seleccion.equals("Conectar")) {
                    // Lógica para mostrar el panel de conexión en el PanelDerecho
                    mostrarPanelConexion();
                } else if (seleccion.equals("Crear base de datos")) {
                    // Lógica para crear una nueva base de datos
                }
                System.out.print(seleccion);
            }
        });
    }

    private void mostrarPanelConexion() {
        // Crea y muestra el panel de conexión en el PanelDerecho
        PanelConexion panelConexion = new PanelConexion();
        panelDerecho.removeAll(); // Limpia el contenido actual
        panelDerecho.add(panelConexion); // Agrega el nuevo panel
        panelDerecho.revalidate();
        panelDerecho.repaint();
        System.out.println(panelDerecho);
    }

    public class PanelConexion extends JPanel {
        public PanelConexion() {
            setPreferredSize(new Dimension(600, 600));
            setBackground(Color.decode("#FBFBFB"));

            // Agrega componentes visuales aquí
            add(new JLabel("Etiqueta de prueba"));
            add(new JTextField(10));
            add(new JButton("Botón de prueba"));
        }
    }

}

