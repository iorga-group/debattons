
jersey-example-java8-webapp
==========================================================

This example demonstrates how to ...

Contents
--------

The mapping of the URI path space is presented in the following table:

URI path                                   | Resource class            | HTTP methods
------------------------------------------ | ------------------------- | --------------
**_/resource_**                            | JerseyResource            | GET

Sample Response
---------------

```javascript

```

Running the Example
-------------------

Run the example using [Grizzly](http://grizzly.java.net/) container as follows:

>     mvn clean compile exec:java

Run the example using Jetty as follows:

>     mvn clean package jetty:run

-   <http://localhost:8080/base/jersey-resource>
