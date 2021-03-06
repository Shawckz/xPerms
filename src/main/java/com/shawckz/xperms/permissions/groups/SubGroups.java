package com.shawckz.xperms.permissions.groups;

import com.shawckz.xperms.permissions.perms.Permission;
import com.shawckz.xperms.permissions.perms.Permissions;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.PostLoad;
import org.mongodb.morphia.annotations.Reference;
import org.mongodb.morphia.annotations.Transient;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by 360 on 9/21/2015.
 */
@RequiredArgsConstructor
@Setter
@Embedded
public class SubGroups {

    @Reference
    private Group superGroup;

    @Transient
    private Permissions allPermissions = new Permissions();

    @Reference
    private Set<Group> subGroups = new HashSet<>();

    @PostLoad
    public void onPostLoad() {
        // Called by ODM, after object is loaded from database
        // Refresh our local allPermissions
        refreshGroupPermissions();
    }

    public boolean hasSubGroup(String groupName) {
        Group group = getSubGroup(groupName);
        if (group != null) {
            return hasSubGroup(group);
        }
        return false;
    }

    public boolean hasSubGroup(Group group) {
        return subGroups.contains(group);
    }

    public void addSubGroup(Group group) {
        subGroups.add(group);
        refreshGroupPermissions();
    }

    public boolean removeSubGroup(String groupName) {
        Group group = getSubGroup(groupName);
        if (group != null) {
            return removeSubGroup(group);
        }
        return false;
    }

    public Group getSubGroup(String name) {
        for (Group group : subGroups) {
            if (group.getName().equals(name)) {
                return group;
            }
        }
        return null;
    }

    public boolean removeSubGroup(Group group) {
        boolean found = subGroups.remove(group);
        if (found) {
            refreshGroupPermissions();
        }
        return found;
    }

    public Group getSuperGroup() {
        return superGroup;
    }

    public Permissions getAllPermissions() {
        return allPermissions;
    }

    public void refreshGroupPermissions() {
        this.allPermissions.getPermissions().clear();
        for (Group subGroup : subGroups) {
            for (Permission permission : subGroup.getPermissions().getPermissions().values()) {
                this.allPermissions.setPermission(permission);
            }
        }
        //We want to set the Super Group's permissions AFTER so that it's permissions will override any duplicates in the sub groups.
        for (Permission permission : superGroup.getGroupPermissions().getPermissions().values()) {
            this.allPermissions.setPermission(permission);
        }
    }

    public Set<Group> getSubGroups() {
        return subGroups;
    }

    public String[] getSubGroupsAsString() {
        String[] arr = new String[subGroups.size()];
        int x = 0;
        for (Group subGroup : subGroups) {
            arr[x] = subGroup.getName();
            x++;
        }
        return arr;
    }

}
