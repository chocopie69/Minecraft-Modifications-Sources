// 
// Decompiled by Procyon v0.5.36
// 

package vip.radium.friend;

import net.minecraft.entity.player.EntityPlayer;
import vip.radium.utils.Wrapper;
import java.util.Iterator;
import vip.radium.utils.FileUtils;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.io.File;
import vip.radium.utils.handler.Manager;

public final class FriendManager extends Manager<Friend>
{
    public static final File FRIENDS_FILE;
    
    static {
        FRIENDS_FILE = new File("Radium", "friends.txt");
    }
    
    public FriendManager() {
        super(loadFriends());
        if (!FriendManager.FRIENDS_FILE.exists()) {
            try {
                FriendManager.FRIENDS_FILE.createNewFile();
            }
            catch (IOException ex) {}
        }
    }
    
    private static List<Friend> loadFriends() {
        final ArrayList<Friend> friends = new ArrayList<Friend>();
        if (FriendManager.FRIENDS_FILE.exists()) {
            final List<String> lines = FileUtils.getLines(FriendManager.FRIENDS_FILE);
            for (final String line : lines) {
                if (line.contains(":") && line.length() > 2) {
                    final String[] split = line.split(":");
                    if (split.length != 2) {
                        continue;
                    }
                    friends.add(new Friend(split[0], split[1]));
                }
            }
        }
        return friends;
    }
    
    public boolean isFriend(final String username) {
        for (final Friend friend : this.getElements()) {
            if (friend.getUsername().equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }
    
    public String friend(final String friendName, final String alias) {
        for (final EntityPlayer player : Wrapper.getLoadedPlayers()) {
            final String username = player.getGameProfile().getName();
            if (username.equalsIgnoreCase(friendName)) {
                this.getElements().add(new Friend(alias, friendName));
                return username;
            }
        }
        return null;
    }
    
    public String unfriend(final String username) {
        for (final Friend friend : this.getElements()) {
            final String friendName = friend.getUsername();
            if (friendName.equalsIgnoreCase(username)) {
                this.getElements().remove(friend);
                return friendName;
            }
        }
        return null;
    }
    
    public boolean isFriend(final EntityPlayer player) {
        return this.isFriend(player.getGameProfile().getName());
    }
}
