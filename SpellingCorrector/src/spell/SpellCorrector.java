package spell;


import java.io.*;
import java.util.*;

public class SpellCorrector implements ISpellCorrector {

    private Trie trie;

    static String swapChars(String str, int i, int j)
    {
        StringBuilder newSB = new StringBuilder(str);
        newSB.setCharAt(i, str.charAt(j));
        newSB.setCharAt(j, str.charAt(i));
        return newSB.toString();
    }

    public SpellCorrector() {
        trie = new Trie();
    }

    @Override
    public void useDictionary(String dictionaryFileName) throws IOException {

        Scanner scan;

        //try to open the file given
        File text = new File(dictionaryFileName);
        scan = new Scanner(text);

        while (scan.hasNext()) {
            String line = scan.next();
            trie.add(line);
        }

        //close file
        scan.close();
    }

    @Override
    public String suggestSimilarWord(String inputWord) {

        ArrayList<TrieNode> editDistance1 = new ArrayList<>();
        ArrayList<TrieNode> editDistance2 = new ArrayList<>();
        ArrayList<String> edStr1 = new ArrayList<>();
        ArrayList<String> edStr2 = new ArrayList<>();
        ArrayList<String> allPossibleED1 = new ArrayList<>();

        //convert word to all lowercase
        String word = inputWord.toLowerCase();

        TrieNode found = trie.find(word);

        //if the word exists, return that word
        if (found != null) return word;

        else {
            findEditDistanceWords(word, editDistance1, edStr1, editDistance2, edStr2, allPossibleED1);

            //check editDistance1  words
            if (editDistance1.size() > 0) {
                int h = suggestSimilarWord_Helper(editDistance1);
                return edStr1.get(h);
            }

            //check editDistance2 words
            else if (editDistance2.size() > 0) {
                int h = suggestSimilarWord_Helper(editDistance2);
                if (h >= 0) return edStr2.get(h);
                else return null;
            }

            //no similar words
            else {
                return null;
            }
        }
    }

    private int suggestSimilarWord_Helper(ArrayList<TrieNode> editDistance) {

        //if there is only one close word, suggest that.
        if (editDistance.size() == 1) return 0;

        else if (editDistance.size() > 1) {
            int modeLoc = 0;
            for (int i = 0; i < editDistance.size(); ++i) {

                //get the index of the most frequently occurring word
                //just > instead of  >= because this way it returns first alphabetically automatically
                if (editDistance.get(i).getValue() > editDistance.get(modeLoc).getValue()) {
                    modeLoc = i;
                }
            }
            return modeLoc;
        }
        else {
            return -1;
        }
    }

    private void findEditDistanceWords(String ogWord, ArrayList<TrieNode> editDistance1, ArrayList<String> edStr1,
                                       ArrayList<TrieNode> editDistance2, ArrayList<String> edStr2,
                                       ArrayList<String> allPossibleED1) {

        //Find editDistance1 words
        findDeletionWords(ogWord, editDistance1, edStr1, allPossibleED1, 1);
        findTranspositionWords(ogWord, editDistance1, edStr1, allPossibleED1, 1);
        findAlterationWords(ogWord, editDistance1, edStr1, allPossibleED1, 1);
        findInsertionWords(ogWord, editDistance1, edStr1, allPossibleED1, 1);

        if (editDistance1.size() > 0) return;

        else {
            //if no editDistance1 words are found, find editDistance2 words
            for (int i = 0; i < allPossibleED1.size(); i++) {
                findDeletionWords(allPossibleED1.get(i), editDistance2, edStr2, allPossibleED1, 2);
                findTranspositionWords(allPossibleED1.get(i), editDistance2, edStr2, allPossibleED1, 2);
                findAlterationWords(allPossibleED1.get(i), editDistance2, edStr2, allPossibleED1, 2);
                findInsertionWords(allPossibleED1.get(i), editDistance2, edStr2, allPossibleED1, 2);
            }
        }
    }

    private void findDeletionWords(String ogWord, ArrayList<TrieNode> tnList, ArrayList<String> sList,
                                   ArrayList<String> allPossibleED1, int n) {
        for (int i = 0; i < ogWord.length(); i++) {
            StringBuilder word = new StringBuilder();
            word.append(ogWord);

            if (i > ogWord.length() - 1) break;
            StringBuilder temp = word.deleteCharAt(i);

            //makes a list of all possible ed1 words for ed2
            if (n == 1) allPossibleED1.add(temp.toString());

            TrieNode found = trie.find(temp.toString());
            if (found != null) {
                sList.add(temp.toString());
                tnList.add(found);
            }
        }
    }

    private void findTranspositionWords(String ogWord, ArrayList<TrieNode> tnList, ArrayList<String> sList,
                                        ArrayList<String> allPossibleED1, int n) {

        for (int i = 0; i < ogWord.length() - 1; i++) {
            if (i+1 == ogWord.length()) break;
            String temp = swapChars(ogWord, i, i+1);

            //makes a list of all possible ed1 words for ed2
            if (n == 1) allPossibleED1.add(temp);

            TrieNode found = trie.find(temp);
            if (found != null) {
                sList.add(temp);
                tnList.add(found);
            }
        }
    }

    private void findAlterationWords(String ogWord, ArrayList<TrieNode> tnList, ArrayList<String> sList,
                                     ArrayList<String> allPossibleED1, int n) {

        for (int i = 0; i < 26; ++i) {
            for (int j = 0; j < ogWord.length(); j++) {
                StringBuilder word = new StringBuilder();
                word.append(ogWord);
                char ch = (char) (i + 'a');
                StringBuilder temp = word;
                temp.setCharAt(j, ch);

                //makes a list of all possible ed1 words for ed2
                if (n == 1) allPossibleED1.add(temp.toString());

                TrieNode found = trie.find(temp.toString());
                if (found != null) {
                    sList.add(temp.toString());
                    tnList.add(found);
                }
            }
        }
    }

    private void findInsertionWords(String ogWord, ArrayList<TrieNode> tnList, ArrayList<String> sList,
                                    ArrayList<String> allPossibleED1, int n) {

        for (int i = 0; i < 26; ++i) {
            for (int j = 0; j < ogWord.length() + 1; j++) {
                StringBuilder word = new StringBuilder();
                word.append(ogWord);
                char ch = (char) (i + 'a');
                StringBuilder temp;
                if (j == word.length()) {
                    temp = word.append(ch);
                }
                else {
                    temp = word.insert(j, ch);
                }

                //makes a list of all possible ed1 words for ed2
                if (n == 1) allPossibleED1.add(temp.toString());

                TrieNode found = trie.find(temp.toString());
                if (found != null) {
                    sList.add(temp.toString());
                    tnList.add(found);
                }
            }
        }
    }
}
