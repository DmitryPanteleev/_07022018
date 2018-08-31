import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Iterator;

public class CheackHW {
    public static void main(String[] args) throws Exception {
        CheackHW cheackHW = new CheackHW();
        cheackHW.testSum("C://testFolder");
    }


    public void testSum(String path) throws Exception {
        File file = new File(path);
        String[] str = file.list();

        ArrayList<String> fileName = new ArrayList<String>();

        for (String o : str) {
            String[] mass = o.split("\\.");
            if (mass[1].equalsIgnoreCase("class")) {
                fileName.add(mass[0]);
            }
        }

        Iterator it = fileName.iterator();
        while (it.hasNext()) {
            String name = String.valueOf(it.next());
            Class ch = URLClassLoader.newInstance(new URL[]{new File(path).toURL()})
                    .loadClass(name);
            Constructor constructor = ch.getConstructor();
            Object calc = constructor.newInstance();

            Method m = ch.getDeclaredMethod("calc", int.class, int.class);

            int res = (Integer) m.invoke(calc, 2, 2);
            System.out.println(res);
            Method[] methods = ch.getMethods();
            for (Method method :
                    methods) {
                if (method.getParameterTypes().length == 4 &&
                        method.getParameterTypes()[0] == int.class &&
                        method.getParameterTypes()[1] == int.class &&
                        method.getParameterTypes()[2] == int.class &&
                        method.getParameterTypes()[3] == int.class &&
                        method.getReturnType() == int.class) {
                    int result = (Integer) method.invoke(calc, 2, 2, 2, 2);
                    System.out.println(result);
                } else if (method.getParameterTypes().length == 2 &&
                        method.getParameterTypes()[0] == int.class &&
                        method.getParameterTypes()[1] == int.class &&
                        method.getReturnType() == Boolean.class) {
                    Boolean result = (Boolean) method.invoke(calc, 10 + 5);
                    System.out.println(result);
                } else if (method.getParameterTypes().length == 1 &&
                        method.getParameterTypes()[0] == int.class &&
                        method.getReturnType() == String.class) {
                    String result = (String) method.invoke(calc, 10);
                    System.out.println(result);
                } else if (method.getParameterTypes().length == 1 &&
                        method.getParameterTypes()[0] == int.class &&
                        method.getReturnType() == Boolean.class) {
                    Boolean result = (Boolean) method.invoke(calc, -10);
                    System.out.println(result);
                } else if (method.getParameterTypes().length == 1 &&
                        method.getParameterTypes()[0] == String.class &&
                        method.getReturnType() == String.class) {
                    String result = (String) method.invoke(calc, "Name");
                    System.out.println(result);
                } else if (method.getParameterTypes().length == 1 &&
                        method.getParameterTypes()[0] == int.class &&
                        method.getReturnType() == Boolean.class) {
                    Boolean result = (Boolean) method.invoke(calc, 2018);
                    System.out.println(result);
                }
            }

            if (res == 4) {
                System.out.println(name + " Выполнил ДЗ");
            } else {
                System.out.println(name + " не выполнил ДЗ");
            }

        }

    }

}
