# mcjelly-server

**mcjelly-server** to zaawansowany system sektorów dla serwerów Minecraft, oparty na Redis.  
Projekt został stworzony z myślą o skalowalności i wydajności dużych serwerów, umożliwiając dynamiczne rozdzielanie graczy pomiędzy niezależne instancje, pełną synchronizację danych oraz obsługę trybów gry i eventów.

---

## Kluczowe funkcjonalności

- **Zarządzanie sektorami** – podział serwera na niezależne sektory (np. spawn, magazyn, kopalnia, event).  
- **Komunikacja między sektorami** – kanały Redis zapewniające szybki i bezpieczny przesył pakietów.  
- **Obsługa użytkowników** – teleportacja między sektorami, synchronizacja ekwipunku, statystyk i lokalizacji.  
- **System ekonomii** – wsparcie dla sklepów (ItemShop), aukcji i płatności między graczami.  
- **Eventy globalne** – obsługa wydarzeń takich jak *Lodowy Golem, Piniata, Turbokasa, Deszcz kluczy*.  
- **Strefy PvP i eventowe** – dedykowane sektory dla walk i wydarzeń specjalnych.  
- **Obsługa przedmiotów specjalnych** – system przedmiotów premium, efektów i ulepszeń.  
- **System anty-AFK** – ochrona przed nieuczciwym farmieniem.  
- **Obsługa czatu** – zarządzanie wiadomościami, chat administracyjny, kontrola spamu.  

---

## Technologie i bazy danych

- **Redis** – komunikacja międzysektorowa w czasie rzeczywistym, przechowywanie sesji i synchronizacja danych graczy.  
- **MongoDB** – długoterminowe przechowywanie danych, m.in. statystyk, rankingów i konfiguracji sklepów.  

---

## Instalacja i konfiguracja

### 1. Pobieranie i instalacja
Umieść odpowiednie pluginy w katalogu `plugins` wybranych instancji serwera Minecraft:

- **Serwer Lobby**:
  - `mcjelly-global-controller`  
  - `mcjelly-bukkit-lobby`  
  - `mcjelly-bukkit-itemshop`  

- **Serwer Cashblock**:
  - `mcjelly-global-controller`  
  - `mcjelly-bukkit-cashblock`  
  - `mcjelly-bukkit-itemshop`  
  - `mcjelly-bukkit-tools`  

- **Serwer Event**:
  - `mcjelly-global-controller`  
  - `mcjelly-bukkit-cashblock`  
  - `mcjelly-bukkit-event`  
  - `mcjelly-bukkit-itemshop`  
  - `mcjelly-bukkit-tools`  

- **Proxy (Velocity)**:
  - `mcjelly-velocity-tools`  

Po uruchomieniu serwera wygenerowane zostaną pliki konfiguracyjne.

---

### 2. Konfiguracja sektorów (`SectorsConfig.yml`)

```yaml
trybName: cashblock
sectorType: GAME
sectorName: cashblock_1
sectorList:
  cashblock_1
  cashblock_2
```

---

## Struktura projektu

- **mcjelly-global-controller** – centralny moduł komunikacyjny oparty na Redis, odpowiedzialny za synchronizację.  
- **mcjelly-bukkit-tools** – obsługa i rejestracja sektorów, główne komendy serwerowe.  
- **mcjelly-bukkit-cashblock** – implementacja trybu gry Cashblock.  
- **mcjelly-bukkit-event** – obsługa eventów specjalnych.  
- **mcjelly-bukkit-lobby** – system poczekalni oraz kolejek między sektorami.  
- **mcjelly-bukkit-itemshop** – obsługa zakupów w grze połączonych z systemem www.  
- **mcjelly-velocity-tools** – moduł integracyjny dla proxy Velocity.  

---

