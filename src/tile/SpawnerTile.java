package tile;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import util.GameManager;

public class SpawnerTile extends Tile{

	private static final long serialVersionUID = 1L;
	public SpawnerTile() {
		
	}

	public SpawnerTile(int x, int y) {
		super(x, y, 50);
	}

	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(new Color(255, 0, 0));
		g2d.setStroke(new BasicStroke(3));
		g2d.draw(new Line2D.Double(0, 0, 20, 20));
		g2d.draw(new Line2D.Double(20, 0, 0, 20));
	}

	public void spawnEnemy() {
		GameManager.getInstance().spawners.remove(GameManager.getInstance().MAP[this.x][this.y]);
		
		// di soal gak dispecify posisi enemy yang dispawn random atau sesuai posisi si spawner, 
		// jadi, kalo mau random baris 37-43 boleh di-uncomment
//		Random rand = new Random();
//		int x, y;
//		do {
//			x = rand.nextInt(GameManager.getInstance().TILE_COUNT_TALL);
//			y = rand.nextInt(GameManager.getInstance().TILE_COUNT_WIDE);
//		}while(!(GameManager.getInstance().MAP[x][y] instanceof FloorTile));
//		GameManager.getInstance().MAP[this.x][this.y] = new FloorTile(this.x, this.y);
		
		EnemyTile et = new EnemyTile(x, y);
		GameManager.getInstance().MAP[x][y] = et;
				
		
		GameManager.getInstance().enemyCount += 1;
		GameManager.getInstance().refresh();

		et.startMove();
	}

}
