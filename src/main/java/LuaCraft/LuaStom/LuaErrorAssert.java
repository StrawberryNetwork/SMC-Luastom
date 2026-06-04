package LuaCraft.LuaStom;

import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

public class LuaErrorAssert {

    public static String makeItActuallyPretty(String rawError) {
        if (rawError == null) return "Unknown error";

        String clean = rawError.replaceAll("\\[string \"(.+?)\"\\]:", "$1:");

        String[] parts = clean.split(":", 3);

        if (parts.length >= 3) {
            String file = parts[0].trim();
            String line = parts[1].trim();
            String message = parts[2].trim();
            message = message.replaceAll("\\(to close '(.*)' at line (\\d+)\\)", "to close '$1' on line $2");

            return file + " on line " + line + ": " + message;
        }

        return clean;
    }

    public static boolean checkBoolean(LuaValue value, String funcName, int argNum) {
        if (!value.isboolean()) {
            throw new LuaError("[LuaCraft] Bad Argument #" + argNum + " to '" + funcName + "' Boolean expected, got " + value.typename());
        }

        return value.toboolean();
    }

    public static String checkString(LuaValue value, String funcName, int argNum) {
        if (!value.isstring()) {
            throw new LuaError("[LuaCraft] Bad Argument #" + argNum + " to '" + funcName + "' String expected, got " + value.typename());
        }

        return value.tojstring();
    }

    public static int checkInt(LuaValue value, String funcName, int argNum) {
        if (!value.isnumber()) {
            throw new LuaError("[LuaCraft] Bad Argument #" + argNum + " to '" + funcName + "' Number expected, got " + value.typename());
        }

        return value.toint();
    }

    public static double checkDouble(LuaValue value, String funcName, int argNum) {
        if (!value.isnumber()) {
            throw new LuaError("[LuaCraft] Bad Argument #" + argNum + " to '" + funcName + "' Number expected, got " + value.typename());
        }

        return value.todouble();
    }

    public static float checkFloat(LuaValue value, String funcName, int argNum) {
        if (!value.isnumber()) {
            throw new LuaError("[LuaCraft] Bad Argument #" + argNum + " to '" + funcName + "' Number expected, got " + value.typename());
        }

        return value.tofloat();
    }

    public static long checkLong(LuaValue value, String funcName, int argNum) {
        if (!value.isnumber()) {
            throw new LuaError("[LuaCraft] Bad Argument #" + argNum + " to '" + funcName + "' Number expected, got " + value.typename());
        }

        return value.tolong();
    }

    public static LuaTable checkTable(LuaValue value, String funcName, int argNum) {
        if (!value.istable()) {
            throw new LuaError("[LuaCraft] Bad Argument #" + argNum + " to '" + funcName + "' Table expected, got " + value.typename());
        }

        return (LuaTable) value;
    }

    public static LuaFunction checkFunction(LuaValue value, String funcName, int argNum) {
        if (!value.isfunction()) {
            throw new LuaError("[LuaCraft] Bad Argument #" + argNum + " to '" + funcName + "' Function expected, got " + value.typename());
        }

        return (LuaFunction) value;
    }
}