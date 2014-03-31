randomfinder
============

Finds random seeds to match strings.

==Why it's useful==

You can protect your strings by using this method.
For example (protected):
```Java
Random rand = new Random(-2147477273);
String secret = ((char)(rand.nextInt(96)+32)) + "" + ((char)(rand.nextInt(96)+32));
```
Unprotected:
```Java
String visible = "hi";
```

==Note==

This is not designed to work with large strings. It works with about 4 characters best (sometimes with 5 and 6, but takes a lot longer to find). I also have it to where you could theoretically use it with ```long``` instead of ```int```, but you would have to do a little playing around to get it. If you want to use it with strings > 4 or 5 characters, simply break them up into segments.

==License Note==

It's still licensed under MIT, but you don't **have** to credit me.
