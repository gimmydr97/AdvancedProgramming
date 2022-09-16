import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Stream;

//class that implementh the generic abstract clss AJob
public class Job extends AJob<String,String> {

    String fileName;
    public Job (String fileName){
        this.fileName = fileName;
    }
    //override of the abstract method execute from AJob with in this implementation 
    //must read the file, and it must return a stream containing all pairs of
    //the form (ciao(w), w), where w is a word of the file satisfying the above properties.
    @Override
    public Stream<Pair<String,String>> execute(){
        ArrayList<Pair<String,String>> lw = new ArrayList<Pair<String,String>>();
        File file = new File(fileName);
        try{
            Scanner words = new Scanner(file);
            while (words.hasNext()) {
                String word  = words.next();
                //selection of words to be included in the list
                word.replace("[,.;:?^!\"']"," ");
                if(word.length() > 3 && !word.matches(".*[^a-zA-Z0-9].*")){
                    char[] charWord = word.toLowerCase().toCharArray();
                    //creation of the "cioa" of word
                    Arrays.sort(charWord);
                    String ciao = new String(charWord);
                    //adding the couple (ciao(w), w) to the ArrayList
                    lw.add(new Pair<String,String>(ciao,word));
                }
            }
            words.close();
        }catch(FileNotFoundException e){e.printStackTrace();}; 

        //creationn of the stream on the ArrayList
        return lw.stream();
    }
}
