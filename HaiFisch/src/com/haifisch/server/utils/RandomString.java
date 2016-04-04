package com.haifisch.server.utils;

import java.util.Random;
import java.util.stream.Stream;

/**
 * Creates a random string
 */

public class RandomString {
	
	/**
	 * Holds all the alpharithmetic values
	 */
    private static final char[] symbols;
    
    /**
     * Initially builds the symbols array
     */
    static {
        StringBuilder tmp = new StringBuilder();
        for (char ch = '0'; ch <= '9'; ++ch)
            tmp.append(ch);
        for (char ch = 'a'; ch <= 'z'; ++ch)
            tmp.append(ch);
        symbols = tmp.toString().toCharArray();
    }
    
    /**
     * Random shit
     */
    private final Random random = new Random();
    
    /**
     * A char buffer. It holds the new random String.
     */
    private final char[] buf;
    
    /**
     * Constructs the buffer array by a specific length.
     * @param length
     */
    public RandomString(int length) {
        if (length < 1)
            throw new IllegalArgumentException("length < 1: " + length);
        buf = new char[length];
    }
    
    /**
     * Returns a new random string :D
     * @return
     */
    public String nextString() {
        for (int idx = 0; idx < buf.length; ++idx)
            buf[idx] = symbols[random.nextInt(symbols.length)];
        return new String(buf);
    }
}