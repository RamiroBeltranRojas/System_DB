package DB;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class basesDatosList extends JButton {
    private String nombreBaseDatos;

    public basesDatosList(String nombreBaseDatos) {
    	 super(nombreBaseDatos);
         this.nombreBaseDatos = nombreBaseDatos;
         setFocusPainted(false);
         setContentAreaFilled(false);
         setBorderPainted(false);
         setForeground(Color.WHITE);
         setHorizontalAlignment(SwingConstants.LEFT);
         setPreferredSize(new Dimension(180, 30)); // Personaliza las dimensiones según tu necesidad

         // Agregar MouseListener para detectar cuando el cursor está sobre el botón
         addMouseListener(new MouseAdapter() {
             @Override
             public void mouseEntered(MouseEvent e) {
            	 ;
            	 setContentAreaFilled(true);
                
                 setBackground(Color.decode("#527079")); // Cambiar color al pasar el cursor
                 setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Cambiar el cursor a mano
             }

             @Override
             public void mouseExited(MouseEvent e) {
            	 setContentAreaFilled(false);
                 setBackground(null); // Restaurar el color original al salir del botón
                 setCursor(Cursor.getDefaultCursor()); // Restaurar el cursor predeterminado
             }
         });
    }

    public String getNombreBaseDatos() {
        return nombreBaseDatos;
    }
}
