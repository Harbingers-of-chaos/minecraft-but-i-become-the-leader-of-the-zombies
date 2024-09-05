package su.harbingers_of_chaos.util;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import static su.harbingers_of_chaos.Leaderofthezombies.MOD_ID;

public class ModTags {
    public static final TagKey<Item> COOKED_MEAT = of("cooked_meat");
    private static TagKey<Item> of(String id) {
        return TagKey.of(RegistryKeys.ITEM, Identifier.of(MOD_ID, id));
    }
}
