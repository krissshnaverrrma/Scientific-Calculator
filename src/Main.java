package src;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CalculatorUI ui = new CalculatorUI();
            ui.setIconImage(createCustomIcon());
        });
    }

    private static Image createCustomIcon() {
        int size = 64;
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setColor(new Color(30, 35, 40));
        g2.fillOval(4, 4, 56, 56);
        g2.setStroke(new BasicStroke(3));
        g2.setColor(new Color(200, 200, 200));
        g2.drawOval(4, 4, 56, 56);
        g2.setColor(Color.WHITE);
        Font font = new Font("SansSerif", Font.BOLD, 30);
        g2.setFont(font);
        String text = "√x";
        FontMetrics fm = g2.getFontMetrics();
        int x = (size - fm.stringWidth(text)) / 2;
        int y = ((size - fm.getHeight()) / 2) + fm.getAscent() - 2;
        g2.drawString(text, x, y);
        g2.dispose();
        return img;
    }
}