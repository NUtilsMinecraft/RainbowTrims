package net.nutils.rainbowtrims.mixin;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.feature.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.registry.entry.RegistryEntry;
import net.nutils.rainbowtrims.RainbowTrimsConfig;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(ArmorFeatureRenderer.class)
public abstract class ArmorFeatureRendererMixin<T extends LivingEntity, M extends BipedEntityModel<T>, A extends BipedEntityModel<T>> extends FeatureRenderer<T, M> {

    @Shadow @Final private SpriteAtlasTexture armorTrimsAtlas;
    @Unique private static final RainbowTrimsConfig config = AutoConfig.getConfigHolder(RainbowTrimsConfig.class).getConfig();

    public ArmorFeatureRendererMixin(FeatureRendererContext<T, M> parent) {
        super(parent);
    }

    @Inject(method = "renderTrim", at = @At("HEAD"), cancellable = true)
    public void renderInjection(RegistryEntry<ArmorMaterial> material, MatrixStack stack, VertexConsumerProvider provider, int light, ArmorTrim trim, A model, boolean leggings, @NotNull CallbackInfo info) {
        info.cancel();
        if(config.isEnabled) {
            var sprite = this.armorTrimsAtlas.getSprite(leggings ? trim.getLeggingsModelId(material) : trim.getGenericModelId(material));
            var consumer = sprite.getTextureSpecificVertexConsumer(ItemRenderer.getDirectItemGlintConsumer(provider, TexturedRenderLayers.getArmorTrims(trim.getPattern().value().decal()),
                    true, leggings));
            var effect = getEffectColor();
            model.render(stack, consumer, light, OverlayTexture.DEFAULT_UV, ((float) (effect >> 16 & 255) / 255F), ((float) (effect >> 8 & 255) / 255F), ((float) (effect & 255) / 255F),
                    /*config.hideTrims ? 0F : */1F);
        } else {
            var sprite = this.armorTrimsAtlas.getSprite(leggings ? trim.getLeggingsModelId(material) : trim.getGenericModelId(material));
            var consumer = sprite.getTextureSpecificVertexConsumer(provider.getBuffer(TexturedRenderLayers.getArmorTrims(trim.getPattern().value().decal())));
            model.render(stack, consumer, light, OverlayTexture.DEFAULT_UV, 1F, 1F, 1F, 1F);
        }
    }

    @Unique
    private static int getEffectColor() {
        return Color.HSBtoRGB((float) (System.currentTimeMillis() % 7000) / config.speed, 1F, 1F);
    }

}