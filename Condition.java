public class Condition {

    public int[] cond_details;
    public int cond_type;

    /**
     * Class which saves all required information of specific cond to check on an img
     * @param cond_details
     * @param cond_type
     */
    public Condition(int[] cond_details, int cond_type) {
        this.cond_details = cond_details;
        this.cond_type = cond_type;
    }

    public Condition() {
        this.cond_details = null;
        this.cond_type = 0;
    }

    /**
     * check if this Condition is true or flase on input img
     * @param img
     * @return
     */
    public boolean CheckCondition(int[] img) {
        if (this.cond_type == 1) {
            int img_index = this.cond_details[0];
            int more_than = this.cond_details[1];
            return img[img_index] > more_than;
        } else if (this.cond_type == 2 || this.cond_type == 3) {
            int counter = 0;
            int top_left = this.cond_details[0];
            int more_than = this.cond_details[1];
            int num_of_cells = this.cond_details[2];
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (img[top_left + i * 28 + j] > more_than)
                        counter++;
                }
            }
            if (counter >= num_of_cells)
                return true;
            return false;
        } else if (this.cond_type == 4 || this.cond_type == 5) {
            int counter = 0;
            int top_left = this.cond_details[0];
            int more_than = this.cond_details[1];
            int num_of_cells = this.cond_details[2];
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    if (img[top_left + i * 28 + j] > more_than)
                        counter++;
                }
            }
            if (counter >= num_of_cells)
                return true;
            return false;
        }
        else if (this.cond_type == 6) {
            DataBase db = DataBase.getInstance();
            int type = cond_details[0];
            int row = cond_details[1];
            int col = cond_details[2];
            if (db.type6_data.get(img[785])[row*28+col+1+(type-1)] == 1)
                return true;
            return false;

        }
        else if (this.cond_type == 7) {
            DataBase db = DataBase.getInstance();
            int is_hori = cond_details[0];
            int block = cond_details[1];
            int len = cond_details[2];
            if(db.type7_data.get(img[785])[block+is_hori]>=len)
                return true;
            return false;
        }
        else if(this.cond_type == 8){
            DataBase db = DataBase.getInstance();
            int center = cond_details[0];
            if (db.type8_data.get(img[785])[center] == 1)
                return true;
            return false;
        }
        else if (this.cond_type == 9){
            int dig = cond_details[0];
            DataBase db = DataBase.getInstance();
            int counter = 0;
            if (db.type9_data.get(img[785])[dig] == 1)
                return true;
            return false;
        }
        return false;
    }
}
