## Guidelines

* break the traditional Java thinking and sacrifice it to readability, simplicity and lower development cycles!
* easy to grasp interfaces and source code
* no more then a dozen of method names per class/interface
* should have flexibility over what is used as a parameters
* should have a fallback overload method that can alert if some stupid parameter is passed
* should rely on code-complete
* should not magnify constants, for example if status can be set as 200, it's clear what it is
* should be able to create http endpoints and do it fast and well, any other functionality is of lower importance
* reuse JDK classes as much as possible
* fluent interfaces everywhere

* where it is not possible to fir interface into 10-12 methods, all frequent methods extended method version should be provided
* 

* memory footprint of under 40M for simple webapps

