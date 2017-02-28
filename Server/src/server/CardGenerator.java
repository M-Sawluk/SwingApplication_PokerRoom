package server;

import java.util.ArrayList;

public class CardGenerator {
	private static CardDeck deck = new CardDeck();
	private static ArrayList<String> newDeck = deck.getDeck();
	private ArrayList<String> hand;
	private ArrayList<String> flop;
	private ArrayList<String> turn;
	private ArrayList<String> river;

	public void setHand() {

		hand = new ArrayList<String>();
		hand.clear();

		int n = newDeck.size();
		int first = (int) (Math.random() * n);

		hand.add(newDeck.get(first));
		newDeck.remove(first);

		n = newDeck.size();
		int second = (int) (Math.random() * n);

		hand.add(newDeck.get(second));
		newDeck.remove(second);

	}

	public void setTableCards() {

		flop = new ArrayList<String>();
		for (int i = 0; i < 3; i++) {
			int n = newDeck.size();
			int x = (int) (Math.random() * (n));
			flop.add(newDeck.get(x));
			newDeck.remove(x);
		}

		turn = new ArrayList<String>();
		int n = newDeck.size();
		int x = (int) (Math.random() * (n));
		turn.add(newDeck.get(x));
		newDeck.remove(x);

		river = new ArrayList<String>();
		n = newDeck.size();
		x = (int) (Math.random() * n);
		river.add(newDeck.get(x));
		newDeck.remove(x);

		deck.renewCardDeck();

	}

	public ArrayList<String> getHand() {
		return hand;
	}

	public ArrayList<String> getFlop() {

		return flop;
	}

	public ArrayList<String> getTurn() {

		return turn;
	}

	public ArrayList<String> getRiver() {

		return river;
	}

	public void getSize() {
		System.out.println(newDeck.size());
	}

	public ArrayList<String> getTableCards() {
		setTableCards();

		ArrayList<String> gtc = new ArrayList<String>();

		gtc.addAll(flop);
		gtc.addAll(turn);
		gtc.addAll(river);

		return gtc;
	}
}
