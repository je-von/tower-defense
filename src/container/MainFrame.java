package container;
import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import util.GameManager;


public class MainFrame extends JFrame{

	private static final long serialVersionUID = 1L;
	
	public MainFrame() {
		GamePanel gp = GameManager.getInstance().GAME;
		InfoPanel ip = GameManager.getInstance().INFO;
		
		setLayout(new BorderLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
		setFocusable(true);
		setTitle("Tower Defense - LC035");
		
		add(gp, BorderLayout.WEST);
		add(ip, BorderLayout.EAST);
		
		pack();
		setLocationRelativeTo(null);
		
		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				super.keyReleased(e);
				if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					//exit
					System.exit(0);
				}else if(e.getKeyCode() == KeyEvent.VK_P) {
					//play or pause
					if(GameManager.getInstance().isPaused) {
						//play
						synchronized(GameManager.getInstance().PAUSER) {
							GameManager.getInstance().isPaused = false;
							GameManager.getInstance().PAUSER.notifyAll();
						}
					}else {
						//pause
						GameManager.getInstance().isPaused = true;
					}
					GameManager.getInstance().INFO.refresh();
				}
			}
		
		});
	}
	
	public void showEndWindow() {
		GameManager.getInstance().isRunning = false;
		
		boolean isWin = GameManager.getInstance().spawners.size() == 0 && GameManager.getInstance().HOME.hpCount > 0;
		
		// Di soal, waktu game over kalkulasinya minta tower lifes, aku asumsi tower lifes itu jumlah tower yang ada
		int score = GameManager.getInstance().towerLifeCount * 1000 + GameManager.getInstance().coinCount * 100;
		String title = "";
		int messageType;
		if(isWin) {
			title = "YOU WIN";
			messageType = JOptionPane.INFORMATION_MESSAGE;
		}else {
			title = "GAME OVER";
			messageType = JOptionPane.ERROR_MESSAGE;
		}
		String message = String.format("YOUR SCORE: %d\n%s\nPlay Again?", score, title);
		
		int option = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION, messageType);
		if(option == JOptionPane.YES_OPTION) {
			//play again
			GameManager.getInstance().restart();
		}else {
			System.exit(0);
		}
		
	}
	
}
