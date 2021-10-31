/**
 * @author Avraham sikirov 318731478
 * Simple animation which creates one moving ball
 * in a specific frame. (The ball stays in the frame)
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.List;
import java.util.Map;


/**
 * Check all the hypernym for specific lemma.
 */

public class DiscoverHypernym {


    /**
     * @param args args for the location of the input and a lemma to search for it's hypernyms.
     * @throws IOException in case of error input file name.
     */
    public static void main(String[] args) throws IOException {

        String fileInput = args[0];
        String lemma = args[1];
        List<String> regExpressions = new ArrayList<>();
        Map<String, Integer> category = new TreeMap<>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(fileInput));
        String line;

        while ((line = bufferedReader.readLine()) != null) {
            CreateHypernymDatabase.addRegTolist(line, regExpressions);
        }
        List<Hypernymy> hypernymies = CreateHypernymDatabase.decompose(regExpressions);

        for (Hypernymy hypernymy : hypernymies) {
            Map<String, Integer> map = hypernymy.getHyponym();
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                if (entry.getKey().equals(lemma)) {
                    if (category.containsKey(hypernymy.getHypernymyName())) {
                        category.put(hypernymy.getHypernymyName(), category.get(hypernymy.getHypernymyName())
                                + entry.getValue());
                    }
                    category.put(hypernymy.getHypernymyName(), entry.getValue());
                }
            }
        }
        for (Map.Entry<String, Integer> entry : category.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
    }
}