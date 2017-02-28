package com.Michal.Poker;

import java.util.ArrayList;

public class PlayerClient {

	private String playerName;
	private int chips;
	private ArrayList<String> hand = new ArrayList<String>();
	private boolean move = true;
	private int chipsInvested;
	private int bid;
	private int seat;
	private String inNetAddress;
	private boolean remote = false;

	PlayerClient(String x) {
		playerName = x;
		remote = false;
	}

	PlayerClient(String x, int chip, int se, String inNet) {
		playerName = x;
		chips = chip;
		seat = se;
		inNetAddress = inNet;
		remote = true;
	}

	public void setName(String s) {
		playerName = s;
	}

	public void setCards(ArrayList<String> s) {
		hand.addAll(s);
	}

	public ArrayList<String> getHand() {
		return hand;
	}

	public String getName() {
		return playerName;
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

	public void setSeat(int x) {
		seat = x;
	}

	public int getSeat() {
		return seat;
	}

	public void setHand(String s) {
		hand.add(s);
	}

	public void setChipsInvested(int x) {
		chipsInvested += x;
	}

	public int getChipsInvested() {
		return chipsInvested;
	}

	public void chips(int x) {
		chips = x;
	}

	public void clear() {
		hand.clear();
	}

	public void setInNet(String x) {
		inNetAddress = x;
	}

	public String getInNetAdress() {
		return inNetAddress;
	}

	public boolean getRemote() {
		return remote;
	}
}
