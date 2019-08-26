package com.mygdx.game;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class Entity extends Rectangle2D.Float {
	
	private int removeTimer;
	private int removeDirection;
	private boolean removing;
	private boolean remove;
	
	public Entity(float x, float y, float width, float height) {
		super(x, y, width, height);
	}
	
	public void render() {
		if (removing) {
			removeTimer += removeDirection;
			if (removeTimer == 5) {
				removeDirection = -2;
			}
			if (removeTimer == -15) {
				remove = true;
			}
		}
	}
	
	public void unCollide(Entity entity) {
		if (entity != null) {
			Rectangle2D.Float collision = (Float)createIntersection(entity);
			if (collision.height < collision.width && getMaxY() < entity.getMaxY()) {
				setY(entity.y - height);
	        }
	        if (collision.height < collision.width && y > entity.y) {
	        	setY((float)entity.getMaxY());
	        }
			if (collision.height > collision.width && x > entity.x) {
				setX((float)entity.getMaxX());
			}
	        if (collision.height > collision.width && getMaxX() < entity.getMaxX()) {
	        	setX(entity.x - width);
	        }
		}
	}
	
	public boolean touchingAny(ArrayList<Entity> array) {
		for (Entity entity : array) {
			if (intersects(entity)) {
				return true;
			}
		}
		return false;
	}
	
	public void setX(float x) {
		setRect(x, this.y, width, height);
	}
	
	public void setY(float y) {
		setRect(x, y, width, height);
	}
	
	public void changeX(float x) {
		setRect(this.x + x, this.y, width, height);
	}
	
	public void changeY(float y) {
		setRect(x, this.y + y, width, height);
	}
	
	public boolean getRemove() {
		return remove;
	}
	
	public void setRemove(boolean remove) {
		this.remove = remove;
	}
	
	public void remove() {
		if (!removing) {
			removing = true;
			removeDirection = 1;
		}
	}
	
	public int getRemoveTimer() {
		return removeTimer;
	}
	
	public boolean getRemoving() {
		return removing;
	}
}
