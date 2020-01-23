
public class Condition {

    public int[] cond_details;
    public int cond_type;

    public Condition(int [] cond_details, int cond_type){
        this.cond_details = cond_details;
        this.cond_type = cond_type;
    }

    public Condition(){
        this.cond_details = null;
        this.cond_type = 0;
    }

    //CHECK IF WE NEED BOUND_PICS
    public boolean CheckCondition(int[] img){
        if (this.cond_type == 1){
            int img_index = this.cond_details[0];
            int more_than = this.cond_details[1];
            return img[img_index] > more_than;
        }
        else if(this.cond_type == 2 || this.cond_type == 3){
            int counter = 0;
            int top_left = this.cond_details[0];
            int more_than = this.cond_details[1];
            int num_of_cells = this.cond_details[2];
            for (int i=0; i<3; i++){
                for (int j=0; j<3 ; j++){
                    if (img[top_left+ i*28 + j] > more_than)
                        counter++;
                }
            }
            if (counter >= num_of_cells)
                return true;
            return false;
        }
        else if(this.cond_type == 4 || this.cond_type == 5){
            int counter = 0;
            int top_left = this.cond_details[0];
            int more_than = this.cond_details[1];
            int num_of_cells = this.cond_details[2];
            for (int i=0; i<4; i++){
                for (int j=0; j<4 ; j++){
                    if (img[top_left+ i*28 + j] > more_than)
                        counter++;
                }
            }
            if (counter >= num_of_cells)
                return true;
            return false;
        }
        else if(this.cond_type == 6){
            boolean is_row = false;
            boolean is_max = false;
            int row = this.cond_details[1];
            int col = this.cond_details[2];
            if (this.cond_details[0] == 1 || this.cond_details[0] == 3){
                is_row = true;
            }
            if (this.cond_details[0] == 3 || this.cond_details[0] == 4){
                is_max = true;
            }
            if (is_row){
                int this_square_value = 0;
                for(int i = 0; i < 4 ; i++){
                    for (int j = 0 ; j < 4 ; j++){
                        this_square_value += img[(row+i)*28 + col + j + 1];
                    }
                }
                for (int i = 0; i < 28; i+=4){
                    int sum = 0;
                    for (int j = 0; j < 4; j++) {
                        for (int k = 0; k < 4; k++) {
                            sum = sum + img[(row + j) * 28 + i + k + 1];
                        }
                    }
                    if ((is_max && sum > this_square_value) || (!is_max && sum < this_square_value)){
                        return false;
                    }
                }
                return true;
            }
            else{
                int this_square_value = 0;
                for(int i = 0; i < 4 ; i++){
                    for (int j = 0 ; j < 4 ; j++){
                        this_square_value += img[(row+i)*28 + col + j + 1];
                    }
                }
                for (int i = 0; i < 28; i+=4){
                    int sum = 0;
                    for (int j = 0; j < 4; j++) {
                        for (int k = 0; k < 4; k++) {
                            sum = sum + img[(i+j) * 28 + col + k + 1];
                        }
                    }
                    if ((is_max && sum > this_square_value) || (!is_max && sum < this_square_value)){
                        return false;
                    }
                }
                return true;
            }

        }
        return false;
    }
}
