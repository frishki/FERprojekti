package trgovackiputnik;
public class Obilazak {

		private Grad ruta[];
		private double duljina = 0;
		
		public Obilazak(Jedinka jedinka, Grad gradovi[]) {
			
			int kromosom[] = jedinka.getKromosom();
			
			this.ruta = new Grad[gradovi.length];
			
			for ( int gen = 0; gen < kromosom.length ; gen++) {
				this.ruta[gen] = gradovi[kromosom[gen]];
			}
		}
		
		public double getDuljina() {
			
			if (this.duljina > 0) {
				return this.duljina;
			}
			
			double ukupnaDuljina = 0;
			
			for (int grad= 0; grad + 1 < this.ruta.length ; grad++) {
				
				ukupnaDuljina += this.ruta[grad].udaljenostGradova(this.ruta[grad+1]);
				
			}
			
			ukupnaDuljina += this.ruta[this.ruta.length - 1].udaljenostGradova(this.ruta[0]);
			
			this.duljina = ukupnaDuljina;
			
			
			return duljina;
		}
		
		public Grad[] getRuta() {
			return ruta;
		}
}
