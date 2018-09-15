package trgovackiputnik;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Prozor extends JFrame {
	
	private static final long serialVersionUID = 1L;
	//private static final int BROJ_GRADOVA = 50;
	public static final int MAX_GENERACIJA = 100000;
	private static final int MAX_WIDTH = 1600;
	private static final int MAX_HEIGHT = 900;
	
	private JTextArea konzola;
	private JButton pokreni;
	private JButton pauziraj;
	private JButton zavrsi;
	private JTextField unosgradova;
	private JDrawPanel panel;
	private JPanel at;
	
	private static int BrojGradova;
	private boolean pauza = false;
	private boolean zaustavi = false;
	
	
	private Runnable posao = new Posao();
	
	public Prozor() {
		setTitle("TSP");
		setLocation(0, 0);
		setBackground(Color.DARK_GRAY);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setPreferredSize(new Dimension(1200, 600));
		
		nacrtajAlatnuTraku();
		nacrtajPanel();
		nacrtajKonzolu();
		
		pack();
	}

	private void nacrtajAlatnuTraku() {
		at = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));;
		at.setBackground(Color.GRAY);

		pokreni = new JButton("Start");
		pokreni.addActionListener(l -> {
			pokreni.setEnabled(false);
			pauziraj.setEnabled(true);
			zavrsi.setEnabled(true);
			
			BrojGradova = Integer.parseInt(unosgradova.getText());
			
			if(pauza) {
				pauza = false;
				return;
			}
			
			Thread radnik = new Thread(posao);
			radnik.setDaemon(true);
			radnik.start();
		});
		at.add(pokreni);
		
		
		unosgradova = new JTextField(10);
		at.add(unosgradova);
		
		pauziraj = new JButton("Pause");
		pauziraj.setEnabled(false);
		pauziraj.addActionListener(l -> {
			pauza = true;
			pokreni.setEnabled(true);
			pauziraj.setEnabled(false);
			zavrsi.setEnabled(false);
		});
		at.add(pauziraj);
		
		
		zavrsi = new JButton("Stop");
		zavrsi.setEnabled(false);
		zavrsi.addActionListener(l -> {
			zaustavi = true;
			pokreni.setEnabled(true);
			pauziraj.setEnabled(false);
			zavrsi.setEnabled(false);
		});
		at.add(zavrsi);
		
		add(at, BorderLayout.NORTH);		
	}

	private void nacrtajKonzolu() {
		konzola = new JTextArea();
		
		JScrollPane sp = new JScrollPane(konzola, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		sp.setPreferredSize(new Dimension(0, 100));
		add(sp, BorderLayout.SOUTH);
	}

	private void nacrtajPanel() {
		panel = new JDrawPanel(MAX_WIDTH-590, MAX_HEIGHT-280);
		
		add(panel, BorderLayout.CENTER);
		
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new Prozor().setVisible(true);
		});
	}

	private class Posao implements Runnable{

		@Override
		public void run() {
			Grad gradovi[] = new Grad[BrojGradova];
			
			for (int grad = 0; grad < BrojGradova; grad++) {
				int xPoz = (int) (panel.getWidth() * Math.random());
				int yPoz = (int) (panel.getHeight() * Math.random());
				
				gradovi[grad] = new Grad(xPoz, yPoz);
			}
			
			GA ga = new GA(BrojGradova, 0.005, 0.93, 3, 6);
			Populacija populacija = ga.initPopul(BrojGradova);
			ga.dobrotaPopulacije(populacija, gradovi);
			Obilazak novaruta = new Obilazak(populacija.najJedinka(0), gradovi); 
			
			final double startdist = novaruta.getDuljina();

			try {
				SwingUtilities.invokeAndWait(() -> {					
					konzola.append("Inicijalizirano je " + BrojGradova +" gradova.\n");
					konzola.append("Početna udaljenost: " + startdist + ".\n");
				});
			} catch (InvocationTargetException | InterruptedException e) {
				e.printStackTrace();
				System.exit(1);
			}
			
			for(int brojGeneracija = 1; ga.zadovoljenKriterij(brojGeneracija, MAX_GENERACIJA) == false; brojGeneracija++) {
				while (pauza) {
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				if(zaustavi) {
					zaustavi = false;
					return;
				}
				
				Obilazak ruta = new Obilazak(populacija.najJedinka(0), gradovi);
				populacija = ga.krizajPopulaciju_OX(populacija);
				populacija = ga.mutirajPopulaciju(populacija, gradovi);

				ga.dobrotaPopulacije(populacija, gradovi);
				
				final int gen = brojGeneracija;
				try {
					Grad[] gradoviNaRuti = ruta.getRuta();
					SwingUtilities.invokeAndWait(() -> {
						if(gen%10 == 0) {
							panel.ocistiPanel();
							
							for(Grad g: gradovi) {
								panel.nacrtajTocku(g.getX(), g.getY());
							}
							
							for(int i = 0; i < gradoviNaRuti.length - 1; i++) {
								Grad g1 = gradoviNaRuti[i];
								Grad g2 = gradoviNaRuti[i+1];
								
								panel.spojiTocke(g1.getX(), g1.getY(), g2.getX(), g2.getY());
							}
							
							panel.spojiTocke(gradoviNaRuti[gradovi.length-1].getX(), gradoviNaRuti[gradovi.length-1].getY(), gradoviNaRuti[0].getX(), gradoviNaRuti[0].getY());
						}
						
						konzola.append("| Generacija:  "+ gen + " | ---> Duljina ciklusa:  " + ruta.getDuljina() + " |\n");
					});
				} catch (InvocationTargetException | InterruptedException e) {
					e.printStackTrace();
					System.exit(1);
				}
			}
			
			Obilazak ruta = new Obilazak(populacija.najJedinka(0), gradovi);
			try {
				SwingUtilities.invokeAndWait(() -> {
					konzola.append("Zaustavljeno nakon " + MAX_GENERACIJA + " generacija.\n");
					konzola.append("Početna udaljenost: " + startdist + ".");
					konzola.append("***** Najbolji rezultat: " + ruta.getDuljina() + "*****");
					
				});
			} catch (InvocationTargetException | InterruptedException e) {
				e.printStackTrace();
				System.exit(1);
			}			
		}
	}
	
	private static class JDrawPanel extends JComponent {
		private int width;
		private int height;
		
		public int getWidth() {
			return width;
		}

		public int getHeight() {
			return height;
		}

		private static final long serialVersionUID = 1L;
		
		public JDrawPanel(int width, int height) {
			this.width = width;
			this.height = height;
			
			Dimension dim = new Dimension(width, height);
			
			setBackground(Color.DARK_GRAY);
			setSize(dim);
			setPreferredSize(dim);
			setMinimumSize(dim);
		}
		
		public void spojiTocke(int x1, int y1, int x2, int y2) {
			Graphics2D g = (Graphics2D) getGraphics();
			g.setColor(Color.GREEN);
			g.setStroke(new BasicStroke(2));
			g.drawLine(x1, y1, x2, y2);
		}
		
		public void nacrtajTocku(int x, int y) {
			Graphics2D g1 = (Graphics2D) getGraphics();
			g1.setColor(Color.WHITE);
			g1.drawOval(x, y, 6, 6);
			g1.fillOval(x, y, 6, 6);
		}
		
		public void ocistiPanel() {
			getGraphics().clearRect(0, 0, width, height);
		}

	}
}
