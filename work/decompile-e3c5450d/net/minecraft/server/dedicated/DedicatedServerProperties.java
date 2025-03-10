package net.minecraft.server.dedicated;

import java.nio.file.Path;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import net.minecraft.core.IRegistryCustom;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.level.EnumGamemode;
import net.minecraft.world.level.levelgen.GeneratorSettings;

public class DedicatedServerProperties extends PropertyManager<DedicatedServerProperties> {

    public final boolean onlineMode = this.getBoolean("online-mode", true);
    public final boolean preventProxyConnections = this.getBoolean("prevent-proxy-connections", false);
    public final String serverIp = this.getString("server-ip", "");
    public final boolean spawnAnimals = this.getBoolean("spawn-animals", true);
    public final boolean spawnNpcs = this.getBoolean("spawn-npcs", true);
    public final boolean pvp = this.getBoolean("pvp", true);
    public final boolean allowFlight = this.getBoolean("allow-flight", false);
    public final String resourcePack = this.getString("resource-pack", "");
    public final boolean requireResourcePack = this.getBoolean("require-resource-pack", false);
    public final String resourcePackPrompt = this.getString("resource-pack-prompt", "");
    public final String motd = this.getString("motd", "A Minecraft Server");
    public final boolean forceGameMode = this.getBoolean("force-gamemode", false);
    public final boolean enforceWhitelist = this.getBoolean("enforce-whitelist", false);
    public final EnumDifficulty difficulty;
    public final EnumGamemode gamemode;
    public final String levelName;
    public final int serverPort;
    public final Boolean announcePlayerAchievements;
    public final boolean enableQuery;
    public final int queryPort;
    public final boolean enableRcon;
    public final int rconPort;
    public final String rconPassword;
    public final String resourcePackHash;
    public final String resourcePackSha1;
    public final boolean hardcore;
    public final boolean allowNether;
    public final boolean spawnMonsters;
    public final boolean snooperEnabled;
    public final boolean useNativeTransport;
    public final boolean enableCommandBlock;
    public final int spawnProtection;
    public final int opPermissionLevel;
    public final int functionPermissionLevel;
    public final long maxTickTime;
    public final int rateLimitPacketsPerSecond;
    public final int viewDistance;
    public final int maxPlayers;
    public final int networkCompressionThreshold;
    public final boolean broadcastRconToOps;
    public final boolean broadcastConsoleToOps;
    public final int maxWorldSize;
    public final boolean syncChunkWrites;
    public final boolean enableJmxMonitoring;
    public final boolean enableStatus;
    public final int entityBroadcastRangePercentage;
    public final String textFilteringConfig;
    public final PropertyManager<DedicatedServerProperties>.EditableProperty<Integer> playerIdleTimeout;
    public final PropertyManager<DedicatedServerProperties>.EditableProperty<Boolean> whiteList;
    @Nullable
    public GeneratorSettings worldGenSettings;

    public DedicatedServerProperties(Properties properties) {
        super(properties);
        this.difficulty = (EnumDifficulty) this.a("difficulty", a(EnumDifficulty::getById, EnumDifficulty::a), EnumDifficulty::c, EnumDifficulty.EASY);
        this.gamemode = (EnumGamemode) this.a("gamemode", a(EnumGamemode::getById, EnumGamemode::a), EnumGamemode::b, EnumGamemode.SURVIVAL);
        this.levelName = this.getString("level-name", "world");
        this.serverPort = this.getInt("server-port", 25565);
        this.announcePlayerAchievements = this.b("announce-player-achievements");
        this.enableQuery = this.getBoolean("enable-query", false);
        this.queryPort = this.getInt("query.port", 25565);
        this.enableRcon = this.getBoolean("enable-rcon", false);
        this.rconPort = this.getInt("rcon.port", 25575);
        this.rconPassword = this.getString("rcon.password", "");
        this.resourcePackHash = this.a("resource-pack-hash");
        this.resourcePackSha1 = this.getString("resource-pack-sha1", "");
        this.hardcore = this.getBoolean("hardcore", false);
        this.allowNether = this.getBoolean("allow-nether", true);
        this.spawnMonsters = this.getBoolean("spawn-monsters", true);
        if (this.getBoolean("snooper-enabled", true)) {
            ;
        }

        this.snooperEnabled = false;
        this.useNativeTransport = this.getBoolean("use-native-transport", true);
        this.enableCommandBlock = this.getBoolean("enable-command-block", false);
        this.spawnProtection = this.getInt("spawn-protection", 16);
        this.opPermissionLevel = this.getInt("op-permission-level", 4);
        this.functionPermissionLevel = this.getInt("function-permission-level", 2);
        this.maxTickTime = this.getLong("max-tick-time", TimeUnit.MINUTES.toMillis(1L));
        this.rateLimitPacketsPerSecond = this.getInt("rate-limit", 0);
        this.viewDistance = this.getInt("view-distance", 10);
        this.maxPlayers = this.getInt("max-players", 20);
        this.networkCompressionThreshold = this.getInt("network-compression-threshold", 256);
        this.broadcastRconToOps = this.getBoolean("broadcast-rcon-to-ops", true);
        this.broadcastConsoleToOps = this.getBoolean("broadcast-console-to-ops", true);
        this.maxWorldSize = this.a("max-world-size", (integer) -> {
            return MathHelper.clamp(integer, 1, 29999984);
        }, 29999984);
        this.syncChunkWrites = this.getBoolean("sync-chunk-writes", true);
        this.enableJmxMonitoring = this.getBoolean("enable-jmx-monitoring", false);
        this.enableStatus = this.getBoolean("enable-status", true);
        this.entityBroadcastRangePercentage = this.a("entity-broadcast-range-percentage", (integer) -> {
            return MathHelper.clamp(integer, 10, 1000);
        }, 100);
        this.textFilteringConfig = this.getString("text-filtering-config", "");
        this.playerIdleTimeout = this.b("player-idle-timeout", 0);
        this.whiteList = this.b("white-list", false);
    }

    public static DedicatedServerProperties load(Path path) {
        return new DedicatedServerProperties(loadPropertiesFile(path));
    }

    @Override
    protected DedicatedServerProperties reload(IRegistryCustom iregistrycustom, Properties properties) {
        DedicatedServerProperties dedicatedserverproperties = new DedicatedServerProperties(properties);

        dedicatedserverproperties.a(iregistrycustom);
        return dedicatedserverproperties;
    }

    public GeneratorSettings a(IRegistryCustom iregistrycustom) {
        if (this.worldGenSettings == null) {
            this.worldGenSettings = GeneratorSettings.a(iregistrycustom, this.properties);
        }

        return this.worldGenSettings;
    }
}
