package javax.vecmath;

import java.io.Serializable;

public abstract class Tuple3d implements Serializable, Cloneable {
   static final long serialVersionUID = 5542096614926168415L;
   public double x;
   public double y;
   public double z;

   public Tuple3d(double x, double y, double z) {
      this.x = x;
      this.y = y;
      this.z = z;
   }

   public Tuple3d(double[] t) {
      this.x = t[0];
      this.y = t[1];
      this.z = t[2];
   }

   public Tuple3d(Tuple3d t1) {
      this.x = t1.x;
      this.y = t1.y;
      this.z = t1.z;
   }

   public Tuple3d(Tuple3f t1) {
      this.x = (double)t1.x;
      this.y = (double)t1.y;
      this.z = (double)t1.z;
   }

   public Tuple3d() {
      this.x = 0.0D;
      this.y = 0.0D;
      this.z = 0.0D;
   }

   public final void set(double x, double y, double z) {
      this.x = x;
      this.y = y;
      this.z = z;
   }

   public final void set(double[] t) {
      this.x = t[0];
      this.y = t[1];
      this.z = t[2];
   }

   public final void set(Tuple3d t1) {
      this.x = t1.x;
      this.y = t1.y;
      this.z = t1.z;
   }

   public final void set(Tuple3f t1) {
      this.x = (double)t1.x;
      this.y = (double)t1.y;
      this.z = (double)t1.z;
   }

   public final void get(double[] t) {
      t[0] = this.x;
      t[1] = this.y;
      t[2] = this.z;
   }

   public final void get(Tuple3d t) {
      t.x = this.x;
      t.y = this.y;
      t.z = this.z;
   }

   public final void add(Tuple3d t1, Tuple3d t2) {
      this.x = t1.x + t2.x;
      this.y = t1.y + t2.y;
      this.z = t1.z + t2.z;
   }

   public final void add(Tuple3d t1) {
      this.x += t1.x;
      this.y += t1.y;
      this.z += t1.z;
   }

   public final void sub(Tuple3d t1, Tuple3d t2) {
      this.x = t1.x - t2.x;
      this.y = t1.y - t2.y;
      this.z = t1.z - t2.z;
   }

   public final void sub(Tuple3d t1) {
      this.x -= t1.x;
      this.y -= t1.y;
      this.z -= t1.z;
   }

   public final void negate(Tuple3d t1) {
      this.x = -t1.x;
      this.y = -t1.y;
      this.z = -t1.z;
   }

   public final void negate() {
      this.x = -this.x;
      this.y = -this.y;
      this.z = -this.z;
   }

   public final void scale(double s, Tuple3d t1) {
      this.x = s * t1.x;
      this.y = s * t1.y;
      this.z = s * t1.z;
   }

   public final void scale(double s) {
      this.x *= s;
      this.y *= s;
      this.z *= s;
   }

   public final void scaleAdd(double s, Tuple3d t1, Tuple3d t2) {
      this.x = s * t1.x + t2.x;
      this.y = s * t1.y + t2.y;
      this.z = s * t1.z + t2.z;
   }

   /** @deprecated */
   public final void scaleAdd(double s, Tuple3f t1) {
      this.scaleAdd(s, (Tuple3d)(new Point3d(t1)));
   }

   public final void scaleAdd(double s, Tuple3d t1) {
      this.x = s * this.x + t1.x;
      this.y = s * this.y + t1.y;
      this.z = s * this.z + t1.z;
   }

   public String toString() {
      return "(" + this.x + ", " + this.y + ", " + this.z + ")";
   }

   public int hashCode() {
      long bits = 1L;
      bits = VecMathUtil.hashDoubleBits(bits, this.x);
      bits = VecMathUtil.hashDoubleBits(bits, this.y);
      bits = VecMathUtil.hashDoubleBits(bits, this.z);
      return VecMathUtil.hashFinish(bits);
   }

   public boolean equals(Tuple3d t1) {
      try {
         return this.x == t1.x && this.y == t1.y && this.z == t1.z;
      } catch (NullPointerException var3) {
         return false;
      }
   }

   public boolean equals(Object t1) {
      try {
         Tuple3d t2 = (Tuple3d)t1;
         return this.x == t2.x && this.y == t2.y && this.z == t2.z;
      } catch (ClassCastException var3) {
         return false;
      } catch (NullPointerException var4) {
         return false;
      }
   }

   public boolean epsilonEquals(Tuple3d t1, double epsilon) {
      double diff = this.x - t1.x;
      if (Double.isNaN(diff)) {
         return false;
      } else if ((diff < 0.0D ? -diff : diff) > epsilon) {
         return false;
      } else {
         diff = this.y - t1.y;
         if (Double.isNaN(diff)) {
            return false;
         } else if ((diff < 0.0D ? -diff : diff) > epsilon) {
            return false;
         } else {
            diff = this.z - t1.z;
            if (Double.isNaN(diff)) {
               return false;
            } else {
               return !((diff < 0.0D ? -diff : diff) > epsilon);
            }
         }
      }
   }

   /** @deprecated */
   public final void clamp(float min, float max, Tuple3d t) {
      this.clamp((double)min, (double)max, t);
   }

   public final void clamp(double min, double max, Tuple3d t) {
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

      if (t.z > max) {
         this.z = max;
      } else if (t.z < min) {
         this.z = min;
      } else {
         this.z = t.z;
      }

   }

   /** @deprecated */
   public final void clampMin(float min, Tuple3d t) {
      this.clampMin((double)min, t);
   }

   public final void clampMin(double min, Tuple3d t) {
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

      if (t.z < min) {
         this.z = min;
      } else {
         this.z = t.z;
      }

   }

   /** @deprecated */
   public final void clampMax(float max, Tuple3d t) {
      this.clampMax((double)max, t);
   }

   public final void clampMax(double max, Tuple3d t) {
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

      if (t.z > max) {
         this.z = max;
      } else {
         this.z = t.z;
      }

   }

   public final void absolute(Tuple3d t) {
      this.x = Math.abs(t.x);
      this.y = Math.abs(t.y);
      this.z = Math.abs(t.z);
   }

   /** @deprecated */
   public final void clamp(float min, float max) {
      this.clamp((double)min, (double)max);
   }

   public final void clamp(double min, double max) {
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

      if (this.z > max) {
         this.z = max;
      } else if (this.z < min) {
         this.z = min;
      }

   }

   /** @deprecated */
   public final void clampMin(float min) {
      this.clampMin((double)min);
   }

   public final void clampMin(double min) {
      if (this.x < min) {
         this.x = min;
      }

      if (this.y < min) {
         this.y = min;
      }

      if (this.z < min) {
         this.z = min;
      }

   }

   /** @deprecated */
   public final void clampMax(float max) {
      this.clampMax((double)max);
   }

   public final void clampMax(double max) {
      if (this.x > max) {
         this.x = max;
      }

      if (this.y > max) {
         this.y = max;
      }

      if (this.z > max) {
         this.z = max;
      }

   }

   public final void absolute() {
      this.x = Math.abs(this.x);
      this.y = Math.abs(this.y);
      this.z = Math.abs(this.z);
   }

   /** @deprecated */
   public final void interpolate(Tuple3d t1, Tuple3d t2, float alpha) {
      this.interpolate(t1, t2, (double)alpha);
   }

   public final void interpolate(Tuple3d t1, Tuple3d t2, double alpha) {
      this.x = (1.0D - alpha) * t1.x + alpha * t2.x;
      this.y = (1.0D - alpha) * t1.y + alpha * t2.y;
      this.z = (1.0D - alpha) * t1.z + alpha * t2.z;
   }

   /** @deprecated */
   public final void interpolate(Tuple3d t1, float alpha) {
      this.interpolate(t1, (double)alpha);
   }

   public final void interpolate(Tuple3d t1, double alpha) {
      this.x = (1.0D - alpha) * this.x + alpha * t1.x;
      this.y = (1.0D - alpha) * this.y + alpha * t1.y;
      this.z = (1.0D - alpha) * this.z + alpha * t1.z;
   }

   public Object clone() {
      try {
         return super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new InternalError();
      }
   }

   public final double getX() {
      return this.x;
   }

   public final void setX(double x) {
      this.x = x;
   }

   public final double getY() {
      return this.y;
   }

   public final void setY(double y) {
      this.y = y;
   }

   public final double getZ() {
      return this.z;
   }

   public final void setZ(double z) {
      this.z = z;
   }
}
