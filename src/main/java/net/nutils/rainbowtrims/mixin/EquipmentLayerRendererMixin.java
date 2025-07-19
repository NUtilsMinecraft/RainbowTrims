package net.nutils.rainbowtrims.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.EquipmentAssetManager;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.nutils.rainbowtrims.RainbowTrimsConfig;
import net.nutils.rainbowtrims.utils.ReflectionUtils;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.util.function.Function;

@Mixin(EquipmentLayerRenderer.class)
public abstract class EquipmentLayerRendererMixin {

    @Shadow @Final private EquipmentAssetManager equipmentAssets;
    @Shadow private static int getColorForLayer(EquipmentClientInfo.Layer layer, int i) { return 0; }
    @Shadow @Final private Function<EquipmentLayerRenderer.LayerTextureKey, ResourceLocation> layerTextureLookup;
    @Shadow @Final private Function<EquipmentLayerRenderer.TrimSpriteKey, TextureAtlasSprite> trimSpriteLookup;

    @Unique private static final RainbowTrimsConfig config = AutoConfig.getConfigHolder(RainbowTrimsConfig.class).getConfig();

    @Inject(method = "renderLayers(Lnet/minecraft/client/resources/model/EquipmentClientInfo$LayerType;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/client/model/Model;Lnet/minecraft/world/item/ItemStack;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/resources/ResourceLocation;)V", at = @At("HEAD"), cancellable = true)
    public void render(EquipmentClientInfo.LayerType type, ResourceKey<EquipmentAsset> key, Model model, ItemStack item, PoseStack pose, MultiBufferSource source,
                       int light, ResourceLocation identifier, @NotNull CallbackInfo info) {
        info.cancel();

        var list = this.equipmentAssets.get(key).getLayers(type);
        if(list.isEmpty()) return;

        var color = item.is(ItemTags.DYEABLE) ? DyedItemColor.getOrDefault(item, 0) : 0;
        var glint = item.hasFoil();

        for(EquipmentClientInfo.Layer entry : list) {
            try {
                var dye = getColorForLayer(entry, color);
                if(dye != 0) {
                    var textureKey = entry.usePlayerTexture() && identifier != null ? identifier : this.layerTextureLookup.apply(ReflectionUtils.createLayerTextureKey(type, entry));
                    var consumer = ItemRenderer.getArmorFoilBuffer(source, RenderType.armorCutoutNoCull(textureKey), glint);

                    model.renderToBuffer(pose, consumer, light, OverlayTexture.NO_OVERLAY, dye);
                    glint = false;
                }
            } catch(Exception exception) {
                System.out.println(exception.getMessage());
                exception.printStackTrace();
            }
        }

        var trim = item.get(DataComponents.TRIM);
        if(trim != null) {
            try {
                var sprite = this.trimSpriteLookup.apply(ReflectionUtils.createTrimSpriteKey(trim, type, key));
                var consumer = sprite.wrap(source.getBuffer(Sheets.armorTrimsSheet(trim.pattern().value().decal())));
                var alpha = config.hideTrims ? 0F : 1F;

                if(config.hideTrims) return;

                if(config.showAnimation) {
                    var effect = getEffect();
                    model.renderToBuffer(pose, consumer, light, OverlayTexture.NO_OVERLAY,
                            ((effect >> 16 & 255) << 16) | ((effect >> 8 & 255) << 8) | (effect & 255) | ((int) (alpha * 255) << 24));
                    return;
                }
                if(config.useCustomColor) {
                    model.renderToBuffer(pose, consumer, light, OverlayTexture.NO_OVERLAY,
                            ((config.customColorRed * 255) << 16) | ((config.customColorGreen * 255) << 8) | (config.customColorBlue * 255) | ((int) alpha * 255) << 24);
                    return;
                }
                model.renderToBuffer(pose, consumer, light, OverlayTexture.NO_OVERLAY);
            } catch(Exception exception) {
                System.out.println(exception.getMessage());
                exception.printStackTrace();
            }
        }
    }

    @Unique
    private static int getEffect() {
        return Color.HSBtoRGB((float) (System.currentTimeMillis() % 7000) / config.animationSpeed, 1F, 1F);
    }

}
