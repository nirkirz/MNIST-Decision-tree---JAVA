import java.util.ArrayList;
import java.util.List;

public class CalculateConds extends Thread {
    public Condition[] conds;
    public BinTree L;
    public List<int[]> local_max_la_examples;
    public List<int[]> local_max_lb_examples;
    public double local_max_value;
    public Condition local_max_X;
    public BinTree tree;
    public int cond_type;

    /**
     * Runnable class which its run function finds the best X and L from one group of conditions
     * @param conds - the conditions group to be checked
     * @param L - leaf to check
     * @param local_max_la_examples - examples which the best condtions is true for them
     * @param local_max_lb_examples - examples which the best condition is false for them
     * @param local_max_value - the best entropy value from this group of conditions
     * @param local_max_X - the best condition
     * @param tree - the tree which the leaf is in
     */
    public CalculateConds(Condition[] conds, BinTree L, List<int[]> local_max_la_examples, List<int[]>local_max_lb_examples, double local_max_value, Condition local_max_X, BinTree tree){
        this.conds = conds;
        this.L = L;
        this.local_max_la_examples = local_max_la_examples;
        this.local_max_lb_examples = local_max_lb_examples;
        this.local_max_value = local_max_value;
        this.local_max_X = local_max_X;
        this.tree = tree;
        cond_type = conds[0].cond_type;
    }

    public void run(){
        // Calculate entropy for each condition by the formula
        for (Condition X : conds){
            List<int[]> la = new ArrayList<>();
            List<int[]> lb = new ArrayList<>();
            for (int [] ex : L.examples){
                if (X.CheckCondition(ex))
                    la.add(ex);
                else
                    lb.add(ex);
            }
            double h_la = tree.H(la);
            double h_lb = tree.H(lb);
            double n = L.examples.size();
            double IG_X_L = 0;
            double h_x = 0;
            double h_l = 0;
            double la_size = la.size();
            double lb_size = lb.size();
            if (n != 0) {
                h_x = (la_size) / n * h_la + (lb_size) / n * h_lb;
                h_l = tree.H(L.examples);
                IG_X_L = h_l - h_x;
            }
            // if this condition is better the all the previous conditions than updates max values
            if (local_max_value < IG_X_L * n){
                local_max_value = IG_X_L * n;
                local_max_X = X;
                local_max_la_examples = la;
                local_max_lb_examples = lb;
            }
        }
        // Update the value that the leaf will hold - the best X and L
        synchronized (this){
            if (L.IG_details == null)
                L.IG_details = new IG_Details(local_max_value, local_max_X, local_max_la_examples, local_max_lb_examples);
            else{
                if (L.IG_details.value < local_max_value)
                    L.IG_details = new IG_Details(local_max_value, local_max_X, local_max_la_examples, local_max_lb_examples);
            }
        }
    }
}
