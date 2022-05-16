package tile;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Vector;
import util.GameManager;

public class EnemyTile extends Tile implements Runnable {

	private static final long serialVersionUID = 1L;
	private Color HP_COLOR = new Color(255, 0, 0);
	private Color BASE_COLOR = new Color(165, 42, 42);

	public int hpCount;
	public EnemyTile(int x, int y) {
		super(x, y, 600);
		this.hpCount = 100;
	}

	public EnemyTile(int hpCount) {
		this.hpCount = hpCount;
	}
	
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;

		Ellipse2D.Double el = new Ellipse2D.Double(0, 0, 20, 20);

		g2d.setColor(BASE_COLOR);
		g2d.fill(el);

		Arc2D.Double arc = new Arc2D.Double(0, 0, 20, 20, 0, ((double) hpCount / 100.0) * 360.0, Arc2D.PIE);

		g2d.setColor(HP_COLOR);
		g2d.fill(arc);
	}

	@Override
	public void run() {
		moveEnemy();
	}

	private void moveEnemy() {
		Tile prevTile = new FloorTile(x, y);
		Vector<Tile> path = this.dijkstra();
		int currStep = (path.size() <= 2) ? 0 : 1;
		while (GameManager.getInstance().isRunning) {
//			System.out.printf("HP(%d, %d) -> %d\n", this.x, this.y, hpCount);
			
			try {
				Thread.sleep(500);
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

			GameManager.getInstance().MAP[this.x][this.y] = prevTile;

			if (this.hpCount <= 0) {
				// enemynya mati
				GameManager.getInstance().coinCount += 1;
				deleteEnemy();
				return;
			}

			Tile nextTile = path.get(currStep);
			currStep += 1;
			
			if (nextTile instanceof HomeTile) {
				// enemy udah sampe home, hp home decrease, enemy ilang
				GameManager.getInstance().HOME.hpCount -= 1;
				if (GameManager.getInstance().HOME.hpCount <= 0) {
					GameManager.getInstance().FRAME.showEndWindow();
				}
				deleteEnemy();
				return;
			}else if(!nextTile.towersInThisRange.isEmpty()) {
				nextTile.setTileSteppedByEnemy(this);
			}
			
			this.x = nextTile.x;
			this.y = nextTile.y;
			
			Color bgColor = nextTile.getBackground();
			this.setBackground(bgColor);

			GameManager.getInstance().MAP[this.x][this.y] = this;
			GameManager.getInstance().GAME.refresh();

			prevTile = nextTile;
			
			if(path.size() == 2){ //restart dijkstra
				path = this.dijkstra();
				currStep = 0;
			}
		}
	}

	private void deleteEnemy() {
//		this.x = -1;
//		this.y = -1;
		this.hpCount = -1;
		GameManager.getInstance().enemyCount -= 1;
		GameManager.getInstance().refresh();
	}

	public void startMove() {
		Thread t = new Thread(this);
		t.start();
	}

	/*
	 * Method dijkstra, buat cari shortest path, returnnya Pathnya,
	 * yang didapet setelah dibacktrace sama method getPath()
	 * */
	public Vector<Tile> dijkstra() {
		Hashtable<Tile, Boolean> visited = new Hashtable<>();
		Hashtable<Tile, Integer> totalCost = new Hashtable<>();
		Hashtable<Tile, Tile> parents = new Hashtable<>(); // buat simpen path nya 

		Tile destination = null;

		for (int i = 0; i < GameManager.getInstance().TILE_COUNT_TALL; i++) {
			for (int j = 0; j < GameManager.getInstance().TILE_COUNT_WIDE; j++) {
				Tile tile = GameManager.getInstance().MAP[i][j];
				visited.put(tile, false);
//				totalCost.put(tile, Integer.MAX_VALUE);
				totalCost.put(tile, 99999);
				
				if (tile instanceof HomeTile)
					destination = tile;
			}
		}
//		System.out.println("Home: " + destination.x + ", " + destination.y);

		totalCost.replace(this, 0);
		Tile currTile = this;
		Tile backupTile = currTile;
		while (true) {
			visited.replace(currTile, true);
//			int minNext = Integer.MAX_VALUE;
			int minNext = 99999;
			
			Tile nextTile = null;

			/*
			 * NOTES:
			 * yg adjacent sama enemyTile skrg kan pasti ke-8 tile yg ada di sekitar dia,
			 * makanya pake nested loop gini, bukan loop dari 0 sampe sejumlah vertexnya
			 * 
			 * Contoh: enemy -> O, tile lain -> X
			 * XXX
			 * XOX
			 * XXX
			 * Jadi yang adjacent cuma ke-8 X yg ada dideket dia aja, terus weightnya sesuai
			 * sama weight yang uda di-assign ke masing2 tile pas awal di create
			 */
			for (int i = -1; i <= 1; i++) {
				for (int j = -1; j <= 1; j++) {
					if (i == 0 && j == 0)
						continue;

					boolean isReachable = (currTile.x + i > 0
							&& currTile.x + i < GameManager.getInstance().TILE_COUNT_TALL)
							&& (currTile.y + j > 0 && currTile.y + j < GameManager.getInstance().TILE_COUNT_WIDE);
							
					if (!isReachable) {
						continue;
					}

					Tile temp = GameManager.getInstance().MAP[currTile.x + i][currTile.y + j];

					if (visited.get(temp) != null && !visited.get(temp) && totalCost.get(temp) <= minNext && (temp instanceof FloorTile || temp instanceof HomeTile)) {
						minNext = totalCost.get(temp);
						nextTile = temp;
						if(currTile == this)
							backupTile = temp;
					}
				}
			}
			
			for (int i = -1; i <= 1; i++) {
				for (int j = -1; j <= 1; j++) {
					if (i == 0 && j == 0)
						continue;

					boolean isReachable = (currTile.x + i > 0
							&& currTile.x + i < GameManager.getInstance().TILE_COUNT_TALL)
							&& (currTile.y + j > 0 && currTile.y + j < GameManager.getInstance().TILE_COUNT_WIDE);

					if (!isReachable) {
						continue;
					}
					Tile temp = GameManager.getInstance().MAP[currTile.x + i][currTile.y + j];
					int cost = temp.weight + totalCost.get(currTile);

					if (totalCost.get(temp) != null && cost < totalCost.get(temp)) {
						totalCost.replace(temp, cost);
						if(nextTile != null) parents.put(temp, nextTile);
					}
				}
			}
			if (visited.get(destination)) {
				break;
			}
			
			if(nextTile == null) {
				nextTile = parents.get(currTile);
				
				//untuk mencegah deadend.
				if(nextTile == null) { // jadi kalo uda ga ketemu path lagi, disini mundur selangkah
					nextTile = backupTile; 
				}else if(nextTile == currTile) {
					parents.put(destination, backupTile);
					break;
				}
			}
			currTile = nextTile;
			
		}
		

		Vector<Tile> path = getPath(destination, parents);
//		System.out.println(this.x + "," + this.y);
//		for (Tile t : path) {
//			System.out.printf("(%d, %d) ->", t.x, t.y);
//		}
//		System.out.println();

		return path;
	}

	private Vector<Tile> getPath(Tile destination, Hashtable<Tile, Tile> parents) {
		Vector<Tile> path = new Vector<>();

		Tile curr = destination, parent = null;
		while (curr != this && curr != null) {
			parent = parents.get(curr);
			
			path.add(curr);
			if (curr == parent)
				break;

			curr = parent;
		}

		Collections.reverse(path); //direverse supaya urutannya bener
		return path;
	}
	
	public boolean isEnemyInRangeOfTower(TowerTile tower) {
//		for (Tile tile : tower.attackRanges) {
//			if(tile.x == this.x && tile.y == this.y) {
//				return true;
//			}
//		}
		for(int i = 0; i < tower.attackRanges.size(); i++) {
			Tile tile = tower.attackRanges.get(i);
			if(tile.x == this.x && tile.y == this.y) {
				return true;
			}
		}
		
		return false;
	}

}
