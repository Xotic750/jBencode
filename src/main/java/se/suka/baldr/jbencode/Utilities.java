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
import static java.util.Objects.requireNonNull;
import java.util.Random;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Graham Fairweather
 * @see <a href="https://en.wikipedia.org/wiki/Bencode">Bencode</a>
 */
public class Utilities {

    private static final Logger LOGGER = getLogger(Utilities.class);
    private static final Random RAND = new Random();

    /**
     * Returns a random integer between min (included) and max (excluded)
     *
     * @param min
     * @param max
     * @return
     */
    public static final int randInt(final int min, final int max) {
        return RAND.nextInt(max - min) + min;
    }

    /**
     * Returns a random integer between min (included) and max (included)
     *
     * @param min
     * @param max
     * @return
     */
    public static final int randIntClosed(final int min, final int max) {
        return randInt(min, max + 1);
    }

    /**
     *
     * @param value
     * @param min
     * @param max
     * @return
     */
    public static final int clampInt(final int value, final int min, final int max) {
        return min(max(value, min), max);
    }

    /**
     *
     * @param pattern
     * @param chars
     * @param startIndex
     * @return
     */
    public static final int findFirstNotOf(final String pattern, final String chars, final int startIndex) {
        if (startIndex > requireNonNull(pattern).length()) {
            return -1;
        }
        return anyOf(requireNonNull(chars)).negate().indexIn(pattern, startIndex);
    }

    /**
     *
     */
    private static Object[] makeParam(final Throwable ex) {
        return new Object[]{
            requireNonNull(ex).getClass().getName(),
            ex.getMessage()
        };
    }

    /**
     *
     * @param pathToAFile
     * @return
     */
    public static final String readTorrentFile(final String pathToAFile) {
        final File file;
        try {
            file = new File(pathToAFile);
        } catch (NullPointerException npex) {
            LOGGER.warn(format("{0}: {1}", makeParam(npex)));
            return null;
        }
        final byte[] bytes;
        try {
            bytes = toByteArray(file);
        } catch (IOException | IllegalArgumentException ioex) {
            LOGGER.warn(format("{0}: {1}", makeParam(ioex)));
            return null;
        }
        return new String(bytes, US_ASCII);
    }

    /**
     *
     * @param o
     * @return
     */
    public static final boolean isString(Object o) {
        return o instanceof String;
    }

    /**
     *
     * @param o
     * @return
     */
    public static final String requireString(Object o) {
        if (!isString(requireNonNull(o))) {
            throw new IllegalArgumentException();
        }
        return (String) o;
    }

    /**
     *
     * @param o
     * @param message
     * @return
     */
    public static final String requireString(Object o, String message) {
        if (!isString(requireNonNull(o))) {
            throw new IllegalArgumentException(message);
        }
        return (String) o;
    }

    /**
     * No construction allowed
     */
    private Utilities() {
    }

}
