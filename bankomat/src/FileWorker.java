import java.util.List;

abstract public class FileWorker {
    abstract List<Card> fileReader();

    abstract void fileWriter();
}
