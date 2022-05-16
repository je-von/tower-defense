package util;

import java.awt.Shape;
import java.util.Random;
import java.util.Vector;

import container.GamePanel;
import container.InfoPanel;
import container.MainFrame;
import tile.FloorTile;
import tile.HomeTile;
import tile.SpawnerTile;
import tile.Tile;
import tile.WallTile;

public class GameManager {
	public boolean isRunning;
	public boolean isPaused;
	public final int TILE_COUNT_WIDE = 40;
	public final int TILE_COUNT_TALL = 30;
	public final int WIDTH = (TILE_COUNT_WIDE) * Tile.TILE_SIZE;
	public final int HEIGHT = (TILE_COUNT_TALL) * Tile.TILE_SIZE;
	public Tile[][] MAP = new Tile[TILE_COUNT_TALL][TILE_COUNT_WIDE];
	public Vector<SpawnerTile> spawners;
	public int coinCount;
	public int towerLifeCount;
	public int enemyCount;
	public Vector<Shape> attackLines;
	public int spawnerInterval;
	
	public final Object PAUSER = new Object(); // buat jadi lock di thread (wait & notify)
	
	private static GameManager instance = null;
	
	public GamePanel GAME;
	public InfoPanel INFO;
	public HomeTile HOME;
	public MainFrame FRAME;
	
	synchronized public static GameManager getInstance() {
		if(instance == null) {
			instance = new GameManager();
			instance.init();
		}

		return instance;
	}
	private GameManager() {
		isRunning = true;
		isPaused = false;
		spawners = new Vector<>();
		attackLines = new Vector<>();
		coinCount = 3;
		towerLifeCount = 0;
		enemyCount = 0;
		spawnerInterval = 3000;
		
		HOME = new HomeTile(25, 20);
	}
	private void init() {
		//initialize map & tiles
		for (int i = 0; i < TILE_COUNT_TALL; i++) {
			for (int j = 0; j < TILE_COUNT_WIDE; j++) {
				Tile tile;
				if (i == 0 || j == 0 || i == TILE_COUNT_TALL - 1 || j == TILE_COUNT_WIDE - 1)
					tile = new WallTile(i, j);
				else if (i == 1 || j == 1 || j == TILE_COUNT_WIDE - 2) {
					tile = new SpawnerTile(i, j);
					spawners.add((SpawnerTile)tile);
				}
				else if (i == 25 && j == 20) {
					tile = HOME;
					HOME.hpCount = 3;
				}
				else
					tile = new FloorTile(i, j);

				MAP[i][j] = tile;
			}
		}
		
		GAME = new GamePanel();
		INFO = new InfoPanel();
		
		FRAME = new MainFrame();
		
		runSpawnThread();
	}
	
	public void restart() {
		FRAME.dispose();
		GAME = null;
		INFO = null;
		HOME = null;
		FRAME = null;
		
		instance = new GameManager();
		instance.init();
	}
	
	public void refresh() {
		GAME.refresh();
		INFO.refresh();
	}
	
	private void runSpawnThread() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				startSpawn();
			}

		});
		t.start();
	}
	private void startSpawn() {
		Random rand = new Random();
		while(isRunning && spawners.size() > 0) {

			try {
				Thread.sleep(spawnerInterval);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// ditaro sini supaya setelah thread.sleep, kalo uda dipause atau gameover, gak lanjut
			if(isPaused) continue;
			if(!isRunning) break;
			
			SpawnerTile spawner = null;
			
			/*
			 * Randomize spawner yang akan nge-spawn
			 * Sesuai soal: "After certain interval of time, one of the remaining Spawners will spawn an Enemy." 
			 * */
			do {
				int index = rand.nextInt(spawners.size());
				spawner = spawners.get(index);
			}while(spawner == null);
			spawner.spawnEnemy();
			if(spawnerInterval > 100) 
				spawnerInterval -= 100;
		}
	}
}