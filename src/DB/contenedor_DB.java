package DB;


import javax.swing.*;
import java.awt.*;

public class contenedor_DB extends JPanel {
    public contenedor_DB() {
        // Configurando el panel izquierdo
        setPreferredSize(new Dimension(200, 600)); // Personaliza las dimensiones según tu necesidad
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        super.paintComponent(grphcs);

        Graphics2D g2 = (Graphics2D) grphcs;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint g = new GradientPaint(0, 0, Color.decode("#1C88C5"), 0, getHeight(), Color.decode("#2C3E50"));
        g2.setPaint(g);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 0, 0);
        g2.fillRect(getWidth() - 20, 0, getWidth(), getHeight());
    }
}
