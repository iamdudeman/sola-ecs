# TODO List

## Performance

* try to optimize destroying entities
    * consider looping through component arrays and then nulling out each entity to be removed rather than looping entities

## Features

* add ability to destroy a View if it is no longer needed
* implement ViewN + ViewNEntry
    * include convenience method like `<T extends Component> T c<T>(0)` or `c(0, TestComponent.class)` on ViewNEntry

## Bugs

*
