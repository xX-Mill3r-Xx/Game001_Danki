package com.vascodeveloper.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.vascodeveloper.world.World;

public class Menu {
	
	public String[] options = {"novo jogo", "carregar jogo", "sair"};
	
	public int currentOption = 0;
	public int maxOption = options.length -1;
	
	public boolean up, down, enter;
	
	public static boolean pause = false;
	
	public static boolean saveExists = false;
	public static boolean saveGame = false;

	public void tick() {
		File file = new File("save.txt");
		if(file.exists()) {
			saveExists = true;
		}else {
			saveExists = false;
		}
		if(up) {
			Sound.music2.play();
			up = false;
			currentOption--;
			if(currentOption < 0)
				currentOption = maxOption;
		}
		if(down) {
			Sound.music2.play();
			down = false;
			currentOption++;
			if(currentOption > maxOption)
				currentOption = 0;
		}
		
		if(enter) {
			//Sound.music.loop();
			Sound.music7.play();
			enter = false;
			if(options[currentOption] == "novo jogo" || options[currentOption] == "continuar") {
				Game.gameState = "NORMAL";
				pause = false;
				file = new File("save.txt");
				file.delete();
			}else if(options[currentOption] == "carregar jogo") {
				file = new File("save.txt");
				if(file.exists()) {
					String saver = loadGame(10);
					applySave(saver);
				}
			}
			
			else if(options[currentOption] == "sair"){
				Sound.music2.play();
				System.exit(1);
			}
		}
	}
	
	public static void applySave(String str) {
		String[] spl = str.split("/");
		for(int i = 0; i < spl.length; i++) {
			String[] spl2 = spl[i].split(":");
			switch(spl2[0]) 
			{
			    case "level":
			    	World.restartGame("level"+spl2[1]+".png");
			    	Game.gameState = "NORMAL";
			    	pause = false;
			    	break;
			    case "vida":
			    	Game.player.Life = Integer.parseInt(spl2[1]);
			    	break;
			}
		}
	}
	
	public static String loadGame(int encode) {
		String line = "";
		File file = new File("save.txt");
		if(file.exists()) {
			try {
				String singleLine = null;
				BufferedReader reader = new BufferedReader(new FileReader("save.txt"));
				try {
					while((singleLine = reader.readLine()) != null) {
						String[] trans = singleLine.split(":");
					    char[] val1 = trans[1].toCharArray();
					    trans[1] = "";
					    for(int i = 0; i < val1.length; i++) {
					    	val1[i]-=encode;
					    	trans[1]+=val1[i];
					    }
					    line+=trans[0];
					    line+=":";
					    line+=trans[1];
					    line+="/";
					    
					}
				}catch(IOException e) {}
			}catch (FileNotFoundException e) {}
		}
		return line;
	}
	
	public static void saveGame(String[] val1, int[] val2, int encode) {
		BufferedWriter write = null;
		try {
			write = new BufferedWriter(new FileWriter("save.txt"));
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		for(int i = 0; i < val1.length; i++) {
			String current = val1[i];
			current+=":";
			char[] value = Integer.toString(val2[i]).toCharArray();
			for(int n = 0; n < value.length; n++) {
				value[n]+=encode;
				current+=value[n];
			}
			try {
				write.write(current);
				if(i < val1.length - 1)
					write.newLine();
			}catch(IOException e) {}
		}
		try {
			write.flush();
			write.close();
		}catch(IOException e) {}
	}
	
	public void render(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE);
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD, 36));
		g.drawString(">MILLER GAME<", (Game.WIDTH*Game.SCALE) / 2 - 140, (Game.HEIGHT*Game.SCALE) / 2 - 230);
		
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD, 20));
		
		if(pause == false)
			g.drawString("NOVO JOGO", (Game.WIDTH*Game.SCALE) / 2 - 65, (Game.HEIGHT*Game.SCALE) / 2 - 10);
		else
		    g.drawString("CONTINUE;", (Game.WIDTH*Game.SCALE) / 2 - 60, (Game.HEIGHT*Game.SCALE) / 2 - 10);
		g.drawString("CARREGAR JOGO", (Game.WIDTH*Game.SCALE) / 2 - 90, (Game.HEIGHT*Game.SCALE) / 2 - -10);
		g.drawString("SAIR", (Game.WIDTH*Game.SCALE) / 2 - 30, (Game.HEIGHT*Game.SCALE) / 2 - -30);
		
		g.drawString(">Criado Por MillerVasco @vascodeveloper.com<", (Game.WIDTH*Game.SCALE) / 2 - 230, (Game.HEIGHT*Game.SCALE) / 2 - -320);
		g.drawString(">/ 2021 vascodeveloper.com //Game Teste 001<", (Game.WIDTH*Game.SCALE) / 2 - 220, (Game.HEIGHT*Game.SCALE) / 2 - -340);
		
		if(options[currentOption] == "novo jogo" ) {
			g.drawString(">", (Game.WIDTH*Game.SCALE) / 2 - 95, 348);
		}else if(options[currentOption] == "carregar jogo") {
			g.drawString(">", (Game.WIDTH*Game.SCALE) / 2 - 120, 370);
		}else if(options[currentOption] == "sair") {
			g.drawString(">", (Game.WIDTH*Game.SCALE) / 2 - 60, 390);
		}
	}
	
}
