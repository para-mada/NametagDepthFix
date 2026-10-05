package net.paramada.nametagdepthfix.mixin.client;

import net.minecraft.client.renderer.SubmitNodeCollection;
import net.minecraft.client.renderer.feature.phase.SimpleFeatureRenderPhase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Defers depth-tested nametags until after translucent terrain has been submitted.
 *
 * <p>This fixes MC-162693 without touching the separate see-through phase. With
 * improved transparency enabled, both phases already point at the same OIT
 * phase, so this redirect intentionally becomes a no-op.</p>
 */
@Mixin(SubmitNodeCollection.class)
public abstract class SubmitNodeCollectionMixin {
    @Redirect(
            method = "submitNameTagPart",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/renderer/SubmitNodeCollection;nameTags:Lnet/minecraft/client/renderer/feature/phase/SimpleFeatureRenderPhase;"
            )
    )
    private SimpleFeatureRenderPhase nametagDepthFix$deferDepthTestedNameTags(
            SubmitNodeCollection collection
    ) {
        return collection.afterTerrain;
    }
}
