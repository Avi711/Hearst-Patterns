/**
 * @author Avraham sikirov 318731478
 * Simple animation which creates one moving ball
 * in a specific frame. (The ball stays in the frame)
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Collections;

/**
 * Creating data base of Hypernyms.
 */

public class CreateHypernymDatabase {
    private static final int PREFIX_NP = 4;
    private static final int SUFIX_NP = 5;
    public static final String NP = "[ ,]*(<np>)[A-z0-9 ,:.&()_-]+(</np>)[ ,]*";
    // public static final String NP = "[- ,:]*(<np>)[A-z0-9 ,:.&()_-]+(</np>)[- ,:]*";

    /**
     * @param args args for the location of the input and output files.
     * @throws IOException in case of error input file name.
     */

    public static void main(String[] args) throws IOException {

        String fileInput = args[0];
        String fileOutput = args[1];
        File f = new File(fileInput);
        File[] files = f.listFiles();
        List<String> regExpressions = new ArrayList<>();
        String line;
        BufferedReader bufferedReader = null;
        for (File file : files) {
            if (file.isFile()) {
                bufferedReader = new BufferedReader(new FileReader(file));
                while ((line = bufferedReader.readLine()) != null) {
                    addRegTolist(line, regExpressions);
                }
            }
        }


        //   BufferedReader bufferedReader = new BufferedReader(new FileReader(f));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileOutput));

        //  while ((line = bufferedReader.readLine()) != null) {
        //      addRegTolist(line, regExpressions);
        //   }

/*        int counter1 = 0;
        for (String token : regExpressions) {
            System.out.println(token);
            ++counter1;
        }
        System.out.println(counter1);*/

        int coutner = 0;
        List<Hypernymy> hypernymies = decompose(regExpressions);
        Comparator<Hypernymy> comparator = new Comparator<Hypernymy>() {
            @Override
            public int compare(Hypernymy o1, Hypernymy o2) {
                return o1.getHypernymyName().toLowerCase().compareTo(o2.getHypernymyName().toLowerCase());
            }
        };
        //  List<String> checklist = new ArrayList<>();
        Collections.sort(hypernymies, comparator);
        for (Hypernymy token : hypernymies) {
            if (token.getCounter() >= 3) {
                System.out.println(token);
                //   checklist.add(token.getHypernymyName());
                bufferedWriter.write(token.toString() + "\n");
                ++coutner;
            }
        }
        System.out.println(coutner);


/*        int ducplicates = 0;
        for (int i = 0; i < checklist.size(); ++i) {
            for (int j = 0; j < checklist.size(); ++j) {
                if (checklist.get(i).equalsIgnoreCase(checklist.get(j)) && i != j) {
                    System.out.println(checklist.get(i));
                    ++ducplicates;
                }
            }
        }
        System.out.println(ducplicates);*/


        bufferedReader.close();
        bufferedWriter.close();

    }

    /**
     * Adding correct regs to List of regs.
     *
     * @param string string to check if contains regs to add.
     * @param list   list to add the regex to.
     */
    public static void addRegTolist(String string, List<String> list) {

        List<Pattern> patternList = new LinkedList<>();

        patternList.add(Pattern.compile("such" + NP + "as" + NP + "(" + NP + ")*(and|or)" + NP));
        patternList.add(Pattern.compile(NP + "which is" + "[ ,]*(an example|a kind|a class)[, ]*of" + NP));
        patternList.add(Pattern.compile(NP + "(such as|including|expecially|which is)" + NP
                + "(" + NP + ")*((and|or)( )*" + NP + ")?"));

        String word;
        for (Pattern patternIter : patternList) {
            Matcher matcher = patternIter.matcher(string);
            while (matcher.find()) {
                word = string.substring(matcher.start(), matcher.end());
                list.add(word);
            }
        }
    }

    /**
     * Disassembles the pattern words and divides them into Hypernymy and hyponym.
     *
     * @param list list of strings to decompose.
     * @return new list of hypernyms.
     */
    public static List<Hypernymy> decompose(List<String> list) {
        String np = "(<np>)[A-z0-9 ,&:._-]+(</np>)";
        Pattern patternNP = Pattern.compile(np);
        List<Hypernymy> newList = new ArrayList<>();
        List<String> nameList = new ArrayList<>();

        for (String string : list) {
            Matcher matcher = patternNP.matcher(string);
            Hypernymy hypernymy = null;
            int i = 0;
            while (matcher.find()) {
                String matcherName = string.substring(matcher.start() + PREFIX_NP, matcher.end() - SUFIX_NP);
                if (i != 0) {
                    hypernymy.addHyponym(matcherName);
                } else if (nameList.contains(matcherName.toLowerCase())) {
                    int index = nameList.indexOf(matcherName.toLowerCase());
                    hypernymy = newList.get(index);
                } else {
                    hypernymy = new Hypernymy(matcherName);
                    newList.add(hypernymy);
                    nameList.add(hypernymy.getHypernymyName().toLowerCase());
                }
                ++i;
            }
        }
        return newList;
    }
}