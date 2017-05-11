package ast;

public interface IConfiguration {

    State getState();

    boolean isEndConfiguration();

    IConfiguration step();

}
