package cc.polyfrost.polyaccessibility.client;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;

import cc.polyfrost.polyaccessibility.PolyAccessibility;
import cc.polyfrost.polyaccessibility.narrator.PolyNarrator;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.BookScreen;
import net.minecraft.client.gui.screen.ingame.CraftingScreen;
import net.minecraft.client.gui.screen.ingame.EnchantmentScreen;
import net.minecraft.client.gui.screen.ingame.LecternScreen;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.screen.LecternScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.MutableText;

public class ClientHandler {
    private int minColumn;
    private int maxColumn;
    private int currentColumn;
    private int differenceColumn;
    private int minRow;
    private int maxRow;
    private int currentRow;
    private int differenceRow;
    private int windowPosX;
    private int windowPosY;
    private int hudScreenOffsetX;
    private int hudScreenOffsetY;
    public static boolean isSearchingRecipies = false;
    public static int bookPageIndex = 0;

    public ClientHandler() {
        minColumn = 0;
        maxColumn = 0;
        currentColumn = 0;
        differenceColumn = 0;
        minRow = 0;
        maxRow = 0;
        currentRow = 0;
        differenceRow = 0;
        windowPosX = 0;
        windowPosY = 0;
        hudScreenOffsetX = 0;
        hudScreenOffsetY = 0;
    }

    public void screenHandler(Screen screen) {
        MutableText titleMutableText = Text.literal("").append(screen.getTitle());
        String titleString = titleMutableText.getString().toLowerCase();

        {
            if (!PolyAccessibility.delayThreadMap.containsKey("stonecutter_result_slot")
                    && titleString.contains("stonecutter")) {
                try {
                    stonecutterScreen(screen);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if ((titleString.contains("armorer") || titleString.contains("butcher")
                    || titleString.contains("cartographer") || titleString.contains("cleric") || titleString.contains("farmer")
                    || titleString.contains("fisherman") || titleString.contains("fletcher")
                    || titleString.contains("leatherworker") || titleString.contains("librarian") || titleString.contains("mason")
                    || titleString.contains("shepherd") || titleString.contains("toolsmith")
                    || titleString.contains("weaponsmith") || titleString.contains("wandering trader"))) {
                try {
                    tradingScreen(screen);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (titleString.contains("crafting")) {
                try {
                    craftingScreen(screen);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (screen instanceof EnchantmentScreen) {
                try {
                    enchantingScreen(screen);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void stonecutterScreen(Screen screen) {
        MinecraftClient client = MinecraftClient.getInstance();
        try {
            minColumn = (int) (client.getWindow().getWidth() * 0.4409);
            differenceColumn = (int) (client.getWindow().getWidth() * 0.0347) + (int) ((double) (480 - screen.width) / 8);
            maxColumn = minColumn + (3 * differenceColumn);

            minRow = (int) (client.getWindow().getHeight() * 0.3);
            differenceRow = (int) (client.getWindow().getHeight() * 0.0666) + (int) ((double) (300 - screen.height) / 10);
            maxRow = minRow + (2 * differenceRow);

            windowPosX = client.getWindow().getX();
            windowPosY = client.getWindow().getY();

            hudScreenOffsetX = (int) ((double) (480 - screen.width) / 4);
            hudScreenOffsetY = (int) ((double) (300 - screen.height) / 3);
            Robot robot;
            robot = new Robot();
            currentColumn = PolyAccessibility.currentColumn;
            currentRow = PolyAccessibility.currentRow;

            // D Pressed :- Next Column
            if (PolyAccessibility.isDPressed && !PolyAccessibility.delayThreadMap.containsKey("stonecutter_result_slot")) {
                if (currentColumn == 0 && currentRow == 0) {
                    currentColumn = minColumn;
                    currentRow = minRow;
                } else if (currentColumn >= maxColumn - 5 && currentColumn <= maxColumn + 5) {
                    currentColumn += 0;
                } else {
                    currentColumn += differenceColumn;
                }
                robot.mouseMove(currentColumn + windowPosX - hudScreenOffsetX, currentRow + windowPosY - hudScreenOffsetY);
                PolyAccessibility.delayThreadMap.put("stonecutter_result_slot", 200);
            }

            // A Pressed :- Previous Column
            if (PolyAccessibility.isAPressed && !PolyAccessibility.delayThreadMap.containsKey("stonecutter_result_slot")) {
                if (currentColumn == 0 && currentRow == 0) {
                    currentColumn = minColumn;
                    currentRow = minRow;
                } else if (currentColumn >= minColumn - 5 && currentColumn <= minColumn + 5) {
                    currentColumn -= 0;
                } else {
                    currentColumn -= differenceColumn;
                }
                robot.mouseMove(currentColumn + windowPosX - hudScreenOffsetX, currentRow + windowPosY - hudScreenOffsetY);
                PolyAccessibility.delayThreadMap.put("stonecutter_result_slot", 200);
            }

            // S Pressed :- Down Row
            if (PolyAccessibility.isSPressed && !PolyAccessibility.delayThreadMap.containsKey("stonecutter_result_slot")) {
                if (currentColumn == 0 && currentRow == 0) {
                    currentColumn = minColumn;
                    currentRow = minRow;
                } else if (currentRow >= maxRow - 5 && currentRow <= maxRow + 5) {
                    currentRow += 0;
                } else {
                    currentRow += differenceRow;
                }
                robot.mouseMove(currentColumn + windowPosX - hudScreenOffsetX, currentRow + windowPosY - hudScreenOffsetY);
                PolyAccessibility.delayThreadMap.put("stonecutter_result_slot", 200);
            }

            // W Pressed :- Up Row
            if (PolyAccessibility.isWPressed && !PolyAccessibility.delayThreadMap.containsKey("stonecutter_result_slot")) {
                if (currentColumn == 0 && currentRow == 0) {
                    currentColumn = minColumn;
                    currentRow = minRow;
                } else if (currentRow >= minRow - 5 && currentRow <= minRow + 5) {
                    currentRow -= 0;
                } else {
                    currentRow -= differenceRow;
                }
                robot.mouseMove(currentColumn + windowPosX - hudScreenOffsetX, currentRow + windowPosY - hudScreenOffsetY);
                PolyAccessibility.delayThreadMap.put("stonecutter_result_slot", 200);
            }

            PolyAccessibility.currentColumn = currentColumn;
            PolyAccessibility.currentRow = currentRow;
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    private void tradingScreen(Screen screen) {
        MinecraftClient client = MinecraftClient.getInstance();
        try {
            minColumn = (int) (client.getWindow().getWidth() * 0.25347222);
            differenceColumn = (int) (client.getWindow().getWidth() * 0.0625) + (int) ((double) (480 - screen.width) / 8);
            maxColumn = minColumn + (2 * differenceColumn);

            minRow = (int) (client.getWindow().getHeight() * 0.32222);
            differenceRow = (int) (client.getWindow().getHeight() * 0.066667) + (int) ((double) (300 - screen.height) / 10);
            maxRow = minRow + (6 * differenceRow);

            windowPosX = client.getWindow().getX();
            windowPosY = client.getWindow().getY();

            hudScreenOffsetX = (int) ((double) (480 - screen.width) / 2);
            hudScreenOffsetY = (int) ((double) (300 - screen.height) / 3);

            Robot robot;
            robot = new Robot();
            currentColumn = PolyAccessibility.currentColumn;
            currentRow = PolyAccessibility.currentRow;

            // D Pressed :- Next Column
            if (!PolyAccessibility.delayThreadMap.containsKey("trading_screen") && PolyAccessibility.isDPressed) {
                if (currentColumn == 0 && currentRow == 0) {
                    currentColumn = minColumn;
                    currentRow = minRow;
                } else if (currentColumn >= maxColumn - 5 && currentColumn <= maxColumn + 5) {
                    currentColumn += 0;
                } else {
                    currentColumn += differenceColumn;
                }
                robot.mouseMove(currentColumn + windowPosX - hudScreenOffsetX, currentRow + windowPosY - hudScreenOffsetY);
                PolyAccessibility.delayThreadMap.put("trading_screen", 200);
            }

            // A Pressed :- Previous Column
            if (!PolyAccessibility.delayThreadMap.containsKey("trading_screen") && PolyAccessibility.isAPressed) {
                if (currentColumn == 0 && currentRow == 0) {
                    currentColumn = minColumn;
                    currentRow = minRow;
                } else if (currentColumn >= minColumn - 5 && currentColumn <= minColumn + 5) {
                    currentColumn -= 0;
                } else {
                    currentColumn -= differenceColumn;
                }
                robot.mouseMove(currentColumn + windowPosX - hudScreenOffsetX, currentRow + windowPosY - hudScreenOffsetY);
                PolyAccessibility.delayThreadMap.put("trading_screen", 200);
            }

            // S Pressed :- Down Row
            if (!PolyAccessibility.delayThreadMap.containsKey("trading_screen") && PolyAccessibility.isSPressed) {
                if (currentColumn == 0 && currentRow == 0) {
                    currentColumn = minColumn;
                    currentRow = minRow;
                } else if (currentRow >= maxRow - 5 && currentRow <= maxRow + 5) {
                    currentRow += 0;
                } else {
                    currentRow += differenceRow;
                }
                robot.mouseMove(currentColumn + windowPosX - hudScreenOffsetX, currentRow + windowPosY - hudScreenOffsetY);
                PolyAccessibility.delayThreadMap.put("trading_screen", 200);
            }

            // W Pressed :- Up Row
            if (!PolyAccessibility.delayThreadMap.containsKey("trading_screen") && PolyAccessibility.isWPressed) {
                if (currentColumn == 0 && currentRow == 0) {
                    currentColumn = minColumn;
                    currentRow = minRow;
                } else if (currentRow >= minRow - 5 && currentRow <= minRow + 5) {
                    currentRow -= 0;
                } else {
                    currentRow -= differenceRow;
                }
                robot.mouseMove(currentColumn + windowPosX - hudScreenOffsetX, currentRow + windowPosY - hudScreenOffsetY);
                PolyAccessibility.delayThreadMap.put("trading_screen", 200);
            }

            // R Pressed :- Scroll Up by 1
            if (!PolyAccessibility.delayThreadMap.containsKey("trading_scrolled_screen") && PolyAccessibility.isRPressed) {
                robot.mouseWheel(-1);
                PolyAccessibility.delayThreadMap.put("trading_scrolled_screen", 200);
            }

            // F Pressed :- Scrol Down by 1
            if (!PolyAccessibility.delayThreadMap.containsKey("trading_scrolled_screen") && PolyAccessibility.isFPressed) {
                robot.mouseWheel(+1);
                PolyAccessibility.delayThreadMap.put("trading_scrolled_screen", 200);
            }

            PolyAccessibility.currentColumn = currentColumn;
            PolyAccessibility.currentRow = currentRow;

        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    private void craftingScreen(Screen screen) {
        int recipeBookX;
        int recipeBookY;
        int filteringX;
        int filteringY;
        int nextX;
        int nextY;
        int prevX;
        int prevY;
        boolean isCraftingOpen = false;

        if (screen instanceof CraftingScreen)
            isCraftingOpen = true;

        MinecraftClient client = MinecraftClient.getInstance();
        client.player.getRecipeBook().isGuiOpen(RecipeBookCategory.CRAFTING);
        client.player.getRecipeBook().isFilteringCraftable(RecipeBookCategory.CRAFTING);

        if (PolyAccessibility.isTPressed && !isSearchingRecipies
                && client.player.getRecipeBook().isGuiOpen(RecipeBookCategory.CRAFTING))
            isSearchingRecipies = true;

        nextX = (int) (client.getWindow().getWidth() * 0.368055556);
        nextY = (int) (client.getWindow().getHeight() * 0.705555556);

        prevX = (int) (client.getWindow().getWidth() * 0.263888889);
        prevY = (int) (client.getWindow().getHeight() * 0.705555556);

        minColumn = (int) (client.getWindow().getWidth() * 0.215277778);
        differenceColumn = (int) (client.getWindow().getWidth() * 0.052083333) + (int) ((double) (480 - screen.width) / 8);
        maxColumn = minColumn + (4 * differenceColumn);

        minRow = (int) (client.getWindow().getHeight() * 0.366666667);
        differenceRow = (int) (client.getWindow().getHeight() * 0.088888889) + (int) ((double) (300 - screen.height) / 10);
        maxRow = minRow + (3 * differenceRow);

        windowPosX = client.getWindow().getX();
        windowPosY = client.getWindow().getY();

        hudScreenOffsetX = (int) ((double) (480 - screen.width) / 2);
        hudScreenOffsetY = (int) ((double) (300 - screen.height) / 3);

        filteringX = (int) (client.getWindow().getWidth() * 0.416666667) + (hudScreenOffsetX / 2);
        filteringY = (int) (client.getWindow().getHeight() * 0.288888889) - (hudScreenOffsetY / 2);

        if (isCraftingOpen) {
            if (client.player.getRecipeBook().isGuiOpen(RecipeBookCategory.CRAFTING)) {
                recipeBookX = (int) (client.getWindow().getWidth() * 0.506944444) + (hudScreenOffsetX / 2);
                recipeBookY = (int) (client.getWindow().getHeight() * 0.366666667);
            } else {
                recipeBookX = (int) (client.getWindow().getWidth() * 0.347222222);
                recipeBookY = (int) (client.getWindow().getHeight() * 0.366666667);
            }
        } else {
            if (client.player.getRecipeBook().isGuiOpen(RecipeBookCategory.CRAFTING)) {
                recipeBookX = (int) (client.getWindow().getWidth() * 0.715277778) + (hudScreenOffsetX) + (hudScreenOffsetX / 2);
                recipeBookY = (int) (client.getWindow().getHeight() * 0.455555556) + (hudScreenOffsetY / 2);
            } else {
                recipeBookX = (int) (client.getWindow().getWidth() * 0.555555556) + (hudScreenOffsetX) + (hudScreenOffsetX / 2);
                recipeBookY = (int) (client.getWindow().getHeight() * 0.455555556) + (hudScreenOffsetY / 2);
            }
        }

        if (!isSearchingRecipies) {
            try {

                Robot robot;
                robot = new Robot();
                currentColumn = PolyAccessibility.currentColumn;
                currentRow = PolyAccessibility.currentRow;

                // D Pressed :- Next Column
                if (!PolyAccessibility.delayThreadMap.containsKey("crafting_table_screen") && PolyAccessibility.isDPressed) {
                    if (currentColumn == 0 && currentRow == 0) {
                        currentColumn = minColumn;
                        currentRow = minRow;
                    } else if (currentColumn >= maxColumn - 5 && currentColumn <= maxColumn + 5) {
                        currentColumn += 0;
                    } else {
                        currentColumn += differenceColumn;
                    }
                    robot.mouseMove(currentColumn + windowPosX - hudScreenOffsetX, currentRow + windowPosY - hudScreenOffsetY);
                    PolyAccessibility.delayThreadMap.put("crafting_table_screen", 200);
                }

                // A Pressed :- Prev Column
                if (!PolyAccessibility.delayThreadMap.containsKey("crafting_table_screen") && PolyAccessibility.isAPressed) {
                    if (currentColumn == 0 && currentRow == 0) {
                        currentColumn = minColumn;
                        currentRow = minRow;
                    } else if (currentColumn >= minColumn - 5 && currentColumn <= minColumn + 5) {
                        currentColumn -= 0;
                    } else {
                        currentColumn -= differenceColumn;
                    }
                    robot.mouseMove(currentColumn + windowPosX - hudScreenOffsetX, currentRow + windowPosY - hudScreenOffsetY);
                    PolyAccessibility.delayThreadMap.put("crafting_table_screen", 200);
                }

                // S Pressed :- Down Row
                if (!PolyAccessibility.delayThreadMap.containsKey("crafting_table_screen") && PolyAccessibility.isSPressed) {
                    if (currentColumn == 0 && currentRow == 0) {
                        currentColumn = minColumn;
                        currentRow = minRow;
                    } else if (currentRow >= maxRow - 5 && currentRow <= maxRow + 5) {
                        currentRow += 0;
                    } else {
                        currentRow += differenceRow;
                    }
                    robot.mouseMove(currentColumn + windowPosX - hudScreenOffsetX, currentRow + windowPosY - hudScreenOffsetY);
                    PolyAccessibility.delayThreadMap.put("crafting_table_screen", 200);
                }

                // W Pressed :- Up Row
                if (!PolyAccessibility.delayThreadMap.containsKey("crafting_table_screen") && PolyAccessibility.isWPressed) {
                    if (currentColumn == 0 && currentRow == 0) {
                        currentColumn = minColumn;
                        currentRow = minRow;
                    } else if (currentRow >= minRow - 5 && currentRow <= minRow + 5) {
                        currentRow -= 0;
                    } else {
                        currentRow -= differenceRow;
                    }
                    robot.mouseMove(currentColumn + windowPosX - hudScreenOffsetX, currentRow + windowPosY - hudScreenOffsetY);
                    PolyAccessibility.delayThreadMap.put("crafting_table_screen", 200);
                }

                // R Pressed :- Next Page
                if (!PolyAccessibility.delayThreadMap.containsKey("crafting_table_scrolled_screen")
                        && PolyAccessibility.isRPressed && client.player.getRecipeBook().isGuiOpen(RecipeBookCategory.CRAFTING)) {
                    robot.mouseMove(nextX + windowPosX - hudScreenOffsetX + (hudScreenOffsetX / 2),
                            nextY + windowPosY + hudScreenOffsetY);
                    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                    PolyAccessibility.delayThreadMap.put("crafting_table_scrolled_screen", 200);
                }

                // F Pressed :- Prev Page
                if (!PolyAccessibility.delayThreadMap.containsKey("crafting_table_scrolled_screen")
                        && PolyAccessibility.isFPressed && client.player.getRecipeBook().isGuiOpen(RecipeBookCategory.CRAFTING)) {
                    robot.mouseMove(prevX + windowPosX - hudScreenOffsetX, prevY + windowPosY + hudScreenOffsetY);
                    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                    PolyAccessibility.delayThreadMap.put("crafting_table_scrolled_screen", 200);
                }

                // C Pressed :- Open/Close Recipe Book
                if (!PolyAccessibility.delayThreadMap.containsKey("space_pressed") && PolyAccessibility.isCPressed) {
                    robot.mouseMove(recipeBookX + windowPosX - hudScreenOffsetX, recipeBookY + windowPosY - hudScreenOffsetY);
                    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                    if (isCraftingOpen) {
                        if (!client.player.getRecipeBook().isGuiOpen(RecipeBookCategory.CRAFTING)) {
                            recipeBookX = (int) (client.getWindow().getWidth() * 0.506944444) + (hudScreenOffsetX / 2);
                            recipeBookY = (int) (client.getWindow().getHeight() * 0.366666667);
                        } else {
                            recipeBookX = (int) (client.getWindow().getWidth() * 0.347222222);
                            recipeBookY = (int) (client.getWindow().getHeight() * 0.366666667);
                        }
                    } else {
                        if (!client.player.getRecipeBook().isGuiOpen(RecipeBookCategory.CRAFTING)) {
                            recipeBookX = (int) (client.getWindow().getWidth() * 0.715277778) + (hudScreenOffsetX)
                                    + (hudScreenOffsetX / 2);
                            recipeBookY = (int) (client.getWindow().getHeight() * 0.455555556) + (hudScreenOffsetY / 2);
                        } else {
                            recipeBookX = (int) (client.getWindow().getWidth() * 0.555555556) + (hudScreenOffsetX)
                                    + (hudScreenOffsetX / 2);
                            recipeBookY = (int) (client.getWindow().getHeight() * 0.455555556) + (hudScreenOffsetY / 2);
                        }
                    }

                    robot.mouseMove(recipeBookX + windowPosX - hudScreenOffsetX, recipeBookY + windowPosY - hudScreenOffsetY);

                    PolyAccessibility.delayThreadMap.put("space_pressed", 200);
                    PolyNarrator.narrate("Recipe Book "
                            + (!(client.player.getRecipeBook().isGuiOpen(RecipeBookCategory.CRAFTING)) ? "on" : "off"));
                }

                // v Pressed :- Enable/Disable Show All
                if (!PolyAccessibility.delayThreadMap.containsKey("filtering_pressed") && PolyAccessibility.isVPressed
                        && client.player.getRecipeBook().isGuiOpen(RecipeBookCategory.CRAFTING)) {
                    robot.mouseMove(filteringX + windowPosX - hudScreenOffsetX, filteringY + windowPosY - hudScreenOffsetY);
                    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                    PolyAccessibility.delayThreadMap.put("filtering_pressed", 200);
                }

                PolyAccessibility.currentColumn = currentColumn;
                PolyAccessibility.currentRow = currentRow;

            } catch (AWTException e) {
                e.printStackTrace();
            }
        } else {

            try {

                // Disable Search Recipe
                if (PolyAccessibility.isEnterPressed) {

                    Robot robot;
                    robot = new Robot();

                    windowPosX = client.getWindow().getX();
                    windowPosY = client.getWindow().getY();

                    hudScreenOffsetX = (int) ((double) (480 - screen.width) / 2);
                    hudScreenOffsetY = (int) ((double) (300 - screen.height) / 3);

                    int closeSearchX = (int) (client.getWindow().getWidth() * 0.194444444);
                    int closeSearchY = (int) (client.getWindow().getHeight() * 0.277777778) - (hudScreenOffsetY / 2);

                    robot.mouseMove(closeSearchX + windowPosX - hudScreenOffsetX, closeSearchY + windowPosY - hudScreenOffsetY);
                    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

                    isSearchingRecipies = false;
                }
            } catch (AWTException e) {
                e.printStackTrace();
            }
        }
    }

    private void enchantingScreen(Screen screen) {
        MinecraftClient client = MinecraftClient.getInstance();
        try {
            minColumn = (int) (client.getWindow().getWidth() * 0.555555556);

            minRow = (int) (client.getWindow().getHeight() * 0.3);
            differenceRow = (int) (client.getWindow().getHeight() * 0.066666667)
                    + (int) ((double) (300 - screen.height) / 10);
            maxRow = minRow + (2 * differenceRow);

            windowPosX = client.getWindow().getX();
            windowPosY = client.getWindow().getY();

            hudScreenOffsetX = (int) ((double) (480 - screen.width) / 4);
            hudScreenOffsetY = (int) ((double) (300 - screen.height) / 3);
            Robot robot;
            robot = new Robot();
            currentColumn = PolyAccessibility.currentColumn;
            currentRow = PolyAccessibility.currentRow;

            // S Pressed :- Down Row
            if (PolyAccessibility.isSPressed && !PolyAccessibility.delayThreadMap.containsKey("enchanting_table_slot")) {
                if (currentColumn == 0 && currentRow == 0) {
                    currentColumn = minColumn;
                    currentRow = minRow;
                } else if (currentRow >= maxRow - 5 && currentRow <= maxRow + 5) {
                    currentRow += 0;
                } else {
                    currentRow += differenceRow;
                }
                robot.mouseMove(currentColumn + windowPosX - hudScreenOffsetX, currentRow + windowPosY - hudScreenOffsetY);
                PolyAccessibility.delayThreadMap.put("enchanting_table_slot", 200);
            }

            // W Pressed :- Up Row
            if (PolyAccessibility.isWPressed && !PolyAccessibility.delayThreadMap.containsKey("enchanting_table_slot")) {
                if (currentColumn == 0 && currentRow == 0) {
                    currentColumn = minColumn;
                    currentRow = minRow;
                } else if (currentRow >= minRow - 5 && currentRow <= minRow + 5) {
                    currentRow -= 0;
                } else {
                    currentRow -= differenceRow;
                }
                robot.mouseMove(currentColumn + windowPosX - hudScreenOffsetX, currentRow + windowPosY - hudScreenOffsetY);
                PolyAccessibility.delayThreadMap.put("enchanting_table_slot", 200);
            }

            PolyAccessibility.currentColumn = currentColumn;
            PolyAccessibility.currentRow = currentRow;

        } catch (AWTException e) {
            e.printStackTrace();
        }
    }
}
