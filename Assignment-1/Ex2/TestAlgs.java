import java.io.File;
import java.lang.reflect.*;
import java.nio.file.*;
import java.util.List;

public class TestAlgs {



    public static void main(String args[]) throws Exception{
        
        File f = null;
        if (args.length ==1 ){
             f = new File(args[0] + "/crypto");
             if(f.isDirectory() == false) System.exit(-1);
        }
        else throw new Exception("wrong number of arguments");
        
        
        //spone an istance of keyRegistry
        KeyRegistry Registry = new KeyRegistry();

        Path path = Paths.get(args[0] +"/crypto/keys.list");
        List<String> lines = Files.readAllLines(path);

        //for each line of the keys.list add the pair(algo, key) in the registry
        for(int i = 0; i < lines.size(); i++){
            String line = lines.get(i);
            //System.out.println(line);
            Registry.add(Class.forName(line.substring(0, line.indexOf(" "))),
                         line.substring(line.indexOf(" ")+1,line.length()));
            
        }
        //for each <algo>.class in the algos directory spone an object of type class of the class <algo>.class
        //and check if is a crypto algo
        File algos = new File( args[0] +"/crypto/algos");
        for (File file : algos.listFiles()) {
            System.out.println(file.getName().substring(0,file.getName().indexOf(".")));
			Class<?> algo = Class.forName("crypto.algos." + file.getName().substring(0, file.getName().indexOf(".")));
            
            if(isCryptoAlgo(algo)){
                
                
                //if "algo" is a crypto algo, spone an istance of "algo" via his costructor method using reflection
                try{   
                        Class<?>[] ArgsClass = new Class[]{ String.class};
                        Object[] Args = new Object[]{Registry.get(algo)};
                        Constructor<?> ctor = algo.getConstructor(ArgsClass);
                        Object instance = ctor.newInstance(Args);

                        
                            
                        String enc = (algo.getDeclaredMethods())[0].getName();
                        String dec = (algo.getDeclaredMethods())[1].getName();

                        if(!enc.startsWith("enc")){
                            String temp = enc;
                            enc = dec;
                            dec = temp;
                        }
                        
                        Class<?>[] paramtypes = new Class[] { String.class } ;
                        Method encryMethod = algo.getDeclaredMethod(enc,paramtypes);
                        Method decMethod = algo.getDeclaredMethod(dec,paramtypes);

                        List<String> wrds = Files.readAllLines(Paths.get(args[0] + "/crypto/secret.list"));
                        //for each word in the secret.list identify the encrypt and decrypt method of "algo" class
                        //  using reflection call the encrypto method of the istance on the "word" of the secret list 
                        //  on the resulted object using reflection call the decrypto method of the istance and check if is equal to "word"
                        //  if is no equal print the log of the encryption and decryption
                        for ( String wrd: wrds) {
                        
                                String encwrd = (String) encryMethod.invoke(instance,new Object[] { wrd });
                                String decwrd = (String) decMethod.invoke(instance,new Object[] { encwrd });
                                if(!wrd.equals(decwrd) && !wrd.equals(decwrd.replaceAll("#",""))){
                                    System.out.println("KO: "+ wrd + " -> " + encwrd + " -> " + decwrd);
                                }
                                
                        }
                }catch(Exception e){System.out.println(e);}
            //if "algo" isn't a crypto algo print "Enc/Dec methods not found"
            }else{
                System.out.println("Enc/Dec methods not found");
            }   
		}  
    }
    
//auxiliar method that check if an algo is an crypto algo
    public static boolean isCryptoAlgo(Class<?> algo){
        //boolean variable for the checking phase related to the encrypt and decrypt method
        boolean fencdec = false;
        //boolean variable for the checking phase related to the constructor method
        boolean fcon= false;
        Method[] ms = algo.getDeclaredMethods();
        Constructor<?>[] ctors = algo.getConstructors();

        //the algorithm can present the encrypt method both before and after the decrypt method
        // so need to check both possibilities
        //both possibilities in addition incorporate control over the string type parameter
        //if "fencdec" is setted to true the phase related to the check on the enrypth and decrypth methods has been passed
        if( ms[0].getName().startsWith("enc") &&
            (ms[0].getParameterTypes())[0].getName().equals("java.lang.String")){
                        if( ms[1].getName().startsWith("dec") &&
                            (ms[1].getParameterTypes())[0].getName().equals("java.lang.String")){
                                fencdec = true;
                        }
        }
        else if( ms[0].getName().startsWith("dec") &&
                 (ms[0].getParameterTypes())[0].getName().equals("java.lang.String")){
                        if( ms[1].getName().startsWith("enc") &&
                            (ms[1].getParameterTypes())[0].getName().equals("java.lang.String")){
                                fencdec = true;
                        }
        }
        //if there is a constructor method that have one string paramether "fcon" is setted to true 
        //so the phase related to the check of the costructor has been passed 
        for (int i = 0; i < ctors.length; ++i) {
            if((ctors[i].getParameterTypes())[0].getName().equals("java.lang.String")){
                fcon = true;
            }       
        }       
               
        return fencdec && fcon;
    }

   
}
