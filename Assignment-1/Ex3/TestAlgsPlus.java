import java.io.File;
import java.lang.reflect.*;
import java.nio.file.*;
import java.lang.annotation.*;
import java.util.List;

public class TestAlgsPlus {



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
        File algos = new File(args[0] + "/crypto/algos");
        for (File file : algos.listFiles()) {
            System.out.println(file.getName().substring(0,file.getName().indexOf(".")));

			Class<?> algo = Class.forName("crypto.algos." + file.getName().substring(0, file.getName().indexOf(".")));
            
            ////check if "algo" is a crypto algo or a crypto annotated algo
            boolean  ca = isCryptoAlgo(algo);
            boolean caa=  isCryptoAnnotateAlgo(algo);
            if(ca || caa){
                //if is a crypto annotated algo print ann
                if(caa && !ca){ System.out.println("ann");}
                try{
                    
                    //if "algo" is a crypto algo or a crypto annotated algo, 
                    //spone an istance of "algo" via his costructor method using reflection
                    Class<?>[] ArgsClass = new Class[]{ String.class};
                    Object[] Args = new Object[]{Registry.get(algo)};
                    Constructor<?> ctor = algo.getConstructor(ArgsClass);
                    Object instance = ctor.newInstance(Args);
                    
                    //retrive the name of encrypt and decrypth method of the "algo" via the auxiliar method getEncMethod and getDecMethod

                    String enc = getEncMethod(algo).getName();
                    String dec = getDecMethod(algo).getName();
                    
                    
                    System.out.println(enc + "_" + dec);

                    //instantiation of the encrypt and decrypth methods
                    Class<?>[] paramtypes = new Class[] { String.class } ;
                    Method encMethod = algo.getDeclaredMethod(enc,paramtypes);
                    Method decMethod = algo.getDeclaredMethod(dec,paramtypes);
                    
                    
                    List<String> wrds = Files.readAllLines(Paths.get(args[0] + "/crypto/secret.list"));
                    //for each word in the secret.list identify the encrypt and decrypt method of "algo" class
                        //  using reflection call the encrypto method of the istance on the "word" of the secret list 
                        //  on the resulted object using reflection call the decrypto method of the istance and check if is equal to "word"
                        //  if is no equal print the log of the encryption and decryption
                    for ( String wrd: wrds) {
                        
                        String encwrd = (String) encMethod.invoke(instance, new Object[] { wrd } );
                        String decwrd = (String) decMethod.invoke(  instance, new Object[] { encwrd });
                        if(!wrd.equals(decwrd) && !wrd.equals(decwrd.replaceAll("#",""))){
                            System.out.println("KO: "+ wrd + " -> " + encwrd + " -> " + decwrd);
                        }      
                    }
                }catch(Exception e){System.out.println(e.getCause().getClass());}
            //if "algo" isn't a crypto algo or a crypto annotated algo print "Enc/Dec methods not found"
            }else{
                System.out.println("Enc/Dec methods not found");
            }
		}  
    }
    
    //the same of ex2
    public static boolean isCryptoAlgo(Class<?> algo){

        boolean fencdec = false;
        boolean fcon= false;
        Method[] ms = algo.getDeclaredMethods();
        Constructor<?>[] ctors = algo.getConstructors();

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
        
        for (int i = 0; i < ctors.length; ++i) {
            if((ctors[i].getParameterTypes())[0].getName().equals("java.lang.String")){
                fcon = true;
            }       
        }       
               
        return fencdec && fcon;
    }

    //method that check if an crypto algo use annotation for identify his encrypt and decrypt methods
    public static boolean isCryptoAnnotateAlgo(Class<?> algo){
        //boolean variable for the checking phase related to the encrypt and decrypt method
        boolean fencdec = false;
        //boolean variable for the checking phase related to the constructor method
        boolean fcon= false;
        Method[] ms = algo.getDeclaredMethods();
        Constructor<?>[] ctors = algo.getConstructors();//retrive public contructors
        
        //if in there is just one method annotated with @Encrypt and just one with @Decrypt
        //the phase that check the enc and dec method has been passed
        if(howMuch(ms, "crypto.annot.Encrypt") == 1 && howMuch(ms, "crypto.annot.Decrypt") == 1){
            fencdec = true;
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

    //method that take as par. an array of metohd(of an algo) and an annotation "ann"
    //check how much method there are in the passed array "ms" that take an string as paramether
    // and are annoted by "ann" 
    public static int howMuch(Method[] ms, String ann ){

        int count = 0;
        for (Method m : ms){
            Annotation[] as = m.getDeclaredAnnotations();
            
            if((m.getParameterTypes())[0].getName().equals("java.lang.String") && as.length != 0){
                for(Annotation a: as){
                    
                    if(a.annotationType().getName().equals(ann)){
                        count++;
                    }
                    
                } 
            }
        }
        return count;    
    }

    //method that retrive the encrypt method of "algo" 
    //returns the first method whose name begins with "enc" or which is annotated with "@Encrypt"
    public static Method getEncMethod(Class<?> algo){
        Method [] methods = algo.getDeclaredMethods();
        for(Method method : methods){
           
            if (method.getName().startsWith("enc"))
                return method;
            Annotation[] anns = method.getDeclaredAnnotations();
            for(Annotation a: anns){
                if(a.annotationType().getName().equals("crypto.annot.Encrypt"))
                    return method;
            }          
        }
        return null;
    }
    //method that retrive the decrypt method of "algo"
    //returns the first method whose name begins with "dec" or which is annotated with "@Decrypt"
    public static Method getDecMethod(Class<?> algo){
        Method [] methods = algo.getDeclaredMethods();
        for(Method method : methods){
           
            if (method.getName().startsWith("dec"))
                return method;
            Annotation[] anns = method.getDeclaredAnnotations();
            for(Annotation a: anns){
                if(a.annotationType().getName().equals("crypto.annot.Decrypt"))
                    return method;
            }          
        }
        return null;
    }
}
    


