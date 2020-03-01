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
    //public int[][] all_means_type_9; //maybe delete

    public BinTree(Condition cond, int label, int id){
        this.cond = cond;
        this.label = label;
        this.id = id;
        this.right = null;
        this.left = null;
        this.IG_details = null;
        this.examples = null;
        //this.all_means_type_9 = null; //maybe delete
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

    /**
     * Function return true if this tree is a leaf with no children
     * @return
     */
    public boolean isLeaf(){
        return ((this.right == null) && (this.left == null));
    }

    /**
     * this functions defines equalization between 2 trees
     * @return
     */
    public boolean equals(BinTree other){
        return this.id == other.id;
    }

    /**
     * Calculate H value by the formula
     * @return
     */
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

    /**
     * find common label among the parameter train_set
     * @return
     */
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

    /**
     * Function gets tree and img, simulate the tree conditions on image and return its predicted label
     * @param tree
     * @param img
     * @return
     */
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

    /**
     * Function clean the tree from examples to make it exportable to json
     */
    public void clean(){
        this.examples = new ArrayList<>();
        this.IG_details = null;
        if (this.right != null)
            this.right.clean();
        if (this.left != null)
            this.left.clean();
    }
}
