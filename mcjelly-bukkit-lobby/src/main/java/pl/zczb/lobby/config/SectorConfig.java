package pl.zczb.lobby.config;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import lombok.Data;
import pl.zczb.sectors.managers.data.SectorTypeEnum;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
public class SectorConfig extends OkaeriConfig {

    @Comment({"ip bazy danych"})
    private String host = "127.0.0.1";

    @Comment({"Nazwa bazy danych"})
    private String base = "test";

    @Comment({"Użytkownik bazy danych"})
    private String user = "root";

    @Comment({"Port bazy danych"})
    private int port = 3306;

    @Comment({"Hasło do bazy danych"})
    private String pass = "";

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
    private Map<SectorTypeEnum, List<String>> definedSectors = new LinkedHashMap<>();

    @Comment({
            "Konfiguracja aktualnie uruchomionego sektora.",
            "Te ustawienia identyfikują ten konkretny serwer w systemie sektorów.",
            "Uwaga: adminMode to początkowa wartość, może być zmieniona dynamicznie."
    })
    private CurrentSectorConfig currentSector = new CurrentSectorConfig();


    public SectorConfig() {
        definedSectors.put(SectorTypeEnum.CASHBLOCK, Arrays.asList("cashblock_1", "cashblock_2", "cashblock_3", "cashblock_4", "cashblock_5"));
        definedSectors.put(SectorTypeEnum.IGRZYSKA, Arrays.asList("igrzyska_1", "igrzyska_2", "igrzyska_solo_1", "igrzyska_solo_2", "igrzyska_solo_3", "igrzyska_duo_1", "igrzyska_duo_2"));
        definedSectors.put(SectorTypeEnum.LOBBY, Arrays.asList("lobby_1", "lobby_2", "lobby_3", "lobby_4", "lobby_5"));
    }

    @Data
    public static class CurrentSectorConfig extends OkaeriConfig {

        @Comment({"Nazwa tego sektora (np. lobby_1, cashblock_alpha). Musi być unikalna."})
        private String sectorName = "lobby_1";

        @Comment({"Typ tego sektora: LOBBY, CASHBLOCK, IGRZYSKA, EVENT, GAME."})
        private SectorTypeEnum sectorType = SectorTypeEnum.LOBBY;

        @Comment({"Czy ten sektor jest w trybie administracyjnym? (Początkowa wartość, może być zmieniona dynamicznie)"})
        private boolean adminMode = false;

    }
}