package app.commands;

import app.collection.CollectionManager;
import app.factory.HumanBeingFactory;
import app.model.HumanBeing;
import app.transfer.Request;
import app.transfer.Response;
import app.util.InputReader;

import java.util.List;

/**
 * replace_if_greater command: Replaces a value by key if the new value is greater than the old one.
 */
public class ReplaceIfGreaterCommand implements Command {
    private final CollectionManager<HumanBeing> collectionManager;
    private final HumanBeingFactory factory;
    private final InputReader reader;

    public ReplaceIfGreaterCommand(CollectionManager<HumanBeing> collectionManager, HumanBeingFactory factory, InputReader reader) {
        this.collectionManager = collectionManager;
        this.factory = factory;
        this.reader = reader;
    }

    @Override
    public Response execute(Request request) {
        String keyStr = request.args().isEmpty()
                ? reader.prompt("Enter key for replacement: ")
                : request.args().get(0);

        try {
            Long key = Long.parseLong(keyStr);
            HumanBeing current = collectionManager.getCollection().get(key);
            if (current == null) {
                return new Response("Element with this key not found");
            }
            HumanBeing newHuman = factory.createHumanBeing();
            if (newHuman.compareTo(current) > 0) {
                collectionManager.getCollection().put(key, newHuman);
                return new Response("Element replaced successfully");
            } else {
                return new Response("The new element is not greater than the existing one");
            }
        } catch (NumberFormatException e) {
            return new Response("The key must be a number");
        }
    }

    @Override
    public String getName() {
        return "replace_if_greater";
    }

    @Override
    public String getDescription() {
        return "Заменить значение по ключу, если новое значение больше старого";
    }
}
