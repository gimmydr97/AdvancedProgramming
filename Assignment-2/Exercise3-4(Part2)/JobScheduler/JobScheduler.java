import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

//calls that implemets the abstract class JobSchedulerStrategy
public class JobScheduler extends JobSchedulerStrategy<String,String>{

    
    //Emit asks the user for the absolute path of a directory where documents are stored.
    @Override
    public Stream<AJob<String,String>> emit() {
        System.out.println("Insert the absolute path :");

        Scanner s = new Scanner(System.in);
        String path = s.nextLine();
        s.close();
        Stream<AJob<String, String>> streamOfJobs = null;
        ArrayList<AJob<String,String>> jobs = new ArrayList<AJob<String,String>>();
        File folder = new File(path);
        // It visits the directory and creates a new job for each file ending with .txt in that directory
        if(folder.isDirectory()){
            for (final File file : folder.listFiles()) {
                String fn = file.getAbsolutePath();
                if (fn.endsWith(".txt")){
                    jobs.add(new Job(fn));
                }
            }
            streamOfJobs = jobs.stream();
        } 
            
        return streamOfJobs;
    }
    //Output should write the list of ciao keys and the number of words associated with each key 

    @Override
    public void output (Stream<Pair<String,List<String>>> stream) {
        try{
            //write take an iterator created from stram with a map that for each element of the stream return a string formed like:
            //"key of the element" "number of the associated values" 
            Files.write(Paths.get("count_anagrams.txt"),
                        (Iterable<String>)stream.map(x -> x.getKey() + " " + x.getValue().size())::iterator);
        }catch(IOException e){
            e.printStackTrace();
        }
       
    }

    public static void main(String[] args ){
        //create a JobScheduler (istance of JobSchedulerStrategy)
        JobSchedulerStrategy<String,String> jss = new JobScheduler(); 
        //assign to the costructor of the JobSchedulerContext the JobScheduler (and so the object delegated to implement the hook methods)
        JobSchedulerContext<String,String> jsc = new JobSchedulerContext<>(jss);
        //call on the JobSchedulerContext object the main method
        jsc.main();
        
    } 
    
}
