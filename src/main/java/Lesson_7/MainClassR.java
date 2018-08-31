package Lesson_7;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class MainClassR {

    static Connection connection;
    static Statement stmt;

    public static void main(String[] args) throws Exception {


        prepareTable(Student.class);

//        Student student = new Student(1, "w", 10, "a");

        Student stud = new Student(1, "w", 10, "a");

        addStudent(stud.getClass());

    }

    public static void connect() throws Exception {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:main.db");
        stmt = connection.createStatement();
    }


    public static void disconnect() throws SQLException {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void prepareTable(Class c) throws SQLException {
        if (!c.isAnnotationPresent(XTable.class)) throw new RuntimeException("XTable missed");


        // если у нас есть поле int то напишем его как "INTEGER" и т.д.
        HashMap<Class, String> hm = new HashMap<Class, String>();
        hm.put(int.class, "INTEGER");
        hm.put(String.class, "TEXT");

        try {
            connect();
            // узнаем имя таблицы
            String tableName = ((XTable) c.getAnnotation(XTable.class)).name();
            // если такая таблица есть то ее дропнем
            stmt.executeUpdate("DROP TABLE IF EXISTS " + tableName + ";");

            // создадим таблицу  (но мы не можем сразу собрать запрос, соберем по кускам)
// 'CREATE TABLE IF NOT EXISTS students (id INTEGER, name TEXT, score INTEGER, email TEXT);'

            String query = "CREATE TABLE IF NOT EXISTS " + tableName + " (";
            // 'CREATE TABLE IF NOT EXISTS students ('
            // получаем все поля из класса
            Field[] fields = c.getDeclaredFields();
            for (Field o : fields) {
                // если есть анотация то добавляем ее в таблицу
                if (o.isAnnotationPresent(XField.class)) {
                    // если написать o.getName() + o.getType()
                    // будет проблема так как у нас String а нужен Text
                    // создадим HM которая преобразует класс к строке
                    query += o.getName() + " " + hm.get(o.getType()) + ", ";
                }
            }
            // 'CREATE TABLE IF NOT EXISTS students (id INTEGER, name TEXT, score INTEGER, email TEXT, ' // обрежем строку
            query = query.substring(0, query.length() - 2);
            // 'CREATE TABLE IF NOT EXISTS students (id INTEGER, name TEXT, score INTEGER, email TEXT'
            query += ");";
            // 'CREATE TABLE IF NOT EXISTS students (id INTEGER, name TEXT, score INTEGER);'
            stmt.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            disconnect();
        }
    }

    public static void addStudent(Class c) throws IllegalAccessException {
        Object o = c.getClass();
        Field[] fields = c.getDeclaredFields();
        int id = 0;
        String name = null;
        int score = 0;
        String email = null;
        for (Field f :
                fields) {
            if (f.getName() == "id") {
                id = f.getInt(o);
            } else if (f.getName() == "name") {
                name = String.valueOf(f.getChar(o));
            } else if (f.getName() == "score") {
                score = f.getInt(o);
            } else if (f.getName() == "email") {
                email = String.valueOf(f.getChar(o));
            }
        }

        String str = String.format("INSERT INTO students (id, name, score, email) " +
                "values ('%d','%S','%d','%S')", id, name, score, email);
        try {
            stmt.executeUpdate(str);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                disconnect();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


}


class Cat {
    public static int age;
    public String color;
    String name;

    public Cat(String name, String color, int age) {
        this.name = name;
        this.color = color;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @MyAnon
    public void info() {
        System.out.println(name + " " + color + " " + age);
    }

    @MyAnon(priotity = 4)
    public void info2() {
        System.out.println(name + " " + color + " " + age + " MyAnon");
    }
}


//    Class c = Cat.class;
//    Cat cat = new Cat("C", "W", 2);
//    Method[] methods = c.getDeclaredMethods();
//
//        for (Method o: methods) {
//                if(o.isAnnotationPresent(MyAnon.class)) {
//        o.invoke(cat);
//        }
//        }


