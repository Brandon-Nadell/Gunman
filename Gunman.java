package com.mygdx.game;

import java.util.ArrayList;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.mygdx.game.Block.Type;
import com.mygdx.game.Powerup.Power;
import com.mygdx.game.Block.BlockTemplate;
import com.mygdx.game.Unit.Team;

public class Gunman extends ApplicationAdapter {
	
	private static SpriteBatch batch;
	private static ShapeRenderer sr;
	private static ArrayList<Unit> units;
	private static ArrayList<Projectile> projectiles;
	private static ArrayList<Block> blocks;
	private static ArrayList<Powerup> powerups;
	private static Texture score;
	private static boolean resetting;
	private static int timer;
	private static TextureRegion[] scores = new TextureRegion[5];
	
	
	public void create() {
		batch = new SpriteBatch();
		sr = new ShapeRenderer();
		score = new Texture("scores.png");
		for (int i = 0; i < scores.length; i++) {
			scores[i] = new TextureRegion(score, 50*i, 0, 50, 50);
		}
		reset();
	}

	public void render() {
		sr.begin(ShapeType.Filled);
		sr.setColor(Color.BLACK);
		sr.rect(0, 0, 1000, 600);
		sr.setColor(198/255f, 171/255f, 150/255f, 1);
		sr.rect(2, 2, 1000 - 4, 600 - 4);
		sr.end();
		if (timer % 600 == 599 && powerups.size() < 5) {
			int x;
			int y;
			ArrayList<Entity> entities = new ArrayList<Entity>();
			entities.addAll(blocks);
			entities.addAll(powerups);
			do {
				x = ((int)(Math.random()*20))*50;
				y = ((int)(Math.random()*12))*50;
			} while (new Entity(x, y, 50, 50).touchingAny(entities));
			powerups.add(new Powerup(x, y, Power.values()[(int)(Math.random()*Power.values().length)]));
		}
		
		for (int i = 0; i < projectiles.size(); i++) {
			projectiles.get(i).render();
			if (projectiles.get(i).getRemove()) {
				projectiles.remove(i);
				i--;
			}
		}
		for (int i = 0; i < blocks.size(); i++) {
			blocks.get(i).render();
			if (blocks.get(i).getRemove()) {
				blocks.remove(i);
				i--;
			}
		}
		for (int i = 0; i < units.size(); i++) {
			units.get(i).render();
			if (units.get(i).getRemove()) {
				units.remove(i);
				i--;
			}
		}
		for (int i = 0; i < powerups.size(); i++) {
			powerups.get(i).render();
			if (powerups.get(i).getRemove()) {
				powerups.remove(i);
				i--;
			}
		}
		if (resetting) {
			reset();
			resetting = false;
		}
		timer++;
		if (Gdx.input.isKeyPressed(Keys.H)) {
			sr.begin(ShapeType.Line);
			sr.setColor(Color.WHITE);
			for (int i = 0; i < 1000; i += 50) {
				for (int j = 0; j < 600; j += 50) {
					sr.rect(i, j, 50, 50);
				}
			}
			sr.end();
		}
	}
	
	public static void end() {
		resetting = true;
	}
	
	public static void reset() {
		units = new ArrayList<Unit>();
		projectiles = new ArrayList<Projectile>();
		blocks = new ArrayList<Block>();
		powerups = new ArrayList<Powerup>();
		units.add(new Gunner(100, 275, Team.RED));
		units.add(new Gunner(1000 - 100 - 42, 275, Team.BLUE));
		
		blocks.add(new Block(0, -25, 1000, 25, Type.INDESTRUCTABLE));
		blocks.add(new Block(-25, 0, 25, 600, Type.INDESTRUCTABLE));
		blocks.add(new Block(0, 600, 1000, 25, Type.INDESTRUCTABLE));
		blocks.add(new Block(1000, 0, 25, 600, Type.INDESTRUCTABLE));
		
		
		initializeField(Field.SECOND);
		
//		for (int i = 0; i < 6; i++) {
//			int x = (int)(Math.random()*800)/50*50;
//			int y = (int)(Math.random()*400)/50*50;
//			blocks.add(new Block(x + 100, y + 100, 50, 50, Type.values()[(int)(Math.random()*Type.values().length)]));
//		}
		
//		blocks.add(new Block(500, 200, 50, 50));
		
//		powerups.add(new Powerup(200, 400, Power.RAPID));
//		powerups.add(new Powerup(750, 400, Power.SHIELD));
	}
	
	public static void initializeField(Field field) {
		for (int i = 0; i < field.field.length; i++) {
			for (int j = 0; j < field.field[0].length; j++) {
				int x = j*50;
				int y = 550 - i*50;
				if (field.field[i][j] != null) {
					if (field.field[i][j] instanceof Type) {
						blocks.add(new Block(x, y, 50, 50, (Type)field.field[i][j]));
					} else if (field.field[i][j] instanceof BlockTemplate){
						blocks.add(new Block(x, y, 50, 50, ((BlockTemplate)field.field[i][j]).type, ((BlockTemplate)field.field[i][j]).variable, ((BlockTemplate)field.field[i][j]).movement));
						if (((BlockTemplate)field.field[i][j]).movement != null) {
							((BlockTemplate)field.field[i][j]).movement.reset();
						}
					} else {
						powerups.add(new Powerup(x, y, (Power)field.field[i][j]));
					}
				}
			}
		}
	}
	
	public static void addProjectile(Projectile projectile) {
		projectiles.add(projectile);
	}
	
	public static ArrayList<Unit> getUnits() {
		return units;
	}
	
	public static ArrayList<Block> getBlocks() {
		return blocks;
	}
	
	public static ArrayList<Powerup> getPowerups() {
		return powerups;
	}
	
	public static TextureRegion[] getScores() {
		return scores;
	}
	
	public static SpriteBatch g() {
		return batch;
	}
	
	public static ShapeRenderer s() {
		return sr;
	}
}
