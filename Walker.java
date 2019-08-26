package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

@SuppressWarnings("serial")
public class Walker extends Unit {
	
	private boolean reflected;
	
	public Walker(float x, float y, Team team) {
		super(x, y, team);
		textureIndex = 1;
	}
	
	public void render() {
		super.render();
		if (!getRemoving()) {
			changeX(3*team.direction);
		}
		//block touching
		for (Block block : Gunman.getBlocks()) {
			if (!block.getRemoving() && intersects(block)) {
				block.getType().effect(block, this);
			}
		}
		for (Unit unit : Gunman.getUnits()) {
			if (unit != this && intersects(unit)) {
				remove();
				if (unit.getTeam() != team) {
					unit.hurt();
				}
			}
		}
		Gunman.g().begin();
		Gunman.g().setColor(Color.WHITE);
		float scale = getRemoving() ? (1 + getRemoveTimer()/15f) : 1;
		Gunman.g().draw(new TextureRegion(team.textures[textureIndex]), x, y, width/2, height/2, width, height, scale, scale, 0);
		Gunman.g().end();
	}
	
	public void hurt() {
		remove();
	}
	
	public void explode() {
		//TOOD
	}
	
	public void remove() {
		super.remove();
		explode();
	}
	
	public boolean getReflected() {
		return reflected;
	}
	
	public void setReflected(boolean reflected) {
		this.reflected = reflected;
	}

}
