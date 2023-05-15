public class Polynomial {
	double[] coefficients;
	
	public Polynomial() {
		double[] newC = {0};
		this.coefficients = newC;
	}
	public Polynomial(double[] coefficients) {
		this.coefficients = coefficients;
	}
	public Polynomial add(Polynomial p) {
		int max = Math.max(this.coefficients.length, p.coefficients.length);
		double[] newC = new double[max];
		double a = 0;
		double b = 0;

		for(int i = 0; i < max; i++) {
			if(i < p.coefficients.length) {
				a = p.coefficients[i];
			}
			if(i < this.coefficients.length) {
				b = this.coefficients[i];
			}
			newC[i] = a + b;
			a = 0;
			b = 0;
		}
		return new Polynomial(newC);
	}
	public double evaluate(double x) {
		double sum = 0.0;
		for(int i = 0; i < this.coefficients.length; i++){
			sum += this.coefficients[i] * Math.pow(x, i);
		}
		return sum;
	}
	public boolean hasRoot(double x) {
		return this.evaluate(x) == 0;
	}
}