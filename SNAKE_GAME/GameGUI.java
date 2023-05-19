import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameGUI extends JFrame {
    private Image background;
    private static final int SCRN_HEIGHT = 600;
    private static final int SCRN_WIDTH = 600;

    public GameGUI() {
        setTitle("SnakeGame");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton startButton = new JButton("Start Game");
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new GameFrame();
            }
        });

        JPanel panel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
                g.setColor(new Color(217,237,236));
                g.setFont(new Font("Sans Serif", Font.BOLD, 50));
                g.drawString("SnakeGame with Java",45, SCRN_WIDTH/2);
            }
        };

        panel.setPreferredSize(new Dimension(SCRN_HEIGHT, SCRN_WIDTH)); // Set the panel size to 600x600
        panel.setOpaque(true); // Make the panel transparent
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(200, 0, 0, 0); // Adjust top margin for vertical centering
        panel.add(startButton, constraints);

        setLayout(new BorderLayout()); // Use BorderLayout for centering the panel
        add(panel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null); // Center the window on the screen

        background = new ImageIcon("SnekGem.jpeg").getImage();
        background = background.getScaledInstance(SCRN_WIDTH, SCRN_HEIGHT, Image.SCALE_DEFAULT);

        setVisible(true);
    }
}
