package com.distraction.omo.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.distraction.omo.Omo;
import com.distraction.omo.states.TransitionState.Type;
import com.distraction.omo.ui.TextImage;

public class ScoreState extends State{

	private TextImage image;
	public ScoreState(GSM gsm,int score) {
		super(gsm);
		image=new TextImage(String.valueOf(score), Omo.WIDTH/2, Omo.HEIGHT/2);
	}

	@Override
	public void handleInput() {
		if(Gdx.input.justTouched()){
//			gsm.set(new MenuState(gsm));
			gsm.set(new TransitionState(gsm, this, new MenuState(gsm), Type.BLACK_FADE));
		}
	}

	@Override
	public void update(float dt) {
		handleInput();
	}

	@Override
	public void render(SpriteBatch sb) {
		sb.setProjectionMatrix(cam.combined);
		sb.begin();
		image.render(sb);
		sb.end();
	}
	
	

}
