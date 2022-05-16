package tile;

import java.awt.Color;


public class WallTile extends Tile{

	private static final long serialVersionUID = 1L;
	protected Color DEFAULT = new Color(255, 255, 255);
	
	public WallTile(int x, int y) {
		super(x, y, 99999, new Color(0, 0, 0));
	}
}
