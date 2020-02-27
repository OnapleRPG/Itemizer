# Itemizer  [![Build Status](https://travis-ci.org/OnapleRPG/Itemizer.svg?branch=master)](https://travis-ci.org/OnapleRPG/Itemizer) ![Sonarcloud Status](https://sonarcloud.io/api/project_badges/measure?project=Itemizer&metric=alert_status)   [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0) [![sponge version](https://img.shields.io/badge/sponge-7.2.0-yellow.svg)](https://www.spongepowered.org/)


Itemizer is a Sponge Minecraft plugin that allows custom item creation as described in configuration files, as well as random item generation.  
You can edit most of the item's informations like :  

* Item name
* Item lore
* Enchantements
* Durability
* *can mine* property
* Attribute modifiers
    * *maxHealth*
    * *followRange*
    * *knockbackResistance*
    * *movementSpeed*
    * *attackDamage*
    * *armor*
    * *armorToughness*
    * *attackSpeed*
    * *luck*  
If you want more details about *AttributeModifiers* check the Minecraft [wiki](https://minecraft.gamepedia.com/Attribute)


## Installation
To install this plugin, you must have a sponge server 1.12. Download the [latest release](https://github.com/OnapleRPG/Itemizer/releases) and drag and drop it into your server's `mods/` folder. Then restart your server.

## Minecraft Commands

* `/retrieve <itemId> [quantity] [player]` : Obtain an item specified in the configuration file for the given id.  
Permission : *itemizer.command.rerieve*
* `fetch <poolId> [quantity] [player]` : Try to obtain an item from a configured pool in the configuration file with its *id*.  
Permission : *itemizer.command.fetch*
* ``reload-itemizer`` : Reload each configuration file.  
Permission : *itemizer.command.reload*
* ``/analyse`` : give information about data stored in the item hold in main 
Permission : *itemizer.command.analyse*
* ``/register <newId>`` : register item into the config with his *name*,*lore*,*durability*,*enchants* and *attributes*

## Configuration files

All configuration files use HOCON format. When you first install the plugin a default configuration with example is loaded in your `config/itemizer/` folder.

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
For each item configured, the following data can be provided :
* The mandatory __id__ field is used as a key identifier
* The mandatory __type__ is the item type that will be generated
* __name__ can override default item name
* __lore__ can be set to provide an item description
* __unbreakable__ is a boolean to prevent tool consumption, defaulting to false
* __durability__ is the amount of uses a tool can be used before breaking
* __enchants__ is a list of enchants that will be added to the item
* __miners__ are references to harvesting profiles, that enable to break blocks when the item is held (_see next section_)
* __attributes__ is a list of modification, an attribute is defined by :
    * The __name__ of modifier (example : `generic.attackDamage`) you can get the
    complete list [here](https://minecraft.gamepedia.com/Attribute).
    * The __amount__ of the attribute.
    * The __operation__ is how the amount is applied. 0 for an addition,
    1 for a additive percent,and 2 for a multiplicative percent.
    * The __slot__ is where the item must be for applying the attribute. It can be `head` ,`mainhand`
    `offhand`, `chest`, `legs` or `feet`. 
    
    You can also add any NBT you want to your item, with its path and its value.
#### Example
```
items = [
    {
        id: 1,
        type: "minecraft:stone_axe",
        lore: "This axe is not really efficient...\nHowever it is sharp on your finger.",
        unbreakable: true,
        enchants {
            efficiency {level = 3}
        },
        miners: [
            2
        ]
    },
    {
        id: 2,
        type: "wooden_sword",
        durability: 5,
        name: "Training stick",
        attributes : [
            {
            name: "generic.attackDamage"
            amount : 5
            operation : 0
            slot : "mainhand"
            }
        ]
    }
]
```

### Harvesting capabilities

A file named __*miners.conf*__ defines profiles to allow block destruction when given items are in hand.

The root element must be __miners__.
For each profile, you can define the following elements :
* __id__ is used to reference the profile from an item configuration
* __mine_types__ is a list of blocks that are breakable with the associated profile
* __inherit__ is an optional list of ids to include all the blocks of different profiles

```
miners = [
    {
        id: 1,
        mine_types: {
           "coal" : "minecraft:coal_ore",
           "iron" : "minecraft:iron_ore"
        }
    },
    {
        id: 2,
        mine_types: {
            "gold" :"minecraft:gold_ore"
        },
        inherit: [
            1
        ]
    }
]
```
_An item referencing miner 1 will be able to break coal and iron ore, whereas referencing miner 2 allow breaking of coal, iron and gold ore._

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
* The `type` can be of three types : *ShapelessCrafting*, *Smelting* or *ShapedCrafting*.
* The result must be an object containing one of the following fields :
    * Using `name` followed by a string can be used to reference a minecraft item.
    * Using `ref` with a number or a string can be used to retrieve an item from Itemizer's items.
* The `recipe` is used for ShapelessCrafting and Smelting to define the item needed for the recipe. It must contain an object containing the following element :
    * `name` associated with a string to match a minecraft item
* The `pattern` is used for ShapedCrafting to define the pattern which will be used to craft an item *(An example is available below)*
* The `ingredients` are used to match the characters used in the pattern for a ShapedCrafting.
    * Each different character used in the pattern must be used as a key with the following object :
        * An object with "name" as a key and the item name as a value

```
crafts = [
  {
    id : 1
    type : "ShapelessCrafting"
    result : {ref : 1},
    recipe : {name : "stone_axe"}
  },
  {
    id : 2
    type : "Smelting"
    result : {name : "coal"},
    recipe : {name : "cooked_porkchop"}
  },
  {
    id : 3
    type : "ShapedCrafting"
    result : {ref : 2},
    pattern : [" a "," a "," a "],
    ingredients : {
      a : {name: "stick"},
    }
  }
]
```
_The first craft requires a stone axe to craft the item referenced "1", the second craft enable us to cook a cooked_porkchop into a coal, 
and the third one is used to craft the item referenced "2" with three sticks aligned in a vertical centered line (notice the whitespaces before and after the "a")_

## For developer
If you are willing to use **Itemizer** in your plugin development, we provide services to ease interactions.  
 
### Services
* **IItemService** : Give access to the object getters functions to a plugin.
    * ```Optional<ItemStack retrieve(String id)``` : Try to retrieve a configured item.
    * ```Optional<ItemStack> fetch(String id)``` : Try to fetch an item from a configured item pool.
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
      implementation 'com.github.OnapleRPG:Itemizer:V1.1.0'
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
 
