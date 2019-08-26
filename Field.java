package com.mygdx.game;

import com.mygdx.game.Block.Type;
import com.mygdx.game.Block.BlockTemplate;
import com.mygdx.game.Block.Typeable;
import com.mygdx.game.Powerup.Power;

public enum Field {
	//a field is 20 x 12
	DEFAULT(new Typeable[][] {
		{ null, null, null, null, Type.NORMAL, null, null, null, null, Type.MIRROR, Type.MIRROR, null, null, null, null, Type.NORMAL, null, null, null, null },
		{ null, null, null, null, Type.NORMAL, null, null, null, null, Type.MIRROR, Type.MIRROR, null, null, null, null, Type.NORMAL, null, null, null, null },
		{ null, null, null, Power.RAPID, Type.NORMAL, null, null, null, null, Type.MIRROR, Type.MIRROR, null, null, null, null, Type.NORMAL, Power.SHIELD, null, null, null },
		{ null, null, null, null, null,        null, null, new BlockTemplate(Type.MOVING, new Movement(new int[][] { { -1, 0, 50 }, { 0, 1, 50 }, { 1, 0, 50 }, { 0, -1, 50 } })), null, Type.MIRROR, Type.MIRROR, null, null, null, null, null,        null, null, null, null },
		{ null, null, null, null, null,        null, null, null, null, Type.MIRROR, Type.MIRROR, null, null, null, null, null,        null, null, null, null },
		{ null, null, null, null, Type.NORMAL, null, null, null, null, Type.MIRROR, Type.MIRROR, null, null, null, null, Type.NORMAL, null, null, null, null },
		{ null, null, null, null, Type.NORMAL, null, null, null, null, Type.MIRROR, Type.MIRROR, null, null, null, null, Type.NORMAL, null, null, null, null },
		{ null, null, null, null, null,        null, null, null, null, Type.MIRROR, Type.MIRROR, null, null, null, null, null,        null, null, null, null },
		{ null, null, null, null, null,        null, null, null, null, Type.MIRROR, Type.MIRROR, null, null, null, null, null,        null, null, null, null },
		{ null, null, null, null, Type.NORMAL, null, null, null, null, Type.MIRROR, Type.MIRROR, null, null, null, null, Type.NORMAL, null, null, null, null },
		{ null, null, null, null, Type.NORMAL, null, null, null, null, Type.MIRROR, Type.MIRROR, null, null, null, null, Type.NORMAL, null, null, null, null },
		{ null, null, null, null, Type.NORMAL, null, null, null, null, Type.MIRROR, Type.MIRROR, null, null, null, null, Type.NORMAL, null, null, null, null }
	}),
	SECOND(new Typeable[][] {
		{ Type.MIRROR, Type.MIRROR, null, null, null, null, null, null, Type.NORMAL, Type.NORMAL, Type.NORMAL, Type.NORMAL, null, null, null, null, null, null, Type.MIRROR, Type.MIRROR },
		{ Type.MIRROR, null, null, null, null, null, null, null, Type.NORMAL, Type.INDESTRUCTABLE, Type.INDESTRUCTABLE, Type.NORMAL, null, null, null, null, null, null, null, Type.MIRROR },
		{ null, null, null, null, null, new BlockTemplate(Type.MOVING, new Movement(new int[][] { { 2, 0, 225 }, { 0, -2, 175 }, { -2, 0, 225 }, { 0, 2, 175 } })), null, null, null, null, null, null, null, null, null, null, null, null, null, null },
		{ null, null, null, null, Type.NORMAL, null, null, null, null, Type.NORMAL, Type.NORMAL, null, null, null, null, Type.NORMAL, null, null, null, null },
		{ null, null, null, null, Type.NORMAL, null, null, null, null, Type.MIRROR, Type.MIRROR, null, null, null, null, Type.NORMAL, null, null, null, null },
		{ Type.MIRROR, null, null, null, null, null, null, Type.NORMAL, Type.MIRROR, Type.INDESTRUCTABLE, Type.INDESTRUCTABLE, Type.MIRROR, Type.NORMAL, null, null, null, null, null, null, Type.MIRROR },
		{ Type.MIRROR, null, null, null, null, null, null, Type.NORMAL, Type.MIRROR, Type.INDESTRUCTABLE, Type.INDESTRUCTABLE, Type.MIRROR, Type.NORMAL, null, null, null, null, null, null, Type.MIRROR },
		{ null, null, null, null, Type.NORMAL, null, null, null, null, Type.MIRROR, Type.MIRROR, null, null, null, null, Type.NORMAL, null, null, null, null },
		{ null, null, null, null, Type.NORMAL, null, null, null, null, Type.NORMAL, Type.NORMAL, null, null, null, null, Type.NORMAL, null, null, null, null },
		{ null, null, null, null, null, null, null, null, null, null, null, null, null, null, new BlockTemplate(Type.MOVING, new Movement(new int[][] { { -2, 0, 225 }, { 0, 2, 175 }, { 2, 0, 225 }, { 0, -2, 175 } })), null, null, null, null, null },
		{ Type.MIRROR, null, null, null, null, null, null, null, Type.NORMAL, Type.INDESTRUCTABLE, Type.INDESTRUCTABLE, Type.NORMAL, null, null, null, null, null, null, null, Type.MIRROR },
		{ Type.MIRROR, Type.MIRROR, null, null, null, null, null, null, Type.NORMAL, Type.NORMAL, Type.NORMAL, Type.NORMAL, null, null, null, null, null, null, Type.MIRROR, Type.MIRROR },
	}),
	;
	
	public Typeable[][] field;
	
	private Field(Typeable[][] field) {
		this.field = field;
	}
	
	public static class Movement {
		private int[][] directions;
		private int timer;
		private int index;
		
		public Movement(int[]... directions) {
			this.directions = directions;
		}
		
		public void render(Block block) {
			block.changeX(directions[index][0]);
			block.changeY(directions[index][1]);
			if (timer == directions[index][2]) {
				timer = 0;
				index++;
				if (index == directions.length) {
					index = 0;
				}
			} else {
				timer++;
			}
		}
		
		public void reset() {
			timer = 0;
			index = 0;
		}
	}
	

}
