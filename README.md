# Itemizer

Itemizer is a Sponge Minecraft plugin that allow custom item creation as described in configuration files.

The plugin implements the command "__/retrieve *\<id\>*__", which gives a configured item to the player.

## Configuration files

All configuration files use HOCON format.

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