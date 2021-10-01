// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.utils;

import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.datatransfer.DataFlavor;
import java.awt.Toolkit;

public final class ClipboardUtils
{
    private ClipboardUtils() {
    }
    
    public static String getClipboardContents() {
        try {
            return (String)Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
        }
        catch (UnsupportedFlavorException | IOException ex2) {
            final Exception ex;
            final Exception ignored = ex;
            return null;
        }
    }
    
    public static void setClipboardContents(final String contents) {
        final StringSelection selection = new StringSelection(contents);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, selection);
    }
}
