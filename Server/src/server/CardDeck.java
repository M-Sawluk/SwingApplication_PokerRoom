package server;
import java.util.ArrayList;

public class CardDeck 
{
	private static ArrayList<String> Card_Deck = new ArrayList<String>();
	private int x=0;
	String[] cardDeck={"2H","2D","2C","2S","3H","3D","3C","3S","4H","4D","4C","4S","5H","5D","5C","5S","6H","6D","6C","6S","7H","7D","7C","7S","8H","8D","8C","8S","9H","9D","9C","9S","TH","TD","TC","TS",
			"JH","JD","JC","JS","QH","QD","QC","QS","KH","KD","KC","KS","AH","AD","AC","AS"};
	
	public CardDeck()
	{
		for(int i=0;i<cardDeck.length;i++)
		{
		Card_Deck.add(cardDeck[x]);
		x++;
		} 
	
	}
	public void renewCardDeck()
	{
		Card_Deck.clear();
		
		for(int i=0;i<cardDeck.length;i++)
		{
		Card_Deck.add(cardDeck[i]);
		} 
		
		
		
		
		
	}
	public ArrayList<String> getDeck()
	{
		return Card_Deck;
	}
}
