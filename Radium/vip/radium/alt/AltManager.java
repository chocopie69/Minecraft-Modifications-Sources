// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.alt;

import java.io.FileWriter;
import java.util.Iterator;
import java.util.Set;
import java.util.Collection;
import vip.radium.utils.FileUtils;
import java.util.HashSet;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import com.thealtening.auth.TheAlteningAuthentication;
import com.thealtening.api.retriever.BasicDataRetriever;
import java.io.File;
import vip.radium.utils.handler.Manager;

public final class AltManager extends Manager<Alt>
{
    public static final File ALT_FILE;
    public static final File ALTENING_TOKEN_FILE;
    private final BasicDataRetriever alteningAltFetcher;
    private final TheAlteningAuthentication alteningAuth;
    private String apiKey;
    
    static {
        ALT_FILE = new File("Radium", "alts.txt");
        ALTENING_TOKEN_FILE = new File("Radium", "token.txt");
    }
    
    public AltManager() {
        super(new ArrayList());
        this.apiKey = "api-0000-0000-0000";
        this.alteningAuth = TheAlteningAuthentication.mojang();
        this.alteningAltFetcher = new BasicDataRetriever(this.apiKey);
        if (!AltManager.ALT_FILE.exists()) {
            try {
                AltManager.ALT_FILE.createNewFile();
            }
            catch (IOException ex) {}
        }
        final Set<Alt> alts = new HashSet<Alt>();
        for (final String line : FileUtils.getLines(AltManager.ALT_FILE)) {
            alts.add(parseAlt(line));
        }
        this.getElements().addAll(alts);
        if (!AltManager.ALTENING_TOKEN_FILE.exists()) {
            try {
                AltManager.ALTENING_TOKEN_FILE.createNewFile();
            }
            catch (IOException ex2) {}
        }
        else {
            final List<String> lines = FileUtils.getLines(AltManager.ALTENING_TOKEN_FILE);
            if (!lines.isEmpty()) {
                this.setAPIKey(lines.get(0));
            }
        }
    }
    
    public static Alt parseAlt(final String line) {
        if (line == null) {
            return null;
        }
        if (line.length() == 0) {
            return null;
        }
        final String[] userPass = line.split(":", 2);
        if (userPass.length == 2) {
            return new Alt(userPass[0], userPass[1]);
        }
        return null;
    }
    
    public void addAlt(final Alt alt) {
        this.getElements().add(alt);
    }
    
    public BasicDataRetriever getAlteningAltFetcher() {
        return this.alteningAltFetcher;
    }
    
    public TheAlteningAuthentication getAlteningAuth() {
        return this.alteningAuth;
    }
    
    public String getAPIKey() {
        return this.apiKey;
    }
    
    public void setAPIKey(final String apiKey) {
        this.apiKey = apiKey;
        this.alteningAltFetcher.updateKey(apiKey);
    }
    
    public void saveAPIKey(final String apiKey) {
        if (apiKey == null) {
            return;
        }
        this.setAPIKey(apiKey);
        if (!AltManager.ALTENING_TOKEN_FILE.exists()) {
            try {
                if (!AltManager.ALTENING_TOKEN_FILE.createNewFile()) {
                    return;
                }
            }
            catch (IOException ignored) {
                return;
            }
        }
        try {
            final FileWriter fileWriter = new FileWriter(AltManager.ALTENING_TOKEN_FILE);
            fileWriter.write(apiKey);
            fileWriter.close();
        }
        catch (IOException ex) {}
    }
}
