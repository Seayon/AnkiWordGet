package com;

import org.json.*;

public class App {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Word w = new Word("ground");
		JSONObject wordInfo = w.getJSON();
		AddCard addCard = new AddCard("Temp","单词-经典修改",w.getJSON());
		
	}
	

}
