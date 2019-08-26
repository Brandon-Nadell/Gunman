package com.mygdx.game;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

@SuppressWarnings("serial")
public abstract class Unit extends Entity {
	
	protected Team team;
	protected int textureIndex;
	
	public Unit(float x, float y, Team team) {
		super(x, y, 42, 50);
		this.team = team;
	}
	
	public enum Team {
		RED(Color.RED, new Texture[] { new Texture("gunner.png"), new Texture("walker.png") }, 1, new int[] { Keys.W, Keys.A, Keys.S, Keys.D, Keys.SHIFT_LEFT, Keys.C } ),
		BLUE(Color.BLUE, new Texture[] { new Texture("gunnerBlue.png"), new Texture("walkerBlue.png") }, -1, new int[] { Keys.UP, Keys.LEFT, Keys.DOWN, Keys.RIGHT, Keys.SHIFT_RIGHT, Keys.SLASH } );
		
		public final Color color;
		public final Texture[] textures;
		public final int[] keys;
		public final int direction;
		public int score;
		
		private Team(Color color, Texture[] textures, int direction, int[] keys) {
			this.color = color;
			this.textures = textures;
			this.direction = direction;
			this.keys = keys;
		}
		
		public Team opposite() {
			return this == Team.RED ? Team.BLUE : Team.RED;
		}
		
		public void incrementScore() {
			score++;
			if (score == 5) {
				//WIN
				reset();
			}
		}
		
		public static void reset() {
			for (Team team : values()) {
				team.score = 0;
			}
		}
	}
	
	public abstract void hurt();
	
	public Team getTeam() {
		return team;
	}
	
	public void setTeam(Team team) {
		this.team = team;
	}

}
