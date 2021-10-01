// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.alt;

import java.util.Objects;

public final class Alt
{
    private final String email;
    private final String password;
    private String username;
    private boolean banned;
    private boolean working;
    
    public Alt(final String username, final String email, final String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
    
    public Alt(final String email, final String password) {
        this.email = email;
        this.password = password;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final Alt alt = (Alt)o;
        return this.email.equals(alt.email) && this.password.equals(alt.password);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(this.email, this.password);
    }
    
    public boolean isWorking() {
        return this.working;
    }
    
    public void setWorking(final boolean working) {
        this.working = working;
    }
    
    public boolean isBanned() {
        return this.banned;
    }
    
    public void setBanned(final boolean banned) {
        this.banned = banned;
    }
    
    public void setBanned() {
        this.setBanned(true);
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public void setUsername(final String username) {
        this.username = username;
    }
    
    public String getEmail() {
        return this.email;
    }
    
    public String getPassword() {
        return this.password;
    }
}
