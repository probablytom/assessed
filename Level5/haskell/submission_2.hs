import Text.XML.HXT.Core
import Text.HandsomeSoup
import Data.List.Split

-- TODO: Should we use a regular notation instead of record?
data PhonebookEntry = PhonebookEntry { name :: String
                                     , number :: String}

type PhoneBook = [PhonebookEntry]

rootUrl = "http://www.gla.ac.uk/schools/computing/staff/"

-- Convenience function to get the last part of a string separated by '/'
staffSection :: String -> String
staffSection str = last $ splitOn "/" str

main :: IO()
main = do
  let rootSource = fromUrl rootUrl
  staffSegments <- runX $ rootSource >>> css "ul#research-teachinglist li a" >>> getAttrValue "href"
--  staffUrls <- fmap (\x -> rootUrl ++ ( staffSection x)) staffSegments
  print $ fmap (\x -> rootUrl ++ (staffSection x)) staffSegments  -- TODO: actually process the list into a phonebook
