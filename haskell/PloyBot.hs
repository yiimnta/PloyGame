--- module (NICHT AENDERN!)
module PloyBot where

import Data.Char
import Util

--- external signatures (NICHT AENDERN!)
getMove :: String -> String
getMove x = do
    let list = searchMove x
    let arr = splitOn "," list
    arr!!0


listMoves :: String -> String
listMoves x = "[" ++ searchMove x ++ "]"

--- YOUR IMPLEMENTATION STARTS HERE ---
--             0
-- 7 (-1,-1) (0,-1) (1,-1) 1
-- 6 (-1,0)         (1,0)  2
-- 5 (-1,1)  (0,1)  (1,1)  3
--             4  
directions :: [(Int, Int)]
directions = [(0,-1), (1,-1), (1,0), (1,1),(0,1),(-1,1),(-1,0),(-1,-1)]


searchMove :: String -> String
searchMove strData = do
    let [figureString, color] = splitOn " " strData
    let gb = gameBoard figureString
    let listFigure = getAllFigureWithColor strData
    let listRotation = getListRotation listFigure gb
    let listCanGo = searchWay listFigure gb color
    init $ listCanGo ++ listRotation



getAllFigureWithColor :: String -> [String]
getAllFigureWithColor dt = do 
    let replace = map(\char -> if char == '/' then ',' else char)
    let [gameBoardString, color] = splitOn " " dt
    let gameBoard = splitOn "," (replace gameBoardString)
    let getFigurColor = map(\c -> (c, color))
    removeSpace $ map compareColor (getFigurColor gameBoard)


compareColor :: (String, String) -> String
compareColor(x, color) = do 
    if x == "" then ""
    else if head x == head color
        then x
    else ""



removeSpace :: [String] -> [String]
removeSpace st = [c | c  <- st, c /= ""]


gameBoard :: String -> [[String]]
gameBoard d = [splitOn "," c|c <- (splitOn "/" d)]



searchWay :: [String] -> [[String]] -> String -> String
searchWay [] _ _ = [] 
searchWay (x:xs) gb color = do 
    let directFigure = searchDirections x
    let stepsNumber = (steps directFigure) - 1
    let pos = searchPositionFigure x gb
    let rs = searchDirectionToGo directFigure stepsNumber gb color pos
    rs ++ searchWay xs gb color



searchDirections :: String -> [Int]
searchDirections figure = do
    let x = read $ (tail figure) :: Int
    [ n | (n,m) <- (zip [0..] $ reverse $ decToBin8Char x) , m == 1]



decToBin :: Int -> [Int]
decToBin x = reverse $ decToBin' x
  where
    decToBin' 0 = []
    decToBin' y = let (a,b) = quotRem y 2 in [b] ++ decToBin' a



decToBin8Char :: Int -> [Int]
decToBin8Char x = [ 0 | _<- [1..(8 - (length $ decToBin x))]] ++  decToBin x



steps :: [Int] -> Int
steps di = if length di == 4 then 1 else length di 



searchDirectionToGo :: [Int] -> Int -> [[String]] -> String -> (Int, Int) -> String
searchDirectionToGo [] _ _ _ _ = ""
searchDirectionToGo (x:xs) stepsNumber gb color pos = do
    let (dx,dy) = directions!!x
    let posNextDxDy = [calcNextDxDyPos n (dx,dy)| n <- [0..stepsNumber]]
    let posNextFigur = [calcNextPos pos (n,y)|(n,y) <- posNextDxDy]
    let posNotNegativ = removeNegativPos posNextFigur gb
    let posNextFigurCanGo = removeBarrier posNotNegativ gb color
    let convert = map(\posNew -> convertFormat pos posNew 0)
    let listCanMove = concat $ convert posNextFigurCanGo
    listCanMove ++ searchDirectionToGo xs stepsNumber gb color pos    


getListRotation :: [String] -> [[String]] -> String
getListRotation [] _ = ""
getListRotation (x:xs) gb = do
    let pos = searchPositionFigure x gb
    let listRotation = concat $ [convertFormat pos pos x| x <- [1..7]]
    listRotation ++ getListRotation xs gb   


searchPositionFigure :: String -> [[String]] -> (Int, Int)
searchPositionFigure figure gb = do
    let y = [n|(n,m) <- (zip [0..] gb), isElement figure m]
    let x = [n|(n,m) <- (zip [0..] $ gb!!(y!!0)), m == figure]
    (x!!0, y!!0)



isElement :: Eq a => a -> [a] -> Bool
isElement a [] = False
isElement a (x:xs)
  | a == x = True
  | otherwise = isElement a xs



calcNextDxDyPos :: Int -> (Int, Int) -> (Int, Int)
calcNextDxDyPos = (\x (dx,dy) -> (dx + dx * x, dy + dy * x))



calcNextPos :: (Int, Int) -> (Int, Int) -> (Int, Int)
calcNextPos = (\(x,y) (dx,dy) -> (x + dx, y + dy))



removeNegativPos :: [(Int, Int)] -> [[String]] -> [(Int, Int)]
removeNegativPos arr gb = [(x,y)|(x,y) <- arr, x >0 && y > 0 && x < (length $ gb!!0) && y < (length gb)]



removeBarrier :: [(Int, Int)] -> [[String]] -> String -> [(Int, Int)]
removeBarrier arr gb color = do
    let newArr = zip[0..] arr
    let a1 = [(z,(x,y))|(z,(x,y)) <- newArr, (gb!!y!!x /= "") && (head (gb!!y!!x)) == (head color)]
    if length a1 > 0 then do
        let (m,n) = a1!!0
        [y|(x,y) <- newArr, x < m]
    else do
        let a3 = [(x,y)|(x,y) <- arr,  (gb!!y!!x /= "") && head (gb!!y!!x) /= (head color)]
        if length a3 > 0 then a3
        else arr



convertFormat :: (Int, Int) -> (Int, Int) -> Int ->String
convertFormat (xOld, yOld) (xNew, yNew) rotation = do
    let horizon = ["a","b","c","d","e","f","g","h","i"]
    let vertical = ["9","8","7","6","5","4","3","2","1"]
    let rs = horizon!!xOld ++ vertical!!yOld ++ "-" ++ horizon!!xNew ++ vertical!!yNew ++ "-" ++ show rotation
    rs ++ ","