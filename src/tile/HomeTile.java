package tile;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;


public class HomeTile extends Tile{

	private static final long serialVersionUID = 1L;

	public int hpCount;
	
	public HomeTile(int x, int y) {
		super(x, y, 0);
		hpCount = 3;
		
	}
	
	public HomeTile() {
		
	}

	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(new Color(0, 0, 255));
		
		Rectangle2D rect = new Rectangle2D.Float(0, 10, 20, 10);
		g2d.fill(rect);
		
		Path2D.Double triangle = new Path2D.Double();
		triangle.moveTo(10, 0);
		triangle.lineTo(20, 10);
		triangle.lineTo(0, 10);
		g2d.fill(triangle);
	}
}
