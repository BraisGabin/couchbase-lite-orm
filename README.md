# Couchbase-Lite-ORM [![Build Status](https://travis-ci.org/BraisGabin/couchbase-lite-orm.svg?branch=master)](https://travis-ci.org/BraisGabin/couchbase-lite-orm)

This is the first approach to implement an ORM for Couchbase Lite in Java. I'm still working on it.

## Main ideas
- Couchbase Lite's API is pretty good, I wont hide it.
- Couchbase Lite ORM only transform Documents to your domain Objects and vice versa.
- You won't need to extend any class.
- You will only need to annotate your classes.
- This library will not use reflection. Reflection is *too* slow.

## Easter Egg
The annotations in Java have *a* weakness: their value can't be `null`. You can read this in [the JSL](JSL):

> Note that null is not a legal element value for any element type.

For more information I recommend read this [Stack Overflow answer](so-default-null).

For this reason I'm forced to use the [magic string](magic-string) `"☕️💩"`. So, if one of your types is named `"☕️💩"` you have found the Easter Egg!

## License
Apache License 2.0

 [JSL]: http://docs.oracle.com/javase/specs/jls/se7/html/jls-9.html#jls-9.7.1
 [so-default-null-]: http://stackoverflow.com/a/1178272/842697
 [magic-string]: http://en.wikipedia.org/wiki/Magic_string
