package com.ylinor.brawlator;

import com.ylinor.brawlator.data.beans.EffectBean;
import com.ylinor.brawlator.data.beans.MonsterBean;
import com.ylinor.brawlator.data.dao.MonsterDAO;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.PotionEffectData;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.effect.potion.PotionEffect;
import org.spongepowered.api.entity.ArmorEquipable;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.monster.Zombie;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.entity.spawn.SpawnTypes;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.*;

public class MonsterAction {

    /**
     * Invoque un monstre à une position donnée
     *
     * @param world Instance du monde
     * @param location Emplacement de spawn
     * @param monster Bean du monstre contenant ses caractéristiques
     * @return Entité (optionnelle) spawn
     */
    public static Optional<Entity> invokeMonster(World world, Location location, MonsterBean monster) {
        //  Vérification que le type du monstre existe pour le plugin
        if (!MonsterBean.monsterTypes.containsKey(monster.getType())) {
            return Optional.empty();
        }
        //  Création de l'entité
        Entity entity = world.createEntity(MonsterBean.monsterTypes.get(monster.getType()), location.getPosition());
        entity = editCharacteristics(entity, monster.getName(), monster.getHp(), monster.getAttackDamage(), monster.getSpeed(), monster.getKnockbackResistance());
        //  Gestion des effets de potion à donner au monstre
        entity = addEffects(entity, monster.getEffectLists());
        //  Gestion des objets appartenant au monstre
        if (entity instanceof ArmorEquipable) {
            HashMap<String, ItemType> items = new HashMap<>();
            items.put("hand", ItemTypes.WOODEN_SWORD);
            items.put("helmet", ItemTypes.LEATHER_HELMET);
            entity = (Entity)addItems((ArmorEquipable)entity, items);
        }
        //  Spawn de l'entité dans le monde
        Cause cause = Cause.source(SpawnTypes.PLUGIN).build();
        world.spawnEntity(entity, cause);
        return Optional.ofNullable(entity);
    }


    /**
     * Fixe les charactéristiques d'un monstre, et renvoie l'entité modifiée
     * @param entity Monstre à éditer
     * @param displayName Nom à afficher au dessus
     * @param hp Points de vie
     * @param attackDamage Puissance d'attaque
     * @param speed Vitesse de déplacement
     * @param knockbackResistance Résistance au recul
     * @return Entité modifiée
     */
    private static Entity editCharacteristics(Entity entity, String displayName, double hp, double attackDamage, double speed, int knockbackResistance) {
        entity.offer(Keys.DISPLAY_NAME, Text.of(displayName));
        entity.offer(Keys.MAX_HEALTH, hp);
        entity.offer(Keys.ATTACK_DAMAGE, attackDamage);
        entity.offer(Keys.WALKING_SPEED, speed);
        entity.offer(Keys.KNOCKBACK_STRENGTH, knockbackResistance);
        return entity;
    }

    /**
     * Ajoute des effets de potion à un monstre, et renvoie l'entité modifiée
     * @param entity Monstre auquel ajouter un effet
     * @param effects Liste des effets à ajouter
     * @return Entité modifiée
     */
    private static Entity addEffects(Entity entity, List<EffectBean> effects){
        List<PotionEffect> potions = new ArrayList();
        for (EffectBean effect :
             effects) {
            PotionEffect potion = PotionEffect.builder().potionType(EffectBean.effectTypes.get(effect.getType()))
                    .duration(effect.getDuration()).amplifier(effect.getAmplifier()).build();
            potions.add(potion);
        }
        PotionEffectData effectData = entity.getOrCreate(PotionEffectData.class).get();
        effectData.addElements(potions);
        entity.offer(effectData);
        return entity;
    }

    /**
     * Ajoute des objets à un monstre, et renvoie l'entité modifiée
     * @param entity Monstre à équiper
     * @param items Hashmap des objets à équiper avec leurs types
     * @return Entité modifiée
     */
    private static ArmorEquipable addItems(ArmorEquipable entity, HashMap<String, ItemType> items) {
        for (Map.Entry<String, ItemType> item : items.entrySet()) {
            ItemStack itemStack = ItemStack.builder().itemType(item.getValue()).build();
            switch (item.getKey().toString()) {
                case "hand":
                    entity.setItemInHand(HandTypes.MAIN_HAND, itemStack);
                    break;
                case "helmet":
                    entity.setHelmet(itemStack);
                    break;
                case "chestplate":
                    entity.setChestplate(itemStack);
                    break;
                case "boots":
                    entity.setBoots(itemStack);
                    break;
                case "leggings":
                    entity.setLeggings(itemStack);
                    break;
            }
        }
        return entity;
    }


    /**
     * Requête le DAO pour récupérer un monstre de la BDD
     *
     * @param id
     * @return
     */
    public static Optional<MonsterBean> getMonster(int id){
        MonsterDAO monsterDao = new MonsterDAO();
        return monsterDao.getMonster(id);
    }
}
