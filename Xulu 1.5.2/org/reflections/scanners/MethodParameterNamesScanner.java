// 
// Decompiled by Procyon v0.5.36
// 

package org.reflections.scanners;

import java.util.List;
import javassist.bytecode.CodeAttribute;
import java.util.Iterator;
import org.reflections.adapters.MetadataAdapter;
import java.util.Collection;
import org.reflections.util.Utils;
import java.util.ArrayList;
import java.lang.reflect.Modifier;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
import org.reflections.Store;

public class MethodParameterNamesScanner extends AbstractScanner
{
    @Override
    public void scan(final Object cls, final Store store) {
        final MetadataAdapter md = this.getMetadataAdapter();
        for (final Object method : md.getMethods(cls)) {
            final String key = md.getMethodFullKey(cls, method);
            if (this.acceptResult(key)) {
                final CodeAttribute codeAttribute = ((MethodInfo)method).getCodeAttribute();
                final LocalVariableAttribute table = (codeAttribute != null) ? ((LocalVariableAttribute)codeAttribute.getAttribute("LocalVariableTable")) : null;
                final int length = (table != null) ? table.tableLength() : 0;
                int i = Modifier.isStatic(((MethodInfo)method).getAccessFlags()) ? 0 : 1;
                if (i >= length) {
                    continue;
                }
                final List<String> names = new ArrayList<String>(length - i);
                while (i < length) {
                    names.add(((MethodInfo)method).getConstPool().getUtf8Info(table.nameIndex(i++)));
                }
                this.put(store, key, Utils.join(names, ", "));
            }
        }
    }
}
