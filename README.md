# while-small-step

This project is intended to graphically illustrate the small-step semantics of the classic <i>While</i> language. 
It only serves educational purposes.

## Install

```
$ git clone https://github.com/zsolttabi/while_small_step.git
$ cd while_small_step
$ mvn clean package
```

## Run

```
$ ./target/while-small-step-1.2-full.jar
```

## Use

* to create a new AST write statements in the left pane
* to execute a configuration transition click **Step**
* to execute all available transition click **Reduce**
* if there are invalid expressions or statements evaluation will fail and the corresponding nodes will be <font color="red">highlighted</font>
* variables with assigned values are shown at top pane
* to restart the evaluation process type something in the left pane
