/**
 * @author Avraham sikirov 318731478
 * Simple animation which creates one moving ball
 * in a specific frame. (The ball stays in the frame)
 */

import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Collections;
import java.util.List;

/**
 * Class represent a hypernym. with all his hyponyms is a list.
 */

public class Hypernymy implements Comparable<Hypernymy> {
    private String hypernymyName;
    private Map<String, Integer> hyponymMap;
    private int counter;

    /**
     * Constructor.
     *
     * @param string name of the Hypernym.
     */

    public Hypernymy(String string) {
        this.hypernymyName = string;
        this.hyponymMap = new HashMap<String, Integer>();
        this.counter = 0;
    }

    /**
     * @return name of the hypernym.
     */

    public String getHypernymyName() {
        return this.hypernymyName;
    }


    /**
     * Sorting map by values. converting unsortedMap to list, then applying it to new map
     *
     * @param unsortedMap map to sort.
     * @return sorted map.
     */
    public static HashMap<String, Integer> sortByValue(Map<String, Integer> unsortedMap) {
        List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(unsortedMap.entrySet());

        // Create new comparator.
        Comparator<Map.Entry<String, Integer>> comparator = new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> value1,
                               Map.Entry<String, Integer> value2) {
                return (value1.getValue()).compareTo(value2.getValue());
            }
        };
        list.sort(comparator);
        Collections.reverse(list);

        HashMap<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> aa : list) {
            sortedMap.put(aa.getKey(), aa.getValue());
        }
        return sortedMap;
    }

    /**
     * @return number of hyponyms.
     */
    public int getCounter() {
        return this.counter;
    }

    /**
     * adding new hyponym.
     *
     * @param hyponymName hyponym name.
     */

    public void addHyponym(String hyponymName) {
        if (this.hyponymMap.containsKey(hyponymName)) {
            hyponymMap.put(hyponymName, hyponymMap.get(hyponymName) + 1);
        } else if (this.hypernymyName.equals(hyponymName)) {
            return;
        } else {
            hyponymMap.put(hyponymName, 1);

        }
        ++this.counter;
    }

    @Override
    public String toString() {

        this.hyponymMap = sortByValue(this.hyponymMap);
        String allhyponyms = this.hypernymyName + ": ";
        for (Map.Entry<String, Integer> entry : hyponymMap.entrySet()) {
            allhyponyms = allhyponyms + entry.getKey() + " (" + entry.getValue() + "), ";
        }
        return allhyponyms.substring(0, allhyponyms.length() - 2);
    }

    /**
     * @return hyponym list.
     */
    public Map<String, Integer> getHyponym() {
        return this.hyponymMap;
    }

    @Override
    public int compareTo(Hypernymy o) {
        return this.hypernymyName.compareTo(o.getHypernymyName());
    }
}