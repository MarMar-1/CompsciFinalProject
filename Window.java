package CompsciFinalProject;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Window extends JFrame{
	private Window(){
		this.setTitle("Circle the Boofpac");
		this.setUndecorated(true);
		this.setExtendedState(this.MAXIMIZED_BOTH);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setContentPane(new Framework());
		this.setVisible(true);
	}

	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Window();
			}
		});
	}
}