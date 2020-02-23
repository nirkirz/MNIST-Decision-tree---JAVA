import java.io.FileWriter;
import java.util.*;
import java.util.Collections;
import java.lang.Math;
import com.google.gson.Gson;

import java.io.IOException;

public class learningAlgorithm {

    public List<BinTree> BuildDecisionTree(double t, List<int[]> train_set, Condition[] conds, BinTree tree, List<BinTree> leaves, int id_leaf, double last_t, Condition[][] condsv2){
        System.out.println("starting t = " + t);
        for (double i=last_t; i< t; i++){
            // ---- Variables to save optimal X and L ----
            double max_value = 0;
            BinTree max_L = new BinTree();
            Condition max_X = new Condition();
            List<int[]> max_la_examples = new ArrayList<>();
            List<int[]> max_lb_examples = new ArrayList<>();
            // ---- End Initial Variables ----
            int index_threads;
            for (BinTree L : leaves){
                CalculateConds[] threadsv2 = new CalculateConds[8];
                index_threads = 0;
                // ---- Find Optimal X for each leaf
                double local_max_value = -1;
                Condition local_max_X = new Condition();
                List<int[]> local_max_la_examples = new ArrayList<>();
                List<int[]> local_max_lb_examples = new ArrayList<>();
                if (L.IG_details == null){
                    for (Condition[] condv2 : condsv2){
                        CalculateConds t1 = new CalculateConds(condv2 ,L,local_max_la_examples,local_max_lb_examples,local_max_value,local_max_X,tree);
                        threadsv2[index_threads] = t1;
                        index_threads++;
                        t1.start();
                    }
                    for (CalculateConds thread : threadsv2){
                        try{
                            thread.join();
                        }
                        catch (InterruptedException e){
                        }
                    }
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
                cond_det[2] = 1;
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
                cond_det[2] = 7;
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

    public Condition[] BuildType7(){
        Condition[] conds = new Condition[88];
        int counter = 0;
        for (int i = 1; i < 5; i++){ // i means num of tested block
            for (int j = 3 ; j < 14; j++){ // j means tested length of line
                int [] det = {0, (i-1)*28*7 + 1, j};
                Condition cond1 = new Condition(det, 7); // 0 is horizontal
                int [] det2 = {1, (i-1)*7 + 1, j};
                Condition cond2 = new Condition(det2, 7); // 1 is vertical
                conds[counter] = cond1;
                counter++;
                conds[counter] = cond2;
                counter++;
            }
        }
        return conds;
    }

    public Condition[] BuildType8(){
        Condition[] conds = new Condition[81];
        int counter = 0;
        int i = 3;
        for (int row = 0 ; row < 28-i+1 ; row=row+i){
            for (int col = 0 ; col < 28 - i+1 ; col=col+i){
                int [] det = {(row+1)*28+col+1};
                Condition cond = new Condition(det, 8);
                conds[counter] = cond;
                counter++;
            }
        }
        return conds;
    }

    public Condition[] BuildType9(){
        Condition[] conds = new Condition[10];
        for (int i = 0; i < 10; i++){
            int[] det1 = {i};
            conds[i] = new Condition(det1, 9);
        }
        return conds;
    }

    public static void main(String[] args){
        int version = 2;
        double p = 20;
        double l = 8;
        //String train_set_file_name = "C:\\Users\\yuval\\IdeaProjects\\mini_mnist\\mnist_train.csv";
        //String output_tree_file_name = "C:\\Users\\yuval\\IdeaProjects\\mini_mnist\\out_tree.json";
        String train_set_file_name = "C:\\Users\\yuval\\IdeaProjects\\mini_mnist\\Kannada\\train.csv";
        String output_tree_file_name = "C:\\Users\\yuval\\IdeaProjects\\mini_mnist\\out_tree.json";
        ReaderCsv reader = new ReaderCsv();
        ArrayList<int[]> pics1 = reader.getCSVArray(train_set_file_name);

        Collections.shuffle(pics1);
        double div = p/100;
        double valid_set1 = (div)*pics1.size();
        int valid_set_size = (int) Math.round(valid_set1);
        List<int[]> bounderies;
        List<int[]> sharpImages;
        List<int[]> pics;
        DataBase db = null;
        Condition[] conds;
        Condition[][] condsver2 = new Condition[8][];
        if (version == 1){
            conds = new learningAlgorithm().BuildType1();
            pics = pics1;
        }
        else
        {
            pics = new ArrayList<>();
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

            sharpImages = new learningAlgorithm().SharpImage(pics1);
            System.out.println("Finish Sharps");
            bounderies = new learningAlgorithm().BoundImage(sharpImages);
            System.out.println("Finish Bounds");

            db = new DataBase(bounderies, sharpImages);

            db.CreateType6Data(pics1);
            db.CreateType7Data(pics);
            db.CreateType8Data(pics);

            Condition[] conds1 = new learningAlgorithm().BuildType1();
            Condition[] conds2 = new learningAlgorithm().BuildType2();
            Condition[] conds3 = new learningAlgorithm().BuildType3();
            Condition[] conds4 = new learningAlgorithm().BuildType4();
            Condition[] conds5 = new learningAlgorithm().BuildType5();
            Condition[] conds6 = new learningAlgorithm().BuildType6();
            Condition[] conds7 = new learningAlgorithm().BuildType7();
            Condition[] conds8 = new learningAlgorithm().BuildType8();
            Condition[] conds9 = new learningAlgorithm().BuildType9();

            conds = new Condition[conds1.length + conds2.length + conds3.length + conds4.length + conds5.length + conds6.length + conds7.length + conds8.length + conds9.length];
            condsver2[0] = conds7;
            condsver2[1] = conds2;
            condsver2[2] = conds3;
            condsver2[3] = conds4;
            condsver2[4] = conds5;
            condsver2[5] = conds6;
            condsver2[6] = conds8;
            condsver2[7] = conds9;
            //condsver2[8] = conds1;

       }

        List<int[]> valid_set = pics.subList(0, valid_set_size);
        List<int[]> train_set = pics.subList(valid_set_size, pics.size());

        if(version == 2) {
            db.CreateMeanImages(train_set);
            db.CreateType9Data(pics);
        }

//        for (int w = 1 ; w < 10 ; w++) {
//            System.out.println("Label: " + w);
//
//            System.out.println("mean bad");
//            System.out.print("[");
//            for (int f1 = 0; f1 < 784; f1++) {
//                System.out.print(db.means[w][f1] + ",");
//            }
//            System.out.println("");
//            System.out.println("means good");
//            System.out.print("[");
//            for (int f1 = 0; f1 < 784; f1++) {
//                System.out.print(db.better_means[w][f1] + ",");
//            }
//            System.out.println("");
//            System.out.println("histogram");
//            System.out.print("[");
//            for (int f1 = 0; f1 < 256; f1++) {
//                System.out.print(db.means_histogram[w][f1] + ",");
//            }
//            System.out.println("");
//        }


        //DELETE WHEN SUBMITION!!!
        for (int i = 0 ; i < 10 ; i ++){
            System.out.print("[");
            for (int j = 1; j < 785; j++){
                System.out.print(db.better_means[i][j] + ",");
            }
            System.out.println("]");
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
            List<BinTree> res_build = new learningAlgorithm().BuildDecisionTree(t, train_set, conds,  tree,  leaves,  id_leaf,  last_t, condsver2);
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
        System.out.println("Build final tree");
        id_leaf = 1;
        max_digit = tree.common_label(pics);
        tree = new BinTree(null, max_digit, id_leaf);
        id_leaf++;
        tree.examples = pics;
        last_t = 0;
        leaves = new ArrayList<>();
        leaves.add(tree);
        double final_t = Math.pow(2, max_l);
        List<BinTree> res_final = new learningAlgorithm().BuildDecisionTree(final_t, pics, conds, tree, leaves, id_leaf, last_t, condsver2);
        BinTree final_tree = res_final.remove(1);
        final_tree.clean();

        //Maybe Delete!!!
//        BinTree mean_node = new BinTree(conds[0],0,22222222);
//        mean_node.all_means_type_9 = db.better_means;
//        mean_node.right = final_tree;

        Gson gson = new Gson();
        try {
            FileWriter writer = new FileWriter(output_tree_file_name);
            //gson.toJson(final_tree, writer);
            ToPrediction to_pred = new ToPrediction(final_tree, db.better_means);
            gson.toJson(to_pred, writer);
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

    public List<int[]> SharpImage(List<int[]> pics){
        List <int[]> result = new ArrayList<>();
        int counter = 0;
        for (int[] img : pics){
            int [] new_img = new int [img.length];
            new_img[0] = img[0];
            for (int i = 1 ; i < img.length ; i++){
                if (img[i] > 64)
                    new_img[i] = 255;
                else
                    new_img[i] = 0;
            }
            result.add(counter, new_img);
            counter++;
        }
        return result;
    }

    public List<int[]> BoundImage(List<int[]> sharp_pics) {
        List <int[]> result = new ArrayList<>();
        int counter = 0;
        for (int[] img : sharp_pics){
            int [] new_img = new int [img.length];
            new_img[0] = img[0];
            for (int i = 1; i < 27; i++){
                for (int j = 1; j < 27; j++){
                    int left = img[i*28 + j] - img[i*28+j-1];
                    int right = img[i*28 + j] - img[i*28 + j + 1];
                    int up = img[i*28 + j] - img[(i+1)*28 + j];
                    int down = img[i*28 + j] - img[(i-1)*28 + j];
                    new_img[i*28 + j] = Math.max(Math.max(left, right),Math.max(up, down));
                }
            }
            result.add(counter, new_img);
            counter++;
        }
        return result;
    }
}
