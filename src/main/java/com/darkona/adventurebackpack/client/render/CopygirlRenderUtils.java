package com.darkona.adventurebackpack.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 copygirl
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
 * Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
@SideOnly(Side.CLIENT)
public final class CopygirlRenderUtils {

    private static final ResourceLocation glint = new ResourceLocation("textures/misc/enchanted_item_glint.png");

    private CopygirlRenderUtils() {}

    public static void renderItemIn3d(ItemStack stack) {
        TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
        // Not sure why but this can be null when the world loads.
        if (textureManager == null) {
            return;
        }
        Item item = stack.getItem();
        GL11.glPushAttrib(
                GL11.GL_COLOR_BUFFER_BIT | GL11.GL_LIGHTING_BIT | GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_TRANSFORM_BIT);
        GL11.glPushMatrix();

        Tessellator tessellator = Tessellator.instance;
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
        GL11.glTranslatef(-0.5F, -0.5F, 1 / 32.0F);

        int passes = item.getRenderPasses(stack.getItemDamage());
        for (int pass = 0; pass < passes; pass++) {
            textureManager.bindTexture(
                    ((stack.getItemSpriteNumber() == 0) ? TextureMap.locationBlocksTexture
                            : TextureMap.locationItemsTexture));
            IIcon icon = item.getIcon(stack, pass);
            if (icon != null) {
                float minU = icon.getMinU();
                float maxU = icon.getMaxU();
                float minV = icon.getMinV();
                float maxV = icon.getMaxV();
                CopygirlRenderUtils.setColorFromInt(item.getColorFromItemStack(stack, pass));
                ItemRenderer.renderItemIn2D(
                        tessellator,
                        maxU,
                        minV,
                        minU,
                        maxV,
                        icon.getIconWidth(),
                        icon.getIconHeight(),
                        0.0625F);
            }
        }

        if (stack.hasEffect(0)) {
            GL11.glDepthFunc(GL11.GL_EQUAL);
            GL11.glDisable(GL11.GL_LIGHTING);
            textureManager.bindTexture(glint);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
            float f7 = 0.76F;
            GL11.glColor4f(0.5F * f7, 0.25F * f7, 0.8F * f7, 1.0F);
            GL11.glMatrixMode(GL11.GL_TEXTURE);
            GL11.glPushMatrix();
            float f8 = 0.125F;
            GL11.glScalef(f8, f8, f8);
            float f9 = Minecraft.getSystemTime() % 3000L / 3000.0F * 8.0F;
            GL11.glTranslatef(f9, 0.0F, 0.0F);
            GL11.glRotatef(-50.0F, 0.0F, 0.0F, 1.0F);
            ItemRenderer.renderItemIn2D(tessellator, 0.0F, 0.0F, 1.0F, 1.0F, 256, 256, 0.0625F);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glScalef(f8, f8, f8);
            f9 = Minecraft.getSystemTime() % 4873L / 4873.0F * 8.0F;
            GL11.glTranslatef(-f9, 0.0F, 0.0F);
            GL11.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);
            ItemRenderer.renderItemIn2D(tessellator, 0.0F, 0.0F, 1.0F, 1.0F, 256, 256, 0.0625F);
            GL11.glPopMatrix();
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
        }

        GL11.glPopMatrix();
        GL11.glPopAttrib();
    }

    public static void setColorFromInt(int color) {
        float r = (color >> 16 & 255) / 255.0F;
        float g = (color >> 8 & 255) / 255.0F;
        float b = (color & 255) / 255.0F;
        GL11.glColor4f(r, g, b, 1.0F);
    }

    public static void drawTexturedModalRect(int x, int y, int u, int v, int width, int height, float zLevel,
            int textureWidth, int textureHeight) {
        float xScale = 1.0F / textureWidth;
        float yScale = 1.0F / textureHeight;
        Tessellator tess = Tessellator.instance;
        tess.startDrawingQuads();
        tess.addVertexWithUV(x + 0, y + height, zLevel, (u + 0) * xScale, (v + height) * yScale);
        tess.addVertexWithUV(x + width, y + height, zLevel, (u + width) * xScale, (v + height) * yScale);
        tess.addVertexWithUV(x + width, y + 0, zLevel, (u + width) * xScale, (v + 0) * yScale);
        tess.addVertexWithUV(x + 0, y + 0, zLevel, (u + 0) * xScale, (v + 0) * yScale);
        tess.draw();
    }

    public static void bindTexture(ResourceLocation texture) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
    }
}
