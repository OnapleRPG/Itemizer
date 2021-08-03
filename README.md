# Itemizer | ![Github Action](https://github.com/OnapleRPG/Itemizer/actions/workflows/gradle.yml/badge.svg) [![Quality gate](https://sonarcloud.io/api/project_badges/measure?project=com.onaple%3AItemizer&metric=reliability_rating)](https://sonarcloud.io/dashboard?id=com.onaple%3AItemizer) [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0) [![sponge version](https://img.shields.io/badge/sponge-7.2.0-blue.svg)](https://www.spongepowered.org/)  


Itemizer is a Sponge Minecraft plugin that allows custom items creation. Store plenty of custom items in configurations files.  
Once an item is registered, you can also register :
* crafts : register new crafts (with or without shape) and smelting recipes
* pools : assign items to pools with a probability, and fetch them randomly
* affixes : for additionnal customization, items can be modified afterwards using affixes (also pickable in a pool) to slighly modify their characteristics.

An examples of each config file is loaded at the first launch of the plugin, under the folder `/config/itemizer/`. This way you can see how they're structured.  
>Don't be afraid by item config complexity, it can be generated from existing items in-game

## Installation
This plugin needs a sponge server 1.12. Download our [latest release](https://github.com/OnapleRPG/Itemizer/releases) and copy it into your server's `mods/` folder. Then restart your server.  
Check in the `/config/itemizer/` folder if default configuration is properly copied, and you should notice server logs mentionning the proper plugin loaded.  

## Minecraft Commands

* `/register <newId>` : register item in hand into the config. I advise you to use this [generator](http://mapmaking.fr/give1.12/) to generate the item to register.  
Once registered, the item will be saved into the configuration file, and you will be able to retrieve it.  
Permission : *itemizer.command.register*  
* `/retrieve <itemId> [quantity] [player]` : Obtain an item specified in the configuration file (see the `items.conf`) for the given id.  
Permission : *itemizer.command.rerieve*  
* `/fetch <poolId> [quantity] [player]` : Try to obtain an item from a configured pool (see the `pools.conf`) using its *id*.  
Permission : *itemizer.command.fetch*  
* `/has-item <player> <itemId> [quantity]` : check if a player has an specific item from configuration in his inventory  
Permission : *itemizer.command.hasitem*  
* `/reload-itemizer` : Reload all itemizer configuration files.  
Permission : *itemizer.command.reload*  

## Configuration files

All configuration files use HOCON format. They are loaded at plugin start. You can force a reload with the command `/reload-itemizer` (crafts will require a server restart).  
The configuration files are stored in the *config/itemizer* folder of your server. Default configuration files will be generated if they do not exist. You can also use a folder instead of a plain file, the plugin will then read every files within it (for instance, create a folder *items* instead of the *items.conf*, and put every HOCON files within it).  
> I highly recommend to backup your files to avoid any unwanted loss.  

### Item creation

A file named __*items.conf*__ stores the items that can be retrieved from the plugin.

The root element has to be __items__.  
For each item configured, the following data can be provided :  
* The mandatory `id` field is used as a key identifier
* The mandatory `item` contains all the item data. I advise you to generate the item with a [generator](http://mapmaking.fr/give1.12/) using the `/give` command and register it using `/register`.   
* __thirdParties__ allows third party plugin to update your item (by now only [Jessie tags](https://github.com/OnapleRPG/Jessie-Tags) is available)
* __Affix__ pick in a pool of affix to randomly affect item statistics.  

When starting the server with the plugin for the first time, [a default configuration](https://github.com/OnapleRPG/Itemizer/blob/master/src/main/resources/assets/itemizer/items.conf) will be generated (we didn't write those configurations manually).  

### Items pool

A file named __*pools.conf*__ defines pools to select item randomly from.  
When fetching from a pool, it will randomly gives you one (or none) of the listed items.  

The root element must be `pools`.  
* An `id` is used to enable a pool to be referenced.
* `items` is an array containing the items obtainable, it contains :
    * A `probability` between 0 and 1, giving the actual chances of having a given item. _ex: 0.2 gives you a 20% chance of obtaining that item. The last item in a pool can have a probability of 1, it would then be the default drop_  
    * An `Item` reference, it could be its _name_ , its _ref_ or directly is _type_
        * `ref` is used as a reference to a configured item id
        * `name` will be used if _ref_ is absent or if no item were returned to generate an item with a given type
        * you can use `type` instead of the above both to set a full item configuration (same as item in _item property_ in item.conf) 
    * Two quantities (optional) `maxQuantity` and `minQuantity`.  
      _if this item is fetched from the pool, you will obtain between minQuantity and maxQuantity of that item. By default 1._
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
* Used in all crafting recipes the, `result` must be an object containing one of the following fields:
    * `name` can be used with a string to reference a minecraft item
    * `ref` used with a number or a string can be used to retrieve an item from Itemizer's items.
* The `recipe` is used for ShapelessCrafting and Smelting only, and is used to define the item needed for the recipe. It must contain an object with one of the following fields:
    * `name` associated with a string for a minecraft item.
    * `ref` with a number or a string for the desired Itemizer id of the item.
* The `pattern` is used for ShapedCrafting only to define the pattern which will be used to craft an item *(example available and explained below)*
* The `ingredients` are used for ShapedCrafting to match pattern keys with ingredients:
    * Each different character used in the pattern must be used as a key with one of the following:
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
_The first craft requires a stone axe to craft the item referenced "1", the second craft enable us to cook a cooked_porkchop into a coal, and the third one is used to craft the item referenced "king_sword" with three sticks aligned in a vertical centered line (notice the whitespaces before and after the "a")_  
_In case you wanted to make the same recipe with a square pattern: `pattern : ["aaa", "a a", "aaa"]`_  

### Global configuration
In the global configuration file, you can change plugin settings for default names in item descriptions.  
* __RewriteParts__: instead of using default Minecraft notation for _enchantments_, _modifiers_ , _unbreakable_ , _canMine_ ; you can choose to change their name.  
For each key in this object, set `false` to let default look or `true` to rewrite it.
* __DefaulColor__: if you decide to rewrite a part, you can choose the color of each element in this object.  
* __EnchantRewrite__: you can change the name of an enchantment in this object. If you let an enchantment blank, it will not appear.
* __ModifierRewrite__: you can change an attribute name (AttributeModifier like `generic.attackDamage`) as you like in this object.  
* __CanMineRewrite__ and __UnbreakableRewrite__ are string to overwrite the default text of those traits.  

You can choose to edit the config file or use `/configure <Type> <Key> [value]` command. 
__Be careful using the command, the file will be overwritten__  

### Affix

Affix are buffs/debuffs that can be applied to items.  
They are pre-registered in pools (*not* related to the pools used for items). They are stored in the `affix.conf` file as an HOCON file.  


The root is `affixes`, it contains a list of affix groups.  

A group is categorized by its `group` name.  
it's a schematic name only used for configuration like damage, speed, balanced, only_negative, etc... It will be used to attach a group to an item.  

Then there are multiples `tiers` for each affix.  
A tier is like a power level. `__class__` is the implementation of the affix, you can use your own (leave it as is if you don't know what that mean).  

You can set a `prefix` and a `suffix` to modify the item name. Don't leave space before and after.  
The `probability` is the chance to choose this tiers more than another. the smaller it is, the less chance you have to get this tier. Be careful to stay under the probability of 1, otherwise the tier which total probability exceeds 1 will never get picked.  

Then, the `attributes` are the data modifiers which change the item statistics.  
You have 4 properties , `name`, `slot`, `amount` and `operation`. Value are the same than vanilla.  
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
You can also use it with js (like with customNPC).  

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
