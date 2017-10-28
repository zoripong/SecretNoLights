import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.ImageIcon;

import gui.SNL;
import model.Area;
import model.Location;
import model.Monster;

public class Test {

	private int mStage;
	private int mMapInfo[][];
	private int mBlock[][];
	private ImageIcon doorImageIcon;
	private ImageIcon blockImageIcon;
	private int doorX;
	private int doorY;

	ArrayList<Monster> monsters;
	private ArrayList<Location> monsterLocations;

	private HashMap<Location, Area> blockMap;

	public Test(int stage) {
		mStage = stage;
		blockMap = new HashMap<>();
		blockImageIcon = new ImageIcon(SNL.class.getResource("/images/block.png"));

		doorImageIcon = new ImageIcon(SNL.class.getResource("/images/door_close.png"));
		readFile();
		monsterLocations = new ArrayList<>();
	}

	private void readFile() {

		String fileName = "./src/map/stage_" + mStage + ".txt";
		String line = "";
		StringBuffer buff = new StringBuffer();

		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(fileName)));

			while ((line = br.readLine()) != null) {
				buff.append(line);
			}
			// System.out.println(buff);
			br.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		char[] mapData = buff.toString().toCharArray();
		mMapInfo = new int[18][20];

		int cnt = 0;
		for (int i = 0; i < mMapInfo.length; i++) {
			for (int j = 0; j < mMapInfo[0].length; j++) {
				switch (mapData[cnt++]) {
				case 51:
					mMapInfo[i][j] = 3;
					break;
				case 50:
					mMapInfo[i][j] = 2;
					break;
				case 49:
					mMapInfo[i][j] = 1;
					break;
				case 48:
					mMapInfo[i][j] = 0;
					break;
				}

			}
		}
	}

	// 문과 몬스터의 좌표만 읽어들임
	public void setStage() {

		int x = 0, y = 0;

		monsterLocations.clear();

		for (int i = 0; i < mMapInfo.length; i++) {
			for (int j = 0; j < mMapInfo[0].length; j++) {

				if (mMapInfo[i][j] == 2) {
					doorX = x + 5;
					doorY = y - doorImageIcon.getIconHeight() + 45;
				} else if (mMapInfo[i][j] == 3) {
					// monster
					monsterLocations.add(new Location(x, y));
					// System.out.println(monsterLocations.size());
				}

				x += blockImageIcon.getIconWidth();

			}
			x = 0;
			y += blockImageIcon.getIconHeight();
		}

	}

	public void test() {
		// TODO : ADD BLOCK MAP
		boolean isBlock = false;

		int startX = -1, startY = -1;
		int endX = -1, endY = -1;

		for (int i = 0; i < mMapInfo.length; i++) {
			for (int j = 0; j < mMapInfo[0].length; j++) {
				System.out.print(mMapInfo[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();
		System.out.println();

		for (int i = 0; i < mMapInfo.length; i++) {
			for (int j = 0; j < mMapInfo[0].length; j++) {
				if (mMapInfo[i][j] == 1) {
					if (isBlock == false) {
						startX = j;
						isBlock = true;
					}
				} else if (mMapInfo[i][j] == 0) {
					if (isBlock) {
						endX = j - 1;
						System.out.println("x = " + startX + "~" + endX + "/ y = " + i);
						putData(startX, endX, i);
						// blockMap.put(new Location, value)
						isBlock = false;
					}
				}

				// System.out.print(mMapInfo[i][j] + " ");

			}
			isBlock = false;
			// System.out.println();
		}
		putData(0, 19, 17);
		System.out.println(blockMap);
	}

	private void putData(int startX, int endX, int y) {
		// TODO : 위에도 체크,, !

		Area block;

		for (int i = 0; i < (endX - startX + 1); i++) {
			if (blockMap.containsKey(
					new Location((startX * blockImageIcon.getIconWidth()) + i * blockImageIcon.getIconWidth(),
							(y - 1) * blockImageIcon.getIconHeight()))) {

				block = new Area(startX * blockImageIcon.getIconWidth(), y * blockImageIcon.getIconHeight(),
						(endX - startX + 1) * blockImageIcon.getIconWidth(), blockImageIcon.getIconHeight(), true);

			} else {
				block = new Area(startX * blockImageIcon.getIconWidth(), y * blockImageIcon.getIconHeight(),
						(endX - startX + 1) * blockImageIcon.getIconWidth(), blockImageIcon.getIconHeight(), false);

			}

			blockMap.put(new Location((startX * blockImageIcon.getIconWidth()) + i * blockImageIcon.getIconWidth(),
					y * blockImageIcon.getIconHeight()), block);
		}
	}

	public static void main(String[] args) {
		Test test = new Test(2);
		test.readFile();
		test.setStage();
		test.test();

	}

}
