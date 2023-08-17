package de.nikey.nikeyv1.Util;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Config {
    private FileConfiguration fileConfiguration;
    private File file;

    public File getFile() {
        return file;
    }

    public FileConfiguration toFileConfiguration() {
        return fileConfiguration;
    }

    public Config(String name, File path){
        file = new File(path,name);

        if (!file.exists()){
            path.mkdir();
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        fileConfiguration= new YamlConfiguration();
        try {
            fileConfiguration.load(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }

    }
    public void save(){
        try {
            fileConfiguration.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void reload(){
        try {
            fileConfiguration.load(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
    }
}
