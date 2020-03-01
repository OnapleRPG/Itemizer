# Itemizer  ![Java CI](https://github.com/OnapleRPG/Itemizer/workflows/Java%20CI/badge.svg?branch=master) ![Sonarcloud Status](https://sonarcloud.io/api/project_badges/measure?project=Itemizer&metric=alert_status)   [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0) [![sponge version](https://img.shields.io/badge/sponge-7.2.0-yellow.svg)](https://www.spongepowered.org/)


Itemizer is a Sponge Minecraft plugin that allows custom item creation. Store plenty of custom objects in configurations files.
 Once item registered, you can also register :
 * crafts : register crafts (with or without shape) and smelting recipe.
 * pools : assign items to pools with a probability and fetch them randmly.
 * affixes : for more customisation item can be modified afterward by affix (also pick in a pool) to slighly modify their characteristics.

An examples of each config file is loaded at the first launch of the plugin under the folder `/config/itemizer/`.This way you can see how they're build.
>Don't be afraid by item complexity, they can be generated in-game)

## Installation
To install this plugin, you must have a sponge server 1.12. Download the [latest release](https://github.com/OnapleRPG/Itemizer/releases) and drag and drop it into your server's `mods/` folder. Then restart your server.
Check in the `/config/itemizer/` folder if default configuration is well copied and in logs if files is well loaded.

## Minecraft Commands

* `/register <newId>` : register item into the config. I advise you to use this [generator](http://mapmaking.fr/give1.12/). to give you the item before registering them.
once registered, you will see te item in your configuration file and you will be able to edit it.
Permission : *itemizer.command.register*
* `/retrieve <itemId> [quantity] [player]` : Obtain an item specified in the configuration file for the given id.  
Permission : *itemizer.command.rerieve*
* `/fetch <poolId> [quantity] [player]` : Try to obtain an item from a configured pool in the configuration file with its *id*.
Permission : *itemizer.command.fetch*
* `/has-item <player> <itemId> [quantity]` : check if a player has an specific item in his inventory
Permission : *itemizer.command.hasitem*
* `/reload-itemizer` : Reload each configuration file.
Permission : *itemizer.command.reload*

## Configuration files

All configuration files use HOCON format. they are loaded at the plugin start. you can force a reload with the command `/reload-itemizer`
> I higly recommend you to backup your files to avoid any loss.

### Global  configuration
In the global configuration file you can change plugin settings : 
* __RewriteParts__ instead of using default Minecraft notation for _enchantments_, _modifiers_ , _undreakable_ , _canMine_ .
You can chose to rewrite them manually. set `False` to let default look or `True` to rewrite It.
* __DefaulColor__ if you decide to rewrite a part you can chose the color of each elements.
* __EnchantRewrite__ you can change the name of enchantment. if you let an enchantment blank, it will not appear.
* __ModifierRewrite__ (AttributeModifier like `generic.attackDamage`) you can rewrite his name.
* __CanMineRewrite__ and __UnbreakableRewrite__ set the text of your choice for it.

You can chose to edit the config file or use `/configure <Type> <Key> [value]` command. 

__Be careful the command override the file change__


### Item creation

A file named __*items.conf*__ stores the items that can be retrieved from the plugin.

The root element has to be __items__.
For each  item you have the following properties :
For each item configured, the following data can be provided :
* The mandatory `id` field is used as a key identifier
* The mandatory `item` who contain all the item data. I advise you to generate the data with a `/give` command and register it. sadly I haven't implemented update yet
* __thirdParties__ allows third party plugin to update your item (by now only [Jessie tags](https://github.com/OnapleRPG/Jessie-Tags) is available)
* __Affix__ pick in a pool of affix to randomly affect item statistics.

#### Example
An example of an battle axe indexed as _barbarian_axe_ with some attribute modifiers and an affix in _damage_ pool.
```
    {
        id="barbarian_axe"
        item {
            ContentVersion=2
            Count=1
            Data=[
                {
                    ContentVersion=2
                    ManipulatorData {
                        "." {
                            id="barbarian_axe"
                        }
                        ContentVersion=1
                    }
                    ManipulatorId="itemizer:item.id"
                }
            ]
            ItemType="minecraft:iron_axe"
            UnsafeDamage=0
            UnsafeData {
                AttributeModifiers=[
                    {
                        Amount=5
                        AttributeName="generic.attackDamage"
                        Name="generic.attackDamage"
                        Operation=0
                        Slot=mainhand
                        UUIDLeast=160005
                        UUIDMost=13658
                    },
                    {
                        Amount=-0.1
                        AttributeName="generic.attackSpeed"
                        Name="generic.attackSpeed"
                        Operation=1
                        Slot=mainhand
                        UUIDLeast=169991
                        UUIDMost=15894
                    }
                ]
                display {
                    Lore=[
                        "Nobody have whet this blade yet",
                        "however it still can sharp your finger"
                    ]
                    Name="Barbarian Axe"
                }
            }
        }
        thirdParties=[],
         affix=damage
    }
```
don't be afraid all data in `item` are generated.

### Items pool

A file named __*pools.conf*__ defines pools to select item randomly from.

The root element must be `pools`.
* An `id` is used to enable a pool to be referenced.
* `items` is an array containing the items obtainable, it contains :
    * A `probability` between 0 and 1, giving the actual chances of having a given item. _The last item in a pool can have a probability of 1, it would then be the default drop_
    * An `Item` reference, it could be its _name_ or its _ref_ 
        * `ref` is used as a reference to a configured item id
        * `type` will be used if _ref_ is absent or if no item were returned to generate an item with a given type
    * Two quantity (optional) `maxQuantity` and `minDauntity` 
```
pools = [
  {
    id: 1,
    items: [
      {
        probability: 0.5,
        item : {name: "minecraft:cooked_porkchop"},
        maxQuantity : 3, #default 1
        minQuantity : 1  #default 1
      },
      {
        probability: 0.2,
        item : {ref: "training_stick"}
      }
    ]
  }
]
```
_The first pool of item has a 50% chance of getting a porkchop, 20% chance of getting the configured item with id 1, and 30% chance of getting nothing._

### Craft configuration

A file named __*crafts.conf*__ defines new crafts to be implemented in game.

The root element must be `crafts`.
* An `id` must be defined.
* The `__class__` can be of three types : *com.onaple.itemizer.data.beans.recipes.ShapelessCrafting*, *com.onaple.itemizer.data.beans.recipes.Smelting* or *com.onaple.itemizer.data.beans.recipes.ShapedCrafting*.
* Used in all crafting recipes the the result must be an object containing one of the following fields:
    * Using `name` followed by a string can be used to reference a minecraft item.
    * Using `ref` with a number or a string can be used to retrieve an item from Itemizer's items.
* The `recipe` is used for ShapelessCrafting and Smelting only and is used to define the item needed for the recipe. It must contain an object containing one of the following fields:
    * Using `name` associated with a string to match a minecraft item.
    * Using `ref` with a number or a string for the desired Itemizer id of the item.
* The `pattern` is used for ShapedCrafting only to define the pattern which will be used to craft an item *(An example is available and explained below)*
* The `ingredients` are used to match the characters used in the pattern for a ShapedCrafting.
    * Each different character used in the pattern must be used as a key with the following object :
        * An object with "name" as a key and the item name as a value (e.g: `{name: "minecraft:stick"}`)
        * An object with "ref" as a key and the itemizer id as a value (e.g: `{ref: 1}`)

```

crafts = [
  {
    __class__ : "com.onaple.itemizer.data.beans.recipes.ShapelessCrafting"
    id : 1
    result : {ref : 1},
    recipe : {name : "stone_axe"}
  },
  {
    __class__ : "com.onaple.itemizer.data.beans.recipes.Smelting"
    id : 2
    result : {name : "coal"},
    recipe : {name : "cooked_porkchop"}
  },
  {
    __class__ : "com.onaple.itemizer.data.beans.recipes.ShapedCrafting"
    id : 3
    result : {ref : "king_sword"},
    pattern : [" a "," a "," a "],
    ingredients : {
      a : {name: "minecraft:stick"},
    }
  }
]
```
_The first craft requires a stone axe to craft the item referenced "1", the second craft enable us to cook a cooked_porkchop into a coal, 
and the third one is used to craft the item referenced "2" with three sticks aligned in a vertical centered line (notice the whitespaces before and after the "a")_

### Affix

Affix are modifiers who can be applied to items. They are pre-registered in pools (different from the pools used for items). They are stored 
in the `affix.conf` file as an HOCON file.
The root is `affixes`, it contains a list of affix groups.  
   
 A group is categorized by its `group` name.
it's a schematic name only used for configuration like damage, speed, balanced, only_negative, etc... It will be 
used to attach a group to an item.
   
 And then the multiples `tiers` of each affix.
 A tier is like a power level. `__class__` is the implementation of the affix, you can use your own
 (leave it as is if you don't know what that mean). 
   
   
 You can set a `prefix` and `suffix` which modify the name item. Don't leave space before and after,
 these arguments will be set afterward.  
 The `probability` is the chance to choose this tiers more than another. the smaller it is, the less chance you have
 to get this tier. Be careful to stay under the probability of one otherwise the tier which total probability exceeds 1 will never get picked.
   
 And then : the **attributes**, `attributes` are the modifiers of the data who change the item statistics.
 you have 4 properties , `name`, `slot`, `amount` and `operation`. value are the same than vanilla.
If you want more details about *AttributeModifiers* check the Minecraft [wiki](https://minecraft.gamepedia.com/Attribute)
  
  
```
affixes = [
     {
       group:damage
       tiers : [
         {
           __class__ : com.onaple.itemizer.data.beans.affix.AffixTier
           suffix : "of stupidity"
           probability : 1
           attributes : [
             {
               name : attackDamage
               slot: mainHand
               amount : 10
               operation :0
             }
           ]
         }
       ]
     }
   ]
```
## For developer
If you are willing to use **Itemizer** in your plugin development, we provide services to ease interactions.  
you can also use it with js (like with customNPC).

you can check the [javadoc](https://onaplerpg.github.io/Itemizer/javadoc/) for more information.
 
### Services
* **IItemService** : Give access to the object getters functions to a plugin.  

   | Return |  Method  |
   | :--- | :--- |
   | `org.spongepowered.api.item.inventory.ItemStack` | 	`construct(ItemBean item)` |
   | `java.util.Optional<org.spongepowered.api.item.inventory.ItemStack>` |	`fetch(java.lang.String id)` |
   | `boolean` |	`hasItem(org.spongepowered.api.entity.living.player.Player player, java.lang.String id, int quantity)` |
   | `void` |	`instanciate(org.spongepowered.api.item.inventory.ItemStack itemStack, java.lang.String worldName, double x, double y, double z)` |
   | `void` |	`instantiate(org.spongepowered.api.item.inventory.ItemStack itemStack, org.spongepowered.api.world.Location<org.spongepowered.api.world.World> location)` |
   | `void` |	`register(java.lang.String id, org.spongepowered.api.item.inventory.ItemStackSnapshot snapshot)` |
   | `void` |	`removeItem(org.spongepowered.api.entity.living.player.Player player, java.lang.String id, java.lang.Integer quantity)` |
   | `java.util.Optional<org.spongepowered.api.item.inventory.ItemStack>` |	`retrieve(java.lang.String id)` |
   | `java.util.Optional<org.spongepowered.api.item.inventory.ItemStack>` |	`retrieve(java.lang.String id, int qte)` |
   
    ### Installation with Gradle
 
 * Add [Jitpack](https://jitpack.io/) into your repositories
 ```
   repositories {
     mavenCentral()
     maven {
         name = 'jitpack'
         url = 'https://jitpack.io'
     }
 }  
 ```
 * Add **Itemizer** to your dependencies
 ```
 dependencies {
      compile 'org.spongepowered:spongeapi:7.0.0'
      implementation 'com.github.OnapleRPG:Itemizer:V3.2'
  }
 ```
 * Use services 
 ```java
Optional<IItemService> optionalIItemService = Sponge.getServiceManager().provide(IItemService.class);
            if (optionalIItemService.isPresent()) {
                IItemService iItemService = optionalIItemService.get();
                optionalItem = iItemService.retrieve(itemId);
            }
```
 
