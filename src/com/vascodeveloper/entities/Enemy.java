package com.vascodeveloper.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.vascodeveloper.main.Game;
import com.vascodeveloper.main.Sound;
import com.vascodeveloper.world.AStar;
import com.vascodeveloper.world.Vector2i;

public class Enemy extends Entity {
	
	@SuppressWarnings("unused")
	private double speed = 0.4;
	
	private int maskx = 8, masky = 8, maskw = 5, maskh = 5;
	
	
	//private boolean isDamage = false;
	//private int damageFrames = 10, damageCurrent = 0;
	
	private int life = 3;

	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		
	}
	
	public void tick() {
		depth = 0;
		/*if(this.calculateDistance(this.getX(), this.getY(), Game.player.getX(), Game.player.getY()) < 40) {
			
		}
		
		if(this.isColiddingWithPlayer() == false) {
		if((int)x < Game.player.getX()&& World.isFree((int)(x+speed), this.getY())
				&& !isColidding((int)(x+speed), this.getY())) {
			x+=speed;
		}
		else if((int)x > Game.player.getX()&& World.isFree((int)(x-speed), this.getY())
				&& !isColidding((int)(x-speed), this.getY())) {
			x-=speed;
		}
		if((int)y < Game.player.getY()&& World.isFree(this.getX(), (int)(y+speed)) &&
				!isColidding(this.getX(), (int)(y+speed))) {
			y+=speed;
		}
		else if((int)y > Game.player.getY()&& World.isFree(this.getX(), (int)(y-speed)) &&
				!isColidding(this.getX(), (int)(y-speed))) {
			y-=speed;
		}
		}else {
			//estamos perto do player
			//o que podemos fazer?
			if(Game.rand.nextInt(100) < 10) {
				Sound.hurtEfect.play();
			Game.player.Life-=Game.rand.nextInt(3);
			Game.player.isDamage = true;
			
			}
			
		}*/
		
		
			
		if(!isColiddingWithPlayer()) {
		if(path == null || path.size() == 0) {
			Vector2i start = new Vector2i((int)(x/16),(int)(y/16));
			Vector2i end = new Vector2i((int)(Game.player.x/16),(int)(Game.player.y/16));
			path = AStar.findPath(Game.world, start, end);
		}
		}else {
			if(Game.rand.nextInt(100) < 5) {
				Sound.music4.play();
			Game.player.Life-=Game.rand.nextInt(3);
			Game.player.isDamage = true;
			}
		}
		followPath(path);
		
		collidingBullet();
		
		if(life <= 0) {
			Sound.music3.play();
			Game.player.score+=100;
			destroySelf();
			return;
		}
	}
	
	public void destroySelf() {
		Game.enemies.remove(this);
		Game.entities.remove(this);
	}
	
	public void collidingBullet() {
		for(int i = 0; i < Game.bullets.size(); i++) {
			Entity e = Game.bullets.get(i);
			if(e instanceof BulletShoot) {
				
				if(Entity.isColidding(this, e)) {
					//isDamage = true;
					life--;
					Game.bullets.remove(i);
					return;
				}
				
			}
		}
		
	}
	
	public boolean isColiddingWithPlayer() {
		Rectangle enemyCurrent = new Rectangle(this.getX() + maskx,this.getY() + masky, maskw, maskh);
		Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY(), 16,16);
		
		return enemyCurrent.intersects(player);
	}
	
	public boolean isColidding(int xnext, int ynext) {
		Rectangle enemyCurrent = new Rectangle(xnext + maskx,ynext + masky, maskw, maskh);
		
		for(int i = 0; i < Game.enemies.size(); i++) {
			Enemy e = Game.enemies.get(i);
			if(e == this)
				continue;
			Rectangle targetEnemy = new Rectangle(e.getX() + maskx,e.getY()+ masky, maskw, maskh);
		    if(enemyCurrent.intersects(targetEnemy)) {
		    	return true;
		    }
		}
		return false;
	}
	
	public void render(Graphics g) {
		//if(!isDamage)
			//g.drawImage(sprites[index], this.getX() - Camera.x,this.getY() - Camera.y,null);
		//else
			//g.drawImage(Entity.ENEMY_FEEDBACK, this.getX() - Camera.x,this.getY() - Camera.y,null);
		super.render(g);
		//g.setColor(Color.blue);
		//g.fillRect(this.getX() + maskx - Camera.x, this.getY() + masky - Camera.y, maskw, maskh);
	}

}
