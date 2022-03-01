package com.vascodeveloper.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.vascodeveloper.main.Game;
import com.vascodeveloper.world.Camera;
import com.vascodeveloper.world.World;

public class BulletShoot extends Entity {
	
        private double dx;
        private double dy;
        
        private double spd = 2;
        
        private int life = 1000,curLife = 0;

		public BulletShoot(int x, int y, int width, int height, BufferedImage sprite, double dx, double dy) {
			super(x, y, width, height, sprite);
			this.dx = dx;
			this.dy = dy;
		}
		
		public void tick() {
			if(World.isFreeDynamic((int)(x+(dx*spd)),(int)(y+(dx*spd)),3,3)) {
			x+=dx*spd;
			y+=dy*spd;
			}else {
				Game.bullets.remove(this);
				return;
			}
			curLife++;
			if(curLife == life) {
				Game.bullets.remove(this);
				return;
			}
		
	    }
		
		public void render(Graphics g) {
			g.setColor(Color.YELLOW);
			g.fillOval(this.getX() - Camera.x, this.getY() - Camera.y,width, height);
		}
	
}
