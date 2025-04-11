package org.tnine_application.t9_keyboard;

import java.math.BigDecimal;

public class Word implements Comparable{
    private final String word;
    private int usedFreq;
    private final double termFrequency;
    private double recentlyBoost;
    private BigDecimal weight;

    public Word(String word, double termFrequency, int usedFreq){
        this.word = word.toLowerCase();
        this.usedFreq = usedFreq;
        this.termFrequency = termFrequency;
        this.recentlyBoost = 1.0;
        this.weight = BigDecimal.valueOf(0.000000000001);
        calculateWeight();
    }

    public Word(String word, double termFrequency){
        this(word, termFrequency, 0);
    }

    /*
     *   usedFreq = ( amount of times used / 100 ) * 0.7
     *   termFrequency = constant from file * 0.2
     *   recentlyBoost = ( normal 1.0, 2.0 if in 50 latest used) * 0.1
     */
    private void calculateWeight() {
        weight = BigDecimal.valueOf(((usedFreq ) * 0.8 + (termFrequency * 10000  * 0.2)));// + recentlyBoost * 0.1;
    }

    public void addUsedFreq(){
        usedFreq += 1;
        calculateWeight();
    }

    public void addRecentlyBoost(){
        recentlyBoost = 2;
        calculateWeight();
    }

    public void removeRecentlyBoost(){
        recentlyBoost = 1;
        calculateWeight();
    }

    public String getWord() {
        return word;
    }

    public BigDecimal getWeight(){
        return weight;
    }

    public int getUsedFreq(){
        return usedFreq;
    }

    public double getTermFrequency(){
        return termFrequency;
    }

    @Override
    public String toString() {
        return "[" + getWord() + " : " + getWeight() + "]";
    }

    @Override
    public int compareTo(Object o) {
        if(o instanceof Word){

            // OBS! Bytte plats på < och >
            if(this.weight.compareTo(((Word) o).weight) > 0){
                return -1;
            }
            else if(this.weight.compareTo(((Word) o).weight) < 0){
                return 1;
            }
            else{
                if(this.usedFreq < ((Word) o).usedFreq){
                    return -1;
                }
                else if(this.usedFreq > ((Word) o).usedFreq){
                    return 1;
                }
                else{
                    return 0;
                }
            }
        }
        else{
            throw new ClassCastException("Cannot compare MyClass with " + o.getClass().getName());
        }
    }
}
