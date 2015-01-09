# Couchbase-Lite-ORM [![Build Status](https://travis-ci.org/BraisGabin/couchbase-lite-orm.svg?branch=master)](https://travis-ci.org/BraisGabin/couchbase-lite-orm)

This is the first approach to implement an ORM for Couchbase Lite in Java. I'm still working on it.

## Main ideas
- Couchbase Lite's API is pretty good, I wont hide it.
- Couchbase Lite ORM only transform Documents to your domain Objects and vice versa.
- You won't need to extend any class.
- You will only need to annotate your classes.
- This library will not use reflection. Reflection is *too* slow.

## License
Apache License 2.0
