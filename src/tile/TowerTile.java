package tile;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.util.Vector;

import util.GameManager;

public class TowerTile extends Tile implements Runnable{

	private static final long serialVersionUID = 1L;

	public Vector<Tile> attackRanges;
	
	public Vector<EnemyTile> enemiesInRange;

	private boolean isAttacking;
	
	public TowerTile(int x, int y) {
		super(x, y, 500, new Color(255, 0, 255, 60));
		enemiesInRange = new Vector<>();
		isAttacking = false;
		initAttackRange();
	}

	private void initAttackRange() {
		attackRanges = new Vector<>();
		
		/*
		 * buat bikin range tower bentuk diamond
		 * Di soal minta Tower Attack Rangenya ada 32, 
		 * tapi di screenshot yang ada di soal gak segitu (yang bentuk diamond) 
		 * jadi aku samain kayak contoh screenshot di soal aja.
		 * */
		int rows = 5;		
		for (int i = 0; i <= rows; i++) {
			int limit = rows - i + 1;
			for (int j = limit-1; j <= limit+ (2 * i - 1); j++) {
				setTileAsRange(rows, i, j);
			}
		}
		
		for (int i = rows+1, k = rows - 1; k >= 0; i++, k--) {
			int limit = rows - k + 1;
			for (int j = limit-1; j <= limit + (2 * k - 1); j++) {
				setTileAsRange(rows, i, j);
			}
		}
	}

	private void setTileAsRange(int rows, int i, int j) {
		GameManager game = GameManager.getInstance();
		boolean isReachable = (this.x - rows + i > 0 && this.x - rows + i < game.TILE_COUNT_TALL)
							&& (this.y - rows + j > 0 && this.y - rows + j < game.TILE_COUNT_WIDE)
							&& !(game.MAP[x - rows + i][y - rows + j] instanceof WallTile);
		if (!isReachable || (i - rows == 0 && j - rows == 0))
			return;

		Tile range = game.MAP[x - rows + i][y - rows + j];
		range.setTileAsAttackRange(this);		
		if(range instanceof FloorTile) {
			attackRanges.add(range);
		}
	}
	
	
	
	public TowerTile() {
		
	}
	
	synchronized public void startAttacking() {
		if(isAttacking) return;
		isAttacking = true;
		Thread t = new Thread(this);
		t.start();
	}
	
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(new Color(0, 255, 0));
		
		Path2D.Double triangle = new Path2D.Double();
		triangle.moveTo(10, 0);
		triangle.lineTo(20, 20);
		triangle.lineTo(0, 20);
		g2d.fill(triangle);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		EnemyTile target = enemiesInRange.get(0);
		
		while (GameManager.getInstance().isRunning) {
			try {
				/*
				 * Di soal minta Tower nyerang enemy itu tiap 1000/60 seconds, berkurang 1 hp. 
				 * Tapi jadinya bakal lama banget (enemynya gak bakal mati2), 
				 * jadi kubikin nyerangnya tiap 1000/60 milliseconds.
				 * */
				Thread.sleep(1000 / 60);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// ditaro sini supaya setelah thread.sleep, kalo uda dipause atau gameover, gak lanjut
			synchronized(GameManager.getInstance().PAUSER) {
				if(!GameManager.getInstance().isRunning)
					break;
				if(GameManager.getInstance().isPaused) {
					try {
						synchronized(GameManager.getInstance().PAUSER) {
							GameManager.getInstance().PAUSER.wait();
						}
					} catch (InterruptedException e) {
						break;
					}
					if(!GameManager.getInstance().isRunning)
						break;
				}
			}
			
			target.hpCount -= 1;
			
			// bikin attack line dari tower ke enemynya (nanti dioper ke GamePanel)
			Path2D.Double line = new Path2D.Double();
			line.moveTo(this.y * 20 + 10, this.x * 20 + 10);
			line.lineTo(target.y * 20 + 10, target.x * 20 + 10);
			
			if(target.hpCount <= 0 || !target.isEnemyInRangeOfTower(this)) {
				enemiesInRange.remove(target);
				isAttacking = false;
				break;
			}
			
			GameManager.getInstance().attackLines.add(line);
		}
		
		
	}
}
