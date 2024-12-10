package net.nutils.rainbowtrims.utils;

import net.minecraft.client.render.entity.equipment.EquipmentModel;
import net.minecraft.client.render.entity.equipment.EquipmentRenderer;
import net.minecraft.item.equipment.EquipmentAsset;
import net.minecraft.item.equipment.trim.ArmorTrim;
import net.minecraft.registry.RegistryKey;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ReflectionUtils {

    @SuppressWarnings("unchecked")
    public static EquipmentRenderer.@NotNull LayerTextureKey createLayerTextureKey(EquipmentModel.LayerType type, EquipmentModel.Layer layer) throws ClassNotFoundException,
            NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        var constructor = (Constructor<EquipmentRenderer.LayerTextureKey>) Class.forName("net.minecraft.client.render.entity.equipment.EquipmentRenderer$LayerTextureKey")
                .getDeclaredConstructor(EquipmentModel.LayerType.class, EquipmentModel.Layer.class);
        constructor.setAccessible(true);
        return constructor.newInstance(type, layer);
    }

    @SuppressWarnings("unchecked")
    public static EquipmentRenderer.@NotNull TrimSpriteKey createTrimSpriteKey(ArmorTrim trim, EquipmentModel.LayerType type, RegistryKey<EquipmentAsset> identifier) throws ClassNotFoundException,
            NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        var constructor = (Constructor<EquipmentRenderer.TrimSpriteKey>) Class.forName("net.minecraft.client.render.entity.equipment.EquipmentRenderer$TrimSpriteKey")
                .getDeclaredConstructor(ArmorTrim.class, EquipmentModel.LayerType.class, RegistryKey.class);
        constructor.setAccessible(true);
        return constructor.newInstance(trim, type, identifier);
    }

}
