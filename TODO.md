# TODO List

## Performance

* replace UUID usage for unique ids since it is slow
* ViewImpl getEntries could be Collection instead of copied ArrayList
* ViewImpl could be array based instead of List based

## Features

* add ability to destroy a View if it is no longer needed
* implement ViewN + ViewNEntry
    * include convenience method like `<T extends Component> T c<T>(0)` or `c(0, TestComponent.class)` on ViewNEntry

## Bugs

*
