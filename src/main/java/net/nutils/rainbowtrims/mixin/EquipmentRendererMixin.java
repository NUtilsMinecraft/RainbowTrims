package net.nutils.rainbowtrims.mixin;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.equipment.EquipmentModel;
import net.minecraft.client.render.entity.equipment.EquipmentModelLoader;
import net.minecraft.client.render.entity.equipment.EquipmentRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.equipment.EquipmentAsset;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Identifier;
import net.nutils.rainbowtrims.RainbowTrimsConfig;
import net.nutils.rainbowtrims.utils.ReflectionUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.util.function.Function;

@Mixin(EquipmentRenderer.class)
public abstract class EquipmentRendererMixin {

    @Shadow @Final private EquipmentModelLoader equipmentModelLoader;
    @Shadow private static int getDyeColor(EquipmentModel.Layer layer, int i) { return 0; }
    @Shadow @Final private Function<EquipmentRenderer.LayerTextureKey, Identifier> layerTextures;
    @Shadow @Final private Function<EquipmentRenderer.TrimSpriteKey, Sprite> trimSprites;

    @Unique private static final RainbowTrimsConfig config = AutoConfig.getConfigHolder(RainbowTrimsConfig.class).getConfig();

    @Inject(method = "render(Lnet/minecraft/client/render/entity/equipment/EquipmentModel$LayerType;Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/client/model/Model;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/util/Identifier;)V", at = @At("HEAD"), cancellable = true)
    public void render(EquipmentModel.LayerType layer, RegistryKey<EquipmentAsset> registry, Model model, ItemStack item, MatrixStack stack, VertexConsumerProvider provider, int light, @Nullable Identifier identifier, @NotNull CallbackInfo info) {
        info.cancel();
        var list = this.equipmentModelLoader.get(registry).getLayers(layer);
        if(list.isEmpty()) return;
        var color = item.isIn(ItemTags.DYEABLE) ? DyedColorComponent.getColor(item, 0) : 0;
        var glint = item.hasGlint();
        for(EquipmentModel.Layer layerEntry : list) {
            try {
                var dye = getDyeColor(layerEntry, color);
                if(dye != 0) {
                    var textureKey = layerEntry.comp_3173() && identifier != null ? identifier : this.layerTextures.apply(ReflectionUtils.createLayerTextureKey(layer, layerEntry));
                    var consumer = ItemRenderer.getArmorGlintConsumer(provider, RenderLayer.getArmorCutoutNoCull(textureKey), glint);
                    model.render(stack, consumer, light, OverlayTexture.DEFAULT_UV, dye);
                    glint = false;
                }
            } catch(Exception exception) {
                System.out.println("Unable to render!");
            }
        }
        var trim = item.get(DataComponentTypes.TRIM);
        if(trim != null) {
            try {
                var sprite = this.trimSprites.apply(ReflectionUtils.createTrimSpriteKey(trim, layer, registry));
                var consumer = sprite.getTextureSpecificVertexConsumer(provider.getBuffer(TexturedRenderLayers.getArmorTrims(trim.pattern().comp_349().comp_1905())));
                var alpha = config.hideTrims ? 0F : 1F;
                if(config.hideTrims) return;
                if(config.showAnimation) {
                    var effect = getEffect();
                    model.render(stack, consumer, light, OverlayTexture.DEFAULT_UV,
                            ((effect >> 16 & 255) << 16) | ((effect >> 8 & 255) << 8) | (effect & 255) | ((int) (alpha * 255) << 24));
                    return;
                }
                if(config.useCustomColor) {
                    model.render(stack, consumer, light, OverlayTexture.DEFAULT_UV,
                            ((config.customColorRed * 255) << 16) | ((config.customColorGreen * 255) << 8) | (config.customColorBlue * 255) | ((int) alpha * 255) << 24);
                    return;
                }
                model.render(stack, consumer, light, OverlayTexture.DEFAULT_UV);
            } catch(Exception exception) {
                System.out.println("Unable to render!");
            }
        }
    }

    @Unique
    private static int getEffect() {
        return Color.HSBtoRGB((float) (System.currentTimeMillis() % 7000) / config.animationSpeed, 1F, 1F);
    }

}
