import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import fr.mitoto.hardcoreredemption.Main;
import fr.mitoto.hardcoreredemption.inventories.RedemptionInventory;
import fr.mitoto.hardcoreredemption.items.Heads;
import fr.mitoto.hardcoreredemption.utils.BlacklistManager;
import fr.mitoto.hardcoreredemption.utils.InventoryUtils;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@Execution(ExecutionMode.SAME_THREAD)
public class RedemptionInventoryTest {
    private ServerMock server;

    @BeforeEach()
    void setUp() {
        server = MockBukkit.mock();
        MockBukkit.load(Main.class);
        BlacklistManager.setInMemory(true);
    }

    @AfterEach()
    void tearDown() {
        MockBukkit.unmock();

        // reset Blacklist
        HashSet<UUID> uuids = new HashSet<>(BlacklistManager.getBlacklist());
        uuids.forEach(BlacklistManager::removePlayerFromBlacklist);
    }

    @ParameterizedTest()
    @ValueSource(ints = {0, 5, 10})
    public void inventoryIsCreatedWithBlacklistedPlayer(int playerCount) {
        for (int i = 0; i < playerCount; i++) {
            PlayerMock player = server.addPlayer();
            BlacklistManager.addPlayerToBlacklist(player.getUniqueId());
        }

        RedemptionInventory redemptionInventory = RedemptionInventory.getRedemptionInventory(server.addPlayer());
        redemptionInventory.fullUpdateInventory();
        Inventory inventory = redemptionInventory.getInventory();
        long count = Arrays.stream(inventory.getContents())
                .filter(Objects::nonNull)
                .filter(item -> item.getType() == Material.PLAYER_HEAD)
                .count();
        assertEquals(playerCount, count);
    }

    @Test
    public void playerInteractBorder() {
        PlayerMock player = server.addPlayer();
        RedemptionInventory redemptionInventory = RedemptionInventory.getRedemptionInventory(server.addPlayer());
        redemptionInventory.fullUpdateInventory();
        Inventory inventory = redemptionInventory.getInventory();

        int targetSlot = InventoryUtils.getItemSlot(inventory, RedemptionInventory.borderItem);
        assertNotEquals(-1, targetSlot);

        player.openInventory(inventory);
        InventoryClickEvent e = player.simulateInventoryClick(player.getOpenInventory(), targetSlot);

        assertTrue(e.isCancelled());
    }

    @Test
    public void playerReviveAnotherPlayer() {
        PlayerMock deadPlayer = server.addPlayer("deadPlayer");
        PlayerMock player = server.addPlayer("player");
        BlacklistManager.addPlayerToBlacklist(deadPlayer.getUniqueId());

        RedemptionInventory redemptionInventory = RedemptionInventory.getRedemptionInventory(server.addPlayer());
        redemptionInventory.fullUpdateInventory();
        Inventory inventory = redemptionInventory.getInventory();
        int slot = InventoryUtils.getItemSlot(inventory, Heads.getOfflinePlayerHead(deadPlayer));

        player.openInventory(inventory);
        player.simulateInventoryClick(player.getOpenInventory(), slot);

        assertFalse(BlacklistManager.isBlacklisted(deadPlayer.getUniqueId()));
    }
}
