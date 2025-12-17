package src;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class CalculatorUI extends JFrame {
    private JTextField display = new JTextField();
    private JLabel historyLabel = new JLabel(" ");
    private JPanel buttonPanel;
    private JSlider contrastSlider;
    private ArrayList<PillButton> buttons = new ArrayList<>();
    private boolean isDarkMode = true;
    private double firstNum = 0;
    private String operator = "";
    private boolean startNewInput = true;

    public CalculatorUI() {
        setTitle("Scienctific Calculating ENGINE");
        setSize(480, 950);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        this.setFocusable(true);
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() == 'k' || e.getKeyChar() == 'K') {
                    display.setText(String.valueOf(ScientificEngine.LIGHT_SPEED));
                    historyLabel.setText("Constant: Speed of Light (c)");
                } else {
                    handleKeyboard(e);
                }
            }
        });
        setupLCD();
        setupButtons();
        applyTheme();
        display.setText(String.valueOf(Math.PI));
        historyLabel.setText("SYSTEM READY | LOGGING ACTIVE");
        setVisible(true);
    }

    private void setupLCD() {
        JPanel screen = new JPanel(new GridLayout(2, 1));
        screen.setBorder(new EmptyBorder(15, 15, 15, 15));
        historyLabel.setFont(new Font("Monospaced", Font.PLAIN, 18));
        historyLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        display.setFont(new Font("Monospaced", Font.BOLD, 48));
        display.setEditable(false);
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setBorder(null);
        screen.add(historyLabel);
        screen.add(display);
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(screen, BorderLayout.CENTER);
        contrastSlider = new JSlider(0, 255, 185);
        contrastSlider.addChangeListener(e -> applyTheme());
        northPanel.add(contrastSlider, BorderLayout.SOUTH);
        add(northPanel, BorderLayout.NORTH);
    }

    private void setupButtons() {
        buttonPanel = new JPanel(new GridLayout(10, 4, 12, 12));
        buttonPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        String[] labels = {
                "MODE", "CONV", "UTIL", "AC",
                "sin", "cos", "tan", "log",
                "√", "x²", "x^y", "π",
                "7", "8", "9", "DEL",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "0", ".", "=", "+"
        };
        for (String l : labels) {
            PillButton b = new PillButton(l);
            b.addActionListener(e -> handlePress(l, b));
            b.setFocusable(false);
            buttons.add(b);
            buttonPanel.add(b);
        }
        add(buttonPanel, BorderLayout.CENTER);
    }

    private void handleKeyboard(KeyEvent e) {
        char k = e.getKeyChar();
        int c = e.getKeyCode();
        if (Character.isDigit(k) || k == '.')
            handlePress(String.valueOf(k), null);
        else if ("+-*/".indexOf(k) != -1)
            handlePress(String.valueOf(k), null);
        else if (c == KeyEvent.VK_ENTER)
            handlePress("=", null);
        else if (c == KeyEvent.VK_BACK_SPACE)
            handlePress("DEL", null);
        else if (c == KeyEvent.VK_ESCAPE)
            handlePress("AC", null);
    }

    private void handlePress(String cmd, PillButton b) {
        SoundEngine.playBeep();
        if (b != null)
            animateGlow(b);
        if (cmd.equals("UTIL"))
            showUtility();
        else if (cmd.equals("CONV"))
            showConverter();
        else if (cmd.equals("AC")) {
            display.setText("0");
            historyLabel.setText(" ");
            startNewInput = true;
        } else if (cmd.equals("MODE")) {
            isDarkMode = !isDarkMode;
            applyTheme();
        } else if (cmd.equals("DEL")) {
            String s = display.getText();
            display.setText(s.length() > 1 ? s.substring(0, s.length() - 1) : "0");
        } else {
            processMath(cmd);
        }
    }

    private void processMath(String cmd) {
        if (cmd.matches("[0-9.]")) {
            if (startNewInput || display.getText().equals("0")) {
                display.setText(cmd);
                startNewInput = false;
            } else {
                display.setText(display.getText() + cmd);
            }
        } else if ("+-*/x^y".contains(cmd)) {
            firstNum = Double.parseDouble(display.getText());
            operator = cmd;
            historyLabel.setText(firstNum + " " + operator);
            startNewInput = true;
        } else if (cmd.equals("=")) {
            double second = Double.parseDouble(display.getText());
            double res = ScientificEngine.evaluate(firstNum, second, operator);
            updateScreen(firstNum + " " + operator + " " + second + " =", String.valueOf(res));
        } else {
            double v = Double.parseDouble(display.getText());
            updateScreen(cmd + "(" + v + ")", String.valueOf(ScientificEngine.evaluateSci(v, cmd)));
        }
    }

    private void showUtility() {
        String[] opts = { "Force (m*a)", "Quadratic (ax²+bx+c)", "Finance (SI)" };
        String s = (String) JOptionPane.showInputDialog(this, "Science Tools", "Select", 1, null, opts, opts[0]);
        if (s == null)
            return;
        try {
            if (s.contains("Quadratic")) {
                double a = Double.parseDouble(JOptionPane.showInputDialog("Enter a:"));
                double b = Double.parseDouble(JOptionPane.showInputDialog("Enter b:"));
                double c = Double.parseDouble(JOptionPane.showInputDialog("Enter c:"));
                String res = ScientificEngine.solveQuadratic(a, b, c);
                updateScreen("Solving " + a + "x²+" + b + "x+" + c, res);
            } else if (s.contains("Force")) {
                double m = Double.parseDouble(JOptionPane.showInputDialog("Mass (kg):"));
                double a = Double.parseDouble(JOptionPane.showInputDialog("Accel (m/s²):"));
                updateScreen("F = m * a", String.valueOf(ScientificEngine.force(m, a)));
            }
        } catch (Exception e) {
            display.setText("Input Error");
        }
    }

    private void showConverter() {
        String[] opts = { "USD to INR", "C to F", "KM to Miles" };
        String s = (String) JOptionPane.showInputDialog(this, "Units Converter", "Select", 1, null, opts, opts[0]);
        if (s == null)
            return;
        double v = Double.parseDouble(display.getText());
        updateScreen(s, String.valueOf(ConverterEngine.convert(v, s)));
    }

    private void updateScreen(String hist, String res) {
        historyLabel.setText(hist);
        display.setText(res);
        logToFile(hist + " -> " + res);
        startNewInput = true;
    }

    private void logToFile(String entry) {
        try (FileWriter fw = new FileWriter("calc_history.log", true);
                PrintWriter out = new PrintWriter(fw)) {
            out.println(java.time.LocalDateTime.now() + " -> " + entry);
        } catch (Exception e) {
            System.err.println("Logging failed.");
        }
    }

    private void applyTheme() {
        int v = contrastSlider.getValue();
        Color bg = isDarkMode ? new Color(15, 15, 15) : Color.WHITE;
        Color bBg = isDarkMode ? new Color(45, 45, 45) : new Color(225, 225, 225);
        Color bFg = isDarkMode ? Color.WHITE : Color.BLACK;
        Color lcd = isDarkMode ? new Color(v - 20, v, v - 20) : new Color(v, v, v);
        getContentPane().setBackground(bg);
        buttonPanel.setBackground(bg);
        display.setBackground(lcd);
        historyLabel.setOpaque(true);
        historyLabel.setBackground(lcd);
        for (PillButton b : buttons) {
            b.setBackground(bBg);
            b.setForeground(bFg);
            if (b.getText().equals("UTIL") || b.getText().equals("CONV")) {
                b.setForeground(new Color(212, 175, 55)); // Gold Elite text
            }
        }
    }

    private void animateGlow(PillButton b) {
        Color o = b.getBackground();
        b.setBackground(Color.GRAY);
        new Timer(80, e -> b.setBackground(o)).start();
    }

    class PillButton extends JButton {
        public PillButton(String l) {
            super(l);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setFont(new Font("Arial", Font.BOLD, 14));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 35, 35));
            super.paintComponent(g2);
            g2.dispose();
        }
    }
}