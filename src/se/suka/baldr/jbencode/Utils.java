package se.suka.baldr.jbencode;

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
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Graham Fairweather
 * @see <a href="https://en.wikipedia.org/wiki/Bencode">Bencode</a>
 */
public class Utils {

    /**
     *
     * @param value
     * @param min
     * @param max
     * @return
     */
    public static int clampInt(final int value, final int min, final int max) {
        return Math.min(Math.max(value, min), max);
    }

    /**
     *
     * @param pattern
     * @param chars
     * @return
     */
    public static int findFirstNotOf(final String pattern, final String chars) {
        return findFirstNotOf(pattern, chars, 0);
    }

    /**
     *
     * @param pattern
     * @param chars
     * @param startIndex
     * @return
     */
    public static int findFirstNotOf(final String pattern, final String chars, final int startIndex) {
        return getPos(pattern, "[^" + chars + "]", startIndex);
    }

    /**
     *
     * @param pattern
     * @param chars
     * @return
     */
    public static int findFirstOf(final String pattern, final String chars) {
        return findFirstOf(pattern, chars, 0);
    }

    /**
     *
     * @param pattern
     * @param chars
     * @param startIndex
     * @return
     */
    public static int findFirstOf(final String pattern, final String chars, final int startIndex) {
        return getPos(pattern, "[" + chars + "]", startIndex);
    }

    /**
     *
     * @param absolutePathToAFile
     * @param charsetName
     * @return
     */
    public static String readFileBytesToString(final String absolutePathToAFile, final String charsetName) {
        final Path path;
        try {
            path = Paths.get(absolutePathToAFile);
        } catch (final InvalidPathException | NullPointerException ioex) {
            System.err.println("readFileBytesToString: " + ioex);
            return null;
        }
        final Charset charset;
        try {
            charset = Charset.forName(charsetName);
        } catch (final IllegalArgumentException iaex) {
            System.err.println("readFileBytesToString: " + iaex);
            return null;
        }
        final byte[] arrayOfBytes;
        try {
            arrayOfBytes = Files.readAllBytes(path);
        } catch (final IOException | NullPointerException | OutOfMemoryError | SecurityException ioex) {
            System.err.println("readFileBytesToString: " + ioex);
            return null;
        }
        return Objects.isNull(charset) ? new String(arrayOfBytes) : new String(arrayOfBytes, charset);
    }

    /**
     *
     * @param absolutePathToAFile
     * @return
     */
    public static String readFileLinesToString(final String absolutePathToAFile) {
        final Path path;
        try {
            path = Paths.get(absolutePathToAFile);
        } catch (final InvalidPathException | NullPointerException ipex) {
            System.err.println("readFileLinesToString: " + ipex);
            return null;
        }
        final List<String> lines;
        try {
            lines = Files.readAllLines(path);
        } catch (final IOException | NullPointerException | OutOfMemoryError | SecurityException ioex) {
            System.err.println("readFileLinesToString: " + ioex);
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
    public static int getPos(final String pattern, final String characterSequencce, final int startIndex) {
        final Matcher matcher = Pattern.compile(characterSequencce).matcher(pattern);
        return matcher.find(startIndex) ? matcher.start() : -1;
    }

    private Utils() {
    }

}
