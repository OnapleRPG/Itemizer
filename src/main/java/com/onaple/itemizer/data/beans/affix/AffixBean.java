package com.onaple.itemizer.data.beans.affix;

import lombok.Data;
import lombok.NoArgsConstructor;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.List;

@Data
@NoArgsConstructor
@ConfigSerializable
public class AffixBean {

   @Setting("group")
   private String groupName;
   @Setting("tiers")
   private List<AffixFactory> tiers;

}
