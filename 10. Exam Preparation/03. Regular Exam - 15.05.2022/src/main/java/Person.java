import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class Person {
    private String name;
    private List<String> reactions;

    public Person(String name, ArrayList<String> reactions) {
        this.name = name;
        this.reactions = reactions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getReactions() {
        return reactions;
    }

    public void setReactions(List<String> reactions) {
        this.reactions = reactions;
    }
}
