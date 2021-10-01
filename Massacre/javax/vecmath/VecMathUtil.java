package javax.vecmath;

class VecMathUtil {
   private VecMathUtil() {
   }

   static final long hashLongBits(long hash, long l) {
      hash *= 31L;
      return hash + l;
   }

   static final long hashFloatBits(long hash, float f) {
      hash *= 31L;
      return f == 0.0F ? hash : hash + (long)Float.floatToIntBits(f);
   }

   static final long hashDoubleBits(long hash, double d) {
      hash *= 31L;
      return d == 0.0D ? hash : hash + Double.doubleToLongBits(d);
   }

   static final int hashFinish(long hash) {
      return (int)(hash ^ hash >> 32);
   }
}
