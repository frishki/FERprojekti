package trgovackiputnik;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class Populacija {

	private Jedinka populacija[];
	private double dobrotaPopulacije = -1; 
	
	
	
	
	
	public Populacija(int velPopulacije) {
		this.populacija = new Jedinka[velPopulacije];
	}
	
	
	
	// kromosomLen je brojGradova
	public Populacija(int velPopulacije, int kromosomLen) {
		this.populacija = new Jedinka[velPopulacije];
		
		for (int i = 0; i < velPopulacije ; i++) {
			Jedinka jedinka = new Jedinka(kromosomLen);
			
			this.populacija[i] = jedinka;
		}
	}
	
	
	public Jedinka najJedinka(int redBroj){
		
		Arrays.sort(this.populacija, new Comparator<Jedinka>() {

			@Override
			public int compare(Jedinka o1, Jedinka o2) {
				if (o1.getFitness() > o2.getFitness()) {
					return -1;
				}
				else if (o1.getFitness() < o2.getFitness()) {
					return 1;
				}
				return 0;
			}
		});
		
		return this.populacija[redBroj];
	}
	
		
	public Jedinka[] getPopulacija() {
		return this.populacija;
	}
	
	
	
	public void setFitness(double dobrota) {
		
		this.dobrotaPopulacije = dobrota;
	}
	
	public double getFitness() {
		return this.dobrotaPopulacije;
	}
	
	public void shuffle() {
		Random rnd = new Random();
		for (int i = populacija.length - 1; i > 0; i--) {
			int index = rnd.nextInt(i + 1);
			Jedinka a = populacija[index];
			populacija[index] = populacija[i];
			populacija[i] = a;
		}
	}
	
	public int size() {
		return this.populacija.length;
	}
	
	public Jedinka getJedinka(int redBroj) {
		return populacija[redBroj];
	}
	
	public Jedinka setJedinka(Jedinka jedinka, int redBroj) {
		return populacija[redBroj] = jedinka;
	}
	
}
