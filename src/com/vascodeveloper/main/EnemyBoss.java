package com.vascodeveloper.main;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.vascodeveloper.entities.BulletShoot;
import com.vascodeveloper.entities.Entity;
import com.vascodeveloper.world.AStar;
import com.vascodeveloper.world.Vector2i;

public class EnemyBoss extends Entity{
	
	private int maskx = 2, masky = 2, maskw = 64, maskh = 64;
	
	private int life = 100;

	public EnemyBoss(int x, int y, int width, int height, BufferedImage sprite) {
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
			Game.player.Life-=Game.rand.nextInt(2);
			Game.player.isDamage = true;
			}
		}
		followPath(path);
		
		collidingBullet();
		
		if(life <= 0) {
			Sound.music3.play();
			Game.player.score+=1000;
			Game.player.moedas+=1000;
			destroySelf();
			return;
		}
	}
	
	@SuppressWarnings("unlikely-arg-type")
	public void destroySelf() {
		Game.enemies3.remove(this);
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
		
		for(int i = 0; i < Game.enemiesboss.size(); i++) {
			EnemyBoss e = Game.enemiesboss.get(i);
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
	super.render(g);
	//g.setColor(Color.blue);
	//g.fillRect(this.getX() + maskx - Camera.x, this.getY() + masky - Camera.y, maskw, maskh);
	}

}
