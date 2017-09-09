(ns piotrts.datetime-spit-appender
  (:require [taoensso.encore :as enc])
  (:import (java.io IOException File)
           (java.text SimpleDateFormat)))

;; based on taoensso.timbre.appenders.core/spit-appender
(defn datetime-spit-appender
  "Returns a datetime-spit appender for Clojure. fname-format
  is a java.time.format.DateTimeFormatter pattern."
  [& [{:keys  [fname-format append?]
        :or   {fname-format  "'./timbre-spit-'yyyy-MM-dd'.log'"
               append? true}}]]
  (let  [date-format (SimpleDateFormat. fname-format)]
    {:enabled?   true
     :async?     false
     :min-level  nil
     :rate-limit nil
     :output-fn  :inherit
     :fn         (fn self [data]
                   (let [{:keys [output_ instant]} data
                         fname (.format date-format instant)]
                     (try
                       (spit fname (str (force output_) "\n") :append append?)
                       (catch IOException e
                         (if (:__spit-appender/retry? data)
                           (throw e) ; Unexpected error
                           (let [_ (enc/have? enc/nblank-str? fname)
                                 file (File. ^String fname)
                                 dir (.getParentFile (.getCanonicalFile file))]
                             (when-not (.exists dir) (.mkdirs dir))
                               (self (assoc data :__spit-appender/retry? true))))))))}))
