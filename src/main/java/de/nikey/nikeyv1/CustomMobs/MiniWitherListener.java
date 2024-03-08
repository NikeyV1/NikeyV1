package de.nikey.nikeyv1.CustomMobs;

import de.nikey.nikeyv1.NikeyV1;
import de.nikey.nikeyv1.Util.HelpUtil;
import org.apache.logging.log4j.core.tools.picocli.CommandLine;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MiniWitherListener implements Listener {
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof MiniWither) {
            Location location = event.getEntity().getLocation();
            Entity crystal = event.getEntity().getWorld().spawnEntity(location.add(0, 2, 0), EntityType.ENDER_CRYSTAL);

            new BukkitRunnable() {

                int count = 0;
                Vector movement = null;
                List<Block> blocks = HelpUtil.getNearbyBlocksNoY(location,20);
                List<FallingBlock> fblocks = new ArrayList<>();
                @Override
                public void run() {
                    if (count == 300) {
                        cancel();
                        crystal.remove();
                        TNTPrimed tnt = crystal.getWorld().spawn(crystal.getLocation(),TNTPrimed.class);
                        tnt.setFuseTicks(0);
                        for (FallingBlock b : fblocks) {
                            b.setGravity(true);
                        }
                        return;
                    }
                    Random random = new Random();
                    Block block = blocks.get(random.nextInt(blocks.size() - 0));
                    FallingBlock fblock = location.getWorld().spawnFallingBlock(block.getLocation(),block.getBlockData());
                    fblock.setVelocity((fblock.getLocation().toVector().subtract(crystal.getLocation().toVector()).multiply(-10).normalize()));
                    fblock.setGravity(false);
                    fblock.setHurtEntities(true);

                    fblocks.add(fblock);

                    count++;
                }
            }.runTaskTimer(NikeyV1.getPlugin(),0,0);
        }
    }
}
