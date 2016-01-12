package com.distraction.omo.states;

import java.util.Stack;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class GSM {

	private Stack<State> states;
	//jfy
    public BitmapFont font;
    public BitmapFont font24;
    //jfy to add sound in assets
    public AssetManager assets;
	
	public GSM() {
		// TODO Auto-generated constructor stub
		states=new Stack<State>();
		
		//jfy: Use LibGDX's default Arial font.
        font = new BitmapFont();
        //jfy
        initFonts();
                
        //jfy
        asynLoadAssets();

	}
	
	private void asynLoadAssets() {
		assets = new AssetManager();
		assets.load("sounds/asubuhi0.mp3", Sound.class);
    	assets.finishLoading();
	}

	private void initFonts() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Arcon.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();

        params.size = 45;
        //params.color = Color.BLACK;
        font24 = generator.generateFont(params);
    }
	
	
	
	
	
	public void push(State state){
		states.push(state);
	}
	
	public State pop(){
		return states.pop();
	}
	
	public void set(State state){
		states.pop();
		states.push(state);
	}
	
	public void update(float dt){
		states.peek().update(dt);
	}
	
	public void render(SpriteBatch sb){
		states.peek().render(sb);
	}
	
	//jfy
	public void dispose() {
        font.dispose();
    	font24.dispose();
    	assets.dispose();
    }
	
}
