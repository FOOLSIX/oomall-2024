package cn.edu.xmu.javaee.core.aop;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.SOURCE)
public @interface CopyTo {
    Class<?>[] value();
    @Target({ ElementType.METHOD,ElementType.FIELD  })
    @Retention(RetentionPolicy.SOURCE)
    public @interface Exclude {
        Class<?>[] value();
    }
    @Target({ ElementType.METHOD,ElementType.FIELD })
    @Retention(RetentionPolicy.SOURCE)
    public @interface Of {
        Class<?>[] value();
    }
}
