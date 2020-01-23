import java.util.List;

public class IG_Details {
    public double value;
    public Condition X;
    public List<int[]> la;
    public List<int[]> lb;
    public boolean empty = true;

    public IG_Details(double value, Condition X, List<int[]> la, List<int[]> lb){
        this.value = value;
        this.X = X;
        this.la = la;
        this.lb = lb;
        this.empty = false;
    }

    public boolean isEmpty(){
        return this.empty;
    }
}
