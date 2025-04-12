package net.nutils.rainbowtrims.utils;

import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.trim.ArmorTrim;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ReflectionUtils {

    public static EquipmentLayerRenderer.@NotNull LayerTextureKey createLayerTextureKey(EquipmentClientInfo.LayerType type, EquipmentClientInfo.Layer layer)
        throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Constructor<?> constructor = Class.forName("net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer$LayerTextureKey")
            .getDeclaredConstructor(EquipmentClientInfo.LayerType.class, EquipmentClientInfo.Layer.class);
        constructor.setAccessible(true);
        return (EquipmentLayerRenderer.LayerTextureKey) constructor.newInstance(type, layer);
    }

    public static EquipmentLayerRenderer.@NotNull TrimSpriteKey createTrimSpriteKey(ArmorTrim trim, EquipmentClientInfo.LayerType type, ResourceKey<EquipmentAsset> resourceKey)
        throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Constructor<?> constructor = Class.forName("net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer$TrimSpriteKey")
            .getDeclaredConstructor(ArmorTrim.class, EquipmentClientInfo.LayerType.class, ResourceKey.class);
        constructor.setAccessible(true);
        return (EquipmentLayerRenderer.TrimSpriteKey) constructor.newInstance(trim, type, resourceKey);
    }

}
