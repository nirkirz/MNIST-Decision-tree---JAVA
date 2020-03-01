import java.io.FileReader;
import java.util.*;
import com.google.gson.Gson;

import java.io.IOException;


public class PredictionAlgorithm {

    public static void main(String[] args){
//        String tree_file = "/users/studs/bsc/2019/nirkirz/mini-project/out_tree.json";
//        String test_file = "/users/studs/bsc/2019/nirkirz/mini-project/mnist_test.csv";
        String tree_file = args[0];
        String test_file = args[1];
                //String test_file = "C:\\Users\\yuval\\IdeaProjects\\mini_mnist\\Kannada\\train.csv";
        //String test_file = "C:\\Users\\yuval\\IdeaProjects\\mini_mnist\\Kannada\\Dig-MNIST.csv";
        Gson gson = new Gson();
        BinTree tree = null;
        ToPrediction to_pred = null;
        try{
            to_pred = gson.fromJson(new FileReader(tree_file), ToPrediction.class);
            tree = to_pred.tree;
        }
        catch (IOException e){
            e.printStackTrace();
        }

        ReaderCsv reader = new ReaderCsv();
        ArrayList<int[]> pics1 = reader.getCSVArray(test_file);

        // Arrange pics in Array List
        ArrayList<int[]> pics = new ArrayList<>();
        int counter = 0;
        for (int[] img : pics1){
            int[] new_img = new int[786];
            for (int i = 0; i < 785 ; i++){
                new_img[i] = img[i];
            }
            new_img[785] = counter;         //Add index of linked images
            pics.add(counter, new_img);
            counter++;
        }

        List<int[]> sharpImages = new learningAlgorithm().SharpImage(pics1);
        List<int[]> bounderies = new learningAlgorithm().BoundImage(sharpImages);

        DataBase db = new DataBase(bounderies, sharpImages);

        //db.CreateMeanImages(pics1);
        db.better_means = to_pred.means;

        db.CreateType6Data(pics1);
        db.CreateType7Data(pics);
        db.CreateType8Data(pics);
        db.CreateType9Data(pics);

        for (int [] img : pics){
            int to_print = tree.getLabelByTree(tree, img);
            System.out.println(to_print);
        }
    }

    /**
     * Self use function for printing conds that selected to the tree
     * @param tree
     * @param histo
     */
    public void print_conds(BinTree tree, int[] histo){
        if (tree.isLeaf())
            return;
        if  (tree.cond != null) {
            System.out.print(tree.cond.cond_type + " :: ");
            for (int i = 0; i < tree.cond.cond_details.length; i++)
                System.out.print(tree.cond.cond_details[i] + ", ");
            System.out.print(tree.id);
            System.out.println("");
            histo[tree.cond.cond_type]++;
        }
        print_conds(tree.left, histo);
        print_conds(tree.right, histo);
    }
}
