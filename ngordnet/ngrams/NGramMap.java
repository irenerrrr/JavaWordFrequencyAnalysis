package ngordnet.ngrams;

import edu.princeton.cs.algs4.In;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * An object that provides utility methods for making queries on the
 * Google NGrams dataset (or a subset thereof).
 *
 * An NGramMap stores pertinent data from a "words file" and a "counts
 * file". It is not a map in the strict sense, but it does provide additional
 * functionality.
 *
 * @author Josh Hug
 */
public class NGramMap {

    private static final int MIN_YEAR = 1400;
    private static final int MAX_YEAR = 2100;

    private Map<String, TimeSeries> hashmap;
    private TimeSeries c;

    /**
     * Constructs an NGramMap from WORDSFILENAME and COUNTSFILENAME.
     */
    public NGramMap(String wordsFilename, String countsFilename) {

        hashmap = new HashMap<>();
        c = new TimeSeries();

        In words = new In(wordsFilename);


        while (words.hasNextLine()) {
            String lines = words.readLine();
            String[] line = lines.split("\t");


            String name = line[0];
            int year = Integer.parseInt(line[1]);
            Double number = Double.parseDouble(line[2]);

            if (!hashmap.containsKey(name)) {
                TimeSeries n = new TimeSeries();
                n.put(year, number);
                hashmap.put(name, n);
            } else {
                hashmap.get(name).put(year, number);
            }
        }

        In counts = new In(countsFilename);
        while (counts.hasNextLine()) {
            String lines = counts.readLine();
            String[] line = lines.split(",");
            c.put(Integer.valueOf(line[0]), Double.valueOf(line[1]));
        }

    }

    /**
     * Provides the history of WORD between STARTYEAR and ENDYEAR, inclusive of both ends. The
     * returned TimeSeries should be a copy, not a link to this NGramMap's TimeSeries. In other
     * words, changes made to the object returned by this function should not also affect the
     * NGramMap. This is also known as a "defensive copy".
     */
    public TimeSeries countHistory(String word, int startYear, int endYear) {
        return new TimeSeries(hashmap.get(word), startYear, endYear);

    }

    /**
     * Provides the history of WORD. The returned TimeSeries should be a copy,
     * not a link to this NGramMap's TimeSeries. In other words, changes made
     * to the object returned by this function should not also affect the
     * NGramMap. This is also known as a "defensive copy".
     */
    public TimeSeries countHistory(String word) {
        return hashmap.get(word);
    }

    /**
     * Returns a defensive copy of the total number of words recorded per year in all volumes.
     */
    public TimeSeries totalCountHistory() {
        return c;
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD between STARTYEAR
     * and ENDYEAR, inclusive of both ends.
     */
    public TimeSeries weightHistory(String word, int startYear, int endYear) {
        return countHistory(word, startYear, endYear).dividedBy(new TimeSeries(c, startYear, endYear));

    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD compared to
     * all words recorded in that year. If the word is not in the data files, return an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word) {
        if (!hashmap.containsKey(word)) {
            return new TimeSeries();
        }
        return countHistory(word).dividedBy(c);
    }

    /**
     * Provides the summed relative frequency per year of all words in WORDS
     * between STARTYEAR and ENDYEAR, inclusive of both ends. If a word does not exist in
     * this time frame, ignore it rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words,
                                          int startYear, int endYear) {
        TimeSeries n = new TimeSeries();
        for (String word : words) {
            if (hashmap.containsKey(word)) {
                n = n.plus(weightHistory(word, startYear, endYear));
            }
        }
        return n;
    }

    /**
     * Returns the summed relative frequency per year of all words in WORDS.
     */
    public TimeSeries summedWeightHistory(Collection<String> words) {
        TimeSeries n = new TimeSeries();
        for (String word : words) {
            if (hashmap.containsKey(word)) {
                n = n.plus(weightHistory(word));
            }
        }
        return n;

    }

}
