package Java;
import java.util.Scanner;

class Program{
    public static void main(String[] args){
        System.out.println("Привет, выбери что ты хочешь сделать: ");
        Matrix m = null;
        Matrix[] all = new Matrix[3];   
        int count = 0;
        Scanner ch = new Scanner(System.in);
        System.out.println();
        while(true){
        System.out.println("Выйти из программы (Напишите 0)");
        System.out.println("Создать матрицу (Напишите 1) max: 2");
        System.out.println("Удалить матрицу (Напишите 2)");
        System.out.println("Напечатать матрицу (Напишите 3)");
        System.out.println("Изменить матрицу (Напишите 4)");
        System.out.println("Сложить матрицы (Напишите 5)");
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
                all[0].plucM(all[1]);
                System.out.println("Новая матрица:");
                all[0].getM();
                break;
            }
        }
    }
}


class Matrix{
    private int x,y;
    private double arr[][];
    public static int mat = 0;

    Scanner s = new Scanner(System.in);   
    public Matrix(){}                       //Создать матрицу
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
    public void plucM(Matrix other){
        for(int i = 0; i < x; i++){
            for(int j = 0; j < y; j++){
                this.arr[i][j] += other.arr[i][j];
            }
        }
    }
}

