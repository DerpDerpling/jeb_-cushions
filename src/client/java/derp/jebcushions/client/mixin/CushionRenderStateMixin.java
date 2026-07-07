package derp.jebcushions.client.mixin;

import derp.jebcushions.client.access.CushionRenderStateAccess;
import net.minecraft.client.renderer.entity.state.CushionRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(CushionRenderState.class)
public abstract class CushionRenderStateMixin implements CushionRenderStateAccess {

    @Unique
    private int jebTint = -1;

    @Override
    public int jebCushions$getTintColor() {
        return jebTint;
    }

    @Override
    public void jebCushions$setTintColor(int color) {
        jebTint = color;
    }
}
