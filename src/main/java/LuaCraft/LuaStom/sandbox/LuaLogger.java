package LuaCraft.LuaStom.sandbox;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import LuaCraft.LuaStom.LuaErrorAssert;

public class LuaLogger extends LuaTable {
    private static final Logger logger = LoggerFactory.getLogger("LuaCraft ScriptLog");

    public LuaLogger() {
        rawset("Log", new VarArgFunction() {
            @Override
            public LuaValue invoke(Varargs args) {
                int argCount = args.narg();

                String logMessage;
                String logSeverity;

                if (argCount == 2) {
                    logSeverity = "Info";
                    logMessage = LuaErrorAssert.checkString(args.arg(2), "Logger.Log", 1);
                } else {
                    logSeverity = LuaErrorAssert.checkString(args.arg(2), "Logger.Log", 1);
                    logMessage = LuaErrorAssert.checkString(args.arg(3), "Logger.Log", 2);
                }

                switch (logSeverity) {
                    case "Info": 
                        logger.info(logMessage); 
                        break;
                    case "Warn": 
                        logger.warn(logMessage);
                        break;
                    case "Error": 
                        logger.error(logMessage);
                        break;
                    default: 
                        logger.info(logMessage);
                        break;
                }

                return LuaValue.NIL;
            }
        });
    }
}