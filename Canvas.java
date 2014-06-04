package CompsciFinalProject;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class Canvas extends JPanel implements MouseListener{

	private static boolean[] mouseState = new boolean[3];

	public Canvas()
	{
		this.setDoubleBuffered(true);
		this.setFocusable(true);
		this.setBackground(Color.black);

		this.addMouseListener(this);
	}

	public abstract void Draw(Graphics2D g2d);

	public void paintComponent(Graphics g)
	{
		Graphics2D g2d = (Graphics2D)g;        
		super.paintComponent(g2d);        
		Draw(g2d);
	}

	/**
     * Is mouse button "button" down?
     * Parameter "button" can be "MouseEvent.BUTTON1" - Indicates mouse button #1
     * or "MouseEvent.BUTTON2" - Indicates mouse button #2 ...
     * 
     * @param button Number of mouse button for which you want to check the state.
     * @return true if the button is down, false if the button is not down.
     */
	public static boolean mouseButtonState(int button)
	{
		return mouseState[button - 1];
	}

	private void mouseKeyStatus(MouseEvent e, boolean status)
	{
		if(e.getButton() == MouseEvent.BUTTON1)
			mouseState[0] = status;
		else if(e.getButton() == MouseEvent.BUTTON2)
			mouseState[1] = status;
		else if(e.getButton() == MouseEvent.BUTTON3)
			mouseState[2] = status;
	}

	public void mousePressed(MouseEvent e)
	{
		mouseKeyStatus(e, true);
	}

	public void mouseReleased(MouseEvent e)
	{
		mouseKeyStatus(e, false);
	}

	public void mouseClicked(MouseEvent e) { }

	public void mouseEntered(MouseEvent e) { }

	public void mouseExited(MouseEvent e) { }
}