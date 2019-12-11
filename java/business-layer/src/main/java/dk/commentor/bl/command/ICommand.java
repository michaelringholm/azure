package dk.commentor.bl.command;

public interface ICommand<I, O>
{
    O Execute(I input) throws Exception;
}
