# datetime-spit-appender

DateTime spit appender for Timbre.

## Usage
```clojure
(require '[piotrts.datetime-spit-appender :refer [datetime-spit-appender]])
```

Then add it as one of your appenders:

```clojure
(timbre/merge-config!
  {;; ...
   :appenders {;; ...
               :dt-spit (datetime-spit-appender)
               ;;       ^^^^^^^^^^^^^^^^^^^^^^^^
               }})
```

To specify output path (that should be a *java.text.SimpleDateFormat* pattern) pass a map containing `:fname-format` param to `datetime-spit-appender` function.

Contributors welcome!
