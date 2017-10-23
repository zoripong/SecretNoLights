package adapter;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.ImageIcon;

import gui.SNL;

public class MapReader {

	private int mStage;
	private int mMapInfo[][];

	public MapReader(int stage) {

		mStage = stage;
	}
	
	public void setStage(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		readFile();
		int x = 0, y = 0;

		ImageIcon blockImageIcon = new ImageIcon(SNL.class.getResource("../images/block.png"));

		for (int i = 0; i < mMapInfo.length; i++) {
			for (int j = 0; j < mMapInfo[0].length; j++) {
				if (mMapInfo[i][j] == 1) {
					// 블록 설치
					g2d.drawImage(blockImageIcon.getImage(), x, y, null);

				}
				x += blockImageIcon.getIconWidth();
			}
			x = 0;
			y+=blockImageIcon.getIconHeight();
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
		mStage ++ ;
	}
}
