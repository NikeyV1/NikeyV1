package de.nikey.nikeyv1.Commands;

import de.nikey.nikeyv1.NikeyV1;
import de.slikey.effectlib.effect.*;
import de.slikey.effectlib.util.DynamicLocation;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PEffectCMD implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player p) {
            if (args.length == 1){
                if (p.isOp() || p.getName().equalsIgnoreCase("NikeyV1") || p.getName().equalsIgnoreCase("NikeyV3")) {
                    if (args[0].equalsIgnoreCase("Ball")){
                        AnimatedBallEffect effect = new AnimatedBallEffect(NikeyV1.em);
                        effect.setLocation(p.getLocation());
                        effect.start();
                        p.sendMessage("Started");
                    }else if (args[0].equalsIgnoreCase("Arc")){
                        ArcEffect effect = new ArcEffect(NikeyV1.em);
                        effect.setLocation(p.getLocation());
                        effect.start();
                        p.sendMessage("Started");
                    }else if (args[0].equalsIgnoreCase("Atom")){
                        AtomEffect effect = new AtomEffect(NikeyV1.em);
                        effect.setLocation(p.getLocation());
                        effect.start();
                        p.sendMessage("Started");
                    }else if (args[0].equalsIgnoreCase("BigBang")){
                        BigBangEffect effect = new BigBangEffect(NikeyV1.em);
                        effect.setLocation(p.getLocation());
                        effect.start();
                        p.sendMessage("Started");
                    }else if (args[0].equalsIgnoreCase("Bleed")){
                        BleedEffect effect = new BleedEffect(NikeyV1.em);
                        effect.setLocation(p.getLocation());
                        effect.start();
                        p.sendMessage("Started");
                    }else if (args[0].equalsIgnoreCase("Circle")){
                        CircleEffect effect = new CircleEffect(NikeyV1.em);
                        effect.setLocation(p.getLocation());
                        effect.start();
                        p.sendMessage("Started");
                    }else if (args[0].equalsIgnoreCase("Cloud")){
                        CloudEffect effect = new CloudEffect(NikeyV1.em);
                        effect.setLocation(p.getLocation());
                        effect.start();
                        p.sendMessage("Started");
                    }else if (args[0].equalsIgnoreCase("Cone")){
                        ConeEffect effect = new ConeEffect(NikeyV1.em);
                        effect.setLocation(p.getLocation());
                        effect.start();
                        p.sendMessage("Started");
                    }else if (args[0].equalsIgnoreCase("Cube")){
                        CubeEffect effect = new CubeEffect(NikeyV1.em);
                        effect.setLocation(p.getLocation());
                        effect.start();
                        p.sendMessage("Started");
                    }else if (args[0].equalsIgnoreCase("Cuboid")){
                        CuboidEffect effect = new CuboidEffect(NikeyV1.em);
                        effect.setLocation(p.getLocation());
                        effect.start();
                        p.sendMessage("Started");
                    }else if (args[0].equalsIgnoreCase("Cylinder")){
                        CylinderEffect effect = new CylinderEffect(NikeyV1.em);
                        effect.setLocation(p.getLocation());
                        effect.start();
                        p.sendMessage("Started");
                    }else if (args[0].equalsIgnoreCase("Earth")){
                        EarthEffect effect = new EarthEffect(NikeyV1.em);
                        effect.setLocation(p.getLocation());
                        effect.start();
                        p.sendMessage("Started");
                    }else if (args[0].equalsIgnoreCase("Equation")){
                        EquationEffect effect = new EquationEffect(NikeyV1.em);
                        effect.setLocation(p.getLocation());
                        effect.start();
                        p.sendMessage("Started");
                    }else if (args[0].equalsIgnoreCase("Explode")){
                        ExplodeEffect effect = new ExplodeEffect(NikeyV1.em);
                        effect.setLocation(p.getLocation());
                        effect.amount = 100;
                        effect.start();
                        p.sendMessage("Started");
                    }else if (args[0].equalsIgnoreCase("Fountain")){
                        FountainEffect effect = new FountainEffect(NikeyV1.em);
                        effect.setLocation(p.getLocation());
                        effect.start();
                        p.sendMessage("Started");
                    }else if (args[0].equalsIgnoreCase("Grid")){
                        GridEffect effect = new GridEffect(NikeyV1.em);
                        effect.setLocation(p.getLocation());
                        effect.start();
                        p.sendMessage("Started");
                    }else if (args[0].equalsIgnoreCase("Hill")){
                        HillEffect effect = new HillEffect(NikeyV1.em);
                        effect.setLocation(p.getLocation());
                        effect.start();
                        p.sendMessage("Started");
                    }else if (args[0].equalsIgnoreCase("Icon")){
                        IconEffect effect = new IconEffect(NikeyV1.em);
                        effect.setLocation(p.getLocation());
                        effect.start();
                        p.sendMessage("Started");
                    }else if (args[0].equalsIgnoreCase("Modified")){
                        ModifiedEffect effect = new ModifiedEffect(NikeyV1.em);
                        effect.setLocation(p.getLocation());
                        effect.start();
                        p.sendMessage("Started");
                    }else if (args[0].equalsIgnoreCase("Tornado")){
                        TornadoEffect effect = new TornadoEffect(NikeyV1.em);
                        effect.setLocation(p.getLocation());
                        effect.maxTornadoRadius = 3F;
                        effect.start();
                        p.sendMessage("Started");
                    } else if (args[0].equalsIgnoreCase("Wave")) {
                        WaveEffect effect = new WaveEffect(NikeyV1.em);
                        effect.setLocation(p.getLocation());
                        effect.start();
                        p.sendMessage("Started");
                    }else if (args[0].equalsIgnoreCase("Smoke")) {
                        SmokeEffect effect = new SmokeEffect(NikeyV1.em);
                        effect.setLocation(p.getLocation());
                        effect.start();
                        p.sendMessage("Started");
                    }else if (args[0].equalsIgnoreCase("Flame")) {
                        FlameEffect effect = new FlameEffect(NikeyV1.em);
                        effect.setLocation(p.getLocation());
                        effect.start();
                        p.sendMessage("Started");
                    }else if (args[0].equalsIgnoreCase("Shield")) {
                        ShieldEffect effect = new ShieldEffect(NikeyV1.em);
                        effect.setLocation(p.getLocation());
                        effect.start();
                        p.sendMessage("Started");
                    }else if (args[0].equalsIgnoreCase("Musik")) {
                        MusicEffect effect = new MusicEffect(NikeyV1.em);
                        effect.setLocation(p.getLocation());
                        effect.start();
                        p.sendMessage("Started");
                    }else if (args[0].equalsIgnoreCase("Line")) {
                        LineEffect effect = new LineEffect(NikeyV1.em);
                        effect.setLocation(p.getLocation());
                        effect.start();
                        p.sendMessage("Started");
                    }else if (args[0].equalsIgnoreCase("Turn")) {
                        TurnEffect effect = new TurnEffect(NikeyV1.em);
                        effect.setEntity(p);
                        effect.start();
                        p.sendMessage("Started");
                    }else if (args[0].equalsIgnoreCase("Wave")) {
                        WaveEffect effect = new WaveEffect(NikeyV1.em);
                        effect.start();
                        p.sendMessage("Started");
                    }else if (args[0].equalsIgnoreCase("Star")) {
                        StarEffect effect = new StarEffect(NikeyV1.em);
                        effect.start();
                        p.sendMessage("Started");
                    }else if (args[0].equalsIgnoreCase("jump")) {
                        JumpEffect effect = new JumpEffect(NikeyV1.em);
                        effect.setEntity(p);
                        effect.start();
                        p.sendMessage("Started");
                    }else if (args[0].equalsIgnoreCase("Donut")) {
                        DonutEffect effect = new DonutEffect(NikeyV1.em);
                        effect.setLocation(p.getLocation());
                        effect.start();
                        p.sendMessage("Started");
                    }
                }
            }else {
                p.sendMessage("That doesnt work like so");
            }
        }
        return true;
    }
}
