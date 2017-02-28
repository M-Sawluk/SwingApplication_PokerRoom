package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;

public class PokerServer {
	private CardGenerator cards = new CardGenerator();

	private ShowDownWinner sHDW = new ShowDownWinner();

	// Stores remote connected players
	private ArrayList<PlayerServer> playerBase = new ArrayList<PlayerServer>();
	// Temporary stores player hand
	private ArrayList<String> playerHand = new ArrayList<String>();
	// Stores seats
	private ArrayList<Integer> seats = new ArrayList<Integer>();
	// Stores printers for name check
	private ArrayList<PrintWriter> startingWindowNameCheck = new ArrayList<PrintWriter>();

	private int playerNumber;
	private int uniquePlayerNumber = -1;
	private int tour = -1;
	private int highestBid = 0;
	private int sbSeat, bbSeat, sb = 0, dealerSeat = 1;
	private int pool;
	private int timer = 1000;
	private int smallBlind = 500;
	private int bigBlind = 1000;
	private int moving = 0;

	private int[] winners;

	private boolean playerChoosing = false;

	public static void main(String[] args) throws InterruptedException {
		PokerServer pokServ = new PokerServer();

		pokServ.startServerSockets();
	}

	PokerServer() {
		for (int i = 1; i <= 6; i++) {
			seats.add(i);
		}

		Thread sp = new Thread(new CheckIfPlayerDisconnected());
		sp.start();

		Thread ramka = new Thread(new StartingWindowServerSocket());
		ramka.start();

		Thread game = new Thread(new TheGame());
		game.start();

	}

	private class SocketListener3000 implements Runnable {

		BufferedReader read;
		Socket socket;

		public SocketListener3000(Socket clientSocket) {
			try {
				socket = clientSocket;
				InputStreamReader isReader = new InputStreamReader(socket.getInputStream());
				read = new BufferedReader(isReader);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}

		public void run() {
			int x = playerNumber;
			playerNumber++;
			try {
				String k = "";

				while ((k = read.readLine()) != null) {
					String[] playerInfo = k.split("/");

					if (playerInfo[0].equals("501")) {
						addPlayer(playerInfo, socket.getInetAddress().toString());

						try {
							Thread.sleep(300);
							sendPlayerBase();
						} catch (InterruptedException e) {
						}

					}

				}

			}

			catch (IOException e) {
				uniquePlayerNumber = x;
			}
		}

	}

	private class SocketListener3001 implements Runnable {
		BufferedReader read;
		Socket socket;

		public SocketListener3001(Socket clientSocket) {
			try {
				socket = clientSocket;
				InputStreamReader isReader = new InputStreamReader(socket.getInputStream());
				read = new BufferedReader(isReader);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}

		public void run() {

			try {
				String k = "";

				while ((k = read.readLine()) != null) {
					String[] playerInfo = k.split("/");

					if (playerInfo[0].equals("503")) {
						String s = "106/" + playerInfo[1] + "/" + playerInfo[2] + "/" + playerInfo[3] + "/"
								+ playerInfo[4];

						setBaseData(playerInfo);

						instToEveryClient3001(s);

						playerChoosing = false;
					}

				}

			}

			catch (IOException e) {

			}
		}

	}

	private class SocketListener3002 implements Runnable {
		BufferedReader read;
		Socket socket;

		public SocketListener3002(Socket clientSocket) {
			try {
				socket = clientSocket;
				InputStreamReader isReader = new InputStreamReader(socket.getInputStream());
				read = new BufferedReader(isReader);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}

		public void run() {

			try {
				String k = "";

				while ((k = read.readLine()) != null) {
					String[] playerInfo = k.split("/");

					if (playerInfo[0].equals("504")) {
						String s = "111/" + playerInfo[1];
						instToEveryClient3002(s);

					}

				}

			}

			catch (IOException e) {

			}
		}

	}

	private class SocketListener3003 implements Runnable {
		BufferedReader read;
		Socket socket;

		public SocketListener3003(Socket clientSocket) {
			try {
				socket = clientSocket;
				InputStreamReader isReader = new InputStreamReader(socket.getInputStream());
				read = new BufferedReader(isReader);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}

		public void run() {

			try {
				String k = "";

				while ((k = read.readLine()) != null) {
					String[] playerInfo = k.split("/");

					if (playerInfo[0].equals("501")) {
						addPlayer(playerInfo, socket.getInetAddress().toString());

						try {
							sendPlayerBase();
						} catch (InterruptedException e) {

						}

					}

				}

			}

			catch (IOException e) {

			}
		}

	}

	private class Socket3000 implements Runnable {

		public void run() {
			try {
				ServerSocket serverSock = new ServerSocket(3000);

				while (true) {
					Thread.sleep(1000);

					Socket client = serverSock.accept();
					PrintWriter writer = new PrintWriter(client.getOutputStream(), true);

					cards.setHand();
					playerHand = cards.getHand();

					PlayerServer ps = new PlayerServer(playerHand, setSeat(), client.getInetAddress().toString(),
							playerNumber);

					if (tour > -1) {
						ps.setMove(false);
					}

					ps.setSource1(writer);
					playerBase.add(ps);

					sendData(playerBase.size() - 1);

					Thread t = new Thread(new SocketListener3000(client));
					t.start();

				}
			}

			catch (Exception ex) {
			}

		}

	}

	private class Socket3001 implements Runnable {

		public void run() {
			try {
				ServerSocket serverSock = new ServerSocket(3001);

				while (true) {

					Socket client = serverSock.accept();
					PrintWriter writer = new PrintWriter(client.getOutputStream(), true);

					playerBase.get(playerBase.size() - 1).setSource2(writer);

					Thread t = new Thread(new SocketListener3001(client));
					t.start();

				}
			}

			catch (Exception ex) {
			}

		}

	}

	private class Socket3002 implements Runnable {

		public void run() {
			try {
				ServerSocket serverSock = new ServerSocket(3002);

				while (true) {

					Socket client = serverSock.accept();
					PrintWriter writer = new PrintWriter(client.getOutputStream(), true);

					playerBase.get(playerBase.size() - 1).setSource3(writer);

					Thread t = new Thread(new SocketListener3002(client));
					t.start();

				}
			}

			catch (Exception ex) {
			}

		}

	}

	private class Socket3003 implements Runnable {

		public void run() {
			try {
				ServerSocket serverSock = new ServerSocket(3003);

				while (true) {

					Socket client = serverSock.accept();
					PrintWriter writer = new PrintWriter(client.getOutputStream(), true);

					playerBase.get(playerBase.size() - 1).setSource4(writer);

					Thread t = new Thread(new SocketListener3003(client));
					t.start();

				}
			}

			catch (Exception ex) {
			}

		}

	}

	// Checks if player disconnects
	private class CheckIfPlayerDisconnected implements Runnable {

		public void run() {

			while (true) {

				for (int i = 0; i < playerBase.size(); i++) {

					if (playerBase.get(i).getUnique() == uniquePlayerNumber) {
						seats.add(playerBase.get(i).getSeat());

						pool -= playerBase.get(i).getChipsInvested();

						playerBase.get(i).setConnected(false);

						playerBase.get(i).setMove(false);

						if (moving == playerBase.get(i).getSeat()) {
							playerChoosing = false;
						}
						if (playerBase.size() == 1) {
							playerBase.remove(i);
						} else {
							instToEveryClient3002("109/" + playerBase.get(i).getSeat());
						}
						uniquePlayerNumber = -1;
					}

				}
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
				}

			}

		}

	}

	private class StartingWindowServerSocket implements Runnable {
		public void run() {
			try {
				ServerSocket ramkaSock = new ServerSocket(3004);

				String s = "";
				while (true) {
					Socket ramkaS = ramkaSock.accept();

					PrintWriter pw = new PrintWriter(ramkaS.getOutputStream(), true);

					startingWindowNameCheck.add(pw);

					Thread thr = new Thread(new StartingWindowListener(ramkaS));
					thr.start();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	private class StartingWindowListener implements Runnable {
		BufferedReader czytaj;
		Socket gniazdo;

		public StartingWindowListener(Socket clientSocket) {
			try {
				gniazdo = clientSocket;
				InputStreamReader isReader = new InputStreamReader(gniazdo.getInputStream());
				czytaj = new BufferedReader(isReader);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}

		public void run() {

			try {

				while ((czytaj.readLine()) != null) {
					String s = playerBase.size() + "/";

					for (PlayerServer y : playerBase) {
						s += y.getName() + "/";
					}

					for (PrintWriter x : startingWindowNameCheck) {
						x.println(s);
						x.flush();
					}

				}
			} catch (IOException e) {
			}
		}

	}

	// Ins 103 sending order with player to be removed
	// port 3003
	// Ins 116 sending new cards for another round
	// port 3003
	private class TheGame implements Runnable {

		public void run() {
			while (true) {
				try {
					Thread.sleep(500);
					while (howManyHaveChips() > 1) {

						Thread.sleep(2000);

						if (tour == -1) {
							preFlop();
						}

						if (howManyPlaying() > 1) {
							for (int i = 1; i < 4; i++) {
								Thread.sleep(1500);

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

								if (tour == 1 && howManyPlaying() > 1) {
									ArrayList<String> s = new ArrayList<String>();
									s.addAll(cards.getFlop());
									s.addAll(cards.getTurn());
									sendTableCards(s);
									s.clear();
								}

								else if (tour == 2 && howManyPlaying() > 1) {
									ArrayList<String> s = new ArrayList<String>();
									s.addAll(cards.getFlop());
									s.addAll(cards.getTurn());
									s.addAll(cards.getRiver());
									sendTableCards(s);
									s.clear();
								}

							}
						}

						if (howManyPlaying() > 1) {
							showDownResult();
						}

						tour = -1;
						pool = 0;

						refreshMove();

						for (int k = 0; k < playerBase.size(); k++) {

							if (playerBase.get(k).getConnected() == false || playerBase.get(k).getChips() <= 0) {
								String s = "";

								s = "103/" + playerBase.get(k).getName() + "/" + playerBase.get(k).getSeat();

								instToEveryClient3003(s);

								playerBase.remove(k);

							}

							else if (playerBase.get(k).getConnected() == true && playerBase.get(k).getChips() > 0) {
								playerBase.get(k).setHand();

								String s = "116/" + playerBase.get(k).getSeat() + "/"
										+ playerBase.get(k).getHand().get(0) + "/" + playerBase.get(k).getHand().get(1);

								sendNewCards(s, k);

								playerBase.get(k).setChipsInvested(-playerBase.get(k).getChipsInvested());
							}

						}

						sendPoolHide();

					}

				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public void startServerSockets() {

		Thread start = new Thread(new Socket3000());
		start.start();

		Thread start1 = new Thread(new Socket3001());
		start1.start();

		Thread start2 = new Thread(new Socket3002());
		start2.start();

		Thread start3 = new Thread(new Socket3003());
		start3.start();

	}

	// Ins 101 setting local player hand, seat, timer, big blind amount,small
	// blind amount
	// port 3000
	public void sendData(int x) {
		PrintWriter pisarz = playerBase.get(x).getSource1();
		String s = "";
		s = "101/" + playerBase.get(x).getChips() + "/" + playerBase.get(x).getHand().get(0) + "/"
				+ playerBase.get(x).getHand().get(1) + "/" + playerBase.get(x).getSeat() + "/"
				+ playerBase.get(x).getInNet() + "/" + timer + "/" + bigBlind + "/" + smallBlind;
		pisarz.println(s);
		pisarz.flush();
	}

	// Setting seats for players
	public int setSeat() {
		int k = (int) (Math.random() * seats.size() - 1);

		int o = seats.get(k);

		seats.remove(k);

		return o;
	}

	//
	public void setBaseData(String[] x) {
		// nr / action / seat / chips / bid

		for (PlayerServer y : playerBase) {
			if (y.getSeat() == Integer.parseInt(x[2]) && x[1].equalsIgnoreCase("Fold")) {
				y.setMove(false);
			}

			else if (y.getSeat() == Integer.parseInt(x[2])) {
				y.setBid(Integer.parseInt(x[4]) + y.getBid());
				y.setChips(Integer.parseInt(x[4]));
			}

			if (y.getBid() > highestBid && y.getBid() != 0) {
				highestBid = y.getBid();
			}

		}

	}

	// Ins 102 sending player base
	// port 3003
	public void sendPlayerBase() throws InterruptedException {

		for (int i = playerBase.size() - 1; i >= 0; i--) {
			PrintWriter pisarz = playerBase.get(i).getSource4();

			for (int j = 0; j < playerBase.size(); j++) {

				if (i != j) {
					String s = "";
					s = "102/" + playerBase.get(j).getName() + "/" + playerBase.get(j).getChips() + "/"
							+ playerBase.get(j).getSeat() + "/" + playerBase.get(j).getInNet();
					pisarz.println(s);
					pisarz.flush();
				}
				Thread.sleep(200);
			}
			Thread.sleep(200);
		}

	}

	public void addPlayer(String[] x, String y) {
		for (int i = 0; i < playerBase.size(); i++) {
			if (playerBase.get(i).getName() == null && playerBase.get(i).getInNet().equals(y)) {
				playerBase.get(i).setName(x[1]);
				if (tour > -1) {
					playerBase.get(i).setMove(false);
				}
			}

		}
	}

	public void instToEveryClient3000(String x) {
		try {

			for (int i = 0; i < playerBase.size(); i++) {

				PrintWriter pw = playerBase.get(i).getSource1();

				pw.println(x);
				pw.flush();
				Thread.sleep(10);
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public void instToEveryClient3001(String x) {
		try {

			for (int i = 0; i < playerBase.size(); i++) {

				PrintWriter pw = playerBase.get(i).getSource2();

				pw.println(x);
				pw.flush();
				Thread.sleep(10);
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public void instToEveryClient3002(String x) {
		try {

			for (int i = 0; i < playerBase.size(); i++) {

				PrintWriter pw = playerBase.get(i).getSource3();

				pw.println(x);
				pw.flush();
				Thread.sleep(10);
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public void instToEveryClient3003(String x) {
		try {

			for (int i = 0; i < playerBase.size(); i++) {

				PrintWriter pw = playerBase.get(i).getSource4();

				pw.println(x);
				pw.flush();
				Thread.sleep(10);
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	// Ins 104 sending sb seat bb seat and dealer seat
	// port 3000
	private void setBlinds() {
		boolean sbX = false;
		boolean bbX = false;
		boolean de = false;

		sb++;

		if (sb > 6) {
			sb = 1;
		}

		if (sb < 7) {
			for (int i = sb; i < 7; i++) {
				for (int j = 0; j < playerBase.size(); j++) {
					if (playerBase.get(j).getSeat() == i && playerBase.get(j).getMove() == true) {
						sbSeat = i;
						sb = i;
						sbX = true;
						break;
					}
				}
				if (sbX == true) {
					break;
				}
			}
		}
		if (sbX == false) {
			for (int i = 1; i < sb; i++) {
				for (int j = 0; j < playerBase.size(); j++) {
					if (playerBase.get(j).getSeat() == i && playerBase.get(j).getMove() == true) {
						sbSeat = i;
						sb = i;
						sbX = true;
						break;
					}
				}
				if (sbX == true) {
					break;
				}
			}

		}
		for (int i = sbSeat + 1; i < 7; i++) {
			for (int j = 0; j < playerBase.size(); j++) {
				if (playerBase.get(j).getSeat() == i && playerBase.get(j).getMove() == true) {
					bbSeat = i;
					bbX = true;
					break;
				}
			}
			if (bbX == true) {
				break;
			}
		}

		if (bbX == false) {
			for (int i = 1; i < sbSeat; i++) {
				for (int j = 0; j < playerBase.size(); j++) {
					if (playerBase.get(j).getSeat() == i && playerBase.get(j).getMove() == true) {
						bbSeat = i;
						bbX = true;
						break;
					}
				}
				if (bbX == true) {
					break;
				}
			}

		}
		if (howManyPlaying() == 2) {
			dealerSeat = bbSeat;
			de = true;
		}

		if (de == false) {
			for (int i = sbSeat; i > 0; i--) {
				for (int j = 0; j < playerBase.size(); j++) {
					if (playerBase.get(j).getMove() == true && i != sbSeat && i != bbSeat
							&& playerBase.get(j).getSeat() == i) {
						dealerSeat = i;
						de = true;
						break;
					}
				}

			}

			if (de == false) {
				for (int y = 6; y > 0; y--) {
					for (int p = 0; p < playerBase.size(); p++) {
						if (playerBase.get(p).getMove() == true && playerBase.get(p).getSeat() == y && y != sbSeat
								&& y != bbSeat) {
							dealerSeat = y;
							de = true;
							break;
						}
					}

					if (de == true) {
						break;
					}
				}

			}

		}

		int y = smallBlind;
		int z = bigBlind;

		for (int i = 0; i < playerBase.size(); i++) {

			if (playerBase.get(i).getSeat() == sbSeat) {
				if (playerBase.get(i).getChips() >= smallBlind) {
					playerBase.get(i).setBid(smallBlind);
					playerBase.get(i).setChips(smallBlind);

				} else if (playerBase.get(i).getChips() < smallBlind) {
					playerBase.get(i).setBid(playerBase.get(i).getChips());
					playerBase.get(i).setChips(playerBase.get(i).getChips());
					y = playerBase.get(i).getBid();
				}

			}

			else if (playerBase.get(i).getSeat() == bbSeat) {
				if (playerBase.get(i).getChips() >= bigBlind) {
					playerBase.get(i).setBid(bigBlind);
					playerBase.get(i).setChips(bigBlind);
				} else if (playerBase.get(i).getChips() < bigBlind) {
					playerBase.get(i).setBid(playerBase.get(i).getChips());
					playerBase.get(i).setChips(playerBase.get(i).getChips());
					z = playerBase.get(i).getBid();
				}

			}

		}

		if (y >= z) {
			highestBid = y;
		} else {
			highestBid = z;
		}

		String s = "104/" + sbSeat + "/" + bbSeat + "/" + dealerSeat;

		instToEveryClient3000(s);

	}

	// Ins 105 sending player move
	// port 3000
	private void sendMove(int x) {
		String s = "105/" + x;
		instToEveryClient3000(s);
	}

	// Ins 107 sending pool value
	// port 3000
	private void sendPool() {
		String s = "107/" + pool;
		instToEveryClient3000(s);
	}

	// Ins 108 sending reset order
	// port 3000
	private void sendReset() {
		String s = "108/";
		instToEveryClient3000(s);
	}

	// Ins 115 sending pool hide order
	// port 3000
	private void sendPoolHide() {
		String s = "115/";
		instToEveryClient3000(s);

	}

	// Ins 114 sending show all cards order
	// port 3003
	private void sendAllCards() throws InterruptedException {
		{

			for (int i = 0; i < playerBase.size(); i++) {
				PrintWriter pisarz = playerBase.get(i).getSource4();

				for (int j = 0; j < playerBase.size(); j++) {

					if (i != j) {
						String s = "114/" + playerBase.get(j).getSeat() + "/" + playerBase.get(j).getHand().get(0) + "/"
								+ playerBase.get(j).getHand().get(1);

						pisarz.println(s);
						pisarz.flush();
					}
					Thread.sleep(300);
				}

			}

		}

	}

	private int howManyPlaying() {
		int k = 0;

		for (int i = 0; i < playerBase.size(); i++) {
			if (playerBase.get(i).getMove() == true) {
				k++;
			}
		}

		return k;
	}

	// ins 110 sending pool split information
	// port 3001
	public void poolSpliter() {
		int[] pools = new int[howManyPlaying() - 1];

		int[] chipsReturned = { 0, 0, 0, 0, 0, 0 };

		ArrayList<Integer> invested = new ArrayList<Integer>();

		int[] chipsEdge = new int[howManyPlaying() - 1];

		for (int y = 0; y < playerBase.size(); y++) {
			if (playerBase.get(y).getMove() == true && playerBase.get(y).getConnected() == true) {
				invested.add(playerBase.get(y).getChipsInvested());
			}
		}

		Collections.sort(invested);

		for (int i = 0; i < howManyPlaying() - 1; i++) {
			if (i == 0) {
				pools[0] = invested.get(0) * howManyPlaying();
				pool -= pools[0];

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
				pools[i] = (invested.get(i) - invested.get(i - 1)) * (howManyPlaying() - i);
				chipsEdge[i] = invested.get(i);
				pool -= pools[i];

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
				for (int o = 0; o < winners.length; o++) {
					chipsReturned[winners[o] - 1] += pools[0] / winners.length;
				}
				pools[0] = 0;

			}

			else {
				for (int o = 0; o < winners.length; o++) {
					for (int j = 0; j < playerBase.size(); j++) {
						if (chipsEdge[i] <= playerBase.get(j).getChipsInvested()
								&& winners[o] == playerBase.get(j).getSeat()) {
							int c = 0;

							l = true;

							for (int y = 0; y < winners.length; y++) {
								for (int h = 0; h < playerBase.size(); h++) {
									if (chipsEdge[i] <= playerBase.get(h).getChipsInvested()
											&& playerBase.get(h).getSeat() == winners[y]) {
										c++;
									}
								}
							}

							chipsReturned[winners[o] - 1] += pools[i] / c;

						}
					}
				}
			}

			if (l == true) {
				pools[i] = 0;
			}

		}

		for (int i = 0; i < pools.length; i++) {

			boolean l = false;

			if (pools[i] > 0) {
				for (int z = 0; z < playerBase.size(); z++) {
					for (int o = 0; o < winners.length; o++) {
						if (chipsEdge[i] <= playerBase.get(z).getChipsInvested()
								&& playerBase.get(z).getSeat() != winners[o] && playerBase.get(z).getMove() == true) {
							int c = 0;

							l = true;

							for (int y = 0; y < winners.length; y++) {
								for (int h = 0; h < playerBase.size(); h++) {
									if (chipsEdge[i] <= playerBase.get(h).getChipsInvested()
											&& playerBase.get(h).getSeat() == winners[y]) {
										c++;
									}
								}
							}

							chipsReturned[playerBase.get(z).getSeat() - 1] += pools[i] / c;

						}
					}
				}
			}

			if (l == true) {
				pools[i] = 0;
			}
		}

		if (pool > 0) {
			for (PlayerServer x : playerBase) {

				if (invested.get(invested.size() - 1) == x.getChipsInvested()) {
					chipsReturned[x.getSeat() - 1] += pool;

				}

			}
		}

		String s = "110/";

		for (int i = 0; i < chipsReturned.length; i++) {
			if (chipsReturned[i] != 0) {
				for (int j = 0; j < playerBase.size(); j++) {
					if (playerBase.get(j).getSeat() == (i + 1)) {
						playerBase.get(j).setChips(-chipsReturned[i]);
					}

				}

			}
			s += chipsReturned[i] + "/";
		}

		instToEveryClient3001(s);
	}

	// ins 112 sending table cards
	// port 3000
	public void sendTableCards(ArrayList<String> x) {
		String s = "112/";

		for (int i = 0; i < x.size(); i++) {
			s += x.get(i) + "/";
		}

		instToEveryClient3000(s);

	}

	public void preFlop() throws InterruptedException {
		tour++;

		Thread.sleep(2000);

		setBlinds();

		cards.setTableCards();

		do {
			if (playerBase.size() > 1) {
				if (bbSeat + 1 < 7) {
					for (int i = bbSeat + 1; i < 7; i++) {
						for (int j = 0; j < playerBase.size(); j++) {
							if (playerBase.get(j).getBid() != highestBid && playerBase.get(j).getMove() == true
									&& playerBase.get(j).getChips() > 0 && playerBase.get(j).getSeat() == i
									&& howManyPlaying() > 1) {
								sendMove(i);
								moving = i;
								playerChoosing = true;

								while (playerChoosing == true) {
									Thread.sleep(500);
								}
								moving = 0;
								Thread.sleep(500);

							}
						}

					}
				}

				for (int i = 1; i <= bbSeat; i++) {
					for (int j = 0; j < playerBase.size(); j++) {
						if (playerBase.get(j).getBid() != highestBid && playerBase.get(j).getMove() == true
								&& playerBase.get(j).getChips() > 0 && playerBase.get(j).getSeat() == i
								&& howManyPlaying() > 1) {
							sendMove(i);
							moving = i;
							playerChoosing = true;

							while (playerChoosing == true) {
								Thread.sleep(500);
							}
							moving = 0;
							Thread.sleep(500);

						}
						if (playerBase.get(j).getBid() == bigBlind && playerBase.get(j).getMove() == true
								&& playerBase.get(j).getChips() > 0 && playerBase.get(j).getSeat() == bbSeat
								&& howManyPlaying() > 1 && i == bbSeat) {

							sendMove(bbSeat);
							moving = bbSeat;
							playerChoosing = true;

							while (playerChoosing == true) {
								Thread.sleep(500);
							}
							moving = 0;
							Thread.sleep(500);

						}
					}
				}

			}

		} while (isTourEnd() == true);

		if (howManyPlaying() >= 2) {
			sendTableCards(cards.getFlop());
		}

		reseting();

		sendReset();

		sendPool();

		gotAWinner();

	}

	private void flopTurnRiver() throws InterruptedException {

		do {
			if (howManyPlaying() > 1) {
				for (int i = sb; i <= 6; i++) {

					for (int j = 0; j < playerBase.size(); j++) {
						if (playerBase.get(j).getBid() != highestBid && playerBase.get(j).getMove() == true
								&& playerBase.get(j).getChips() > 0 && playerBase.get(j).getSeat() == i
								&& howManyPlaying() > 1) {
							sendMove(i);
							moving = i;
							playerChoosing = true;

							while (playerChoosing == true) {
								Thread.sleep(500);
							}
							moving = 0;
							Thread.sleep(500);

						}

					}

				}

				if (sb > 1) {

					for (int i = 1; i < sb; i++) {

						for (int j = 0; j < playerBase.size(); j++) {
							if (playerBase.get(j).getBid() != highestBid && playerBase.get(j).getMove() == true
									&& playerBase.get(j).getChips() > 0 && playerBase.get(j).getSeat() == i
									&& howManyPlaying() > 1) {
								sendMove(i);
								moving = i;
								playerChoosing = true;

								while (playerChoosing == true) {
									Thread.sleep(500);
								}
								moving = 0;
								Thread.sleep(500);

							}

						}
					}
				}

			}

		} while (isTourEnd() == true);

		reseting();

		sendReset();

		sendPool();

		gotAWinner();

	}

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

	// ins 113 sending table cards
	// port 3001
	private void gotAWinner() throws InterruptedException {
		if (howManyPlaying() < 2) {
			for (int i = 0; i < playerBase.size(); i++) {
				if (playerBase.get(i).getMove() == true) {
					playerBase.get(i).setChips((-1) * pool);

					String s = "113/" + playerBase.get(i).getSeat() + "/" + pool;

					instToEveryClient3001(s);

					Thread.sleep(500);

					sendPoolHide();

				}
			}

			pool = 0;

		}

	}

	private void reseting() {
		highestBid = -1;

		for (int i = 0; i < playerBase.size(); i++) {
			pool = pool + playerBase.get(i).getBid();

			playerBase.get(i).setChipsInvested(playerBase.get(i).getBid());

			playerBase.get(i).setBid(0);

		}

	}

	private int howManyHaveChips() {
		int k = 0;

		for (int i = 0; i < playerBase.size(); i++) {
			if (playerBase.get(i).getChips() > 0) {
				k++;
			}
		}

		return k;
	}

	private void refreshMove() {
		for (PlayerServer x : playerBase) {
			if (x.getChips() > 0) {
				x.setMove(true);
			} else {
				x.setMove(false);
			}

		}

	}

	// ins 111 sending winner cards combination
	// port 3002
	private void showDownResult() throws InterruptedException {

		sHDW = new ShowDownWinner();

		String players = "";

		for (int i = 0; i < playerBase.size(); i++) {
			if (playerBase.get(i).getMove() == true && playerBase.get(i).getConnected() == true) {
				String hand = "";
				hand = playerBase.get(i).getHand().get(0) + playerBase.get(i).getHand().get(1) + cards.getFlop().get(0)
						+ cards.getFlop().get(1) + cards.getFlop().get(2) + cards.getTurn().get(0)
						+ cards.getRiver().get(0) + playerBase.get(i).getSeat();
				sHDW.addWinners(hand);
			}
		}

		sHDW.start();

		winners = new int[sHDW.getWin().length];

		for (int v = 0; v < winners.length; v++) {
			winners[v] = sHDW.getWin()[v];
		}

		for (int i = 0; i < winners.length; i++) {
			for (int j = 0; j < playerBase.size(); j++) {
				if (playerBase.get(j).getSeat() == winners[i]) {
					players += playerBase.get(j).getName();
				}
			}

		}

		sendAllCards();

		Thread.sleep(3000);

		poolSpliter();

		Thread.sleep(1000);

		sendPoolHide();

		String[] results = { "HighestCard", "Pair", "Double Pair", "Trips", "Straight", "Flush", "Full House", "Quad",
				"Royal Poker", "Poker" };

		String s = "111/" + players + " scored:" + results[Integer.parseInt(sHDW.getWinners().get(0).substring(0, 1))]
				+ "\n";

		instToEveryClient3002(s);

		Thread.sleep(7000);

		instToEveryClient3003("115/");

	}

	private void sendNewCards(String x, int y) {
		try {

			PrintWriter pw = playerBase.get(y).getSource4();

			pw.println(x);
			pw.flush();
			Thread.sleep(10);

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}
