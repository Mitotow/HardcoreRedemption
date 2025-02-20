import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import fr.mitoto.hardcoreredemption.Main;
import fr.mitoto.hardcoreredemption.utils.SoundUtils;
import org.bukkit.Sound;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SoundUtilsTest {
    private ServerMock server;

    @BeforeEach()
    public void setUp() {
        server = MockBukkit.mock();
        MockBukkit.load(Main.class);
    }

    @AfterEach()
    public void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    public void soundIsBroadcastedToEveryPlayer() {
        // Add 10 players
        for (int i = 0; i < 10; i++)
            server.addPlayer();

        SoundUtils.broadcastSound(Sound.ENTITY_WITHER_SPAWN, 0.5f, 0f);
        server.getOnlinePlayers().forEach(player -> player.assertSoundHeard(Sound.ENTITY_WITHER_SPAWN));
    }
}
