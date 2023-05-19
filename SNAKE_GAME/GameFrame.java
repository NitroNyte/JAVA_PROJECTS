import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class GameFrame extends JFrame   {
    
	GamePanel game;
	GameFrame() {
		game = new GamePanel();
		this.add(game);
		this.setTitle("Snake Game");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.pack();		
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		
	}
}