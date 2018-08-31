import DZ.Task2;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class Task2Test {
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {true, new int[]{2,1,4,4,1,1}},
                {true, new int[]{4,4,4,4,4}},
                {true, new int[]{1,1}},
                {true, new int[]{1,1,4,4,4,4,1,1}},
        });
    }

    private Task2 t;
    private boolean a;
    private int[] b;

    public Task2Test(boolean a, int[] b) {
        this.a = a;
        this.b = b;
    }

    @Before
    public void init() {
        t = new Task2();
    }

    @Test
    public void testTask2() {
        Assert.assertEquals(a, t.doTask2(b));
    }
}
