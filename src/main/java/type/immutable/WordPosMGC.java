package type.immutable;

/**
 * Created by yuao on 19/12/17.
 */
public class WordPosMGC extends WordPos {
    private Boolean isMGC = false;

    public WordPosMGC(String word, String pos, boolean isMGC) {
        super(word, pos);
        this.isMGC = isMGC;
    }

    public Boolean getMGC() {
        return isMGC;
    }
}
