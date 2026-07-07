package derp.jebcushions.client.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import derp.jebcushions.client.access.CushionRenderStateAccess;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.CushionRenderer;
import net.minecraft.client.renderer.entity.state.CushionRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.decoration.Cushion;
import net.minecraft.world.item.DyeColor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.EnumMap;

@Mixin(CushionRenderer.class)
public class CushionRendererMixin {

	@Shadow @Final
	private static EnumMap<DyeColor, Identifier> TEXTURES_BY_COLOR;

	@Inject(method = "extractRenderState(Lnet/minecraft/world/entity/decoration/Cushion;Lnet/minecraft/client/renderer/entity/state/CushionRenderState;F)V", at = @At("TAIL"))
	private void jebCushions$extract(Cushion cushion, CushionRenderState state, float partialTicks, CallbackInfo ci) {
		CushionRenderStateAccess access = (CushionRenderStateAccess) state;

		if (!cushion.hasCustomName() || !"jeb_".equals(cushion.getName().getString())) {
			access.jebCushions$setTintColor(-1);
			return;
		}

		state.texture = TEXTURES_BY_COLOR.get(DyeColor.WHITE);

		DyeColor[] colors = DyeColor.values();

		int interval = 25;
		int offset = cushion.getId() * 7;
		float time = cushion.tickCount + partialTicks + offset;

		int indexA = ((int) (time / interval)) % colors.length;
		int indexB = (indexA + 1) % colors.length;
		float progress = (time % interval) / interval;

		int colorA = colors[indexA].getTextureDiffuseColor();
		int colorB = colors[indexB].getTextureDiffuseColor();

		int blended = jebCushions$lerpColor(progress, colorA, colorB);

		access.jebCushions$setTintColor(0xFF000000 | blended);
	}

	@Redirect(method = "submit(Lnet/minecraft/client/renderer/entity/state/CushionRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/rendertype/RenderType;III)V"))
	private <S> void jebCushions$submitTintedModel(SubmitNodeCollector collector, Model<? super S> model, S state, PoseStack poseStack, RenderType renderType, int lightCoords, int overlayCoords, int outlineColor) {
		int tint = -1;

		if (state instanceof CushionRenderStateAccess access) {
			tint = access.jebCushions$getTintColor();
		}

		collector.submitModel(model, state, poseStack, renderType, lightCoords, overlayCoords, tint, null, outlineColor);
	}

	@Unique
    private static int jebCushions$lerpColor(float progress, int colorA, int colorB) {
		int ar = colorA >> 16 & 255;
		int ag = colorA >> 8 & 255;
		int ab = colorA & 255;

		int br = colorB >> 16 & 255;
		int bg = colorB >> 8 & 255;
		int bb = colorB & 255;

		int r = (int) (ar + (br - ar) * progress);
		int g = (int) (ag + (bg - ag) * progress);
		int b = (int) (ab + (bb - ab) * progress);

		return r << 16 | g << 8 | b;
	}
}