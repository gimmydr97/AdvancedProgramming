{-# OPTIONS_GHC -Wno-unrecognised-pragmas #-}
{-# HLINT ignore "Use newtype instead of data" #-}

module Dictionary
(  Dictionary
,  empty
,  eq
,  insert
,  Dictionary.lookup
,  keys
,  values
,  merge
) where
  
import Data.List (sort, sortBy, nub)
import Distribution.Simple.Setup (trueArg)

data Dictionary a b = Dict [(a, [b])]
    deriving (Show)

{-the empty costructor that return the empty Dictionary-}
empty :: Dictionary a b
empty = Dict []


instance (Eq a ,Eq  b) => Eq (Dictionary a b) where
(==) (Dict[]) (Dict[]) = True
(==) (Dict dict1) (Dict dict2) = eq (Dict dict1) (Dict dict2)


{--}
{-the eq operator that return true if the two Dictionaries are equal false otherwise-}
eq :: (Eq b, Ord a2) => Dictionary a2 b -> Dictionary a2 b -> Bool
{-base case: return True if the two Dictionaries are empty-}
eq (Dict[]) (Dict[]) = True
{-otherwise, sort the two dictionaries with an auxiliar function dictSort and retrive the list of keys of the two dictionaries-}
eq (Dict dict1) (Dict dict2) =
  let d1 = dictSort (Dict dict1)
      d2 = dictSort (Dict dict2)
      ks1= keys d1
      ks2= keys d2 in
{-if the two list of keys are different return false otherwise call the inspect auxiliar function on the two sorted dictionaries-}
{-the preliminary check only on the two list of keys is done because if they are not equal we avoid scrolling through all the lists of the single key -} 
  if not( ks1 Prelude.== ks2) then False
                           else inspect d1 d2 
           

inspect :: Eq b => Dictionary a1 b -> Dictionary a2 b -> Bool
{-base case: if the two Dictionaries are empty-}
inspect (Dict[]) (Dict[])  = True 
{-i took a couples (k1,vs1) of d1 and (k2,vs2) of d2. From the check on the keys we know that k1 and k2 are equal-}
{-so we just have to check that vs1 and vs2 are equals through the isPresent auxiliar function-}
{-if isPresent return False, so vs1 and vs2 are not equals, return False, otherwise, continue to scroll the two Dictionaries-}
inspect (Dict((k1,vs1):xs1)) (Dict((k2,vs2):xs2)) = if not(isPresent vs1 vs2) then False
                                                                              else inspect  (Dict xs1) (Dict xs2)
{-in any other case return False-}                                                                              
inspect _ _  = False

isPresent :: (Foldable t, Eq a) => [a] -> t a -> Bool
{-base case: l1 is empty -> every element of l1 is an element of l2 -> return True -}
isPresent [] l2 = True
{-if l is not an element of l2 return False otherwise continue to check the two lists-}
isPresent (l:l1) l2 = if l `notElem` l2 then False else isPresent l1 l2  


dictSort :: Ord a => Dictionary a b -> Dictionary a b
{-base case: having as parameter an empty dictionary return an empty dictionary-}
dictSort (Dict[]) = Dict[]
{-sorting done only by comparing the dictionary keys using the function sortBy -}
dictSort (Dict dict) = let sl = sortBy (\(a,_) (b,_) -> compare a b)  dict in Dict sl

{-function used for insert an element (k,v) in a dictionary-}
insert :: Ord a => Dictionary a b -> a -> b -> Dictionary a b
{-base case: insert a caple (k,v) in an empty dictionary-}
insert (Dict []) k v = Dict [(k,[v])]
{-using lookup function we see if k is already a key of the dictionary-}
insert (Dict dict) k v = case Dictionary.lookup (Dict dict) k of
  {-if not, concatenate the couple (k,[v]) with the other couples of (k,[v]) already present in the dictionary-}  
  Nothing -> Dict ((k,[v]):dict)
  {-if k is already in dictionary as a key c, using the higher order function map to find the list of c and concatenate v to it -}
  Just vs -> let l = map (\(c,vs)-> if c Prelude.== k then (c,v:vs) else (c,vs)) dict 
             in Dict l

{-function used to find a couple with k as key in the dictionary. If k is present  in the dict return his list of values-}
lookup :: Eq t => Dictionary t b -> t -> Maybe [b]
{-base case: if the dictionary is empty return Nothing -}
lookup (Dict []) k = Nothing 
{-otherwise scroll the dictionary and, if find the element k as a key of dictionary, return his list of values-}
lookup (Dict ((c, vs):xs)) k = if c Prelude.== k  then Just vs else Dictionary.lookup (Dict xs) k 

{-function that return the list of dictionary keys-}
keys :: Dictionary a b -> [a]
{-base case: return a empty list for an empty dictionary-}
keys (Dict []) = []
{-scrolling the dictionary I build the list of keys by adding the key of the element I am scrolling for each recursion-}
keys (Dict ((c, vs):xs)) = c:keys (Dict xs)

{-function that return the list of all the dictionary values-}
values :: Dictionary a1 a2 -> [a2]
{-base case:  return a empty list for an empty dictionary -}
values (Dict []) = []
{-using a higher function concatMap that Map a function (in this case "snd" that return th second element in a couple) 
  over all the elements of a container and concatenate the resulting lists -}
values (Dict dict) =  concatMap snd dict
                               

{-function that merge two dictionaries-}
merge :: (Ord b, Ord a) => Dictionary a b -> Dictionary a b -> Dictionary a b
{-base case: two empty dictionaries as paramether, return an empty dictionary-}
merge (Dict[]) (Dict[]) = Dict[]
{-the two case in witch one o the two dictionaries passed as argument is empty so return as merge the non-empty one -}
merge (Dict dict1) (Dict[]) = Dict dict1
merge (Dict []) (Dict dict2) = Dict dict2
{-to merge two non-empty dictionaries first of all i sort with the auxiliar function dictSort the dictinary made by concat the two dictionaries-}
{-so the concatenation is made by couple with possibly repeted keys but, the couple with repeted keys thaks to the dictSort are adjacent-}
{-then on this concatenation call the auxiliar function subMerge-} 
merge (Dict dict1) (Dict dict2) = let Dict sd = dictSort (Dict(dict1++dict2)) in Dict( subMerge sd)


{-auxiliar function subMerge that merge the adjacent couples, with the same key k, of a list. 
  The result of this "merge" is one couple with k as key and the concatenation of all the values of the merged couples -}
subMerge :: (Ord a1, Eq a2) => [(a2, [a1])] -> [(a2, [a1])]
{-base case: return a empty list for an empty list passed as paramether-}
subMerge [] = []
{-having only one cople in a list passed as paramether the result is the same list-}
subMerge [(c,vs)] = [(c,vs)]
{-if the keys of the two adjacent couple are equals, "merge" this couples otherwise scroll the list-}
subMerge ((c1, vs1):(c2,vs2):xs1) = if c1 Prelude.== c2 then (c1, vs1++vs2) : subMerge xs1
                                                else (c1,vs1): subMerge ((c2,vs2):xs1)
                                                    