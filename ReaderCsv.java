import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ReaderCsv {

    public ReaderCsv(){
    }

    public ArrayList<int[]> getCSVArray(String file_name){
        ArrayList<int []> pics = new ArrayList<>();
        String line = "";
        String splitby = ",";
        try {
            BufferedReader br = new BufferedReader(new FileReader(file_name));
            while ((line = br.readLine()) != null) {
                String[] st = line.split(splitby);
                int[] pic = new int[st.length];
                for (int i = 0 ; i < st.length ; i++){
                    pic[i] = Integer.parseInt(st[i]);
                }
                pics.add(pic);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return pics;
    }
}
