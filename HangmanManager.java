import java.util.*;

// Evelyn Salas
// CSE 143 AO with Stuart Reges
// Homework 4
// HangmanManager creates a game of hangman where the word is chosen as late as possible

public class HangmanManager {
    private Set<String> wordFamily; //keeps track of the potenital answers
    private int guessNum; //number of gueses in a game
    private SortedSet<Character> guessedLetters; //keeps track of guesses
    private String dashes; // holds place for letters have have not been guesse

   // Initializes the state of the game
   // @param dictionary takes in a collection of words and chooses a set for the game
   // @param length random integer that represents the length of the word
   // @param max random integer that represents the maximum number of guesses
   // @throws IllegalArgumentException if the length of the word is zero and below or
   // if the max is negative.
    public HangmanManager(Collection<String> dictionary, int length, int max){
        if (length < 1 || max < 0) {
            throw new IllegalArgumentException();
        } else {
            wordFamily = new TreeSet<String>();
            guessedLetters = new TreeSet<Character>();
            dashes = "";
            guessNum = max;
            for (String word : dictionary) {
                if (length == word.length()) {
                    wordFamily.add(word);
                }
            }
            for (int i = 0; i < length; i++) {
                dashes += "- ";
            }
        }
    }

    // provides access to the words that could be the answer
    // @return wordFamily, object contain potential answers
    public Set<String> words(){
        return wordFamily;
    }

    // Provides access to the number of guesses left
    // @return guessNum, a number representing guesses
    public int guessesLeft(){
        return guessNum;
    }

    // Gives the guessed characters
    // @return guessedLetters, object containing the letters guessed
    public Set<Character> guesses(){
        return guessedLetters;
    }

    // Represents the answer as a combination of guessed letters and dashes
    // @return dashes, the dashes that hold a place for unguessed letters in the word
    // @throws IllegalArgumentException if no words were generated
    public String pattern(){
        if(wordFamily.isEmpty()) {
            throw new IllegalStateException();
        } else {
            return dashes;
        }
    }

    // Helper method gives the pattern of dashes
    // @param char guess to see if guess is correct
    // @param String word to see if guess is correct
    // @return pattern the pattern for the dashes
    private String getDashes(char guess, String word){
        String pattern = "";
        int length = word.length();
        for(int i = 0; i < length; i++) {
            if (guess == word.charAt(i)) {
                pattern = pattern + guess + " ";
            } else {
                pattern = pattern + dashes.substring(i, i+2);
            }
            i += 2;
        }
        return pattern;
    }

    // Returns the frequency of a certain letter witin a word
    // @param char guess is the character who's frequency we want
    // @returns int count which represents the frequency of guess
    private int freq(char guess) {
        int count = 0;
        for (int i = 0; i < dashes.length(); i++) {
            char answer = dashes.charAt(i);
            if (guess == answer) {
                count++;
            }
        }
        if (count == 0) {
            guessNum--;
        }
        guessedLetters.add(guess);
        return count;
    }

    // Keeps a record of a player's guesses
    // @param char guess, the letter the player guessed
    // @return the number of times guessed letter appears in answer
    // @throws IllegalStatementException if guesses left is zero
    // @throws IllegalArgumentException if letter was guessed already
    public int record(char guess){
        if (guessNum < 1 || wordFamily.isEmpty()) {
            throw new IllegalStateException();
        } else if (guessedLetters.contains(guess) && !wordFamily.isEmpty()) {
            throw new IllegalArgumentException();
        }
        guessedLetters.add(guess);
        Map<String, Set<String>> currentFamily = new TreeMap<String, Set<String>>();
        for (String word : wordFamily) {
            String dashPattern = getDashes(guess, word);
            Set<String> set = new TreeSet<String>();
            if (currentFamily.containsKey(dashPattern)) {
                currentFamily.get(dashPattern).add(word);
            } else {
                currentFamily.put(dashPattern, set);
            }
        }
        if (!currentFamily.isEmpty()) {
            for(String pattern : currentFamily.keySet()) {
                int size = 0;
                if (currentFamily.get(pattern).size() > size) {
                    wordFamily.clear();
                    wordFamily.addAll(currentFamily.get(pattern));
                    dashes = pattern;
                    size = currentFamily.get(pattern).size();
                }
            }
        }
        return freq(guess);
    }
}