package adapter;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.ImageIcon;

import customInterface.Direction;
import gui.SNL;
import model.Location;
import model.Monster;

public class MapReader implements Direction {
	private int mStage;
	private int mMapInfo[][];
	private ImageIcon doorImageIcon;
	private ImageIcon blockImageIcon;
	private int doorX;
	private int doorY;

	ArrayList<Monster> monsters;
	private ArrayList<Location> monsterLocations;

	private HashSet <Rectangle2D> rectangleSet;
	
	public MapReader(int stage) {
		mStage = stage;
		rectangleSet = new HashSet<>();

		blockImageIcon = new ImageIcon(SNL.class.getResource("../images/block.png"));

		doorImageIcon = new ImageIcon(SNL.class.getResource("../images/door_close.png"));
		monsters = new ArrayList<Monster>();
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

		initMap();
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
			monsters.add(new Monster(monsterLocations.get(i).getX(), monsterLocations.get(i).getY(), monster, this));
		}
		return monsters;
	}

	public boolean isBlock(int coordX, int coordY) {
		if (mMapInfo[coordY / blockImageIcon.getIconHeight()][coordX / blockImageIcon.getIconWidth()] == 1)
			return true;
		else
			return false;
	}

	public void initMap() {
		boolean isBlock = false;

		int startX = -1, startY = -1;
		int endX = -1, endY = -1;

		rectangleSet.clear();
		for (int i = 0; i < mMapInfo.length; i++) {
			for (int j = 0; j < mMapInfo[0].length; j++) {

				if (mMapInfo[i][j] == 1) {
					if (isBlock == false) {
						startX = j;
						isBlock = true;
					}
				} else {
					if (isBlock) {
						endX = j - 1;
						putData(startX, endX, i);
						isBlock = false;
					}
				}

			}
			isBlock = false;
		}

		putData(0, 19, 17); // 천장
	}

	private void putData(int startX, int endX, int y) {
		
		for (int i = 0; i < (endX - startX + 1); i++) {
			rectangleSet.add(new Rectangle2D.Double(startX * blockImageIcon.getIconWidth(), y * blockImageIcon.getIconHeight(),
					(endX - startX + 1) * blockImageIcon.getIconWidth(), blockImageIcon.getIconHeight()));
		}

	}

	public boolean isCrush(Rectangle2D player) {		
		Iterator<Rectangle2D> iterator = rectangleSet.iterator();
		while(iterator.hasNext()) {
			Rectangle2D map = iterator.next();
			if(player.intersects(map))
				return true;
		}

		return false;
	}

	public int getBlockHeight() {
		return blockImageIcon.getIconHeight();
	}

	public int getBlockWidth() {
		return blockImageIcon.getIconWidth();
	}

	public int getStage() {
		return mStage;
	}
}
