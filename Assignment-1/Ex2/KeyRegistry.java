import java.io.IOException;
import java.util.HashMap;

public class KeyRegistry {

    HashMap<Class<?>,String> Registry;

    public KeyRegistry() throws IOException, ClassNotFoundException{

        Registry = new HashMap<Class<?>,String>();
    
    }
    //method for add a pair(class of the crypto algo, key of the crypto algo)
    public void add (Class<?> c, String key){
            Registry.put(c,key);
    }
    //method for getting the key associated in the KeyRegistry with the crypto algo c 
    public String get (Class<?> c){
            return Registry.get(c);
    }
}
   
       
        
    

