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
	int tileCounter;
	int numTilesToLight;
	String[] puzzleStrings; 
	
	float scoreTimer;
	private Score score;
	
	private TextureRegion light;
	private TextureRegion dark;
	
	int level;
	int maxLevel;
	
	private Array<Glow> glows;
	private float wrongTimer;
	
	boolean done;
	
	private Graphic back;

	public PlayState(GSM gsm) {
		super(gsm);
		level=1;
		maxLevel = gsm.wordList.size();
		finished=new Array<Tile>();
		selected=new Array<Tile>();
		
		createBoard(3, 3);
		formPuzzle(level);
		createPuzzleTiles(numTilesToLight);
		
		score=new Score(Omo.WIDTH/2, Omo.HEIGHT-50);
		light=Omo.res.getAtlas("pack").findRegion("light");
		dark=Omo.res.getAtlas("pack").findRegion("dark");
		back=new Graphic(Omo.res.getAtlas("pack").findRegion("back"), Omo.WIDTH/2, 100);
		
		glows=new Array<Glow>();
	}
	
	private void formPuzzle(int level) {
		String s = gsm.wordList.get(level);
        puzzleStrings = s.split("/");
        numTilesToLight = puzzleStrings.length-1;  //jfy: as the first slice is the word/sentence 
        System.out.println("Tiles:" + numTilesToLight);
        for (int i=1; i<numTilesToLight+1 ; i++)
            System.out.println("The pieces:" + puzzleStrings[i]);
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
	 * This part doesn't make sense; need refactoring
	 * 
	 * @param dt
	 */
	private void lightUpTiles(float dt){
		if(showing){		
			timer+=dt;
			if(timer >  numTilesToLight - 0.5f){      //jfy: why? need refactoring
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
	
	
	private void createPuzzleTiles(int numTilestoLight){
		showing=true;
		timer=0;
		finished.clear();
		selected.clear();
		scoreTimer=5;
		wrongTimer=0;
		//jfy
		tileCounter=0;
		for(int i=0;i<numTilestoLight;i++){
		//jfy for(int i=0;i<numTilestoLight;i++){
			int row=0;
			int col=0;
			do{
				row=MathUtils.random(tiles.length-1);
				col=MathUtils.random(tiles[0].length-1);
			}while(finished.contains(tiles[row][col], true));
			finished.add(tiles[row][col]);
			//jfy
			tiles[row][col].setText(puzzleStrings[i+1]);			
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
	
	private void transit() {
//		gsm.set(new MenuState(gsm));
//		gsm.set(new ScoreState(gsm, score.getScore()));
		gsm.set(new TransitionState(gsm, this, new ScoreState(gsm, score.getScore()), Type.EXPAND));
	}

	@Override
	public void handleInput() {
		for(int i=0;i<MAX_FINGERs;i++){
			if(!showing && !done && Gdx.input.justTouched()){
			//jfy: if(!showing && !done && Gdx.input.isTouched(i)){
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
					//jfy if(!t.isSelected()){
						t.setSelected(true);
						selected.add(t);

						//jfy
						checkAgainstPuzzle();
						//jfy
						tileCounter ++;
						
						if(isFinished()){  //jfy check if all puzzles lit up have been chosen
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

					//}
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
				
				selected.removeIndex(tileCounter);
				t.setWrong();
				tileCounter -= 1;
			}
	}

	@Override
	public void update(float dt) {

		handleInput();
		lightUpTiles(dt);
		
		if(!showing){
			scoreTimer-=dt;
		}
		
		if(done){
			
			if(level == maxLevel){
				transit();
			}
			
			wrongTimer+=dt;
			if(wrongTimer>=1 && glows.size==0){
				createBoard(3,3);
				formPuzzle(level);
				createPuzzleTiles(numTilesToLight);
				done=false;
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
			
				//jfy: see if the tile contains letter(s)
				String t = tiles[row][col].getText(); 
				if (t !=null){
					gsm.font24.draw(sb, t, tiles[row][col].getX(), tiles[row][col].getY());
				}
			}
		}
		for(int i=0;i<glows.size;i++){
			glows.get(i).render(sb);
		}
		
		//jfy
        //gsm.font.draw(sb, "Swahili!!! ", 100, 150);
        //gsm.font24.draw(sb, "English", 200, 250);
		
		sb.end();
		
		
	}	
}
