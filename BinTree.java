import java.lang.Math;
import java.util.ArrayList;
import java.util.List;

public class BinTree {

    public BinTree right;
    public BinTree left;
    public Condition cond;
    public int label;
    public List<int[]> examples;
    public int id;
    public IG_Details IG_details;

    public BinTree(Condition cond, int label, int id){
        this.cond = cond;
        this.label = label;
        this.id = id;
        this.right = null;
        this.left = null;
        this.IG_details = null;
        this.examples = null;
    }

    public BinTree(){
        this.cond = null;
        this.label = 0;
        this.id = 0;
        this.right = null;
        this.left = null;
        this.IG_details = null;
        this.examples = null;
    }

    public boolean isLeaf(){
        return ((this.right == null) && (this.left == null));
    }

    public boolean equals(BinTree other){
        return this.id == other.id;
    }

    public double H(List<int[]> leaf_ex){
        int [] Ni = new int [10];
        for(int [] ex : leaf_ex){
            Ni[ex[0]]++;
        }

        double h_value = 0;
        double n = leaf_ex.size();
        for (double ni : Ni){
            if (!(n == 0 || ni == 0)){
                double temp = Math.log10(n/ni);
                h_value = h_value + (ni/n)*temp;
            }
        }

        return h_value;
    }

    public int common_label(List<int[]> train_set){
        int [] digit_count = new int [10];

        for (int[] pic : train_set){
            digit_count[pic[0]]++;
        }

        int max_digit = -1;
        int max_i = -1;

        for (int i = 0; i < digit_count.length; i++){
            int dig = digit_count[i];
            if (dig > max_digit){
                max_digit = dig;
                max_i = i;
            }
        }

        return max_i;
    }

    //CHECK IF WE NEED TO ADD BOUND_PICS!!!
    public int getLabelByTree(BinTree tree, int[] img){
        while(! tree.isLeaf()){
            if (tree.cond.CheckCondition(img)){
                tree = tree.right;
            }
            else{
                tree = tree.left;
            }
        }
        return tree.label;
    }

    public void clean(){
        this.examples = new ArrayList<>();
        this.IG_details = null;
        if (this.right != null)
            this.right.clean();
        if (this.left != null)
            this.left.clean();
    }
}
