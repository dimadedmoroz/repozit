import java.util.Scanner;

class Program{
    public static void main(String[] args){
        System.out.println("Выбери что ты хочешь сделать: ");
        Matrix[] all = new Matrix[3];    
        int count = 0;
        Scanner ch = new Scanner(System.in);
        System.out.println();
        while(true){
            System.out.println("\nВыйти из программы             (Напишите 0)");
            System.out.println("Создать матрицу                (Напишите 1) max: 2");
            System.out.println("Удалить матрицу                (Напишите 2)");
            System.out.println("Напечатать матрицу             (Напишите 3)");
            System.out.println("Изменить матрицу               (Напишите 4)");
            System.out.println("Сложить матрицы                (Напишите 5)");
            System.out.println("Вычесть матрицы                (Напишите 6)");
            System.out.println("Протранспонировать матрицу     (Напишите 7)");
            System.out.println("Перемножить матрицы            (Напишите 8)");
            System.out.println("Разделить матрицы              (Напишите 9)");
            System.out.println("Вычислить определитель матрицы (Напишите 10)");
            
            if (!ch.hasNextInt()) {
                System.out.println("Ошибка: введите число.");
                ch.next();
                continue;
            }
            
            int choise = ch.nextInt();
            switch (choise) {
                case 0:
                    return;
                case 1:
                    if (count >= 2) {
                        System.out.println("Уже создано максимум матриц (2).");
                        break;
                    }
                    System.out.println("Сколько строк и столбцов?");
                    int x = ch.nextInt();
                    int y = ch.nextInt();
                    if (x <= 0 || y <= 0) {
                        System.out.println("Размеры должны быть больше нуля!");
                        break;
                    }
                    all[count] = new Matrix(x, y); 
                    count++;
                    System.out.println("Ваша матрица готова");
                    break;
                case 2:
                    if(count < 1){System.out.println("Матрицы не найдены"); break;} 
                    if(count == 1) {
                        all[0] = null; 
                        System.out.println("Матрица удалена");
                        count = 0; 
                        break;
                    } else {
                        System.out.println("Какую матрицу вы хотите удалить? Всего: " + count);
                        int t = ch.nextInt();
                        if(t == 1){
                            all[0] = all[1];
                            all[1] = null;
                            count = 1;
                        } else if(t == 2){
                            all[1] = null;
                            count = 1;
                        } else {
                            System.out.println("Неверный номер.");
                            break;
                        }
                        System.out.println("Матрица удалена");
                        break;
                    }
                case 3:
                    if(count < 1){System.out.println("Матрицы не найдены"); break;} 
                    if(count == 1){
                        all[0].getM();
                        break;
                    } else {
                        System.out.println("Какую матрицу вы хотите вывести? Всего: " + count);
                        int t = ch.nextInt();
                        if(t < 1 || t > 2){System.out.println("Матрицы не найдены"); break;}
                        all[t-1].getM();
                        break;
                    }
                case 4:
                    if(count < 1){System.out.println("Матрицы не найдены"); break;}
                     if(count == 1){
                        all[0].chanM();
                     } else {
                        System.out.println("Какую матрицу вы хотите изменить? Всего: " + count);
                        int t = ch.nextInt();
                        if(t < 1 || t > 2){System.out.println("Матрицы не найдены"); break;}
                        all[t-1].chanM();
                        break;
                     }
                case 5:
                    if(count < 2){System.out.println("Нужно ровно 2 матрицы"); break;}
                    all[0].plucM(all[1]);
                    System.out.println("Результат (в первой матрице):");
                    all[0].getM();
                    break;
                case 6:
                    if(count < 2){System.out.println("Нужно ровно 2 матрицы"); break;}
                    all[0].minusM(all[1]);
                    System.out.println("Результат (в первой матрице):");
                    all[0].getM();
                    break;
                case 7:
                    if(count < 1){System.out.println("Матрицы не найдены"); break;}
                    if(count == 1){all[0] = all[0].transpose(); System.out.println("Матрица протранспонирована"); break;}
                    if(count == 2){
                        System.out.println("Какую матрицу протранспонировать? Всего: " + count);
                        int t = ch.nextInt();
                        if(t < 1 || t > 2){System.out.println("Матрицы не найдены"); break;}
                        all[t-1] = all[t-1].transpose();
                        System.out.println("Готово");
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
                    if(count < 1){System.out.println("Матрицы не найдены"); break;}
                    if(count == 1){
                        Complex det = all[0].determinant();
                        System.out.println("Определитель матрицы: " + det);
                        break;
                    } else {
                        System.out.println("Какую матрицу посчитать? Всего: " + count);
                        int t = ch.nextInt();
                        if(t < 1 || t > 2){System.out.println("Матрицы не найдены"); break;}
                        Complex det = all[t-1].determinant();
                        System.out.println("Определитель матрицы " + t + ": " + det);
                        break;
                    }
            }    
        }
    }
}