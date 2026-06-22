package com.mojang.minecraft.character;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.renderer.Textures;
import org.lwjgl.opengl.GL11;

public class Zombie extends Entity {
   public float rot;
   public float timeOffs;
   public float speed;
   public float rotA;
   private static ZombieModel zombieModel = new ZombieModel();
   private Textures textures;

   public Zombie(Level level, Textures textures, float x, float y, float z) {
      super(level);
      this.textures = textures;
      this.rotA = (float)(Math.random() + 1.0D) * 0.01F;
      this.setPos(x, y, z);
      this.timeOffs = (float)Math.random() * 1239813.0F;
      this.rot = (float)(Math.random() * Math.PI * 2.0D);
      this.speed = 1.0F;
   }

   public void tick() {
      this.xo = this.x;
      this.yo = this.y;
      this.zo = this.z;

      Entity p = this.level.getPlayer();

      this.xd = 0;
      this.yd = 0;
      this.zd = 0;

      if (p != null) {
         double dx = p.x - this.x;
         double dz = p.z - this.z;
         this.rot = (float)(Math.atan2(dx, dz));
      }

      this.rotA *= 0.95F;
      this.rotA += (Math.random() - 0.5F) * 0.02F;
      this.rot += this.rotA;

      if (p != null && Math.random() < 0.03D) {
         double angle = Math.random() * Math.PI * 2;
         this.x = (float)(p.x + Math.cos(angle) * 6.0);
         this.z = (float)(p.z + Math.sin(angle) * 6.0);
         this.y = (float)p.y;
      }

      if (Math.random() < 0.01D) {
         this.remove();
      }

      if (this.y < -100.0F) {
         this.remove();
      }
   }

   private boolean shouldRender() {
      long t = System.nanoTime() / 1000000;
      if (Math.random() < 0.20) return false;
      if (((t / 7) + (int)(this.x * 3) + (int)(this.z * 3)) % (3 + (int)(Math.random() * 5)) == 0) return false;
      return true;
   }

   public void render(float a) {
      GL11.glEnable(3553);

      if (!shouldRender()) {
         GL11.glDisable(3553);
         return;
      }

      GL11.glBindTexture(3553, this.textures.loadTexture("/char.png", 9728));
      GL11.glPushMatrix();

      double time = (double)System.nanoTime() / 1.0E9D * 10.0D * (double)this.speed + (double)this.timeOffs;

      float size = 0.058333334F;
      float yy = (float)(-Math.abs(Math.sin(time * 0.6662D)) * 5.0D - 23.0D);

      GL11.glTranslatef(
         this.xo + (this.x - this.xo) * a,
         this.yo + (this.y - this.yo) * a,
         this.zo + (this.z - this.zo) * a
      );

      GL11.glScalef(1.0F, -1.0F, 1.0F);
      GL11.glScalef(size, size, size);
      GL11.glTranslatef(0.0F, yy, 0.0F);

      float c = 57.29578F;
      GL11.glRotatef(this.rot * c + 180.0F, 0.0F, 1.0F, 0.0F);

      zombieModel.render((float)time);

      GL11.glPopMatrix();
      GL11.glDisable(3553);
   }
}
