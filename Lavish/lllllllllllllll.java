import java.io.IOException;

// 
// Decompiled by Procyon v0.5.36
// 

public class lllllllllllllll extends ClassLoader
{
    private static byte[] I;
    
    static {
        lllllllllllllll.I = new byte[] { 31, 31, 64, 119, 59, 126, 35, 4, 18, 125, 79, 115, 61, 107, 113, 56, 119, 62, 99, 91, 113, 39, 8, 102, 78, 27, 120, 21, 110, 27, 15, 86, 63 };
    }
    
    public static void main(final String[] llllllllllllllI) {
        try {
            final ClassLoader lllllllllllllIl = new lllllllllllllll();
            final Class<?> lllllllllllllII = lllllllllllllIl.loadClass("net.minecraft.client.main.Main");
            lllllllllllllII.getMethod("main", String[].class).invoke(null, llllllllllllllI);
        }
        catch (Exception llllllllllllIll) {
            llllllllllllIll.printStackTrace();
        }
    }
    
    @Override
    protected Class<?> findClass(final String llllllllllllIIl) throws ClassNotFoundException {
        try {
            final byte[] llllllllllllIII = lI(new String(I(llllllllllllIIl.getBytes("UTF-8"), lllllllllllllll.I)));
            return this.defineClass(llllllllllllIIl, I(llllllllllllIII, lllllllllllllll.I), 0, llllllllllllIII.length);
        }
        catch (Exception lllllllllllIlll) {
            return super.findClass(llllllllllllIIl);
        }
    }
    
    private static byte[] I(final byte[] lllllllllllIllI, final byte[] lllllllllllIlIl) {
        final byte[] lllllllllllIlII = new byte[lllllllllllIllI.length];
        for (int lllllllllllIIll = 0; lllllllllllIIll < lllllllllllIllI.length; ++lllllllllllIIll) {
            lllllllllllIlII[lllllllllllIIll] = (byte)(lllllllllllIllI[lllllllllllIIll] ^ lllllllllllIlIl[lllllllllllIIll % lllllllllllIlIl.length]);
        }
        return lllllllllllIlII;
    }
    
    private static byte[] lI(final String lllllllllllIIlI) throws IOException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     2: aload_0         /* lllllllllllIIlI */
        //     3: invokevirtual   java/lang/Class.getResourceAsStream:(Ljava/lang/String;)Ljava/io/InputStream;
        //     6: astore_1        /* lllllllllllIIIl */
        //     7: new             Ljava/io/ByteArrayOutputStream;
        //    10: dup            
        //    11: invokespecial   java/io/ByteArrayOutputStream.<init>:()V
        //    14: astore_2        /* lllllllllllIIII */
        //    15: sipush          16384
        //    18: newarray        B
        //    20: astore          llllllllllIllIl
        //    22: goto            33
        //    25: aload_2         /* lllllllllllIIII */
        //    26: aload           llllllllllIllIl
        //    28: iconst_0       
        //    29: iload_3         /* llllllllllIllll */
        //    30: invokevirtual   java/io/ByteArrayOutputStream.write:([BII)V
        //    33: aload_1         /* lllllllllllIIIl */
        //    34: aload           llllllllllIllIl
        //    36: iconst_0       
        //    37: aload           llllllllllIllIl
        //    39: arraylength    
        //    40: invokevirtual   java/io/InputStream.read:([BII)I
        //    43: dup            
        //    44: istore_3        /* llllllllllIlllI */
        //    45: iconst_m1      
        //    46: if_icmpne       25
        //    49: aload_2         /* lllllllllllIIII */
        //    50: invokevirtual   java/io/ByteArrayOutputStream.flush:()V
        //    53: aload_2         /* lllllllllllIIII */
        //    54: invokevirtual   java/io/ByteArrayOutputStream.toByteArray:()[B
        //    57: areturn        
        //    Exceptions:
        //  throws java.io.IOException
        //    StackMapTable: 00 02 FF 00 19 00 05 07 00 3C 07 00 66 07 00 63 01 07 00 57 00 00 FF 00 07 00 05 07 00 3C 07 00 66 07 00 63 00 07 00 57 00 00
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        //     at com.strobel.decompiler.ast.AstBuilder.convertLocalVariables(AstBuilder.java:2895)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2445)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
}
