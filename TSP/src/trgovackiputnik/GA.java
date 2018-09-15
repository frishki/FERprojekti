package trgovackiputnik;
import java.util.Arrays;

public class GA {

	
	private int velPop;
	private double paramMut;
	private double paramKriz;
	private int velTurnir;
	private int elitizam;
	
	
	
	public GA(int velPop, double paramMut, double paramKriz, int velTurnir, int elitizam){
		
		this.velPop = velPop;
		this.velTurnir = velTurnir;
		this.paramMut = paramMut;
		this.paramKriz = paramKriz;
		this.elitizam = elitizam;
	}
	
	
	//populacija ima jedinke ciji je kromosom velicine broja gradova
	public Populacija initPopul(int kromosomLen) {
		
		Populacija populacija = new Populacija(this.velPop, kromosomLen);
		return populacija;
	}
	
	
	public double dobrotaJedinke(Jedinka jedinka, Grad gradovi[]) {
		
		Obilazak ruta = new Obilazak(jedinka, gradovi);
		double dobrota = 1 / ruta.getDuljina();
		
		jedinka.setFitness(dobrota);
		
		return dobrota;
	}
	
	public void dobrotaPopulacije(Populacija populacija, Grad gradovi[]) {
		double dobrota = 0; 
		
		for (Jedinka jed : populacija.getPopulacija()) {
			dobrota += this.dobrotaJedinke(jed, gradovi);
		}
		
		double avgDobrota = dobrota / populacija.size();
		
		populacija.setFitness(avgDobrota);
	}
	

	
	public Jedinka Selekcija(Populacija populacija) {
		
		Populacija turnirska = new Populacija(this.velTurnir);
		
		populacija.shuffle();
		
		for (int i = 0 ; i < this.velTurnir ; i++ ) {
			
			Jedinka jedTurnira = populacija.getJedinka(i);
			
			turnirska.setJedinka(jedTurnira, i);
		}
		
		return turnirska.najJedinka(0); 
	}
	
	//treba doradit jos
	public Populacija krizajGSX(Populacija populacija) {
		
		
		Populacija novapopulacija = new Populacija(populacija.size());
		
		for (int jed = 0; jed < populacija.size() ; jed++) {
			
			Jedinka rod1 = populacija.najJedinka(jed);
			
			
			if (this.paramKriz > Math.random() && jed >= elitizam) {
				
				Jedinka rod2 = this.Selekcija(populacija);
				
				int novikromosom[] = new int[rod1.getKromosomLen()];
				Arrays.fill(novikromosom, -1);
				
				Jedinka dijete = new Jedinka(novikromosom);
				
				int rod1indeks = (int) (Math.random() * rod1.getKromosomLen());
				int rod2indeks = (int) (Math.random() * rod1.getKromosomLen());
				int zajednickigen = rod1.getGen(rod1indeks);
				
				
				if (rod2.containsGen(zajednickigen)) {
					
					for (int i = 0; i < rod2.getKromosomLen(); i++) {
						
						if (zajednickigen == rod2.getGen(i)) {
							rod2indeks = i;
							break;
						}
					}
					
					
					int dijeteindeks = (int) ((rod1indeks + rod2indeks)/2 * Math.random());
					
					dijete.setGen(dijeteindeks, zajednickigen);
					
					
					rod1indeks++;
					rod2indeks--;
					
					boolean sudargena = false;
					int negodmak = 1;
					int pozodmak = 1;
					
					while (sudargena == false) {
						
						
						if (dijete.containsGen(rod1.getGen(rod1indeks)) == false && (dijeteindeks + pozodmak <= dijete.getKromosomLen())) {
							
							dijete.setGen(dijeteindeks + pozodmak, rod1.getGen(rod1indeks));
							pozodmak++;
						
						} else {
							
							
							sudargena = true;
							break;
						}
						
						rod1indeks++;
						
						if (dijete.containsGen(rod2.getGen(rod2indeks)) == false) {
							
							if (dijeteindeks - negodmak >= 0) {
								
								dijete.setGen(dijeteindeks - negodmak, rod2.getGen(rod2indeks));
								negodmak++;				
							}
				
						} else {
							
							sudargena = true;
							break;
						}
						
						rod2indeks--;
						
					}
					//ostatak dodaj nasumicno
					
					for ( int ost = 0; ost < dijete.getKromosomLen() ; ost++) {
						
						if (dijete.getGen(ost) == -1) {
							
							for (int o = 0; o < rod1.getKromosomLen() ; o++) {
								
								if (dijete.containsGen(rod1.getGen(o)) == false){
									dijete.setGen(ost, rod1.getGen(o));
									break;
								}
								if (dijete.containsGen(rod2.getGen(o)) == false){
									dijete.setGen(ost, rod2.getGen(o));
									break;
								}
							}
						}
						
					}
						
					//svi geni dodani
						
				} else {
				
				novapopulacija.setJedinka(rod1, jed);
				}	
				
		}
		
	}
		
		return novapopulacija;
}
	
	
	
	
	
	
	
	
	public Populacija krizajPopulaciju_OX(Populacija populacija) {
			
		
		Populacija novapopulacija = new Populacija(populacija.size());
		
		
		for (int jed = 0 ; jed < populacija.size() ; jed++) {
			
			Jedinka rod1 = populacija.najJedinka(jed);
			
			if (this.paramKriz > Math.random() && jed >= elitizam) {
				
				Jedinka rod2 = this.Selekcija(populacija);
				
				int novikromosom[] = new int[rod1.getKromosomLen()];
				Arrays.fill(novikromosom, -1);
			
				Jedinka dijete = new Jedinka(novikromosom);
			
				int substrPos1 = (int) (Math.random() * rod1.getKromosomLen());
				int substrPos2 = (int) (Math.random() * rod1 .getKromosomLen());
			
				final int pocSubstr = Math.min(substrPos1, substrPos2);
                final int krajSubstr = Math.max(substrPos1, substrPos2);
                
	                for (int i = pocSubstr; i < krajSubstr; i++) {
	                    dijete.setGen(i, rod1.getGen(i));
	                }
	                
	                
	                for (int i = 0; i < rod2.getKromosomLen(); i++) {
	                	
	                	int rod2gen = i + krajSubstr;
	                	
	                	if (rod2gen >= rod2.getKromosomLen()) {
	                		rod2gen -= rod2.getKromosomLen();
	                	}
	                	
	                	//ostatak dodajemo slijedno 
	                	if(dijete.containsGen(rod2.getGen(rod2gen)) == false) {
	                	
	                		for (int j = 0 ; j < dijete.getKromosomLen(); j++ ) {
	                			
	                			if (dijete.getGen(j) == -1) {
	                				dijete.setGen(j, rod2.getGen(rod2gen));
	                				break;
	                			}
	                		}
	               
	                	}
	                }
	                
	                
	                novapopulacija.setJedinka(dijete, jed);
	                
			} else {
					
				novapopulacija.setJedinka(rod1, jed);
			
			}
			
		}
		
		
		return novapopulacija;
		
	}
	                	
	               
	      
	
	
	public Populacija mutirajPopulaciju(Populacija populacija, Grad gradovi[]) {
		
		
		Populacija novapopulacija = new Populacija(this.velPop);
		
		
		for (int redBroj = 0; redBroj < populacija.size(); redBroj++) {
			
			Jedinka jedinka = populacija.najJedinka(redBroj);
			
			Jedinka probnajedinka = jedinka;
			
			if (redBroj >= this.elitizam) {
			
				for (int genpoz = 0; genpoz < jedinka.getKromosomLen(); genpoz++) {
					
					if (this.paramMut > Math.random()) {
						
						int novapoz = (int) (Math.random() * jedinka.getKromosomLen());
						
						int gen1 = jedinka.getGen(novapoz);
						int gen2 = jedinka.getGen(genpoz);
						
						probnajedinka.setGen(genpoz, gen1);
						probnajedinka.setGen(novapoz, gen2);
						
						Obilazak novaruta = new Obilazak(jedinka, gradovi);
						Obilazak stararuta = new Obilazak(probnajedinka, gradovi);
						
						//ako imamo bolju rutu, zamjeni gene jedinke
						if (novaruta.getDuljina() <= stararuta.getDuljina()) {
							
							jedinka.setGen(genpoz, gen1);
							jedinka.setGen(novapoz, gen2);
						}
						
					}
				}
			
			}
			
			novapopulacija.setJedinka(jedinka, redBroj);
			
		}
		
		return novapopulacija;
	}
	

	
	public boolean zadovoljenKriterij(int brojGeneracija, int maxgeneracija) {
		return (brojGeneracija > maxgeneracija);
	}
	
	
}
