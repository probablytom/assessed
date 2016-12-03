{-# LANGUAGE OverloadedStrings #-}

import Text.XML.HXT.Core
import Text.HandsomeSoup
import Data.List.Split
import Data.Maybe
--import Control.Monad.IO.Class
import Data.Char (isDigit)
import Text.LaTeX
import Data.Text (pack)

data PhonebookEntry = Entry (String, String) deriving Show

rootUrl = "http://www.gla.ac.uk/schools/computing/staff/"

-- Convenience function to get the last part of a string separated by '/'
staffSection :: String -> String
staffSection str = (last $ splitOn "/" str) ++ "/"

parseStaffEntry :: String -> IO (Maybe PhonebookEntry)
parseStaffEntry staffurl = do
  let source = fromUrl staffurl
  name <- runX $ source >>> css "h1.responsivestyle" /> getText
  phoneNumber <- runX $ source >>> css "div#sp_contactInfo" //> getText
  return $ contructEntry name phoneNumber

contructEntry :: [String] -> [String] -> Maybe PhonebookEntry
contructEntry name phoneNumber = if validName name && validNumber phoneNumber
  then Just $ Entry (name !! 0, findNumber phoneNumber)
  else Nothing
   where
    validName name = length name /= 0
    validNumber phoneNumber = "telephone" `elem` phoneNumber

-- TODO: Don't just select the second element, select the element after "telephone"
findNumber :: [String] -> String
findNumber x = [n | n <- (x !! 2), isDigit n]

phoneDirectory :: Monad m => [PhonebookEntry] -> LaTeXT_ m
phoneDirectory directory = do
  preamble
  document $ createDirectory directory

-- A small preamble creating section
preamble :: Monad m => LaTeXT_ m
preamble = do
  documentclass [] article
  author "Tom Wallis"
  title "Functional Programming A.E. 2"

-- Rendering and layout of a [PhonebookEntry] directory to LaTeX
createDirectory :: Monad m => [PhonebookEntry] -> LaTeXT_ m
createDirectory directory = do
  maketitle
  section "Directory"
  center $ tabular Nothing [CenterColumn, LeftColumn] $ do
    let entries = fmap (fmap pack) [[name, number] | (Entry (name, number)) <- directory]
    foldr render_entry (return ()) entries
     where
      render_entry :: (Monad m, Texy t) => [t] -> LaTeXT_ m -> LaTeXT_ m
      render_entry [name, number] l = do
        texy name & texy number
        lnbk
        hline
        l
       

-- To be run
main :: IO ()
main = do
  let rootSource = fromUrl rootUrl
  staffSegments <- runX $ rootSource >>> css "ul#research-teachinglist li a" ! "href"
  let staffUrls = fmap (\x -> rootUrl ++ (staffSection x)) staffSegments
  let phonebook_entries = foldr (\url book -> book ++ [parseStaffEntry url]) [] staffUrls
  unfiltered_phonebook <- sequence phonebook_entries
  let remove_empty_entries = (map fromJust) . (filter isJust)
  let phonebook = remove_empty_entries unfiltered_phonebook
  execLaTeXT (phoneDirectory phonebook) >>= renderFile "directory.tex"
