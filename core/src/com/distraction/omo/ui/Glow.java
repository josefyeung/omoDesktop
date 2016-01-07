package com.distraction.omo.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Glow extends Tile{
	
	float minWidth;
	float minHeight;
	private boolean remove;
	private float alpha;
	float speed=200;

	public Glow(float x, float y, float width, float height) {
		super(x, y, width, height);
		this.width=width;
		this.height=height;
	}
	
	
	public boolean shoudRemove(){
		return remove;
	}
	
	public void update(float dt){
		timer+=dt;
		width+=dt*speed;
		height+=dt*speed;
		if(timer>maxTimer){
			remove=true;
		}
		
	}


	@Override
	public void render(SpriteBatch sb) {
		// TODO Auto-generated method stub
		alpha=1-timer/maxTimer;
		sb.setColor(1, 1, 1, alpha);
		sb.draw(light, x-width/2, y-height/2,width,height);
		sb.setColor(1, 1, 1, 1);
	}
	
	

}
