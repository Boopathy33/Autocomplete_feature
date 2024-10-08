import java.io.*;
import java.util.Arrays;

class TrieNode {
    TrieNode[] children;
    boolean isEndOfWord;

    public TrieNode() {
        children = new TrieNode[60]; // ALPHABET_SIZE = 60
        isEndOfWord = false;
    }
}

public class AutoCompleteSystem {

    private static final int ALPHABET_SIZE = 60;
    private static final int MAX_SUGGESTION_SIZE = 10;
    private static final String DICTIONARY_FILE_NAME = "dictionary.txt";

    // Insert a word into the Trie
    public static void insert(TrieNode root, String key) {
        TrieNode currentNode = root;
        for (int i = 0; i < key.length(); i++) {
            char ch = key.charAt(i);
            int index = ch - 'A';
            if (currentNode.children[index] == null) {
                currentNode.children[index] = new TrieNode();
            }
            currentNode = currentNode.children[index];
        }
        currentNode.isEndOfWord = true;
    }

    // Search helper to find possible words
    public static void possibleTextHelper(TrieNode node, String key, String[] possibleTexts, int[] idx) {
        if (idx[0] >= MAX_SUGGESTION_SIZE || node == null) {
            return;
        }
        if (node.isEndOfWord) {
            possibleTexts[idx[0]] = key;
            idx[0]++;
        }
        for (int i = 0; i < ALPHABET_SIZE; i++) {
            if (node.children[i] != null) {
                char ch = (char) ('A' + i);
                possibleTextHelper(node.children[i], key + ch, possibleTexts, idx);
            }
        }
    }

    // Search function to find suggestions
    public static String[] search(TrieNode root, String key) {
        TrieNode currentNode = root;
        for (int i = 0; i < key.length(); i++) {
            char ch = key.charAt(i);
            int index = ch - 'A';
            if (currentNode.children[index] == null) {
                return new String[MAX_SUGGESTION_SIZE];
            }
            currentNode = currentNode.children[index];
        }
        String[] possibleTexts = new String[MAX_SUGGESTION_SIZE];
        Arrays.fill(possibleTexts, "");
        int[] idx = {0};
        possibleTextHelper(currentNode, key, possibleTexts, idx);
        return possibleTexts;
    }

    // Get suggestions based on user input
    public static String getSuggestions(TrieNode root, String key) {
        String[] result = search(root, key);
        StringBuilder suggestionFormat = new StringBuilder();
        for (String s : result) {
            if (s != null && !s.isEmpty()) {
                suggestionFormat.append(s).append(", ");
            }
        }
        return suggestionFormat.toString();
    }

    // Insert words from a dictionary file into the Trie
    public static void insertDictionary(TrieNode root, String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String word;
        while ((word = br.readLine()) != null) {
            insert(root, word.trim().toUpperCase());
        }
        br.close();
    }

    // Main function to handle user interaction in console
    public static void windowsOperation(TrieNode root) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String userInput = "";
        String[] result = new String[MAX_SUGGESTION_SIZE];

        while (true) {
            System.out.print(">> " + userInput);
            String input = reader.readLine();

            if (input.equals("\r")) {
                break;
            } else if (input.equals("\t")) {
                userInput = result[0];
            } else if (input.equals("\b")) {
                if (userInput.length() > 0) {
                    userInput = userInput.substring(0, userInput.length() - 1);
                }
            } else {
                userInput += input;
            }

            String suggestionFormat = getSuggestions(root, userInput);
            System.out.println("\nSuggestions: " + suggestionFormat);
        }
    }

    public static void main(String[] args) throws IOException {
        TrieNode autoCompletionNode = new TrieNode();
        String fileName = "J:/AutoComplete/words.txt"; // Path to the dictionary file

        insertDictionary(autoCompletionNode, fileName);

        System.out.print(">> ");
        windowsOperation(autoCompletionNode);
    }
}
