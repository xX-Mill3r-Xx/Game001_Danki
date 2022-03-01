package com.vascodeveloper.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import com.vascodeveloper.main.Game;
import com.vascodeveloper.main.Sound;
import com.vascodeveloper.world.Camera;
import com.vascodeveloper.world.World;

public class Player extends Entity {
	
	public boolean right, up, left, down;
	public int right_dir = 0, left_dir = 1;
	public int dir = right_dir;
	public double speed = 1.4;

	private int frames = 0, maxFrames = 5,index = 0,maxIndex = 0;
	private boolean moved = false;
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	
	private BufferedImage playerDamage;
	
	private boolean arma = false;
	
	public int ammo = 1000;
	public int score = 0;
	public int moedas = 0;
	public int redCard = 0;
	
	public boolean isDamage = false;
	private int damageFrames = 0;
	
	public boolean shoot = false, mouseShoot = false;
	
	public double Life = 100, maxLife = 100;
	public int mx, my;
	
	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		rightPlayer = new BufferedImage[5];
		leftPlayer = new BufferedImage[5];
		playerDamage = Game.spritesheet.getSprite(0,16,16,16);
		for(int i = 0; i < 4; i++) {
		    rightPlayer[0] = Game.spritesheet.getSprite(32 + (i*16), 0, 16, 16);
		}
		for(int i = 0; i < 4; i++) {
		    leftPlayer[0] = Game.spritesheet.getSprite(32 + (i*16), 16, 16, 16);
		}
		
	}
	
	public void tick() {
		depth = 1;
		moved = false;
		if(right && World.isFree((int)(x+speed),this.getY())) {
			moved = true;
			dir = right_dir;
			x+=speed;
		}
		else if(left && World.isFree((int)(x-speed),this.getY())) {
			moved = true;
			dir = left_dir;
			x-=speed;
		}
		if(up && World.isFree(this.getX(),(int)(y-speed))) {
			moved = true;
			y-=speed;
		}
		else if(down && World.isFree(this.getX(),(int)(y+speed))) {
			moved = true;
			y+=speed;
		}
		
		if(moved) {
			frames++;
			if(frames == maxFrames) {
				frames = 0;
				index++;
				if(index > maxIndex)
					index = 0;
			}
		}
		this.checkCollisionLifePack();
		checkCollisionAmmo();
		checkCollisionGun();
		checkCollisionCoin();
		checkCollisionRedCard();
		
		if(isDamage) {
			this.damageFrames++;
			if(this.damageFrames == 8) {
				this.damageFrames = 0;
				isDamage = false;
			}
		}
		
		if(shoot) {
			shoot = false;
			if(arma && ammo > 0) {
			ammo --;
			//Criar bala e atirar;
			
			int dx = 0;
			int px = 0;
			int py = 8;
			//System.out.println("Atirando");
			if(dir == right_dir) {
				px = 18;
				dx = 1;
			}else {
				px = -8;
				dx = -1;
			}
			
			BulletShoot bullet = new BulletShoot(this.getX()+px, this.getY()+py, 3, 3, null, dx, 0);
			Game.bullets.add(bullet);
			}
			
			if(mouseShoot) {
				System.out.println("Mouse funcionando");
				mouseShoot = false;
				double angle = (Math.atan2(my - (this.getY()+8 - Camera.y), mx - (this.getX()+8 - Camera.x)));
				
				if(arma && ammo > 0) {
				ammo --;
				//Criar bala e atirar;

				double dx = Math.cos(angle);
				double dy = Math.sin(angle);
				int px = 8;
				int py = 8;
				
				
				BulletShoot bullet = new BulletShoot(this.getX()+px, this.getY()+py, 3, 3, null, dx, dy);
				Game.bullets.add(bullet);
				
			    }
			}
		}
		
		if(Life<=0) {
			//Game Over!
			Life = 0;
			Game.gameState = "GAME_OVER";
			Sound.music8.play();
		}
		
		updateCamera();
	}
		
		public void updateCamera() {
			Camera.x = Camera.clamp(this.getX() - (Game.WIDTH/2),0,World.WIDTH*16 - Game.WIDTH);
			Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT/2),0,World.HEIGHT*16 - Game.HEIGHT);
		}
		
	
	public void checkCollisionGun() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if(atual instanceof Weapon) {
				if(Entity.isColidding(this, atual)) {
					Sound.music9.play();
					arma = true;
					//System.out.println("Pegou a Arma");
					Game.entities.remove(atual);
				}
			}
		}
	}
	
	public void checkCollisionAmmo() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if(atual instanceof Bullet) {
				if(Entity.isColidding(this, atual)) {
					Sound.music9.play();
					ammo+=10;
					//System.out.println("Muniçaõ atual" + ammo);
					Game.entities.remove(atual);
				}
			}
		}
	}
	
	public void checkCollisionLifePack() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if(atual instanceof LifePac) {
				if(Entity.isColidding(this, atual)) {
					Sound.music6.play();
					Life+=10;
					if(Life > 100)
						Life = 100;
					Game.entities.remove(atual);
				}
			}
		}
	}
	
	public void checkCollisionCoin() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if(atual instanceof Coin) {
				if(Entity.isColidding(this, atual)) {
				Sound.music6.play();
				moedas+=10;
				Game.entities.remove(atual);
			   }
				
		    }
		}
	}
	
	public void checkCollisionRedCard() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if(atual instanceof RedCard) {
				if(Entity.isColidding(this, atual)) {
					Sound.music6.play();
				redCard+=1;
				Game.entities.remove(atual);
			   }
				
		    }
		}
	}
	
		
	public void render(Graphics g) {
		if(!isDamage) {
		if(dir == right_dir) {
		    g.drawImage(rightPlayer[index],this.getX() - Camera.x,this.getY() - Camera.y, null);
		    if(arma) {
		    	//Desenhar arma para direita;
		    	g.drawImage(Entity.GUN_RIGHT, this.getX()+7 - Camera.x, this.getY() - Camera.y, null);
		    }
		}else if(dir == left_dir) {
			g.drawImage(leftPlayer[index],this.getX() - Camera.x,this.getY() - Camera.y, null);
			if(arma) {
				//Desenhar arma para esquerda;
				g.drawImage(Entity.GUN_LEFT, this.getX()-7 - Camera.x, this.getY() - Camera.y, null);
			}
		}
		}else {
			g.drawImage(playerDamage,this.getX() - Camera.x, this.getY() - Camera.y,null);
		}
	}

}
