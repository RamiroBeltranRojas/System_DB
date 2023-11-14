package DB;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.plaf.ColorUIResource;

public class CustomComboBoxUI extends BasicComboBoxUI {

    @Override
    protected JButton createArrowButton() {
        JButton button = super.createArrowButton();
        button.setContentAreaFilled(false); // Quita el fondo
        button.setBorderPainted(false); // Quita el borde
        button.setFocusPainted(false); // Quita el borde al obtener el foco
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(200, 200, 200)); // Cambia el color al pasar el cursor
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(UIManager.getColor("ComboBox.background")); // Restaura el color al salir del cursor
            }
        });
        return button;
    }

    @Override
    protected ComboPopup createPopup() {
        BasicComboPopup popup = (BasicComboPopup) super.createPopup();
        popup.setBorder(BorderFactory.createLineBorder(Color.GRAY)); // Cambia el borde de la lista desplegable
        return popup;
    }

    @Override
    public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
        if (hasFocus) {
            g.setColor(UIManager.getColor("ComboBox.selectionBackground")); // Cambia el fondo cuando tiene el foco
        } else {
            g.setColor(UIManager.getColor("ComboBox.background"));
        }
        g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
    }

   
}

