public class ToPrediction {

    public BinTree tree;
    public int[][] means;

    /**
     * Class that will hold the 2 object needed for prediction
     * @param tree - the output tree from the learning algorithm
     * @param means - average images the tree uses in its conditions
     */
    public ToPrediction(BinTree tree, int[][] means){
        this.tree = tree;
        this.means = means;
    }
}
