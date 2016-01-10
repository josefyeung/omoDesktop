package com.distraction.omo.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.distraction.omo.Omo;
import com.distraction.omo.states.TransitionState.Type;
import com.distraction.omo.ui.Glow;
import com.distraction.omo.ui.Graphic;
import com.distraction.omo.ui.Score;
import com.distraction.omo.ui.Tile;

public class PlayState extends State {

	//jfy to copy the font drawing from 15Slidez
	public BitmapFont font24;
	
	
	private Tile[][] tiles;
	private int tileSize;
	float boardOffset;
	private static final int MAX_FINGERs=2;
	
	private Array<Tile> finished;
	private Array<Tile> selected;
	
	private boolean showing;
	private float timer;
	//jfy
	private int tileCounter;
	float scoreTimer;
	
	private Score score;
	
	private TextureRegion light;
	private TextureRegion dark;
	
	public enum Difficulty{
		EASY,
		NORMAL,
		HARD,
		INSANE
	}
	
	int level;
	int maxLevel;
	Difficulty difficulty;
	int[] args;
	private Array<Glow> glows;
	
	private float wrongTimer;
	
	boolean done;
	
	private Graphic back;

	public PlayState(GSM gsm,Difficulty df) {
		super(gsm);
		this.difficulty=df;
		level=1;
		finished=new Array<Tile>();
		selected=new Array<Tile>();
		args=getArgs();
		createBoard(args[0], args[1]);
		createFinished(args[2]);
		
		score=new Score(Omo.WIDTH/2, Omo.HEIGHT-50);
		light=Omo.res.getAtlas("pack").findRegion("light");
		dark=Omo.res.getAtlas("pack").findRegion("dark");
		back=new Graphic(Omo.res.getAtlas("pack").findRegion("back"), Omo.WIDTH/2, 100);
		
		glows=new Array<Glow>();
	}
	
	private int[] getArgs(){
		int[] res=new int[3];
		if(difficulty==Difficulty.EASY){
			res[0]=3;
			res[1]=3;
			if(level>=1&&level<=3){
				res[2]=3;
			}
			if(level==4||level==5){
				res[2]=4;
			}
			maxLevel=5;
		}
		if(difficulty==Difficulty.NORMAL){
			res[0]=4;
			res[1]=4;
			if(level==1||level==2){
				res[2]=4;
			}
			if(level==3||level==4){
				res[2]=5;
			}
			if(level==5||level==6){
				res[2]=6;
			}
			maxLevel=6;
		}
		if(difficulty==Difficulty.HARD){
			res[0]=5;
			res[1]=5;
			if(level==1||level==2){
				res[2]=6;
			}
			if(level==3||level==4){
				res[2]=7;
			}
			if(level==5||level==6){
				res[2]=8;
			}
			if(level==7||level==8){
				res[2]=9;
			}
			maxLevel=8;
		}
		if(difficulty==Difficulty.INSANE){
			res[0]=6;
			res[1]=6;
			if(level==1||level==2){
				res[2]=7;
			}
			if(level==3||level==4){
				res[2]=8;
			}
			if(level==5||level==6){
				res[2]=9;
			}
			if(level==7||level==8){
				res[2]=10;
			}
			if(level==9||level==10){
				res[2]=11;
			}
			maxLevel=10;
		}
		return res;
	}
	
	private void createBoard(int numRows,int numCols){
		//jfy: tiles = new Tile[numRows][numCols];
		tiles = new Tile [3][3];
		tileSize = Omo.WIDTH / tiles[0].length;
		boardOffset = (Omo.HEIGHT - (tileSize * tiles.length)) / 2;

		for (int row = 0; row < tiles.length; row++) {
			for (int col = 0; col < tiles[0].length; col++) {
				tiles[row][col] = new Tile(
						col * tileSize+tileSize/2,
						row * tileSize+boardOffset+tileSize/2,
						tileSize, 
						tileSize);
				
				tiles[row][col].setTimer((-(tiles.length-row)-col)*0.09f);
			}
		}
	}
	
	/****
	 * jfy's checkShowing: to show the lit-up tiles one by one
	 * @param dt
	 */
	private void checkShowing(float dt){
		if(showing){		
			timer+=dt;
			if(timer > 5.5){      //jfy: the last tile has 0.9 to persist
					timer = 0;
					showing=false;
					for(int i=0;i<finished.size;i++){
						finished.get(i).setSelected(false);
					}
			} else {
				finished.get(Math.round(timer)).setSelected(true);		
			}
		}
	}
	
	
	private void createFinished(int numTilestoLight){
		showing=true;
		timer=0;
		finished.clear();
		selected.clear();
		scoreTimer=5;
		wrongTimer=0;
		tileCounter=0;
		///jfy: for(int i=0;i<numTilestoLight;i++){
		for(int i=0;i<6;i++){
			int row=0;
			int col=0;
			do{
				row=MathUtils.random(tiles.length-1);
				col=MathUtils.random(tiles[0].length-1);
				//jfy
				//finished.add(tiles[row][col]);
				//tiles[row][col].setSelected(true);
			}while(finished.contains(tiles[row][col], true));
			finished.add(tiles[row][col]);
			//jfy: tiles[row][col].setSelected(true);
		}	
	}
	
	private boolean isFinished(){
		for(int i=0;i<finished.size;i++){
			Tile tf=finished.get(i);
			if(!selected.contains(tf, true)){
				return false;
			}
		}
		return true;
	}
	
	private void done() {
//		gsm.set(new MenuState(gsm));
//		gsm.set(new ScoreState(gsm, score.getScore()));
		gsm.set(new TransitionState(gsm, this, new ScoreState(gsm, score.getScore()), Type.EXPAND));
	}

	@Override
	public void hangInput() {
		for(int i=0;i<MAX_FINGERs;i++){
			if(!showing && !done && Gdx.input.isTouched(i)){
				mouse.x=Gdx.input.getX(i);
				mouse.y=Gdx.input.getY(i);
				cam.unproject(mouse);
				
				checkAnswer();
			}
			
			//jfy Back button 
			if(Gdx.input.justTouched()){
				mouse.x=Gdx.input.getX(i);
				mouse.y=Gdx.input.getY(i);
				cam.unproject(mouse);
				if(back.contains(mouse.x, mouse.y)){
					gsm.set(new TransitionState(gsm, this, new DifficultyStage(gsm), Type.BLACK_FADE));
				}
			}
		}
	}

	private void checkAnswer() {
		for(Tile[] ts:tiles){
			for(Tile t:ts){
				if(t.contains(mouse.x, mouse.y)){
					if(!t.isSelected()){
						t.setSelected(true);
						selected.add(t);

						//jfy
						checkAgainstPuzzle();
						//jfy
						tileCounter += 1;
						
						if(isFinished()){  //jfy check if all puzzles lit up previously have been chosen
							done=true;
							level++;
							int inc=(int)(scoreTimer*10);
							int dec=5*(selected.size-finished.size);
							for(int j=0;j<selected.size;j++){
								Tile tile=selected.get(j);
								if(!finished.contains(tile, true)){
									tile.setWrong();
								}
							}
							if(dec==0)
								wrongTimer=1;
							score.incrementScore(inc-dec);
							
							
						}
					}
				}
			}
		}	
	}

	/**
	 * jfy  
	 */
	private void checkAgainstPuzzle() {
			Tile t = selected.get(tileCounter);
			if (t == finished.get(tileCounter)){
				glows.add(new Glow(t.getX(), t.getY(), t.getWidth(), t.getHeight()));
			}else{
				t.setWrong();
			}
	}

	@Override
	public void update(float dt) {

		hangInput();
		checkShowing(dt);
		
		if(!showing){
			scoreTimer-=dt;
		}
		
		if(done){
			wrongTimer+=dt;
			if(wrongTimer>=1 && glows.size==0){
				args=getArgs();
				createBoard(args[0],args[1]);
				createFinished(args[2]);
				done=false;
				if(level>maxLevel ){
					done();
				}
			}
		}
		
		for(int i=0;i<glows.size;i++){
			glows.get(i).update(dt);
			if(glows.get(i).shoudRemove()){
				glows.removeIndex(i);
				i--;
			}
		}
		for (int row = 0; row < tiles.length; row++) {
			for (int col = 0; col < tiles[0].length; col++) {
				tiles[row][col].update(dt);
			}
		}
	}

	@Override
	public void render(SpriteBatch sb) {

		sb.setProjectionMatrix(cam.combined);

		sb.begin();
		back.render(sb);
		
		//jfy
        gsm.font.draw(sb, "Swahili!!! ", 100, 150);
        
		for(int i=0;i<maxLevel;i++){
			if(i<level){
				sb.draw(light, Omo.WIDTH/2-(2*maxLevel-1)*10/2+20*i, Omo.HEIGHT-100,10,10);
			}else{
				sb.draw(dark, Omo.WIDTH/2-(2*maxLevel-1)*10/2+20*i, Omo.HEIGHT-100,10,10);
			}
		}
	
		score.render(sb);
		
		for (int row = 0; row < tiles.length; row++) {
			for (int col = 0; col < tiles[0].length; col++) {
				tiles[row][col].render(sb);
			}
		}
		for(int i=0;i<glows.size;i++){
			glows.get(i).render(sb);
		}
		//gsm.font.draw(sb, "English!", 100, 100);
        gsm.font24.draw(sb, "English", 200, 250);
        
		sb.end();
	}	
}
