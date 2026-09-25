package net.mat0u5.matlib.utils.other;

import net.mat0u5.matlib.MatLib;
import net.minecraft.resources.Identifier;

public class IdentifierHelper {
    /**
     * Returns an identifier with the given {@code namespace} and {@code path}.
     */
    public static Identifier of(String namespace, String path) {
        //? if <= 1.20.5 {
        /*return new Identifier(namespace, path);
        *///?} else {
        return Identifier.fromNamespaceAndPath(namespace, path);
        //?}
    }

    /**
     * Returns an identifier with the {@code matlib} namespace.
     */
    public static Identifier matlib(String path) {
        //? if <= 1.20.5 {
        /*return new Identifier(MatLib.MOD_ID, path);
        *///?} else {
        return Identifier.fromNamespaceAndPath(MatLib.MOD_ID, path);
        //?}
    }

    /**
     * Returns an identifier with the vanilla namespace.
     */
    public static Identifier vanilla(String path) {
        //? if <= 1.20.5 {
        /*return new Identifier("minecraft", path);
        *///?} else {
        return Identifier.withDefaultNamespace(path);
        //?}
    }

    /**
     * @param string Example: {@code minecraft:stone}
     */
    public static Identifier parse(String string) {
        //? if <= 1.20.5 {
        /*return new Identifier(string);
        *///?} else {
        return Identifier.parse(string);
        //?}
    }
}
