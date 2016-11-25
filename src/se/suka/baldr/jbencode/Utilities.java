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

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Graham Fairweather
 * @see <a href="https://en.wikipedia.org/wiki/Bencode">Bencode</a>
 */
public class Utilities {

    private static final Logger LOGGER = Logger.getLogger(Utilities.class.getSimpleName());
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
        return Math.min(Math.max(value, min), max);
    }

    /**
     *
     * @param pattern
     * @param chars
     * @return
     */
    public static final int findFirstNotOf(final String pattern, final String chars) {
        return findFirstNotOf(pattern, chars, 0);
    }

    /**
     *
     * @param pattern
     * @param chars
     * @param startIndex
     * @return
     */
    public static final int findFirstNotOf(final String pattern, final String chars, final int startIndex) {
        return getPos(pattern, "[^" + chars + "]", startIndex);
    }

    /**
     *
     * @param pattern
     * @param chars
     * @return
     */
    public static final int findFirstOf(final String pattern, final String chars) {
        return findFirstOf(pattern, chars, 0);
    }

    /**
     *
     * @param pattern
     * @param chars
     * @param startIndex
     * @return
     */
    public static final int findFirstOf(final String pattern, final String chars, final int startIndex) {
        return getPos(pattern, "[" + chars + "]", startIndex);
    }

    /**
     *
     */
    private static Object[] makeParam(final Throwable ex) {
        return new Object[]{ex.getClass().getName(), ex.getMessage()};
    }

    /**
     *
     * @param absolutePathToAFile
     * @param charsetName
     * @return
     */
    public static final String readFileBytesToString(final String absolutePathToAFile, final String charsetName) {
        final Path path;
        try {
            path = Paths.get(absolutePathToAFile);
        } catch (final InvalidPathException | NullPointerException ioex) {
            LOGGER.log(Level.WARNING, "{0}: {1}", makeParam(ioex));
            return null;
        }
        final Charset charset;
        try {
            charset = Charset.forName(charsetName);
        } catch (final IllegalArgumentException iaex) {
            LOGGER.log(Level.WARNING, "{0}: {1}", makeParam(iaex));
            return null;
        }
        final byte[] arrayOfBytes;
        try {
            arrayOfBytes = Files.readAllBytes(path);
        } catch (final IOException | NullPointerException | OutOfMemoryError | SecurityException ioex) {
            LOGGER.log(Level.WARNING, "{0}: {1}", makeParam(ioex));
            return null;
        }
        return Objects.isNull(charset) ? new String(arrayOfBytes) : new String(arrayOfBytes, charset);
    }

    /**
     *
     * @param absolutePathToAFile
     * @return
     */
    public static final String readFileLinesToString(final String absolutePathToAFile) {
        final Path path;
        try {
            path = Paths.get(absolutePathToAFile);
        } catch (final InvalidPathException | NullPointerException ipex) {
            LOGGER.log(Level.WARNING, "{0}: {1}", makeParam(ipex));
            return null;
        }
        final List<String> lines;
        try {
            lines = Files.readAllLines(path);
        } catch (final IOException | NullPointerException | OutOfMemoryError | SecurityException ioex) {
            LOGGER.log(Level.WARNING, "{0}: {1}", makeParam(ioex));
            return null;
        }
        final StringBuilder stringOfLines = new StringBuilder();
        lines.stream().forEach(line -> stringOfLines.append(line));
        return stringOfLines.toString();
    }

    /**
     *
     * @param pattern
     * @param characterSequencce
     * @param startIndex
     * @return
     */
    public static final int getPos(final String pattern, final String characterSequencce, final int startIndex) {
        final Matcher matcher = Pattern.compile(characterSequencce).matcher(pattern);
        return matcher.find(startIndex) ? matcher.start() : -1;
    }

    /**
     * No construction allowed
     */
    private Utilities() {
    }

}
