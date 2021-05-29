package org.eurostates.technology;

import org.bukkit.permissions.Permission;

import java.util.HashSet;
import java.util.Set;

public class Technology {

    private String id;
    private Set<String> requirements = new HashSet<>();
    private Set<Permission> permissions = new HashSet<>();

    public static final String ID_NODE = "id";
    public static final String REQ_NODE = "requirements";
    public static final String PERM_NODE = "permissions";

    public Technology(String id){
        this.id = id;
    }

    public Technology(String id, Set<String> requirements, Set<Permission> permissions){
        this.id = id;
        this.requirements = requirements;
        this.permissions = permissions;
    }

    public String getID(){ return this.id; }

    public void setPermissions(Set<Permission> permissions) { this.permissions = permissions; }
    public Set<Permission> getPermissions() { return permissions; }

    public void setRequirements(Set<String> requirements) { this.requirements = requirements; }

    public Set<String> getRequirements() { return requirements; }
}
