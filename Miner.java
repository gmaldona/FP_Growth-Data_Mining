import java.util.ArrayList;

public abstract class Miner {
    
    protected int minSupport = 300;

    public int calculateSupport(String item, ArrayList<ArrayList<String>> transactions) {

        int supportCount = 0;

        for (ArrayList<String> trans : transactions) {
            if (trans.contains(item)) supportCount++; 
        }

        return supportCount;
    }
}