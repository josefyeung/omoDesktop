package com.distraction.omo.states;

import java.util.Stack;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GSM {

	private Stack<State> states;
	
	public GSM() {
		// TODO Auto-generated constructor stub
		states=new Stack<State>();
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
	
}
