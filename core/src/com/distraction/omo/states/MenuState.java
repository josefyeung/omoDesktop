package com.distraction.omo.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.distraction.omo.Omo;
import com.distraction.omo.states.PlayState.Difficulty;
import com.distraction.omo.states.TransitionState.Type;
import com.distraction.omo.ui.Graphic;
import com.distraction.omo.ui.TextImage;

public class MenuState extends State{

	Graphic title;
	TextImage play;
	public MenuState(GSM gsm) {
		super(gsm);
		title=new Graphic(Omo.res.getAtlas("pack").findRegion("omo"), 
				Omo.WIDTH/2,
				Omo.HEIGHT/2+100);
		play=new TextImage("PLAY",Omo.WIDTH/2, Omo.HEIGHT/2);
	}

	@Override
	public void hangInput() {
		if(Gdx.input.justTouched()){
			mouse.x=Gdx.input.getX();
			mouse.y=Gdx.input.getY();
			cam.unproject(mouse);
			if(play.contains(mouse.x,mouse.y)){
//				gsm.set(new PlayState(gsm, Difficulty.NORMAL));
//				gsm.set(new DifficultyStage(gsm));
				gsm.set(new TransitionState(gsm, this, new DifficultyStage(gsm), Type.EXPAND));
			}
		}
	}

	@Override
	public void update(float dt) {
		hangInput();
	}

	@Override
	public void render(SpriteBatch sb) {
		sb.setProjectionMatrix(cam.combined);
		sb.begin();
		title.render(sb);
		play.render(sb);
		sb.end();
	}

}
