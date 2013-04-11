package org.yaml.snakeyaml.array;

import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Assert;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.ConstructorException;
import org.yaml.snakeyaml.error.YAMLException;

public class PrimitiveArrayTest extends TestCase {
    
    private final String pkg = "!!org.yaml.snakeyaml.array";
    
    private final byte[]    bytes    = new byte[]{1,2,3};
    private final short[]    shorts    = new short[]{300,301,302};
    private final int[]        ints    = new int[]{40000,40001,40002};
    private final long[]    longs    = new long[]{5000000000L,5000000001L};
    private final float[]    floats    = new float[]{0.1f,3.1415f};
    private final double[]    doubles    = new double[]{50.0001, 2150.0002};
    private final char[]    chars    = new char[]{'a', 'b', 'c', 'd', 'e'};
    private final boolean[]    bools    = new boolean[]{true, false};

    public void testValidConstructor () {
        String testInput =
                "- " + pkg + ".ByteArr [ " + Arrays.toString(bytes) + " ]\n" +
                "- " + pkg + ".ShortArr [ " + Arrays.toString(shorts) + " ]\n" +
                "- " + pkg + ".IntArr [ " + Arrays.toString(ints) + " ]\n" +
                "- " + pkg + ".LongArr [ " + Arrays.toString(longs) + " ]\n" +
                "- " + pkg + ".FloatArr [ " + Arrays.toString(floats) + " ]\n" +
                "- " + pkg + ".DoubleArr [ " + Arrays.toString(doubles) + " ]\n" +
                "- " + pkg + ".CharArr [ " + Arrays.toString(chars) + " ]\n" +
                "- " + pkg + ".BooleanArr [ " + Arrays.toString(bools) + " ]\n";
        
        Yaml yaml = new Yaml();
        List<Object> wrappers = (List<Object>)yaml.load(testInput);

        Assert.assertArrayEquals ( bytes,  ((ByteArr)  wrappers.get(0)).getBytes() );
        Assert.assertArrayEquals ( shorts, ((ShortArr) wrappers.get(1)).getShorts() );
        Assert.assertArrayEquals ( ints,   ((IntArr)   wrappers.get(2)).getInts() );
        Assert.assertArrayEquals ( longs,  ((LongArr)  wrappers.get(3)).getLongs() );
        Assert.assertArrayEquals ( floats, ((FloatArr) wrappers.get(4)).getFloats(), 0.001f );
        Assert.assertArrayEquals ( doubles,((DoubleArr)wrappers.get(5)).getDoubles(), 0.001 );
        Assert.assertArrayEquals ( chars,  ((CharArr)  wrappers.get(6)).getChars() );
        assertArrayEquals        ( bools, ((BooleanArr)wrappers.get(7)).getBools() );
    }
    
    /*
     * For some reason, every other assertArrayEquals specialization is
     * provided by org.junit.Assert, but not this one.
     */
    private void assertArrayEquals ( boolean[] expected, boolean[] actuals ) {
        assertEquals ( "Arrays differ in length", expected.length, actuals.length );
        for ( int i = 0; i < expected.length; ++i ) {
            if ( expected[i] != actuals[i] ) {
                fail ( "Arrays first differ at " + i + "; expected " + expected[i] + " but got " + actuals[i] ); 
            }
        }
    }
    
    private void tryInvalid ( String t, Class<?> expectedException ) {
        Yaml yaml = new Yaml();
        try {
            Object loaded = yaml.load(t);
            fail ( "Expected " + expectedException.getCanonicalName() + " but loaded = \"" + loaded + "\"" );
        } catch ( YAMLException e ) {
            assertEquals ( expectedException, e.getCause().getClass() );
        }
    }
    
    public void testInvalidConstructors() {
        // Loading a character as any primitive other than 'char' is a ClassCastException
        tryInvalid ( pkg + ".ByteArr [ [ 'a' ] ]", ClassCastException.class );
        tryInvalid ( pkg + ".ShortArr [ [ 'a' ] ]", ClassCastException.class );
        tryInvalid ( pkg + ".IntArr [ [ 'a' ] ]", ClassCastException.class );
        tryInvalid ( pkg + ".LongArr [ [ 'a' ] ]", ClassCastException.class );
        tryInvalid ( pkg + ".FloatArr [ [ 'a' ] ]", ClassCastException.class );
        tryInvalid ( pkg + ".DoubleArr [ [ 'a' ] ]", ClassCastException.class );
        tryInvalid ( pkg + ".BooleanArr [ [ 'a' ] ]", ClassCastException.class );
        
        // Loading a floating-point number as a character is a ClassCastException
        tryInvalid ( pkg + ".CharArr [ [ 1.2 ] ]", ClassCastException.class );
        
        // Loading a String as a Character is a YAMLException
        tryInvalid ( pkg + ".CharArr [ [ 'abcd' ] ]", YAMLException.class );
        
    }
    
    public void testTruncation() {
        Yaml yaml = new Yaml();
        
        // Loading floating-point numbers as integer types is allowed, because
        // that's how the Number.byteValue(), .shortValue(), .intValue(),
        // .longValue().
        Assert.assertArrayEquals (
                new byte[]{3,5},
                ((ByteArr)yaml.load(pkg + ".ByteArr [ [ 3.14, 5.2 ] ] ")).getBytes()
        );

        Assert.assertArrayEquals (
                new short[]{3,5},
                ((ShortArr)yaml.load(pkg + ".ShortArr [ [ 3.14, 5.2 ] ] ")).getShorts()
        );

        Assert.assertArrayEquals (
                new int[]{3,5},
                ((IntArr)yaml.load(pkg + ".IntArr [ [ 3.14, 5.2 ] ] ")).getInts()
        );

        Assert.assertArrayEquals (
                new long[]{3,5},
                ((LongArr)yaml.load(pkg + ".LongArr [ [ 3.14, 5.2 ] ] ")).getLongs()
        );
    }
    
    public void testPromotion() {
        Yaml yaml = new Yaml();
        
        // Loading integer numbers as floating-point types is allowed...
        Assert.assertArrayEquals (
                new float[]{3,5},
                ((FloatArr)yaml.load(pkg + ".FloatArr [ [ 3, 5 ] ] ")).getFloats(),
                0.001f
        );
        
        Assert.assertArrayEquals (
                new double[]{3,5},
                ((DoubleArr)yaml.load(pkg + ".DoubleArr [ [ 3, 5 ] ] ")).getDoubles(),
                0.001f
        );
    }
    
    public void testStringCharArray () {
        Yaml yaml = new Yaml();
        
        try {
            yaml.load(pkg + ".CharArr [ [ abcd ] ]");
            fail ( "Expected exception." );
        } catch ( Exception e ) {
            assertEquals ( ConstructorException.class, e.getClass() );
            assertEquals ( "expected char but got \"abcd\"", e.getCause().getMessage() );
        }
    }
    
    private static Object cycle ( Object in ) {
        Yaml yaml = new Yaml();
        String dumped = yaml.dump(in);
        //System.out.println ( dumped );
        return yaml.load(dumped);
    }
    
    /**
     * All kinds of primitive arrays should be able to cycle from (Java array)
     * to (YAML string) to (Java array) again, and they should be exactly the
     * same before and after. 
     */
    public void testCycle() {
        ByteArr byteArr = new ByteArr(bytes);
        Assert.assertArrayEquals ( byteArr.getBytes(), ((ByteArr)cycle(byteArr)).getBytes() );
        
        ShortArr shortArr = new ShortArr(shorts);
        Assert.assertArrayEquals ( shortArr.getShorts(), ((ShortArr)cycle(shortArr)).getShorts() );

        IntArr intArr = new IntArr(ints);
        Assert.assertArrayEquals ( intArr.getInts(), ((IntArr)cycle(intArr)).getInts() );

        LongArr longArr = new LongArr(longs);
        Assert.assertArrayEquals ( longArr.getLongs(), ((LongArr)cycle(longArr)).getLongs() );

        FloatArr floatArr = new FloatArr(floats);
        Assert.assertArrayEquals ( floatArr.getFloats(), ((FloatArr)cycle(floatArr)).getFloats(), 0.001f );

        DoubleArr doubleArr = new DoubleArr(doubles);
        Assert.assertArrayEquals ( doubleArr.getDoubles(), ((DoubleArr)cycle(doubleArr)).getDoubles(), 0.001 );

        CharArr charArr = new CharArr(chars);
        Assert.assertArrayEquals ( charArr.getChars(), ((CharArr)cycle(charArr)).getChars() );

        BooleanArr boolArr = new BooleanArr(bools);
        assertArrayEquals ( boolArr.getBools(), ((BooleanArr)cycle(boolArr)).getBools() );
    }
}
