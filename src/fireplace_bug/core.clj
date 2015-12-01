(ns fireplace-bug.core
  (:require
    [schema.core :as s]))

(defprotocol IThing
  (thing-name [this]))

(defrecord Thing [name age]
  IThing
  (thing-name [_] name))

(s/defn h
  [thing :- (s/protocol IThing)]
  (thing-name thing))

(s/with-fn-validation
  ;; The following expr hangs vim for 30s or so, then
  ;; java.lang.StackOverflowError on stdout of REPL.
  ;; With `(require 'fireplace-bug.core :reload)` in leinigen REPL you get the
  ;; expected error:
  ;; CompilerException clojure.lang.ExceptionInfo: Input to h does not match schema: [(named (not (satisfies? IThing a-clojure.lang.PersistentArrayMap)) thing)] {:type :schema.core/error, :schema [#schema.core.One{:schema (protocol IThing), :optional? false, :name thing}], :value [{:name "mark", :age 100}], :error [(named (not (satisfies? IThing a-clojure.lang.PersistentArrayMap)) thing)]}, compiling:(fireplace_bug/core.clj:16:1)
  (h {:name "mark" :age 100})
  )
