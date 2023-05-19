import javax.swing.SwingUtilities;

public class SnakeGame{
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                GameGUI gameGUI = new GameGUI();
                gameGUI.setVisible(true);
            }
        });
    }
}
