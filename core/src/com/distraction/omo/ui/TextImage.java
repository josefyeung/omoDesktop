package com.distraction.omo.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.distraction.omo.Omo;

public class TextImage extends Box{

	private TextureRegion[][] fontSheet;
	private String text;
	
	public TextImage(String text,float x,float y) {
	  TextureRegion sheet=Omo.res.getAtlas("pack").findRegion("fontsheet");
	  fontSheet=sheet.split(50, 50);
	  this.x=x;
	  this.y=y;
	  if(text!=null){
		  setText(text);
	  }
	  
	}
	
	public void setText(String text){
		  this.width=50*text.length();
		  this.height=50;
		  this.text=text.toUpperCase();
	}
	
	public String getText() {
		return text;
	}


	public void render(SpriteBatch sb) {
		if(text==null)return;
		for(int i=0;i<text.length();i++){
			char c  =text.charAt(i);
			int index=(int)c;
			if(index>='0'&&index<='9'){
				index-='0';
				index+=27;
			}else{
				index-='A';
			}
			int row=index/fontSheet[0].length;
			int col=index%fontSheet[0].length;
			sb.draw(fontSheet[row][col], x-width/2+50*i, y-height/2);
		}
	}
}
