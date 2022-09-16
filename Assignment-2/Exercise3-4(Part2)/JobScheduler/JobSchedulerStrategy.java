import java.util.List;
import java.util.stream.Stream;
//following the strategy design pattern, this is the Strategy abstract class
// in which we include all the hot spots (emit and output as abstract methods)
//whose implementation will be delegated to an object that will implement this strategy abstract class
public abstract class JobSchedulerStrategy <K,V> {

    public abstract Stream<AJob<K,V>> emit();
    
    public abstract  void output(Stream<Pair<K,List<V>>> stream );
}
