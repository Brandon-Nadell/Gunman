package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.mygdx.game.Field.Movement;

@SuppressWarnings("serial")
public class Block extends Entity {
	
	private Type type;
	private int variable;
	private Movement movement;
	private ArrayList<Entity> hit;
	private int rotation;
	
	public Block(float x, float y, float width, float height, Type type, int variable, Movement movement) {
		super(x, y, width, height);
		this.type = type;
		this.variable = variable;
		this.movement = movement;
		hit = new ArrayList<Entity>();
	}
	
	public Block(float x, float y, float width, float height, Type type) {
		this(x, y, width, height, type, 0, null);
	}
	
	public interface Typeable { }
	
	public static class BlockTemplate implements Typeable {
		public Type type;
		public int variable;
		public Movement movement;
		
		public BlockTemplate(Type type, int variable) {
			this.type = type;
			this.variable = variable;
		}
		
		public BlockTemplate(Type type, Movement movement) {
			this.type = type;
			this.movement = movement;
		}
	}
	
	public enum Type implements Typeable {
		NORMAL(Color.GREEN) {
			public void effect(Block block, Projectile projectile) {
				block.remove();
				projectile.remove();
			}
			
			public void effect(Block block, Walker walker) {
				block.remove();
				walker.remove();
			}
		},
		INDESTRUCTABLE(Color.DARK_GRAY) {
			public void effect(Block block, Projectile projectile) {
				projectile.remove();
			}
			
			public void effect(Block block, Walker walker) {
				walker.remove();
			}
		},
		MIRROR(Color.RED) {
			public void effect(Block block, Projectile projectile) {
				block.remove();
				if (!block.hit.contains(projectile) && !projectile.getReflected()) {
					projectile.getVelocity().scl(-1);
					projectile.setTeam(projectile.getTeam().opposite());
					block.hit.add(projectile);
				}
				projectile.setReflected(true);
			}
			
			public void effect(Block block, Walker walker) {
				block.remove();
				if (!walker.getReflected()) {
					walker.setTeam(walker.getTeam().opposite());
				}
				walker.setReflected(true);
			}
		},
		MOVING(Color.ROYAL) {
			public void effect(Block block, Projectile projectile) {
				if (!block.hit.contains(projectile)) {
					block.variable++;
					block.rotation = 10;
					block.hit.add(projectile);
					if (block.variable == 3) {
						block.remove();
					}
					projectile.remove();
				}
			}
			
			public void effect(Block block, Walker walker) {
				if (!block.hit.contains(walker)) {
					block.variable++;
					block.rotation = 10;
					block.hit.add(walker);
					if (block.variable == 3) {
						block.remove();
					}
					walker.remove();
				}
			}
			
			public void render(Block block) {
				block.movement.render(block);
				if (block.rotation > 0) {
					block.rotation += 10;
					if (block.rotation == 180) {
						block.rotation = 0;
					}
				}
			}
			
			public void touch(Unit unit) { 
				unit.hurt();
			}
		}
		;
		
		public final Color color;
		
		private Type(Color color) {
			this.color = color;
		}
		
		public void effect(Block block, Projectile projectile) { }
		
		public void effect(Block block, Walker projectile) { }
		
		public void render(Block block) { }

		public void touch(Unit unit) { }
	}
	
	public void render() {
		super.render();
		Gunman.s().begin(ShapeType.Filled);
		Gunman.s().setColor(Color.BLACK);
		float scale = getRemoving() ? (1 + getRemoveTimer()/15f) : 1;
		Gunman.s().rect(x, y, width/2, height/2, width, height, scale, scale, rotation);
		Gunman.s().setColor(type.color);
		Gunman.s().rect(x + 2, y + 2, width/2 - 2, height/2 - 2, width - 4, height - 4, scale, scale, rotation);
//		Gunman.s().rect(x + 2, y + 2, width/2 - 2, height/2 - 2, width - 4, height - 4, scale, scale, rotation, type.color, type.color.cpy().lerp(Color.WHITE, .3f), type.color, type.color.cpy().lerp(Color.WHITE, .3f));
		Gunman.s().end();
		type.render(this);
	}
	
	public Type getType() {
		return type;
	}
	
}
