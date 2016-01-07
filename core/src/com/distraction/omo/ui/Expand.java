package com.distraction.omo.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Expand extends Tile{

	private boolean expanding,contracting;
	
	public Expand(float x, float y, float width, float height) {
		super(x, y, width, height);
		totalWidth+=8;
		totalHeight+=8;
		expanding=true;
	}
	
	public void setContracting(float timer) {
		expanding=false;
		contracting = true;
//		timer=0;
		this.timer=timer;
	}
	
	public void setMaxTimer(float t){
		maxTimer=t;
	}

	@Override
	public void update(float dt) {
		// TODO Auto-generated method stub
		if(expanding){
			super.update(dt);
			
		}else if(contracting){
			timer+=dt;
			width=totalWidth*(1-(timer/maxTimer));
			height=totalHeight*(1-(timer/maxTimer));
			if(width<0)width=0;
			if(height<0)height=0;
		    if(width>totalWidth)width=totalWidth;
			if(height>totalHeight)height=totalHeight;
		}
	}
	
	public boolean isDoneExpanding(){
		return width==totalWidth&&height==totalHeight;
	}
	
	public boolean isDoneContracting(){
		return width==0&&height==0;
	}

	@Override
	public void render(SpriteBatch sb) {
		// TODO Auto-generated method stub
		sb.setColor(0,0,0,1);
		sb.draw(dark, x-width/2, y-height/2,width,height);
		sb.setColor(1, 1,1, 1);
	}
	
	

}
