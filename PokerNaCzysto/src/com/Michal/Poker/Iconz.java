package com.Michal.Poker;


import java.util.ArrayList;

//Hard coded icons
public class Iconz 
{
	
	ArrayList<String> paths;
	ArrayList<String> numbers;
	Iconz()

	{
		paths = new ArrayList<String>();
		
		paths.add("pics/2C.png");
		paths.add("pics/2D.png");
		paths.add("pics/2S.png");
		paths.add("pics/2H.png");
		
		paths.add("pics/3C.png");
		paths.add("pics/3D.png");
		paths.add("pics/3S.png");
		paths.add("pics/3H.png");
		
		paths.add("pics/4C.png");
		paths.add("pics/4D.png");
		paths.add("pics/4S.png");
		paths.add("pics/4H.png");
		
		paths.add("pics/5C.png");
		paths.add("pics/5D.png");
		paths.add("pics/5S.png");
		paths.add("pics/5H.png");
		
		paths.add("pics/6C.png");
		paths.add("pics/6D.png");
		paths.add("pics/6S.png");
		paths.add("pics/6H.png");
		
		paths.add("pics/7C.png");
		paths.add("pics/7D.png");
		paths.add("pics/7S.png");
		paths.add("pics/7H.png");
		
		paths.add("pics/8C.png");
		paths.add("pics/8D.png");
		paths.add("pics/8S.png");
		paths.add("pics/8H.png");
		
		paths.add("pics/9C.png");
		paths.add("pics/9D.png");
		paths.add("pics/9S.png");
		paths.add("pics/9H.png");
		
		paths.add("pics/TC.png");
		paths.add("pics/TD.png");
		paths.add("pics/TS.png");
		paths.add("pics/TH.png");
		
		paths.add("pics/JC.png");
		paths.add("pics/JD.png");
		paths.add("pics/JS.png");
		paths.add("pics/JH.png");
		
		paths.add("pics/QC.png");
		paths.add("pics/QD.png");
		paths.add("pics/QS.png");
		paths.add("pics/QH.png");
		
		paths.add("pics/KC.png");
		paths.add("pics/KD.png");
		paths.add("pics/KS.png");
		paths.add("pics/KH.png");
		
		paths.add("pics/AC.png");
		paths.add("pics/AD.png");
		paths.add("pics/AS.png");
		paths.add("pics/AH.png");
		
		numbers = new ArrayList<String>();
		
		numbers.add("2C");
		numbers.add("2D");
		numbers.add("2S");
		numbers.add("2H");
		numbers.add("3C");
		numbers.add("3D");
		numbers.add("3S");
		numbers.add("3H");
		numbers.add("4C");
		numbers.add("4D");
		numbers.add("4S");
		numbers.add("4H");
		numbers.add("5C");
		numbers.add("5D");
		numbers.add("5S");
		numbers.add("5H");
		numbers.add("6C");
		numbers.add("6D");
		numbers.add("6S");
		numbers.add("6H");
		numbers.add("7C");
		numbers.add("7D");
		numbers.add("7S");
		numbers.add("7H");
		numbers.add("8C");
		numbers.add("8D");
		numbers.add("8S");
		numbers.add("8H");
		numbers.add("9C");
		numbers.add("9D");
		numbers.add("9S");
		numbers.add("9H");
		numbers.add("TC");
		numbers.add("TD");
		numbers.add("TS");
		numbers.add("TH");
		numbers.add("JC");
		numbers.add("JD");
		numbers.add("JS");
		numbers.add("JH");
		numbers.add("QC");
		numbers.add("QD");
		numbers.add("QS");
		numbers.add("QH");
		numbers.add("KC");
		numbers.add("KD");
		numbers.add("KS");
		numbers.add("KH");
		numbers.add("AC");
		numbers.add("AD");
		numbers.add("AS");
		numbers.add("AH");
}	
	
	public String giveIcon(String x)
	{
		String k;
		int y;
		
		y=numbers.indexOf(x);
		
		k=paths.get(y);
		

		return k;
	}

}
