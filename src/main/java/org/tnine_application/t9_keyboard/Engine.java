package org.tnine_application.t9_keyboard;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.io.*;
import java.util.*;

public class Engine {

    @FXML
    TreeMap<String, PriorityQueue<Word>> wordBank;
    private boolean t9On = true;
    private String[] lettersForSuggestion = {};
    private ArrayList<String> wordsForSuggestion = new ArrayList<>();

    @FXML
    private ListView suggestionList;

    @FXML
    private TextField textField;

    @FXML
    private Text sf1;
    @FXML
    private Text sf2;
    @FXML
    private Text sf3;
    @FXML
    private Button leftButton;
    @FXML
    private Button rightButton;

    @FXML
    private Button textDeleteButton;

    @FXML
    private RadioButton toggleT9;

    @FXML
    private Button eight;

    @FXML
    private Button five;

    @FXML
    private Button four;

    @FXML
    private Button nine;

    @FXML
    private Button one;

    @FXML
    private Button seven;

    @FXML
    private Button six;

    @FXML
    private Button three;

    @FXML
    private Button two;

    @FXML
    private Button zero;


    TreeMap<String, String> buttonComb = new TreeMap<>();
    String currentT9Word = "";
    boolean erase = false;
    final double TERM_FREQUENCY = 0.0037200000; // Term frequency för ordet "my"


    public Engine() {
        wordBank = new TreeMap<>();

        // Prepare load
        buttonComb.putIfAbsent("1", ".,?!'\"-@#&");
        buttonComb.putIfAbsent("2", "ABC");
        buttonComb.putIfAbsent("3", "DEF");
        buttonComb.putIfAbsent("4", "GHI");
        buttonComb.putIfAbsent("5", "JKL");
        buttonComb.putIfAbsent("6", "MNO");
        buttonComb.putIfAbsent("7", "PQRS");
        buttonComb.putIfAbsent("8", "TUV");
        buttonComb.putIfAbsent("9", "WXYZ");
        loadInitBank();
    }

    @FXML
    public void printSizeOfWordbank(){
        System.out.println("Size of Wordbank: "+ wordBank.size());
    }

    @FXML
    public void toggleT9(){
        t9On = !t9On;

        sf1.setText("");
        sf2.setText("");
        sf3.setText("");
    }

    @FXML
    public void onKeyPressed(ActionEvent actionEvent){
        leftButton.setDisable(true);
        if(wordsForSuggestion != null && wordsForSuggestion.size()<=3){
            rightButton.setDisable(true);
        }
        Button button =  (Button) actionEvent.getSource();

        String text = button.getText();
        char num = text.charAt(0);

        // Handle pressing non functioning buttons.
        if(num == '*' || num == '#') return;

        if(num == '0'){
            // TODO: Justera så att hela ordet hämtas och läggs till i wordbank.
            if(!t9On){
                // Hämta och skapa nytt ord
                String[] words = textField.getText().split(" ");
                String newWord = words[words.length-1];
                String newWordTo9 = wordToNine(newWord);

                // Stops creating new word-element if wordbank already contains the word.
                for (Word word : wordBank.get(newWordTo9)) {
                    if(word.getWord().equals(newWord)){
                        erase = true;
                        return;
                    }
                }
            }
            else{
                // Disable rightbutton if letters is smaller then 3.
                rightButton.setDisable(lettersForSuggestion.length <= 2);

                String temp = textField.getText();
                if(wordBank.get(currentT9Word) != null && !wordBank.get(currentT9Word).isEmpty()){
                    textField.setText(temp + " " + getmostRelevantWords(currentT9Word).get(0));

                    sf1.setText("");
                    sf2.setText("");
                    sf3.setText("");
                }

            }
            suggestionList.getItems().clear();
            wordsForSuggestion.clear();
            erase = true;
            return;
        }



        sf1.setText("");
        sf2.setText("");
        sf3.setText("");


        if(erase){
            erase = false;
            currentT9Word = "";
        }

        suggestionList.getItems().clear();
        // Handle writing if T9 is toggled off.
        if(!t9On){
            writeWithoutT9(num);
        }
        else{
            writeWithT9(num);
        }

    }

    private void writeWithoutT9(char num) {
        String letters = buttonComb.get(String.valueOf(num));
        lettersForSuggestion = letters.split("");
        if(lettersForSuggestion.length > 0)
            sf1.setText(lettersForSuggestion[0].toLowerCase());
        if(lettersForSuggestion.length > 1)
            sf2.setText(lettersForSuggestion[1].toLowerCase());
        if (lettersForSuggestion.length > 2)
            sf3.setText(lettersForSuggestion[2].toLowerCase());

        if(lettersForSuggestion.length < 4)
            rightButton.setDisable(true);


    }

    private void writeWithT9(char num) {
        currentT9Word += String.valueOf(num);

        wordsForSuggestion = getmostRelevantWords(currentT9Word);

        suggestionList.getItems().addAll(wordsForSuggestion);

        if(wordsForSuggestion == null) return;
        wordsForSuggestion.forEach(System.out::println);

        if(wordsForSuggestion.size() > 3)
            rightButton.setDisable(false);

        if (!wordsForSuggestion.isEmpty()) {
            sf1.setText(wordsForSuggestion.get(0));
            if(wordsForSuggestion.size()>=2){
                sf2.setText(wordsForSuggestion.get(1));
            }
            if (wordsForSuggestion.size() >= 3) {
                sf3.setText(wordsForSuggestion.get(2));
            }
        }
        //leftButton.setDisable(true);
    }

    @FXML
    public void suggestionToTextField(MouseEvent event){
        String word = suggestionList.getSelectionModel().getSelectedItem().toString().trim();

        if(word.isEmpty())
            return;
        String textBefore = textField.getText();
        if(t9On)
            textField.setText(textBefore+ " " + word + " ");
        else{
            textField.setText(textBefore + word);
        }
        currentT9Word = "";
        sf1.setText("");
        sf2.setText("");
        sf3.setText("");
        leftButton.setDisable(true);
        if(t9On)
            addUserWeightToWord(word);
    }

    @FXML
    public void deleteText(){
        //TODO: Implement delting text from textfield
        int pos = textField.caretPositionProperty().get();
        String text = textField.getText();
        if(!text.isEmpty()){
            textField.setText(text.substring(0, text.length()-1));
            sf1.setText("");
            sf2.setText("");
            sf3.setText("");

        }
    }

    private String wordToNine(String word) {
        String nineWord = "";

        for(String l : word.split("")){
            for (Map.Entry<String, String> entry: buttonComb.entrySet()){
                if(entry.getValue().contains(l.toUpperCase())){
                    nineWord += String.valueOf(Integer.parseInt(entry.getKey()));
                }
            }
        }
        return nineWord;
    }

    // Add point to used frequency for word.
    private void addUserWeightToWord(String word) {
        String nineWord = wordToNine(word);

        PriorityQueue<Word> queue = wordBank.get(nineWord);
        System.out.println(queue.size());

        Iterator iterator = queue.iterator();
        boolean done = false;
        while(iterator.hasNext() && !done){
            Word w = (Word) iterator.next();
            if(w.getWord().equals(word)){
                w.addUsedFreq();
                Word newWord = new Word(word, w.getTermFrequency(), w.getUsedFreq());
                System.out.println(wordBank.get(nineWord).remove(w));//remove old word
                System.out.println(wordBank.get(nineWord).add(newWord));// add and priorities new word

                System.out.println("Added point to word: " + newWord.getWord() + " - " + newWord.getUsedFreq() + ", " + newWord.getWeight());
                System.out.println(wordBank.get(nineWord).peek());
                done = true;
            }
        }
        if(!done){
            System.out.println("Could not find word");
        }
    }

    @FXML
    public void changeSuggBar(ActionEvent event){
        //leftButton.setDisable(true);
        Button button = (Button)event.getSource();
        String text = button.getText();

        if(!t9On){

            if(text.equals(">")){
                sf1.setText("");
                sf2.setText(lettersForSuggestion[3]);
                sf3.setText("");
                leftButton.setDisable(false);
            }
            else{
                sf1.setText(lettersForSuggestion[0]);
                sf2.setText(lettersForSuggestion[1]);
                sf3.setText(lettersForSuggestion[2]);
                rightButton.setDisable(false);
                leftButton.setDisable(true);
            }

            return;
        }
        if(wordsForSuggestion == null) return;

        int indexSf1 = -1;
        int size = wordsForSuggestion.size();

        // get index of sf1
        String sf1text = sf1.getText();
        for (int i = 0; i < size; i++) {
            if(wordsForSuggestion.get(i).equals(sf1text)){
                indexSf1 = i;
                break;
            }
        }

        // Move to next suggestion
        if(text.equals("<")){
            if((indexSf1 - 3) - size >= 0)
                leftButton.setDisable(true);
            sf1.setText(wordsForSuggestion.get(indexSf1-3));
            sf2.setText(wordsForSuggestion.get(indexSf1-2));
            sf3.setText(wordsForSuggestion.get(indexSf1-1));
            rightButton.setDisable(false);
        }
        else if(text.equals(">")){
            leftButton.setDisable(false);
            if((indexSf1 + 5) - size < 0 ){
                rightButton.setDisable(true);
            }

            sf1.setText(wordsForSuggestion.get(indexSf1+3));
            sf2.setText(wordsForSuggestion.get(indexSf1+4));
            sf3.setText(wordsForSuggestion.get(indexSf1+5));
        }
        else{
            throw new IllegalCallerException();
        }
    }

    private ArrayList<String> getmostRelevantWords(String currentT9Word) {

        PriorityQueue<Word> temp = wordBank.get(currentT9Word);
        if(temp == null) return null;
        ArrayList<String> words = new ArrayList<>(temp.size());

        Iterator iterator = temp.iterator();
        while(iterator.hasNext()){
            Word item = (Word) iterator.next();
            words.add(item.getWord());
        }
        return words;
    }


    @FXML
    public boolean loadInitBank(){
        File savedWords = new File("src/main/resources/data/word_savings.txt");
        if(savedWords.exists()){
            loadData();
            return true;
        }

        String fileName = "src/main/resources/data/wordsFixed.txt";
        try{
            FileReader fileReader = new FileReader(fileName);
            BufferedReader reader = new BufferedReader(fileReader);

            int index;
            String line;
            while((line = reader.readLine()) != null){
                convertToTNineAndAdd(line);
            }
            for (PriorityQueue value : wordBank.values()) {
                System.out.println(value.size());
            }
            fileReader.close();
            reader.close();
        }
        catch(FileNotFoundException e){
            System.out.printf("%s not found%n", fileName);
            return false;
        }
        catch (IOException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void convertToTNineAndAdd(String line) {
        int index;
        index = 0;
        double tf = 0.0000000001;
        line = line.trim().toUpperCase();
        String[] lineParts = line.split(",");
        String word = lineParts[0].trim().toUpperCase();

        if(lineParts.length >= 2){
            tf += Double.valueOf(lineParts[1].trim());
        }

        String tNineWord = "";


        for(String l : word.split("")){
            for (Map.Entry<String, String> entry: buttonComb.entrySet()){
                if(entry.getValue().contains(l)){
                    tNineWord += String.valueOf(Integer.parseInt(entry.getKey()));
                    index += 1;
                }
            }
        }

        if(!tNineWord.isEmpty()){
            String key = tNineWord;
            if(!wordBank.containsKey(key)){
                wordBank.put(key, new PriorityQueue<>());
            }

            Word w = new Word(word, tf);
            wordBank.get(key).add(w);
        }
    }

    @FXML
    public void loadData(){
        String fileName = "src/main/resources/data/word_savings.txt";
        try{
            FileReader fileReader = new FileReader(fileName);
            BufferedReader reader = new BufferedReader(fileReader);


            int index;
            String line;
            while((line = reader.readLine()) != null){
                String[] parts = line.split(",");

                String word = parts[0].trim();
                String tf = parts[1].trim();
                String usedFreq = parts[2].trim();
                Word newWord = new Word(word, Double.valueOf(tf), Integer.parseInt(usedFreq));
                String wordTo9 = wordToNine(word);
                if(!wordBank.containsKey(wordTo9)){
                    wordBank.put(wordTo9, new PriorityQueue<Word> ());
                }
                wordBank.get(wordTo9).add(newWord);
            }

            fileReader.close();
            reader.close();
        }
        catch(FileNotFoundException e){
            System.out.printf("%s not found%n", fileName);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void saveData(){
        Set<String> keys = wordBank.keySet();
        PrintWriter writer = null;
        String line = "";
        int count = 0;

        try{

            File file = new File("src/main/resources/data/word_savings.txt");
            if(file.exists()){
                file.delete();
            }

            writer = new PrintWriter(new FileWriter("src/main/resources/data/word_savings.txt"));
            for (String key : keys) {
                PriorityQueue<Word> queue = wordBank.get(key);
                for (Word word : queue) {
                    line = word.getWord() + ", " + String.format("%.10f",word.getTermFrequency()) + ", " + word.getUsedFreq();
                    writer.print(line);
                    writer.println();


                }
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        finally {
            writer.close();
        }

    }


}
