package server;

import java.io.PrintWriter;
import java.util.ArrayList;

public class PlayerServer {

	private String playerName;

	private boolean move = true;
	private boolean connected = true;

	private int chips;
	private int chipsInvested;
	private int bid;
	private int seat;
	private int uniqNum;

	private ArrayList<String> hand = new ArrayList<String>();

	private PrintWriter source1;
	private PrintWriter source2;
	private PrintWriter source3;
	private PrintWriter source4;

	private String inNetAdress;

	CardGenerator gen = new CardGenerator();

	PlayerServer(ArrayList<String> cards, int se, String in, int i) {
		chips = 10000;
		hand.addAll(cards);
		seat = se;
		inNetAdress = in;
		uniqNum = i;
		connected = true;
	}

	public void setName(String s) {
		playerName = s;
	}

	public void setCards(String s) {
		playerName = s;
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

	public void setHand(ArrayList<String> s) {
		hand.clear();
		hand.addAll(s);
	}

	public void setChipsInvested(int x) {
		chipsInvested += x;
	}

	public int getChipsInvested() {
		return chipsInvested;
	}

	public PrintWriter getSource1() {
		return source1;
	}

	public PrintWriter getSource2() {
		return source2;
	}

	public PrintWriter getSource3() {
		return source3;
	}

	public PrintWriter getSource4() {
		return source4;
	}

	public void setSource1(PrintWriter x) {
		source1 = x;
	}

	public void setSource2(PrintWriter x) {
		source2 = x;
	}

	public void setSource3(PrintWriter x) {
		source3 = x;
	}

	public void setSource4(PrintWriter x) {
		source4 = x;
	}

	public String getInNet() {
		return inNetAdress;
	}

	public int getUnique() {
		return uniqNum;
	}

	public void setConnected(boolean x) {
		connected = false;
	}

	public boolean getConnected() {
		return connected;
	}

	public void setHand() {
		hand.clear();
		gen.setHand();
		hand = gen.getHand();
	}
}
