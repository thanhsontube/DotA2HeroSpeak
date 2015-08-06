package son.nt.dota2.dto;

import java.util.ArrayList;
import java.util.List;

import son.nt.dota2.base.AObject;

/**
 * Created by Sonnt on 7/17/15.
 *
 * ﹕ >>>text:  Cast Time: 0.3+0Self Damage Bonus: 100%/150%/200%Allies Bonus Radius: 0 (900*)Allies Attack Damage Bonus: 0 (50%/75%/100%*)Buff Duration: 25
 07-17 17:26:15.540  21532-22635/dev.son.nt.dota2 D/AbilitiesLoader﹕ >>>Integer:5
 07-17 17:26:15.540  21532-22635/dev.son.nt.dota2 D/AbilitiesLoader﹕ >>>a1:CastTime:0.3+0
 07-17 17:26:15.540  21532-22635/dev.son.nt.dota2 D/AbilitiesLoader﹕ >>>a1:SelfDamageBonus:100%/150%/200%
 07-17 17:26:15.540  21532-22635/dev.son.nt.dota2 D/AbilitiesLoader﹕ >>>a1:AlliesBonusRadius:0(900*)
 07-17 17:26:15.540  21532-22635/dev.son.nt.dota2 D/AbilitiesLoader﹕ >>>a1:AlliesAttackDamageBonus:0(50%/75%/100%*)
 07-17 17:26:15.540  21532-22635/dev.son.nt.dota2 D/AbilitiesLoader﹕ >>>a1:BuffDuration:25
 */
public class AbilityLevelDto extends AObject {
    public String heroId;
    public String abilityName;
    public String name; //SelfDamageBonus
    public String value;
    public List <String> list = new ArrayList<>(); //100%/150%/200%
}
