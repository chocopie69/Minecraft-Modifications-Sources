// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.friend;

public final class Friend
{
    private final String username;
    private String alias;
    
    public Friend(final String username) {
        this(username, username);
    }
    
    public Friend(final String alias, final String username) {
        this.alias = alias;
        this.username = username;
    }
    
    public String getAlias() {
        return this.alias;
    }
    
    public void setAlias(final String alias) {
        this.alias = alias;
    }
    
    public String getUsername() {
        return this.username;
    }
}
