public class Complex {
    private double real;
    private double imag;

    public Complex(double real, double imag) {
        this.real = real;
        this.imag = imag;
    }

    public double getReal() { return real; }
    public double getImag() { return imag; }

    public Complex add(Complex o) {
        return new Complex(this.real + o.real, this.imag + o.imag);
    }

    public Complex subtract(Complex o) {
        return new Complex(this.real - o.real, this.imag - o.imag);
    }

    public Complex multiply(Complex o) {
        return new Complex(
            this.real * o.real - this.imag * o.imag,
            this.real * o.imag + this.imag * o.real
        );
    }

    public double absSq() {
        return real * real + imag * imag;
    }

    public Complex divide(Complex o) {
        double denom = o.absSq();
        if (denom == 0) throw new ArithmeticException("Деление на комплексный ноль");
        return new Complex(
            (this.real * o.real + this.imag * o.imag) / denom,
            (this.imag * o.real - this.real * o.imag) / denom
        );
    }

    @Override
    public String toString() {
        if (imag == 0) return String.valueOf(real);
        if (real == 0) return imag + "i";
        return real + (imag < 0 ? " - " + (-imag) + "i" : " + " + imag + "i");
    }

    public static Complex parse(String s) {
        s = s.replaceAll("\\s+", "");
        if (s.isEmpty()) return new Complex(0, 0);
        try {
            if (!s.contains("i")) return new Complex(Double.parseDouble(s), 0);
            if (s.equals("i") || s.equals("+i")) return new Complex(0, 1);
            if (s.equals("-i")) return new Complex(0, -1);
            
            int lastPlus = s.lastIndexOf('+');
            int lastMinus = s.lastIndexOf('-');
            if (lastMinus == 0) lastMinus = s.lastIndexOf('-', 1);
            int split = Math.max(lastPlus, lastMinus);
            
            if (split <= 0) {
                return new Complex(0, Double.parseDouble(s.replace("i", "")));
            } else {
                String r = s.substring(0, split);
                String im = s.substring(split, s.length() - 1);
                if (im.equals("+") || im.equals("")) im = "1";
                if (im.equals("-")) im = "-1";
                return new Complex(Double.parseDouble(r), Double.parseDouble(im));
            }
        } catch (Exception e) {
            return new Complex(0, 0);
        }
    }
}