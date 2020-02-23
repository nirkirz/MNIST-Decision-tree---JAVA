import java.util.ArrayList;
import java.util.List;

public class DataBase {
    private static DataBase instance = null;

    public List<int[]> bounds;
    public List<int[]> sharps;
    public List<int[]> type6_data;
    public List<int[]> type7_data;
    public List<int[]> type8_data;
    public List<int[]> type9_data;
    public int[][] means;
    public int[][] better_means;
    public int[][] means_histogram;

    public DataBase(List<int[]> b, List<int[]> s){
        bounds = b;
        sharps = s;
        means = null;
        means_histogram = null;
        better_means = null;
        //real_means = null; //Maybe delete
        type6_data = new ArrayList<>();
        type7_data = new ArrayList<>();
        type8_data = new ArrayList<>();
        type9_data = new ArrayList<>();
        instance = this;
    }

    public static DataBase getInstance(){
        return instance;
    }

    public void CreateMeanImages(List<int[]> train_set){
        System.out.println("Create means start");
        int[][] result = new int[10][785];
        int[][] result_histo = new int[10][];
        int[][] result_better_means = new int[10][785];
        int[] counters = new int [10];
        for (int[] img : train_set){
            for (int i = 1 ; i < 785 ; i++)
                result[img[0]][i] = result[img[0]][i] + img[i];
            counters[img[0]]++;
        }
        for (int i = 0 ; i < 10 ; i++)
            for (int j = 1 ; j < 785 ; j++)
                result[i][j] = result[i][j]/counters[i];
        this.means = result;

        for (int i = 0; i < 10; i++){
            result_histo[i] = CalculateHistogram(result[i]);
        }
        this.means_histogram = result_histo;

        for (int i = 0; i < 10; i++){
            int min = 0;
            int max = 256;

            for (int j = 255; j >= 1; j--){
                if (means_histogram[i][j] != 0){
                    max = j;
                    break;
                }
            }
            for (int j = 255-max; j < 256; j++){
                if (means_histogram[i][j] != 0){
                    min = j;
                    break;
                }
            }

            int[] cdf = new int[256];
            cdf[min] = means_histogram[i][min];
            for (int j = min+1 ; j < 256; j++){
                cdf[j] = cdf[j-1] + means_histogram[i][j];
            }

            int[] better_img = new int[785];
            int[] new_pixel_values = new int[256];
            for (int j = min; j <= max; j++){
                double x = ((cdf[j] - cdf[min] + 0.0)/(cdf[max]-cdf[min]))*255;
                new_pixel_values[j] = (int) Math.round(x);
            }
            for (int j = 1; j < 785; j++){
                if (means[i][j]>=min && means[i][j]<=max)
                    better_img[j] = new_pixel_values[means[i][j]];
                else
                    better_img[j] = means[i][j];
            }
            result_better_means[i] = better_img;
        }
        this.better_means = result_better_means;

        //Maybe delete
//        if (this.real_means == null)
//            this.better_means = result_better_means;
//        else
//            this.better_means = real_means;
        System.out.println("Create means end");
    }

    public void CreateType6Data(List<int[]> pics){
        int counter = 0;
        for (int[] img : pics){
            int[] data = new int[785];
            for (int type = 1 ; type <= 4 ; type++){
                for (int i = 0; i<28 ; i+=4) {
                    for (int j = 0; j < 28; j += 4) {
                        boolean res = check_min_max(img, i, j, type);
                        if (res){
                            data[i*28+j+1+(type-1)] = 1;
                        }
                    }
                }
            }
            type6_data.add(counter, data);
            counter++;
        }
    }

    public void CreateType7Data(List<int[]> pics){
        int counter = 0;
        for (int[] img : pics){
            int [] data = new int [785];
            for (int i = 1; i < 5; i++){ // i means num of tested block
                for (int is_hori = 0; is_hori <= 1 ; is_hori++) {
                    for (int j = 13; j >= 3; j--) {
                        boolean res;
                        if (is_hori == 0) {
                            res = find_lines(img, 0, j, (i - 1) * 28 * 7 + 1);
                            if (res){
                                data[(i - 1) * 28 * 7 + 1] = j;
                                break;
                            }
                        }
                        else {
                            res = find_lines(img, 1, j, (i - 1) * 7 + 1);
                            if (res){
                                data[(i - 1) * 28 * 7 + 1 + 1] = j;
                                break;
                            }
                        }
                    }
                }
            }
            type7_data.add(counter, data);
            counter++;
        }
    }

    public void CreateType8Data(List<int[]> pics){
        int counter = 0;
        for (int [] img : pics){
            int[] data = new int[785];
            int i = 3;
            for (int row = 0 ; row < 28-i+1 ; row=row+i){
                for (int col = 0 ; col < 28 - i+1 ; col=col+i) {
                    boolean res = check_holes(img, (row+1)*28+col+1);
                    if (res)
                        data[(row+1)*28+col+1] = 1;
                }
            }
            type8_data.add(counter,data);
            counter++;
        }
    }

    public void CreateType9Data(List<int[]> pics){
        int counter = 0;
        int place = 0;
        for(int[] img : pics){
            int[] results = new int[10];
            int ind_im = 0;
            for (int[] mean_img : better_means){
                int total_whites = 0;
                int same_whites = 0;
                int total_blacks = 0;
                int same_blacks = 0;
                int mean_whites = 0;
                for (int i = 1; i < 785; i++){
                    if (img[i] >= 64){
                        total_whites++;
                        if(mean_img[i] >= 50)
                            same_whites++;
                    }
                    else{
                        total_blacks++;
                        if (mean_img[i] < 50)
                            same_blacks++;

                    }
                    if (mean_img[i] >= 50)
                        mean_whites++;
                }
                double percent_whites = (same_whites + 0.0)/total_whites;
                double percent_blacks = (same_blacks + 0.0)/total_blacks;
                //if (Math.abs(total_whites-mean_whites) <= 80 && percent_whites >= 0.6)
                if (percent_whites >= 0.6 && percent_blacks >= 0.6 && Math.abs(total_whites-mean_whites) <= 100)
                    results[ind_im] = 1;
                ind_im++;
            }
            type9_data.add(place, results);
            place++;
        }
    }

    public boolean check_min_max(int [] img, int row, int col, int type){
        boolean is_row = false;
        boolean is_max = false;
        if (type == 1 || type == 3) {
            is_row = true;
        }
        if (type == 3 || type == 4) {
            is_max = true;
        }
        if (is_row) {
            //Calculate the sum of the cell in the tested square
            int this_square_value = 0;
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    this_square_value += img[(row + i) * 28 + col + j + 1];
                }
            }
            //Check sum in all other squares in the row/col
            for (int i = 0; i < 28; i += 4) {
                int sum = 0;
                for (int j = 0; j < 4; j++) {
                    for (int k = 0; k < 4; k++) {
                        sum = sum + img[(row + j) * 28 + i + k + 1];
                    }
                }
                if ((is_max && sum > this_square_value) || (!is_max && sum < this_square_value)) {
                    return false;
                }
            }
            return true;
        } else {
            int this_square_value = 0;
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    this_square_value += img[(row + i) * 28 + col + j + 1];
                }
            }
            for (int i = 0; i < 28; i += 4) {
                int sum = 0;
                for (int j = 0; j < 4; j++) {
                    for (int k = 0; k < 4; k++) {
                        sum = sum + img[(i + j) * 28 + col + k + 1];
                    }
                }
                if ((is_max && sum > this_square_value) || (!is_max && sum < this_square_value)) {
                    return false;
                }
            }
            return true;
        }
    }

    public boolean find_lines(int[] img, int is_hori, int len, int block){
        int ind = img[785];
        int[] bound = this.bounds.get(ind);
        if (is_hori == 0) { // horizontal
            int start_of_row = block;
            for (int i = start_of_row; i < start_of_row + 28 * 7; i = i + 28) {
                int counter = 0;
                for (int j = 0; j < 28; j++) {
                    if (bound[i + j] == 255) {
                        counter++;
                        if (counter == len) {
                            return true;
                        }
                    }
                    else {
                        counter = 0;
                    }
                }
            }
            return false;
        }
        else{
            int start_of_col = block;
            for (int i = start_of_col; i < start_of_col +  7; i++) {
                int counter = 0;
                for (int j = 0; j < 28*28; j=j+28) {
                    if (bound[i + j] == 255) {
                        counter++;
                        if (counter == len) {
                            return true;
                        }
                    }
                    else {
                        counter = 0;
                    }
                }
            }
            return false;
        }

    }

    public boolean check_holes(int[] img, int center){
        int index = img[785];
        int[] sharp = sharps.get(index);
        int loc = center;
        boolean indicator = false;
        if (sharp[loc] == 0){
            indicator = true;
        }
        else if (sharp[loc-1] == 0){
            loc--;
            indicator = true;
        }
        else if (sharp[loc+1] == 0){
            loc++;
            indicator = true;
        }
        else if (sharp[loc-28] == 0){
            loc = loc -28;
            indicator = true;
        }
        else if (sharp[loc+28] == 0){
            loc = loc + 28;
            indicator = true;
        }
        else if (sharp[loc-27] == 0){
            loc = loc -27;
            indicator = true;
        }
        else if (sharp[loc+27] == 0){
            loc = loc + 27;
            indicator = true;
        }
        else if (sharp[loc-29] == 0){
            loc = loc -29;
            indicator = true;
        }
        else if (sharp[loc+29] == 0){
            loc = loc + 29;
            indicator = true;
        }
        if (indicator == false)
            return false;
        else
            return isHole(loc, sharp);
    }

    public boolean isHole(int loc, int[] img){
        int[][] img_2d = new int[28][28];
        for (int i = 0 ; i < 28 ; i++){
            for (int j = 0 ; j < 28 ; j++){
                img_2d[i][j] = img[28*i+j+1];
            }
        }
        boolean total_flag = true;

        int col = loc%28;
        int row = loc/28;
        int global_col = col;
        int global_row = row;
        int global_iter = 784;
        int local_iter = global_iter;

        while(row >=0 && local_iter >= 0){
            if (img_2d[row][col] == 255)
                break;
            else {
                row--;
                local_iter--;
            }

        }
        if (row < 0 || local_iter < 0)
            total_flag = false;

        col = global_col;
        row = global_row;
        local_iter = global_iter;
        while(row <=27 && total_flag && local_iter >= 0){
            if (img_2d[row][col] == 255)
                break;
            else {
                row++;
                local_iter--;
            }

        }
        if (row == 28 || local_iter < 0)
            total_flag = false;

        col = global_col;
        row = global_row;
        local_iter = global_iter;
        while(col <=27 && total_flag && local_iter>=0){
            if (img_2d[row][col] == 255)
                break;
            else {
                col++;
                local_iter--;
            }
        }
        if (col == 28 || local_iter<0)
            total_flag = false;

        col = global_col;
        row = global_row;
        local_iter = global_iter;
        while(col >= 0 && total_flag && local_iter>=0){
            if (img_2d[row][col] == 255)
                break;
            else {
                col--;
                local_iter--;
            }
        }
        if (col < 0 || local_iter<0)
            total_flag = false;

        col = global_col;
        row = global_row;
        local_iter = global_iter;
        while(col >= 0 && row >= 0 && total_flag && local_iter>=0){
            if (img_2d[row][col] == 255)
                break;
            else{
                col--;
                row--;
                local_iter--;
            }
        }
        if (col < 0 || row < 0 || local_iter<0)
            total_flag = false;

        col = global_col;
        row = global_row;
        local_iter = global_iter;
        while(col <= 27 && row >= 0 && total_flag && local_iter >=0 ){
            if (img_2d[row][col] == 255)
                break;
            else{
                col++;
                row--;
                local_iter--;
            }
        }
        if (col == 28 || row < 0 || local_iter < 0)
            total_flag = false;

        col = global_col;
        row = global_row;
        local_iter = global_iter;
        while(col <= 27 && row <= 27 && total_flag && local_iter >= 0){
            if (img_2d[row][col] == 255)
                break;
            else{
                col++;
                row++;
                local_iter--;
            }
        }
        if (col == 28 || row == 28 || local_iter < 0)
            total_flag = false;

        col = global_col;
        row = global_row;
        local_iter = global_iter;
        while(col >= 0 && row <= 27 && total_flag && local_iter >= 0){
            if (img_2d[row][col] == 255)
                break;
            else{
                col--;
                row++;
                local_iter--;
            }
        }
        if (col < 0 || row == 28 || local_iter <0)
            total_flag = false;
        return total_flag;
    }

    public int[] CalculateHistogram(int[] img){
        int[] data = new int [256];
        for (int i = 1; i < 785; i++){
            data[img[i]]++;
        }
        return data;
    }
}
