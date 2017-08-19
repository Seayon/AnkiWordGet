package com;

import java.util.Iterator;

public class App {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	
		String word = "ground";
		String sentence = "And which are,therefore,extremely popular subjects of taxation";
		String translation = "因此，也成为了征税的绝佳对象";
		AddCard addCard = new AddCard("Temp","单词-经典修改",word,sentence,translation);
		if(addCard.getWordJSONInfo().has("error")) {
			System.out.println("抓取单词信息失败: "+addCard.getWordJSONInfo().getString("error"));
		}else if(addCard.getAddState()) {
			System.out.println("添加成功！卡片ID为:"+addCard.getResponseString());
		}else if(addCard.getResponseString().equals("null")) {
			System.out.println("添加失败，可能卡片已经存在!");
		}else {
			System.out.println("添加失败，错误:"+addCard.getResponseString());
		}
//		deckName d = new deckName();
//		String result = d.getDeckName();
//		System.out.println(result);
	}
}
