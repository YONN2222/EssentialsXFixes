package com.yonn2222.essentialsxfixes.route;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RulesTest {
    private static final Facts NONE = new Facts(false, false, false);
    private static final Facts PLAYERS = new Facts(true, false, false);
    private static final Facts ENTITIES = new Facts(true, true, false);

    @Test
    void teleportToCoordinatesUsesVanilla() {
        assertEquals(Route.EXTENDED, Rules.decide(Twin.TP, List.of("@s", "100", "100", "100"), PLAYERS));
        assertEquals(Route.EXTENDED, Rules.decide(Twin.TP, List.of("~", "~1", "~"), NONE));
        assertEquals(Route.EXTENDED, Rules.decide(Twin.TP, List.of("@s", "^", "^", "^5"), PLAYERS));
    }

    @Test
    void teleportBetweenPlayersStaysWithEssentials() {
        assertEquals(Route.ESSENTIALS, Rules.decide(Twin.TP, List.of("Steve"), NONE));
        assertEquals(Route.ESSENTIALS, Rules.decide(Twin.TP, List.of("@a", "@p"), PLAYERS));
    }

    @Test
    void teleportOfEntitiesUsesVanilla() {
        assertEquals(Route.EXTENDED, Rules.decide(Twin.TP, List.of("@e[type=cow]", "Steve"), ENTITIES));
    }

    @Test
    void killWithoutArgumentsOrWithEntitiesUsesVanilla() {
        assertEquals(Route.EXTENDED, Rules.decide(Twin.KILL, List.of(), NONE));
        assertEquals(Route.EXTENDED, Rules.decide(Twin.KILL, List.of("@e[type=zombie]"), ENTITIES));
        assertEquals(Route.ESSENTIALS, Rules.decide(Twin.KILL, List.of("Steve"), NONE));
    }

    @Test
    void clearWithTagsOrComponentsUsesVanilla() {
        assertEquals(Route.EXTENDED, Rules.decide(Twin.CLEAR, List.of("@s", "#minecraft:logs"), PLAYERS));
        assertEquals(Route.EXTENDED, Rules.decide(Twin.CLEAR, List.of("@s", "stone", "0"), PLAYERS));
        assertEquals(Route.ESSENTIALS, Rules.decide(Twin.CLEAR, List.of("Steve", "stone"), NONE));
    }

    @Test
    void enchantWithATargetFirstUsesVanilla() {
        assertEquals(Route.EXTENDED, Rules.decide(Twin.ENCHANT, List.of("@s", "sharpness"), PLAYERS));
        assertEquals(Route.ESSENTIALS, Rules.decide(Twin.ENCHANT, List.of("sharpness", "5"), NONE));
    }

    @Test
    void experienceAddAndQueryUseVanilla() {
        assertEquals(Route.EXTENDED, Rules.decide(Twin.XP, List.of("add", "@s", "5", "levels"), PLAYERS));
        assertEquals(Route.EXTENDED, Rules.decide(Twin.XP, List.of("query", "@s", "points"), PLAYERS));
        assertEquals(Route.ESSENTIALS, Rules.decide(Twin.XP, List.of("set", "Steve", "10"), NONE));
        assertEquals(Route.EXTENDED, Rules.decide(Twin.XP, List.of("set", "@s", "10", "levels"), PLAYERS));
    }

    @Test
    void timeAndWeatherVanillaForms() {
        assertEquals(Route.EXTENDED, Rules.decide(Twin.TIME, List.of("query", "daytime"), NONE));
        assertEquals(Route.EXTENDED, Rules.decide(Twin.TIME, List.of("add", "1d"), NONE));
        assertEquals(Route.EXTENDED, Rules.decide(Twin.TIME, List.of("set", "noon"), NONE));
        assertEquals(Route.ESSENTIALS, Rules.decide(Twin.TIME, List.of("set", "6000"), NONE));
        assertEquals(Route.ESSENTIALS, Rules.decide(Twin.TIME, List.of(), NONE));
        assertEquals(Route.EXTENDED, Rules.decide(Twin.WEATHER, List.of("clear"), NONE));
        assertEquals(Route.EXTENDED, Rules.decide(Twin.WEATHER, List.of("thunder", "10m"), NONE));
        assertEquals(Route.ESSENTIALS, Rules.decide(Twin.WEATHER, List.of("storm"), NONE));
    }

    @Test
    void giveWithComponentsUsesVanilla() {
        assertEquals(Route.EXTENDED, Rules.decide(Twin.GIVE, List.of("@s", "diamond_sword[enchantments={sharpness:5}]"), PLAYERS));
        assertEquals(Route.ESSENTIALS, Rules.decide(Twin.GIVE, List.of("Steve", "diamond", "3"), NONE));
    }

    @Test
    void itemRecipeAndListSubcommands() {
        assertEquals(Route.EXTENDED, Rules.decide(Twin.ITEM, List.of("replace", "entity", "@s", "weapon.mainhand", "with", "stone"), PLAYERS));
        assertEquals(Route.ESSENTIALS, Rules.decide(Twin.ITEM, List.of("stone", "64"), NONE));
        assertEquals(Route.EXTENDED, Rules.decide(Twin.RECIPE, List.of("give", "@s", "*"), PLAYERS));
        assertEquals(Route.ESSENTIALS, Rules.decide(Twin.RECIPE, List.of("stone"), NONE));
        assertEquals(Route.EXTENDED, Rules.decide(Twin.LIST, List.of("uuids"), NONE));
        assertEquals(Route.ESSENTIALS, Rules.decide(Twin.LIST, List.of(), NONE));
    }

    @Test
    void labelsMapToTheirVanillaTwin() {
        assertEquals(Twin.XP, Twin.of("xp").orElseThrow());
        assertEquals(Twin.TP, Twin.of("TELEPORT").orElseThrow());
        assertTrue(Twin.of("gmc").isEmpty());
    }

    @Test
    void tokensKeepSelectorsWithSpacesTogether() {
        assertEquals(List.of("@e[type=zombie, limit=2]", "Steve"), Tokens.split("@e[type=zombie, limit=2] Steve"));
        assertEquals(List.of("a", "b"), Tokens.split("  a   b "));
    }

    @Test
    void selectorAndCoordinateDetection() {
        assertTrue(Tokens.selector("@a"));
        assertTrue(Tokens.selector("@e[type=cow]"));
        assertFalse(Tokens.selector("@everyone"));
        assertFalse(Tokens.selector("Steve"));
        assertTrue(Tokens.coordinate("~"));
        assertTrue(Tokens.coordinate("-12.5"));
        assertTrue(Tokens.coordinate("^3"));
        assertFalse(Tokens.coordinate("Steve"));
        assertFalse(Tokens.coordinate(""));
    }
}
