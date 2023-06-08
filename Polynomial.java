import java.io.*;
import java.util.Arrays;

public class Polynomial {
    double[] coefficients;
    int[] exponents;

    public Polynomial() {
        this.coefficients = new double[0];
        this.exponents = new int[0];
    }

    public Polynomial(double[] coefficients, int[] exponents) {
        this.coefficients = coefficients;
        this.exponents = exponents;
    }

    public Polynomial(File f) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(f));
        String in = br.readLine();
        if (in.equals("0")) {
            this.coefficients = new double[0];
            this.exponents = new int[0];
            return;
        }
        String[] arrLength = in.split("\\+|-");
        int len = arrLength.length;
        if (!(in.isEmpty()) && in.charAt(0) == '-') {//extra regexd term
            len--;
        }

        double[] newC = new double[len];
        int[] newE = new int[len];

        int c = 0;

        String[] posCoeff = in.split("\\+");
        if (posCoeff.length == 0) { //empty
            return;
        }
        for (int i = 0; i < posCoeff.length; i++) {
            if (!(posCoeff[i].contains("-"))) {
                String[] term = posCoeff[i].split("x");
                if (term.length == 0) { //just x
                    newC[c] = 1;
                    newE[c] = 0;
                    c++;
                } else if (term.length == 1) { //form of cx or c
                    if (posCoeff[i].length() == 1) { //c
                        newC[c] = Double.parseDouble(term[0]);
                        newE[c] = 0;
                        c++;
                    } else { //cx
                        newC[c] = Double.parseDouble(term[0]);
                        newE[c] = 1;
                        c++;
                    }
                } else { //len 2, cxe or xe
                    if (term[0].isEmpty()) { //xe
                        newC[c] = 1;
                        newE[c] = Integer.parseInt(term[1]);
                        c++;
                    } else { //cxe
                        newC[c] = Double.parseDouble(term[0]);
                        newE[c] = Integer.parseInt(term[1]);
                        c++;
                    }
                }
            }
            //need to get first term of this split
            else {
                String[] neg = posCoeff[i].split("-");
                if (neg.length > 0 && !(neg[0].isEmpty())) {
                    String[] term = neg[0].split("x");
                    if (term.length == 0) { //just x
                        newC[c] = 1;
                        newE[c] = 0;
                        c++;
                    } else if (term.length == 1) { //form of cx or c
                        if (neg[0].length() == 1) { //c
                            newC[c] = Double.parseDouble(term[0]);
                            newE[c] = 0;
                            c++;
                        } else { //cx
                            newC[c] = Double.parseDouble(term[0]);
                            newE[c] = 1;
                            c++;
                        }
                    } else { //len 2, cxe or xe
                        if (term[0].isEmpty()) { //xe
                            newC[c] = 1;
                            newE[c] = Integer.parseInt(term[1]);
                            c++;
                        } else { //cxe
                            newC[c] = Double.parseDouble(term[0]);
                            newE[c] = Integer.parseInt(term[1]);
                            c++;
                        }
                    }
                }
            }
        }
        String[] negCoeff = in.split("-");
        if (negCoeff.length == 0) { //empty
            return;
        }
        for (int i = 0; i < negCoeff.length; i++) {
            if (!(negCoeff[i].contains("+")) && !(negCoeff[i].isEmpty())) {
                String[] term = negCoeff[i].split("x");
                if (term.length == 0) { //just x
                    newC[c] = -1;
                    newE[c] = 0;
                    c++;
                } else if (term.length == 1) { //form of cx or c
                    if (negCoeff[i].length() == 1) { //c
                        newC[c] = Double.parseDouble(term[0]) * -1;
                        newE[c] = 0;
                        c++;
                    } else { //cx
                        newC[c] = Double.parseDouble(term[0]) * -1;
                        newE[c] = 1;
                        c++;
                    }
                } else { //len 2, cxe or xe
                    if (term[0].isEmpty()) { //xe
                        newC[c] = -1;
                        newE[c] = Integer.parseInt(term[1]);
                        c++;
                    } else { //cxe
                        newC[c] = Double.parseDouble(term[0]) * -1;
                        newE[c] = Integer.parseInt(term[1]);
                        c++;
                    }
                }
            }
        }
        this.coefficients = newC;
        this.exponents = newE;
    }

    public Polynomial add(Polynomial p) {
        int expCount = 0; //create new exponent array
        int indexCount = 0;
        for (int i = 0; i < this.exponents.length; i++) {
            if (find(p.exponents, this.exponents[i]) == -1) {
                expCount++;
            }
        }
        for (int i = 0; i < p.exponents.length; i++) {
            expCount++;
        }
        int[] newExp = new int[expCount];
        double[] newCoeff = new double[expCount];
        for (int i = 0; i < this.exponents.length; i++) {
            if (find(p.exponents, this.exponents[i]) == -1) {
                newExp[indexCount] = this.exponents[i];
                indexCount++;
            }
        }
        for (int i = 0; i < p.exponents.length; i++) {
            newExp[indexCount] = p.exponents[i];
            indexCount++;
        }
        Arrays.sort(newExp);
        //add coefficients of each exponent
        for (int i = 0; i < expCount; i++) {
            int t = find(this.exponents, newExp[i]);
            int pf = find(p.exponents, newExp[i]);
            if (t != -1 && pf != -1) {
                newCoeff[i] = this.coefficients[t] + p.coefficients[pf];
            } else if (t != -1) {
                newCoeff[i] = this.coefficients[t];
            } else {
                newCoeff[i] = p.coefficients[pf];
            }
        }
        //remove zero coefficients
        int remCount = 0;
        for (int i = 0; i < expCount; i++) {
            if (newCoeff[i] == 0) {
                remCount++;
            }
        }
        int[] finalExp = new int[expCount - remCount];
        double[] finalCoeff = new double[expCount - remCount];

        int c = 0;
        for (int i = 0; i < expCount; i++) {
            if (newCoeff[i] != 0) {
                finalCoeff[c] = newCoeff[i];
                finalExp[c] = newExp[i];
                c++;
            }
        }

        return new Polynomial(finalCoeff, finalExp);
    }

    public Polynomial multiply(Polynomial p) {
        if (this.exponents.length == 0 || p.exponents.length == 0) {
            return new Polynomial();
        }
        Polynomial prev = new Polynomial();
        double[] currCoeff = new double[p.exponents.length];
        int[] currExp = new int[p.exponents.length];
        for (int i = 0; i < this.exponents.length; i++) {
            for (int j = 0; j < p.exponents.length; j++) {
                currCoeff[j] = this.coefficients[i] * p.coefficients[j];
                currExp[j] = this.exponents[i] + p.exponents[j];
            }
            Polynomial curr = new Polynomial(currCoeff, currExp);
            prev = prev.add(curr);
        }
        return prev;
    }

    public double evaluate(double x) {
        double sum = 0.0;
        for (int i = 0; i < this.coefficients.length; i++) {
            sum += this.coefficients[i] * Math.pow(x, this.exponents[i]);
        }
        return sum;
    }

    public boolean hasRoot(double x) {
        return this.evaluate(x) == 0;
    }

    public void saveToFile(String fname) throws IOException {
        FileWriter fout = new FileWriter(fname);
        if (exponents.length == 0) {
            fout.write("0");
            fout.close();
            return;
        }
        String out = "";
        String start = "+";

        for (int i = 0; i < exponents.length; i++) {
            if (out.isEmpty()) {
                start = "";
            }
            if (exponents[i] == 0) { //c
                if (coefficients[i] < 0) {
                    out += coefficients[i];
                } else {
                    out += start + coefficients[i];
                }
            } else if (coefficients[i] == 1) { //x or xe
                if (exponents[i] == 1) { //x
                    out += start + "x";
                } else { //xe
                    out += start + "x" + exponents[i];
                }
            } else { //cx or cxe
                if (exponents[i] == 1) { //cx
                    if (coefficients[i] < 0) {
                        out += coefficients[i] + "x";
                    } else {
                        out += start + coefficients[i] + "x";
                    }
                } else { //cxe
                    if (coefficients[i] < 0) {
                        if (coefficients[i] < 0) {
                            out += coefficients[i] + "x" + exponents[i];
                        } else {
                            out += start + coefficients[i] + "x" + exponents[i];
                        }
                    }
                }

            }
            start = "+";
        }
        fout.write(out);
        fout.close();
    }

    public int find(int[] arr, int x) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == x) {
                return i;
            }
        }
        return -1;
    }

    public void printStuff() {
        System.out.println("coeff: " + Arrays.toString(this.coefficients));
        System.out.println("exp: " + Arrays.toString(this.exponents));
    }
}