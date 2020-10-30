# Stardust
 A Minecraft plugin for customizable shooting stars with custom loot
# Custom Loot Information
To add custom loot, you will need to create a loot table. To do this there are two options:
### Option 1
This method is required for items that are enchanted or have other NBT. First [create a datapack](https://minecraft.gamepedia.com/Tutorials/Creating_a_data_pack) and then [create a loot table](https://minecraft.gamepedia.com/Loot_table). **Set loot table type to chest, generic will cause errors.** Then, just edit Stardust/config.yml and change `loot_table` to your namespaced loot_table (for example: `test:test_loot`).
### Option 2
Using the config.yml templates for simple_loot_tables.common and simple_loot_tables.rare, define a custom simple loot table, for example, with the key `test`, and then for the star type, put the loot table as `stardust:test` to access that simple loot table.