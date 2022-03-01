package com.vascodeveloper.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import com.vascodeveloper.entities.BulletShoot;
import com.vascodeveloper.entities.Enemy;
import com.vascodeveloper.entities.Enemy2;
import com.vascodeveloper.entities.Enemy3;
import com.vascodeveloper.entities.Entity;
import com.vascodeveloper.entities.Npc;
import com.vascodeveloper.entities.Player;
import com.vascodeveloper.graficos.Spritesheet;
import com.vascodeveloper.graficos.UI;
import com.vascodeveloper.world.Camera;
import com.vascodeveloper.world.World;
 
public class Game extends Canvas implements Runnable, KeyListener, MouseListener{

	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning = true;
	public static final int WIDTH = 320;
	public static final int HEIGHT = 240;
	public static final int SCALE = 3;
	
	private int CUR_LEVEL = 1, MAX_LEVEL = 3;
	
	private BufferedImage image;
	
	public static List<Entity> entities;
	public static List<Enemy> enemies;
	public static List<Enemy2> enemies2;
	public static List<Enemy3> enemies3;
	public static List<EnemyBoss> enemiesboss;
	public static List<BulletShoot> bullets;
	
	public static Spritesheet spritesheet;
	
	public static World world;
	public Npc npc;
	public static Player player;
	
	public static Random rand;
	
	public UI ui;
	
	public static String gameState = "MENU";
	private boolean showMessageGameOver = true;
	private int framesGameOver = 0;
	private boolean restartGame = false;
	
	//Sistema de cutscene
	public static int entrada = 1;
	public static int comecar = 2;
	public static int jogando = 3;
	public static int estado_cena = entrada;
	public int timeCena = 0,maxTimeCena = 60*3;
	
	public Menu menu;
	//public static int[] pixels;
	//public static int[] minimapaPixels;
	
	public static BufferedImage minimapa;
	
	public boolean saveGame = false;
	
	
	
	public Game(){
		//Sound.music.loop();
		rand = new Random();
		addKeyListener(this);
		addMouseListener(this);
		///////<<
		//setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize()));
		///////<
		setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		initFrame();
		//inicializando objetos;
		
		ui = new UI();
		image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
		//pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
		
		entities = new ArrayList<Entity>();
		enemies = new ArrayList<Enemy>();
		enemies2 = new ArrayList<Enemy2>();
		enemies3 = new ArrayList<Enemy3>();
		enemiesboss = new ArrayList<EnemyBoss>();
		bullets = new ArrayList<BulletShoot>();
		spritesheet = new Spritesheet("/spritesheet.png");
		
		player = new Player(0,0,16,16,spritesheet.getSprite(33, 0, 16, 16));
		entities.add(player);
		world = new World("/Level1.png");
		
		
		//minimapa = new BufferedImage(World.WIDTH,World.HEIGHT,BufferedImage.TYPE_INT_RGB);
		//minimapaPixels = ((DataBufferInt)minimapa.getRaster().getDataBuffer()).getData();
		
		menu = new Menu();
		
		Npc npc = new Npc(32,32,16,16,spritesheet.getSprite(9*16,0,16,16));
		entities.add(npc);
		
	}
	
	public void initFrame() {
		frame = new JFrame("Meu Jogo");
		frame.add(this);
		////////
		//frame.setUndecorated(true);
		///////
		frame.setResizable(false);
		frame.pack();
		/////////
		//Icone da janela
		//Image imagem = null;
		//try {
			//imagem = ImageIO.read(getClass().getResource("/icon.png"));
		//}catch (IOException e) {
			//e.printStackTrace();
		//}
		//Toolkit toolkit = Toolkit.getDefaultToolkit();
		//Image image = toolkit.getImage(getClass().getResource("/icon.png"));
		//Cursor c = toolkit.createCustomCursor(image, new Point(0,0), "img");
		//frame.setCursor(c);
		//frame.setIconImage(imagem);
		////////
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}
	
	public synchronized void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
	}

	public static void main(String args[]){
		Game game = new Game();
		game.start();
	}
	
	public void tick() {
		
		if(gameState == "NORMAL") {
			if(this.saveGame) {
				this.saveGame = false;
				String[] opt1 = {"level", "vida"};
				int[] opt2 = {this.CUR_LEVEL, (int) player.Life, player.ammo, player.moedas, player.score};
				Menu.saveGame(opt1,opt2,10);
				System.out.println("Jogo Salvo");
			}
		this.restartGame = false;	
	
		if(Game.estado_cena == Game.jogando) {
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.tick();
		}
		
		for(int i = 0; i< bullets.size(); i++) {
			bullets.get(i).tick();
		}
		}else {
			if(Game.estado_cena ==  Game.entrada) {
				if(player.getX() - Camera.x + player.getY() - Camera.y < 100) {
					player.x++;
					player.y++;
				}else {
					Game.estado_cena = Game.comecar;
				}
			}else if(Game.estado_cena == Game.comecar) {
					timeCena++;
					if(timeCena == maxTimeCena) {
						Game.estado_cena = Game.jogando;
					}
				}
			
		}
		
		if(player.redCard == 1) {
			//Avançar para o proximo level;
			CUR_LEVEL++;
			Sound.music7.play();
			if(CUR_LEVEL > MAX_LEVEL) {
				CUR_LEVEL = 1;
			}
			String newWorld = "Level"+CUR_LEVEL+".png";
			//System.out.println("new world");
			World.restartGame(newWorld);
		}
		}else if (gameState == "GAME_OVER"){
			this.framesGameOver++;
			if(this.framesGameOver == 30) {
				this.framesGameOver = 0;
				if(this.showMessageGameOver)
					this.showMessageGameOver = false;
					else
						this.showMessageGameOver = true;
			}
			
			if(restartGame) {
				this.restartGame = false;
				Game.gameState = "NORMAL";
				CUR_LEVEL = 1;
				String newWorld = "Level"+CUR_LEVEL+".png";
				//System.out.println("new world");
				World.restartGame(newWorld);
			}
		}else if(gameState == "MENU") {
			menu.tick();
		}
	}
	
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = image.getGraphics();
		g.setColor(new Color(0, 0, 0));
		g.fillRect(0, 0,WIDTH,HEIGHT);
		
		//Aqui eu edito a fonte e o que sera escrito na tela.
		//g.setFont(new Font("Arial",Font.BOLD,16)); //AQUI SE DEFINE O TAMANHO DA FONTE
		//g.setColor(Color.white); //AQUI SE DEFINE A COR DA FONTE
		//g.drawString("THE LEGEND OF JITSU", 23, 60); //AQUI SE DEFINE O TEXTO A SER EXIBIDO
		
		//TEXTO PRESSIONE START
		//g.setFont(new Font("Arial",Font.BOLD,10));
		//g.drawString("PRESSIONE START", 70, 100);
		
		//TEXTO CRIADO POR MILLER VASCO
		//g.setFont(new Font("Arial",Font.BOLD,10));
		//g.drawString("CRIADO POR MILLER VASCO", 44, 150);
		
		/*Renderização do Jogo*/
		//AQUI EU POSSO EXIBIR O PERSONAGEM NA TELA
		//Graphics2D g2 = (Graphics2D) g;
		
		world.render(g);
		Collections.sort(entities,Entity.nodeSorter);
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.render(g);
		}
		
		for(int i = 0; i< bullets.size(); i++) {
			bullets.get(i).render(g);
		}
		
		ui.render(g);
		/***/
		g.dispose();
		g = bs.getDrawGraphics();
		//g.drawImage(image, 0, 0,Toolkit.getDefaultToolkit().getScreenSize().width,Toolkit.getDefaultToolkit().getScreenSize().height,null);
		g.drawImage(image, 0, 0,WIDTH*SCALE,HEIGHT*SCALE,null);
		g.setFont(new Font("arial", Font.BOLD, 20));
		g.setColor(Color.white);
		g.drawString("Munição:" + player.ammo, 795, 33);
		g.setColor(Color.white);
		g.drawString("Pontuação Score:" + player.score, 495, 33);
		g.setColor(Color.white);
		g.drawString("Moedas:" + player.moedas, 295, 33);
		if(gameState == "GAME_OVER") {
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(new Color(0,0,0,100));
			g2.fillRect(0,0, WIDTH*SCALE, HEIGHT*SCALE);
			g.setFont(new Font("arial", Font.BOLD, 50));
			g.setColor(Color.white);
			g.drawString("GAME OVER",320,190);
			g.setFont(new Font("arial", Font.BOLD, 10));
			g.drawString("2021 >GameTeste< VascoDevelopercopyRights ",360,700);
			g.setFont(new Font("arial", Font.BOLD, 20));
			if(showMessageGameOver)
			    g.drawString(">PRESSIONE ENTER PARA REINICIAR<", 290, 500);
		}else if(gameState == "MENU") {
			menu.render(g);
		}
		
		if(Game.estado_cena == Game.comecar) {
			g.setFont(new Font("arial", Font.BOLD, 50));
			g.drawString("Se Prepare Pra Começar",210,350);
		}
		//World.renderMiniMap();
		g.drawImage(minimapa,830,40,World.WIDTH * 3,World.HEIGHT * 3, null);
		bs.show();
	}

	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		requestFocus();
		while(isRunning) {
			long now = System.nanoTime();
			delta+= (now - lastTime) / ns;
			lastTime = now;
			if(delta >= 1) {
				tick();
				render();
				frames++;
				delta--;
			}
			
			if(System.currentTimeMillis() - timer >= 1000) {
				System.out.println("FPS:" + frames);
				frames = 0;
				timer+=1000;
			}
		}
		
		stop();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		//if(e.getKeyCode() == KeyEvent.VK_E) {
			//npc.showMessage = false;
			//npc.show = false;
		//}
	
		if(e.getKeyCode() == KeyEvent.VK_RIGHT ||
				e.getKeyCode() == KeyEvent.VK_D) {
			player.right = true;
		}else if(e.getKeyCode() == KeyEvent.VK_LEFT ||
				e.getKeyCode() == KeyEvent.VK_A) {
			player.left = true;
		}
		
		if((e.getKeyCode() == KeyEvent.VK_UP ||
				e.getKeyCode() == KeyEvent.VK_W)) {
			player.up = true;
			
			if(gameState == "MENU") {
				menu.up = true;
			}
			
		}else if((e.getKeyCode() == KeyEvent.VK_DOWN ||
				e.getKeyCode() == KeyEvent.VK_S)) {
			player.down = true;
			
			if(gameState == "MENU") {
				menu.down = true;
			}
			
		}
		
		if(e.getKeyCode() == KeyEvent.VK_X) {
			Sound.music5.play();
			player.shoot = true;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			this.restartGame = true;
			if(gameState == "MENU") {
				menu.enter = true;
			}
		}
		
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			gameState = "MENU";
			Menu.pause = true;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			if(Game.gameState == "NORMAL")
			this.saveGame = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	
		if(e.getKeyCode() == KeyEvent.VK_RIGHT ||
				e.getKeyCode() == KeyEvent.VK_D) {
			player.right = false;
		}else if(e.getKeyCode() == KeyEvent.VK_LEFT ||
				e.getKeyCode() == KeyEvent.VK_A) {
			player.left = false;
		}
		
		if((e.getKeyCode() == KeyEvent.VK_UP ||
				e.getKeyCode() == KeyEvent.VK_W)) {
			player.up = false;
		}else if((e.getKeyCode() == KeyEvent.VK_DOWN ||
				e.getKeyCode() == KeyEvent.VK_S)) {
			player.down = false;
		}
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		player.mouseShoot = true;
		player.mx = (e.getX() /3);
		player.my = (e.getY() /3);
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}
	
	
}

