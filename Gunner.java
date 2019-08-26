package com.mygdx.game;

import java.util.ArrayList;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

@SuppressWarnings("serial")
public class Gunner extends Unit {
	
	private int gunCooldown;
	private int walkerCooldown;
	private int hurtCooldown;
	private float health;
	private Vector2 velocity;
	private ArrayList<Powerup> powerups;
	private static final int HEALTH_MAX = 5;
	private static final int GUN_COOLDOWN_MAX = 40;
	private static final int WALKER_COOLDOWN_MAX = 180;
	private static final int HURT_COOLDOWN_MAX = 40;
	
	public Gunner(float x, float y, Team team) {
		super(x, y, team);
		health = HEALTH_MAX;
		textureIndex = 0;
		velocity = new Vector2(0, 0);
		powerups = new ArrayList<Powerup>();
	}
	
	public void render() {
		super.render();
		moveByKeys();
		changeX(velocity.x);
		changeY(velocity.y);
		updateForce(velocity, .4f);
		for (int i = 0; i < powerups.size(); i++) {
			powerups.get(i).getPower().effect(this);
			if (powerups.get(i).use()) {
				powerups.remove(i);
				i--;
			}
		}
		//block touching
		for (Block block : Gunman.getBlocks()) {
			if (!block.getRemoving() && intersects(block)) {
				unCollide(block);
				block.getType().touch(this);
				resetVelocity();
			}
		}
		for (Unit unit : Gunman.getUnits()) {
			if (unit != this && intersects(unit) && !unit.getRemoving()) {
				unCollide(unit);
				resetVelocity();
			}
		}
		for (Powerup powerup : Gunman.getPowerups()) {
			if (intersects(powerup) && !powerup.getRemoving()) {
				powerup.remove();
				addPowerup(powerup);
			}
		}
		decrementGunCooldown();
		if (walkerCooldown > 0) {
			walkerCooldown--;
		}
		if (gunCooldown == 0 && Gdx.input.isKeyPressed(team.keys[4])) {
			Gunman.addProjectile(new Projectile((float)getCenterX() - 15 + (float)getWidth()/2*team.direction, (float)getCenterY() - 3, 30, 6, new Vector2(10*team.direction, 0), team));
			gunCooldown = GUN_COOLDOWN_MAX;
		}
		if (walkerCooldown == 0 && Gdx.input.isKeyPressed(team.keys[5])) {
			Gunman.getUnits().add(new Walker(x + width*team.direction, y, team));
			walkerCooldown = WALKER_COOLDOWN_MAX;
		}
		Gunman.g().begin();
		if (hurtCooldown > 0) {
			Gunman.g().setColor(Color.RED);
		}
		Gunman.g().draw(team.textures[textureIndex], x, y, width, height);
		Gunman.g().setColor(Color.WHITE);
		Gunman.g().end();
		//healthbar
		if (health != (int)health) {
			health = (health*10 - 1)/10f;
		}
		Gunman.s().begin(ShapeType.Filled);
		Gunman.s().setColor(Color.BLACK);
		Gunman.s().rect((team == Team.RED ? 20 : 780) - 3, 20 - 3, 200 + 6, 20 + 6);
		Gunman.s().setColor(team.color);
		Gunman.s().rect(team == Team.RED ? 20 : (980 - 200f*health/HEALTH_MAX), 20, 200f*health/HEALTH_MAX, 20);
		Gunman.s().end();
		//walker cooldown
		Gunman.s().begin(ShapeType.Filled);
		Gunman.s().setColor(Color.BLACK);
		Gunman.s().rect((team == Team.RED ? 20 : 780) - 3, 40 - 3, 200 + 6, 10 + 6);
		Gunman.s().setColor(Color.GREEN);
		Gunman.s().rect(team == Team.RED ? 20 : (980 + 200f*(walkerCooldown - WALKER_COOLDOWN_MAX)/WALKER_COOLDOWN_MAX) , 40, 200f*(WALKER_COOLDOWN_MAX - walkerCooldown)/WALKER_COOLDOWN_MAX, 10);
		Gunman.s().end();
		//scores
		Gunman.g().begin();
		Gunman.g().draw(Gunman.getScores()[team.score], team == Team.RED ? 20 : 930, 530);
		Gunman.g().end();
		if (hurtCooldown > 0) {
			hurtCooldown--;
		}
	}
	
	public void moveByKeys() {
		if (Gdx.input.isKeyPressed(team.keys[3])) { //D - right
			velocity.x = 5;
		}
		if (Gdx.input.isKeyPressed(team.keys[1])) { //A - left
			velocity.x = -5;
		}
		if (Gdx.input.isKeyPressed(team.keys[0])) {//W - up
			velocity.y = 5;
		}
		if (Gdx.input.isKeyPressed(team.keys[2])) {//S - down
			velocity.y = -5;
		}
	}
	
	public static void updateForce(Vector2 force, float friction) {
		force.x -= friction*Math.abs(Math.cos(Math.toRadians(force.angle())))*Math.signum(force.x);
		force.y -= friction*Math.abs(Math.sin(Math.toRadians(force.angle())))*Math.signum(force.y);
		if (nearZero(force.x, friction)) {
			force.x = 0;
		}
		if (nearZero(force.y, friction)) {
			force.y = 0;
		}
	}
	
	public static boolean nearZero(float value, float friction) {
		return Math.abs(value) < friction;
	}
	
	public void resetVelocity() {
		velocity.set(0, 0);
	}
	
	public void hurt() {
		if (hurtCooldown == 0) {
			health = (health*10 - 1)/10f;
			hurtCooldown = HURT_COOLDOWN_MAX;
			if ((int)health == 0) {
				team.opposite().incrementScore();
				Gunman.end();
			}
		}
	}
	
	public void decrementGunCooldown() {
		if (gunCooldown > 0) {
			gunCooldown--;
		}
	}
	
	public void addPowerup(Powerup power) {
		powerups.add(power);
	}
	
	public float getHealth() {
		return health;
	}
	
	public void setHealth(float health) {
		this.health = health;
	}
	
	public void resetHurtCooldown() {
		hurtCooldown = 0;
	}
	
}
