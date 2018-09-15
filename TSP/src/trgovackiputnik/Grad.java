package trgovackiputnik;

public class Grad {

	private int x;
	private int y; 
	
	public Grad(int x, int y) {
		this.x = x; 
		this.y = y; 
	}
	
	public double udaljenostGradova(Grad grad) {
		double deltaXSq = Math.pow((grad.getX() - this.getX()), 2);
		double deltaYSq = Math.pow((grad.getY() - this.getY()), 2);

		double udaljenost = Math.sqrt(Math.abs(deltaXSq + deltaYSq));
		return udaljenost;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
}
