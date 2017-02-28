package com.Michal.Poker;

import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.*;

public class GameVsBot {
	private String userName;
	// Window with game
	private JFrame game;
	// Panel for buttons
	private JPanel buttonArea;
	private JLabel background, topBar, exit, minimize, raiseAmount, poolImg, poolValue;
	private JButton raise, fold, call, allin, check, bet;
	// Area that presents game info or massages from other players
	private JTextArea gameInfo;
	// Allow to communicate with players
	private JTextField input;
	// Slider for bet/raise amount
	private JSlider slider;

	private Thread gameThread;

	private CardGenerator cardGenerator;

	private ShowDownWinner winnerOutput;
	// List that stores user Panels
	private ArrayList<JLabel> userPanel;
	// List that stores name labels
	private ArrayList<JLabel> name;
	// List that stores chips labels in user panel
	private ArrayList<JLabel> chips;
	// List that stores card1 labels of individual player
	private ArrayList<JLabel> card1;
	// List that stores card2 labels of individual player
	private ArrayList<JLabel> card2;
	// List that stores table card labels
	private ArrayList<JLabel> tableCards;
	// List that stores bids labels in userPanel
	private ArrayList<JLabel> bids;
	// List that stores action labels in userPanel
	private ArrayList<JLabel> actions;
	// List that stores bid chips labels
	private ArrayList<JLabel> bidChips;
	// List that stores dealer labels
	private ArrayList<JLabel> dealerButton;
	// List holding generated cards
	private ArrayList<String> generatedTableCards;
	// window buttons
	private ArrayList<JButton> buttons;
	// Player Base
	private ArrayList<Player> playerBase;

	// Coordinates for top bar
	private int x0, y0;
	// Helper that stores recent highest bid
	private int highestBid = 0;
	// tour counter
	private int tour = 0;
	// pool amount holder
	private int pool;
	// current big blind amount
	private int bigBlind = 1000;
	// current small blind amount
	private int smallBlind = 500;
	// helpers used to set small blind seat big blind seat and dealer seat
	private int sbSeat = 0, bb, sb, bbSeat, dealerSeat = 1;
	// Stops and start counting under player panel
	private boolean startCounting;
	// Allows player to use buttons
	private boolean allowButtons = false;
	// Stores player number in list
	private int pNum;
	int[] winners;

	// Creates window with game
	public void gameCreator() {

		LookAndFeel();

		createPlayers();

		setPanels();

		setNames();

		setChips();

		setCard1();

		setCard2();

		setTableCards();

		setBids();

		setButtons();

		setActions();

		setBidChips();

		putD();

		game = new JFrame();
		game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.setPreferredSize(new Dimension(800, 650));
		game.setUndecorated(true);
		game.setLayout(null);

		topBar = new JLabel();
		topBar.setSize(new Dimension(800, 20));
		topBar.setLocation(0, 0);
		topBar.setIcon(new ImageIcon(getClass().getResource("/pics/topbar.png")));
		topBar.addMouseListener(new myMouseListener());
		topBar.addMouseMotionListener(new BarListener());

		background = new JLabel();
		background.setIcon(new ImageIcon(getClass().getResource("/pics/table.jpg")));
		background.setSize(800, 530);
		background.setLocation(0, 20);
		background.addMouseListener(new myMouseListener());

		buttonArea = new JPanel();
		buttonArea.setSize(new Dimension(800, 100));
		buttonArea.setBackground(Color.BLACK);
		buttonArea.setLayout(null);
		buttonArea.setLocation(0, 550);

		exit = new JLabel();
		exit.setLocation(780, 0);
		exit.setSize(new Dimension(20, 20));
		exit.addMouseListener(new myMouseListener());

		minimize = new JLabel();
		minimize.setLocation(757, 0);
		minimize.setSize(new Dimension(20, 20));
		minimize.addMouseListener(new myMouseListener());

		gameInfo = new JTextArea(200, 80);
		gameInfo.setLineWrap(true);
		gameInfo.setWrapStyleWord(true);
		gameInfo.append("WELCOME ! \n");
		gameInfo.setEditable(false);
		gameInfo.setFont(new Font("Arial", Font.BOLD, 12));

		JLabel cardstack = new JLabel();
		cardstack.setIcon(new ImageIcon(getClass().getResource("/pics/cardstack.png")));
		cardstack.setSize(new Dimension(70, 60));
		cardstack.setLocation(369, 186);

		poolImg = new JLabel();
		poolImg.setIcon(new ImageIcon(getClass().getResource("/pics/pool.png")));
		poolImg.setSize(new Dimension(120, 120));
		poolImg.setLocation(345, 285);
		poolImg.setVisible(false);

		poolValue = new JLabel("" + pool);
		poolValue.setFont(new Font("Arial", Font.BOLD, 13));
		poolValue.setSize(new Dimension(100, 15));
		poolValue.setLocation(390, 380);
		poolValue.setVisible(false);

		JScrollPane pane = new JScrollPane(gameInfo);
		pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		pane.setLocation(400, 0);
		pane.setSize(new Dimension(400, 78));

		input = new JTextField("Send a message");
		input.setSize(new Dimension(400, 22));
		input.setLocation(400, 78);
		input.addMouseListener(new myMouseListener());
		input.addKeyListener(new myKeyListener());

		slider = new JSlider();
		slider.setSize(new Dimension(400, 20));
		slider.setLocation(0, 70);
		slider.setMaximum(playerBase.get(0).getChips());
		slider.setMinimum(0);
		slider.setValue(0);
		slider.setMajorTickSpacing(playerBase.get(0).getChips() / 100);
		slider.addChangeListener(new myChangeListener());

		raiseAmount = new JLabel("Raise for :0", JLabel.CENTER);
		raiseAmount.setSize(new Dimension(400, 10));
		raiseAmount.setFont(new Font("Arial", Font.BOLD, 10));
		raiseAmount.setLocation(0, 85);
		raiseAmount.setForeground(Color.WHITE);

		buttonArea.add(input);
		buttonArea.add(pane);
		buttonArea.add(slider);
		buttonArea.add(raiseAmount);

		for (int i = 0; i < buttons.size(); i++) {
			buttonArea.add(buttons.get(i));
		}
		for (int i = 0; i < actions.size(); i++) {
			game.getContentPane().add(actions.get(i));
		}
		for (int i = 0; i < bidChips.size(); i++) {
			game.getContentPane().add(bidChips.get(i));
		}
		for (int i = 0; i < bids.size(); i++) {
			game.getContentPane().add(bids.get(i));
		}
		for (int i = 0; i < tableCards.size(); i++) {
			game.getContentPane().add(tableCards.get(i));
		}
		for (int i = 0; i < card1.size(); i++) {
			game.getContentPane().add(card1.get(i));
		}
		for (int i = 0; i < card2.size(); i++) {
			game.getContentPane().add(card2.get(i));
		}
		for (int i = 0; i < chips.size(); i++) {
			game.getContentPane().add(chips.get(i));
		}
		game.setLayout(null);
		for (int i = 0; i < name.size(); i++) {
			game.getContentPane().add(name.get(i));
		}
		for (int i = 0; i < userPanel.size(); i++) {
			game.getContentPane().add(userPanel.get(i));
		}
		for (int i = 0; i < dealerButton.size(); i++) {
			game.getContentPane().add(dealerButton.get(i));
		}

		game.getContentPane().add(poolValue);
		game.getContentPane().add(poolImg);
		game.getContentPane().add(cardstack);
		game.getContentPane().add(background);
		game.getContentPane().add(minimize);
		game.getContentPane().add(exit);
		game.getContentPane().add(topBar);
		game.getContentPane().add(buttonArea);
		game.setVisible(true);
		game.pack();

		gameThread = new Thread(new Run());
		gameThread.start();

	}

	public void LookAndFeel() {
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			try {
				UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			} catch (Exception ex) {
			}
		}
	}

	public void setUserName(String x) {
		userName = x;
	}

	// Listener for input text field,exit and minimize
	class myMouseListener implements MouseListener {
		public void mouseClicked(MouseEvent e) {

			if (input == e.getSource()) {
				input.setText("");
				input.requestFocus();
			}
		}

		public void mouseEntered(MouseEvent e) {

		}

		public void mouseExited(MouseEvent e) {

		}

		public void mousePressed(MouseEvent e) {
			x0 = e.getX();
			y0 = e.getY();
		}

		public void mouseReleased(MouseEvent e) {
			if (exit == e.getSource()) {
				System.exit(0);
			}
			if (minimize == e.getSource()) {
				game.setState(Frame.ICONIFIED);
			}
		}

	}

	// Listener for buttons
	class MyActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			if (e.getSource() == buttons.get(1) && allowButtons == true) {
				call(pNum);
			}
			if (e.getSource() == buttons.get(2) && allowButtons == true) {
				check(pNum);
			}
			if (e.getSource() == buttons.get(4) && allowButtons == true) {
				allIn(pNum);
			}
			if (e.getSource() == buttons.get(3) && allowButtons == true) {
				fold(pNum);
			}
			if (e.getSource() == buttons.get(5) && allowButtons == true) {
				raise(pNum);
			}
			if (e.getSource() == buttons.get(0) && allowButtons == true) {
				bet(pNum);

			}
		}

	}

	// Listener for top bar
	class BarListener implements MouseMotionListener {

		public void mouseDragged(MouseEvent e) {
			int x = e.getXOnScreen();
			int y = e.getYOnScreen();

			game.setLocation(x - x0, y - y0);

		}

		public void mouseMoved(MouseEvent e) {
		}

	}

	// Listener for slider
	public class myChangeListener implements ChangeListener {

		public void stateChanged(ChangeEvent e) {
			for (int x = 0; x < playerBase.size(); x++) {
				if (playerBase.get(x).getBot() == false) {
					slider.setMaximum(playerBase.get(x).getChips());
				}
			}

			slider = (JSlider) e.getSource();

			if (!slider.getValueIsAdjusting()) {

				raiseAmount.setText("Raise for :" + slider.getValue());

			}

		}

	}

	// Listener for input text field
	public class myKeyListener implements KeyListener {

		public void keyPressed(KeyEvent e) {

		}

		public void keyReleased(KeyEvent e) {
			if (input.getText().length() > 0 && !input.getText().equals("Send a message")) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					int y = 0;
					for (int x = 0; x < playerBase.size(); x++) {
						if (playerBase.get(x).getBot() == false) {
							y = x;
						}
					}

					String k = gameInfo.getText();
					gameInfo.setText("");

					gameInfo.append(playerBase.get(y).getName() + " said : " + input.getText() + "\n");
					input.setText("");
					gameInfo.append(k);
				}
			}

		}

		public void keyTyped(KeyEvent arg0) {

		}

	}

	// Setting panel with coordinates and icons
	public ArrayList<JLabel> setPanels() {
		userPanel = new ArrayList<JLabel>();
		int[] x = { -110, -100, 105, 335, 520, 540 };
		int[] y = { 100, -60, -120, -120, -60, 100 };

		for (int j = 1; j <= playerBase.size(); j++) {
			for (int i = 0; i < playerBase.size(); i++) {
				if (playerBase.get(i).getSeat() == j) {
					JLabel panel = new JLabel();
					panel.setLocation(x[i], y[i]);
					panel.setSize(new Dimension(400, 400));
					panel.setIcon(new ImageIcon(getClass().getResource("/pics/smile.png")));
					userPanel.add(panel);
				}
			}
		}
		return userPanel;
	}

	// Setting actions with coordinates
	public ArrayList<JLabel> setActions() {
		actions = new ArrayList<JLabel>();

		int[] x = { 98, 109, 315, 545, 730, 748 };
		int[] y = { 335, 176, 115, 115, 174, 333 };

		for (int j = 1; j <= playerBase.size(); j++) {
			for (int i = 0; i < playerBase.size(); i++) {
				if (playerBase.get(i).getSeat() == j) {
					JLabel action = new JLabel("");
					action.setSize(new Dimension(100, 22));
					action.setForeground(Color.RED);
					action.setFont(new Font("Arial", Font.BOLD, 12));
					action.setLocation(x[j - 1], y[j - 1]);
					action.setVisible(false);
					actions.add(action);
				}
			}
		}

		return actions;
	}

	// Setting card1 with coordinates
	public ArrayList<JLabel> setCard1() {
		card1 = new ArrayList<JLabel>();

		int[] x = { 105, 114, 322, 430, 613, 634 };
		int[] y = { 280, 120, 60, 60, 120, 280 };

		int k = 0;

		for (int j = 1; j <= playerBase.size(); j++) {
			for (int i = 0; i < playerBase.size(); i++) {
				if (playerBase.get(i).getSeat() == j && playerBase.get(i).getBot() == false) {
					JLabel card = new JLabel();
					card.setSize(new Dimension(30, 45));
					card.setLocation(x[k], y[k]);
					card.setIcon(new ImageIcon(
							getClass().getResource("/" + new Iconz().giveIcon(playerBase.get(i).getHand().get(0)))));
					card1.add(card);
					k++;
				} else if (playerBase.get(i).getSeat() == j && playerBase.get(i).getBot() == true) {
					JLabel card = new JLabel();
					card.setSize(new Dimension(30, 45));
					card.setLocation(x[k], y[k]);
					card.setIcon(new ImageIcon(getClass().getResource("/pics/cardback.png")));
					card1.add(card);
					k++;

				}
			}
		}

		return card1;
	}

	// Setting card2 with coordinates
	public ArrayList<JLabel> setCard2() {
		card2 = new ArrayList<JLabel>();

		int[] x = { 136, 146, 353, 461, 644, 665 };
		int[] y = { 280, 120, 60, 60, 120, 280 };

		// int[] a={105,136,114,146,322,353,430,461,613,644,634,665};
		// int[] b={280,280,120,120,60,60,60,60,120,120,280,280};

		int k = 0;

		for (int j = 1; j <= playerBase.size(); j++) {
			for (int i = 0; i < playerBase.size(); i++) {
				if (playerBase.get(i).getSeat() == j && playerBase.get(i).getBot() == false) {
					JLabel card = new JLabel();
					card.setSize(new Dimension(30, 45));
					card.setLocation(x[k], y[k]);
					card.setIcon(new ImageIcon(
							getClass().getResource("/" + new Iconz().giveIcon(playerBase.get(i).getHand().get(1)))));
					card2.add(card);
					k++;
				} else if (playerBase.get(i).getSeat() == j && playerBase.get(i).getBot() == true) {
					JLabel card = new JLabel();
					card.setSize(new Dimension(30, 45));
					card.setLocation(x[k], y[k]);
					card.setIcon(new ImageIcon(getClass().getResource("/pics/cardback.png")));
					card2.add(card);
					k++;
				}
			}
		}

		return card2;
	}

	// Setting names with coordinates
	public ArrayList<JLabel> setNames() {
		name = new ArrayList<JLabel>();

		int[] x = { 8, 19, 225, 455, 640, 658 };
		int[] y = { 335, 176, 115, 115, 174, 333 };

		for (int j = 1; j <= playerBase.size(); j++) {
			for (int i = 0; i < playerBase.size(); i++) {
				if (playerBase.get(i).getSeat() == j) {
					JLabel name1 = new JLabel(playerBase.get(i).getName());
					name1.setSize(new Dimension(100, 22));
					name1.setForeground(Color.white);
					name1.setFont(new Font("Arial", Font.BOLD, 12));
					name1.setLocation(x[j - 1], y[j - 1]);
					name.add(name1);
				}
			}
		}

		return name;
	}

	// Setting chips with coordinates
	public ArrayList<JLabel> setChips() {
		chips = new ArrayList<JLabel>();

		int[] x = { 8, 19, 225, 455, 640, 658 };
		int[] y = { 355, 196, 135, 135, 194, 353 };

		for (int i = 0; i < playerBase.size(); i++) {
			JLabel chips1 = new JLabel("Chips :" + playerBase.get(i).getChips());
			chips1.setSize(new Dimension(100, 22));
			chips1.setForeground(Color.white);
			chips1.setFont(new Font("Arial", Font.BOLD, 12));
			chips1.setLocation(x[i], y[i]);
			chips.add(chips1);
		}

		return chips;
	}

	// Setting table cards with coordinates
	public ArrayList<JLabel> setTableCards() {
		tableCards = new ArrayList<JLabel>();

		cardGenerator = new CardGenerator();

		generatedTableCards = cardGenerator.getTableCards();

		int[] x = { 325 };
		int[] y = { 265 };

		for (int i = 0; i < 5; i++) {
			JLabel card1 = new JLabel();
			card1.setSize(new Dimension(40, 55));
			card1.setLocation(x[0], y[0]);
			card1.setIcon(
					new ImageIcon(getClass().getResource("/" + new Iconz().giveIcon(generatedTableCards.get(i)))));
			card1.setVisible(false);
			tableCards.add(card1);
			x[0] += 31;

		}

		return tableCards;
	}

	// Setting bids with coordinates
	public ArrayList<JLabel> setBids() {
		bids = new ArrayList<JLabel>();

		int[] x = { 190, 190, 270, 460, 550, 550 };
		int[] y = { 295, 200, 170, 170, 200, 280 };

		for (int j = 1; j <= playerBase.size(); j++) {
			for (int i = 0; i < playerBase.size(); i++) {
				if (playerBase.get(i).getSeat() == j) {
					JLabel bid1 = new JLabel();
					bid1.setSize(new Dimension(100, 100));
					bid1.setLocation(x[j - 1], y[j - 1]);
					bid1.setIcon(new ImageIcon(getClass().getResource("/pics/chips.png")));
					bid1.setVisible(false);
					bids.add(bid1);

				}
			}
		}

		return bids;

	}

	// Setting bid chips with coordinates
	public ArrayList<JLabel> setBidChips() {
		bidChips = new ArrayList<JLabel>();

		int[] x = { 190, 190, 270, 460, 550, 550 };
		int[] y = { 295, 200, 170, 170, 200, 280 };

		for (int j = 1; j <= playerBase.size(); j++) {
			for (int i = 0; i < playerBase.size(); i++) {
				if (playerBase.get(i).getSeat() == j) {
					JLabel bid2 = new JLabel("" + playerBase.get(i).getBid(), JLabel.CENTER);
					bid2.setSize(new Dimension(40, 100));
					bid2.setLocation(x[j - 1], y[j - 1] + 30);
					bid2.setFont(new Font("Arial", Font.BOLD, 10));
					bid2.setVisible(false);
					bidChips.add(bid2);
				}
			}
		}

		return bidChips;

	}

	// Setting buttons with coordinates
	public ArrayList<JButton> setButtons() {
		buttons = new ArrayList<JButton>();

		call = new JButton("CALL");
		call.setSize(new Dimension(200, 25));
		call.setLocation(0, 0);
		call.addActionListener(new MyActionListener());

		check = new JButton("CHECK");
		check.setSize(new Dimension(200, 25));
		check.setLocation(200, 0);
		check.addActionListener(new MyActionListener());

		fold = new JButton("FOLD");
		fold.setSize(new Dimension(200, 25));
		fold.setLocation(0, 25);
		fold.addActionListener(new MyActionListener());

		allin = new JButton("ALL IN");
		allin.setSize(new Dimension(200, 25));
		allin.setLocation(200, 25);
		allin.addActionListener(new MyActionListener());

		raise = new JButton("RAISE");
		raise.setSize(new Dimension(200, 25));
		raise.setLocation(0, 50);
		raise.addActionListener(new MyActionListener());

		bet = new JButton("BET");
		bet.setSize(new Dimension(200, 25));
		bet.setLocation(200, 50);
		bet.addActionListener(new MyActionListener());

		buttons.add(bet);
		buttons.add(call);
		buttons.add(check);
		buttons.add(fold);
		buttons.add(allin);
		buttons.add(raise);

		return buttons;
	}

	// Setting dealer button with coordinates
	public ArrayList<JLabel> putD() {
		dealerButton = new ArrayList<JLabel>();

		int[] x = { 156, 170, 241, 517, 608, 627 };
		int[] y = { 330, 225, 190, 190, 225, 350 };

		for (int i = 0; i < x.length; i++) {
			JLabel d1 = new JLabel();
			d1.setIcon(new ImageIcon(getClass().getResource("/pics/d.png")));
			d1.setSize(new Dimension(20, 20));
			d1.setLocation(x[i], y[i]);
			d1.setVisible(false);
			dealerButton.add(d1);
		}

		return dealerButton;
	}

	// Adding Player and Bots to playerBase and seting seats for them
	public void createPlayers() {

		playerBase = new ArrayList<Player>();

		Player player1 = new Player(userName, false);
		playerBase.add(player1);

		int rand = (int) (Math.random() * 4 + 1);

		for (int i = 0; i <= rand; i++) {
			Player p = new Player();
			playerBase.add(p);

		}

		ArrayList<Integer> Seats = new ArrayList<Integer>();

		int t = 1;

		for (int x = 0; x < playerBase.size(); x++) {
			Seats.add(t);
			t++;
		}

		for (int x = 0; x < playerBase.size(); x++) {
			int i = (int) (Math.random() * (Seats.size() - 1));
			playerBase.get(x).setSeat(Seats.get(i));
			Seats.remove(i);

		}

		ArrayList<Player> tmp = new ArrayList<Player>();
		int i = 1;
		for (int x = 0; x < playerBase.size(); x++) {
			for (int z = 0; z < playerBase.size(); z++) {
				if (playerBase.get(z).getSeat() == i) {
					tmp.add(playerBase.get(z));
					i++;
					break;
				}
			}
		}

		playerBase.clear();

		for (int x = 0; x < tmp.size(); x++) {
			playerBase.add(tmp.get(x));
		}

		// Setting value of pNum for player
		for (int z = 0; z < playerBase.size(); z++) {

			if (playerBase.get(z).getBot() == false) {
				pNum = z;
			}

		}

	}

	// PreparingGame
	// Sets small blind, big blind and dealer seat
	// Distributes cards to players
	// Taking blinds from selected players
	private void prepareGame() throws InterruptedException {

		// Seting Seats
		boolean sbX = false;
		boolean bbX = false;
		boolean de = false;

		sbSeat++;

		while (true) {

			for (int i = 0; i < playerBase.size(); i++) {
				if (playerBase.get(i).getSeat() == sbSeat && playerBase.get(i).getMove() == true) {
					sb = i;
					sbX = true;
					break;
				}
			}
			if (sbX == true) {
				break;
			} else {
				sbSeat++;
			}

			if (sbSeat > playerBase.size()) {
				sbSeat = 1;
			}

		}

		bbSeat = sbSeat + 1;

		if (bbSeat > playerBase.size()) {
			bbSeat = 1;
		}

		while (true) {

			for (int i = 0; i < playerBase.size(); i++) {
				if (playerBase.get(i).getSeat() == bbSeat && playerBase.get(i).getMove() == true) {
					bb = i;
					bbX = true;
					break;
				}
			}
			if (bbX == true) {
				break;
			} else {
				bbSeat++;
			}

			if (bbSeat > playerBase.size()) {
				bbSeat = 1;
			}

		}

		dealerSeat = sbSeat - 1;

		while (true) {
			if (dealerSeat == 0) {
				for (int y = playerBase.size(); y > 0; y--) {
					for (int p = playerBase.size() - 1; p >= 0; p--) {
						if (playerBase.get(p).getMove() == true && playerBase.get(p).getSeat() == y) {
							dealerSeat = y;
							break;
						}
					}
					if (dealerSeat > 0) {
						break;
					}
				}

			} else {
				for (int i = 0; i < playerBase.size(); i++) {

					if (playerBase.get(i).getSeat() == dealerSeat && playerBase.get(i).getMove() == true
							&& dealerSeat != sbSeat && dealerSeat != bbSeat) {
						dealerButton.get(dealerSeat - 1).setVisible(true);
						de = true;
						break;
					}
				}
				if (de == true) {
					break;
				} else {
					dealerSeat++;
				}

				if (dealerSeat > playerBase.size()) {
					dealerSeat = 1;
				}

				if (howManyPlaying() <= 2) {
					dealerButton.get(bbSeat - 1).setVisible(true);
					break;
				}
			}
		}
		// Distributes Cards
		for (int i = 0; i < playerBase.size(); i++) {
			if (playerBase.get(i).getMove() == true && playerBase.get(i).getBot() == false) {
				card1.get(playerBase.get(i).getSeat() - 1).setVisible(true);
				card2.get(playerBase.get(i).getSeat() - 1).setVisible(true);
			}
			if (playerBase.get(i).getMove() == true && playerBase.get(i).getBot() == true) {
				card1.get(playerBase.get(i).getSeat() - 1).setVisible(true);
				card1.get(playerBase.get(i).getSeat() - 1)
						.setIcon(new ImageIcon(getClass().getResource("/pics/cardback.png")));

				card2.get(playerBase.get(i).getSeat() - 1).setVisible(true);
				card2.get(playerBase.get(i).getSeat() - 1)
						.setIcon(new ImageIcon(getClass().getResource("/pics/cardback.png")));
			}
		}

		// Taking blinds

		if (playerBase.get(sb).getChips() < smallBlind) {
			playerBase.get(sb).setBid(playerBase.get(sb).getChips());
			playerBase.get(sb).setChips(playerBase.get(sb).getChips());
		} else {
			playerBase.get(sb).setBid(smallBlind);
			playerBase.get(sb).setChips(smallBlind);
		}

		chips.get(playerBase.get(sb).getSeat() - 1).setText("Chips : " + playerBase.get(sb).getChips());

		bids.get(playerBase.get(sb).getSeat() - 1).setVisible(true);

		actions.get(playerBase.get(sb).getSeat() - 1).setVisible(true);
		actions.get(playerBase.get(sb).getSeat() - 1).setText("SB");

		bidChips.get(playerBase.get(sb).getSeat() - 1).setText("" + playerBase.get(sb).getBid());
		bidChips.get(playerBase.get(sb).getSeat() - 1).setVisible(true);

		Thread.sleep(1500);

		if (playerBase.get(bb).getChips() < bigBlind) {
			playerBase.get(bb).setBid(playerBase.get(bb).getChips());
			playerBase.get(bb).setChips(playerBase.get(bb).getChips());
		} else {
			playerBase.get(bb).setBid(bigBlind);
			playerBase.get(bb).setChips(bigBlind);
		}

		actions.get(playerBase.get(bb).getSeat() - 1).setVisible(true);
		actions.get(playerBase.get(bb).getSeat() - 1).setText("BB");

		chips.get(playerBase.get(bb).getSeat() - 1).setText("Chips : " + playerBase.get(bb).getChips());

		bids.get(playerBase.get(bb).getSeat() - 1).setVisible(true);

		bidChips.get(playerBase.get(bb).getSeat() - 1).setText("" + playerBase.get(bb).getBid());
		bidChips.get(playerBase.get(bb).getSeat() - 1).setVisible(true);

		if (playerBase.get(sb).getBid() > playerBase.get(bb).getBid()) {
			highestBid = playerBase.get(sb).getBid();
		} else {
			highestBid = playerBase.get(bb).getBid();
		}

		Thread.sleep(1500);

	}

	// PreFlop Stage
	public void preFlop() throws InterruptedException {

		prepareGame();

		do {
			if (playerBase.size() > 1) {
				if (bb + 1 <= playerBase.size()) {
					for (int i = playerBase.get(bb).getSeat() + 1; i <= playerBase.size(); i++) {
						for (int j = 0; j < playerBase.size(); j++) {
							if (playerBase.get(j).getBid() != highestBid && playerBase.get(j).getMove() == true
									&& playerBase.get(j).getBot() == false && playerBase.get(j).getChips() > 0
									&& playerBase.get(j).getSeat() == i && howManyPlaying() > 1) {
								allowButtons = true;
								counting(j);
								while (allowButtons == true) {
									Thread.sleep(1000);
								}
								startCounting = true;

							} else if (playerBase.get(j).getBid() != highestBid && playerBase.get(j).getMove() == true
									&& playerBase.get(j).getBot() == true && playerBase.get(j).getChips() > 0
									&& playerBase.get(j).getSeat() == i && howManyPlaying() > 1) {
								counting(j);
								getBotDecision(j);
								startCounting = true;
							}
						}
					}
				}

				for (int i = 1; i <= playerBase.get(bb).getSeat(); i++) {
					for (int j = 0; j < playerBase.size(); j++) {
						if (playerBase.get(j).getBid() != highestBid && playerBase.get(j).getMove() == true
								&& playerBase.get(j).getBot() == false && playerBase.get(j).getChips() > 0
								&& playerBase.get(j).getSeat() == i && howManyPlaying() > 1) {
							allowButtons = true;
							counting(j);
							while (allowButtons == true) {
								Thread.sleep(1000);
							}
							startCounting = true;

						} else if (playerBase.get(j).getBid() != highestBid && playerBase.get(j).getMove() == true
								&& playerBase.get(j).getBot() == true && playerBase.get(j).getChips() > 0
								&& playerBase.get(j).getSeat() == i && howManyPlaying() > 1) {
							counting(j);
							getBotDecision(j);
							startCounting = true;
						} else if (playerBase.get(j).getBid() == bigBlind && playerBase.get(j).getMove() == true
								&& playerBase.get(j).getBot() == false && playerBase.get(bb).getChips() > 0
								&& playerBase.get(j).getSeat() == i && howManyPlaying() > 1) {
							allowButtons = true;
							counting(j);
							while (allowButtons == true) {
								Thread.sleep(1000);
							}
							startCounting = true;

						} else if (playerBase.get(j).getBid() == bigBlind && playerBase.get(j).getMove() == true
								&& playerBase.get(j).getBot() == true && playerBase.get(j).getChips() > 0
								&& playerBase.get(j).getSeat() == i && howManyPlaying() > 1) {
							counting(j);
							getBotDecision(j);
							startCounting = true;
						}
					}

				}

			}

		} while (isTourEnd() == true);

		tour++;

		if (howManyPlaying() >= 2) {
			tableCards.get(0).setVisible(true);
			tableCards.get(1).setVisible(true);
			tableCards.get(2).setVisible(true);
		}

		reseting();

		gotAWinner();
	}

	// Rest stages
	private void flopTurnRiver() throws InterruptedException {

		do {
			if (howManyPlaying() > 1) {
				for (int i = sb + 1; i <= 6; i++) {
					
					for (int j = 0; j < playerBase.size(); j++) {
						if (playerBase.get(j).getBid() != highestBid && playerBase.get(j).getMove() == true
								&& playerBase.get(j).getBot() == false && playerBase.get(j).getChips() > 0
								&& playerBase.get(j).getSeat() == i && howManyPlaying() > 1) {
							allowButtons = true;
							counting(j);
							while (allowButtons == true) {
								Thread.sleep(1000);
							}
							startCounting = true;

						} else if (playerBase.get(j).getBid() != highestBid && playerBase.get(j).getMove() == true
								&& playerBase.get(j).getBot() == true && playerBase.get(j).getChips() > 0
								&& playerBase.get(j).getSeat() == i && howManyPlaying() > 1) {
							counting(j);
							getBotDecision(j);
							startCounting = true;
						}
					}
				}
				if (sb >= 1) {

					for (int i = 1; i <= sb; i++) {
						
						for (int j = 0; j < playerBase.size(); j++) {
							if (playerBase.get(j).getBid() != highestBid && playerBase.get(j).getMove() == true
									&& playerBase.get(j).getBot() == false && playerBase.get(j).getChips() > 0
									&& playerBase.get(j).getSeat() == i && howManyPlaying() > 1) {
								allowButtons = true;
								counting(j);
								while (allowButtons == true) {
									Thread.sleep(1000);
								}
								startCounting = true;

							} else if (playerBase.get(j).getBid() != highestBid && playerBase.get(j).getMove() == true
									&& playerBase.get(j).getBot() == true && playerBase.get(j).getChips() > 0
									&& playerBase.get(j).getSeat() == i && howManyPlaying() > 1) {
								counting(j);
								getBotDecision(j);
								startCounting = true;
							}
						}
					}
				}

			}

		} while (isTourEnd() == true);

		reseting();

		gotAWinner();

	}

	// Counts how many players can make a move
	private int howManyPlaying() {
		int k = 0;

		for (int i = 0; i < playerBase.size(); i++) {
			if (playerBase.get(i).getMove() == true) {
				k++;
			}
		}

		return k;
	}

	// Counts how many players have chips
	private int howManyHaveChips() {
		int k = 0;

		for (int i = 0; i < playerBase.size(); i++) {
			if (playerBase.get(i).getChips() > 0) {
				k++;
			}
		}

		return k;
	}

	// Checks if can go to another tour
	private boolean isTourEnd() {

		if (highestBid == -1) {
			highestBid = 0;
		}
		int counter = 0;
		int counter1 = 0;
		for (int i = 0; i < playerBase.size(); i++) {
			if (playerBase.get(i).getMove() == true) {
				counter++;
			}

		}
		for (int i = 0; i < playerBase.size(); i++) {
			if ((playerBase.get(i).getBid() == highestBid && playerBase.get(i).getMove() == true)
					|| (playerBase.get(i).getChips() == 0 && playerBase.get(i).getMove() == true)) {
				counter1++;
			}
		}

		if (counter == counter1) {
			counter = 0;
			counter1 = 0;
			return false;
		} else {
			counter = 0;
			counter1 = 0;
			return true;
		}

	}

	// gets bot decision depending on his chips amount
	private void getBotDecision(int i) {

		if (playerBase.get(i).getChips() < highestBid) {
			while (true) {
				int k = (int) (Math.round(Math.random()));

				if (k == 1) {
					fold(i);
					break;
				}

				else if (k == 0) {
					allIn(i);
					break;
				}

			}
		}
		if (playerBase.get(i).getChips() >= highestBid) {
			while (true) {
				int k = (int) (Math.round(Math.random() * 10));

				if (k >= 0 && k <= 4) {
					call(i);
					break;
				}

				else if (k >= 5 && k <= 6 && highestBid != -1 && highestBid != playerBase.get(i).getBid()) {
					fold(i);
					break;
				}

				else if (k == 7) {
					raise(i);
					break;
				}

				else if (k < 10 && k > 8 && playerBase.get(i).getBid() == highestBid) {
					check(i);
					break;
				}

				else if (k == 8 && highestBid <= 0) {
					bet(i);
					break;
				}

				else if (k == 10) {
					allIn(i);
					break;
				}

			}
		}
	}

	// reseting appropriate values and summing pool value
	private void reseting() {

		try {

			highestBid = -1;

			for (int i = 0; i < playerBase.size(); i++) {
				pool = pool + playerBase.get(i).getBid();

				playerBase.get(i).setChipsInvested(playerBase.get(i).getBid());

				playerBase.get(i).setBid(0);

				bids.get(playerBase.get(i).getSeat() - 1).setVisible(false);

				bidChips.get(playerBase.get(i).getSeat() - 1).setVisible(false);
			}

			Thread.sleep(1000);

			for (int i = 0; i < playerBase.size(); i++) {
				actions.get(playerBase.get(i).getSeat() - 1).setVisible(false);
			}

			Thread.sleep(700);

			poolImg.setVisible(true);
			poolValue.setVisible(true);
			poolValue.setText("" + pool);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	// Checks if game is done and there is a winner
	private void gotAWinner() {

		if (howManyPlaying() < 2) {
			for (int i = 0; i < playerBase.size(); i++) {
				if (playerBase.get(i).getMove() == true) {
					playerBase.get(i).setChips((-1) * pool);

				}

				chips.get(playerBase.get(i).getSeat() - 1).setText("Chips : " + playerBase.get(i).getChips());

				poolValue.setVisible(false);
				poolImg.setVisible(false);
			}
			for (int i = 0; i < tableCards.size(); i++) {
				tableCards.get(i).setVisible(false);
			}
			pool = 0;
		}
	}

	// Method for call button
	private void call(int x) {
		if (playerBase.get(x).getBid() == highestBid || highestBid == -1) {
			check(x);
			allowButtons = false;
		}

		else if (playerBase.get(x).getChips() + playerBase.get(x).getBid() >= highestBid)

		{
			playerBase.get(x).setChips((highestBid - playerBase.get(x).getBid()));

			chips.get(playerBase.get(x).getSeat() - 1).setText("Chips : " + playerBase.get(x).getChips());

			actions.get(playerBase.get(x).getSeat() - 1).setVisible(true);
			actions.get(playerBase.get(x).getSeat() - 1).setText("Call");

			playerBase.get(x).setBid(highestBid);

			bids.get(playerBase.get(x).getSeat() - 1).setVisible(true);

			bidChips.get(playerBase.get(x).getSeat() - 1).setText("" + playerBase.get(x).getBid());
			bidChips.get(playerBase.get(x).getSeat() - 1).setVisible(true);

			allowButtons = false;
			startCounting = false;
		} else if (playerBase.get(x).getChips() + playerBase.get(x).getBid() < highestBid) {
			gameInfo.append(playerBase.get(x).getName() + " You Cant Call! \n");
			actions.get(playerBase.get(x).getSeat() - 1).setText("ERROR");
			actions.get(playerBase.get(x).getSeat() - 1).setVisible(true);

			if (playerBase.get(x).getBot() == true) {
				getBotDecision(x);
			}
		}

	}

	// Method for check button
	private void check(int x) {

		if (highestBid == -1) {
			playerBase.get(x).setBid(0);
			actions.get(playerBase.get(x).getSeat() - 1).setText("Check");
			actions.get(playerBase.get(x).getSeat() - 1).setVisible(true);
			allowButtons = false;
			startCounting = false;
		}

		else if (playerBase.get(x).getBid() != highestBid && highestBid > 0) {
			gameInfo.append(playerBase.get(x).getName() + " You Cant Check! \n");
			actions.get(playerBase.get(x).getSeat() - 1).setText("ERROR");
			actions.get(playerBase.get(x).getSeat() - 1).setVisible(true);

			if (playerBase.get(x).getBot() == true) {
				getBotDecision(x);
			}
		} else {
			actions.get(playerBase.get(x).getSeat() - 1).setText("Check");
			actions.get(playerBase.get(x).getSeat() - 1).setVisible(true);
			startCounting = false;
			allowButtons = false;
		}
	}

	// Method for fold button
	private void fold(int x) {
		playerBase.get(x).setMove(false);
		actions.get(playerBase.get(x).getSeat() - 1).setText("Fold");
		actions.get(playerBase.get(x).getSeat() - 1).setVisible(true);
		allowButtons = false;
		startCounting = false;
		card1.get(playerBase.get(x).getSeat() - 1).setVisible(false);
		card2.get(playerBase.get(x).getSeat() - 1).setVisible(false);
	}

	// Method for raise button
	private void raise(int x) {
		if (playerBase.get(x).getBot() == false) {

			if (highestBid == -1) {
				highestBid = 0;
			}
			int s = slider.getValue();
			if (s > highestBid) {
				playerBase.get(x).setChips(s);

				chips.get(playerBase.get(x).getSeat() - 1).setText("Chips : " + playerBase.get(x).getChips());

				playerBase.get(x).setBid(s + playerBase.get(x).getBid());

				actions.get(playerBase.get(x).getSeat() - 1).setText("Raise");
				actions.get(playerBase.get(x).getSeat() - 1).setVisible(true);

				bids.get(playerBase.get(x).getSeat() - 1).setVisible(true);

				bidChips.get(playerBase.get(x).getSeat() - 1).setText("" + playerBase.get(x).getBid());
				bidChips.get(playerBase.get(x).getSeat() - 1).setVisible(true);

				highestBid = playerBase.get(x).getBid();

				allowButtons = false;
				startCounting = false;
			} else if (s <= highestBid) {
				actions.get(playerBase.get(x).getSeat() - 1).setText("ERROR");
				actions.get(playerBase.get(x).getSeat() - 1).setVisible(true);

				gameInfo.append(playerBase.get(x).getName() + " Raise More! \n");

			}

		}

		if (playerBase.get(x).getBot() == true) {

			int t = 0;

			if (highestBid > 0) {
				t = highestBid;
			} else {
				t = 2 * bigBlind;
			}

			if (playerBase.get(x).getChips() > (2 * t)) {
				playerBase.get(x).setChips(2 * t);

				chips.get(playerBase.get(x).getSeat() - 1).setText("Chips : " + playerBase.get(x).getChips());

				playerBase.get(x).setBid(2 * t + playerBase.get(x).getBid());

				bids.get(playerBase.get(x).getSeat() - 1).setVisible(true);

				bidChips.get(playerBase.get(x).getSeat() - 1).setText("" + playerBase.get(x).getBid());
				bidChips.get(playerBase.get(x).getSeat() - 1).setVisible(true);

				highestBid = playerBase.get(x).getBid();

				actions.get(playerBase.get(x).getSeat() - 1).setText("Raise");
				actions.get(playerBase.get(x).getSeat() - 1).setVisible(true);
			} else {
				allIn(x);
			}
		}

	}

	// Method for all in button
	private void allIn(int x) {
		if ((playerBase.get(x).getChips() + playerBase.get(x).getBid()) >= highestBid) {
			highestBid = playerBase.get(x).getChips();

			playerBase.get(x).setBid(playerBase.get(x).getChips() + playerBase.get(x).getBid());

			bids.get(playerBase.get(x).getSeat() - 1).setVisible(true);

			playerBase.get(x).setChips(playerBase.get(x).getChips());

			chips.get(playerBase.get(x).getSeat() - 1).setText("Chips : " + playerBase.get(x).getChips());

			highestBid = playerBase.get(x).getBid();

			actions.get(playerBase.get(x).getSeat() - 1).setText("All In");
			actions.get(playerBase.get(x).getSeat() - 1).setVisible(true);

			bidChips.get(playerBase.get(x).getSeat() - 1).setText("" + playerBase.get(x).getBid());
			bidChips.get(playerBase.get(x).getSeat() - 1).setVisible(true);

			allowButtons = false;
			startCounting = false;
		} else if ((playerBase.get(x).getChips() + playerBase.get(x).getBid()) < highestBid
				&& playerBase.get(x).getChips() > 0) {

			playerBase.get(x).setBid(playerBase.get(x).getChips() + playerBase.get(x).getBid());
			playerBase.get(x).setChips(playerBase.get(x).getChips());

			bids.get(playerBase.get(x).getSeat() - 1).setVisible(true);

			chips.get(playerBase.get(x).getSeat() - 1).setText("Chips : " + playerBase.get(x).getChips());

			actions.get(playerBase.get(x).getSeat() - 1).setText("All In");
			actions.get(playerBase.get(x).getSeat() - 1).setVisible(true);

			bidChips.get(playerBase.get(x).getSeat() - 1).setText("" + playerBase.get(x).getBid());
			bidChips.get(playerBase.get(x).getSeat() - 1).setVisible(true);

			allowButtons = false;
			startCounting = false;
		}

	}

	// Method for bet button
	private void bet(int x) {
		if (highestBid <= 0 && playerBase.get(x).getBot() == false && slider.getValue() > 0) {

			int s = slider.getValue();

			playerBase.get(x).setChips(s);

			chips.get(playerBase.get(x).getSeat() - 1).setText("Chips : " + playerBase.get(x).getChips());

			playerBase.get(x).setBid(s + playerBase.get(x).getBid());

			actions.get(playerBase.get(x).getSeat() - 1).setText("Bet");
			actions.get(playerBase.get(x).getSeat() - 1).setVisible(true);

			bids.get(playerBase.get(x).getSeat() - 1).setVisible(true);

			bidChips.get(playerBase.get(x).getSeat() - 1).setText("" + playerBase.get(x).getBid());
			bidChips.get(playerBase.get(x).getSeat() - 1).setVisible(true);

			highestBid = playerBase.get(x).getBid();

			allowButtons = false;
			startCounting = false;
		}

		else if (highestBid > 0 && playerBase.get(x).getBot() == false) {
			raise(x);
		}

		if (highestBid <= 0 && playerBase.get(x).getBot() == true) {
			int t = 0;

			if (highestBid > 0) {
				t = highestBid;
			} else {
				t = 2 * bigBlind;
			}

			if (playerBase.get(x).getChips() > (t)) {
				playerBase.get(x).setChips(t);

				chips.get(playerBase.get(x).getSeat() - 1).setText("Chips : " + playerBase.get(x).getChips());

				playerBase.get(x).setBid(t + playerBase.get(x).getBid());

				actions.get(playerBase.get(x).getSeat() - 1).setText("Bet");
				actions.get(playerBase.get(x).getSeat() - 1).setVisible(true);

				bids.get(playerBase.get(x).getSeat() - 1).setVisible(true);

				bidChips.get(playerBase.get(x).getSeat() - 1).setText("" + playerBase.get(x).getBid());
				bidChips.get(playerBase.get(x).getSeat() - 1).setVisible(true);

				highestBid = playerBase.get(x).getBid();

			} else {
				allIn(x);
			}
		}

	}

	// Ending method that gives a winner or winers
	private void showDownResult() {

		winnerOutput = new ShowDownWinner();

		String players = "";

		for (int i = 0; i < playerBase.size(); i++) {
			if (playerBase.get(i).getMove() == true) {
				String hand = "";
				hand = playerBase.get(i).getHand().get(0) + playerBase.get(i).getHand().get(1)
						+ generatedTableCards.get(0) + generatedTableCards.get(1) + generatedTableCards.get(2)
						+ generatedTableCards.get(3) + generatedTableCards.get(4) + i;
				winnerOutput.addWinners(hand);
			}
		}

		winnerOutput.start();

		winners = new int[winnerOutput.getWin().length];

		for (int v = 0; v < winnerOutput.getWin().length; v++) {
			winners[v] = winnerOutput.getWin()[v];
			players += playerBase.get(winnerOutput.getWin()[v]).getName() + " ";
		}

		poolValue.setVisible(false);
		poolImg.setVisible(false);

		for (int i = 0; i < playerBase.size(); i++) {
			card1.get(playerBase.get(i).getSeat() - 1).setIcon(new ImageIcon(
					getClass().getResource("/" + new Iconz().giveIcon(playerBase.get(i).getHand().get(0)))));
			card2.get(playerBase.get(i).getSeat() - 1).setIcon(new ImageIcon(
					getClass().getResource("/" + new Iconz().giveIcon(playerBase.get(i).getHand().get(1)))));

		}

		poolSpliter();

		String[] results = { "HighestCard", "Pair", "Double Pair", "Trips", "Straight", "Flush", "Full House", "Quad",
				"Royal Poker", "Poker" };

		String k = gameInfo.getText();
		gameInfo.setText("");

		gameInfo.append(players + " scored:"
				+ results[Integer.parseInt(winnerOutput.getWinners().get(0).substring(0, 1))] + "\n");

		gameInfo.append(k);

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < playerBase.size(); i++) {

			card1.get(playerBase.get(i).getSeat() - 1).setVisible(false);
			card2.get(playerBase.get(i).getSeat() - 1).setVisible(false);
			chips.get(playerBase.get(i).getSeat() - 1).setText("Chips : " + playerBase.get(i).getChips());
		}

	}

	// Clearing players with 0 chips
	private void cleaningFromLoosers() {
		for (int x = 0; x < playerBase.size(); x++) {
			if (playerBase.get(x).getChips() == 0) {

				chips.get(playerBase.get(x).getSeat() - 1).setVisible(false);

				userPanel.get(playerBase.get(x).getSeat() - 1).setVisible(false);

				name.get(playerBase.get(x).getSeat() - 1).setVisible(false);

				card1.get(playerBase.get(x).getSeat() - 1).setVisible(false);

				card2.get(playerBase.get(x).getSeat() - 1).setVisible(false);

				if (playerBase.get(x).getChips() > 0) {
					playerBase.get(x).setMove(true);
				}

				else {
					playerBase.get(x).setMove(false);
				}
			}
		}

	}

	// Method that splits chips to players
	public void poolSpliter() {

		// Array holding main pool [0] and side pools
		int[] pools = new int[howManyPlaying() - 1];

		// List with chipsInvested by players
		ArrayList<Integer> invested = new ArrayList<Integer>();

		// Array holding chips stages to figure if winning player should obtain
		// chips
		int[] chipsEdge = new int[howManyPlaying() - 1];

		for (int y = 0; y < playerBase.size(); y++) {
			if (playerBase.get(y).getMove() == true) {
				invested.add(playerBase.get(y).getChipsInvested());
			}
		}
		// Sorting Collection
		Collections.sort(invested);

		for (int i = 0; i < howManyPlaying() - 1; i++) {
			if (i == 0) {
				// Main pool (pools[0]) has always value of lowest chips
				// invested multiplied by players in game
				pools[0] = invested.get(0) * howManyPlaying();
				// pool decreased by main pool
				pool -= pools[0];

				// adding chips invested by players who folded to main pool and
				// removing them from pool
				for (int j = 0; j < playerBase.size(); j++) {

					if (playerBase.get(j).getMove() == false && playerBase.get(j).getChipsInvested() > 0
							&& playerBase.get(j).getChipsInvested() >= invested.get(0)) {
						pools[0] += invested.get(0);
						pool -= invested.get(0);
						playerBase.get(j).setChipsInvested(-invested.get(0));

					} else if (playerBase.get(j).getMove() == false && playerBase.get(j).getChipsInvested() > 0
							&& playerBase.get(j).getChipsInvested() < invested.get(0)) {
						pools[0] += playerBase.get(j).getChipsInvested();
						pool -= playerBase.get(j).getChipsInvested();
						playerBase.get(j).setChipsInvested(-playerBase.get(j).getChipsInvested());

					}

				}

			} else {
				// Creating side pools
				pools[i] = (invested.get(i) - invested.get(i - 1)) * (howManyPlaying() - i);
				// Populating chipsEdge to figure if winning player should
				// obtain chips
				chipsEdge[i] = invested.get(i);
				// pool decreased by side pools
				pool -= pools[i];

				// adding chips invested by players who folded to side pool and
				// removing them from pool
				for (int j = 0; j < playerBase.size(); j++) {
					if (playerBase.get(j).getMove() == false && playerBase.get(j).getChipsInvested() > 0
							&& playerBase.get(j).getChipsInvested() >= (invested.get(i) - invested.get(i - 1))) {
						pools[i] += (invested.get(i) - invested.get(i - 1));
						pool -= (invested.get(i) - invested.get(i - 1));
						playerBase.get(j).setChipsInvested(-(invested.get(i) - invested.get(i - 1)));
					} else if (playerBase.get(j).getMove() == false && playerBase.get(j).getChipsInvested() > 0
							&& playerBase.get(j).getChipsInvested() < (invested.get(i) - invested.get(i - 1))) {
						pools[i] += playerBase.get(j).getChipsInvested();
						pool -= playerBase.get(j).getChipsInvested();
						playerBase.get(j).setChipsInvested(-playerBase.get(j).getChipsInvested());
					}
				}

			}
		}

		for (int i = 0; i < pools.length; i++) {
			boolean l = false;
			if (i == 0) {
				// Adding main pool (pool[0]) divided by number of winners to
				// winners
				for (int o = 0; o < winners.length; o++) {
					playerBase.get(winners[o]).setChips(-pools[0] / winners.length);
					chips.get(playerBase.get(winners[o]).getSeat() - 1)
							.setText("Chips : " + playerBase.get(winners[o]).getChips());
				}
				pools[0] = 0;

			}

			else {
				// Adding side pools divided by number of winners who are
				// included in specific chips edge
				for (int o = 0; o < winners.length; o++) {
					if (chipsEdge[i] <= playerBase.get(winners[o]).getChipsInvested()) {
						int c = 0;

						l = true;
						// Counting how many players are in selected chipsEdge
						for (int y = 0; y < winners.length; y++) {
							if (chipsEdge[i] <= playerBase.get(winners[y]).getChipsInvested()) {
								c++;
							}

						}
						// Redistributing chips
						playerBase.get(winners[o]).setChips(-pools[i] / c);
						chips.get(playerBase.get(winners[o]).getSeat() - 1)
								.setText("Chips : " + playerBase.get(winners[o]).getChips());
					}
				}
			}

			if (l == true) {
				pools[i] = 0;
			}

		}

		// Redistributing chips from side pools which are not allowed by winners
		for (int i = 0; i < pools.length; i++) {

			boolean l = false;

			if (pools[i] > 0) {
				for (int z = 0; z < playerBase.size(); z++) {
					for (int o = 0; o < winners.length; o++) {
						if (chipsEdge[i] <= playerBase.get(z).getChipsInvested() && z != winners[o]
								&& playerBase.get(z).getMove() == true) {
							int c = 0;

							l = true;

							for (int y = 0; y < playerBase.size(); y++) {
								for (int t = 0; t < winners.length; t++) {
									if (chipsEdge[i] <= playerBase.get(y).getChipsInvested() && y != winners[t]
											&& playerBase.get(y).getMove() == true) {
										c++;
									}
								}
							}
							playerBase.get(z).setChips(-pools[i] / c);
							chips.get(playerBase.get(z).getSeat() - 1)
									.setText("Chips : " + playerBase.get(z).getChips());
						}
					}
				}
			}

			if (l == true) {
				pools[i] = 0;
			}
		}

		// For situation when player who invested most didn't win
		if (pool > 0) {
			for (Player x : playerBase) {
				if (invested.get(invested.size() - 1) == x.getChipsInvested()) {
					x.setChips(-pool);
					pool = 0;
					chips.get(x.getSeat() - 1).setText("Chips : " + x.getChips());
					;
				}

			}
		}

	}

	// Method displaying counting under player panel
	public void counting(int x) {
		int t = 800;
		startCounting = true;
		double g = 0;
		try {
			if (startCounting == true) {
				if (playerBase.get(x).getBot() == true) {
					g = Math.random();
				}
				if (g > 0.67) {
					startCounting = false;
				}
				userPanel.get(playerBase.get(x).getSeat() - 1)
						.setIcon(new ImageIcon(getClass().getResource("/pics/smile10.png")));
				Thread.sleep(t);
			}
			if (startCounting == true) {
				if (playerBase.get(x).getBot() == true) {
					g = Math.random();
				}
				if (g > 0.67) {
					startCounting = false;
				}
				userPanel.get(playerBase.get(x).getSeat() - 1)
						.setIcon(new ImageIcon(getClass().getResource("/pics/smile9.png")));
				Thread.sleep(t);
			}
			if (startCounting == true) {
				if (playerBase.get(x).getBot() == true) {
					g = Math.random();
				}
				if (g > 0.67) {
					startCounting = false;
				}
				userPanel.get(playerBase.get(x).getSeat() - 1)
						.setIcon(new ImageIcon(getClass().getResource("/pics/smile8.png")));
				Thread.sleep(t);
			}
			if (startCounting == true) {
				if (playerBase.get(x).getBot() == true) {
					g = Math.random();
				}
				if (g > 0.67) {
					startCounting = false;
				}
				userPanel.get(playerBase.get(x).getSeat() - 1)
						.setIcon(new ImageIcon(getClass().getResource("/pics/smile7.png")));
				Thread.sleep(t);
			}
			if (startCounting == true) {
				if (playerBase.get(x).getBot() == true) {
					g = Math.random();
				}
				if (g > 0.67) {
					startCounting = false;
				}
				userPanel.get(playerBase.get(x).getSeat() - 1)
						.setIcon(new ImageIcon(getClass().getResource("/pics/smile6.png")));
				Thread.sleep(t);
			}
			if (startCounting == true) {
				if (playerBase.get(x).getBot() == true) {
					g = Math.random();
				}
				if (g > 0.67) {
					startCounting = false;
				}
				userPanel.get(playerBase.get(x).getSeat() - 1)
						.setIcon(new ImageIcon(getClass().getResource("/pics/smile5.png")));
				Thread.sleep(t);
			}
			if (startCounting == true) {
				if (playerBase.get(x).getBot() == true) {
					g = Math.random();
				}
				if (g > 0.67) {
					startCounting = false;
				}
				userPanel.get(playerBase.get(x).getSeat() - 1)
						.setIcon(new ImageIcon(getClass().getResource("/pics/smile4.png")));
				Thread.sleep(t);
			}

			if (startCounting == true) {
				if (playerBase.get(x).getBot() == true) {
					g = Math.random();
				}
				if (g > 0.67) {
					startCounting = false;
				}
				userPanel.get(playerBase.get(x).getSeat() - 1)
						.setIcon(new ImageIcon(getClass().getResource("/pics/smile3.png")));
				Thread.sleep(t);
			}
			if (startCounting == true) {
				if (playerBase.get(x).getBot() == true) {
					g = Math.random();
				}
				if (g > 0.67) {
					startCounting = false;
				}
				userPanel.get(playerBase.get(x).getSeat() - 1)
						.setIcon(new ImageIcon(getClass().getResource("/pics/smile2.png")));
				Thread.sleep(t);
			}
			if (startCounting == true) {
				if (playerBase.get(x).getBot() == true) {
					startCounting = false;
				}
				userPanel.get(playerBase.get(x).getSeat() - 1)
						.setIcon(new ImageIcon(getClass().getResource("/pics/smile1.png")));
				Thread.sleep(t);
			}
			if (startCounting == true) {
				if (playerBase.get(x).getBot() == true) {
					g = Math.random();
				}
				if (g > 0.67) {
					startCounting = false;
				}
				userPanel.get(playerBase.get(x).getSeat() - 1)
						.setIcon(new ImageIcon(getClass().getResource("/pics/smile0.png")));
				Thread.sleep(t);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		userPanel.get(playerBase.get(x).getSeat() - 1)
				.setIcon(new ImageIcon(getClass().getResource("/pics/smile.png")));

		if (playerBase.get(x).getBid() == highestBid && startCounting == true) {
			userPanel.get(playerBase.get(x).getSeat() - 1)
					.setIcon(new ImageIcon(getClass().getResource("/pics/smile.png")));
			allowButtons = false;
			check(x);
		} else if (playerBase.get(x).getBid() != highestBid && startCounting == true) {
			userPanel.get(playerBase.get(x).getSeat() - 1)
					.setIcon(new ImageIcon(getClass().getResource("/pics/smile.png")));
			allowButtons = false;
			fold(x);
		}

	}
	
	//Setting starting values at the end
	private void end()
	{
		tour = 0;
		pool = 0;

		for (int k = 0; k < playerBase.size(); k++) {
			if (playerBase.get(k).getChips() > 0) {
				playerBase.get(k).setMove(true);
				playerBase.get(k).setHand();
				
				card1.get(playerBase.get(k).getSeat() - 1).setIcon(new ImageIcon(getClass()
						.getResource("/" + new Iconz().giveIcon(playerBase.get(k).getHand().get(0)))));
				card2.get(playerBase.get(k).getSeat() - 1).setIcon(new ImageIcon(getClass()
						.getResource("/" + new Iconz().giveIcon(playerBase.get(k).getHand().get(1)))));

			}

			dealerButton.get(k).setVisible(false);
			playerBase.get(k).setChipsInvested(-playerBase.get(k).getChipsInvested());
		}

		generatedTableCards.clear();
		generatedTableCards = cardGenerator.getTableCards();

		for (int u = 0; u < tableCards.size(); u++) {
			tableCards.get(u).setIcon(new ImageIcon(
					getClass().getResource("/" + new Iconz().giveIcon(generatedTableCards.get(u)))));
			tableCards.get(u).setVisible(false);
		}

		poolValue.setVisible(false);
		poolImg.setVisible(false);

	}
	
	// Game Loop which works until 1 player has chips
	class Run implements Runnable {

		public void run() {

			try {
				while (howManyHaveChips() > 1) {

					if (tour == 0) {

						preFlop();
					}

					if (howManyPlaying() > 1) {
						for (int i = 1; i < 4; i++) {

							int l = 0;
							for (int k = 0; k < playerBase.size(); k++) {
								if (playerBase.get(k).getChips() > 0 && playerBase.get(k).getMove() == true) {
									l++;
								}
							}

							if (l > 1) {
								flopTurnRiver();
							}

							tour++;
							if (tour == 2 && howManyPlaying() > 1) {
								tableCards.get(3).setVisible(true);
							} else if (tour == 3 && howManyPlaying() > 1) {
								tableCards.get(4).setVisible(true);
							}

						}
					}

					reseting();

					if (howManyPlaying() > 1) {

						showDownResult();
					}

					cleaningFromLoosers();

					end();

				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	
}
