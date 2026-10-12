package Java;
import java.util.Scanner;

class Program{
    public static void main(String[] args){
        System.out.println("Выбери что ты хочешь сделать: ");
        Matrix m = null;
        Matrix[] all = new Matrix[3];   
        int count = 0;
        Scanner ch = new Scanner(System.in);
        System.out.println();
        while(true){
        System.out.println("Выйти из программы              (Напишите 0)");
        System.out.println("Создать матрицу                 (Напишите 1) max: 2");
        System.out.println("Удалить матрицу                 (Напишите 2)");
        System.out.println("Напечатать матрицу              (Напишите 3)");
        System.out.println("Изменить матрицу                (Напишите 4)");
        System.out.println("Сложить матрицы                 (Напишите 5)");
        System.out.println("Вычесть матрицы                 (Напишите 6)");
        System.out.println("Протранспонировать матрицу      (Напишите 7)");
        System.out.println("Перемножить матрицы             (Напишите 8)");
        System.out.println("Разделить матрицы               (Напишите 9)");
        System.out.println("Вычислить определитель матрицы  (Напишите 10)");
        int choise = ch.nextInt();
        switch (choise) {
            case 0:
                return;
            case 1:
                System.out.println("Сколько строк и столбцов?");
                int x = ch.nextInt();
                int y = ch.nextInt();
                all[count] = new Matrix(x, y); count++;
                System.out.println("Ваша матрица готова");
                break;
            case 2:
                if(count < 1 || count > 2){System.out.println("Матрицы не найдены"); break;} 
                if(count == 1) {
                    all[0].delM(all, 1); System.out.println("Матрица " + count + " удалена");
                    count -= 1; break;
                } else{
                    System.out.println("Какую матрицу вы хотите удалить? Всего: " + count);
                    int t = ch.nextInt()-1;
                    if(t < 1 || t > 2){System.out.println("Матрицы не найдены"); break;}
                    all[t].delM(all, t);
                    count -= 1;
                    System.out.println("Матрица " +(t+1) + " удалена");
                    break;
                }
            case 3:
                if(count < 1 || count > 2){System.out.println("Матрицы не найдены"); break;} 
                if(count == 1){
                    all[0].getM();
                    break;
                } else if (count == 2){
                    System.out.println("Какую матрицу вы хотите вывести? Всего: " + count);
                    int t = ch.nextInt();
                    if(t < 1 || t > 2){System.out.println("Матрицы не найдены"); break;}
                    all[t-1].getM();
                    break;
                }
            case 4:
                if(count < 1 || count > 2){System.out.println("Матрицы не найдены"); break;}
                 if(count == 1){
                    all[0].chanM();
                 } else {
                    System.out.println("Какую матрицу вы хотите изменить? Всего: " + count);
                    int t = ch.nextInt()-1;
                    if(t < 1 || t > 2){System.out.println("Матрицы не найдены"); break;}
                    all[t].chanM();
                    break;
                 }
            case 5:
                if(count < 1 || count > 2){System.out.println("Матрицы не найдены"); break;}
                if(count == 1){System.out.println("Вторая матрица не найдена"); break;}
                if(count == 2){
                    all[0].plucM(all[1]);
                    System.out.println("Новая матрица:");
                    all[0].getM();
                    break;
                }
            case 6:
                if(count < 1 || count > 2){System.out.println("Матрицы не найдены"); break;}
                if(count == 1){System.out.println("Вторая матрица не найдена"); break;}
                if(count == 2){
                    all[0].minusM(all[1]);
                    System.out.println("Новая матрица: ");
                    all[0].getM();
                    break;
                }
            case 7:
                if(count < 1 || count > 2){System.out.println("Матрицы не найдены"); break;}
                if(count == 1){all[0] = all[0].transpose(); System.out.println("Матрица протранспонирована"); break;}
                if(count == 2){
                    System.out.println("Какую матрицу вы хотите протранспонировать? Всего: " + count);
                    int t = ch.nextInt();
                    if(t < 1 || t > 2){System.out.println("Матрицы не найдены"); break;}
                    all[t-1] = all[t-1].transpose();
                    break;
                }
            case 8:
                if(count < 2){System.out.println("Нужно ровно 2 матрицы для умножения"); break;}
              Matrix resUmn = all[0].umnM(all[1]);
                if(resUmn != null) {
                    resUmn.getM();
                }
                break;
            case 9:
                if(count < 2){System.out.println("Нужно ровно 2 матрицы для деления"); break;}
                Matrix resDel = all[0].delM(all[1]);
                if(resDel != null) {
                    resDel.getM();
                }
                break;
            case 10:
                if(count < 1 || count > 2){System.out.println("Матрицы не найдены"); break;}
                if(count == 1){
                    double det = all[0].determinant();
                    System.out.println("Определитель матрицы: " + det);
                    break;
                } else {
                    System.out.println("Какую матрицу посчитать? Всего: " + count);
                    int t = ch.nextInt();
                    if(t < 1 || t > 2){System.out.println("Матрицы не найдены"); break;}
                    double det = all[t-1].determinant();
                    System.out.println("Определитель матрицы " + t + ": " + det);
                    break;
                }
            }   
        }
    }
}


class Matrix{
    private int x,y;
    private double arr[][];
    public static int mat = 0;

    Scanner s = new Scanner(System.in);   
    public Matrix(int x, int y) {
        this.x = x;
        this.y = y;
        arr = new double[x][y];

        for(int i = 0; i < x; i++){                   
            for(int j = 0; j < y; j++){
                System.out.println("Your rows: " + i + " your cols: " + j);
                double v = s.nextDouble();
                arr[i][j] = v;
            }
        }
        mat++;
    }
    public Matrix(int rows, int cols, boolean skip) {
    this.x = rows;
    this.y = cols;
    arr = new double[rows][cols];
    }

    public void getM(){                                     //Вывести матрицу
        System.out.print("Полученная матрица: ");
        for(int i = 0; i < x; i++){
            System.out.println();
            for(int j = 0; j < y; j++){
                System.out.print(arr[i][j] + " ");
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
        System.out.println("Число: "); double z = s.nextDouble();
        arr[a][b] = z;
    }
    public void plucM(Matrix other){                        //сложить матрицу
        for(int i = 0; i < x; i++){
            for(int j = 0; j < y; j++){
                this.arr[i][j] += other.arr[i][j];
            }
        }
    }
    public void minusM(Matrix other){                        //вычесть матрицу
        for(int i = 0; i < x; i++){
            for(int j = 0; j < y; j++){
                this.arr[i][j] -= other.arr[i][j];
            }
        }
    }

    public Matrix transpose() {                             //транспонировать матрицу
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
                for (int k = 0; k < this.y; k++) {
                    umn.arr[i][j] += this.arr[i][k] * other.arr[k][j];
                }
            }
        }
        return umn;
    }

    public Matrix delM(Matrix other) {
        Matrix inv = other.inverse();
        if (inv == null) {
            System.out.println("Ошибка: вторая матрица должна быть квадратной и иметь обратную матрицу.");
            return null;
        }
        return this.umnM(inv);
    }

    public Matrix inverse() {
        if (x != y) return null;
        int n = x;
        double[][] aug = new double[n][2 * n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) aug[i][j] = arr[i][j];
            aug[i][i + n] = 1.0;
        }
        for (int i = 0; i < n; i++) {
            double pivot = aug[i][i];
            if (Math.abs(pivot) < 1e-12) return null;
            for (int j = 0; j < 2 * n; j++) aug[i][j] /= pivot;
            for (int k = 0; k < n; k++) {
                if (k != i) {
                    double factor = aug[k][i];
                    for (int j = 0; j < 2 * n; j++) aug[k][j] -= factor * aug[i][j];
                }
            }
        }
        Matrix invMat = new Matrix(n, n, true);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) invMat.arr[i][j] = aug[i][j + n];
        }
        return invMat;
    }
    public double determinant() {
        if (x != y) {
            System.out.println("Ошибка: матрица не квадратная!");
            return 0;
        }
        if (x == 1) return arr[0][0];
        if (x == 2) {
            return arr[0][0] * arr[1][1] - arr[0][1] * arr[1][0];
        }
        
        double det = 0;
        for (int p = 0; p < x; p++) {
            Matrix sub = new Matrix(x - 1, y - 1, true);
            for (int i = 1; i < x; i++) {
                int subCol = 0;
                for (int j = 0; j < y; j++) {
                    if (j == p) continue;
                    sub.arr[i - 1][subCol++] = arr[i][j];
                }
            }
            det += Math.pow(-1, p) * arr[0][p] * sub.determinant();
        }
        return det;
    }
}