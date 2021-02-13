package hw6;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestHw6 {
    MainMt mainMt;

    @Before
    public void prepare(){
        mainMt = new MainMt();
    }

    @Test
    public void testOnesAndFours(){
        Assert.assertEquals(true, mainMt.isOneAndFour(new int[]{1,4,1}));
    }

    @Test
    public void testOnesWithoutFours(){
        Assert.assertEquals(false, mainMt.isOneAndFour(new int[]{1,1,1}));
    }

    @Test
    public void testFoursWithoutOnes(){
        Assert.assertEquals(false, mainMt.isOneAndFour(new int[]{4,4,4}));
    }

    @Test
    public void testOnesAndFoursAndOthers(){
        Assert.assertEquals(false, mainMt.isOneAndFour(new int[]{4,1,4,3}));
    }

    @Test
    public void testLastAfterFour(){
        Assert.assertArrayEquals(new int[]{5,6,7}, mainMt.lastAfterFour(new int[]{4,1,4,5,6,7}));
    }

    @Test
    public void testLastFour(){
        Assert.assertArrayEquals(new int[]{}, mainMt.lastAfterFour(new int[]{4,1,4,5,6,7,4}));
    }


    @Test (expected = RuntimeException.class)
    public void testLastAfterFourEx(){
        mainMt.lastAfterFour(new int[]{2,1,0,5,6,7,3});
    }
}
