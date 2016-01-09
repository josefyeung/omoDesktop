package com.distraction.omo.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.distraction.omo.Omo;
import com.distraction.omo.ui.Expand;

public class TransitionState extends State{

	public enum Type{
		BLACK_FADE,
		EXPAND
	};
	
	private State prev;
	private State next;
	TextureRegion dark;
	
	//black_pade suff
	Type type;
	private float maxTime,timer;
	float alpha;
	
	//expand suff
	private Expand[][] expands;
	private boolean doneExpanding;
	private boolean doneContracting;
	
	public TransitionState(GSM gsm,State prev,State next,Type type) {
		super(gsm);
		this.prev=prev;
		this.next=next;
		this.type=type;
		dark=Omo.res.getAtlas("pack").findRegion("dark");
		if(type==Type.BLACK_FADE){
			maxTime=1;
		}else if(type==Type.EXPAND){
			expands=new Expand[10][6];
			int size=80;
			for(int row=0;row<expands.length;row++){
				for(int col=0;col<expands[0].length;col++){
					expands[row][col]=new Expand(col*size+size/2, row*size+size/2, size, size);
					expands[row][col].setMaxTimer(0.5f);
					expands[row][col].setTimer((-(expands.length-row)-col)*0.05f);
				}
			}
		}
	}

	@Override
	public void handleInput() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(float dt) {
		// TODO Auto-generated method stub
		timer+=dt;
		if(type==Type.BLACK_FADE){
			if(timer>=maxTime){
				gsm.set(next);
			}
		}else if(type==Type.EXPAND){
			if(!doneExpanding){
				boolean okay=true;
				for(int row=0;row<expands.length;row++){
					for(int col=0;col<expands[0].length;col++){
						expands[row][col].update(dt);
						if(!expands[row][col].isDoneExpanding()){
							okay=false;
						}
					}
				}
				if(okay&&!doneExpanding){
					doneExpanding=true;
					for(int row=0;row<expands.length;row++){
						for(int col=0;col<expands[0].length;col++){
							expands[row][col].setContracting((-(expands.length-row)-col)*0.05f);
						}
					}
				}
			}else {
				boolean okay=true;
				for(int row=0;row<expands.length;row++){
					for(int col=0;col<expands[0].length;col++){
						expands[row][col].update(dt);
						if(!expands[row][col].isDoneContracting()){
							okay=false;
						}
					}
				}
				if(okay&&!doneContracting){
					doneContracting=true;
					gsm.set(next);
				}
			}
			
		}
	}

	@Override
	public void render(SpriteBatch sb) {
		// TODO Auto-generated method stub
		if(type==Type.BLACK_FADE){
			if(timer<maxTime/2){
				alpha=timer/(maxTime/2);
				prev.render(sb);
			}else{
				alpha=1-timer/(maxTime/2);
				next.render(sb);
			}
			sb.setColor(0, 0, 0, alpha);
			sb.setProjectionMatrix(cam.combined);
			sb.begin();
			sb.draw(dark, 0, 0,Omo.WIDTH,Omo.HEIGHT);
			sb.end();
			sb.setColor(1, 1, 1, 1);
		}
		else if(type==Type.EXPAND){
			if(!doneExpanding){
				prev.render(sb);
			}else{
				next.render(sb);
			}
			sb.setProjectionMatrix(cam.combined);
			sb.begin();
			for(int row=0;row<expands.length;row++){
				for(int col=0;col<expands[0].length;col++){
					expands[row][col].render(sb);
				}
			}
			sb.end();
		}
	}
	
	

}
