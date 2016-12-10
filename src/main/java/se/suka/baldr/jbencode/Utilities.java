/*
 * The MIT License
 *
 * Copyright 2016 Graham Fairweather.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package se.suka.baldr.jbencode;

import static com.google.common.base.CharMatcher.anyOf;
import static com.google.common.io.Files.toByteArray;
import java.io.File;
import java.io.IOException;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.nio.charset.StandardCharsets.US_ASCII;
import static java.text.MessageFormat.format;
import java.util.Optional;
import java.util.Random;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Graham Fairweather
 */
class Utilities {

    private static final Logger LOGGER = getLogger(Utilities.class);
    private static final Random RAND = new Random();

    /**
     * Returns a random integer between min (included) and max (excluded)
     *
     * @param min The lower bound, inclusive
     * @param max The upper bound, exclusive
     * @return Returns the random integer
     */
    static final int randInt(final int min, final int max) {
        return RAND.nextInt(max - min) + min;
    }

    /**
     * Returns a random integer between min (included) and max (included)
     *
     * @param min The lower bound, inclusive
     * @param max The upper bound, inclusive
     * @return Returns the random integer
     */
    static final int randIntClosed(final int min, final int max) {
        return randInt(min, max + 1);
    }

    /**
     * Clamps an integer within the inclusive lower and upper bounds.
     *
     * @param value The integer to clamp
     * @param min The lower bound, inclusive
     * @param max The upper bound, inclusive
     * @return Returns the clamped integer
     */
    static final int clamp(final int value, final int min, final int max) {
        return min(max(value, min), max);
    }

    /**
     * Find absence of character in string. Searches the string for the first
     * character that does not match any of the characters specified in its
     * arguments.
     *
     * When startIndex is specified, the search only includes characters at or
     * after position startIndex, ignoring any possible occurrences before that
     * character.
     *
     * @param sequence The character sequence to examine
     * @param matcherSequence Another sequence with the set of characters to be
     * used in the search
     * @param startIndex The first index to examine; must be nonnegative and no
     * greater than {@code sequence.length()}
     * @throws NullPointerException if string or chars is {@code null}
     * @throws IndexOutOfBoundsException if startIndex is negative or greater
     * than {@code string.length()}
     * @return The position of the first character that does not match. If no
     * such characters are found, the function returns -1
     */
    static final int findFirstNotOf(final String sequence, final String matcherSequence, final int startIndex) {
        return anyOf(matcherSequence).negate().indexIn(sequence, startIndex);
    }

    /**
     * Find absence of character in string. Searches the string for the first
     * character that does not match any of the characters specified in its
     * arguments.
     *
     * When startIndex is specified, the search only includes characters at or
     * after position startIndex, ignoring any possible occurrences before that
     * character.
     *
     * @param sequence The character sequence to examine
     * @param matcherSequence Another sequence with the set of characters to be
     * used in the search
     * @throws NullPointerException if string or chars is {@code null}
     * @throws IndexOutOfBoundsException if startIndex is negative or greater
     * than {@code string.length()}
     * @return The position of the first character that does not match. If no
     * such characters are found, the function returns -1
     */
    static final int findFirstNotOf(final String sequence, final String matcherSequence) {
        return findFirstNotOf(sequence, matcherSequence, 0);
    }

    /**
     * For making the warning parameters.
     *
     * @param ex The {@link Throwable}
     * @throws NullPointerException If ex is {@code null}
     * @return An {@code Object} array of strings
     */
    private static Object[] makeParam(final Throwable ex) {
        return new Object[]{
            ex.getClass().getName(),
            ex.getMessage()
        };
    }

    static final String asciiBytesToString(final byte[] x) {
        return new String(x, US_ASCII);
    }

    static final byte[] stringToAsciiBytes(final String x) {
        return x.getBytes(US_ASCII);
    }

    /**
     * Reads all bytes from a file into a byte array.
     *
     * @param pathName A pathName string
     * @throws NullPointerException If the {@code pathName} argument is
     * {@code null}
     * @return A byte array containing all the bytes from file
     */
    static final Optional<byte[]> readFileAsBytes(final String pathName) {
        final File file = new File(pathName);
        try {
            return Optional.of(toByteArray(file));
        } catch (IOException | IllegalArgumentException ex) {
            LOGGER.warn(format("{0}: {1}", makeParam(ex)));
            return Optional.empty();
        }
    }

    /**
     * No construction
     */
    Utilities() {
        throw new UnsupportedOperationException();
    }

}
