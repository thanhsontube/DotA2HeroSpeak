package son.nt.dota2.ottobus_entry;

import son.nt.dota2.dto.CircleFeatureDto;

/**
 * Created by sonnt on 12/4/16.
 */

public class GoCircle {
    public CircleFeatureDto mCircleFeatureDto;
    public int posotion;

    public GoCircle(CircleFeatureDto circleFeatureDto, int posotion) {
        mCircleFeatureDto = circleFeatureDto;
        this.posotion = posotion;
    }
}
