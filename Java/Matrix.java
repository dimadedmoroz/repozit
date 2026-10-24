import java.util.Scanner;

class Matrix {
    private int x, y;
    private Complex[][] arr;
    public static int mat = 0;

    Scanner s = new Scanner(System.in);   
    public Matrix(int x, int y) {
        this.x = x;
        this.y = y;
        arr = new Complex[x][y];

        for(int i = 0; i < x; i++){                
            for(int j = 0; j < y; j++){
                System.out.println("Your rows: " + i + " your cols: " + j + " (например 3+2i или 5):");
                String v = s.next();
                arr[i][j] = Complex.parse(v);
            }
        }
        mat++;
    }
    
    public Matrix(int rows, int cols, boolean skip) {
        this.x = rows;
        this.y = cols;
        arr = new Complex[rows][cols];
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                arr[i][j] = new Complex(0, 0);
            }
        }
    }

    public int getRows() { return x; }
    public int getCols() { return y; }

    public void getM(){                                        //Вывести матрицу
        System.out.print("Полученная матрица: ");
        for(int i = 0; i < x; i++){
            System.out.println();
            for(int j = 0; j < y; j++){
                System.out.print(arr[i][j] + "\t");
            }
        }
        System.out.println();
    }

    public void delM(Matrix[] all, int count){              //Удалить матрицу
        all[count] = null;
    }

    public void chanM(){
        System.out.println("Строка: "); int a = s.nextInt()-1; 
        System.out.println("Столбец: "); int b = s.nextInt()-1;
        if (a < 0 || a >= x || b < 0 || b >= y) {
            System.out.println("Ошибка: выход за границы матрицы!");
            return;
        }
        System.out.println("Число (например, 3+2i): "); 
        String z = s.next();
        arr[a][b] = Complex.parse(z);
    }

    public void plucM(Matrix other){                        //сложить матрицу
        if (this.x != other.x || this.y != other.y) {
            System.out.println("Ошибка: нельзя сложить матрицы разных размеров (" + x + "x" + y + " и " + other.x + "x" + other.y + ")");
            return;
        }
        for(int i = 0; i < x; i++){
            for(int j = 0; j < y; j++){
                this.arr[i][j] = this.arr[i][j].add(other.arr[i][j]);
            }
        }
    }

    public void minusM(Matrix other){                        //вычесть матрицу
        if (this.x != other.x || this.y != other.y) {
            System.out.println("Ошибка: нельзя вычесть матрицы разных размеров (" + x + "x" + y + " и " + other.x + "x" + other.y + ")");
            return;
        }
        for(int i = 0; i < x; i++){
            for(int j = 0; j < y; j++){
                this.arr[i][j] = this.arr[i][j].subtract(other.arr[i][j]);
            }
        }
    }

    public Matrix transpose() {                           //транспонировать матрицу
        Matrix trans = new Matrix(y, x, true); 
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                trans.arr[j][i] = this.arr[i][j];
            }
        }
        return trans;
    }

    public Matrix umnM(Matrix other) {
        if (this.y != other.x) {
            System.out.println("Ошибка: число столбцов первой матрицы должно быть равно числу строк второй.");
            return null;
        }
        Matrix umn = new Matrix(this.x, other.y, true);
        for (int i = 0; i < this.x; i++) {
            for (int j = 0; j < other.y; j++) {
                Complex sum = new Complex(0, 0);
                for (int k = 0; k < this.y; k++) {
                    sum = sum.add(this.arr[i][k].multiply(other.arr[k][j]));
                }
                umn.arr[i][j] = sum;
            }
        }
        return umn;
    }

    public Matrix delM(Matrix other) {
        if (other.x != other.y) {
            System.out.println("Ошибка: делить можно только на квадратную матрицу.");
            return null;
        }
        Matrix inv = other.inverse();
        if (inv == null) {
            System.out.println("Ошибка: обратная матрица не существует.");
            return null;
        }
        return this.umnM(inv);
    }

    public Matrix inverse() {
        if (x != y) return null;
        int n = x;
        Complex[][] aug = new Complex[n][2 * n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) aug[i][j] = arr[i][j];
            for (int j = 0; j < n; j++) {
                aug[i][j + n] = (i == j) ? new Complex(1, 0) : new Complex(0, 0);
            }
        }
        for (int i = 0; i < n; i++) {
            Complex pivot = aug[i][i];
            if (pivot.absSq() < 1e-12) return null;
            for (int j = 0; j < 2 * n; j++) aug[i][j] = aug[i][j].divide(pivot);
            for (int k = 0; k < n; k++) {
                if (k != i) {
                    Complex factor = aug[k][i];
                    for (int j = 0; j < 2 * n; j++) aug[k][j] = aug[k][j].subtract(factor.multiply(aug[i][j]));
                }
            }
        }
        Matrix invMat = new Matrix(n, n, true);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) invMat.arr[i][j] = aug[i][j + n];
        }
        return invMat;
    }

    public Complex determinant() {
        if (x != y) {
            System.out.println("Ошибка: матрица не квадратная!");
            return new Complex(0, 0);
        }
        if (x == 1) return arr[0][0];
        if (x == 2) {
            return arr[0][0].multiply(arr[1][1]).subtract(arr[0][1].multiply(arr[1][0]));
        }
        
        Complex det = new Complex(0, 0);
        for (int p = 0; p < x; p++) {
            Matrix sub = new Matrix(x - 1, y - 1, true);
            for (int i = 1; i < x; i++) {
                int subCol = 0;
                for (int j = 0; j < y; j++) {
                    if (j == p) continue;
                    sub.arr[i - 1][subCol++] = arr[i][j];
                }
            }
            Complex sign = (p % 2 == 0) ? new Complex(1, 0) : new Complex(-1, 0);
            det = det.add(sign.multiply(arr[0][p]).multiply(sub.determinant()));
        }
        return det;
    }
}
