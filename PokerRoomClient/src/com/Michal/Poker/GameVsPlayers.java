package com.Michal.Poker;

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

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.io.*;
import java.net.*;

public class GameVsPlayers {
	// Panel for buttons
	private JFrame game;
	private JPanel buttonArea;
	private JLabel background, topBar, x, m, raiseAm, poolimg, poolValue;
	private JButton raise, fold, call, allin, check, bet;

	// Area that presents game info or massages from other players
	private JTextArea gameInfo;
	// Allow to communicate with players
	private JTextField input;
	// Slider for bet/raise amount
	private JSlider slider;

	private CardGenerator cardGenerator;

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
	private ArrayList<PlayerClient> playerBase = new ArrayList<PlayerClient>();

	// Sockets on different ports
	private Socket s1, s2, s3, s4;

	// Coordinates for top bar
	int x0, y0;
	// Helper that stores recent highest bid
	private int highestBid = 0;
	// pool amount holder
	private int pool;
	// big blind and small blind seats
	private int bb, sb;
	// Stops and start counting under player panel
	private boolean startCounting;
	// Allows player to use buttons
	private boolean allowButtons = false;
	// Time in ms.
	private int t;

	// Adding player to local player base and establishing connections
	GameVsPlayers(String x) {

		PlayerClient pS = new PlayerClient(x);
		playerBase.add(pS);

		try {
			s1 = new Socket("127.0.0.1", 3000);

			Thread.sleep(100);

			s2 = new Socket("127.0.0.1", 3001);

			Thread.sleep(100);

			s3 = new Socket("127.0.0.1", 3002);

			Thread.sleep(100);

			s4 = new Socket("127.0.0.1", 3003);
		} catch (UnknownHostException e) {
		} catch (IOException e) {
		} catch (InterruptedException e) {
		}

		gameCreator();

	}

	// Creates window
	public void gameCreator() {

		LookAndFeel();

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

		x = new JLabel();
		x.setLocation(780, 0);
		x.setSize(new Dimension(20, 20));
		x.addMouseListener(new myMouseListener());

		m = new JLabel();
		m.setLocation(757, 0);
		m.setSize(new Dimension(20, 20));
		m.addMouseListener(new myMouseListener());

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

		poolimg = new JLabel();
		poolimg.setIcon(new ImageIcon(getClass().getResource("/pics/pool.png")));
		poolimg.setSize(new Dimension(120, 120));
		poolimg.setLocation(345, 285);
		poolimg.setVisible(false);

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

		raiseAm = new JLabel("Raise for :0", JLabel.CENTER);
		raiseAm.setSize(new Dimension(400, 10));
		raiseAm.setFont(new Font("Arial", Font.BOLD, 10));
		raiseAm.setLocation(0, 85);
		raiseAm.setForeground(Color.WHITE);

		buttonArea.add(input);
		buttonArea.add(pane);
		buttonArea.add(slider);
		buttonArea.add(raiseAm);

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
		game.getContentPane().add(poolimg);
		game.getContentPane().add(cardstack);
		game.getContentPane().add(background);
		game.getContentPane().add(m);
		game.getContentPane().add(x);
		game.getContentPane().add(topBar);
		game.getContentPane().add(buttonArea);
		game.setVisible(true);
		game.pack();

		startConnection();

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
			if (x == e.getSource()) {
				System.exit(0);
			}
			if (m == e.getSource()) {
				game.setState(Frame.ICONIFIED);
			}
		}

	}

	// Listener for buttons
	class MyActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			if (e.getSource() == buttons.get(1) && allowButtons == true) {
				call(0);
			}
			if (e.getSource() == buttons.get(2) && allowButtons == true) {
				check(0);
			}
			if (e.getSource() == buttons.get(4) && allowButtons == true) {
				allIn(0);
			}
			if (e.getSource() == buttons.get(3) && allowButtons == true) {
				fold(0);
			}
			if (e.getSource() == buttons.get(5) && allowButtons == true) {
				raise(0);
			}
			if (e.getSource() == buttons.get(0) && allowButtons == true) {
				bet(0);
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

			slider.setMaximum(playerBase.get(0).getChips());

			slider = (JSlider) e.getSource();

			if (!slider.getValueIsAdjusting()) {
				raiseAm.setText("Raise for :" + slider.getValue());

			}

		}

	}

	// Listener for input text field
	// Code 504 port 3002
	public class myKeyListener implements KeyListener {

		public void keyPressed(KeyEvent e) {

		}

		public void keyReleased(KeyEvent e) {
			if (input.getText().length() > 0 && !input.getText().equals("Send a message")) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					String s = "504/" + playerBase.get(0).getName() + " said: " + input.getText();

					input.setText("");

					instToServer3002(s);
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

		for (int i = 0; i < 6; i++) {
			JLabel panel = new JLabel();
			panel.setLocation(x[i], y[i]);
			panel.setSize(new Dimension(400, 400));
			panel.setIcon(new ImageIcon(getClass().getResource("/pics/smile.png")));
			panel.setVisible(false);
			userPanel.add(panel);
		}

		return userPanel;
	}

	// Setting actions with coordinates
	public ArrayList<JLabel> setActions() {
		actions = new ArrayList<JLabel>();

		int[] x = { 98, 109, 315, 545, 730, 748 };
		int[] y = { 335, 176, 115, 115, 174, 333 };

		for (int i = 0; i < 6; i++) {
			JLabel action = new JLabel("");
			action.setSize(new Dimension(100, 22));
			action.setForeground(Color.RED);
			action.setFont(new Font("Arial", Font.BOLD, 12));
			action.setLocation(x[i], y[i]);
			action.setVisible(false);
			actions.add(action);
		}

		return actions;
	}

	// Setting card1 with coordinates
	public ArrayList<JLabel> setCard1() {
		card1 = new ArrayList<JLabel>();

		int[] x = { 105, 114, 322, 430, 613, 634 };
		int[] y = { 280, 120, 60, 60, 120, 280 };

		for (int i = 0; i < 6; i++) {
			JLabel card = new JLabel();
			card.setSize(new Dimension(30, 45));
			card.setLocation(x[i], y[i]);
			card.setIcon(new ImageIcon(getClass().getResource("/pics/cardback.png")));
			card.setVisible(false);
			card1.add(card);
		}

		return card1;
	}

	// Setting card2 with coordinates
	public ArrayList<JLabel> setCard2() {
		card2 = new ArrayList<JLabel>();

		int[] x = { 136, 146, 353, 461, 644, 665 };
		int[] y = { 280, 120, 60, 60, 120, 280 };

		for (int i = 0; i < 6; i++) {

			JLabel card = new JLabel();
			card.setSize(new Dimension(30, 45));
			card.setLocation(x[i], y[i]);
			card.setIcon(new ImageIcon(getClass().getResource("/pics/cardback.png")));
			card.setVisible(false);
			card2.add(card);

		}

		return card2;
	}

	// Setting names with coordinates
	public ArrayList<JLabel> setNames() {
		name = new ArrayList<JLabel>();

		int[] x = { 8, 19, 225, 455, 640, 658 };
		int[] y = { 335, 176, 115, 115, 174, 333 };

		for (int i = 0; i < 6; i++) {
			JLabel name1 = new JLabel("");
			name1.setSize(new Dimension(100, 22));
			name1.setForeground(Color.white);
			name1.setFont(new Font("Arial", Font.BOLD, 12));
			name1.setLocation(x[i], y[i]);
			name1.setVisible(false);
			name.add(name1);
		}

		return name;
	}

	// Setting chips with coordinates
	public ArrayList<JLabel> setChips() {
		chips = new ArrayList<JLabel>();

		int[] x = { 8, 19, 225, 455, 640, 658 };
		int[] y = { 355, 196, 135, 135, 194, 353 };

		for (int i = 0; i < 6; i++) {
			JLabel chips1 = new JLabel("");
			chips1.setSize(new Dimension(100, 22));
			chips1.setForeground(Color.white);
			chips1.setFont(new Font("Arial", Font.BOLD, 12));
			chips1.setLocation(x[i], y[i]);
			chips1.setVisible(false);
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

		for (int i = 0; i < 6; i++) {
			JLabel bid1 = new JLabel();
			bid1.setSize(new Dimension(100, 100));
			bid1.setLocation(x[i], y[i]);
			bid1.setIcon(new ImageIcon(getClass().getResource("/pics/chips.png")));
			bid1.setVisible(false);
			bids.add(bid1);
		}

		return bids;

	}

	// Setting bid chips with coordinates
	public ArrayList<JLabel> setBidChips() {
		bidChips = new ArrayList<JLabel>();

		int[] x = { 190, 190, 270, 460, 550, 550 };
		int[] y = { 295, 200, 170, 170, 200, 280 };

		for (int i = 0; i < 6; i++) {
			JLabel bid2 = new JLabel("", JLabel.CENTER);
			bid2.setSize(new Dimension(40, 100));
			bid2.setLocation(x[i], y[i] + 30);
			bid2.setFont(new Font("Arial", Font.BOLD, 10));
			bid2.setVisible(false);
			bidChips.add(bid2);
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

	// Method displaying counting under player panel
	public void counting(int x) {
		startCounting = true;

		try {
			if (startCounting == true) {
				userPanel.get(x - 1).setIcon(new ImageIcon(getClass().getResource("/pics/smile10.png")));
				Thread.sleep(t);
			}
			if (startCounting == true) {
				userPanel.get(x - 1).setIcon(new ImageIcon(getClass().getResource("/pics/smile9.png")));
				Thread.sleep(t);
			}
			if (startCounting == true) {
				userPanel.get(x - 1).setIcon(new ImageIcon(getClass().getResource("/pics/smile8.png")));
				Thread.sleep(t);
			}
			if (startCounting == true) {
				userPanel.get(x - 1).setIcon(new ImageIcon(getClass().getResource("/pics/smile7.png")));
				Thread.sleep(t);
			}
			if (startCounting == true) {

				userPanel.get(x - 1).setIcon(new ImageIcon(getClass().getResource("/pics/smile6.png")));
				Thread.sleep(t);
			}
			if (startCounting == true) {

				userPanel.get(x - 1).setIcon(new ImageIcon(getClass().getResource("/pics/smile5.png")));
				Thread.sleep(t);
			}
			if (startCounting == true) {

				userPanel.get(x - 1).setIcon(new ImageIcon(getClass().getResource("/pics/smile4.png")));
				Thread.sleep(t);
			}

			if (startCounting == true) {

				userPanel.get(x - 1).setIcon(new ImageIcon(getClass().getResource("/pics/smile3.png")));
				Thread.sleep(t);
			}
			if (startCounting == true) {

				userPanel.get(x - 1).setIcon(new ImageIcon(getClass().getResource("/pics/smile2.png")));
				Thread.sleep(t);
			}
			if (startCounting == true) {

				userPanel.get(x - 1).setIcon(new ImageIcon(getClass().getResource("/pics/smile1.png")));
				Thread.sleep(t);
			}
			if (startCounting == true) {
				userPanel.get(x - 1).setIcon(new ImageIcon(getClass().getResource("/pics/smile0.png")));
				Thread.sleep(t);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		userPanel.get(x - 1).setIcon(new ImageIcon(getClass().getResource("/pics/smile.png")));

		for (int i = 0; i < playerBase.size(); i++) {
			if (playerBase.get(0).getBid() == highestBid && startCounting == true && playerBase.get(0).getSeat() == x) {
				userPanel.get(x - 1).setIcon(new ImageIcon(getClass().getResource("/pics/smile.png")));
				allowButtons = false;
				check(0);

			} else if (playerBase.get(0).getBid() != highestBid && startCounting == true
					&& playerBase.get(0).getSeat() == x) {
				userPanel.get(x - 1).setIcon(new ImageIcon(getClass().getResource("/pics/smile.png")));
				allowButtons = false;
				fold(0);
			}

		}

	}

	// Method for call button
	// Code 503 port 3001
	private void call(int x) {
		if (playerBase.get(x).getBid() == highestBid || highestBid == -1) {
			check(x);
			allowButtons = false;
		}

		else if (playerBase.get(x).getChips() + playerBase.get(x).getBid() >= highestBid) {
			playerBase.get(x).setChips((highestBid - playerBase.get(x).getBid()));

			chips.get(playerBase.get(x).getSeat() - 1).setText("Chips : " + playerBase.get(x).getChips());

			actions.get(playerBase.get(x).getSeat() - 1).setVisible(true);
			actions.get(playerBase.get(x).getSeat() - 1).setText("Call");

			String s = "503/" + "Call/" + playerBase.get(x).getSeat() + "/" + playerBase.get(x).getChips() + "/"
					+ (highestBid - playerBase.get(x).getBid());

			playerBase.get(x).setBid(highestBid);

			bids.get(playerBase.get(x).getSeat() - 1).setVisible(true);

			bidChips.get(playerBase.get(x).getSeat() - 1).setText("" + playerBase.get(x).getBid());
			bidChips.get(playerBase.get(x).getSeat() - 1).setVisible(true);

			instToServer3001(s);

			allowButtons = false;
			startCounting = false;

		} else if (playerBase.get(x).getChips() + playerBase.get(x).getBid() < highestBid) {
			allIn(x);
		}

	}

	// Method for check button
	// Code 503 port 3001
	private void check(int x) {

		if (highestBid == -1) {
			playerBase.get(x).setBid(0);
			actions.get(playerBase.get(x).getSeat() - 1).setText("Check");
			actions.get(playerBase.get(x).getSeat() - 1).setVisible(true);

			String s = "503/" + "Check/" + playerBase.get(x).getSeat() + "/" + playerBase.get(x).getChips() + "/" + 0;

			instToServer3001(s);

			allowButtons = false;
			startCounting = false;
		}

		else if (playerBase.get(x).getBid() != highestBid && highestBid > 0) {
			gameInfo.append(playerBase.get(x).getName() + " You Cant Check! \n");
			actions.get(playerBase.get(x).getSeat() - 1).setText("ERROR");
			actions.get(playerBase.get(x).getSeat() - 1).setVisible(true);

		} else {
			actions.get(playerBase.get(x).getSeat() - 1).setText("Check");
			actions.get(playerBase.get(x).getSeat() - 1).setVisible(true);

			String s = "503/" + "Check/" + playerBase.get(x).getSeat() + "/" + playerBase.get(x).getChips() + "/" + 0;

			instToServer3001(s);

			startCounting = false;
			allowButtons = false;
		}
	}

	// Method for fold button
	// Code 503 port 3001
	private void fold(int x) {
		playerBase.get(x).setMove(false);

		actions.get(playerBase.get(x).getSeat() - 1).setText("Fold");
		actions.get(playerBase.get(x).getSeat() - 1).setVisible(true);
		allowButtons = false;
		startCounting = false;
		card1.get(playerBase.get(x).getSeat() - 1).setVisible(false);
		card2.get(playerBase.get(x).getSeat() - 1).setVisible(false);

		String s = "503/" + "Fold/" + playerBase.get(x).getSeat() + "/" + playerBase.get(x).getChips() + "/" + 0;

		instToServer3001(s);
	}

	// Method for raise button
	// Code 503 port 3001
	private void raise(int x) {

		if (highestBid == -1) {
			highestBid = 0;
		}

		int s = slider.getValue();

		if ((s + playerBase.get(x).getBid()) > highestBid) {
			playerBase.get(x).setChips(s);

			chips.get(playerBase.get(x).getSeat() - 1).setText("Chips : " + playerBase.get(x).getChips());

			String r = "503/" + "Raise/" + playerBase.get(x).getSeat() + "/" + playerBase.get(x).getChips() + "/" + s;

			playerBase.get(x).setBid(s + playerBase.get(x).getBid());

			actions.get(playerBase.get(x).getSeat() - 1).setText("Raise");
			actions.get(playerBase.get(x).getSeat() - 1).setVisible(true);

			bids.get(playerBase.get(x).getSeat() - 1).setVisible(true);

			bidChips.get(playerBase.get(x).getSeat() - 1).setText("" + playerBase.get(x).getBid());
			bidChips.get(playerBase.get(x).getSeat() - 1).setVisible(true);

			highestBid = playerBase.get(x).getBid();

			instToServer3001(r);

			allowButtons = false;
			startCounting = false;
		} else if ((s + playerBase.get(x).getBid()) <= highestBid) {
			actions.get(playerBase.get(x).getSeat() - 1).setText("ERROR");
			actions.get(playerBase.get(x).getSeat() - 1).setVisible(true);

			gameInfo.append(playerBase.get(x).getName() + " Raise More! \n");
		}

	}

	// Method for all in button
	// Code 503 port 3001
	private void allIn(int x) {
		if ((playerBase.get(x).getChips() + playerBase.get(x).getBid()) >= highestBid) {
			highestBid = playerBase.get(x).getChips();

			String s = "503/" + "Allin/" + playerBase.get(x).getSeat() + "/" + playerBase.get(x).getChips() + "/"
					+ playerBase.get(x).getChips();

			playerBase.get(x).setBid(playerBase.get(x).getChips() + playerBase.get(x).getBid());

			bids.get(playerBase.get(x).getSeat() - 1).setVisible(true);

			playerBase.get(x).setChips(playerBase.get(x).getChips());

			chips.get(playerBase.get(x).getSeat() - 1).setText("Chips : " + playerBase.get(x).getChips());

			highestBid = playerBase.get(x).getBid();

			actions.get(playerBase.get(x).getSeat() - 1).setText("All In");
			actions.get(playerBase.get(x).getSeat() - 1).setVisible(true);

			bidChips.get(playerBase.get(x).getSeat() - 1).setText("" + playerBase.get(x).getBid());
			bidChips.get(playerBase.get(x).getSeat() - 1).setVisible(true);

			instToServer3001(s);

			allowButtons = false;
			startCounting = false;
		}

		else if ((playerBase.get(x).getChips() + playerBase.get(x).getBid()) < highestBid
				&& playerBase.get(x).getChips() > 0) {

			String s = "503/" + "Allin/" + playerBase.get(x).getSeat() + "/" + playerBase.get(x).getChips() + "/"
					+ playerBase.get(x).getChips();

			playerBase.get(x).setBid(playerBase.get(x).getChips() + playerBase.get(x).getBid());

			playerBase.get(x).setChips(playerBase.get(x).getChips());

			bids.get(playerBase.get(x).getSeat() - 1).setVisible(true);

			chips.get(playerBase.get(x).getSeat() - 1).setText("Chips : " + playerBase.get(x).getChips());

			actions.get(playerBase.get(x).getSeat() - 1).setText("All In");
			actions.get(playerBase.get(x).getSeat() - 1).setVisible(true);

			bidChips.get(playerBase.get(x).getSeat() - 1).setText("" + playerBase.get(x).getBid());
			bidChips.get(playerBase.get(x).getSeat() - 1).setVisible(true);

			instToServer3001(s);

			allowButtons = false;
			startCounting = false;
		}

	}

	// Method for bet button
	// Code 503 port 3001
	private void bet(int x) {

		if (highestBid > 0) {
			raise(x);
		}

		else if (highestBid <= 0 && slider.getValue() > 0) {

			int s = slider.getValue();

			playerBase.get(x).setChips(s);

			String r = "503/" + "Bet/" + playerBase.get(x).getSeat() + "/" + playerBase.get(x).getChips() + "/" + s;

			chips.get(playerBase.get(x).getSeat() - 1).setText("Chips : " + playerBase.get(x).getChips());

			playerBase.get(x).setBid(s + playerBase.get(x).getBid());

			actions.get(playerBase.get(x).getSeat() - 1).setText("Bet");
			actions.get(playerBase.get(x).getSeat() - 1).setVisible(true);

			bids.get(playerBase.get(x).getSeat() - 1).setVisible(true);

			bidChips.get(playerBase.get(x).getSeat() - 1).setText("" + playerBase.get(x).getBid());
			bidChips.get(playerBase.get(x).getSeat() - 1).setVisible(true);

			highestBid = playerBase.get(x).getBid();

			instToServer3001(r);

			allowButtons = false;
			startCounting = false;
		}

	}

	// Starting Threads with Server listeners
	// and sending local player data to server
	// Code 501 port 3003
	private void startConnection() {

		instToServer3000("501/" + playerBase.get(0).getName() + "/" + playerBase.size());

		Thread nasluchiwacz3000 = new Thread(new ServerListener3000());
		nasluchiwacz3000.setName("Nasluch");
		nasluchiwacz3000.start();

		Thread nasluchiwacz3001 = new Thread(new ServerListener3001());
		nasluchiwacz3001.setName("Nasluch");
		nasluchiwacz3001.start();

		Thread nasluchiwacz3002 = new Thread(new ServerListener3002());
		nasluchiwacz3002.setName("Nasluch");
		nasluchiwacz3002.start();

		Thread nasluchiwacz3003 = new Thread(new ServerListener3003());
		nasluchiwacz3003.setName("Nasluch");
		nasluchiwacz3003.start();

	}

	// Thread listening on port 3000 for instructions
	// code 101
	// info setting local player hand, seat, timer, big blind amount,small blind
	// amount
	// code 104
	// info with small blind seat, big blind seat and dealer seat
	// code 105
	// info player move
	// code 107
	// info with pool value
	// code 108
	// reset actions order
	// code 112
	// info with table cards
	// code 115
	// pool hide order
	class ServerListener3000 implements Runnable {

		public void run() {
			try {
				while (true) {
					String k = "";

					InputStreamReader is = new InputStreamReader(s1.getInputStream());
					BufferedReader br = new BufferedReader(is);
					if ((k = br.readLine()) != null) {
						String[] data = k.split("/");

						if (data[0].equals("101")) {
							setLocalData(data);
						}
						if (data[0].equals("104")) {
							setBlinds(data);
						}
						if (data[0].equals("105")) {
							playerMove(data);
						}
						if (data[0].equals("107")) {
							setPool(data);
						}
						if (data[0].equals("108")) {
							resetActions();
						}
						if (data[0].equals("112")) {
							addTableCards(data);
						}
						if (data[0].equals("115")) {
							poolHide();
						}

					}
				}
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	// code 106
	// info with recently joined player
	// Thread listening on port 3001 for instructions
	// code 110
	// info with pool split
	// code 113
	// with winner info before show down
	class ServerListener3001 implements Runnable {

		public void run() {
			try {
				while (true) {
					String k = "";

					InputStreamReader is = new InputStreamReader(s2.getInputStream());
					BufferedReader br = new BufferedReader(is);
					if ((k = br.readLine()) != null) {
						String[] data = k.split("/");

						if (data[0].equals("106")) {
							updatePlayer(data);
						}
						if (data[0].equals("110")) {
							splitPools(data);
						}
						if (data[0].equals("113")) {
							addPool(data);
						}

					}
				}
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	// Thread listening on port 3002 for instructions
	// code 111
	// info with winner card combination
	// code 109
	// setting action player disconnected
	class ServerListener3002 implements Runnable {

		public void run() {
			try {
				while (true) {
					String k = "";

					InputStreamReader is = new InputStreamReader(s3.getInputStream());
					BufferedReader br = new BufferedReader(is);
					if ((k = br.readLine()) != null) {
						String[] data = k.split("/");

						if (data[0].equals("111")) {
							addMsg(data[1]);
						}
						if (data[0].equals("109")) {
							setDisconected(data[1]);
						}

					}
				}
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	// Thread listening on port 3003 for instructions
	// code 102
	// adding remote players
	// code 103
	// info with player to be removed
	// code 114
	// sending info with all cards
	// code 116
	// info with new cards for local player
	class ServerListener3003 implements Runnable {

		public void run() {
			try {
				while (true) {
					String k = "";

					InputStreamReader is = new InputStreamReader(s4.getInputStream());
					BufferedReader br = new BufferedReader(is);
					if ((k = br.readLine()) != null) {
						String[] data = k.split("/");

						if (data[0].equals("102")) {
							fillPlayerBase(data);
						}

						if (data[0].equals("103")) {
							removePlayer(data);
						}
						if (data[0].equals("114")) {
							setAllCards(data);
						}
						if (data[0].equals("116")) {
							setNewCards(data);
						}

					}
				}
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public void instToServer3000(String x) {

		try {

			PrintWriter pw = new PrintWriter(s1.getOutputStream(), true);

			pw.println(x);
			pw.flush();

		}

		catch (UnknownHostException e) {
		} catch (IOException e) {
		}

	}

	public void instToServer3001(String x) {

		try {

			PrintWriter pw = new PrintWriter(s2.getOutputStream(), true);

			pw.println(x);
			pw.flush();

		}

		catch (UnknownHostException e) {
		} catch (IOException e) {
		}

	}

	public void instToServer3002(String x) {

		try {

			PrintWriter pw = new PrintWriter(s3.getOutputStream(), true);

			pw.println(x);
			pw.flush();

		}

		catch (UnknownHostException e) {
		} catch (IOException e) {
		}

	}

	public void instToServer3003(String x) {

		try {

			PrintWriter pw = new PrintWriter(s4.getOutputStream(), true);

			pw.println(x);
			pw.flush();

		}

		catch (UnknownHostException e) {
		} catch (IOException e) {
		}

	}

	// Setting local player data
	public void setLocalData(String[] x) {

		playerBase.get(0).chips(Integer.parseInt(x[1]));
		playerBase.get(0).setHand(x[2]);
		playerBase.get(0).setHand(x[3]);
		playerBase.get(0).setSeat(Integer.parseInt(x[4]));
		playerBase.get(0).setInNet(x[6]);
		t = Integer.parseInt(x[7]);
		bb = Integer.parseInt(x[8]);
		sb = Integer.parseInt(x[9]);

		card1.get(playerBase.get(0).getSeat() - 1).setIcon(
				new ImageIcon(getClass().getResource("/" + new Iconz().giveIcon(playerBase.get(0).getHand().get(0)))));
		card2.get(playerBase.get(0).getSeat() - 1).setIcon(
				new ImageIcon(getClass().getResource("/" + new Iconz().giveIcon(playerBase.get(0).getHand().get(1)))));

		setVisibleTrue(Integer.parseInt(x[4]) - 1, playerBase.get(0).getName(), playerBase.get(0).getChips());

	}

	// Populating data base with remote players
	public void fillPlayerBase(String[] x) {

		PlayerClient next = new PlayerClient(x[1], Integer.parseInt(x[2]), Integer.parseInt(x[3]), x[4]);
		playerBase.add(next);
		setVisibleTrue(Integer.parseInt(x[3]) - 1, x[1], Integer.parseInt(x[2]));

	}

	// Removing disconnected player
	public void removePlayer(String[] x) {
		for (int i = 0; i < playerBase.size(); i++) {
			if (playerBase.get(i).getName().equals(x[1])) {
				setVisibleFalse(playerBase.get(i).getSeat() - 1);
				playerBase.remove(i);
			}
			;

		}
	}

	// Setting whole user panel visible
	public void setVisibleTrue(int x, String y, int z) {
		userPanel.get(x).setVisible(true);
		card1.get(x).setVisible(true);
		card2.get(x).setVisible(true);
		name.get(x).setVisible(true);
		name.get(x).setText(y);
		chips.get(x).setVisible(true);
		chips.get(x).setText("Chips : " + z);
		actions.get(x).setVisible(true);
	}

	// Setting whole user panel invisible
	public void setVisibleFalse(int x) {
		userPanel.get(x).setVisible(false);
		card1.get(x).setVisible(false);
		card2.get(x).setVisible(false);
		name.get(x).setVisible(false);
		name.get(x).setText("");
		chips.get(x).setVisible(false);
		chips.get(x).setText("");
		actions.get(x).setVisible(false);
	}

	public void setBlinds(String[] x) {
		int y = 0;
		int z = 0;

		for (int i = 0; i < playerBase.size(); i++) {
			if (playerBase.get(i).getMove() == true && playerBase.get(i).getRemote() == false) {
				card1.get(playerBase.get(i).getSeat() - 1).setVisible(true);
				card2.get(playerBase.get(i).getSeat() - 1).setVisible(true);
			}
			if (playerBase.get(i).getMove() == true && playerBase.get(i).getRemote() == true) {
				card1.get(playerBase.get(i).getSeat() - 1).setVisible(true);
				card1.get(playerBase.get(i).getSeat() - 1)
						.setIcon(new ImageIcon(getClass().getResource("/pics/cardback.png")));

				card2.get(playerBase.get(i).getSeat() - 1).setVisible(true);
				card2.get(playerBase.get(i).getSeat() - 1)
						.setIcon(new ImageIcon(getClass().getResource("/pics/cardback.png")));
			}
		}

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}

		for (int i = 0; i < playerBase.size(); i++) {
			if (playerBase.get(i).getSeat() == Integer.parseInt(x[1])) {

				if (playerBase.get(i).getChips() >= sb) {
					playerBase.get(i).setChips(sb);
					playerBase.get(i).setBid(sb);
					y = sb;
				} else {
					playerBase.get(i).setBid(playerBase.get(i).getChips());
					playerBase.get(i).setChips(playerBase.get(i).getChips());
					y = playerBase.get(i).getBid();
				}

				chips.get(playerBase.get(i).getSeat() - 1).setText("Chips : " + playerBase.get(i).getChips());

				bids.get(playerBase.get(i).getSeat() - 1).setVisible(true);

				actions.get(playerBase.get(i).getSeat() - 1).setVisible(true);
				actions.get(playerBase.get(i).getSeat() - 1).setText("SB");

				bidChips.get(playerBase.get(i).getSeat() - 1).setText("" + playerBase.get(i).getBid());
				bidChips.get(playerBase.get(i).getSeat() - 1).setVisible(true);

			}

		}

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}

		for (int i = 0; i < playerBase.size(); i++) {
			if (playerBase.get(i).getSeat() == Integer.parseInt(x[2])) {
				if (playerBase.get(i).getChips() >= bb) {
					playerBase.get(i).setChips(bb);
					playerBase.get(i).setBid(bb);
					z = bb;
				} else {
					playerBase.get(i).setBid(playerBase.get(i).getChips());
					playerBase.get(i).setChips(playerBase.get(i).getChips());
					z = playerBase.get(i).getBid();
				}

				chips.get(playerBase.get(i).getSeat() - 1).setText("Chips : " + playerBase.get(i).getChips());

				bids.get(playerBase.get(i).getSeat() - 1).setVisible(true);

				actions.get(playerBase.get(i).getSeat() - 1).setVisible(true);
				actions.get(playerBase.get(i).getSeat() - 1).setText("BB");

				bidChips.get(playerBase.get(i).getSeat() - 1).setText("" + playerBase.get(i).getBid());
				bidChips.get(playerBase.get(i).getSeat() - 1).setVisible(true);

			}

		}

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}

		dealerButton.get(Integer.parseInt(x[3]) - 1).setVisible(true);

		if (y >= z)
			highestBid = y;
		else
			highestBid = z;

	}

	// Launches counter under specific player
	// If it is local player move enables buttons
	public void playerMove(String[] x) {

		if (Integer.parseInt(x[1]) == playerBase.get(0).getSeat()) {
			allowButtons = true;
		}

		counting(Integer.parseInt(x[1]));

	}

	// Adding recently joined player
	public void updatePlayer(String[] x) {
		int y = Integer.parseInt(x[2]);
		int z = Integer.parseInt(x[4]);

		// ins / action / seaat / chips/bid

		startCounting = false;

		for (int i = 0; i < playerBase.size(); i++) {
			if (playerBase.get(i).getSeat() == y && playerBase.get(i).getRemote() == true) {
				playerBase.get(i).setChips(z);

				playerBase.get(i).setBid(playerBase.get(i).getBid() + z);

				chips.get(playerBase.get(i).getSeat() - 1).setText("Chips : " + playerBase.get(i).getChips());

				if (z > 0) {
					bids.get(playerBase.get(i).getSeat() - 1).setVisible(true);

					bidChips.get(playerBase.get(i).getSeat() - 1).setText("" + playerBase.get(i).getBid());
					bidChips.get(playerBase.get(i).getSeat() - 1).setVisible(true);
				}

				actions.get(playerBase.get(i).getSeat() - 1).setVisible(true);
				actions.get(playerBase.get(i).getSeat() - 1).setText(x[1]);

				if (x[1].equalsIgnoreCase("fold")) {
					playerBase.get(i).setMove(false);
					card1.get(playerBase.get(i).getSeat() - 1).setVisible(false);
					card2.get(playerBase.get(i).getSeat() - 1).setVisible(false);
				}

				if (playerBase.get(i).getBid() > highestBid) {
					highestBid = playerBase.get(i).getBid();

				}
			}
		}

	}

	public void resetActions() {
		highestBid = -1;

		for (int i = 0; i < playerBase.size(); i++) {
			playerBase.get(i).setBid(0);

			bids.get(playerBase.get(i).getSeat() - 1).setVisible(false);

			bidChips.get(playerBase.get(i).getSeat() - 1).setVisible(false);

			actions.get(playerBase.get(i).getSeat() - 1).setVisible(false);

		}

	}

	public void setPool(String[] x) {
		poolimg.setVisible(true);
		poolValue.setVisible(true);
		poolValue.setText(x[1]);
	}

	public void splitPools(String[] x) {

		int[] y = new int[7];

		for (int i = 1; i < 7; i++) {
			y[i] = Integer.parseInt(x[i]);
		}

		for (int i = 0; i < y.length; i++) {
			if (y[i] != 0) {
				for (int j = 0; j < playerBase.size(); j++) {
					if (playerBase.get(j).getSeat() == i) {
						playerBase.get(j).setChips(-y[i]);
					}

				}

			}

		}

	}

	public void addMsg(String x) {

		String k = gameInfo.getText();

		gameInfo.setText("");

		gameInfo.append(x + "\n");

		gameInfo.append(k + "\n");

	}

	public void addPool(String[] x) {

		for (int i = 0; i < playerBase.size(); i++) {

			if (playerBase.get(i).getSeat() == Integer.parseInt(x[1])) {
				playerBase.get(i).setChips(-(Integer.parseInt(x[2])));
				chips.get(playerBase.get(i).getSeat() - 1).setText("Chips :" + playerBase.get(i).getChips());
			}

		}

	}

	public void addTableCards(String[] x) {
		for (int i = 1; i < x.length; i++) {
			tableCards.get(i - 1).setVisible(true);
			tableCards.get(i - 1).setIcon(new ImageIcon(getClass().getResource("/" + new Iconz().giveIcon(x[i]))));
		}

	}

	public void poolHide() {
		poolValue.setVisible(false);
		poolimg.setVisible(false);

		for (int i = 0; i < playerBase.size(); i++) {
			card1.get(playerBase.get(i).getSeat() - 1).setVisible(false);
			card2.get(playerBase.get(i).getSeat() - 1).setVisible(false);
			chips.get(playerBase.get(i).getSeat() - 1).setText("Chips : " + playerBase.get(i).getChips());
			playerBase.get(i).getHand().clear();

			if (playerBase.get(i).getChips() > 0) {
				playerBase.get(i).setMove(true);
			} else if (playerBase.get(i).getChips() == 0) {
				playerBase.get(i).setMove(false);
			}
		}
		for (int i = 0; i < tableCards.size(); i++) {
			tableCards.get(i).setVisible(false);
			bidChips.get(i).setVisible(false);
			bids.get(i).setVisible(false);
		}
	}

	// Reveals all table cards
	public void setAllCards(String[] x) {
		int seat = Integer.parseInt(x[1]);


		for (int i = 0; i < playerBase.size(); i++) {

			if (playerBase.get(i).getSeat() == seat && playerBase.get(i).getRemote() == true) {
				card1.get(seat - 1).setIcon(new ImageIcon(getClass().getResource("/" + new Iconz().giveIcon(x[2]))));
				card2.get(seat - 1).setIcon(new ImageIcon(getClass().getResource("/" + new Iconz().giveIcon(x[3]))));
			}

		}

	}

	public void setNewCards(String[] x) {
		for (int i = 0; i < playerBase.size(); i++) {

			if (playerBase.get(i).getSeat() == Integer.parseInt(x[1])) {
				card1.get(playerBase.get(i).getSeat() - 1)
						.setIcon(new ImageIcon(getClass().getResource("/" + new Iconz().giveIcon(x[2]))));
				card2.get(playerBase.get(i).getSeat() - 1)
						.setIcon(new ImageIcon(getClass().getResource("/" + new Iconz().giveIcon(x[3]))));
				playerBase.get(i).setChipsInvested(-playerBase.get(i).getChipsInvested());
			}

		}

		for (JLabel z : dealerButton) {
			z.setVisible(false);
		}
	}

	private void setDisconected(String x) {

		for (int i = 0; i < playerBase.size(); i++) {
			if (playerBase.get(i).getSeat() == Integer.parseInt(x)) {
				actions.get(playerBase.get(i).getSeat() - 1).setText("Dc'ed");

			}
		}

	}

}
