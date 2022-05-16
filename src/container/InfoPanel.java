package container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import tile.EnemyTile;
import tile.HomeTile;
import tile.SpawnerTile;
import tile.TowerTile;
import tile.WallTile;
import util.GameManager;

import java.awt.GridLayout;

public class InfoPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	public final int WIDTH = 300;
	public final int HEIGHT = GameManager.getInstance().HEIGHT;
	
	private JPanel tileInfoPanel, gameInfoPanel, buttonInfoPanel, titlePanel;

	public InfoPanel() {
		// TODO Auto-generated constructor stub
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(BorderFactory.createEmptyBorder(30, 10, 10, 10));
		
		refresh();
	}

	public void refresh() {
		this.removeAll();
		
		initTileInfo();
		initGameInfo();
		initButtonInfo();
		initTitlePanel();
		
		add(tileInfoPanel);
		add(gameInfoPanel);
		add(buttonInfoPanel);
		add(titlePanel);
		revalidate();
		repaint();
	}

	private void initTileInfo() {
		tileInfoPanel = new JPanel();
		tileInfoPanel.setLayout(new BoxLayout(tileInfoPanel, BoxLayout.Y_AXIS));
		JPanel panel1, panel2, panel3, panel4, panel5, panel6;
		
		panel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel1.add(new HomeTile());
		panel1.add(new JLabel("Home"));
		
		panel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel2.add(new EnemyTile(0));
		panel2.add(new JLabel("Enemy Base Color"));
		
		panel3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel3.add(new EnemyTile(100));
		panel3.add(new JLabel("Enemy in Full Health"));
		
		panel4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel4.add(new TowerTile());
		panel4.add(new JLabel("Tower"));
		
		panel5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel5.add(new SpawnerTile());
		panel5.add(new JLabel("Enemy Spawner"));
		
		panel6 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel6.add(new WallTile(-1, -1));
		panel6.add(new JLabel("Wall"));
		
		tileInfoPanel.add(panel1);
		tileInfoPanel.add(panel2);
		tileInfoPanel.add(panel3);
		tileInfoPanel.add(panel4);
		tileInfoPanel.add(panel5);
		tileInfoPanel.add(panel6);
	}
	
	private void initGameInfo() {
		gameInfoPanel = new JPanel();
		gameInfoPanel.setLayout(new BoxLayout(gameInfoPanel, BoxLayout.Y_AXIS));
		JPanel panel1, panel2, panel3, panel4;
		
		panel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel1.add(new JLabel("HP: "));
		panel1.add(new JLabel(GameManager.getInstance().HOME.hpCount + ""));
		
		panel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel2.add(new JLabel("Coin: "));
		panel2.add(new JLabel(GameManager.getInstance().coinCount + ""));
		
		panel3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel3.add(new JLabel("Enemy: "));
		panel3.add(new JLabel(GameManager.getInstance().enemyCount + ""));
		
		panel4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel4.add(new JLabel("Spawn: "));
		panel4.add(new JLabel(GameManager.getInstance().spawners.size() + ""));
		
		gameInfoPanel.add(panel1);
		gameInfoPanel.add(panel2);
		gameInfoPanel.add(panel3);
		gameInfoPanel.add(panel4);
	}
	
	private void initButtonInfo() {
		buttonInfoPanel = new JPanel(new GridLayout(2, 1));
		
		String command = (GameManager.getInstance().isPaused) ? "Play" : "Pause";
		JLabel info1 = new JLabel("Press P to " + command);
		JLabel info2 = new JLabel("Press Esc to Exit");
		
		info1.setFont(new Font("Tahoma", Font.PLAIN, 24));
		info2.setFont(new Font("Tahoma", Font.PLAIN, 24));
		
		buttonInfoPanel.add(info1);
		buttonInfoPanel.add(info2);
	}
	
	private void initTitlePanel() {
		titlePanel = new JPanel(new GridLayout(2,1));
		
		JLabel title = new JLabel("Tower Defense");
		title.setFont(new Font("Tahoma", Font.BOLD, 36));
		
		JLabel me = new JLabel("LC035 - Jevon Levin");
		me.setFont(new Font("Times New Roman", Font.ITALIC, 14));
		
		titlePanel.add(title);
		titlePanel.add(me);
	}
}
