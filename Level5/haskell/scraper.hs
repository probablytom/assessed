{-# LANGUAGE OverloadedStrings #-}

import Text.XML.HXT.Core
import Text.HandsomeSoup
import Data.List.Split (splitOn)
import Data.Maybe
import Data.Char (isDigit)
import Text.LaTeX
import Data.Text (pack)
-- Longtable definition from Jeremy Singer. Thanks, Jeremy!
import Text.LaTeX.Base.Class
import Text.LaTeX.Base.Commands
import Text.LaTeX.Base.Syntax
longtabular :: LaTeXC l => [TableSpec] -> l -> l
longtabular ts  = liftL $ TeXEnv "longtable" [ FixArg $ TeXRaw $ renderAppend ts ]

data PhonebookEntry = Entry (String, String) deriving Show

rootUrl = "http://www.gla.ac.uk"


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
findNumber x = [n | n <- (x !! 2), or [isDigit n, n == '+']]

phoneDirectory :: Monad m => [PhonebookEntry] -> LaTeXT_ m
phoneDirectory directory = do
  preamble
  document $ createDirectory directory

-- A small preamble creating section
preamble :: Monad m => LaTeXT_ m
preamble = do
  documentclass [] article
  usepackage [] "longtable"
  author "Tom Wallis"
  title "Functional Programming A.E. 2"

-- Rendering and layout of a [PhonebookEntry] directory to LaTeX
createDirectory :: Monad m => [PhonebookEntry] -> LaTeXT_ m
createDirectory directory = do
  maketitle
  section "Directory"
  center $ longtabular [CenterColumn, LeftColumn] $ do
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
  let rootSource = fromUrl $ rootUrl ++ "/schools/computing/staff/"
  staffPartialUrls <- runX $ rootSource >>> css "ul#research-teachinglist li a" ! "href"
  let staffUrls = fmap (\x -> rootUrl ++ x ++ "/") staffPartialUrls
  let phonebook_entries = foldr (\url book -> book ++ [parseStaffEntry url]) [] staffUrls
  unfiltered_phonebook <- sequence phonebook_entries
  let remove_empty_entries = (map fromJust) . (filter isJust)
  let phonebook = remove_empty_entries unfiltered_phonebook
  execLaTeXT (phoneDirectory phonebook) >>= renderFile "directory.tex"
