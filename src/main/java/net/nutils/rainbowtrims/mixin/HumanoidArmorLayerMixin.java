package net.nutils.rainbowtrims.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
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
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.nutils.rainbowtrims.RainbowTrimsConfig;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
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
    public void renderInjection(ArmorMaterial material, PoseStack stack, MultiBufferSource source, int light, ArmorTrim trim, A model, boolean inner, CallbackInfo info) {
        if(config.isEnabled) {
            TextureAtlasSprite sprite = this.armorTrimAtlas.getSprite(inner ? trim.innerTexture(material) : trim.outerTexture(material));
            VertexConsumer consumer = sprite.wrap(ItemRenderer.getFoilBufferDirect(source, Sheets.armorTrimsSheet(trim.pattern().value().decal()), true, inner));
            int effect = getEffectColor();
            float red = (float) (effect >> 16 & 255) / 255F;
            float green = (float) (effect >> 8 & 255) / 255;
            float blue = (float) (effect & 255) / 255;
            model.renderToBuffer(stack, consumer, light, OverlayTexture.NO_OVERLAY, red, green, blue, config.hideTrims ? 0F : 1F);
        } else {
            TextureAtlasSprite sprite = this.armorTrimAtlas.getSprite(inner ? trim.innerTexture(material) : trim.outerTexture(material));
            VertexConsumer consumer = sprite.wrap(source.getBuffer(Sheets.armorTrimsSheet(trim.pattern().value().decal())));
            model.renderToBuffer(stack, consumer, light, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
        }
        info.cancel();
    }

    @Unique
    private static int getEffectColor() {
        return Color.HSBtoRGB((float) (System.currentTimeMillis() % 7000) / config.speed, 1F, 1F);
    }

}