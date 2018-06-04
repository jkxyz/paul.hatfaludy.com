#!/bin/bash

# Starts nREPL with the CIDER middleware and runs a REPL in the shell.

clj -A:repl -e '(require (quote cider-nrepl.main)) (cider-nrepl.main/init ["cider.nrepl/cider-middleware"])' -r
