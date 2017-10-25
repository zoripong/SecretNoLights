package adapter;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import customInterface.AutoMovingListener;
import gui.SNL;
import model.Location;
import model.Monster;
import thread.MonsterThread;

public class MapReader {

	private int mStage;
	private int mMapInfo[][];
	private ImageIcon doorImageIcon;
	private ImageIcon blockImageIcon;
	private int doorX;
	private int doorY;

	ArrayList<Monster> monsters;

	private ArrayList<Location> monsterLocations;


	public MapReader(int stage) {
		mStage = stage;
		blockImageIcon = new ImageIcon(SNL.class.getResource("../images/block.png"));

		doorImageIcon = new ImageIcon(SNL.class.getResource("../images/door_close.png"));
		readFile();
		monsterLocations = new ArrayList<>();
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

	public void drawStage(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		int x = 0, y = 0;

		for (int i = 0; i < mMapInfo.length; i++) {
			for (int j = 0; j < mMapInfo[0].length; j++) {
				if (mMapInfo[i][j] == 1)
					// 블록 설치
					g2d.drawImage(blockImageIcon.getImage(), x, y, null);

				x += blockImageIcon.getIconWidth();

			}
			x = 0;
			y += blockImageIcon.getIconHeight();
		}

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

	public void nextStage() {
		mStage++;
		readFile(); // 파일 리드
		setStage(); // 좌표 갱신
	}

	public double getDoorMid() {
		return (double) (doorX + doorImageIcon.getIconWidth() / 2);
	}

	public int getDoorX() {
		return doorX;
	}

	public int getDoorY() {
		return doorY;
	}

	public void drawDoor(Graphics g, int isOpen) {
		Graphics2D g2d = (Graphics2D) g;
		ImageIcon doorImage;
		if (isOpen == 1) {
			doorImage = new ImageIcon(SNL.class.getResource("../images/door_open.png"));
		} else {
			doorImage = new ImageIcon(SNL.class.getResource("../images/door_close.png"));
		}
		g2d.drawImage(doorImage.getImage(), doorX, doorY, null);

	}

	public ArrayList<Monster> initMonsters() {
		ImageIcon monster = new ImageIcon(SNL.class.getResource("../images/front_3.png"));
		monsters = new ArrayList<Monster>();
		for (int i = 0; i < monsterLocations.size(); i++) {
			monsters.add(new Monster(monsterLocations.get(i).getX(), monsterLocations.get(i).getY(), monster));

		}

		return monsters;
	}

	public boolean isBlock(int coordX, int coordY) {

		// System.out.println(
		// "나머지 : " + coordX % blockImageIcon.getIconWidth() + "," + coordY %
		// blockImageIcon.getIconHeight());
		// System.out.println("(" + coordX / blockImageIcon.getIconWidth() + "," +
		// coordY / blockImageIcon.getIconHeight()
		// + ") = " + mMapInfo[coordY / blockImageIcon.getIconHeight()][coordX /
		// blockImageIcon.getIconWidth()]);
		if (mMapInfo[coordY / blockImageIcon.getIconHeight()][coordX / blockImageIcon.getIconWidth()] == 1)
			return true;
		else
			return false;
	}

	public boolean isUnderBlock(int coordX, int coordY) {
		
		if (mMapInfo[coordY / blockImageIcon.getIconHeight()+1][coordX / blockImageIcon.getIconWidth()] == 1)
			return true;
		else
			return false;
	}
	// TODO : // 끝에 블록 아니면 점프하면서 부딪히면 안돼용,,
	// public boolean isAmongBlcok(int coordX, int coordY) {
	//
	// // System.out.println("(coord/location)="+coordY+"/"+p.get);
	//
	// // System.out.println("y좌표"+p.getLocation('A').getY()/
	// // blockImageIcon.getIconHeight());
	// if (coordX / blockImageIcon.getIconWidth() >= 0
	// && coordX / blockImageIcon.getIconWidth() < mMapInfo[0].length) {
	// System.out.println("----------------------------------------------------");
	// System.out.println("1");
	// System.out.println("coordY : " + coordY);
	// System.out.println("idx : " + (coordY-blockImageIcon.getIconHeight()) /
	// blockImageIcon.getIconHeight()+1));
	// System.out.println("----------------------------------------------------");
	// if (mMapInfo[(coordY - blockImageIcon.getIconHeight()) /
	// blockImageIcon.getIconHeight()+1][coordX / blockImageIcon.getIconWidth() - 2]
	// == 1
	// && mMapInfo[(coordY- blockImageIcon.getIconHeight()) /
	// blockImageIcon.getIconHeight()+1][coordX / blockImageIcon.getIconWidth()] ==
	// 1) {
	// System.out.println("x=" + coordY / blockImageIcon.getIconHeight() + "y="
	// + (coordX / blockImageIcon.getIconWidth() - 2) + ","
	// + mMapInfo[coordY / blockImageIcon.getIconHeight()][coordX /
	// blockImageIcon.getIconWidth() - 2]);
	// System.out.println("x=" + coordY / blockImageIcon.getIconHeight() + "y="
	// + (coordX / blockImageIcon.getIconWidth()) + ","
	// + mMapInfo[coordY / blockImageIcon.getIconHeight()][coordX /
	// blockImageIcon.getIconWidth()]);
	//
	// return true;
	// }
	// } else if (coordX / blockImageIcon.getIconWidth() < 0) {
	// System.out.println("2");
	// if (mMapInfo[coordY / blockImageIcon.getIconHeight()][coordX /
	// blockImageIcon.getIconWidth() + 1] == 1)
	// return true;
	// } else if (coordX / blockImageIcon.getIconWidth() >= mMapInfo[0].length) {
	// System.out.println("3");
	// if (mMapInfo[coordY / blockImageIcon.getIconHeight()][coordX /
	// blockImageIcon.getIconWidth() - 1] == 1)
	// return true;
	// }
	//
	// return false;
	//
	// }

}
