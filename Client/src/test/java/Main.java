import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);


        while (true){
            int choose = scanner.nextInt();
            System.out.println("===============菜单================");
            System.out.println("=           1  登录               =");
            System.out.println("=           2  退出               =");
            System.out.println("==================================");
            if (choose == 1){
                login();
            }else if (choose ==2){
                System.exit(0);
            }else {
                System.out.println("输入异常");
            }
        }
    }

    public static void login(){

    }

    public static void inputError(){

    }
}
