package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Unit.Team;

@SuppressWarnings("serial")
public class Projectile extends Entity {
	
	private Vector2 velocity;
	private Team team;
	private boolean reflected;
	
	public Projectile(float x, float y, float width, float height, Vector2 velocity, Team team) {
		super(x, y, width, height);
		this.velocity = velocity;
		this.team = team;
	}
	
	public void render() {
		changeX(velocity.x);
		changeY(velocity.y);
		Gunman.s().begin(ShapeType.Filled);
		Gunman.s().setColor(Color.BLACK);
		Gunman.s().rect(x, y, width, height);
		Gunman.s().setColor(team.color);
		Gunman.s().rect(x + 1, y + 1, width - 2, height - 2);
		Gunman.s().end();
		
		for (Unit unit : Gunman.getUnits()) {
			if (unit.getTeam() != team && intersects(unit)) {
				unit.hurt();
			}
		}
		for (Block block : Gunman.getBlocks()) {
			if (intersects(block)) {
				block.getType().effect(block, this);
			}
		}
		reflected = false;
	}
	
	public void remove() {
		setRemove(true);
	}
	
	public Vector2 getVelocity() {
		return velocity;
	}
	
	public Team getTeam() {
		return team;
	}
	
	public void setTeam(Team team) {
		this.team = team;
	}
	
	public boolean getReflected() {
		return reflected;
	}
	
	public void setReflected(boolean reflected) {
		this.reflected = reflected;
	}
	
}
