package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.mygdx.game.Block.Typeable;

@SuppressWarnings("serial")
public class Powerup extends Entity {
	
	private Power power;
	private int duration;
	
	public Powerup(int x, int y, Power power) {
		super(x, y, 50, 50);
		this.power = power;
		duration = 180;
	}
	
	public enum Power implements Typeable {
		RAPID('R') {
			public void effect(Gunner gunner) {
				gunner.decrementGunCooldown();
				gunner.decrementGunCooldown();
			}
		},
		SHIELD('S') {
			public void effect(Gunner gunner) {
				if (gunner.getHealth() != (int)gunner.getHealth()) {
					gunner.setHealth((int)gunner.getHealth() + 1);
					gunner.resetHurtCooldown();
				}
			}
		}
		;
		
		public final char letter;
		
		private Power(char letter) {
			this.letter = letter;
		}
		
		public void effect(Gunner gunner) { }
	}
	
	public void render() {
		super.render();
		Gunman.s().begin(ShapeType.Filled);
		Gunman.s().setColor(Color.BLACK);
		float scale = getRemoving() ? (1 + getRemoveTimer()/15f) : 1;
		Gunman.s().ellipse(x + width/2*(1 - scale), y + height/2*(1 - scale), width * scale, height * scale);
		Gunman.s().setColor(Color.YELLOW);
		Gunman.s().ellipse(x + 2 + width/2*(1 - scale), y + 2+ height/2*(1 - scale), (width - 4)* scale, (height - 4)* scale);
//		Gunman.s().rect(x, y, width/2, height/2, width, height, scale, scale, 0);
//		Gunman.s().setAutoShapeType(true);
//		Gunman.s().set(ShapeType.Line);
//		Gunman.s().rect(x, y, width, height);
		Gunman.s().end();
		BitmapFont f = new BitmapFont();
		f.setColor(Color.BLACK);
		f.getData().setScale((float)Math.max(3*scale, .1));
		Gunman.g().begin();
		f.draw(Gunman.g(), power.letter + "", x + 21 + 21*scale, y + 21 + 21*scale, 0, 0, false);
		Gunman.g().end();
	}
	
	public boolean use() {
		return --duration == 0;
	}
	
	public Power getPower() {
		return power;
	}

}
