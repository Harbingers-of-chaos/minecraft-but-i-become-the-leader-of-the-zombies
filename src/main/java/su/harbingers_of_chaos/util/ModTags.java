package su.harbingers_of_chaos.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import static su.harbingers_of_chaos.Leaderofthezombies.MOD_ID;

public class ModTags {
    public static final TagKey<Item> COOKED_MEAT = ofItem("cooked_meat");
    public static final TagKey<EntityType<?>> INTELLIGENT_BEINGS = ofEntity("intelligent_beings");
    public static final TagKey<EntityType<?>> NOOVERMOB = ofEntity("noovermob");
    private static TagKey<Item> ofItem(String id) {
        return TagKey.of(RegistryKeys.ITEM, Identifier.of(MOD_ID, id));
    }
    private static TagKey<EntityType<?>> ofEntity(String id) {
        return TagKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(MOD_ID, id));
    }
}
