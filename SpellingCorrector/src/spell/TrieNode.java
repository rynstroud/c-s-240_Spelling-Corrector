package spell;

public class TrieNode implements INode {

    private int value;
    private TrieNode[] children;
    private boolean isAWord;

    public TrieNode() {
        setValue(0);
        children = new TrieNode[26];
        isAWord = false;
    }

    public boolean isAWord() { return isAWord; }

    public void setAWord(boolean AWord) { isAWord = AWord; }

    public void setValue(int value) {
        this.value = value;
    }

    //Returns the frequency count for the word represented by the node.
    @Override
    public int getValue() {
        return value;
    }

    //Increments the frequency count for the word represented by the node.
    @Override
    public void incrementValue() {
        ++value;
    }

    public void addChild(TrieNode n, int position) {
        children[position] = n;
    }

    //Returns the children array
    @Override
    public INode[] getChildren() {
        return children;
    }

}
