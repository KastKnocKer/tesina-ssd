package layout;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {

	  BufferedImage  image;
	  
	  public ImagePanel(String imageName) {
		  
		  try {
			  System.out.println("Entrato blabla");
			  /*
			  System.out.println("Enter image name\n");
			  BufferedReader bf=new BufferedReader(new 
					  InputStreamReader(System.in));
			  String imageName=bf.readLine(); */
			  
			  File input = new File(imageName);
			  image = ImageIO.read(input);
		  } catch (IOException ie) {
			  System.out.println("Error:"+ie.getMessage());
		  }
		  setBorder(BorderFactory.createLineBorder(Color.black));
		  
		  // this.setSize(100, 100); 
	  }

	  public void paint(Graphics g) {
		  g.drawImage( image, 0, 0, null);
	  }

	  /*
	  static public void main(String args[]) throws Exception {
		  JFrame frame = new JFrame("Display image");
		  ImagePanel panel = new ImagePanel();
		  frame.getContentPane().add(panel);
		  frame.setSize(150, 150);
		  frame.setVisible(true);
	  }
	  */
	}