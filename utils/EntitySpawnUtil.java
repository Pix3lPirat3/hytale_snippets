import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.NPCPlugin;
import com.hypixel.hytale.component.Ref;
import it.unimi.dsi.fastutil.Pair;
import java.util.Optional;

public class EntitySpawnUtil {

    /**
     * Spawns an NPC entity using the native Hytale NPCPlugin.
     * 
     * @param world The world to spawn the entity in.
     * @param roleName The role name of the NPC (e.g., "Kweebec_Rootling").
     * @param position The position to spawn the entity at.
     * @param rotation The initial rotation of the entity (can be null for default).
     * @return An Optional containing the EntityRef if successful, or empty if failed.
     */
    public static Optional<Ref<EntityStore>> spawnNPC(World world, String roleName, Vector3d position, Vector3f rotation) {
        if (world == null || roleName == null || position == null) {
            System.err.println("[EntitySpawnUtil] Invalid arguments provided for spawnNPC.");
            return Optional.empty();
        }

        if (rotation == null) {
            rotation = new Vector3f(0, 0, 0);
        }

        try {
            NPCPlugin npcPlugin = NPCPlugin.get();
            if (!npcPlugin.hasRoleName(roleName)) {
                System.err.println("[EntitySpawnUtil] Role '" + roleName + "' not found in NPCPlugin.");
                return Optional.empty();
            }

            int roleIndex = npcPlugin.getIndex(roleName);
            
            // Native spawn call
            Pair<Ref<EntityStore>, ?> result = npcPlugin.spawnEntity(
                world.getEntityStore().getStore(), 
                roleIndex, 
                position, 
                rotation, 
                null, 
                null
            );

            if (result != null && result.first() != null) {
                return Optional.of(result.first());
            } else {
                System.err.println("[EntitySpawnUtil] NPCPlugin returned null result for role '" + roleName + "'.");
                return Optional.empty();
            }

        } catch (Exception e) {
            System.err.println("[EntitySpawnUtil] Exception spawning NPC '" + roleName + "': " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
