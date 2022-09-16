import Dictionary(empty, insert, Dictionary, keys, lookup, eq, merge)
import System.IO ()
import Data.Char (toLower)
import Data.List (sort)



{-function readDict that reads a text file and returns a new Dictionary after adding each word of the file using its ciao as key-}
readDict :: FilePath -> IO (Dictionary [Char] [Char])
readDict file = do  
            {-after reading he file i create a list of all the words in the file ws
              and an empty dictionary dict 
              and return the result of the euxiliar function fillDict on this two parameters-}
            contents <- readFile file
            let ws = words contents
                dict = Dictionary.empty in 
                return (fillDict dict ws)

{-function that fill the dictionary passed as first argument with the words passed as second argument-}
fillDict :: Dictionary [Char] [Char] -> [[Char]] -> Dictionary [Char] [Char]
{-base case: having an empty list of words return the dictionary passed as first argumet-}
fillDict dict [] = dict
{-for each word in the list of words (did through the foldl) :
    creation of the ciao of the word: 
             +turn all the letters of the word into their lowercase version through mapping toLower on all the letters of the word  
             +sort the result of the mapping
    insert the couple ("ciao" of the word, word) in the dictionary-}
fillDict dict ws = foldl (\ dict w -> Dictionary.insert dict (sort (map toLower w)) w) dict ws


{-function that given a dictionary and a file name, writes in the file , one per line, 
  each key of the dictionary together with the length of the list of values associated with the key-}
writeDict :: (Eq a, Show a) => Dictionary a b -> FilePath -> IO [()]
writeDict  dict file = do
                    {-I take keys of dict through the keys funtion-}
                    let ls = keys dict in 
                        {-using a map for monadic action, 
                          for each element of the list of keys,
                            add to the file one line with the key and the length of his list of values-}
                        mapM( \ l -> case Dictionary.lookup dict l of
                                  Nothing -> appendFile file ""
                                  Just vs -> appendFile file  (show l ++ " " ++ show (length vs) ++ "\n")
                            ) ls
                

main :: IO ()
main = do 
    
     d1 <- readDict "aux_files/anagram.txt"
     d3 <- readDict "aux_files/anagram-s2.txt"
     d2 <- readDict "aux_files/anagram-s1.txt"
     d4 <- readDict "aux_files/margana2.txt"
     
     if Dictionary.eq d1 d4 then putStr "d1 and d4 are equal\n"
     else if keys d1 /= keys d4  then putStr "the keys of d1 and d4 are non-equal\n"
     else if not(Dictionary.eq d1 (Dictionary.merge d2 d3)) then putStr "d1 differs from the merge of d2 and d3\n"
     else do 
         writeDict d1 "anag-out.txt"
         writeDict d4 "gana-out.txt"
         putStr "everything went fine\n"
               
     