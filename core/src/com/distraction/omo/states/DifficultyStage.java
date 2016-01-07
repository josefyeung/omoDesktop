package com.distraction.omo.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.distraction.omo.Omo;
import com.distraction.omo.states.PlayState.Difficulty;
import com.distraction.omo.ui.Graphic;
import com.distraction.omo.ui.TextImage;


public class DifficultyStage extends State{

	private Array<TextImage> buttons;
	private Graphic back;
	
	public DifficultyStage(GSM gsm) {
		super(gsm);
		// TODO Auto-generated constructor stub
		String[] texts={"easy","normal","hard","insane"};
		buttons=new Array<TextImage>();
		for(int i=0;i<texts.length;i++){
			buttons.add(new TextImage(texts[i], Omo.WIDTH/2, Omo.HEIGHT/2+100-70*i));
		}
		back=new Graphic(Omo.res.getAtlas("pack").findRegion("back"), Omo.WIDTH/2, 100);
	}

	@Override
	public void hangInput() {
		// TODO Auto-generated method stub
		if(Gdx.input.justTouched()){
			mouse.x=Gdx.input.getX();
			mouse.y=Gdx.input.getY();
			cam.unproject(mouse);
			for(int i=0;i<buttons.size;i++){
				if(buttons.get(i).contains(mouse.x, mouse.y)){
					gsm.set(new PlayState(gsm, Difficulty.values()[i]));
				}
			}
			if(back.contains(mouse.x,  mouse.y)){
				gsm.set(new MenuState(gsm));
			}
		}
	}

	@Override
	public void update(float dt) {
		// TODO Auto-generated method stub
		hangInput();
	}

	@Override
	public void render(SpriteBatch sb) {
		// TODO Auto-generated method stub
		sb.setProjectionMatrix(cam.combined);
		sb.begin();
		for(TextImage img:buttons){
			img.render(sb);
		}
		back.render(sb);
		sb.end();
	}


	
}
