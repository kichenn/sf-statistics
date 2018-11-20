package type.immutable;

import java.util.List;

public class NLUData {
    public String query;
    public int nlpState;
    public int stateCode;
    public List<WordPosMGC> segmentMGC;
    public List<WordPos> segment;
    public String namedEntities;
    public String sentenceType;
    public String personNE;
};
