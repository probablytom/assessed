import Text.XML.HXT.Core
import Text.HandsomeSoup

main = do
  let xkcd = fromUrl "http://xkcd.com"
  comic <- runX $ xkcd >>> css "div#comic img" >>> getAttrValue "title"
  print comic
  
