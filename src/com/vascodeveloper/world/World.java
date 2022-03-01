package com.vascodeveloper.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.vascodeveloper.entities.Bullet;
import com.vascodeveloper.entities.Coin;
import com.vascodeveloper.entities.Enemy;
import com.vascodeveloper.entities.Enemy2;
import com.vascodeveloper.entities.Enemy3;
import com.vascodeveloper.entities.Entity;
import com.vascodeveloper.entities.LifePac;
import com.vascodeveloper.entities.Player;
import com.vascodeveloper.entities.RedCard;
import com.vascodeveloper.entities.Weapon;
import com.vascodeveloper.graficos.Spritesheet;
import com.vascodeveloper.main.EnemyBoss;
import com.vascodeveloper.main.Game;

public class World {
	
	public static Tile[] tiles;
	public static int WIDTH,HEIGHT;
	public static final int TILE_SIZE = 16;

	public World(String path) {
		/////////
		try {
			BufferedImage mapa = ImageIO.read(getClass().getResource(path));
			int[] pixels = new int[mapa.getWidth() * mapa.getHeight()];
			WIDTH = mapa.getWidth();
			HEIGHT = mapa.getHeight();
			tiles = new Tile[mapa.getWidth() * mapa.getHeight()];
			mapa.getRGB(0, 0, mapa.getWidth(), mapa.getHeight(), pixels, 0, mapa.getWidth());
			for(int xx = 0; xx < mapa.getWidth(); xx++) {
				for(int yy = 0; yy < mapa.getHeight(); yy++) {
					int pixelAtual = pixels[xx + (yy*mapa.getWidth())];
					tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16,yy*16,Tile.TILE_FLOOR);
					if(pixelAtual == 0xFF000000) {
						//Flor/chão
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16,yy*16,Tile.TILE_FLOOR);
                        ///
					}else if(pixelAtual == 0xFF606060) {
							//Flor/chão/GRAMA
							tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16,yy*16,Tile.TILE_FLOOR_GRAMA);
						///
					}else if(pixelAtual == 0xFFFFFFFF) {
						//parede
						tiles[xx + (yy * WIDTH)] = new WallTile(xx*16,yy*16,Tile.TILE_WALL);
					}else if(pixelAtual == 0xFF7F0000) {
						//parede
						tiles[xx + (yy * WIDTH)] = new WallTile(xx*16,yy*16,Tile.TILE_WALL_2);
					}else if(pixelAtual == 0xFF0026FF) {
						//Player
						Game.player.setX(xx*16);
						Game.player.setY(yy*16);
					}else if(pixelAtual == 0xFFFF0000){
						//Enemy
						Enemy en = new Enemy(xx*16,yy*16,16,16, Entity.ENEMY_EN);
						Game.entities.add(en);
						Game.enemies.add(en);
						/////
					}else if(pixelAtual == 0xFF00FFFF){
						//Enemy2
						Enemy2 en2 = new Enemy2(xx*16,yy*16,16,16, Entity.ENEMY2_EN);
						Game.entities.add(en2);
						Game.enemies2.add(en2);
					}else if(pixelAtual == 0xFFFF6A00){
						//Enemy3
						Enemy3 en3 = new Enemy3(xx*16,yy*16,16,16, Entity.ENEMY3_EN);
						Game.entities.add(en3);
						Game.enemies3.add(en3);
						/////
						
					}else if(pixelAtual == 0xFFD67FFF){
						//EnemyBoss
						EnemyBoss enb = new EnemyBoss(xx*16,yy*16,16,16, Entity.ENEMYCHEFE_EN);
						Game.entities.add(enb);
						Game.enemiesboss.add(enb);
						
					}else if(pixelAtual == 0xFF008080) {
						//Weapon
						Game.entities.add(new Weapon(xx*16,yy*16,16,16, Entity.WEAPON_EN));
					}else if(pixelAtual == 0xFF00FF21) {
						//LifePack
						Game.entities.add(new LifePac(xx*16,yy*16,16,16, Entity.LIFEPACK_EN));
						
					}else if(pixelAtual == 0xFFFF006E) {
						//Coin Moedas
						Game.entities.add(new Coin(xx*16,yy*16,16,16, Entity.COIN_EN));
						
					}else if(pixelAtual == 0xFF6D7F3F) {
						//RedCard
						Game.entities.add(new RedCard(xx*16,yy*16,16,16, Entity.REDCARD_EN));
						
					}else if(pixelAtual == 0xFFFFD800 ) {
						//Bullet
						Game.entities.add(new Bullet(xx*16,yy*16,16,16, Entity.BULLET_EN));
					}
					
					
					
						
					
					
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		/*Game.player.setX(0);
		Game.player.setY(0);
		WIDTH = 100;
		HEIGHT = 100;
		tiles = new Tile[WIDTH*HEIGHT];
		
		for(int xx = 0; xx < WIDTH; xx++) {
			for(int yy = 0; yy < HEIGHT; yy++) {
				tiles[xx+yy*WIDTH] = new WallTile(xx*16, yy*16,Tile.TILE_WALL);
			}
		}
		
		int dir = 0;
		int xx = 0, yy = 0;
		
		for(int i = 0; i < 1000; i++) {
			tiles[xx+yy*WIDTH] = new FloorTile(xx*16, yy*16,Tile.TILE_FLOOR);
			if(dir == 0) {
				//direita
				if(xx < WIDTH) {
					xx++;
				}
			}else if(dir == 1) {
				//esquerda
				if(xx > 0) {
					xx--;
				}
			}else if(dir == 2) {
				//baixo
				if(yy < HEIGHT) {
					yy++;
				}
			}else if(dir == 3) {
				//cima
				if(yy > 0) {
					yy--;
				}
			}
			if(Game.rand.nextInt(100) < 90) {
				dir = Game.rand.nextInt(4);
			}
			
		}*/
		/////////
	}
	
	public static boolean isFreeDynamic(int xnext, int ynext, int width, int height) {
		int x1 = xnext / TILE_SIZE;
		int y1 = ynext / TILE_SIZE;
		
		int x2 = (xnext+width-1) / TILE_SIZE;
		int y2 = ynext / TILE_SIZE;
		
		int x3 = xnext / TILE_SIZE;
		int y3 = (ynext+height-1) / TILE_SIZE;
		
		int x4 = (xnext+width-1) / TILE_SIZE;
		int y4 = (ynext+height-1) / TILE_SIZE;
		
		return !((tiles[x1 + (y1*World.WIDTH)] instanceof WallTile) ||
				(tiles[x2 + (y2*World.WIDTH)] instanceof WallTile) ||
				(tiles[x3 + (y3*World.WIDTH)] instanceof WallTile) ||
				(tiles[x4 + (y4*World.WIDTH)] instanceof WallTile));
		
	}
	
	
	public static boolean isFree(int xnext, int ynext) {
		int x1 = xnext / TILE_SIZE;
		int y1 = ynext / TILE_SIZE;
		
		int x2 = (xnext+TILE_SIZE-1) / TILE_SIZE;
		int y2 = ynext / TILE_SIZE;
		
		int x3 = xnext / TILE_SIZE;
		int y3 = (ynext+TILE_SIZE-1) / TILE_SIZE;
		
		int x4 = (xnext+TILE_SIZE-1) / TILE_SIZE;
		int y4 = (ynext+TILE_SIZE-1) / TILE_SIZE;
		
		return !((tiles[x1 + (y1*World.WIDTH)] instanceof WallTile) ||
				(tiles[x2 + (y2*World.WIDTH)] instanceof WallTile) ||
				(tiles[x3 + (y3*World.WIDTH)] instanceof WallTile) ||
				(tiles[x4 + (y4*World.WIDTH)] instanceof WallTile));
		
	}
	
	public static void restartGame(String Level) {
		Game.entities = new ArrayList<Entity>();
		Game.enemies = new ArrayList<Enemy>();
		Game.enemies2 = new ArrayList<Enemy2>();
		Game.enemies3 = new ArrayList<Enemy3>();
		Game.bullets.clear();
		Game.spritesheet = new Spritesheet("/spritesheet.png");
		Game.player = new Player(0,0,16,16,Game.spritesheet.getSprite(33, 0, 16, 16));
		Game.entities.add(Game.player);
		Game.world = new World("/"+Level);
		return;
	}
	
	public void render(Graphics g) {
		int xstart = Camera.x >> 4;
		int ystart = Camera.y >> 4;
		
		int xfinal = xstart + (Game.WIDTH >> 4);
		int yfinal = ystart + (Game.HEIGHT >> 4);
		
		for(int xx = xstart; xx <= xfinal;	xx++) {
			for(int yy = ystart; yy <= yfinal;	yy++) {
				if(xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT)
				    continue;
				Tile tile = tiles[xx +(yy*WIDTH)];
				tile.render(g);
			}
		}
	}
	
	/*public static void renderMiniMap() {
		for(int i = 0; i < Game.minimapaPixels.length; i++) {
			Game.minimapaPixels[i] = 0;	
		}
		for(int xx = 0; xx < WIDTH; xx++) {
			for(int yy = 0; yy < HEIGHT; yy++) {
				if(tiles[xx + (yy*WIDTH)] instanceof WallTile){
					Game.minimapaPixels[xx +(yy*WIDTH)] = 0xff0000;
			  }	
		   
			}	  
		}
	}*/
}
