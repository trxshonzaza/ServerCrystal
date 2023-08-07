package trxsh.ontop.servercrystal.listener;

import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerPlayerConnection;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftEnderCrystal;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import trxsh.ontop.servercrystal.Main;

import java.util.UUID;

public class ArmEvent implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAnimation(PlayerAnimationEvent e) {

        if(e.getAnimationType() == PlayerAnimationType.ARM_SWING) {

            for(Entity entity : e.getPlayer().getNearbyEntities(4, 4, 4)) {

                if(entity instanceof EnderCrystal) {

                    if(e.getPlayer().hasLineOfSight(entity)) {

                        if(!Main.enableGlobalCrystals)
                            return;

                        long pre = System.currentTimeMillis();

                        Main.crystalSweep.crystals.add(entity);

                        Player player = e.getPlayer();

                        boolean b = Main.players.get(player.getUniqueId());

                        if(!b)
                            return;

                        ServerPlayer sp = ((CraftPlayer) player).getHandle();
                        ServerPlayerConnection c = sp.connection;

                        EndCrystal NmsEntity = ((CraftEnderCrystal) entity).getHandle();

                        c.send(new ClientboundRemoveEntitiesPacket(NmsEntity.getId()));

                        NmsEntity.kill();
                        NmsEntity.remove(net.minecraft.world.entity.Entity.RemovalReason.KILLED);
                        NmsEntity.onClientRemoval();

                        long post = System.currentTimeMillis();

                        for(UUID id : Main.stats.keySet()) {

                            Player p = Bukkit.getPlayer(id);

                            boolean bl = Main.stats.get(id);

                            if(p != null)
                                if(p.isOnline())
                                    if(p.isOp())
                                        if (bl) {

                                            long elapsed = (post - pre);

                                            p.sendMessage(ChatColor.AQUA + player.getName() + ChatColor.GRAY + " used fast crystal (" + ChatColor.AQUA + elapsed + "ms" + ChatColor.GRAY + ")");

                                        }

                        }

                    }

                }

            }

        }

    }

}
