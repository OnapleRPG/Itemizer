package com.onaple.itemizer.data.beans.affix;


import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.List;

@ConfigSerializable
public class AffixBean {

   public AffixBean() {
   }

   public String getGroupName() {
      return groupName;
   }

   public void setGroupName(String groupName) {
      this.groupName = groupName;
   }

   public List<AffixFactory> getTiers() {
      return tiers;
   }

   public void setTiers(List<AffixFactory> tiers) {
      this.tiers = tiers;
   }

   @Setting("group")
   private String groupName;
   @Setting("tiers")
   private List<AffixFactory> tiers;

}
