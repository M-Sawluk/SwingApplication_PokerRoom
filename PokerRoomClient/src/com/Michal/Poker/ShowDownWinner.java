package com.Michal.Poker;

import java.util.ArrayList;

public class ShowDownWinner {
	char[] values = { 'A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2' };

	char[] colors = { 'H', 'C', 'D', 'S' };

	ArrayList<String> winners = new ArrayList<String>();

	int[] win;

	public void start() {

		chooseWinners(winners);
	}

	public void addWinners(String x) {

		ArrayList<Character> p0 = new ArrayList<Character>();
		// Adding player cards data
		for (int i = 0; i < x.length(); i++) {
			p0.add(x.charAt(i));
		}

		check(p0);
	}

	// Sets player card combination
	public void check(ArrayList<Character> x) {

		int z = 9;

		String line = "";

		switch (z) {

		case 9:
			// Checking for royal poker and lowest poker
			for (int i = 0; i < colors.length; i++) {
				ArrayList<Character> a = new ArrayList<Character>();
				// Adding to temp array;
				a.addAll(x);
				// Removing player number
				a.remove(14);

				int b = 0;

				int color = 0;
				// Checks if player has 5 cards in same color
				for (int j = 0; j < 5; j++) {
					if (a.contains(colors[i])) {
						b++;
						if (b == 5) {
							color = i;
							break;
						}
						a.remove(a.indexOf(colors[i]));
					}
				}
				// If player has 5 cards in same color the for loop removes
				// other cards
				if (b == 5) {
					a.clear();
					a.addAll(x);
					a.remove(14);

					for (int j = 1; j < a.size(); j += 2) {
						if (a.get(j) != colors[color]) {
							a.remove(j);
							a.remove(j - 1);
						}
					}
					// Checks for royal poker creates String that has <power of
					// the hand><player number><higestCard> in this case
					// "9<playerNumber>A"
					if (a.contains('A') && a.contains('K') && a.contains('Q') && a.contains('J') && a.contains('T')) {

						if (a.get(a.indexOf('A') + 1) == colors[color] && a.get(a.indexOf('K') + 1) == colors[color]
								&& a.get(a.indexOf('Q') + 1) == colors[color]
								&& a.get(a.indexOf('J') + 1) == colors[color]
								&& a.get(a.indexOf('T') + 1) == colors[color]) {
							// line = "9" + x.get(14) + "A";
							line = new StringBuilder().append("9").append(x.get(14)).append("A").toString();
							break;
						}

					}
					// Checks for the lowest poker creates String Output that
					// has <power of the hand><player number><higestCard> in
					// this case "8<playerNumber>5"
					if (a.contains('A') && a.contains('2') && a.contains('3') && a.contains('4') && a.contains('5')) {

						if (a.get(a.indexOf('A') + 1) == colors[color] && a.get(a.indexOf('2') + 1) == colors[color]
								&& a.get(a.indexOf('3') + 1) == colors[color]
								&& a.get(a.indexOf('4') + 1) == colors[color]
								&& a.get(a.indexOf('5') + 1) == colors[color]) {
							// line = "8" + x.get(14) + "5";
							line = new StringBuilder().append("8").append(x.get(14)).append("5").toString();
							break;
						}

					}
				}
				if (b == 5) {
					break;
				} else {
					a.clear();
					a.addAll(x);
					a.remove(14);
				}
			}
			// Checks if there is a result
			if (line.length() > 0) {

				break;
			}

			z--;

		case 8:
			// Checks for poker w/o lowest poker
			for (int i = 0; i < colors.length; i++) {
				ArrayList<Character> a = new ArrayList<Character>();

				a.addAll(x);
				a.remove(14);

				int b = 0;

				int color = 0;
				// Checks if player has 5 cards in same color
				for (int j = 0; j < 5; j++) {
					if (a.contains(colors[i])) {
						b++;
						if (b == 5) {
							color = i;
							break;
						}
						a.remove(a.indexOf(colors[i]));

					}
				}
				// If player has 5 cards in same color the for loop removes
				// other cards
				if (b == 5) {
					a.clear();
					a.addAll(x);
					a.remove(14);

					for (int j = 1; j < a.size(); j += 2) {
						if (a.get(j) != colors[color]) {
							a.remove(j);
							a.remove(j - 1);
						}
					}
					// Checks for royal poker creates String Output that has
					// <power of the hand><player number><higestCard> in this
					// case "8<playerNumber><higestCard>"
					for (int u = 0; u < 9; u++) {
						if (a.contains(values[u]) && a.contains(values[u + 1]) && a.contains(values[u + 2])
								&& a.contains(values[u + 3]) && a.contains(values[u + 4])) {

							if (a.get(a.indexOf(values[u]) + 1) == colors[color]
									&& a.get(a.indexOf(values[u + 1]) + 1) == colors[color]
									&& a.get(a.indexOf(values[u + 2]) + 1) == colors[color]
									&& a.get(a.indexOf(values[u + 3]) + 1) == colors[color]
									&& a.get(a.indexOf(values[u + 4]) + 1) == colors[color]) {
								// line = "8" + x.get(14) + values[u];
								line = new StringBuilder().append("8").append(x.get(14)).append(values[u]).toString();
								break;
							}

						}
					}
				}
				if (b == 5) {
					break;
				}

				else {
					a.clear();
					a.addAll(x);
					a.remove(14);
				}
			}
			// Checks if there is a result
			if (line.length() > 0) {

				break;
			}

			z--;

		case 7:
			// Checking for quad
			for (int i = 0; i < values.length; i++) {
				ArrayList<Character> a = new ArrayList<Character>();
				a.addAll(x);
				a.remove(14);

				int b = 0;
				// Checking if there is 4 cards in the same value and creating
				// String output with <power of the hand><player number><value
				// of the quad>
				for (int j = 0; j < 4; j++) {
					if (a.contains(values[i])) {
						b++;

						if (b == 4) {
							// line = "7" + x.get(14) + values[i];
							line = new StringBuilder().append("7").append(x.get(14)).append(values[i]).toString();
						}

						a.remove(a.indexOf(values[i]));

					}
				}
				// Adding to String output value of the 5th playing card for
				// ties situations
				if (b == 4) {
					for (int l = 0; l < values.length; l++) {
						if (a.contains(values[l])) {
							line += values[l];
							break;
						}
					}
				}
				if (b == 4) {
					break;
				}

				else {
					a.clear();
					a.addAll(x);
					a.remove(14);
				}
			}
			// Checks if there is a result
			if (line.length() > 0) {

				break;
			}

			z--;
		case 6:
			// Checking for the Full House
			for (int i = 0; i < values.length; i++) {
				ArrayList<Character> a = new ArrayList<Character>();
				a.addAll(x);
				a.remove(14);

				int b = 0;

				int c = 0;
				// Checking if there is 3 cards with the same value creates
				// String output
				for (int j = 0; j < 3; j++) {
					if (a.contains(values[i])) {
						b++;
						if (b == 3) {
							// line = "6" + x.get(14) + values[i];
							line = new StringBuilder().append("6").append(x.get(14)).append(values[i]).toString();
						}
						a.remove(a.indexOf(values[i]));
					}
				}
				// If there is 3 cards with the same value looking for pair to
				// complete Full House
				if (b == 3) {
					for (int l = 0; l < values.length; l++) {
						for (int t = 0; t < 2; t++) {
							if (a.contains(values[l])) {
								c++;
								if (c == 2) {
									line += values[l];
								}
								a.remove(a.indexOf(values[l]));
							}
						}
						if (c == 2) {
							break;
						} else {
							c = 0;
						}
					}
				} else {
					a.clear();
					a.addAll(x);
					a.remove(14);
					line = "";
				}

				if (b == 3 && c == 2) {
					break;
				} else {
					a.clear();
					a.addAll(x);
					a.remove(14);
					line = "";
				}
			}
			// Checks if there is a result
			if (line.length() > 0) {

				break;
			}

			z--;

		case 5:
			// Looking for Flush
			for (int i = 0; i < colors.length; i++) {
				ArrayList<Character> a = new ArrayList<Character>();

				a.addAll(x);
				a.remove(14);

				int b = 0;

				int color = 0;

				int count = 0;
				// Checking for 5 cards in same color creates String output
				for (int j = 0; j < 5; j++) {
					if (a.contains(colors[i])) {
						b++;
						if (b == 5) {
							line = "5" + x.get(14);
							line = new StringBuilder().append("5").append(x.get(14)).toString();
							color = i;
							break;
						}
						a.remove(a.indexOf(colors[i]));
					}
				}
				// If there is a flush loops adds the highest and the lowest
				// value of flush cards to String output
				if (b == 5) {
					a.clear();
					a.addAll(x);
					a.remove(14);

					for (int j = 1; j < a.size(); j += 2) {

						if (a.get(j) != colors[color]) {
							a.remove(j);
							a.remove(j - 1);
						}
					}

					for (int j = 0; j < values.length; j++) {
						if (a.contains(values[j]) && a.get(a.indexOf(values[j]) + 1) == colors[color]) {
							line += values[j];
							break;
						}
					}
					for (int t = 0; t < values.length; t++) {
						if (a.contains(values[t]) && a.get(a.indexOf(values[t]) + 1) == colors[color]) {
							count++;
						}
						if (count == 5) {
							line += values[t];
							break;
						}
					}

				}
				if (b == 5) {
					break;
				}

				else {
					a.clear();
					a.addAll(x);
					a.remove(14);
				}
			}
			// Checks if there is a result
			if (line.length() > 0) {

				break;
			}

			z--;

		case 4:
			// Checking for Straight
			for (int u = 0; u < 9; u++) {
				ArrayList<Character> a = new ArrayList<Character>();
				a.addAll(x);
				a.remove(14);

				if (x.contains(values[u]) && x.contains(values[u + 1]) && x.contains(values[u + 2])
						&& x.contains(values[u + 3]) && x.contains(values[u + 4])) {

					// line = "4" + x.get(14) + values[u];
					line = new StringBuilder().append("4").append(x.get(14)).append(values[u]).toString();
					break;

				}
			}
			// Checks if there is a result
			if (line.length() > 0) {

				break;
			}

			z--;

		case 3:
			// Checking for Trips
			for (int i = 0; i < values.length; i++) {
				ArrayList<Character> a = new ArrayList<Character>();
				a.addAll(x);
				a.remove(14);

				int b = 0;

				int count = 0;

				for (int j = 0; j < 3; j++) {
					if (a.contains(values[i])) {
						b++;

						if (b == 3) {
							// line = "3" + x.get(14) + values[i];
							line = new StringBuilder().append("3").append(x.get(14)).append(values[i]).toString();
						}

						a.remove(a.indexOf(values[i]));

					}
				}
				// If there is a trips it adds two highest cards for tie
				// situations
				if (b == 3) {
					for (int l = 0; l < values.length; l++) {
						if (a.contains(values[l])) {
							line += values[l];
							count++;
						}
						if (count == 2) {
							break;
						}
					}

				}

				if (b == 3) {
					break;
				}

				else {
					a.clear();
					a.addAll(x);
					a.remove(14);
				}

			}
			// Checks if there is a result
			if (line.length() > 0) {

				break;
			}

			z--;

		case 2:
			// Checking for Double Pair
			for (int i = 0; i < values.length; i++) {
				ArrayList<Character> a = new ArrayList<Character>();

				a.addAll(x);
				a.remove(14);

				int b = 0;

				int c = 0;

				for (int j = 0; j < 2; j++) {
					if (a.contains(values[i])) {
						b++;
						if (b == 2) {
							// line = "2" + x.get(14) + values[i];
							line = new StringBuilder().append("2").append(x.get(14)).append(values[i]).toString();
						}
						a.remove(a.indexOf(values[i]));
					}
				}

				if (b == 2) {
					ArrayList<Character> aa = new ArrayList<Character>();

					aa.addAll(a);

					for (int l = 0; l < values.length; l++) {
						for (int t = 0; t < 2; t++) {
							if (aa.contains(values[l])) {
								c++;
								if (c == 2) {
									line += values[l];
								}
								aa.remove(aa.indexOf(values[l]));
							}
						}
						if (c == 2) {
							break;
						} else {
							c = 0;
							aa.clear();
							aa.addAll(a);
						}
					}

					if (b == 2 && c == 2) {
						for (int l = 0; l < values.length; l++) {
							if (aa.contains(values[l])) {
								line += values[l];
								break;
							}

						}
					}
				} else {
					a.clear();
					a.addAll(x);
					a.remove(14);
				}

				if (b == 2 && c == 2) {
					break;
				}

				else {
					a.clear();
					a.addAll(x);
					a.remove(14);
					line = "";
				}

			}
			// Checks if there is a result
			if (line.length() > 0) {

				break;
			}

			z--;

		case 1:
			// Checking for pair
			for (int i = 0; i < values.length; i++) {
				ArrayList<Character> a = new ArrayList<Character>();
				a.addAll(x);
				a.remove(14);

				int b = 0;

				int count = 0;

				for (int j = 0; j < 2; j++) {
					if (a.contains(values[i])) {
						b++;

						if (b == 2) {
							// line = "1" + x.get(14) + values[i];
							line = new StringBuilder().append("1").append(x.get(14)).append(values[i]).toString();
						}

						a.remove(a.indexOf(values[i]));

					}
				}

				if (b == 2) {
					for (int l = 0; l < values.length; l++) {
						if (a.contains(values[l])) {
							line += values[l];
							count++;
						}
						if (count == 3) {
							break;
						}
					}

				}

				if (b == 2) {
					break;
				} else {
					a.clear();
					a.addAll(x);
					a.remove(14);
				}
			}
			// Checks if there is a result
			if (line.length() > 0) {

				break;
			}

			z--;

		case 0:
			// Checking for highest Cards
			int count = 0;

			line = "0" + x.get(14);

			for (int l = 0; l < values.length; l++) {
				if (x.contains(values[l])) {

					line += values[l];
					count++;
				}
				if (count == 5) {

					break;
				}
			}

		}
		// It adds to the winner List then checks another player if exist
		winners.add(line);

	}

	//Sets winner or winners
	public void chooseWinners(ArrayList<String> x) {

		ArrayList<String> tmp = new ArrayList<String>();
		// Stores tmp value
		int ed = -1;

		// Checks a List of added String outputs for highest power of hand
		for (int i = 9; i >= 0; i--) {
			for (int j = 0; j < winners.size(); j++) {
				if (Integer.parseInt(winners.get(j).substring(0, 1)) == i) {
					ed = i;
					// Adding other player with selected power
					for (int k = 0; k < winners.size(); k++) {

						if (Integer.parseInt(winners.get(k).substring(0, 1)) == ed) {
							tmp.add(winners.get(k));
						}
					}
				}
				if (ed > -1) {
					break;
				}
			}
			if (ed > -1) {
				break;
			}
		}
		// Clears winners List and add those with same power of the hand to get
		// a winner
		winners.clear();
		winners.addAll(tmp);

		if (winners.size() > 1) {
			ed = -1;

			// Checks if players has poker,quad or street power

			if (Integer.parseInt(winners.get(0).substring(0, 1)) == 8
					|| Integer.parseInt(winners.get(0).substring(0, 1)) == 4
					|| Integer.parseInt(winners.get(0).substring(0, 1)) == 7) {
				for (int i = 0; i < values.length; i++) {
					for (int j = 0; j < winners.size(); j++) {
						if (winners.get(j).charAt(2) == values[i]) {
							ed = i;

							for (int k = 0; k < winners.size(); k++) {
								if (winners.get(k).charAt(2) != values[ed]) {
									winners.remove(k);
								}
							}
						}
						if (ed > -1) {
							break;
						}
					}
					if (ed > -1) {
						break;
					}
				}
			}

			// Fullhouse Flush

			if (Integer.parseInt(winners.get(0).substring(0, 1)) == 6
					|| Integer.parseInt(winners.get(0).substring(0, 1)) == 5) {
				ed = -1;

				for (int i = 0; i < values.length; i++) {
					for (int j = 0; j < winners.size(); j++) {
						if (winners.get(j).charAt(2) == values[i]) {
							ed = i;

							for (int k = 0; k < winners.size(); k++) {
								if (winners.get(k).charAt(2) != values[ed]) {
									winners.remove(k);
								}
							}
						}
						if (ed > -1) {
							break;
						}
					}
					if (ed > -1) {
						break;
					}
				}
				if (winners.size() > 1) {
					ed = -1;

					for (int i = 0; i < values.length; i++) {
						for (int j = 0; j < winners.size(); j++) {
							if (winners.get(j).charAt(3) == values[i]) {
								ed = i;

								for (int k = 0; k < winners.size(); k++) {
									if (winners.get(k).charAt(2) != values[ed]) {
										winners.remove(k);
									}
								}
							}
							if (ed > -1) {
								break;
							}
						}
						if (ed > -1) {
							break;
						}
					}
				}

			}

			// Trips DblPair

			if (Integer.parseInt(winners.get(0).substring(0, 1)) == 3
					|| Integer.parseInt(winners.get(0).substring(0, 1)) == 2) {

				for (int n = 0; n < 3; n++) {
					ed = -1;
					for (int i = 0; i < values.length; i++) {
						for (int j = 0; j < winners.size(); j++) {
							if (winners.get(j).charAt(n + 2) == values[i]) {
								ed = i;

								for (int k = 0; k < winners.size(); k++) {
									if (winners.get(k).charAt(n + 2) != values[ed]) {
										winners.remove(k);
									}
								}

							}
							if (ed > -1) {
								break;
							}
						}
						if (ed > -1) {
							break;
						}
					}

					if (winners.size() < 2) {
						break;
					}
				}

			}

			// Pair

			if (Integer.parseInt(winners.get(0).substring(0, 1)) == 1) {

				for (int n = 0; n < 4; n++) {
					ed = -1;
					for (int i = 0; i < values.length; i++) {
						for (int j = 0; j < winners.size(); j++) {
							if (winners.get(j).charAt(n + 2) == values[i]) {
								ed = i;

								for (int k = 0; k < winners.size(); k++) {
									if (winners.get(k).charAt(n + 2) != values[ed]) {
										winners.remove(k);
									}
								}

							}
							if (ed > -1) {
								break;
							}
						}
						if (ed > -1) {
							break;
						}
					}

					if (winners.size() < 2) {
						break;
					}
				}

			}
			// Highest

			if (Integer.parseInt(winners.get(0).substring(0, 1)) == 0) {

				for (int n = 0; n < 5; n++) {
					ed = -1;
					for (int i = 0; i < values.length; i++) {
						for (int j = 0; j < winners.size(); j++) {
							if (winners.get(j).charAt(n + 2) == values[i]) {
								ed = i;

								for (int k = 0; k < winners.size(); k++) {
									if (winners.get(k).charAt(n + 2) != values[ed]) {
										winners.remove(k);
									}
								}

							}
							if (ed > -1) {
								break;
							}
						}
						if (ed > -1) {
							break;
						}
					}

					if (winners.size() < 2) {
						break;
					}
				}

			}

		}

		win = new int[winners.size()];

		for (int i = 0; i < winners.size(); i++) {
			// Adds number of players who win to Array
			win[i] = Integer.parseInt(winners.get(i).substring(1, 2));

		}

	}

	public ArrayList<String> getWinners() {

		return winners;
	}

	public int[] getWin() {
		return win;
	}

}
