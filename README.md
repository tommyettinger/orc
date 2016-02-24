# orc

A simple IRC game in Clojure.

Features: move around on a 2D map with `%up`, `%down`, `%left`, and `%right`. Map only displays as 2D on a handful of IRC clients, including [Floe](https://github.com/derrickcreamer/Floe).

## Usage

```
$ lein run
```

If you precompile to an uberjar with
```
$ lein uberjar
```

On Windows the output won't be nice unicode box-drawing characters, but `?`. There may be a workaround with cmd.exe codepages, but `lein repl` doesn't have a problem.

## License

Copyright (C) 2012 Wilfred Hughes, originally as the project [HandyBot](https://github.com/Wilfred/HandyBot)

Modified 2016 by Tommy Ettinger

Distributed under the GNU Public License v3 or later.
