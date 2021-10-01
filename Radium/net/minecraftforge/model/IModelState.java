// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraftforge.model;

import com.google.common.base.Optional;

public interface IModelState
{
    Optional<TRSRTransformation> apply(final Optional<? extends IModelPart> p0);
}
