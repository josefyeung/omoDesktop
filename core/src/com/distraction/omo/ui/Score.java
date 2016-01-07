package com.distraction.omo.ui;

public class Score extends TextImage{

	private int score;
	public Score(float x, float y) {
		super("0", x, y);
	}
	
	public void incrementScore(int i) {
		// TODO Auto-generated method stub
		score+=i;
		if(score<0)
			score=0;
		setText(String.valueOf(score));
	}
	
	public int getScore() {
		return score;
	}

}
