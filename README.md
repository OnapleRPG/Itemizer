# Itemizer  [![Build Status](https://travis-ci.org/OnapleRPG/Itemizer.svg?branch=master)](https://travis-ci.org/OnapleRPG/Itemizer) ![Sonarcloud Status](https://sonarcloud.io/api/project_badges/measure?project=Itemizer&metric=alert_status)   [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)


Itemizer is a Sponge Minecraft plugin that allow custom item creation as described in configuration files, as well as random item generation.

The plugin implements the command "__/retrieve *\<id\>*__", which gives a configured item to the player.  
It also implements "__/fetch *\<id\>*__", which gets a random item from a given item pool.

# Get Started
## Installation
To install this plugin you must have a sponge server 1.12. Download the file [on this link](https://github.com/Ylinor/Itemizer/releases/download/V1.0/Itemizer.jar) and drag and drop it into your server's `mods/` folder. Then restart your server.

## Configuration files

All configuration files use HOCON format. When you first install the plugin a default configuration with example is loaded in your `config/itemizer/` folder.

### Item creation

A file named __*itemizer_items.conf*__ stores the items that can be retrieved from the plugin.

The root element has to be __items__. 
For each item configured, the following data can be provided :
* The mandatory __id__ field is used as a key identifier
* The mandatory __type__ is the item type that will be generated
* __name__ can override default item name
* __lore__ can be set to provide an item description
* __unbreakable__ is a boolean to prevent tool consumption, defaulting to false
* __enchants__ is a list of enchants that will be added to the item
* __miners__ are references to harvesting profiles, that enable to break blocks when the item is held (_see next section_)
  
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
        name: "Training stick",
        miners: [
            1
        ]
    }
]
```

### Harvesting capabilities

A file named __*itemizer_miners.conf*__ defines profiles to allow block destruction when given items are in hand.

The root element must be __miners__.
For each profile, you can define the following elements :
* __id__ is used to reference the profile from an item configuration
* __mine_types__ is a list of blocks that are breakable with the associated profile
* __inherit__ is an optional list of ids to include all the blocks of different profiles

```
miners = [
    {
        id: 1,
        mine_types: [
            "minecraft:coal_ore",
            "minecraft:iron_ore"
        ]
    },
    {
        id: 2,
        mine_types: [
            "minecraft:gold_ore"
        ],
        inherit: [
            1
        ]
    }
]
```
_An item referencing miner 1 will be able to break coal and iron ore, whereas referencing miner 2 allow breaking of coal, iron and gold ore._

### Items pool

A file named __*itemizer_pools.conf*__ defines pools to select item randomly from.

The root element must be __pools__.
* An __id__ is used to enable a pool to be referenced.
* __items__ is an array containing the items obtainable, it contains :
    * A __probability__ between 0 and 1, giving the actual chances of having a given item. _The last item in a pool can have a probability of 1, it would then be the default drop_
    * __ref__ is used as a reference to a configured item id (see _item creation_)
    * __type__ will be used if _ref_ is absent or if no item were returned to generate an item with a given type

```
pools = [
    {
        id: 1,
        items: [
            {
                probability: 0.5,
                type: "minecraft:cooked_porkchop"
            },
            {
                probability: 0.2,
                ref: 1
            }
        ]
    }
]
```
_The first pool of item has a 50% chance of getting a porkchop, 20% chance of getting the configured item with id 1, and 30% chance of getting nothing._
