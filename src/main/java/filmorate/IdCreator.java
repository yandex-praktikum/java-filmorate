package filmorate;

public class IdCreator {
    private int id;

    public IdCreator() {
        this.id = 1;
    }

    public int createId() {
        return id++;
    }
}
