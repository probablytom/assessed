data Tree a = Leaf | Node a (Tree a) (Tree a)  deriving (Show, Ord, Eq, Read)

-- Gosh! Exotic! We make `Tree` a Functor, so we can recursively map along it.
instance Functor Tree where
  fmap f Leaf = Leaf
  fmap f (Node elem left right) = Node newelem newleft newright
    where newelem  = f elem
          newleft  = fmap f left
          newright = fmap f right

treezip :: (Tree a) -> (Tree b) -> (Tree (a,b))
treezip (Node e1 l1 r1) (Node e2 l2 r2) = Node new_e new_l new_r
  where new_e = (e1, e2)
        new_l = treezip l1 l2
        new_r = treezip r1 r2
treezip _ _ = Leaf  -- Didn't match the above pattern so *must* be Leaf

-- Exploit Tree being a Functor to fmap a lambda through it
treeunzip :: (Tree (a,b)) -> (Tree a, Tree b)
treeunzip Leaf = (Leaf, Leaf)
treeunzip intree = (fmap get_left intree, fmap get_right intree)
  where get_left  = \(x, y) -> x
        get_right = \(x, y) -> y
