package trgovackiputnik;

public class Jedinka {
	
	
	private int[] kromosom;
	private double fitness = -1;
	
	public Jedinka(int[] kromosom) {
		this.kromosom = kromosom;
	}

	public Jedinka(int kromosomLen) {
		int[] jedinka;
		jedinka = new int[kromosomLen];
		
		for (int redbroj = 0; redbroj < kromosomLen; redbroj++) {
			jedinka[redbroj] = redbroj;
		}
		
		this.kromosom = jedinka;
	}
	
	
	public int[] getKromosom() {
		return kromosom;
	}

	public void setKromosom(int[] kromosom) {
		this.kromosom = kromosom;
	}

	public double getFitness() {
		return fitness;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}
	
	public void setGen(int redbroj, int gen) {
		this.kromosom[redbroj] = gen;
	}
	
	public int getGen(int redbroj) {
		return this.kromosom[redbroj];
	}
	
	public int getKromosomLen() {
		return this.kromosom.length;
	}
	
	//Postoji li grad u kromosomu
	
	public boolean containsGen(int gen) {
		for (int i = 0; i < this.kromosom.length; i++) {
			if (this.kromosom[i] == gen) {
				return true;
			}
		}
		return false;
	}
	
	public String toString() {
		String ruta = "";
		for (int g = 0; g < this.kromosom.length; g++) {
			ruta += this.kromosom[g] + " | ";
		}
		return ruta;
	}
}
	