# while-small-step

This project is intended to graphically illustrate the small-step semantics of the classic **While** language. 
It only serves educational purposes.

## Install

```
$ git clone https://github.com/zsolttabi/while-small-step.git
$ cd while-small-step
$ mvn clean package
```

## Run

```
$ ./target/while-small-step-1.2-full.jar
```

## Use

### Syntax

* program syntax is defined as an [ANTLR4 grammar](src/main/antlr4/syntax/while_parser/While.g4)
* to write a program use the text-box on the left pane and type some code
* an example code is given at the start of the application

### Semantics

* to start the program (create the AST) use the **Start** button
* program semantics are illustrated by the AST graph in left pane
* syntactically incorrect code will appear as **purple** node in the graph
* to advance the program (go to the next configuration) use the **Next** button
* as the program advances both the graph and the code changes accordingly
* nodes with **blue** text indicate which element will be evaluated next 
* semantic errors are indicated by **red** colored nodes
* it is possible to go back to the previous configuration by pressing the **Prev** button
* jumping to the last or first configuration is available via the **First** and **Last** buttons, respectively
* modifying the **Allowed prefix** spinner changes the maximum number of stored configurations,
  i.e. the maximum steps the program can take
* the **Stop** button unloads the program, making the editor available again