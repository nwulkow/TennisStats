package graphics;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class MatrixImage extends BufferedImage{
	
	double[][] M, scaledMatrix;
	JFrame frame;
	Container container;
	double currentMouseListenerOutputvalue = 0;
	
	public MatrixImage(double[][] M, int width, int height, String filepath, boolean fileOutput){
		super(width, height, BufferedImage.TYPE_INT_RGB);
		
		this.M = M;
		scaledMatrix = new double[width][height];
		
	    double widthRatio = height / (double)M.length;
	    double heightRatio = width / (double)M[0].length;
		try {
		    for(int i=0; i<M.length; i++) {
		        for(int j=0; j < M[0].length; j++) {
		            int a = (int) (M[i][j]*255);
		            Color newColor = new Color(a,a,a);
		            for(int k = (int) (i*widthRatio); k < (i+1)*widthRatio; k++){
		            	for(int l = (int) (j*heightRatio); l < (j+1)*heightRatio; l++){
		            	this.setRGB(k,l,newColor.getRGB());
		            	scaledMatrix[k][l] = M[i][j];
		            	}
		            }
		        }
		    }
		    if(fileOutput){
		    	File output = new File("C:/Users/Niklas/TennisStatsData/" + filepath + ".jpg");
		    	ImageIO.write(this, "jpg", output);
		    }
		    //PlotTools.showImage(this);
		}
		catch(Exception e) {
		}
		
	}
	
	
	public void addAMouseListener(Container container){
		container.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				//System.out.println("JETZT");
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				int X = e.getX();
				int Y = e.getY();
				double value = scaledMatrix[X][Y];
				currentMouseListenerOutputvalue = value;
				System.out.println(value);
				
			}
		});
	}
	
	
	

	public double[][] getM() {
		return M;
	}

	public void setM(double[][] m) {
		M = m;
	}

	public double[][] getScaledMatrix() {
		return scaledMatrix;
	}

	public void setScaledMatrix(double[][] scaledMatrix) {
		this.scaledMatrix = scaledMatrix;
	}

	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}


	public double getCurrentMouseListenerOutputvalue() {
		return currentMouseListenerOutputvalue;
	}


	public void setCurrentMouseListenerOutputvalue(double currentMouseListenerOutputvalue) {
		this.currentMouseListenerOutputvalue = currentMouseListenerOutputvalue;
	}


	public Container getContainer() {
		return container;
	}


	public void setContainer(Container container) {
		this.container = container;
	}
}
