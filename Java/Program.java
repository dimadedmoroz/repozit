import java.util.Scanner;

class Program{
    public static void main(String[] args){
        System.out.println("Привет, выбери что ты хочешь сделать: ");
        Matrix m = null;
        Matrix[] all = new Matrix[3];
        int count = 0;
        Scanner ch = new Scanner(System.in);
        while(true){
        System.out.println();
        System.out.println("Создать матрицу (Напишите 1) max: 2");
        System.out.println("Удалить матрицу (Напишите 2)");
        System.out.println("Напечатать матрицу (Напишите 3)");
        System.out.println("Изменить матрицу (Напишите 4)");
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
                    int t = ch.nextInt();
                    all[t].delM(all, t);
                    count -= 1; break;
                }
                }
        }
    }
}


class Matrix{
    private Matrix[] all;
    private int x,y;
    private double arr[][];
    public static int mat = 0;

    public Matrix(){}
    public Matrix(int x, int y) {
        this.x = x;
        this.y = y;
        arr = new double[x][y];

        Scanner s = new Scanner(System.in); 
        for(int i = 0; i < x; i++){
            for(int j = 0; j < y; j++){
                System.out.println("Your rows: " + i + " your cols: " + j);
                double v = s.nextDouble();
                arr[i][j] = v;
            }
        }
        mat++;
    }
    public void getM(){
        System.out.print("Ваша матрица номер: " + mat);
        for(int i = 0; i < x; i++){
            System.out.println();
            for(int j = 0; j < y; j++){
                System.out.print(arr[i][j] + " ");
            }
        }
        System.out.println();
    }
    public void delM(Matrix[] all, int count){
        all[count] = null;
    }
}