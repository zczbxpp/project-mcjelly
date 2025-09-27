package pl.zczb.tools.config;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import pl.zczb.sectors.managers.data.SectorTypeEnum;
import pl.zczb.sectors.serialization.SerializationHelper;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
public class SectorConfig extends OkaeriConfig {

    @Comment({"AdminMode?"})
    public boolean adminMode = false;

    @Comment({"Czy bossbar wlaczony? Nie ruszaj "})
    public boolean bossbar = false;

    @Comment({"Wiadomosc bossbar"})
    public String bossbar_title = "";

    @Comment({"Kolor bossbar"})
    public String bossbar_color = "";

    @Comment({"Lokalizacja spawna"})
    public String spawn = SerializationHelper.locToString(new Location(Bukkit.getWorld("world"), 0.0D, 90.0D, 0.0D));

    @Comment({"Chat Template"})
    public String chat_template = "%group% &7%nick% &8» &f%message%";

    @Comment({"Automatyczna wiadomosc "})
    public List<String> auto_message = Arrays.asList(
            "&8[&5&l!&8] &7Nie wiesz jak zacząć? Użyj komendy &d/pomoc",
            "&8[&5&l!&8] &7Wpadnij na naszego discorda serwerowego &fdc.mcjelly.pl",
            "&8[&5&l!&8] &7Odbierz testowego brusha używając komendy &f/testowybrush"
    );

    @Comment({"Lokalizacja startu parkoura"})
    public String parkour_start = SerializationHelper.locToString(new Location(Bukkit.getWorld("world"), -2.0D, 97.0D, 5.9D));

    @Comment({"Lokalizacja konca parkoura"})
    public String parkour_end = SerializationHelper.locToString(new Location(Bukkit.getWorld("world"), -2.0D, 97.0D, 5.9D));

    @Comment({"y od ktorego gracza cofa na start"})
    public int parkour_y = 97;

    @Comment({"auto quiz chat events"})
    public String quiz_command = "getcase give {PLAYER} afk 1";

    @Comment({
            "Definicja sektorów. Tutaj określasz tryby gry (kanały) i listę sektorów w każdym trybie.",
            "Nazwy trybów (klucze mapy) muszą odpowiadać wartościom w pl.zczb.sectors.managers.data.SectorTypeEnum.",
            "Przykład:",
            "  CASHBLOCK:",
            "    - cashblock_1",
            "    - cashblock_2",
            "  LOBBY:",
            "    - lobby_main",
            "  IGRZYSKA:",
            "    - igrzyska_arena_1"
    })
    public Map<SectorTypeEnum, List<String>> definedSectors = new LinkedHashMap<>() {{
        put(SectorTypeEnum.CASHBLOCK, Arrays.asList("cashblock_1", "cashblock_2", "cashblock_3", "cashblock_4", "cashblock_5"));
    }};

    @Comment({
            "Konfiguracja aktualnie uruchomionego sektora.",
            "Te ustawienia identyfikują ten konkretny serwer w systemie sektorów.",
            "Uwaga: adminMode to początkowa wartość, może być zmieniona dynamicznie."
    })
    public CurrentSectorConfig currentSector = new CurrentSectorConfig();
    @Data
    public static class CurrentSectorConfig extends OkaeriConfig {

        @Comment({"Nazwa tego sektora (np. lobby_1, cashblock_alpha). Musi być unikalna."})
        public String sectorName = "cashblock_1";

        @Comment({"Typ tego sektora: LOBBY, CASHBLOCK, IGRZYSKA, EVENT, GAME."})
        public SectorTypeEnum sectorType = SectorTypeEnum.CASHBLOCK;

        @Comment({"Czy ten sektor jest w trybie administracyjnym? (Początkowa wartość, może być zmieniona dynamicznie)"})
        public boolean adminMode = false;
    }
}