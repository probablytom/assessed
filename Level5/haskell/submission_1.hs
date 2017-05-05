import Control.Applicative

data Tree a = Leaf | Node a (Tree a) (Tree a)  deriving (Show, Ord, Eq, Read)

-- A functor allows you to map a function across values trapped in a datatype (Tree) using fmap
-- This definition just returns a tree with the funtion passed to fmap recursively applied across all of the tree's values.
instance Functor Tree where
  fmap f Leaf = Leaf
  fmap f (Node elem left right) = Node newelem newleft newright
    where newelem  = f elem
          newleft  = fmap f left
          newright = fmap f right

-- An Applicative types can apply against each other. <*> :: f (a->b) -> f a -> f b (and it's infix)
-- Note: for an Applicative, you can use <$> as an infix version of fmap!
instance Applicative Tree where
  -- We need something that can lift a value, a, into a tree, for the definition of applicative.
  pure a = Node a Leaf Leaf

  -- What happens when we want to apply two trees?
  -- The first tree will have type: Tree (a -> b), and the second is of type: Tree (a). We're going to generate a tree of type: Tree (b)
  -- e1 is a function, because it's the value in the first tree -- we'll call that function on the value in the other tree, which is e2.
  -- We're also going to make this recursive, so that we can apply *all* of the functions in the first tree to *all* of the corresponding values in the other tree.
  -- To do that, we apply the children of the first node to the children of the second.
  -- What if either child is not caught by the first pattern? Then one of the children must be a Leaf, not a Node! Then we will just return a Leaf.
  Node e1 l1 r1 <*> Node e2 l2 r2 = Node (e1 e2) (l1 <*> l2) (r1 <*> r2)
  _ <*> _ = Leaf

-- We make use of Tree being an Applicative.
-- We *partially apply* the function (,), which takes two values a and b and returns a tuple (a,b).      (,) :: a -> b -> (a,b)
-- We can do this because we've defined fmap above -- it's just going to apply the function to all of the values in the tree.
-- The type of first was Tree A, but we've partially applied (,) :: a -> b -> (a,b). That means that the tree that fmap returns is of type Tree (b -> (a,b))...
-- That's just the first step! We want this function to be fully applied -- we want the second value of the tuple to be the value in the second tree.
-- To do that, we *apply* the functions in the tree returned by fmap to the corresponding values in the tree.
-- Because the tree is an applicative, we can apply one tree to another -- and because we've defined above how that application works for trees, it knows to apply functions in the first tree to values in the same position of the second tree.
treezip :: (Tree a) -> (Tree b) -> (Tree (a,b))
treezip first second = (fmap (,) first) <*> second  -- Alternatively: (,) <$> first <$> second

-- We make use of Tree being a Functor.
-- We can just map the fst and snd functions, which get the first and second values of a tuple, across the trees.
-- That's awesome -- we've already described how the mapping works, so there's not really any work to be done here. We just chuck in fst and snd by mapping over the tree with fmap, and Haskell knows what to do because of our earlier definitions.
treeunzip :: (Tree (a,b)) -> (Tree a, Tree b)
treeunzip intree = (fmap fst intree, fmap snd intree)
