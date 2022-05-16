// LC035 - JEVON LEVIN
// Technical Test 21-1
// "Tower Defense"
package main;

import java.awt.Font;
import javax.swing.UIManager;
import util.GameManager;

public class Main {

	public Main() {
		UIManager.put("Label.font", new Font("Tahoma", Font.PLAIN, 14)); //Ganti semua font defaultnya
		GameManager.getInstance();
	}

	public static void main(String[] args) {
		new Main();
	}

}
