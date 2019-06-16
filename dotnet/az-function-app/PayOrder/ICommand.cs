namespace com.opusmagus.bl
{
    public interface ICommand
    {
        dynamic Execute(dynamic input);
    }
}