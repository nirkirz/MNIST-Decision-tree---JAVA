import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ReaderCsv {

    /**
     * Class that used to read from csv file the train and test sets
     */
    public ReaderCsv(){
    }

    public ArrayList<int[]> getCSVArray(String file_name){
        ArrayList<int []> pics = new ArrayList<>();
        String line = "";
        String splitby = ",";
        try {
            BufferedReader br = new BufferedReader(new FileReader(file_name));
            boolean legal_row;
            while ((line = br.readLine()) != null) {
                legal_row = true;
                String[] st = line.split(splitby);

                int[] pic = new int[st.length];
                for (int i = 0 ; i < st.length ; i++){
                    try {
                        pic[i] = Integer.parseInt(st[i]);
                    }
                    catch (Exception e){
                        legal_row = false;
                        break;
                    }
                }
                if (legal_row)
                    pics.add(pic);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return pics;
    }
}
