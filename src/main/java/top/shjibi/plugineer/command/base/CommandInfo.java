package top.shjibi.plugineer.command.base;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CommandInfo {
    String name();
    int minArgs() default 0;
    String[] usage() default {};
    String[] playerOnlyMsg() default "&c该指令只能由玩家执行";
}
