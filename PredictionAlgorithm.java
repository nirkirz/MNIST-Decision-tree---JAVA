import java.io.FileReader;
import java.util.*;
import java.util.Collections;
import java.lang.Math;
import com.google.gson.Gson;
import java.io.IOException;


public class PredictionAlgorithm {

    public static void main(String[] args){
        String tree_file = "C:\\Users\\yuval\\IdeaProjects\\mini_mnist\\out_tree.json";
        String test_file = "C:\\Users\\yuval\\IdeaProjects\\mini_mnist\\mnist_test.csv";
        Gson gson = new Gson();
        BinTree tree = null;
        try{
            tree = gson.fromJson(new FileReader(tree_file), BinTree.class);
        }
        catch (IOException e){
            e.printStackTrace();
        }

        ReaderCsv reader = new ReaderCsv();
        ArrayList<int[]> pics = reader.getCSVArray(test_file);

        new PredictionAlgorithm().print_conds(tree);

        double good = 0;
        System.out.println("");
        for (int [] img : pics){
            int to_print = tree.getLabelByTree(tree, img);
            if (to_print == img[0])
                good++;
            System.out.println(to_print);
        }
        double pics_size = pics.size();
        System.out.println(good / pics_size * 100);

    }

    public void print_conds(BinTree tree){
        if (tree.isLeaf())
            return;
        if  (tree.cond != null)
            System.out.println(tree.cond.cond_type);
        print_conds(tree.left);
        print_conds(tree.right);
    }
}
