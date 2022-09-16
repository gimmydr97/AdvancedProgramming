import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//following the strategy design pattern, this is the concrete Strategy context class
//in which we include all the frozen spots (compute, collect, main) and their implementation 
public final class JobSchedulerContext<K,V> {
    JobSchedulerStrategy<K,V> jsc;

    //costructor
    public JobSchedulerContext (JobSchedulerStrategy<K,V> jsc){
         this.jsc = jsc;
    }

    //compute method that execute the stream of jobs receved as parameter and return the concatenation 
    //of the output of the jobs
    public Stream<Pair<K,V>> compute (Stream<AJob<K,V>> job){
        //use the function flatMap that for each element of the stream call the abstract method execute of AJob 
        //fattMap in addiction concatenate also all the computed result  
        Stream<Pair<K,V>> compJob = job.flatMap(j -> j.execute());
        return compJob;
    }

    //collect , which takes as input the output of compute and groups all the pairs with
    //the same keys in a single pair, having the same key and the list of all values;
    public Stream<Pair<K,List<V>>> collect (Stream<Pair<K,V>> compJob ){
        //i used the function collect with an groupingBy collector that that group the input element
        //   -according to the classification function that in this case is the getKey of the Pair class
        //   -then apply a reduction operation on the values associated with the keys using the specified downstream Collector
        //    that in this case is the collector that accumulate all the values associated with the corrisponding keys
        //   -in the end  The Map produced by the Collector is created with the supplied Mapfactory function LinkedHashMap::new.    
        Stream<Pair<K,List<V>>> collJob = compJob.collect(Collectors.groupingBy(Pair::getKey, 
                                                                                LinkedHashMap::new, 
                                                                                Collectors.mapping(Pair::getValue,
                                                                                                   Collectors.toList()
                                                                                                   )
                                                                                )
                                                         ).entrySet()
                                                          .stream()
        //operation for convert the resulted Stream<Map.Entry<K,List<V>>> in a Stream<Pair<K,List<V>>> 
                                                          .map(e -> new Pair<>(e.getKey(), e.getValue()));
        return collJob;
    }
    
    public void main(){
        Stream<AJob<K,V>> step1 =  this.jsc.emit();
    
        Stream<Pair<K,V>> step2 = compute(step1);

        Stream<Pair<K,List<V>>> step3 = collect(step2);

        this.jsc.output(step3);
    }
}
