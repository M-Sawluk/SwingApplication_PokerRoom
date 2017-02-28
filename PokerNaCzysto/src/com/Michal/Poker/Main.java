package com.Michal.Poker;

public class Main {

	public static void main(String[] args) {

		StartingWindow start = new StartingWindow();

		try {
			start.makeGui();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
}
