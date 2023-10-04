package de.nikey.nikeyv1.Stones;

public class Holystone implements Listener {
    public static ArrayList<Entity> stunned = new ArrayList<>();
    public static HashMap<UUID, Long> cooldown = new HashMap<>();
    public static HashMap<UUID, Long> ability = new HashMap<>();
    private int timer;
    public static long remainingTime1;
    public static long remainingTime2;

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        ItemStack item = event.getItem();
        if (item == null) return;
        if (event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§eElektro Stein")&& event.getItem().getType() == Material.FIREWORK_STAR){
            String[] arr = item.getLore().get(1).split(":");
            int i = Integer.parseInt(arr[1]);
            FileConfiguration config = NikeyV1.plugin.getConfig();
            config.set(p.getName()+".stone","Elektro");
            config.set(p.getName()+".level",i);
            NikeyV1.plugin.saveConfig();
            String stone = config.getString(p.getName() + ".stone");
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
                if (i >= 10){
                    if (cooldown.containsKey(p.getUniqueId()) && cooldown.get(p.getUniqueId()) > System.currentTimeMillis()){
                        event.setCancelled(true);
                        p.updateInventory();
                        remainingTime1 = cooldown.get(p.getUniqueId()) - System.currentTimeMillis();
                    }else {
                        cooldown.put(p.getUniqueId(),System.currentTimeMillis() + (100*1000));
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                cooldown.remove(p.getUniqueId());
                                cancel();
                                return;
                            }
                        }.runTaskLater(NikeyV1.getPlugin(),20*100);
                        if (i ==10 || i == 11){
                            timer = 20;
                            World world = p.getWorld();
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    timer--;
                                    if(timer == 0){
                                        cancel();
                                        return;
                                    }
                                    for(Player player :p.getNearbyEntities(world,4,4,4)){
                                    }
                                }
                            }.runTaskTimer(NikeyV1.getPlugin(),20,20);
                        }
                    }            
                }
            }    
        }    
    }    
}