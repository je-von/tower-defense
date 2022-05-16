package tile;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.Vector;

import javax.swing.JPanel;
import util.GameManager;

public abstract class Tile extends JPanel{

	private static final long serialVersionUID = 1L;

	public static final int TILE_SIZE = 20;
	
	protected Color DEFAULT = new Color(255, 255, 255);
	protected Color AVAILABLE = new Color(0, 255, 0, 130);
	protected Color UNAVAILABLE = new Color(0, 0, 0, 130);
		
	public int x, y, weight;
	
	public Vector<TowerTile> towersInThisRange;
	
	
	public Tile(int x, int y, int weight) {
		this.x = x;
		this.y = y;
		this.weight = weight;
		init();
		initEventListener();
	}
	
	public Tile(int x, int y, int weight, Color DEFAULT) {
		this.DEFAULT = DEFAULT;
		this.x = x;
		this.y = y;
		this.weight = weight;
		init();
		initEventListener();
	}
	
	public Tile() {
		init();
	}

	public void setTileAsAttackRange(TowerTile tower){
		this.towersInThisRange.add(tower);
		this.weight = 27 * towersInThisRange.size(); // 27 itu weightnya tile attack range
		this.DEFAULT = new Color(255, 0, 255, 60 * towersInThisRange.size()); //The color will darken when there is an intersect with other Tower Attack Range.
		setBackground(DEFAULT);
	}
	
	public void setTileSteppedByEnemy(EnemyTile currEnemy) {
		for(int i = 0; i < towersInThisRange.size(); i++) {
			TowerTile t = towersInThisRange.get(i);
			if(!t.enemiesInRange.contains(currEnemy)) {
				t.enemiesInRange.add(currEnemy);
				t.startAttacking();
			}
		}
	}
	
	private void init() {
		this.towersInThisRange = new Vector<>();

		setBackground(DEFAULT);
		setPreferredSize(new Dimension(TILE_SIZE, TILE_SIZE));
		
	}

	private void initEventListener() {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mouseEntered(e);
				if(GameManager.getInstance().isPaused || !GameManager.getInstance().isRunning) return; 
				checkTile();
			}
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mouseExited(e);
				setBackground(DEFAULT);
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mouseClicked(e);
				changeTile();
			}
		});
	}
	
	private void changeTile() {
		if(!GameManager.getInstance().isPaused && GameManager.getInstance().coinCount > 0 && this instanceof FloorTile) {
			GameManager.getInstance().MAP[x][y] = new TowerTile(x, y);
			GameManager.getInstance().towerLifeCount += 1;
			GameManager.getInstance().coinCount -= 1;
			GameManager.getInstance().refresh();
		}
	}
	
	private void checkTile() {
		if(this instanceof FloorTile) {
			setBackground(AVAILABLE);
		}else {
			setBackground(UNAVAILABLE);
		}
	}
	
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		Rectangle2D.Float rect = new Rectangle2D.Float(0, 0, 20, 20);
		
		g2d.setColor(Color.BLACK);
		g2d.draw(rect);
	}

}
