package com.distraction.omo;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.distraction.omo.handler.Content;
import com.distraction.omo.states.GSM;
import com.distraction.omo.states.MenuState;
import com.distraction.omo.states.PlayState;
import com.distraction.omo.states.PlayState.Difficulty;

public class Omo extends ApplicationAdapter {
	
	public static int WIDTH=480;
	public static int HEIGHT=800;
	public static String TITLE="Omo"; 
	public static Content res;
	
	SpriteBatch sb;
	GSM gsm;
	
	@Override
	public void create () {
		
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		
		res=new Content();
//		res.loadAtlas("pack.pack", "pack");
		res.loadAtlas("gack.pack", "pack");
		gsm=new GSM();
		sb=new SpriteBatch();
//		gsm.push(new PlayState(gsm,Difficulty.INSANE));
		gsm.push(new MenuState(gsm));
	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.render(sb);
	}
}
