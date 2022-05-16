package container;
import java.awt.*;

import javax.swing.JPanel;

import util.GameManager;

public class GamePanel extends JPanel{

	private static final long serialVersionUID = 1L;
	
	public GamePanel() {
		setFocusable(true);
		setPreferredSize(new Dimension(GameManager.getInstance().WIDTH, GameManager.getInstance().HEIGHT));

		setLayout(new GridLayout(GameManager.getInstance().TILE_COUNT_TALL, GameManager.getInstance().TILE_COUNT_WIDE));
		
		refresh();
		
	}
	
	public void refresh() {
		this.removeAll();
		for (int i = 0; i < GameManager.getInstance().TILE_COUNT_TALL; i++) {
			for(int j = 0; j < GameManager.getInstance().TILE_COUNT_WIDE; j++) {
				this.add(GameManager.getInstance().MAP[i][j]);
			}
		}
		revalidate();
		repaint();
	}
	
	

	@Override
	public void paint(Graphics g) {
		try {
			super.paint(g);
			Graphics2D g2d = (Graphics2D) g;
			g2d.setColor(new Color(0, 0, 255)); // Di soal warna garis tower waktu attack diminta green, tapi screenshotnya warna biru, jadi aku ikutin screenshot.
			g2d.setStroke(new BasicStroke(5));
		
		// Gambar semua garis attack dari tower ke enemy yang ada di vector attackLines
		
			for(int i = 0; i < GameManager.getInstance().attackLines.size(); i++) {
				Shape line = GameManager.getInstance().attackLines.get(i);
				g2d.draw(line);
			}
		} catch (Exception e) {
		}
		GameManager.getInstance().attackLines.removeAllElements();
		GameManager.getInstance().attackLines.clear();

	}
}
