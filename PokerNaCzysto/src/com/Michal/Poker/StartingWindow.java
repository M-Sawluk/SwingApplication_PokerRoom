package com.Michal.Poker;

import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class StartingWindow {

	private JFrame window;
	// Area containing game options and name text field
	private JPanel gameSelect;
	// Label for Server Status
	private JLabel serverStatus;
	// Label for Name status
	private JLabel nameStatus;
	// Label for botomPanel, exit, minimize, game vs Bots, game vs Players
	private JLabel botPanel, x, m, topBar, vsBot, vsPlayer;
	// Name input
	private JTextField name;
	// Checks server status
	private Thread tryToConnect;
	private InputStreamReader inReader;
	private BufferedReader buffReader;
	private String namesSelected[] = {};
	private Socket status;

	private MyAnimations anim = new MyAnimations();

	private GameVsBot gvb;
	private GameVsPlayers gvsp;

	// Allows to start game vs players
	private boolean goOnline = false;
	// Turns animations
	private boolean animacje = true;
	// Allows checking server
	private boolean checkServer = true;
	// Holds value of connection status
	private boolean connected = false;
	// Holds value of server status
	private boolean fullServ = false;
	// Helpers to top bar mouse motion
	private int x0;
	private int y0;

	// Creates window
	public void makeGui() throws InterruptedException {
		LookAndFeel();

		window = new JFrame("Select Game");
		window.setUndecorated(true);
		window.setPreferredSize(new Dimension(800, 600));
		window.getContentPane().setBackground(Color.BLACK);
		window.setLayout(null);

		anim.setLocation(0, 20);
		anim.setSize(new Dimension(800, 430));

		name = new JTextField("Enter Your Name");
		name.setLocation(340, 60);
		name.setSize(new Dimension(120, 22));
		name.addMouseListener(new myMouseListener());
		name.setBorder(null);
		name.addKeyListener(new myKey());

		serverStatus = new JLabel("Currently Server is down.", JLabel.CENTER);
		serverStatus.setFont(new Font("TimesRoman", Font.BOLD, 10));
		serverStatus.setForeground(Color.RED);
		serverStatus.setLocation(543, 135);
		serverStatus.setSize(new Dimension(200, 15));

		nameStatus = new JLabel("", JLabel.CENTER);
		nameStatus.setFont(new Font("TimesRoman", Font.BOLD, 10));
		nameStatus.setLocation(300, 80);
		nameStatus.setSize(new Dimension(200, 15));
		nameStatus.setVisible(false);

		botPanel = new JLabel("");
		botPanel.setSize(new Dimension(800, 150));
		botPanel.setLocation(0, 0);
		botPanel.setIcon(new ImageIcon(getClass().getResource("/pics/dolnypanel.png")));

		gameSelect = new JPanel();
		gameSelect.setLocation(0, 450);
		gameSelect.setSize(new Dimension(800, 150));
		gameSelect.setLayout(null);
		gameSelect.add(name);
		gameSelect.add(nameStatus);
		gameSelect.add(serverStatus);
		gameSelect.add(botPanel);

		topBar = new JLabel();
		topBar.setIcon(new ImageIcon(getClass().getResource("/pics/topbar.png")));
		topBar.setLocation(0, 0);
		topBar.setSize(new Dimension(800, 20));
		topBar.addMouseMotionListener(new BarListener());
		topBar.addMouseListener(new myMouseListener());

		x = new JLabel();
		x.setLocation(780, 3);
		x.setSize(new Dimension(20, 20));
		x.addMouseListener(new myMouseListener());

		m = new JLabel();
		m.setLocation(757, 3);
		m.setSize(new Dimension(20, 20));
		m.addMouseListener(new myMouseListener());

		vsBot = new JLabel();
		vsBot.setSize(new Dimension(65, 65));
		vsBot.setBackground(Color.RED);
		vsBot.setLocation(119, 508);
		vsBot.addMouseListener(new myMouseListener());

		vsPlayer = new JLabel();
		vsPlayer.setSize(new Dimension(65, 65));
		vsPlayer.setBackground(Color.RED);
		vsPlayer.setLocation(614, 508);
		vsPlayer.addMouseListener(new myMouseListener());

		window.getContentPane().add(x);
		window.getContentPane().add(m);
		window.getContentPane().add(topBar);
		window.getContentPane().add(gameSelect);
		window.getContentPane().add(anim);
		window.getContentPane().add(vsBot);
		window.getContentPane().add(vsPlayer);

		window.setVisible(true);
		window.setResizable(false);
		window.pack();
		window.setLocationRelativeTo(null);

		Runnable connect = new ConnectionStatus();
		tryToConnect = new Thread(connect);
		tryToConnect.start();

		animations();

	}

	// Loads look and feel
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

	// Moves pictures
	public void animations() {

		anim.startingImg(new ImageIcon(getClass().getResource("/pics/img1.jpg")));

		while (animacje) {
			try {

				Thread.sleep(600);
				if (animacje == false) {
					break;
				}
				anim.movingImg0Right(new ImageIcon(getClass().getResource("/pics/img1.jpg")),
						new ImageIcon(getClass().getResource("/pics/img2.jpg")), 800, 10, 4);
				Thread.sleep(400);
				if (animacje == false) {
					break;
				}
				anim.movingImg0Left(new ImageIcon(getClass().getResource("/pics/img2.jpg")),
						new ImageIcon(getClass().getResource("/pics/img3.jpg")), 800, 10, 4);
				Thread.sleep(400);
				if (animacje == false) {
					break;
				}
				anim.movingImg0Right(new ImageIcon(getClass().getResource("/pics/img3.jpg")),
						new ImageIcon(getClass().getResource("/pics/img4.jpg")), 800, 10, 4);
				Thread.sleep(400);
				if (animacje == false) {
					break;
				}
				anim.movingImg0Left(new ImageIcon(getClass().getResource("/pics/img4.jpg")),
						new ImageIcon(getClass().getResource("/pics/img5.jpg")), 800, 10, 4);
				Thread.sleep(400);
				if (animacje == false) {
					break;
				}
				anim.movingImg0Right(new ImageIcon(getClass().getResource("/pics/img5.jpg")),
						new ImageIcon(getClass().getResource("/pics/img1.jpg")), 800, 10, 4);
				Thread.sleep(400);

			} catch (Exception e) {
			}
			;
		}

	}

	// Key listener for name text field
	class myKey implements KeyListener {

		public void keyPressed(KeyEvent e) {

		}

		public void keyReleased(KeyEvent e) {

			if (name == e.getSource() && connected == true) {

				if (!name.getText().equals(null) && !name.getText().equals("Enter Your Name")
						&& Integer.parseInt(namesSelected[0]) == 0) {

					nameStatus.setVisible(true);
					nameStatus.setForeground(Color.GREEN);
					nameStatus.setText("Name is OK!");
					goOnline = true;
				} else if (Integer.parseInt(namesSelected[0]) > 0 && Integer.parseInt(namesSelected[0]) < 6) {

					for (String x : namesSelected) {
						if (name.getText().equalsIgnoreCase(x) || name.getText().equals("")
								|| name.getText().equals("Enter Your Name")) {
							goOnline = false;
							nameStatus.setVisible(true);
							nameStatus.setForeground(Color.RED);
							nameStatus.setText("Name is not OK!");
						} else {
							nameStatus.setVisible(true);
							nameStatus.setForeground(Color.GREEN);
							nameStatus.setText("Name is OK!");
							goOnline = true;
						}

					}
				} else if (Integer.parseInt(namesSelected[0]) == 6) {
					fullServ = true;
					serverStatus.setText("Server Full!");
					serverStatus.setForeground(Color.ORANGE);

				}

			}
		}

		public void keyTyped(KeyEvent e) {
		}

	}

	// Mouse listener for Labels vsPlayer, vsBot, botPanel, x, m, name
	class myMouseListener implements MouseListener {
		public void mouseClicked(MouseEvent e) {

		}

		public void mouseEntered(MouseEvent e) {

			if (vsPlayer == e.getSource()) {
				botPanel.setIcon(new ImageIcon(getClass().getResource("/pics/dolnypanelgracz.png")));

			}

			if (vsBot == e.getSource()) {
				botPanel.setIcon(new ImageIcon(getClass().getResource("/pics/dolnypanelrobot.png")));
			}

		}

		public void mouseExited(MouseEvent e) {

			botPanel.setIcon(new ImageIcon(getClass().getResource("/pics/dolnypanel.png")));
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
				window.setState(Frame.ICONIFIED);
			}

			if (vsBot == e.getSource()) {

				window.setVisible(false);
				gvb = new GameVsBot();
				checkServer = false;
				animacje = false;

				if (name.getText().equalsIgnoreCase("ENTER YOUR Name") || name.getText().equals("")) {
					gvb.setUserName("NoNejm");
				} else {
					gvb.setUserName(name.getText());
				}

				gvb.gameCreator();

			}

			if (vsPlayer == e.getSource() && goOnline == true && fullServ == false) {

				window.setVisible(false);
				checkServer = false;
				animacje = false;
				try {
					status.close();
				} catch (IOException e1) {}
				gvsp = new GameVsPlayers(name.getText());

			}

			if (name == e.getSource()) {
				name.setText(" ");
				name.requestFocus();
				if (connected == true) {
					try {
						// Sends message to obtain names already selected in
						// String format
						// <number of players>/<name1>/<name2>/...
						PrintWriter print = new PrintWriter(status.getOutputStream(), true);
						print.println(1);
					} catch (IOException e1) {
					}
				}
			}
		}

	}

	// Mouse Motion Listener for top bar
	class BarListener implements MouseMotionListener {

		public void mouseDragged(MouseEvent e) {
			int x = e.getXOnScreen();
			int y = e.getYOnScreen();

			window.setLocation(x - x0, y - y0);

		}

		public void mouseMoved(MouseEvent e) {
		}

	}

	// Checks if server is online and gathers selected names
	public class ConnectionStatus implements Runnable {

		public void run() {

			while (checkServer) {
				try {
					checkServerStatus();

				} catch (InterruptedException e) {
				}
			}

		}

		public void checkServerStatus() throws InterruptedException {

			try {
				status = new Socket("127.0.0.1", 3004);

				serverStatus.setText("Server Available!");
				serverStatus.setForeground(Color.GREEN);

				connected = true;

				inReader = new InputStreamReader(status.getInputStream());

				buffReader = new BufferedReader(inReader);

				String a = "";

				while ((a = buffReader.readLine()) != null) {
					namesSelected = a.split("/");
				}

			}

			catch (IOException e) {
				serverStatus.setText("Currently Server is down.");
				serverStatus.setForeground(Color.RED);
				connected = false;
				nameStatus.setText("Name is OK!");
				nameStatus.setForeground(Color.GREEN);

			}

		}

	}

}