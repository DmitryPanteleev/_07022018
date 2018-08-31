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
        cheackHW.testSum();
    }


    public void testSum() throws Exception {
        File file = new File("C:/0123");
        String[] str = file.list();

        ArrayList<String> fileName = new ArrayList<String>();

        for (String o: str) {
            String[] mass = o.split("\\.");
            if(mass[1].equalsIgnoreCase("class")) {
                fileName.add(mass[0]);
            }
        }

        Iterator it = fileName.iterator();
        while (it.hasNext()) {
            String name = String.valueOf(it.next());
            Class ch = URLClassLoader.newInstance(new URL[]{new File("C:/0123").toURL()})
                    .loadClass(name);
            Constructor constructor = ch.getConstructor();
            Object calc = constructor.newInstance();

            Method m = ch.getDeclaredMethod("calc", int.class, int.class);

            int res = (Integer) m.invoke(calc, 2,2);
            System.out.println(res);

            if(res == 4) {
                System.out.println(name + " Выполнил ДЗ");
            } else  {
                System.out.println(name + " не выполнил ДЗ");
            }

        }

    }

}
