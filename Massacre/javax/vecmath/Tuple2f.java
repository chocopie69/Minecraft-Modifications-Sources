package javax.vecmath;

import java.io.Serializable;

public abstract class Tuple2f implements Serializable, Cloneable {
   static final long serialVersionUID = 9011180388985266884L;
   public float x;
   public float y;

   public Tuple2f(float x, float y) {
      this.x = x;
      this.y = y;
   }

   public Tuple2f(float[] t) {
      this.x = t[0];
      this.y = t[1];
   }

   public Tuple2f(Tuple2f t1) {
      this.x = t1.x;
      this.y = t1.y;
   }

   public Tuple2f(Tuple2d t1) {
      this.x = (float)t1.x;
      this.y = (float)t1.y;
   }

   public Tuple2f() {
      this.x = 0.0F;
      this.y = 0.0F;
   }

   public final void set(float x, float y) {
      this.x = x;
      this.y = y;
   }

   public final void set(float[] t) {
      this.x = t[0];
      this.y = t[1];
   }

   public final void set(Tuple2f t1) {
      this.x = t1.x;
      this.y = t1.y;
   }

   public final void set(Tuple2d t1) {
      this.x = (float)t1.x;
      this.y = (float)t1.y;
   }

   public final void get(float[] t) {
      t[0] = this.x;
      t[1] = this.y;
   }

   public final void add(Tuple2f t1, Tuple2f t2) {
      this.x = t1.x + t2.x;
      this.y = t1.y + t2.y;
   }

   public final void add(Tuple2f t1) {
      this.x += t1.x;
      this.y += t1.y;
   }

   public final void sub(Tuple2f t1, Tuple2f t2) {
      this.x = t1.x - t2.x;
      this.y = t1.y - t2.y;
   }

   public final void sub(Tuple2f t1) {
      this.x -= t1.x;
      this.y -= t1.y;
   }

   public final void negate(Tuple2f t1) {
      this.x = -t1.x;
      this.y = -t1.y;
   }

   public final void negate() {
      this.x = -this.x;
      this.y = -this.y;
   }

   public final void scale(float s, Tuple2f t1) {
      this.x = s * t1.x;
      this.y = s * t1.y;
   }

   public final void scale(float s) {
      this.x *= s;
      this.y *= s;
   }

   public final void scaleAdd(float s, Tuple2f t1, Tuple2f t2) {
      this.x = s * t1.x + t2.x;
      this.y = s * t1.y + t2.y;
   }

   public final void scaleAdd(float s, Tuple2f t1) {
      this.x = s * this.x + t1.x;
      this.y = s * this.y + t1.y;
   }

   public int hashCode() {
      long bits = 1L;
      bits = VecMathUtil.hashFloatBits(bits, this.x);
      bits = VecMathUtil.hashFloatBits(bits, this.y);
      return VecMathUtil.hashFinish(bits);
   }

   public boolean equals(Tuple2f t1) {
      try {
         return this.x == t1.x && this.y == t1.y;
      } catch (NullPointerException var3) {
         return false;
      }
   }

   public boolean equals(Object t1) {
      try {
         Tuple2f t2 = (Tuple2f)t1;
         return this.x == t2.x && this.y == t2.y;
      } catch (NullPointerException var3) {
         return false;
      } catch (ClassCastException var4) {
         return false;
      }
   }

   public boolean epsilonEquals(Tuple2f t1, float epsilon) {
      float diff = this.x - t1.x;
      if (Float.isNaN(diff)) {
         return false;
      } else if ((diff < 0.0F ? -diff : diff) > epsilon) {
         return false;
      } else {
         diff = this.y - t1.y;
         if (Float.isNaN(diff)) {
            return false;
         } else {
            return !((diff < 0.0F ? -diff : diff) > epsilon);
         }
      }
   }

   public String toString() {
      return "(" + this.x + ", " + this.y + ")";
   }

   public final void clamp(float min, float max, Tuple2f t) {
      if (t.x > max) {
         this.x = max;
      } else if (t.x < min) {
         this.x = min;
      } else {
         this.x = t.x;
      }

      if (t.y > max) {
         this.y = max;
      } else if (t.y < min) {
         this.y = min;
      } else {
         this.y = t.y;
      }

   }

   public final void clampMin(float min, Tuple2f t) {
      if (t.x < min) {
         this.x = min;
      } else {
         this.x = t.x;
      }

      if (t.y < min) {
         this.y = min;
      } else {
         this.y = t.y;
      }

   }

   public final void clampMax(float max, Tuple2f t) {
      if (t.x > max) {
         this.x = max;
      } else {
         this.x = t.x;
      }

      if (t.y > max) {
         this.y = max;
      } else {
         this.y = t.y;
      }

   }

   public final void absolute(Tuple2f t) {
      this.x = Math.abs(t.x);
      this.y = Math.abs(t.y);
   }

   public final void clamp(float min, float max) {
      if (this.x > max) {
         this.x = max;
      } else if (this.x < min) {
         this.x = min;
      }

      if (this.y > max) {
         this.y = max;
      } else if (this.y < min) {
         this.y = min;
      }

   }

   public final void clampMin(float min) {
      if (this.x < min) {
         this.x = min;
      }

      if (this.y < min) {
         this.y = min;
      }

   }

   public final void clampMax(float max) {
      if (this.x > max) {
         this.x = max;
      }

      if (this.y > max) {
         this.y = max;
      }

   }

   public final void absolute() {
      this.x = Math.abs(this.x);
      this.y = Math.abs(this.y);
   }

   public final void interpolate(Tuple2f t1, Tuple2f t2, float alpha) {
      this.x = (1.0F - alpha) * t1.x + alpha * t2.x;
      this.y = (1.0F - alpha) * t1.y + alpha * t2.y;
   }

   public final void interpolate(Tuple2f t1, float alpha) {
      this.x = (1.0F - alpha) * this.x + alpha * t1.x;
      this.y = (1.0F - alpha) * this.y + alpha * t1.y;
   }

   public Object clone() {
      try {
         return super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new InternalError();
      }
   }

   public final float getX() {
      return this.x;
   }

   public final void setX(float x) {
      this.x = x;
   }

   public final float getY() {
      return this.y;
   }

   public final void setY(float y) {
      this.y = y;
   }
}
