package com;

public class App {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Word w = new Word("temp");
		String sentence = "And whic are,therefore,extremely popular subjects of taxation";
		String translation = "因此，也成为了征税的绝佳对象";
		AddCard addCard = new AddCard("Temp","单词-经典修改",w.getJSON(),sentence,translation);
		if(!addCard.getResponseString().equals("null")) {
			System.out.println("添加成功！卡片ID为:"+addCard.getResponseString());
		}else {
			System.out.println("添加失败，可能卡片已经存在!");
		}
//		deckName d = new deckName();
//		String result = d.getDeckName();
//		System.out.println(result);
	}
	

}
