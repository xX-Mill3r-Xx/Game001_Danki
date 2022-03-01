package com.vascodeveloper.entities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.vascodeveloper.main.Game;

public class Npc extends Entity {
	
	public String[] frases = new String[2];
	
	public boolean showMessage = false;
	//public boolean show = false;
	
	public int curIndexMsg = 0;
	public int fraseIndex = 0;
	
	public int time = 0;
	public int maxTime = 10;

	public Npc(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		frases[0] = ("Olá! Seja muito bem-vindo ao jogo.");
		frases[1] = ("Precione enter para continuar");
		//frases[2] = ("...");
		//frases[3] = ("...");
		//frases[4] = ("...");
	}
	
	public void tick() {
		int xPlayer = Game.player.getX();
		int yPlayer = Game.player.getY();
		
		int xNpc = (int)x;
		int yNpc = (int)y;
		
		if(Math.abs(xPlayer-xNpc) < 20 &&
				Math.abs(yPlayer - yNpc) < 20) {
			//if(show == false) {
			showMessage = true;
			//show = true;
			//}
		}else {
			showMessage = false;
		}
		
		this.time++;
		if(showMessage) {
			
			this.time++;
			if(this.time >= this.maxTime) {
			   this.time = 0;
			if(curIndexMsg < frases[fraseIndex].length() - 1) {
			curIndexMsg++;
			 
			}else {
				if(fraseIndex < frases.length - 1) {
					fraseIndex++;
				    curIndexMsg = 0;
				   
				}
			}
			}
		}
		
	}
	
	public void render(Graphics g) {
		
		super.render(g);
		if(showMessage) {
			g.fillRect(9, 9, Game.WIDTH - 18,Game.HEIGHT - 18);
			g.setColor(Color.white);
			
			g.setColor(Color.blue);
			g.fillRect(10, 10, Game.WIDTH - 20,Game.HEIGHT - 20);
			
			g.setFont(new Font("Arial", Font.BOLD,10));
			g.setColor(Color.white);
			g.drawString(frases[fraseIndex].substring(0,curIndexMsg), (int)x, (int)y);
			
			g.drawString(">Dica para passar de fase", (int)x+5, (int)y+13);
			g.drawString(">Voce precisa pegar o cartao vermelho", (int)x+5, (int)y+20);
			
			
		}
	}

}
