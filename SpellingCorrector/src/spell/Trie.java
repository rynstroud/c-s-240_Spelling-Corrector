package spell;

public class Trie implements ITrie {

    private TrieNode root;
    private int wordCount;
    private int nodeCount;

    public Trie() {
        setNodeCount(1);
        setWordCount(0);
        root = new TrieNode();
    }

    //inserts new words into the Trie
    @Override
    public void add(String word) {

        //convert word to all lowercase
        String lword = word.toLowerCase();

        TrieNode curNode = root;
        for (int i = 0; i < lword.length(); ++i) {
            char letter = lword.charAt(i);
            add_helper(letter, curNode); //calls the helper function on each letter in the word

            if (i != lword.length()) { //if statement gives ability to increment the last node's frequency count if word already existed
                curNode = (TrieNode) curNode.getChildren()[(letter - 'a')];
            }
        }

        //increment the word count if a new word was actually added
        if (!curNode.isAWord()) {
            ++wordCount;
            curNode.setAWord(true);
        }
        else {
            curNode.incrementValue();
        }
    }

    //creates a new node for each letter and parent node provided
    private void add_helper(char curLetter, TrieNode curNode) {

        //make a new node at the letter if it does not already exist
        if (curNode.getChildren()[(curLetter - 'a')] == null) {
            TrieNode child = new TrieNode();
            curNode.addChild(child, curLetter - 'a');

            ++nodeCount; //increment node count if node was added
        }
    }

    @Override
    public TrieNode find(String word) {

        //convert word to all lowercase
        String lword = word.toLowerCase();
        TrieNode curNode = root;
        for (int i = 0; i < lword.length(); ++i) {
            char letter = lword.charAt(i);

            //if  the letter DNE, return null
            if (curNode.getChildren()[(letter - 'a')] == null) {
                return null;
            }

            //if statement gives ability to return the last node
            if ((i != lword.length()) || (lword.length() == 1)){
                curNode = (TrieNode) curNode.getChildren()[(letter - 'a')];
            }
        }

        if (curNode.isAWord()) {
            return curNode;
        }
        else {
            return null;
        }
    }

    @Override
    public int getWordCount() {
        return wordCount;
    }

    @Override
    public int getNodeCount() {
        return nodeCount;
    }

    public void setWordCount(int wordCount) {
        this.wordCount = wordCount;
    }

    public void setNodeCount(int nodeCount) {
        this.nodeCount = nodeCount;
    }

    @Override
    public String toString() {

        StringBuilder curWord = new StringBuilder();
        StringBuilder output = new StringBuilder();

        toString_Helper(root, curWord, output);

        return output.toString();
    }

    private void toString_Helper(TrieNode n, StringBuilder curWord, StringBuilder output) {

        if (n.isAWord()) {
            output.append(curWord.toString());
            output.append("\n");
        }

        for (int i = 0; i < n.getChildren().length; ++i) {
            INode child = n.getChildren()[i];

            if (child != null) {
                char childLetter = (char) ('a' + i);
                curWord.append(childLetter);

                toString_Helper((TrieNode) child, curWord, output);

                curWord.deleteCharAt(curWord.length() - 1);
            }
        }
    }

    @Override
    public int hashCode() {
        int hash = 0;
        for (int i = 0; i < root.getChildren().length; ++i) {
            if (root.getChildren()[i] != null) {
                hash = i;
                break;
            }
        }
        return (hash * nodeCount * wordCount);
    }

    @Override
    public boolean equals(Object o) {
        //check for null
        if (o == null) {
            return false;
        }

        //check if comparing with itself
        if (o == this) {
            return true;
        }

        //check if same class
        if (this.getClass() != o.getClass()) {
            return false;
        }

        Trie other = (Trie)o;

        //check nodeCount and wordCount
        if ((nodeCount != other.nodeCount) || (wordCount != other.wordCount)) {
            return false;
        }

        return equals_Helper(root, other.root);
    }

    private boolean equals_Helper(INode n1, INode n2) {

        //just in case
        try {
            //compare the frequency count in the two nodes
            if (n1.getValue() != n2.getValue()) {
                return false;
            }

            //check to see if they have children in the same positions
            for (int i = 0; i < n1.getChildren().length; ++i) {

                //if n1 doesn't have a child in this position, n2 can't either
                if (n1.getChildren()[i] == null) {
                    if (n2.getChildren()[i] != null) {
                        return false;
                    }
                }

                else {
                    //if n1 does have a child in this position, n2 has to as well
                    if (n2.getChildren()[i] == null) {
                        return false;
                    }

                    //recursively compare the children
                    if (!equals_Helper(n1.getChildren()[i], n2.getChildren()[i])) {
                        return false;
                    }
                }
            }
            return true;
        }
        catch (NullPointerException ex) {
            System.out.println("I have NO IDEA why you got a null node when we literally checked first,\n " +
                    "but you do.");
        }

        return false;
    }
}
