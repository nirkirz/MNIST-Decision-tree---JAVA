import java.io.FileWriter;
import java.util.*;
import java.util.Collections;
import java.lang.Math;
import com.google.gson.Gson;
import com.sun.deploy.util.ArrayUtil;

import java.io.IOException;

public class learningAlgorithm {

    public List<BinTree> BuildDecisionTree(double t, List<int[]> train_set, Condition[] conds, BinTree tree, List<BinTree> leaves, int id_leaf, double last_t){
        for (double i=last_t; i< t; i++){
            // ---- Variables to save optimal X and L ----
            double max_value = 0;
            BinTree max_L = new BinTree();
            Condition max_X = new Condition();
            List<int[]> max_la_examples = new ArrayList<>();
            List<int[]> max_lb_examples = new ArrayList<>();
            // ---- End Initial Variables ----

            for (BinTree L : leaves){
                // ---- Find Optimal X for each leaf
                double local_max_value = -1;
//                BinTree local_max_L = new BinTree();
                Condition local_max_X = new Condition();
                List<int[]> local_max_la_examples = new ArrayList<>();
                List<int[]> local_max_lb_examples = new ArrayList<>();
                if (L.IG_details == null){
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
                        if (local_max_value < IG_X_L * n){
                            local_max_value = IG_X_L * n;
                            local_max_X = X;
                            local_max_la_examples = la;
                            local_max_lb_examples = lb;
                        }
                    }
                    L.IG_details = new IG_Details(local_max_value, local_max_X, local_max_la_examples, local_max_lb_examples);
                }
            }
            for (BinTree L : leaves){
                if (max_value < L.IG_details.value){
                    max_value = L.IG_details.value;
                    max_L = L;
                    max_X = L.IG_details.X;
                    max_la_examples = L.IG_details.la;
                    max_lb_examples = L.IG_details.lb;
                }
            }
            // ---- Build chosen leaf ----
            BinTree leaf_la = new BinTree(null, tree.common_label(max_la_examples), id_leaf);
            id_leaf++;
            leaf_la.examples = max_la_examples;
            BinTree leaf_lb = new BinTree(null, tree.common_label(max_lb_examples), id_leaf);
            id_leaf++;
            leaf_lb.examples = max_lb_examples;
            max_L.left = leaf_lb;
            max_L.right = leaf_la;
            max_L.label = -1;
            max_L.cond = max_X;
            leaves.remove(max_L);
            leaves.add(leaf_la);
            leaves.add(leaf_lb);
        }

        BinTree temp = new BinTree(null, -1, id_leaf );
        leaves.add(0, temp);
        leaves.add(1, tree);
        return leaves;
    }

    public Condition[] BuildType1(){
        Condition[] conds = new Condition [784];
        for (int i = 1 ; i < 785; i++){
            int[] cond_details = new int[2];
            cond_details[0] = i;
            cond_details[1] = 128;
            conds[i-1] = new Condition(cond_details, 1);
        }
        return conds;
    }

    public Condition[] BuildType2(){
        Condition[] conds = new Condition[81];
        int cond_count = 0;
        for (int row = 0; row < 27; row += 3){
            for (int col = 0; col < 27; col += 3){
                int[] cond_det = new int[3];
                //top left
                cond_det[0] = col+1 +28*row;
                //more_than
                cond_det[1] = 128;
                //num of cells
                cond_det[2] = 3;
                conds[cond_count] = new Condition(cond_det, 2);
                cond_count++;
            }
        }
        return conds;
    }

    public Condition[] BuildType3(){
        Condition[] conds = new Condition[81];
        int cond_count = 0;
        for (int row = 0; row < 27; row += 3){
            for (int col = 0; col < 27; col += 3){
                int[] cond_det = new int[3];
                //top left
                cond_det[0] = col+1 +28*row;
                //more_than
                cond_det[1] = 128;
                //num of cells
                cond_det[2] = 5;
                conds[cond_count] = new Condition(cond_det, 3);
                cond_count++;
            }
        }
        return conds;
    }

    public Condition[] BuildType4(){
        Condition[] conds = new Condition[49];
        int cond_count = 0;
        for (int row = 0; row < 28; row += 4){
            for (int col = 0; col < 28; col += 4){
                int[] cond_det = new int[3];
                //top left
                cond_det[0] = col+1 +28*row;
                //more_than
                cond_det[1] = 128;
                //num of cells
                cond_det[2] = 3;
                conds[cond_count] = new Condition(cond_det, 4);
                cond_count++;
            }
        }
        return conds;
    }

    public Condition[] BuildType5(){
        Condition[] conds = new Condition[49];
        int cond_count = 0;
        for (int row = 0; row < 28; row += 4){
            for (int col = 0; col < 28; col += 4){
                int[] cond_det = new int[3];
                //top left
                cond_det[0] = col+1 +28*row;
                //more_than
                cond_det[1] = 128;
                //num of cells
                cond_det[2] = 5;
                conds[cond_count] = new Condition(cond_det, 5);
                cond_count++;
            }
        }
        return conds;
    }

    public Condition[] BuildType6(){
        Condition[] conds = new Condition[196];
        int conds_counter = 0;
        for (int ind = 1; ind <= 4 ; ind++){
            for (int i = 0; i<28 ; i+=4){
                for (int j = 0; j<28 ; j+=4){
                    int [] det = {ind, i, j};
                    conds[conds_counter] = new Condition(det, 6);
                    conds_counter++;
                }
            }
        }
        return conds;
    }


    public static void main(String[] args){
        int version = 2;
        double p = 20;
        double l = 8;
        String train_set_file_name = "C:\\Users\\yuval\\IdeaProjects\\mini_mnist\\mnist_train.csv";
        String output_tree_file_name = "C:\\Users\\yuval\\IdeaProjects\\mini_mnist\\out_tree.json";
        ReaderCsv reader = new ReaderCsv();
        ArrayList<int[]> pics = reader.getCSVArray(train_set_file_name);

        Collections.shuffle(pics);
        double div = p/100;
        double valid_set1 = (div)*pics.size();
        int valid_set_size = (int) Math.round(valid_set1);
        List<int[]> valid_set = pics.subList(0, valid_set_size);
        List<int[]> train_set = pics.subList(valid_set_size, pics.size());

        Condition[] conds;
        if (version == 1){
            conds = new learningAlgorithm().BuildType1();
        }
        else
        {
            Condition[] conds2 = new learningAlgorithm().BuildType2();
            Condition[] conds3 = new learningAlgorithm().BuildType3();
            Condition[] conds4 = new learningAlgorithm().BuildType4();
            Condition[] conds5 = new learningAlgorithm().BuildType5();
            Condition[] conds6 = new learningAlgorithm().BuildType6();
            conds = new Condition[conds2.length + conds6.length];
            for (int i=0; i< conds2.length; i++){
                conds[i] = conds2[i];
            }
            for (int i=conds2.length; i< conds6.length + conds2.length; i++){
                conds[i] = conds6[i-conds2.length];
            }
//            for (int i=conds3.length + conds2.length; i< conds3.length + conds2.length + conds4.length; i++){
//                conds[i] = conds4[i-conds2.length-conds3.length];
//            }
//            for (int i=conds3.length + conds2.length + conds4.length; i< conds3.length + conds2.length + conds4.length + conds5.length; i++){
//                conds[i] = conds5[i-conds2.length-conds3.length-conds4.length];
//            }

        }

        int id_leaf = 1;
        // ---- Build First Leaf ----
        int max_digit = new BinTree().common_label(train_set);
        BinTree tree = new BinTree(null, max_digit, id_leaf);
        id_leaf++;
        tree.examples = train_set;
        // ---- End Build First Leaf ----

        double last_t = 0;
        List<BinTree> leaves = new ArrayList<>();
        leaves.add(tree);
        int max_l = -1;
        int max_value = 0;
        double t = 0;
        for (int i = 0; i < l+1 ; i++){
            t = Math.pow(2, i);
            List<BinTree> res_build = new learningAlgorithm().BuildDecisionTree(t, train_set, conds,  tree,  leaves,  id_leaf,  last_t);
            tree = res_build.remove(1);
            BinTree temp = res_build.remove(0);
            id_leaf = temp.id;

            last_t = Math.pow(2, i);

            int succeeded = 0;
            for (int[] img : valid_set){
                int predicted_label = tree.getLabelByTree(tree, img);
                if (predicted_label == img[0])
                    succeeded++;
            }
            if (max_value<succeeded) {
                max_value = succeeded;
                max_l = i;
            }
        }
        // ---- Build Final Tree ----
        id_leaf = 1;
        max_digit = tree.common_label(pics);
        tree = new BinTree(null, max_digit, id_leaf);
        id_leaf++;
        tree.examples = pics;
        last_t = 0;
        leaves = new ArrayList<>();
        leaves.add(tree);
        double final_t = Math.pow(2, max_l);
        List<BinTree> res_final = new learningAlgorithm().BuildDecisionTree(final_t, pics, conds, tree, leaves, id_leaf, last_t);
        BinTree final_tree = res_final.remove(1);
        final_tree.clean();
        Gson gson = new Gson();
        try {
            FileWriter writer = new FileWriter(output_tree_file_name);
            gson.toJson(final_tree, writer);
            writer.flush();
            writer.close();

        }
        catch (IOException e){
            e.printStackTrace();
        }
        int errors = 0;
        for (int[] img : pics){
            int predicted = final_tree.getLabelByTree(final_tree, img);
            if (predicted != img[0])
                errors++;
        }
        System.out.println("error: " + errors);
        System.out.println("size: " + final_t);



    }

}
