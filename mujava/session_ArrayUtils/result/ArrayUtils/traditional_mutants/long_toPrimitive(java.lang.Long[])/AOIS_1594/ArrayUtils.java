// This is a mutant program.
// Author : ysma

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;


public class ArrayUtils
{

    public static final java.lang.Object[] EMPTY_OBJECT_ARRAY = new java.lang.Object[0];

    public static final java.lang.Class[] EMPTY_CLASS_ARRAY = new java.lang.Class[0];

    public static final java.lang.String[] EMPTY_STRING_ARRAY = new java.lang.String[0];

    public static final long[] EMPTY_LONG_ARRAY = new long[0];

    public static final java.lang.Long[] EMPTY_LONG_OBJECT_ARRAY = new java.lang.Long[0];

    public static final int[] EMPTY_INT_ARRAY = new int[0];

    public static final java.lang.Integer[] EMPTY_INTEGER_OBJECT_ARRAY = new java.lang.Integer[0];

    public static final short[] EMPTY_SHORT_ARRAY = new short[0];

    public static final java.lang.Short[] EMPTY_SHORT_OBJECT_ARRAY = new java.lang.Short[0];

    public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

    public static final java.lang.Byte[] EMPTY_BYTE_OBJECT_ARRAY = new java.lang.Byte[0];

    public static final double[] EMPTY_DOUBLE_ARRAY = new double[0];

    public static final java.lang.Double[] EMPTY_DOUBLE_OBJECT_ARRAY = new java.lang.Double[0];

    public static final float[] EMPTY_FLOAT_ARRAY = new float[0];

    public static final java.lang.Float[] EMPTY_FLOAT_OBJECT_ARRAY = new java.lang.Float[0];

    public static final boolean[] EMPTY_BOOLEAN_ARRAY = new boolean[0];

    public static final java.lang.Boolean[] EMPTY_BOOLEAN_OBJECT_ARRAY = new java.lang.Boolean[0];

    public static final char[] EMPTY_CHAR_ARRAY = new char[0];

    public static final java.lang.Character[] EMPTY_CHARACTER_OBJECT_ARRAY = new java.lang.Character[0];

    public static final int INDEX_NOT_FOUND = -1;

    public ArrayUtils()
    {
        super();
    }

    public static  java.util.Map toMap( java.lang.Object[] array )
    {
        if (array == null) {
            return null;
        }
        final java.util.Map map = new java.util.HashMap( (int) (array.length * 1.5) );
        for (int i = 0; i < array.length; i++) {
            java.lang.Object object = array[i];
            if (object instanceof java.util.Map.Entry) {
                java.util.Map.Entry entry = (java.util.Map.Entry) object;
                map.put( entry.getKey(), entry.getValue() );
            } else {
                if (object instanceof java.lang.Object[]) {
                    java.lang.Object[] entry = (java.lang.Object[]) object;
                    if (entry.length < 2) {
                        throw new java.lang.IllegalArgumentException( "Array element " + i + ", '" + object + "', has a length less than 2" );
                    }
                    map.put( entry[0], entry[1] );
                } else {
                    throw new java.lang.IllegalArgumentException( "Array element " + i + ", '" + object + "', is neither of type Map.Entry nor an Array" );
                }
            }
        }
        return map;
    }

    public static  java.lang.Object[] clone( java.lang.Object[] array )
    {
        if (array == null) {
            return null;
        }
        return array.clone();
    }

    public static  long[] clone( long[] array )
    {
        if (array == null) {
            return null;
        }
        return array.clone();
    }

    public static  int[] clone( int[] array )
    {
        if (array == null) {
            return null;
        }
        return array.clone();
    }

    public static  short[] clone( short[] array )
    {
        if (array == null) {
            return null;
        }
        return array.clone();
    }

    public static  char[] clone( char[] array )
    {
        if (array == null) {
            return null;
        }
        return array.clone();
    }

    public static  byte[] clone( byte[] array )
    {
        if (array == null) {
            return null;
        }
        return array.clone();
    }

    public static  double[] clone( double[] array )
    {
        if (array == null) {
            return null;
        }
        return array.clone();
    }

    public static  float[] clone( float[] array )
    {
        if (array == null) {
            return null;
        }
        return array.clone();
    }

    public static  boolean[] clone( boolean[] array )
    {
        if (array == null) {
            return null;
        }
        return array.clone();
    }

    public static  java.lang.Object[] subarray( java.lang.Object[] array, int startIndexInclusive, int endIndexExclusive )
    {
        if (array == null) {
            return null;
        }
        if (startIndexInclusive < 0) {
            startIndexInclusive = 0;
        }
        if (endIndexExclusive > array.length) {
            endIndexExclusive = array.length;
        }
        int newSize = endIndexExclusive - startIndexInclusive;
        java.lang.Class type = array.getClass().getComponentType();
        if (newSize <= 0) {
            return (java.lang.Object[]) Array.newInstance( type, 0 );
        }
        java.lang.Object[] subarray = (java.lang.Object[]) Array.newInstance( type, newSize );
        System.arraycopy( array, startIndexInclusive, subarray, 0, newSize );
        return subarray;
    }

    public static  long[] subarray( long[] array, int startIndexInclusive, int endIndexExclusive )
    {
        if (array == null) {
            return null;
        }
        if (startIndexInclusive < 0) {
            startIndexInclusive = 0;
        }
        if (endIndexExclusive > array.length) {
            endIndexExclusive = array.length;
        }
        int newSize = endIndexExclusive - startIndexInclusive;
        if (newSize <= 0) {
            return EMPTY_LONG_ARRAY;
        }
        long[] subarray = new long[newSize];
        System.arraycopy( array, startIndexInclusive, subarray, 0, newSize );
        return subarray;
    }

    public static  int[] subarray( int[] array, int startIndexInclusive, int endIndexExclusive )
    {
        if (array == null) {
            return null;
        }
        if (startIndexInclusive < 0) {
            startIndexInclusive = 0;
        }
        if (endIndexExclusive > array.length) {
            endIndexExclusive = array.length;
        }
        int newSize = endIndexExclusive - startIndexInclusive;
        if (newSize <= 0) {
            return EMPTY_INT_ARRAY;
        }
        int[] subarray = new int[newSize];
        System.arraycopy( array, startIndexInclusive, subarray, 0, newSize );
        return subarray;
    }

    public static  short[] subarray( short[] array, int startIndexInclusive, int endIndexExclusive )
    {
        if (array == null) {
            return null;
        }
        if (startIndexInclusive < 0) {
            startIndexInclusive = 0;
        }
        if (endIndexExclusive > array.length) {
            endIndexExclusive = array.length;
        }
        int newSize = endIndexExclusive - startIndexInclusive;
        if (newSize <= 0) {
            return EMPTY_SHORT_ARRAY;
        }
        short[] subarray = new short[newSize];
        System.arraycopy( array, startIndexInclusive, subarray, 0, newSize );
        return subarray;
    }

    public static  char[] subarray( char[] array, int startIndexInclusive, int endIndexExclusive )
    {
        if (array == null) {
            return null;
        }
        if (startIndexInclusive < 0) {
            startIndexInclusive = 0;
        }
        if (endIndexExclusive > array.length) {
            endIndexExclusive = array.length;
        }
        int newSize = endIndexExclusive - startIndexInclusive;
        if (newSize <= 0) {
            return EMPTY_CHAR_ARRAY;
        }
        char[] subarray = new char[newSize];
        System.arraycopy( array, startIndexInclusive, subarray, 0, newSize );
        return subarray;
    }

    public static  byte[] subarray( byte[] array, int startIndexInclusive, int endIndexExclusive )
    {
        if (array == null) {
            return null;
        }
        if (startIndexInclusive < 0) {
            startIndexInclusive = 0;
        }
        if (endIndexExclusive > array.length) {
            endIndexExclusive = array.length;
        }
        int newSize = endIndexExclusive - startIndexInclusive;
        if (newSize <= 0) {
            return EMPTY_BYTE_ARRAY;
        }
        byte[] subarray = new byte[newSize];
        System.arraycopy( array, startIndexInclusive, subarray, 0, newSize );
        return subarray;
    }

    public static  double[] subarray( double[] array, int startIndexInclusive, int endIndexExclusive )
    {
        if (array == null) {
            return null;
        }
        if (startIndexInclusive < 0) {
            startIndexInclusive = 0;
        }
        if (endIndexExclusive > array.length) {
            endIndexExclusive = array.length;
        }
        int newSize = endIndexExclusive - startIndexInclusive;
        if (newSize <= 0) {
            return EMPTY_DOUBLE_ARRAY;
        }
        double[] subarray = new double[newSize];
        System.arraycopy( array, startIndexInclusive, subarray, 0, newSize );
        return subarray;
    }

    public static  float[] subarray( float[] array, int startIndexInclusive, int endIndexExclusive )
    {
        if (array == null) {
            return null;
        }
        if (startIndexInclusive < 0) {
            startIndexInclusive = 0;
        }
        if (endIndexExclusive > array.length) {
            endIndexExclusive = array.length;
        }
        int newSize = endIndexExclusive - startIndexInclusive;
        if (newSize <= 0) {
            return EMPTY_FLOAT_ARRAY;
        }
        float[] subarray = new float[newSize];
        System.arraycopy( array, startIndexInclusive, subarray, 0, newSize );
        return subarray;
    }

    public static  boolean[] subarray( boolean[] array, int startIndexInclusive, int endIndexExclusive )
    {
        if (array == null) {
            return null;
        }
        if (startIndexInclusive < 0) {
            startIndexInclusive = 0;
        }
        if (endIndexExclusive > array.length) {
            endIndexExclusive = array.length;
        }
        int newSize = endIndexExclusive - startIndexInclusive;
        if (newSize <= 0) {
            return EMPTY_BOOLEAN_ARRAY;
        }
        boolean[] subarray = new boolean[newSize];
        System.arraycopy( array, startIndexInclusive, subarray, 0, newSize );
        return subarray;
    }

    public static  boolean isSameLength( java.lang.Object[] array1, java.lang.Object[] array2 )
    {
        if (array1 == null && array2 != null && array2.length > 0 || array2 == null && array1 != null && array1.length > 0 || array1 != null && array2 != null && array1.length != array2.length) {
            return false;
        }
        return true;
    }

    public static  boolean isSameLength( long[] array1, long[] array2 )
    {
        if (array1 == null && array2 != null && array2.length > 0 || array2 == null && array1 != null && array1.length > 0 || array1 != null && array2 != null && array1.length != array2.length) {
            return false;
        }
        return true;
    }

    public static  boolean isSameLength( int[] array1, int[] array2 )
    {
        if (array1 == null && array2 != null && array2.length > 0 || array2 == null && array1 != null && array1.length > 0 || array1 != null && array2 != null && array1.length != array2.length) {
            return false;
        }
        return true;
    }

    public static  boolean isSameLength( short[] array1, short[] array2 )
    {
        if (array1 == null && array2 != null && array2.length > 0 || array2 == null && array1 != null && array1.length > 0 || array1 != null && array2 != null && array1.length != array2.length) {
            return false;
        }
        return true;
    }

    public static  boolean isSameLength( char[] array1, char[] array2 )
    {
        if (array1 == null && array2 != null && array2.length > 0 || array2 == null && array1 != null && array1.length > 0 || array1 != null && array2 != null && array1.length != array2.length) {
            return false;
        }
        return true;
    }

    public static  boolean isSameLength( byte[] array1, byte[] array2 )
    {
        if (array1 == null && array2 != null && array2.length > 0 || array2 == null && array1 != null && array1.length > 0 || array1 != null && array2 != null && array1.length != array2.length) {
            return false;
        }
        return true;
    }

    public static  boolean isSameLength( double[] array1, double[] array2 )
    {
        if (array1 == null && array2 != null && array2.length > 0 || array2 == null && array1 != null && array1.length > 0 || array1 != null && array2 != null && array1.length != array2.length) {
            return false;
        }
        return true;
    }

    public static  boolean isSameLength( float[] array1, float[] array2 )
    {
        if (array1 == null && array2 != null && array2.length > 0 || array2 == null && array1 != null && array1.length > 0 || array1 != null && array2 != null && array1.length != array2.length) {
            return false;
        }
        return true;
    }

    public static  boolean isSameLength( boolean[] array1, boolean[] array2 )
    {
        if (array1 == null && array2 != null && array2.length > 0 || array2 == null && array1 != null && array1.length > 0 || array1 != null && array2 != null && array1.length != array2.length) {
            return false;
        }
        return true;
    }

    public static  int getLength( java.lang.Object array )
    {
        if (array == null) {
            return 0;
        }
        return Array.getLength( array );
    }

    public static  boolean isSameType( java.lang.Object array1, java.lang.Object array2 )
    {
        if (array1 == null || array2 == null) {
            throw new java.lang.IllegalArgumentException( "The Array must not be null" );
        }
        return array1.getClass().getName().equals( array2.getClass().getName() );
    }

    public static  java.lang.Object[] reverse( java.lang.Object[] array )
    {
        if (array == null) {
            return array;
        }
        int i = 0;
        int j = array.length - 1;
        java.lang.Object tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
        return array;
    }

    public static  long[] reverse( long[] array )
    {
        if (array == null) {
            return array;
        }
        int i = 0;
        int j = array.length - 1;
        long tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
        return array;
    }

    public static  int[] reverse( int[] array )
    {
        if (array == null) {
            return array;
        }
        int i = 0;
        int j = array.length - 1;
        int tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
        return array;
    }

    public static  short[] reverse( short[] array )
    {
        if (array == null) {
            return array;
        }
        int i = 0;
        int j = array.length - 1;
        short tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
        return array;
    }

    public static  char[] reverse( char[] array )
    {
        if (array == null) {
            return array;
        }
        int i = 0;
        int j = array.length - 1;
        char tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
        return array;
    }

    public static  byte[] reverse( byte[] array )
    {
        if (array == null) {
            return array;
        }
        int i = 0;
        int j = array.length - 1;
        byte tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
        return array;
    }

    public static  double[] reverse( double[] array )
    {
        if (array == null) {
            return array;
        }
        int i = 0;
        int j = array.length - 1;
        double tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
        return array;
    }

    public static  float[] reverse( float[] array )
    {
        if (array == null) {
            return array;
        }
        int i = 0;
        int j = array.length - 1;
        float tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
        return array;
    }

    public static  boolean[] reverse( boolean[] array )
    {
        if (array == null) {
            return array;
        }
        int i = 0;
        int j = array.length - 1;
        boolean tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
        return array;
    }

    public static  int indexOf( java.lang.Object[] array, java.lang.Object objectToFind )
    {
        return indexOf( array, objectToFind, 0 );
    }

    public static  int indexOf( java.lang.Object[] array, java.lang.Object objectToFind, int startIndex )
    {
        if (array == null) {
            return INDEX_NOT_FOUND;
        }
        if (startIndex < 0) {
            startIndex = 0;
        }
        if (objectToFind == null) {
            for (int i = startIndex; i < array.length; i++) {
                if (array[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = startIndex; i < array.length; i++) {
                if (objectToFind.equals( array[i] )) {
                    return i;
                }
            }
        }
        return INDEX_NOT_FOUND;
    }

    public static  int lastIndexOf( java.lang.Object[] array, java.lang.Object objectToFind )
    {
        return lastIndexOf( array, objectToFind, Integer.MAX_VALUE );
    }

    public static  int lastIndexOf( java.lang.Object[] array, java.lang.Object objectToFind, int startIndex )
    {
        if (array == null) {
            return INDEX_NOT_FOUND;
        }
        if (startIndex < 0) {
            return INDEX_NOT_FOUND;
        } else {
            if (startIndex >= array.length) {
                startIndex = array.length - 1;
            }
        }
        if (objectToFind == null) {
            for (int i = startIndex; i >= 0; i--) {
                if (array[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = startIndex; i >= 0; i--) {
                if (objectToFind.equals( array[i] )) {
                    return i;
                }
            }
        }
        return INDEX_NOT_FOUND;
    }

    public static  boolean contains( java.lang.Object[] array, java.lang.Object objectToFind )
    {
        return indexOf( array, objectToFind ) != INDEX_NOT_FOUND;
    }

    public static  int indexOf( long[] array, long valueToFind )
    {
        return indexOf( array, valueToFind, 0 );
    }

    public static  int indexOf( long[] array, long valueToFind, int startIndex )
    {
        if (array == null) {
            return INDEX_NOT_FOUND;
        }
        if (startIndex < 0) {
            startIndex = 0;
        }
        for (int i = startIndex; i < array.length; i++) {
            if (valueToFind == array[i]) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    public static  int lastIndexOf( long[] array, long valueToFind )
    {
        return lastIndexOf( array, valueToFind, Integer.MAX_VALUE );
    }

    public static  int lastIndexOf( long[] array, long valueToFind, int startIndex )
    {
        if (array == null) {
            return INDEX_NOT_FOUND;
        }
        if (startIndex < 0) {
            return INDEX_NOT_FOUND;
        } else {
            if (startIndex >= array.length) {
                startIndex = array.length - 1;
            }
        }
        for (int i = startIndex; i >= 0; i--) {
            if (valueToFind == array[i]) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    public static  boolean contains( long[] array, long valueToFind )
    {
        return indexOf( array, valueToFind ) != INDEX_NOT_FOUND;
    }

    public static  int indexOf( int[] array, int valueToFind )
    {
        return indexOf( array, valueToFind, 0 );
    }

    public static  int indexOf( int[] array, int valueToFind, int startIndex )
    {
        if (array == null) {
            return INDEX_NOT_FOUND;
        }
        if (startIndex < 0) {
            startIndex = 0;
        }
        for (int i = startIndex; i < array.length; i++) {
            if (valueToFind == array[i]) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    public static  int lastIndexOf( int[] array, int valueToFind )
    {
        return lastIndexOf( array, valueToFind, Integer.MAX_VALUE );
    }

    public static  int lastIndexOf( int[] array, int valueToFind, int startIndex )
    {
        if (array == null) {
            return INDEX_NOT_FOUND;
        }
        if (startIndex < 0) {
            return INDEX_NOT_FOUND;
        } else {
            if (startIndex >= array.length) {
                startIndex = array.length - 1;
            }
        }
        for (int i = startIndex; i >= 0; i--) {
            if (valueToFind == array[i]) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    public static  boolean contains( int[] array, int valueToFind )
    {
        return indexOf( array, valueToFind ) != INDEX_NOT_FOUND;
    }

    public static  int indexOf( short[] array, short valueToFind )
    {
        return indexOf( array, valueToFind, 0 );
    }

    public static  int indexOf( short[] array, short valueToFind, int startIndex )
    {
        if (array == null) {
            return INDEX_NOT_FOUND;
        }
        if (startIndex < 0) {
            startIndex = 0;
        }
        for (int i = startIndex; i < array.length; i++) {
            if (valueToFind == array[i]) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    public static  int lastIndexOf( short[] array, short valueToFind )
    {
        return lastIndexOf( array, valueToFind, Integer.MAX_VALUE );
    }

    public static  int lastIndexOf( short[] array, short valueToFind, int startIndex )
    {
        if (array == null) {
            return INDEX_NOT_FOUND;
        }
        if (startIndex < 0) {
            return INDEX_NOT_FOUND;
        } else {
            if (startIndex >= array.length) {
                startIndex = array.length - 1;
            }
        }
        for (int i = startIndex; i >= 0; i--) {
            if (valueToFind == array[i]) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    public static  boolean contains( short[] array, short valueToFind )
    {
        return indexOf( array, valueToFind ) != INDEX_NOT_FOUND;
    }

    public static  int indexOf( char[] array, char valueToFind )
    {
        return indexOf( array, valueToFind, 0 );
    }

    public static  int indexOf( char[] array, char valueToFind, int startIndex )
    {
        if (array == null) {
            return INDEX_NOT_FOUND;
        }
        if (startIndex < 0) {
            startIndex = 0;
        }
        for (int i = startIndex; i < array.length; i++) {
            if (valueToFind == array[i]) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    public static  int lastIndexOf( char[] array, char valueToFind )
    {
        return lastIndexOf( array, valueToFind, Integer.MAX_VALUE );
    }

    public static  int lastIndexOf( char[] array, char valueToFind, int startIndex )
    {
        if (array == null) {
            return INDEX_NOT_FOUND;
        }
        if (startIndex < 0) {
            return INDEX_NOT_FOUND;
        } else {
            if (startIndex >= array.length) {
                startIndex = array.length - 1;
            }
        }
        for (int i = startIndex; i >= 0; i--) {
            if (valueToFind == array[i]) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    public static  boolean contains( char[] array, char valueToFind )
    {
        return indexOf( array, valueToFind ) != INDEX_NOT_FOUND;
    }

    public static  int indexOf( byte[] array, byte valueToFind )
    {
        return indexOf( array, valueToFind, 0 );
    }

    public static  int indexOf( byte[] array, byte valueToFind, int startIndex )
    {
        if (array == null) {
            return INDEX_NOT_FOUND;
        }
        if (startIndex < 0) {
            startIndex = 0;
        }
        for (int i = startIndex; i < array.length; i++) {
            if (valueToFind == array[i]) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    public static  int lastIndexOf( byte[] array, byte valueToFind )
    {
        return lastIndexOf( array, valueToFind, Integer.MAX_VALUE );
    }

    public static  int lastIndexOf( byte[] array, byte valueToFind, int startIndex )
    {
        if (array == null) {
            return INDEX_NOT_FOUND;
        }
        if (startIndex < 0) {
            return INDEX_NOT_FOUND;
        } else {
            if (startIndex >= array.length) {
                startIndex = array.length - 1;
            }
        }
        for (int i = startIndex; i >= 0; i--) {
            if (valueToFind == array[i]) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    public static  boolean contains( byte[] array, byte valueToFind )
    {
        return indexOf( array, valueToFind ) != INDEX_NOT_FOUND;
    }

    public static  int indexOf( double[] array, double valueToFind )
    {
        return indexOf( array, valueToFind, 0 );
    }

    public static  int indexOf( double[] array, double valueToFind, double tolerance )
    {
        return indexOf( array, valueToFind, 0, tolerance );
    }

    public static  int indexOf( double[] array, double valueToFind, int startIndex )
    {
        if (ArrayUtils.isEmpty( array )) {
            return INDEX_NOT_FOUND;
        }
        if (startIndex < 0) {
            startIndex = 0;
        }
        for (int i = startIndex; i < array.length; i++) {
            if (valueToFind == array[i]) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    public static  int indexOf( double[] array, double valueToFind, int startIndex, double tolerance )
    {
        if (ArrayUtils.isEmpty( array )) {
            return INDEX_NOT_FOUND;
        }
        if (startIndex < 0) {
            startIndex = 0;
        }
        double min = valueToFind - tolerance;
        double max = valueToFind + tolerance;
        for (int i = startIndex; i < array.length; i++) {
            if (array[i] >= min && array[i] <= max) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    public static  int lastIndexOf( double[] array, double valueToFind )
    {
        return lastIndexOf( array, valueToFind, Integer.MAX_VALUE );
    }

    public static  int lastIndexOf( double[] array, double valueToFind, double tolerance )
    {
        return lastIndexOf( array, valueToFind, Integer.MAX_VALUE, tolerance );
    }

    public static  int lastIndexOf( double[] array, double valueToFind, int startIndex )
    {
        if (ArrayUtils.isEmpty( array )) {
            return INDEX_NOT_FOUND;
        }
        if (startIndex < 0) {
            return INDEX_NOT_FOUND;
        } else {
            if (startIndex >= array.length) {
                startIndex = array.length - 1;
            }
        }
        for (int i = startIndex; i >= 0; i--) {
            if (valueToFind == array[i]) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    public static  int lastIndexOf( double[] array, double valueToFind, int startIndex, double tolerance )
    {
        if (ArrayUtils.isEmpty( array )) {
            return INDEX_NOT_FOUND;
        }
        if (startIndex < 0) {
            return INDEX_NOT_FOUND;
        } else {
            if (startIndex >= array.length) {
                startIndex = array.length - 1;
            }
        }
        double min = valueToFind - tolerance;
        double max = valueToFind + tolerance;
        for (int i = startIndex; i >= 0; i--) {
            if (array[i] >= min && array[i] <= max) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    public static  boolean contains( double[] array, double valueToFind )
    {
        return indexOf( array, valueToFind ) != INDEX_NOT_FOUND;
    }

    public static  boolean contains( double[] array, double valueToFind, double tolerance )
    {
        return indexOf( array, valueToFind, 0, tolerance ) != INDEX_NOT_FOUND;
    }

    public static  int indexOf( float[] array, float valueToFind )
    {
        return indexOf( array, valueToFind, 0 );
    }

    public static  int indexOf( float[] array, float valueToFind, int startIndex )
    {
        if (ArrayUtils.isEmpty( array )) {
            return INDEX_NOT_FOUND;
        }
        if (startIndex < 0) {
            startIndex = 0;
        }
        for (int i = startIndex; i < array.length; i++) {
            if (valueToFind == array[i]) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    public static  int lastIndexOf( float[] array, float valueToFind )
    {
        return lastIndexOf( array, valueToFind, Integer.MAX_VALUE );
    }

    public static  int lastIndexOf( float[] array, float valueToFind, int startIndex )
    {
        if (ArrayUtils.isEmpty( array )) {
            return INDEX_NOT_FOUND;
        }
        if (startIndex < 0) {
            return INDEX_NOT_FOUND;
        } else {
            if (startIndex >= array.length) {
                startIndex = array.length - 1;
            }
        }
        for (int i = startIndex; i >= 0; i--) {
            if (valueToFind == array[i]) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    public static  boolean contains( float[] array, float valueToFind )
    {
        return indexOf( array, valueToFind ) != INDEX_NOT_FOUND;
    }

    public static  int indexOf( boolean[] array, boolean valueToFind )
    {
        return indexOf( array, valueToFind, 0 );
    }

    public static  int indexOf( boolean[] array, boolean valueToFind, int startIndex )
    {
        if (ArrayUtils.isEmpty( array )) {
            return INDEX_NOT_FOUND;
        }
        if (startIndex < 0) {
            startIndex = 0;
        }
        for (int i = startIndex; i < array.length; i++) {
            if (valueToFind == array[i]) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    public static  int lastIndexOf( boolean[] array, boolean valueToFind )
    {
        return lastIndexOf( array, valueToFind, Integer.MAX_VALUE );
    }

    public static  int lastIndexOf( boolean[] array, boolean valueToFind, int startIndex )
    {
        if (ArrayUtils.isEmpty( array )) {
            return INDEX_NOT_FOUND;
        }
        if (startIndex < 0) {
            return INDEX_NOT_FOUND;
        } else {
            if (startIndex >= array.length) {
                startIndex = array.length - 1;
            }
        }
        for (int i = startIndex; i >= 0; i--) {
            if (valueToFind == array[i]) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    public static  boolean contains( boolean[] array, boolean valueToFind )
    {
        return indexOf( array, valueToFind ) != INDEX_NOT_FOUND;
    }

    public static  char[] toPrimitive( java.lang.Character[] array )
    {
        if (array == null) {
            return null;
        } else {
            if (array.length == 0) {
                return EMPTY_CHAR_ARRAY;
            }
        }
        final char[] result = new char[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i].charValue();
        }
        return result;
    }

    public static  char[] toPrimitive( java.lang.Character[] array, char valueForNull )
    {
        if (array == null) {
            return null;
        } else {
            if (array.length == 0) {
                return EMPTY_CHAR_ARRAY;
            }
        }
        final char[] result = new char[array.length];
        for (int i = 0; i < array.length; i++) {
            java.lang.Character b = array[i];
            result[i] = b == null ? valueForNull : b.charValue();
        }
        return result;
    }

    public static  java.lang.Character[] toObject( char[] array )
    {
        if (array == null) {
            return null;
        } else {
            if (array.length == 0) {
                return EMPTY_CHARACTER_OBJECT_ARRAY;
            }
        }
        final java.lang.Character[] result = new java.lang.Character[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = new java.lang.Character( array[i] );
        }
        return result;
    }

    public static  long[] toPrimitive( java.lang.Long[] array )
    {
        if (array == null) {
            return null;
        } else {
            if (array.length == 0) {
                return EMPTY_LONG_ARRAY;
            }
        }
        final long[] result = new long[array.length];
        for (int i = 0; --i < array.length; i++) {
            result[i] = array[i].longValue();
        }
        return result;
    }

    public static  long[] toPrimitive( java.lang.Long[] array, long valueForNull )
    {
        if (array == null) {
            return null;
        } else {
            if (array.length == 0) {
                return EMPTY_LONG_ARRAY;
            }
        }
        final long[] result = new long[array.length];
        for (int i = 0; i < array.length; i++) {
            java.lang.Long b = array[i];
            result[i] = b == null ? valueForNull : b.longValue();
        }
        return result;
    }

    public static  java.lang.Long[] toObject( long[] array )
    {
        if (array == null) {
            return null;
        } else {
            if (array.length == 0) {
                return EMPTY_LONG_OBJECT_ARRAY;
            }
        }
        final java.lang.Long[] result = new java.lang.Long[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = new java.lang.Long( array[i] );
        }
        return result;
    }

    public static  int[] toPrimitive( java.lang.Integer[] array )
    {
        if (array == null) {
            return null;
        } else {
            if (array.length == 0) {
                return EMPTY_INT_ARRAY;
            }
        }
        final int[] result = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i].intValue();
        }
        return result;
    }

    public static  int[] toPrimitive( java.lang.Integer[] array, int valueForNull )
    {
        if (array == null) {
            return null;
        } else {
            if (array.length == 0) {
                return EMPTY_INT_ARRAY;
            }
        }
        final int[] result = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            java.lang.Integer b = array[i];
            result[i] = b == null ? valueForNull : b.intValue();
        }
        return result;
    }

    public static  java.lang.Integer[] toObject( int[] array )
    {
        if (array == null) {
            return null;
        } else {
            if (array.length == 0) {
                return EMPTY_INTEGER_OBJECT_ARRAY;
            }
        }
        final java.lang.Integer[] result = new java.lang.Integer[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = new java.lang.Integer( array[i] );
        }
        return result;
    }

    public static  short[] toPrimitive( java.lang.Short[] array )
    {
        if (array == null) {
            return null;
        } else {
            if (array.length == 0) {
                return EMPTY_SHORT_ARRAY;
            }
        }
        final short[] result = new short[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i].shortValue();
        }
        return result;
    }

    public static  short[] toPrimitive( java.lang.Short[] array, short valueForNull )
    {
        if (array == null) {
            return null;
        } else {
            if (array.length == 0) {
                return EMPTY_SHORT_ARRAY;
            }
        }
        final short[] result = new short[array.length];
        for (int i = 0; i < array.length; i++) {
            java.lang.Short b = array[i];
            result[i] = b == null ? valueForNull : b.shortValue();
        }
        return result;
    }

    public static  java.lang.Short[] toObject( short[] array )
    {
        if (array == null) {
            return null;
        } else {
            if (array.length == 0) {
                return EMPTY_SHORT_OBJECT_ARRAY;
            }
        }
        final java.lang.Short[] result = new java.lang.Short[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = new java.lang.Short( array[i] );
        }
        return result;
    }

    public static  byte[] toPrimitive( java.lang.Byte[] array )
    {
        if (array == null) {
            return null;
        } else {
            if (array.length == 0) {
                return EMPTY_BYTE_ARRAY;
            }
        }
        final byte[] result = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i].byteValue();
        }
        return result;
    }

    public static  byte[] toPrimitive( java.lang.Byte[] array, byte valueForNull )
    {
        if (array == null) {
            return null;
        } else {
            if (array.length == 0) {
                return EMPTY_BYTE_ARRAY;
            }
        }
        final byte[] result = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            java.lang.Byte b = array[i];
            result[i] = b == null ? valueForNull : b.byteValue();
        }
        return result;
    }

    public static  java.lang.Byte[] toObject( byte[] array )
    {
        if (array == null) {
            return null;
        } else {
            if (array.length == 0) {
                return EMPTY_BYTE_OBJECT_ARRAY;
            }
        }
        final java.lang.Byte[] result = new java.lang.Byte[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = Byte.valueOf( array[i] );
        }
        return result;
    }

    public static  double[] toPrimitive( java.lang.Double[] array )
    {
        if (array == null) {
            return null;
        } else {
            if (array.length == 0) {
                return EMPTY_DOUBLE_ARRAY;
            }
        }
        final double[] result = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i].doubleValue();
        }
        return result;
    }

    public static  double[] toPrimitive( java.lang.Double[] array, double valueForNull )
    {
        if (array == null) {
            return null;
        } else {
            if (array.length == 0) {
                return EMPTY_DOUBLE_ARRAY;
            }
        }
        final double[] result = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            java.lang.Double b = array[i];
            result[i] = b == null ? valueForNull : b.doubleValue();
        }
        return result;
    }

    public static  java.lang.Double[] toObject( double[] array )
    {
        if (array == null) {
            return null;
        } else {
            if (array.length == 0) {
                return EMPTY_DOUBLE_OBJECT_ARRAY;
            }
        }
        final java.lang.Double[] result = new java.lang.Double[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = new java.lang.Double( array[i] );
        }
        return result;
    }

    public static  float[] toPrimitive( java.lang.Float[] array )
    {
        if (array == null) {
            return null;
        } else {
            if (array.length == 0) {
                return EMPTY_FLOAT_ARRAY;
            }
        }
        final float[] result = new float[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i].floatValue();
        }
        return result;
    }

    public static  float[] toPrimitive( java.lang.Float[] array, float valueForNull )
    {
        if (array == null) {
            return null;
        } else {
            if (array.length == 0) {
                return EMPTY_FLOAT_ARRAY;
            }
        }
        final float[] result = new float[array.length];
        for (int i = 0; i < array.length; i++) {
            java.lang.Float b = array[i];
            result[i] = b == null ? valueForNull : b.floatValue();
        }
        return result;
    }

    public static  java.lang.Float[] toObject( float[] array )
    {
        if (array == null) {
            return null;
        } else {
            if (array.length == 0) {
                return EMPTY_FLOAT_OBJECT_ARRAY;
            }
        }
        final java.lang.Float[] result = new java.lang.Float[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = new java.lang.Float( array[i] );
        }
        return result;
    }

    public static  boolean[] toPrimitive( java.lang.Boolean[] array )
    {
        if (array == null) {
            return null;
        } else {
            if (array.length == 0) {
                return EMPTY_BOOLEAN_ARRAY;
            }
        }
        final boolean[] result = new boolean[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i].booleanValue();
        }
        return result;
    }

    public static  boolean[] toPrimitive( java.lang.Boolean[] array, boolean valueForNull )
    {
        if (array == null) {
            return null;
        } else {
            if (array.length == 0) {
                return EMPTY_BOOLEAN_ARRAY;
            }
        }
        final boolean[] result = new boolean[array.length];
        for (int i = 0; i < array.length; i++) {
            java.lang.Boolean b = array[i];
            result[i] = b == null ? valueForNull : b.booleanValue();
        }
        return result;
    }

    public static  java.lang.Boolean[] toObject( boolean[] array )
    {
        if (array == null) {
            return null;
        } else {
            if (array.length == 0) {
                return EMPTY_BOOLEAN_OBJECT_ARRAY;
            }
        }
        final java.lang.Boolean[] result = new java.lang.Boolean[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i] ? Boolean.TRUE : Boolean.FALSE;
        }
        return result;
    }

    public static  boolean isEmpty( java.lang.Object[] array )
    {
        if (array == null || array.length == 0) {
            return true;
        }
        return false;
    }

    public static  boolean isEmpty( long[] array )
    {
        if (array == null || array.length == 0) {
            return true;
        }
        return false;
    }

    public static  boolean isEmpty( int[] array )
    {
        if (array == null || array.length == 0) {
            return true;
        }
        return false;
    }

    public static  boolean isEmpty( short[] array )
    {
        if (array == null || array.length == 0) {
            return true;
        }
        return false;
    }

    public static  boolean isEmpty( char[] array )
    {
        if (array == null || array.length == 0) {
            return true;
        }
        return false;
    }

    public static  boolean isEmpty( byte[] array )
    {
        if (array == null || array.length == 0) {
            return true;
        }
        return false;
    }

    public static  boolean isEmpty( double[] array )
    {
        if (array == null || array.length == 0) {
            return true;
        }
        return false;
    }

    public static  boolean isEmpty( float[] array )
    {
        if (array == null || array.length == 0) {
            return true;
        }
        return false;
    }

    public static  boolean isEmpty( boolean[] array )
    {
        if (array == null || array.length == 0) {
            return true;
        }
        return false;
    }

    public static  java.lang.Object[] addAll( java.lang.Object[] array1, java.lang.Object[] array2 )
    {
        if (array1 == null) {
            return clone( array2 );
        } else {
            if (array2 == null) {
                return clone( array1 );
            }
        }
        java.lang.Object[] joinedArray = (java.lang.Object[]) Array.newInstance( array1.getClass().getComponentType(), array1.length + array2.length );
        System.arraycopy( array1, 0, joinedArray, 0, array1.length );
        System.arraycopy( array2, 0, joinedArray, array1.length, array2.length );
        return joinedArray;
    }

    public static  boolean[] addAll( boolean[] array1, boolean[] array2 )
    {
        if (array1 == null) {
            return clone( array2 );
        } else {
            if (array2 == null) {
                return clone( array1 );
            }
        }
        boolean[] joinedArray = new boolean[array1.length + array2.length];
        System.arraycopy( array1, 0, joinedArray, 0, array1.length );
        System.arraycopy( array2, 0, joinedArray, array1.length, array2.length );
        return joinedArray;
    }

    public static  char[] addAll( char[] array1, char[] array2 )
    {
        if (array1 == null) {
            return clone( array2 );
        } else {
            if (array2 == null) {
                return clone( array1 );
            }
        }
        char[] joinedArray = new char[array1.length + array2.length];
        System.arraycopy( array1, 0, joinedArray, 0, array1.length );
        System.arraycopy( array2, 0, joinedArray, array1.length, array2.length );
        return joinedArray;
    }

    public static  byte[] addAll( byte[] array1, byte[] array2 )
    {
        if (array1 == null) {
            return clone( array2 );
        } else {
            if (array2 == null) {
                return clone( array1 );
            }
        }
        byte[] joinedArray = new byte[array1.length + array2.length];
        System.arraycopy( array1, 0, joinedArray, 0, array1.length );
        System.arraycopy( array2, 0, joinedArray, array1.length, array2.length );
        return joinedArray;
    }

    public static  short[] addAll( short[] array1, short[] array2 )
    {
        if (array1 == null) {
            return clone( array2 );
        } else {
            if (array2 == null) {
                return clone( array1 );
            }
        }
        short[] joinedArray = new short[array1.length + array2.length];
        System.arraycopy( array1, 0, joinedArray, 0, array1.length );
        System.arraycopy( array2, 0, joinedArray, array1.length, array2.length );
        return joinedArray;
    }

    public static  int[] addAll( int[] array1, int[] array2 )
    {
        if (array1 == null) {
            return clone( array2 );
        } else {
            if (array2 == null) {
                return clone( array1 );
            }
        }
        int[] joinedArray = new int[array1.length + array2.length];
        System.arraycopy( array1, 0, joinedArray, 0, array1.length );
        System.arraycopy( array2, 0, joinedArray, array1.length, array2.length );
        return joinedArray;
    }

    public static  long[] addAll( long[] array1, long[] array2 )
    {
        if (array1 == null) {
            return clone( array2 );
        } else {
            if (array2 == null) {
                return clone( array1 );
            }
        }
        long[] joinedArray = new long[array1.length + array2.length];
        System.arraycopy( array1, 0, joinedArray, 0, array1.length );
        System.arraycopy( array2, 0, joinedArray, array1.length, array2.length );
        return joinedArray;
    }

    public static  float[] addAll( float[] array1, float[] array2 )
    {
        if (array1 == null) {
            return clone( array2 );
        } else {
            if (array2 == null) {
                return clone( array1 );
            }
        }
        float[] joinedArray = new float[array1.length + array2.length];
        System.arraycopy( array1, 0, joinedArray, 0, array1.length );
        System.arraycopy( array2, 0, joinedArray, array1.length, array2.length );
        return joinedArray;
    }

    public static  double[] addAll( double[] array1, double[] array2 )
    {
        if (array1 == null) {
            return clone( array2 );
        } else {
            if (array2 == null) {
                return clone( array1 );
            }
        }
        double[] joinedArray = new double[array1.length + array2.length];
        System.arraycopy( array1, 0, joinedArray, 0, array1.length );
        System.arraycopy( array2, 0, joinedArray, array1.length, array2.length );
        return joinedArray;
    }

    public static  java.lang.Object[] add( java.lang.Object[] array, java.lang.Object element )
    {
        java.lang.Class type = array != null ? array.getClass() : element != null ? element.getClass() : java.lang.Object.class;
        java.lang.Object[] newArray = (java.lang.Object[]) copyArrayGrow1( array, type );
        newArray[newArray.length - 1] = element;
        return newArray;
    }

    public static  boolean[] add( boolean[] array, boolean element )
    {
        boolean[] newArray = (boolean[]) copyArrayGrow1( array, Boolean.TYPE );
        newArray[newArray.length - 1] = element;
        return newArray;
    }

    public static  byte[] add( byte[] array, byte element )
    {
        byte[] newArray = (byte[]) copyArrayGrow1( array, Byte.TYPE );
        newArray[newArray.length - 1] = element;
        return newArray;
    }

    public static  char[] add( char[] array, char element )
    {
        char[] newArray = (char[]) copyArrayGrow1( array, Character.TYPE );
        newArray[newArray.length - 1] = element;
        return newArray;
    }

    public static  double[] add( double[] array, double element )
    {
        double[] newArray = (double[]) copyArrayGrow1( array, Double.TYPE );
        newArray[newArray.length - 1] = element;
        return newArray;
    }

    public static  float[] add( float[] array, float element )
    {
        float[] newArray = (float[]) copyArrayGrow1( array, Float.TYPE );
        newArray[newArray.length - 1] = element;
        return newArray;
    }

    public static  int[] add( int[] array, int element )
    {
        int[] newArray = (int[]) copyArrayGrow1( array, Integer.TYPE );
        newArray[newArray.length - 1] = element;
        return newArray;
    }

    public static  long[] add( long[] array, long element )
    {
        long[] newArray = (long[]) copyArrayGrow1( array, Long.TYPE );
        newArray[newArray.length - 1] = element;
        return newArray;
    }

    public static  short[] add( short[] array, short element )
    {
        short[] newArray = (short[]) copyArrayGrow1( array, Short.TYPE );
        newArray[newArray.length - 1] = element;
        return newArray;
    }

    private static  java.lang.Object copyArrayGrow1( java.lang.Object array, java.lang.Class newArrayComponentType )
    {
        if (array != null) {
            int arrayLength = Array.getLength( array );
            java.lang.Object newArray = Array.newInstance( array.getClass().getComponentType(), arrayLength + 1 );
            System.arraycopy( array, 0, newArray, 0, arrayLength );
            return newArray;
        }
        return Array.newInstance( newArrayComponentType, 1 );
    }

    public static  java.lang.Object[] remove( java.lang.Object[] array, int index )
    {
        return (java.lang.Object[]) remove( (java.lang.Object) array, index );
    }

    public static  java.lang.Object[] removeElement( java.lang.Object[] array, java.lang.Object element )
    {
        int index = indexOf( array, element );
        if (index == INDEX_NOT_FOUND) {
            return clone( array );
        }
        return remove( array, index );
    }

    public static  boolean[] remove( boolean[] array, int index )
    {
        return (boolean[]) remove( (java.lang.Object) array, index );
    }

    public static  boolean[] removeElement( boolean[] array, boolean element )
    {
        int index = indexOf( array, element );
        if (index == INDEX_NOT_FOUND) {
            return clone( array );
        }
        return remove( array, index );
    }

    public static  byte[] remove( byte[] array, int index )
    {
        return (byte[]) remove( (java.lang.Object) array, index );
    }

    public static  byte[] removeElement( byte[] array, byte element )
    {
        int index = indexOf( array, element );
        if (index == INDEX_NOT_FOUND) {
            return clone( array );
        }
        return remove( array, index );
    }

    public static  char[] remove( char[] array, int index )
    {
        return (char[]) remove( (java.lang.Object) array, index );
    }

    public static  char[] removeElement( char[] array, char element )
    {
        int index = indexOf( array, element );
        if (index == INDEX_NOT_FOUND) {
            return clone( array );
        }
        return remove( array, index );
    }

    public static  double[] remove( double[] array, int index )
    {
        return (double[]) remove( (java.lang.Object) array, index );
    }

    public static  double[] removeElement( double[] array, double element )
    {
        int index = indexOf( array, element );
        if (index == INDEX_NOT_FOUND) {
            return clone( array );
        }
        return remove( array, index );
    }

    public static  float[] remove( float[] array, int index )
    {
        return (float[]) remove( (java.lang.Object) array, index );
    }

    public static  float[] removeElement( float[] array, float element )
    {
        int index = indexOf( array, element );
        if (index == INDEX_NOT_FOUND) {
            return clone( array );
        }
        return remove( array, index );
    }

    public static  int[] remove( int[] array, int index )
    {
        return (int[]) remove( (java.lang.Object) array, index );
    }

    public static  int[] removeElement( int[] array, int element )
    {
        int index = indexOf( array, element );
        if (index == INDEX_NOT_FOUND) {
            return clone( array );
        }
        return remove( array, index );
    }

    public static  long[] remove( long[] array, int index )
    {
        return (long[]) remove( (java.lang.Object) array, index );
    }

    public static  long[] removeElement( long[] array, long element )
    {
        int index = indexOf( array, element );
        if (index == INDEX_NOT_FOUND) {
            return clone( array );
        }
        return remove( array, index );
    }

    public static  short[] remove( short[] array, int index )
    {
        return (short[]) remove( (java.lang.Object) array, index );
    }

    public static  short[] removeElement( short[] array, short element )
    {
        int index = indexOf( array, element );
        if (index == INDEX_NOT_FOUND) {
            return clone( array );
        }
        return remove( array, index );
    }

    private static  java.lang.Object remove( java.lang.Object array, int index )
    {
        int length = getLength( array );
        if (index < 0 || index >= length) {
            throw new java.lang.IndexOutOfBoundsException( "Index: " + index + ", Length: " + length );
        }
        java.lang.Object result = Array.newInstance( array.getClass().getComponentType(), length - 1 );
        System.arraycopy( array, 0, result, 0, index );
        if (index < length - 1) {
            System.arraycopy( array, index + 1, result, index, length - index - 1 );
        }
        return result;
    }

}
