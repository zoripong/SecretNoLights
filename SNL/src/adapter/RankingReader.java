package adapter;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import gui.SNL;
import model.Record;

public class RankingReader {
	private String fileName;
	private ArrayList<Record> records;
	private ArrayList<ImageIcon> alphabets;
	private ArrayList<ImageIcon> numbers;
	private ArrayList<ImageIcon> smallNumbers;
	
	private Record record;
	
	public RankingReader(Record record) {
		fileName = "./src/ranking/rank.txt";
		records = new ArrayList<>();
		readFile();

		alphabets = new ArrayList<ImageIcon>();
		char alpha = 'a';
		for (int i = 0; i < 26; i++) {
			String name = "../images/ranking_" + alpha + ".png";
			alphabets.add(new ImageIcon(SNL.class.getResource(name)));
			alpha++;
		}

		numbers = new ArrayList<ImageIcon>();
		for (int i = 0; i < 10; i++) {
			String name = "../images/ranking_" + String.valueOf(i) + ".png";
			numbers.add(new ImageIcon(SNL.class.getResource(name)));
		}

		smallNumbers = new ArrayList<ImageIcon>();
		for(int i = 0; i<10; i++) {
			String name = "../images/" + String.valueOf(i) + ".png";
			smallNumbers.add(new ImageIcon(SNL.class.getResource(name)));
			
		}
		this.record = record;
	}

	private void readFile() {
		String line = "";
		String[] user;
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(fileName)));
			while ((line = br.readLine()) != null) {
				user = line.split(" ");
				records.add(new Record(user[0], user[1]));
			}

			System.out.println(records);
			br.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void writeFile() {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
			for(int i = 0; i<records.size(); i++) {
				bw.write(records.get(i).getName()+" "+records.get(i).getNumber());
				bw.newLine();
			}
			
			bw.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

	
	public int insertRecord() {
		int myScore = Integer.valueOf(record.getNumber());
		
		for(int i = 0; i<records.size(); i++) {
			
			int thisScore = Integer.valueOf(records.get(i).getNumber());
			
			if(myScore > thisScore) {
				records.add(i, record);
				System.out.println(i+"µîÀÔ´Ï´Ù¶û¶û´çµó¤·´çµ¿µ¿");
				return i;
			}
			
		}
		records.add(record);
			
		
		return records.size()-1;
	}

	public void showRecord(Graphics g, int idx) {
		Graphics2D g2d = (Graphics2D) g;

		int startX = 145, startY = 190, termY = 103;

	
		for (int i = idx; i < idx+5; i++) {
			System.out.println(i +", "+records.size());
			char userRank[] = String.valueOf(i).toCharArray();
			char userName[] = records.get(i).getName().toCharArray();
			char userScore[] = records.get(i).getNumber().toCharArray();
			
			if(userRank.length == 1) {
				g2d.drawImage(smallNumbers.get(0).getImage(), startX, startY, null);
				startX += (smallNumbers.get(0).getIconWidth());
				
			}
			for(int j = 0; j<userRank.length; j++) {
				g2d.drawImage(smallNumbers.get(Integer.valueOf(userRank[j]) - 48).getImage(), startX, startY, null);
				startX += (smallNumbers.get(Integer.valueOf(userRank[j]) - 48).getIconWidth());
						
			}
			startX = 250;
			
			for (int j = 0; j < userName.length; j++) {
				g2d.drawImage(alphabets.get(Math.abs((int) (97 - userName[j]))).getImage(), startX, startY, null);
				startX += (alphabets.get(Math.abs((int) (97 - userName[j]))).getIconWidth() + 10);
			}
			startX += 50;
			for (int j = 0; j < userScore.length; j++) {
				g2d.drawImage(numbers.get(Integer.valueOf(userScore[j]) - 48).getImage(), startX, startY, null);
				startX += (numbers.get(Integer.valueOf(userScore[j]) - 48).getIconWidth() + 10);
			}

			startX = 145;
			startY += termY;
		}

	}

	public int getRankingSize() {
		return records.size();
	}
}
