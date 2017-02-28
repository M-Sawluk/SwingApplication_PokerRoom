package com.Michal.Poker;

import java.util.*;

public class Player {
	private static int Number_Of_Players = 0;
	private String name;
	private int chips;
	private boolean isBot;
	private ArrayList<String> hand = new ArrayList<String>();
	CardGenerator gen = new CardGenerator();
	private boolean move = true;
	private int chipsInvested;
	private int bid;
	private int seat;

	Player() {
		Number_Of_Players++;
		name = String.format("Mirek%d", Number_Of_Players);
		chips = 10000;
		isBot = true;
		gen.setHand();
		hand = gen.getHand();

	}

	Player(String nameOfPlayer, boolean izBot) {
		Number_Of_Players++;
		name = nameOfPlayer;
		chips = 10000;
		isBot = izBot;
		gen.setHand();
		hand = gen.getHand();

	}

	Player(String nameOfPlayer) {
		Number_Of_Players++;
		name = nameOfPlayer;
		chips = 10000;
		isBot = true;
		gen.setHand();
		hand = gen.getHand();

	}

	public ArrayList<String> getHand() {
		return hand;
	}

	public String getName() {
		return name;
	}

	public int getChips() {
		return chips;
	}

	public void setChips(int x) {
		chips = chips - x;
	}

	public void setMove(boolean x) {
		move = x;
	}

	public void setBid(int x) {
		bid = x;
	}

	public boolean getMove() {
		return move;
	}

	public int getBid() {
		return bid;
	}

	public boolean getBot() {
		return isBot;
	}

	public void setSeat(int x) {
		seat = x;
	}

	public int getSeat() {
		return seat;
	}

	public void setHand() {
		hand.clear();
		gen.setHand();
		hand = gen.getHand();
	}

	public void setChipsInvested(int x) {
		chipsInvested += x;
	}

	public int getChipsInvested() {
		return chipsInvested;
	}
}
