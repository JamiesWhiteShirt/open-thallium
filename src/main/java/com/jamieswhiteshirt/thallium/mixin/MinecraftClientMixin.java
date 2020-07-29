package com.jamieswhiteshirt.thallium.mixin;

import com.jamieswhiteshirt.thallium.Thallium;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Shadow @Final private Window window;

    @Shadow private int fpsCounter;

    @Inject(
        method = "render(Z)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/util/Window;swapBuffers()V",
            shift = At.Shift.AFTER
        )
    )
    private void inflateFPS(boolean tick, CallbackInfo ci) {
        GL11.glReadBuffer(GL11.GL_FRONT);
        // GL11.glDrawBuffer(GL11.GL_FRONT);
        GL11.glCopyPixels(0, 0, window.getFramebufferWidth(), window.getFramebufferHeight(), GL11.GL_COLOR);
        for (int i = 1; i < Thallium.fpsMultiplier; ++i) {
            // window.swapBuffers();
            ++fpsCounter;
            GLFW.glfwSwapBuffers(window.getHandle());
            // GLFW.glfwPollEvents();
        }
    }
}
