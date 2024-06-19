package net.nutils.rainbowtrims.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.nutils.rainbowtrims.RainbowTrimsConfig;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(HumanoidArmorLayer.class)
public abstract class HumanoidArmorLayerMixin<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> extends RenderLayer<T, M> {

    @Shadow @Final private TextureAtlas armorTrimAtlas;
    @Unique private static final RainbowTrimsConfig config = AutoConfig.getConfigHolder(RainbowTrimsConfig.class).getConfig();

    public HumanoidArmorLayerMixin(RenderLayerParent<T, M> parent) {
        super(parent);
    }

    @Inject(method = "renderTrim", at = @At("HEAD"), cancellable = true)
    public void renderInjection(Holder<ArmorMaterial> holder, PoseStack stack, MultiBufferSource source, int light, ArmorTrim trim, A model, boolean leggings, @NotNull CallbackInfo info) {
        info.cancel();
        if(config.hideTrims) return;
        var sprite = armorTrimAtlas.getSprite(leggings ? trim.innerTexture(holder) : trim.outerTexture(holder));
        var alpha = config.hideTrims ? 0F : 1F;
        if(config.showAnimation) {
            var consumer = sprite.wrap(ItemRenderer.getFoilBufferDirect(source, Sheets.armorTrimsSheet(trim.pattern().value().decal()), true, leggings));
            var effect = getEffectColor();
            model.renderToBuffer(stack, consumer, light, OverlayTexture.NO_OVERLAY, ((effect >> 16 & 255) << 16) | ((effect >> 8 & 255) << 8) | (effect & 255) | ((int)(alpha * 255) << 24));
        } else if(config.useCustomColor) {
            var consumer = sprite.wrap(source.getBuffer(Sheets.armorTrimsSheet(trim.pattern().value().decal())));
            model.renderToBuffer(stack, consumer, light, OverlayTexture.NO_OVERLAY, ((int) (alpha * 255) << 24) | ((int) (config.customColorRed * 255) << 16) | ((int) (config.customColorGreen * 255) << 8) | (int) (config.customColorBlue * 255));
        } else {
            var consumer = sprite.wrap(source.getBuffer(Sheets.armorTrimsSheet(trim.pattern().value().decal())));
            model.renderToBuffer(stack, consumer, light, OverlayTexture.NO_OVERLAY, 0xffffffff);
        }
    }

    @Unique
    private static int getEffectColor() {
        return Color.HSBtoRGB((float) (System.currentTimeMillis() % 7000) / config.animationSpeed, 1F, 1F);
    }

}