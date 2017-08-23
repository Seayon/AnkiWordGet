package com;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Addition")
public class Addition extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public Addition() {
        super();

    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect("/AnkiWordGet/");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");	
		PrintWriter out = response.getWriter();
		String word = request.getParameter("word");
		String sentence = request.getParameter("sentence");
		String translation = request.getParameter("translation");
		
		AddCard addCard = new AddCard("Temp","单词-经典修改",word,sentence,translation);
		if(addCard.getWordJSONInfo().has("error")) {
			out.println("抓取单词信息失败: "+addCard.getWordJSONInfo().getString("error"));
		}else if(addCard.getAddState()) {
			out.println("添加成功！卡片ID为:"+addCard.getResponseString());
		}else if(addCard.getResponseString().equals("null")) {
			out.println("添加失败，可能卡片已经存在!");
		}else {
			out.println("添加失败，错误:"+addCard.getResponseString());
		}
	}

}
