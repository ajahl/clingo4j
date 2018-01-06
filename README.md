
# Clingo4j 

[![Build Status](https://travis-ci.org/lorislab/clingo4j.svg?branch=master)](https://travis-ci.org/lorislab/clingo4j)

Java API for the [clingo](https://github.com/potassco/clingo)

Clingo4j is distributed under the [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0.txt).

# Maven
```xml
    <dependecy>
        <groupId>org.lorislab.clingo4j</groupId>
        <artifactId>clingo4j</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </dependecy>
```
# Example
```java
    Clingo.init("src/main/clingo");
    
    try (Clingo control = Clingo.create()) {
        System.out.println(control.getVersion());
        control.add("base", "b :- not a. a :- not b.");
        control.ground("base");
        
        try (SolveHandle handle = control.solve()) {
            for (Model model : handle)  {
                System.out.println("Model type: " + model.type());
                for (Symbol atom : model.symbols()) {
                    System.out.println(atom);
                }
            }  
        } 
    } catch (ClingoException ex) {
       System.err.println(ex.getMessage());
    }
```

# Build
```bash
mvn clean install
```

### Rebuild the java c-api
```bash
java -jar src/main/jnaerator/jnaerator.jar src/main/jnaerator/c.jnaerator 
```