import java.io.FileReader;
import java.util.*;
import java.util.Collections;
import java.lang.Math;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.IOException;


public class PredictionAlgorithm {

    public static void main(String[] args){
        String tree_file = "C:\\Users\\yuval\\IdeaProjects\\mini_mnist\\out_tree.json";
        String test_file = "C:\\Users\\yuval\\IdeaProjects\\mini_mnist\\mnist_test.csv";
        //String test_file = "C:\\Users\\yuval\\IdeaProjects\\mini_mnist\\Kannada\\train.csv";
        //String test_file = "C:\\Users\\yuval\\IdeaProjects\\mini_mnist\\Kannada\\Dig-MNIST.csv";
        Gson gson = new Gson();
        BinTree tree = null;
        ToPrediction to_pred = null;
        try{
            //tree = gson.fromJson(new FileReader(tree_file), BinTree.class);
            to_pred = gson.fromJson(new FileReader(tree_file), ToPrediction.class);
            tree = to_pred.tree;
        }
        catch (IOException e){
            e.printStackTrace();
        }

        //Maybe Delete
//        BinTree tree2 = tree.right;
//        int[][] real_means = tree.all_means_type_9;
//        tree = tree2;

        ReaderCsv reader = new ReaderCsv();
        ArrayList<int[]> pics1 = reader.getCSVArray(test_file);

        ArrayList<int[]> pics = new ArrayList<>();
        int counter = 0;
        for (int[] img : pics1){
            int[] new_img = new int[786];
            for (int i = 0; i < 785 ; i++){
                new_img[i] = img[i];
            }
            new_img[785] = counter;
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

//        for (int i = 0 ; i < 10 ; i ++){
//            System.out.print("[");
//            for (int j = 1; j < 785; j++){
//                System.out.print(db.better_means[i][j] + ",");
//            }
//            System.out.println("]");
//        }

//        System.out.println("----- Print Conds: -----");
//        int[] histogram = new int [13];
//        new PredictionAlgorithm().print_conds(tree, histogram);
//        System.out.println("----- End Print Conds: -----");
//        System.out.println("----- Print Histogram -----");
//        for (int i = 0 ; i < 13 ; i ++){
//            System.out.println("Cond number " + i + ": " + histogram[i]);
//        }
//        System.out.println("----- End Print Histogram -----");

        int [][] confusion_matrix = new int [10][10];

        double good = 0;
        System.out.println("");
        for (int [] img : pics){
            int to_print = tree.getLabelByTree(tree, img);
            if (to_print == img[0])
                good++;
            confusion_matrix[img[0]][to_print]++;
            System.out.println(to_print);
        }

        System.out.println("Confusion Matrix");
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++)
                System.out.print(confusion_matrix[i][j] + ",");
            System.out.println("");
        }

        double pics_size = pics.size();
        System.out.println(good / pics_size * 100);

    }

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
