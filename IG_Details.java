import java.util.List;

public class IG_Details {
    public double value;
    public Condition X;
    public List<int[]> la;
    public List<int[]> lb;
    public boolean empty = true;

    /**
     * Class to save all important imformation to calculate the IG value and entropy
     * @param value
     * @param X
     * @param la
     * @param lb
     */
    public IG_Details(double value, Condition X, List<int[]> la, List<int[]> lb){
        this.value = value;
        this.X = X;
        this.la = la;
        this.lb = lb;
        this.empty = false;
    }

    /**
     * Function checks if IG_Details is empty and not used
     * @return
     */
    public boolean isEmpty(){
        return this.empty;
    }
}
