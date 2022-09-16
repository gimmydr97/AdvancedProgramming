import functools
import threading
import time
import statistics

#bench: invoking a function func decorated by bench, 
#       func is executed several times in parallel on several threads, 
#       and the whole is repeated a few times returning the average time of execution and the variance.
#parameters
#   func: function decorated by bench so executed several times according with the other parameters
#   n_threads: The number of threads (default: n_threads = 1)
#   seq_ iter: The number of times func must be invoked in each thread (default: seq_iter= 1)
#   iter: The number of times the whole execution of the n_threads threads, each invoking seq_iter times func , is repeated.
#         For each executi on the execution time has to be computed (default iter = 1)
def bench(func, n_threads=1, seq_iter=1, iter=1):
    #definition of the dict to be returned
    IterTimetable={}
    
    @functools.wraps(func)
    #the decorator return a type dict
    def wrapper_bench(*args, **kwargs)-> dict:

        mean = 0
        variance = 0
        #worker for the threads that takes the func to execute seq_iter times sequentially
        def worker(func, i):
            for j in range(seq_iter):
                print(str(i)+"_"+str(j))
                func(*args, **kwargs)
        
        # loop for execute iter time the whole execution
        for i in range(iter):
            #Start the timer
            start_time = time.perf_counter()
            print(start_time)
            # Create the n_threads threads passing him as target argument the worker function defined above
            threads = [threading.Thread(target= worker(func,i)) for i in range(n_threads)]
            # Start all the workers
            [thread.start() for thread in threads]
            # Wait for all the tasks to be processed
            [thread.join() for thread in threads]
            # Take the end time 
            end_time = time.perf_counter()
            print(end_time)
            # Insert in the iter table the execution time for this iteration
            IterTimetable[i] = end_time - start_time
            
        mean = statistics.mean(list(IterTimetable.values()))
        #this check is necessary because statics.variance don't work on a list of only one element
        if (iter != 1):
            variance = statistics.variance(list(IterTimetable.values()))
        
        return {'fun': func.__name__, 
                'args': args, 
                'n_threads': n_threads,
                'seq_iter': seq_iter, 
                'iter': iter, 
                'mean': mean, 
                'variance': variance
                }      
    return  wrapper_bench

def test(iter, fun, *args):

    #16 times fun on args on a single thread
    decFun = bench(fun,1,16,iter)
    #<fun>_<args>_<n_threads>_<seq_iter>
    fileName = str(fun.__name__) + "_" + str(args) +"_"+ str(1) +"_"+ str(16)+".txt"
    file = open(fileName , "w")
    file.write(str(decFun(*args)))
    file.close()

    #8 times fun on args on 2 threads
    decFun = bench(fun,2,8,iter)
    #<fun>_<args>_<n_threads>_<seq_iter>
    fileName = str(fun.__name__) + "_" + str(args) +"_"+ str(2) +"_"+ str(8)+".txt"
    file = open(fileName , "w")
    file.write(str(decFun(*args)))
    file.close()
    
    #4 times fun on args on 4 threads
    decFun = bench(fun,4,4,iter)
    #<fun>_<args>_<n_threads>_<seq_iter>
    fileName = str(fun.__name__) + "_" + str(args) +"_"+ str(4) +"_"+ str(4)+".txt"
    file = open(fileName , "w")
    file.write(str(decFun(*args)))
    file.close()

    #2 times fun on args on 8 threads
    decFun = bench(fun,8,2,iter)
    #<fun>_<args>_<n_threads>_<seq_iter>
    fileName = str(fun.__name__) + "_" + str(args) +"_"+ str(8) +"_"+ str(2)+".txt"
    file = open(fileName , "w")
    file.write(str(decFun(*args)))
    file.close()
    



def just_wait(n): # NOOP for n/10 seconds 
    time.sleep(n * 0.1) 

def grezzo(n): # CPU intensive 
    for i in range(2**n):
        pass

def main():
    test(5,just_wait,10)
    test(5,grezzo,20)
if __name__ == '__main__':
        main()

# using the first function just_wait with parameter n=10 (so every sleep is one second) 
# we can see that the four test (1 th. 16 rep. each),(2 th. 8 rep. each)(4 th. 4 rep. each)(8 th. 2 rep. each),
# everyone so, over a total of 16 function repetitions, have more or less similar duration which is around 16 seconds 
# with some increasing milliseconds of overhead as the number of threads used for testing increases.
# by requiring 16 function repetitions each lasting one second the tests showed that,
# with the use of multiple threads however there was no decrease in the time required due to parallelism of threads.
# as expected knowing the concurrency choices in python,
# the times remained "sequential" even with the use of multiple threads in parallel.

# using the second function grezzo, that it is supposed to be more expensive from the point of view of the usiage of cpu.
# The results were much the same as the previous function, 
# with an accentuation of performance degradation in terms of time as the number of threads used increased 
# as much as 3 seconds more than the "sequential" execution of the first test.
# This degradetion is probably due to the usage of the GIL that increase the overhead in this situation. 