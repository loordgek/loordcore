package loordgek.loordcore.network;

import lombok.Getter;
import lombok.Setter;

public class PowerSync {
    @Setter
    @Getter
    private int energy;
    @Setter
    @Getter
    private int energystore;

    public PowerSync(int energy, int energystore) {
        this.energy = energy;
        this.energystore = energystore;
    }
}
