package com.vascodeveloper.graficos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.vascodeveloper.entities.Player;
import com.vascodeveloper.main.Game;

@SuppressWarnings("unused")
public class UI {

	public void render(Graphics g) {
		g.setColor(Color.red);
		g.fillRect(8, 4,80,8);
		g.setColor(Color.green);
		g.fillRect(8, 4,(int)((Game.player.Life/Game.player.maxLife)*80),8);
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD,10));
		g.drawString((int)Game.player.Life+"/"+(int)Game.player.maxLife,10,20);
	}
	
}
