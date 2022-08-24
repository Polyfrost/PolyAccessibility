package cc.polyfrost.polyaccessibility;

import java.util.HashMap;
import java.util.Map;

import cc.polyfrost.polyaccessibility.client.ClientHandler;
import cc.polyfrost.polyaccessibility.config.PAConfig;
import cc.polyfrost.polyaccessibility.keyboard.KeyboardController;
import cc.polyfrost.polyaccessibility.mixin.AccessorHandledScreen;
import cc.polyfrost.polyaccessibility.narrator.PolyNarrator;
import cc.polyfrost.polyaccessibility.utils.PolyThread;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

public class PolyAccessibility implements ModInitializer {
    public static PAConfig config;
    public static PolyAccessibility instance;
    public static PolyNarrator narrator;
    public static KeyboardController keyboardController;

    public static int currentColumn = 0;
    public static int currentRow = 0;
    public static boolean isDPressed, isAPressed, isWPressed, isSPressed, isRPressed, isFPressed, isCPressed, isVPressed,
            isTPressed, isEnterPressed;
    public static Map<String, Integer> delayThreadMap;
    private static PolyThread delayThread;
    private ClientHandler clientHandler;

    @Override
    public void onInitialize() {
        long time = System.currentTimeMillis();
        instance = this;
        narrator = new PolyNarrator();
        keyboardController = new KeyboardController();

        System.setProperty("java.awt.headless", "false");
        System.setProperty("poly.dev.access", "accessor()V");

        delayThreadMap = new HashMap<String, Integer>();
        delayThread = new PolyThread();
        delayThread.startThread();

        clientHandler = new ClientHandler();

        AutoConfig.register(PAConfig.class, GsonConfigSerializer::new);
        config = AutoConfig.getConfigHolder(PAConfig.class).getConfig();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (config.keyboardInventoryControlToggle) {
                isDPressed = (InputUtil.isKeyPressed(client.getWindow().getHandle(),
                        InputUtil.fromTranslationKey("key.keyboard.d").getCode()));
                isAPressed = (InputUtil.isKeyPressed(client.getWindow().getHandle(),
                        InputUtil.fromTranslationKey("key.keyboard.a").getCode()));
                isWPressed = (InputUtil.isKeyPressed(client.getWindow().getHandle(),
                        InputUtil.fromTranslationKey("key.keyboard.w").getCode()));
                isSPressed = (InputUtil.isKeyPressed(client.getWindow().getHandle(),
                        InputUtil.fromTranslationKey("key.keyboard.s").getCode()));
                isRPressed = (InputUtil.isKeyPressed(client.getWindow().getHandle(),
                        InputUtil.fromTranslationKey("key.keyboard.r").getCode()));
                isFPressed = (InputUtil.isKeyPressed(client.getWindow().getHandle(),
                        InputUtil.fromTranslationKey("key.keyboard.f").getCode()));
                isCPressed = (InputUtil.isKeyPressed(client.getWindow().getHandle(),
                        InputUtil.fromTranslationKey("key.keyboard.c").getCode()));
                isVPressed = (InputUtil.isKeyPressed(client.getWindow().getHandle(),
                        InputUtil.fromTranslationKey("key.keyboard.v").getCode()));
                isTPressed = (InputUtil.isKeyPressed(client.getWindow().getHandle(),
                        InputUtil.fromTranslationKey("key.keyboard.t").getCode()));
                isEnterPressed = (InputUtil.isKeyPressed(client.getWindow().getHandle(),
                        InputUtil.fromTranslationKey("key.keyboard.enter").getCode()));

                if (client.currentScreen == null) {
                    currentColumn = 0;
                    currentRow = 0;
                    ClientHandler.isSearchingRecipies = false;
                    ClientHandler.bookPageIndex =  0;
                } else {
                    Screen screen = client.currentScreen;
                    clientHandler.screenHandler(screen);
                }
            }

            if (client.currentScreen == null)
                return;

            if (client.currentScreen == null || !(client.currentScreen instanceof AccessorHandledScreen)) {
                HitResult hitResult = client.crosshairTarget;
                switch (hitResult.getType()) {
                    case MISS, ENTITY -> {
                    }
                    case BLOCK -> {
                        BlockHitResult blockHitResult = (BlockHitResult) hitResult;
                        BlockPos blockPos = blockHitResult.getBlockPos();
                        BlockState blockState = client.world.getBlockState(blockPos);

                        Block block = blockState.getBlock();

                        if (!block.equals(narrator.lastBlock) || !blockPos.equals(narrator.lastBlockPos)) {
                            narrator.lastBlock = block;
                            narrator.lastBlockPos = blockPos;

                            String output = "";

                            if (config.readBlocksToggle) {
                                output += block.getName().getString();
                            }
                            if (blockState.toString().contains("sign") && config.readSignContentsToggle) {
                                try {
                                    SignBlockEntity signBlockEntity = (SignBlockEntity) client.world.getBlockEntity(blockPos);
                                    output += " says: ";
                                    assert signBlockEntity != null;
                                    output += "1: " + signBlockEntity.getTextOnRow(0, false).getString() + ", ";
                                    output += "2: " + signBlockEntity.getTextOnRow(1, false).getString() + ", ";
                                    output += "3: " + signBlockEntity.getTextOnRow(2, false).getString() + ", ";
                                    output += "4: " + signBlockEntity.getTextOnRow(3, false).getString();
                                } catch (Exception ignored) {
                                }
                            } if (!output.equals("")) {
                                PolyNarrator.narrate(output);
                            }
                        }
                    }
                }
            }
        });

        System.out.println("[PolyAccessibility]: Initialized in " + (System.currentTimeMillis() - time));
    }
}
