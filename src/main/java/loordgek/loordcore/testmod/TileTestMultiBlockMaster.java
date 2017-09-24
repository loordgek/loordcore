package loordgek.loordcore.testmod;

import loordgek.loordcore.tile.MultiBlockCompnent;
import loordgek.loordcore.tile.MultiBlockPattern;
import loordgek.loordcore.tile.TileMultiBlockMaster;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TileTestMultiBlockMaster extends TileMultiBlockMaster<TileTestMultiBlockMaster> {
    private static final List<MultiBlockPattern> pat = new ArrayList<>();

    static {
        char[][][] map = {
                {
                        {'T', 'T'},
                        {'T', 'M'},
                },
                {
                        {'T', 'T'},
                        {'T', 'T'},
                },
        };
        pat.add(new MultiBlockPattern(map){
            @Override
            public Map<Character, MultiBlockCompnent> multiBlockMap() {
                Map<Character, MultiBlockCompnent> objectMap = new HashMap<>();
                objectMap.put('T', new MultiBlockCompnent(null, TileTestMultiBlock.class));
                objectMap.put('M', new MultiBlockCompnent(null, TileTestMultiBlockMaster.class));
                return objectMap;
            }
        });
    }

    protected TileTestMultiBlockMaster() {
        super(pat);
    }

    @Override
    public Map<Character, Object> multiBlockMap() {
        Map<Character, Object> objectMap = new HashMap<>();
        objectMap.put('T', TileTestMultiBlock.class);
        objectMap.put('M', TileTestMultiBlockMaster.class);
        return objectMap;
    }
}
