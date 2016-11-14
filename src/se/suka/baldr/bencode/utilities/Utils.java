package se.suka.baldr.bencode.utilities;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Graham Fairweather
 * @see <a href="https://en.wikipedia.org/wiki/Bencode">Bencode</a>
 */
public class Utils {

    public static int clampInt(final int VALUE_ARG, final int MIN_ARG, final int MAX_ARG) {
        return Math.min(Math.max(VALUE_ARG, MIN_ARG), MAX_ARG);
    }

    public static int findFirstNotOf(final String STRING_ARG, final String CHARS_ARG) {
        return findFirstNotOf(STRING_ARG, CHARS_ARG, 0);
    }

    public static int findFirstNotOf(final String STRING_ARG, final String CHARS_ARG, final int STARTINDEX_ARG) {
        return getPos(Pattern.compile("[^" + CHARS_ARG + "]"), STRING_ARG, STARTINDEX_ARG);
    }

    public static int findFirstOf(final String STRING_ARG, final String CHARS_ARG) {
        return findFirstOf(STRING_ARG, CHARS_ARG, 0);
    }

    public static int findFirstOf(final String STRING_ARG, final String CHARS_ARG, final int STARTINDEX_ARG) {
        return getPos(Pattern.compile("[" + CHARS_ARG + "]"), STRING_ARG, STARTINDEX_ARG);
    }

    public static String readFileBytesToString(final String FILENAME_ARG, final String CHARSETNAME_ARG) {
        final Path PATH;
        try {
            PATH = Paths.get(FILENAME_ARG);
        } catch (final InvalidPathException | NullPointerException IOE) {
            System.err.println("readFileBytesToString: " + IOE);
            return null;
        }
        final Charset CHARSET;
        try {
            CHARSET = Charset.forName(CHARSETNAME_ARG);
        } catch (final IllegalArgumentException IAE) {
            System.err.println("readFileBytesToString: " + IAE);
            return null;
        }
        final byte[] BYTES;
        try {
            BYTES = Files.readAllBytes(PATH);
        } catch (final IOException | NullPointerException | OutOfMemoryError | SecurityException IOE) {
            System.err.println("readFileBytesToString: " + IOE);
            return null;
        }
        return CHARSET == null ? new String(BYTES) : new String(BYTES, CHARSET);
    }

    public static String readFileLinesToString(final String FILENAME_ARG) {
        final Path PATH;
        try {
            PATH = Paths.get(FILENAME_ARG);
        } catch (final InvalidPathException | NullPointerException IPE) {
            System.err.println("readFileLinesToString: " + IPE);
            return null;
        }
        final List<String> LIST;
        try {
            LIST = Files.readAllLines(PATH);
        } catch (final IOException | NullPointerException | OutOfMemoryError | SecurityException IOE) {
            System.err.println("readFileLinesToString: " + IOE);
            return null;
        }
        final StringBuilder STRINGBUILDER = new StringBuilder();
        LIST.stream().forEach((String line) -> {
            STRINGBUILDER.append(line);
        });
        return STRINGBUILDER.toString();
    }

    private static int getPos(final Pattern PATTERN_ARG, final String STRING_ARG, final int STARTINDEX_ARG) {
        final Matcher MATCHER = PATTERN_ARG.matcher(STRING_ARG);
        return MATCHER.find(STARTINDEX_ARG) ? MATCHER.start() : -1;
    }

}
