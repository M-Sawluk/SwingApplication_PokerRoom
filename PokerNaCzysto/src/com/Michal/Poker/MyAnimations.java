package com.Michal.Poker;

import java.awt.*;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
public  class MyAnimations extends JPanel
{

		private static final long serialVersionUID = -8917003682155939057L;
	
		private boolean moveImg0Right=false;
		private boolean moveImg0Left=false;
		private boolean startImage=false;
		Image im,imb;
		int pixels;
		int heigth;
		int size;
		int width,bwidth;

		public void startingImg(ImageIcon img)
		{
			im=img.getImage();
			startImage=true;
			repaint();
			try
			{
				Thread.sleep(1500);
			}
			catch(Exception e){};
			startImage=false;
			
		}
		
		public void movingImg0Right(ImageIcon imgOut,ImageIcon imgIn,int awidth,int asleep,int apixels)
		{
			
			moveImg0Right=true;
			
			im=imgOut.getImage();
			pixels=apixels;
			imb=imgIn.getImage();
			width=0;
			bwidth=-awidth;
			repaint();
			for(int o=0;o<=(awidth/apixels);o++)
			{
				width+=pixels;
				bwidth+=pixels;
			try{Thread.sleep(asleep);}
			catch(Exception e){e.printStackTrace();}
			repaint();
			}
			moveImg0Right=false;
	
		}
		
		public void movingImg0Left(ImageIcon imgOut, ImageIcon imgIn,int awidth,int asleep,int apixels)
		{
			
			moveImg0Left=true;
			
			im=imgOut.getImage();
			imb=imgIn.getImage();
			pixels=apixels;
			width=0;
			bwidth=awidth;
			repaint();
			for(int o=0;o<=(awidth/apixels);o++)
			{
				width-=pixels;
				bwidth-=pixels;
			try{Thread.sleep(asleep);}
			catch(Exception e){e.printStackTrace();}
			repaint();
			}
			moveImg0Left=false;
	
		}
			
		public void paintComponent(Graphics g)
		{
			
			if(startImage==true)
				{
					g.drawImage(im, 0, 0, this);
					
				}
			
			if(moveImg0Right==true)
				{

					g.drawImage(im, width, 0, this);
					g.drawImage(imb, bwidth, 0,this);
				}
			if(moveImg0Left==true)
				{
					g.drawImage(im, width, 0, this);
					g.drawImage(imb, bwidth, 0, this);
				}
			
			
		}

}
