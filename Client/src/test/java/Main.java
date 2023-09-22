import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
//        Scanner scanner = new Scanner(System.in);
//
//
//        while (true){
//            int choose = scanner.nextInt();
//            System.out.println("===============菜单================");
//            System.out.println("=           1  登录               =");
//            System.out.println("=           2  退出               =");
//            System.out.println("==================================");
//            if (choose == 1){
//                login();
//            }else if (choose ==2){
//                System.exit(0);
//            }else {
//                System.out.println("输入异常");
//            }
//        }
        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
        SqlSessionFactory factory = builder.build(Resources.getResourceAsStream("mybatis-config.xml"));
        SqlSession sqlSession = factory.openSession(true);
        sqlSession.insert("message.insert_temp");
//        sqlSession.select("se",new Object(),null);
//        Class.forName("org.sqlite.JDBC");
//
//        Connection connection = DriverManager.getConnection("jdbc:sqlite:./Client/src/main/resources/message.db");
//        Statement statement = connection.createStatement();
//        ResultSet set = statement.executeQuery("select * from messages");
//        while (set.next()){
//            System.out.println(set.getString("1"));
//            System.out.println(set.getString("2"));
//            System.out.println(set.getString("3"));
//        }
    }

    public static void login(){

    }

    public static void inputError(){

    }
}
