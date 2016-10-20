data Tree a = Leaf | Node a (Tree a) (Tree a)  deriving (Show, Ord, Eq, Read)

-- A functor allows you to map a function across values trapped in a datatype (Tree) using fmap
instance Functor Tree where
  fmap f Leaf = Leaf
  fmap f (Node elem left right) = Node newelem newleft newright
    where newelem  = f elem
          newleft  = fmap f left
          newright = fmap f right

-- An Applicative types can apply against each other. <*> :: f (a->b) -> f a -> f b (and it's infix)
-- Note: for an Applicative, you can use <$> as an infix version of fmap!
instance Applicative Tree where
  pure a = Node a Leaf Leaf
  Node e1 l1 r1 <*> Node e2 l2 r2 = Node (e1 e2) (l1 <*> l2) (r1 <*> r2)
  _ <*> _ = Leaf

-- We make use of Tree being an Applicative
treezip :: (Tree a) -> (Tree b) -> (Tree (a,b))
treezip first second = (,) <$> first <*> second

-- We make use of Tree being a Functor
treeunzip :: (Tree (a,b)) -> (Tree a, Tree b)
treeunzip intree = (fmap fst intree, fmap snd intree)
