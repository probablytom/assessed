import Text.XML.HXT.Core
import Text.HandsomeSoup
import Data.List.Split
import Data.Maybe
import Control.Monad.IO.Class

type PhonebookEntry = (String, String)

type PhoneBook = [PhonebookEntry]

rootUrl = "http://www.gla.ac.uk/schools/computing/staff/"

-- Convenience function to get the last part of a string separated by '/'
staffSection :: String -> String
staffSection str = (last $ splitOn "/" str) ++ "/"

parseStaffEntry :: String -> IO PhonebookEntry
parseStaffEntry staffurl = do
  let source = fromUrl staffurl
  let name = runX $ source >>> css "h1.responsivestyle" /> getText
  let phoneNumber = runX $ source >>> css "div#sp_contactInfo" /> getText
  contructEntry name phoneNumber

contructEntry :: IO [String] -> IO [String] -> IO PhonebookEntry
contructEntry io_name io_phoneNumber = do
  name <- io_name
  phoneNumber <- io_phoneNumber
  return (name !! 0, phoneNumber !! 0)

-- TODO: To parse out the phone number from the contents of the contact info block.
findNumber :: String -> String
findNumber x = x

main :: IO PhoneBook
main = do
  let rootSource = fromUrl rootUrl
  staffSegments <- runX $ rootSource >>> css "ul#research-teachinglist li a" ! "href"
  let staffUrls = fmap (\x -> rootUrl ++ (staffSection x)) staffSegments
  let phonebook_entries = foldr (\url book -> book ++ [parseStaffEntry url]) [return (("","")::PhonebookEntry)] staffUrls
  sequence phonebook_entries

