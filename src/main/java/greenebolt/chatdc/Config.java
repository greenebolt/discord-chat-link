package greenebolt.chatdc;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class Config {
    private static final Properties defaultValues = new Properties();
    public static String fileName;

    public static String configVersion = Afkchattodiscord.VERSION;
    public static String BOT_TOKEN = "";
    public static String GUILD_ID = "";
    public static String CHANNEL_ID = "";

    Config(String fileName) {
        this.fileName = fileName;
    }

    public void read() {
        Properties properties = new Properties(defaultValues);

        try {
            FileReader configReader = new FileReader(fileName);
            properties.load(configReader);
            configReader.close();
        } catch (FileNotFoundException ignored) {
            // If the config does not exist, generate the default one.
            Afkchattodiscord.LOGGER.info("Generating the config file at: " + fileName);
            save();
            return;
        } catch (IOException e) {
            Afkchattodiscord.LOGGER.info("Failed to read the config file: " + fileName);
            e.printStackTrace();
        }

        configVersion = properties.getProperty("CONFIG_VERSION");
        BOT_TOKEN = properties.getProperty("BOT_TOKEN");
        GUILD_ID = properties.getProperty("GUILD_ID");
        CHANNEL_ID = properties.getProperty("CHANNEL_ID");

        if (!configVersion.equals(Config.configVersion)) {
            // The mod has been updated: Stuff can happen here
        }
    }

    public static void save() {
        try {
            File config = new File(fileName);
            boolean existed = config.exists();
            File parentDir = config.getParentFile();
            if (!parentDir.exists())
                parentDir.mkdirs();

            FileWriter configWriter = new FileWriter(config);

            writeString(configWriter, "CONFIG_VERSION", Afkchattodiscord.VERSION);
            writeString(configWriter, "BOT_TOKEN", BOT_TOKEN);
            writeString(configWriter, "GUILD_ID", GUILD_ID);
            writeString(configWriter, "CHANNEL_ID", CHANNEL_ID);

            configWriter.close();

            if (!existed)
                Afkchattodiscord.LOGGER.info("Created the config file.");
        } catch (IOException e) {
            Afkchattodiscord.LOGGER.info("Failed to write the config file: " + fileName);
            e.printStackTrace();
        }
    }

    private static void writeString(FileWriter configWriter, String name, String value) throws IOException {
        configWriter.write(name + '=' + value + '\n');
    }

    static {
        defaultValues.setProperty("BOT_TOKEN", "");
        defaultValues.setProperty("CONFIG_VERSION","1.0.0");
        defaultValues.setProperty("GUILD_ID", "");
        defaultValues.setProperty("CHANNEL_ID", "");
    }
}
