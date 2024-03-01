package ngordnet.main;

import ngordnet.browser.NgordnetQuery;
import ngordnet.browser.NgordnetQueryHandler;
import ngordnet.ngrams.NGramMap;

import java.util.List;

public class HistoryTextHandler extends NgordnetQueryHandler {
    private NGramMap n;
    @Override
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        int startYear = q.startYear();
        int endYear = q.endYear();

        String s = "";
        for (String word: words) {
            s += word + ": ";
            s += n.weightHistory(word, startYear, endYear).toString();
            s += "\n";
        }
        return s;

    }

    public HistoryTextHandler(NGramMap map) {
        n = map;
    }
}
